/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.controller.tinyg.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.controller.tinyg.json.TinyGJsonUtils;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystemFactory;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class TinyGCommunicator implements IConnectionDataListener, IConnectionListener {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGCommunicator.class);
	/** The target TinyG service */
	private TinyGControllerService tinyg;
	/** TinyG end line delimiter */
	private char endLineCharDelimiter;
	/** Buffer for incoming data	 */
	private ByteCommandBuffer incomingBuffer;
	/** The connection service */
	private IConnectionService connectionService;
	/** The applicative log service */
	private IApplicativeLogService applicativeLogService;
	/** GCode service */
	private IRS274NGCService gcodeService;

	/**
	 * Constructor
	 * @param tinyg the tinyG service
	 */
	public TinyGCommunicator(TinyGControllerService tinyg) {
		this.tinyg = tinyg;
		endLineCharDelimiter = '\n';
		incomingBuffer 		 = new ByteCommandBuffer((byte) endLineCharDelimiter);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataReceived(java.util.List)
	 */
	@Override
	public void onDataReceived(List<Byte> data) throws GkException {
		incomingBuffer.addAll(data);
		//D�piler dans un thread ?
//		if(incomingBuffer.hasNext()){
//			executor.execute(new Runnable() {
//				@Override
//				public void run() {
					while(incomingBuffer.hasNext()){
						try {
							handleIncomingData(GkUtils.toString(incomingBuffer.unstackNextCommand()));
						} catch (GkException e) {
							LOG.error(e);
						}
					}
//				}
//			});
//		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataSent(java.util.List)
	 */
	@Override
	public void onDataSent(List<Byte> data) throws GkException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionEvent(EnumConnectionEvent event) throws GkException {
		if(event == EnumConnectionEvent.CONNECTED){
			incomingBuffer.clear();
			getConnectionService().addInputDataListener(this);
			// Force strict GCode mode
			send(GkUtils.toBytesList("{\"js\":1}"));			
			tinyg.refreshStatus();
			tinyg.refreshConfiguration();
			updateCoordinateSystem();			
		}else if(event == EnumConnectionEvent.DISCONNECTED){
			getConnectionService().removeInputDataListener(this);
			tinyg.setState(MachineState.UNDEFINED);
			tinyg.setConfiguration(new TinyGConfiguration());
			incomingBuffer.clear();
		}		
	}

	private void handleIncomingData(String data) throws GkException {
		String trimmedData = StringUtils.trim(data);
		if(StringUtils.isNotEmpty(trimmedData)){
			if(TinyGJsonUtils.isJsonFormat(trimmedData)){
				JsonObject response = null;
				try{
					response = JsonObject.readFrom(trimmedData);
				}catch(Exception e){
					String msg = "Error while parsing JSon for string '"+trimmedData+"'"+System.lineSeparator()+e.getMessage();
					applicativeLogService.error(msg, "Goko");
					LOG.error(msg);					
					return;
				}

				JsonValue footerBody = response.get(TinyGJsonUtils.FOOTER);
				TinyGStatusCode status = null;				
				
				if(footerBody != null){
					status = getResponseFooter(footerBody);
				}

				JsonValue responseBody = response.get(TinyGJsonUtils.RESPONSE_ENVELOPE);
				if(responseBody != null){
					handleResponseEnvelope((JsonObject) responseBody, status);
				}
				JsonValue statusReport = response.get(TinyGJsonUtils.STATUS_REPORT);
				if(statusReport != null){
					handleStatusReport(statusReport);
				}
				JsonValue queueReport = response.get(TinyGJsonUtils.QUEUE_REPORT);
				if(queueReport != null){
					handleQueueReport(queueReport);
				}
			}else{
				// Not Json, we force a JSon command to make sure TinyG uses JSon
				tinyg.refreshStatus();
			}
		}
	}

	/**
	 * Verify the response using the footer
	 * @param jsonFooter the parsed response
	 * @throws GkException GkException
	 */
	private TinyGStatusCode getResponseFooter(JsonValue jsonFooter) throws GkException {
		JsonArray footerArray = jsonFooter.asArray();
		int statusCodeIntValue = footerArray.get(TinyGJsonUtils.FOOTER_STATUS_CODE_INDEX).asInt();
		TinyGStatusCode status = TinyGStatusCode.findEnum(statusCodeIntValue);
		return status;
	}
	
	/**
	 * Handle a JSon response envelope
	 * @param jsonValue
	 */
	private void handleResponseEnvelope(JsonObject responseEnvelope, TinyGStatusCode status) throws GkException {
		for(String name : responseEnvelope.names()){

			if(StringUtils.equals(name, TinyGJsonUtils.GCODE_COMMAND)){				
				handleGCodeResponse(responseEnvelope.get(TinyGJsonUtils.GCODE_COMMAND), status);

			}else if(StringUtils.equals(name, TinyGJsonUtils.STATUS_REPORT)){
				handleStatusReport(responseEnvelope.get(TinyGJsonUtils.STATUS_REPORT));
			
			}else if(StringUtils.equals(name, TinyGJsonUtils.QUEUE_REPORT)){
				handleQueueReport(responseEnvelope.get(TinyGJsonUtils.QUEUE_REPORT));

			}else if(StringUtils.equals(name, TinyGJsonUtils.LINE_REPORT)){
		//		LOG.info("Skipping line report "+String.valueOf(responseEnvelope.get(name)));

			}else if(StringUtils.equals(name, TinyGJsonUtils.PROBE_REPORT)){
				handleProbeReport(responseEnvelope.get(TinyGJsonUtils.PROBE_REPORT));
			
			}else if(StringUtils.equals(name, TinyGJsonUtils.MESSAGE_REPORT)){
				handleMessage(responseEnvelope.get(TinyGJsonUtils.MESSAGE_REPORT));
				
			}else if(StringUtils.defaultString(name).matches("(g|G)5(4|5|6|7|8|9)")){
				handleCoordinateSystemOffsetReport(name, responseEnvelope.get(name));
			}else{
				handleConfigurationModification(responseEnvelope);
			}
		}
	}

	private void handleCoordinateSystemOffsetReport(String offsetName, JsonValue jsonOffset) throws GkException{
		CoordinateSystem cs = new CoordinateSystemFactory().get(StringUtils.upperCase(offsetName));
		JsonObject offsetObj = (JsonObject) jsonOffset;
		JsonValue xOffset = offsetObj.get("x");
		JsonValue yOffset = offsetObj.get("y");
		JsonValue zOffset = offsetObj.get("z");
		JsonValue aOffset = offsetObj.get("a");
		Tuple6b offset = new Tuple6b().setZero();
		offset.setX( Length.valueOf(xOffset.asBigDecimal(), tinyg.getCurrentUnit() ));
		offset.setY( Length.valueOf(yOffset.asBigDecimal(), tinyg.getCurrentUnit() ) );
		offset.setZ( Length.valueOf(zOffset.asBigDecimal(), tinyg.getCurrentUnit() ) );
		if(aOffset != null){
			offset.setA(  Angle.valueOf(aOffset.asBigDecimal(), AngleUnit.DEGREE_ANGLE )  );
		}
		tinyg.setCoordinateSystemOffset(cs, offset);
	}
	/**
	 * Handle the configuration changes received from the TinyG device
	 * @param responseEnvelope the response envelope
	 * @throws GkException GkException
	 */
	private void handleConfigurationModification(JsonObject responseEnvelope) throws GkException {
		TinyGConfiguration cfg = tinyg.getConfiguration();
		TinyGControllerUtility.handleConfigurationModification(cfg, responseEnvelope);
		tinyg.setConfiguration(cfg);
	}

	private void handleGCodeResponse(JsonValue jsonValue, TinyGStatusCode status) throws GkException {
		String 	receivedCommand = jsonValue.asString();
		tinyg.handleGCodeResponse(receivedCommand, status);
	}

	private void handleQueueReport(JsonValue queueReport) throws GkException {
		tinyg.setAvailableBuffer(queueReport.asInt());
	}

	private void handleMessage(JsonValue message) throws GkException {
		applicativeLogService.warning(message.asString(), "TinyG");		
	}
	
	private void handleProbeReport(JsonValue probeReport) throws GkException {
		if(probeReport.isObject()){
			Tuple6b 	probePosition 		= null;
			JsonObject 	probeReportObject 	= (JsonObject) probeReport;
			JsonValue 	eProbeResult 		= probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_SUCCESS);
			boolean 	probeSuccess 		= (eProbeResult.asInt() == 1);
			if(probeSuccess){
				JsonValue xProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_X);
				JsonValue yProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_Y);
				JsonValue zProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_Z);
				JsonValue aProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_A);
				JsonValue bProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_B);
				JsonValue cProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_C);
				probePosition = new Tuple6b();
				if(xProbeResult != null){
					probePosition.setX( Length.valueOf(xProbeResult.asBigDecimal(), tinyg.getCurrentUnit()) );
				}
				if(yProbeResult != null){
					probePosition.setY( Length.valueOf(yProbeResult.asBigDecimal(), tinyg.getCurrentUnit()) );
				}
				if(zProbeResult != null){
					probePosition.setZ( Length.valueOf(zProbeResult.asBigDecimal(), tinyg.getCurrentUnit()) );
				}
				if(aProbeResult != null){
					probePosition.setA( Angle.valueOf(aProbeResult.asBigDecimal(), AngleUnit.DEGREE_ANGLE) );
				}
				if(bProbeResult != null){
					probePosition.setB( Angle.valueOf(bProbeResult.asBigDecimal(), AngleUnit.DEGREE_ANGLE) );
				}
				if(cProbeResult != null){
					probePosition.setC( Angle.valueOf(cProbeResult.asBigDecimal(), AngleUnit.DEGREE_ANGLE) );
				}
			}
			tinyg.handleProbeResult(probeSuccess, probePosition);
		}
	}
	/**
	 * Handling status report from TinyG
	 * @param jsonValue
	 */
	private void handleStatusReport(JsonValue statusReport) throws GkException {
		if(statusReport.isObject()){
			JsonObject 	statusReportObject = (JsonObject) statusReport;

			EnumUnit 		 units 			= findUnits(statusReportObject);
			Tuple6b 		 workPosition 	= findWorkPosition(statusReportObject, units);
			MachineState 	 state 			= findState(statusReportObject);
			EnumDistanceMode distanceMode 	= findDistanceMode(statusReportObject);			
			Speed 		 	 velocity 		= findVelocity(statusReportObject);
			Speed 			 feedrate 		= findFeedrate(statusReportObject);
			CoordinateSystem cs 			= findCoordinateSystem(statusReportObject);
			GCodeContext gcodeContext = new GCodeContext(tinyg.getGCodeContext());

			gcodeContext.setPosition(workPosition);			
			gcodeContext.setDistanceMode(distanceMode);
			gcodeContext.setUnit(units);
			gcodeContext.setCoordinateSystem(cs);
			gcodeContext.setFeedrate(feedrate);

			if(state != null){
				tinyg.setState(state);
			}
			if(velocity != null){
				tinyg.setVelocity(velocity);
			}
			tinyg.updateCurrentGCodeContext(gcodeContext);
		}
	}

	private Speed findVelocity(JsonObject statusReport){
		JsonValue velocityReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_VELOCITY);
		if(velocityReport != null){
			Unit<Length> unit = tinyg.getCurrentUnit();
			Unit<Speed> speedUnit = SpeedUnit.MILLIMETRE_PER_MINUTE;
			if(LengthUnit.INCH.equals(unit)){
				speedUnit = SpeedUnit.INCH_PER_MINUTE;
			}
			return Speed.valueOf(velocityReport.asBigDecimal(), speedUnit);
		}
		return null;
	}

	private Speed findFeedrate(JsonObject feedrate){
		JsonValue feedrateReport = feedrate.get(TinyGJsonUtils.STATUS_REPORT_FEEDRATE);
		if(feedrateReport != null){
			Unit<Speed> unit = SpeedUnit.MILLIMETRE_PER_MINUTE;
			if(tinyg.getCurrentUnit().equals(LengthUnit.INCH)){
				unit = SpeedUnit.INCH_PER_MINUTE;	
			}
			return Speed.valueOf(feedrateReport.asBigDecimal().setScale(3, BigDecimal.ROUND_HALF_EVEN), unit);
		}
		return null;
	}
	
	/**
	 * Update the current work position using the given status report
	 * @param statusReport
	 * @return
	 * @throws GkException
	 */
	private Tuple6b findWorkPosition(JsonObject statusReport, EnumUnit unit) throws GkException{
		Tuple6b 	workPosition = tinyg.getGCodeContext().getPosition();
		workPosition = TinyGControllerUtility.updatePosition(workPosition, statusReport, unit);
		return workPosition;
	}
	
	private EnumDistanceMode findDistanceMode(JsonObject statusReport){
		JsonValue distReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_DISTANCE_MODE);
		if(distReport != null){
			int dist = distReport.asInt();
			if(dist == 0){
				return EnumDistanceMode.ABSOLUTE;
			}else{
				return EnumDistanceMode.RELATIVE;
			}
		}
		return null;
	}

	/**
	 * Finds the units declaration in the status report
	 * @param statusReport the status report
	 * @return {@link EnumGCodeCommandUnit}
	 * @throws GkException GkException 
	 */
	private EnumUnit findUnits(JsonObject statusReport) throws GkException{
		JsonValue unitReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_UNITS);
		if(unitReport != null){
			int units = unitReport.asInt();
			if(units == 1){
				return EnumUnit.MILLIMETERS;
			}else{
				return EnumUnit.INCHES;
			}
		}
		return  tinyg.getGCodeContext().getUnit();
	}

	/**
	 * Extract state from status report
	 * @param statusReport
	 * @return
	 */
	private MachineState findState(JsonObject statusReport){
		JsonValue statReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_STATE);
		if(statReport != null){
			return TinyGControllerUtility.getState(statReport.asInt());
		}
		return null;
	}
	/**
	 * Update coordinates
	 * 0=g53, 1=g54, 2=g55, 3=g56, 4=g57, 5=g58, 6=g59
	 */
	private CoordinateSystem findCoordinateSystem(JsonObject statusReport){
		CoordinateSystem coordinateSystem = null;
		JsonValue coordReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_COORDINATES);
		if(coordReport != null){
			int units = coordReport.asInt();
			switch(units){
			case 0: coordinateSystem = CoordinateSystem.G53;
			break;
			case 1: coordinateSystem = CoordinateSystem.G54;
			break;
			case 2: coordinateSystem = CoordinateSystem.G55;
			break;
			case 3: coordinateSystem = CoordinateSystem.G56;
			break;
			case 4: coordinateSystem = CoordinateSystem.G57;
			break;
			case 5: coordinateSystem = CoordinateSystem.G58;
			break;
			case 6: coordinateSystem = CoordinateSystem.G59;
			break;
			}
		}
		return coordinateSystem;
	}

	protected void updateCoordinateSystem() throws GkException{
		send(GkUtils.toBytesList("{\"G55\":\"\"}"));
		send(GkUtils.toBytesList("{\"G56\":\"\"}"));
		send(GkUtils.toBytesList("{\"G57\":\"\"}"));
		send(GkUtils.toBytesList("{\"G58\":\"\"}"));
		send(GkUtils.toBytesList("{\"G59\":\"\"}"));
	}
	
	protected void updateCoordinateSystem(ICoordinateSystem cs) throws GkException{
		send(GkUtils.toBytesList("{\""+cs.getCode()+"\":\"\"}"));		
	}
	
	/**
	 * @return the connectionService
	 */
	protected IConnectionService getConnectionService() {
		return connectionService;
	}

	/**
	 * @param connectionService the connectionService to set
	 * @throws GkException
	 */
	protected void setConnectionService(IConnectionService connectionService) throws GkException {
		this.connectionService = connectionService;
		connectionService.addConnectionListener(this);
	}

	/**
	 * @return the applicativeLogService
	 */
	protected IApplicativeLogService getApplicativeLogService() {
		return applicativeLogService;
	}

	/**
	 * @param applicativeLogService the applicativeLogService to set
	 */
	protected void setApplicativeLogService(IApplicativeLogService applicativeLogService) {
		this.applicativeLogService = applicativeLogService;
	}

	/**
	 * Add the end line character at the end of the given list
	 * @param list the list
	 */
	private void addEndLineCharacter(List<Byte> list){
		list.add(new Byte((byte) endLineCharDelimiter));
	}

	protected void send(List<Byte> lstByte) throws GkException{
		addEndLineCharacter(lstByte);
		getConnectionService().send(lstByte);
	}

	protected void sendWithoutEndLineCharacter(List<Byte> lstByte) throws GkException{
		getConnectionService().send(lstByte);
	}

	protected void sendImmediately(List<Byte> lstByte) throws GkException{
		getConnectionService().send(lstByte, DataPriority.IMPORTANT);
	}

	/**
	 * @return the gcodeService
	 */
	public IRS274NGCService getGcodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGcodeService(IRS274NGCService gcodeService) {
		this.gcodeService = gcodeService;
	}
}
