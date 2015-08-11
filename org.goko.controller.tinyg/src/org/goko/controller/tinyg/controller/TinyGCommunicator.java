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
import org.goko.core.common.applicative.logging.ApplicativeLogEvent;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandDistanceMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandUnit;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;

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
	private IGCodeService gcodeService;

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
		//Dépiler dans un thread ?
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
			tinyg.refreshStatus();
			tinyg.refreshConfiguration();
			updateCoordinateSystem();			
		}else if(event == EnumConnectionEvent.DISCONNECTED){
			getConnectionService().removeInputDataListener(this);
			tinyg.setState(MachineState.UNDEFINED);
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
					LOG.error("Error while parsing JSon for string '"+trimmedData+"'"+System.lineSeparator()+e.getMessage());
					return;
				}

				JsonValue footerBody = response.get(TinyGJsonUtils.FOOTER);
				if(footerBody != null){
					handleResponseFooter(footerBody);
				}

				JsonValue responseBody = response.get(TinyGJsonUtils.RESPONSE_ENVELOPE);
				if(responseBody != null){
					handleResponseEnvelope((JsonObject) responseBody);
				}
				JsonValue statusReport = response.get(TinyGJsonUtils.STATUS_REPORT);
				if(statusReport != null){
					handleStatusReport(statusReport);
				}
				JsonValue queueReport = response.get(TinyGJsonUtils.QUEUE_REPORT);
				if(queueReport != null){
					handleQueueReport(queueReport);
				}
			}
		}
	}

	/**
	 * Verify the response using the footer
	 * @param jsonFooter the parsed response
	 * @throws GkException GkException
	 */
	private void handleResponseFooter(JsonValue jsonFooter) throws GkException {

		JsonArray footerArray = jsonFooter.asArray();
		int statusCodeIntValue = footerArray.get(TinyGJsonUtils.FOOTER_STATUS_CODE_INDEX).asInt();
		TinyGStatusCode status = TinyGStatusCode.findEnum(statusCodeIntValue);

		if(status == TinyGStatusCode.TG_OK){
		}else{
			if(status == null){
				error(" Unknown error status "+statusCodeIntValue);
			}else{
				error(" Error status returned : "+status.getValue() +" - "+status.getLabel());
			}

		}
	}

	/**
	 * Handle a JSon response envelope
	 * @param jsonValue
	 */
	private void handleResponseEnvelope(JsonObject responseEnvelope) throws GkException {
		for(String name : responseEnvelope.names()){

			if(StringUtils.equals(name, TinyGJsonUtils.GCODE_COMMAND)){
				handleGCodeResponse(responseEnvelope.get(TinyGJsonUtils.GCODE_COMMAND));

			}else if(StringUtils.equals(name, TinyGJsonUtils.STATUS_REPORT)){
				handleStatusReport(responseEnvelope.get(TinyGJsonUtils.STATUS_REPORT));

			}else if(StringUtils.equals(name, TinyGJsonUtils.FOOTER)){
				handleResponseFooter(responseEnvelope.get(TinyGJsonUtils.FOOTER));

			}else if(StringUtils.equals(name, TinyGJsonUtils.QUEUE_REPORT)){
				handleQueueReport(responseEnvelope.get(TinyGJsonUtils.QUEUE_REPORT));

			}else if(StringUtils.equals(name, TinyGJsonUtils.LINE_REPORT)){
		//		LOG.info("Skipping line report "+String.valueOf(responseEnvelope.get(name)));

			}else if(StringUtils.equals(name, TinyGJsonUtils.PROBE_REPORT)){
				handleProbeReport(responseEnvelope.get(TinyGJsonUtils.PROBE_REPORT));

			}else if(StringUtils.defaultString(name).matches("(g|G)5(4|5|6|7|8|9)")){
				handleCoordinateSystemOffsetReport(name, responseEnvelope.get(name));
			}else{
				handleConfigurationModification(responseEnvelope);
			}
		}
	}

	private void handleCoordinateSystemOffsetReport(String offsetName, JsonValue jsonOffset) throws GkException{
		EnumCoordinateSystem cs = EnumCoordinateSystem.valueOf(StringUtils.upperCase(offsetName));
		JsonObject offsetObj = (JsonObject) jsonOffset;
		JsonValue xOffset = offsetObj.get("x");
		JsonValue yOffset = offsetObj.get("y");
		JsonValue zOffset = offsetObj.get("z");
		JsonValue aOffset = offsetObj.get("a");
		Tuple6b offset = new Tuple6b().setZero();
		offset.setX( NumberQuantity.of(xOffset.asBigDecimal(), tinyg.getCurrentUnit() ));
		offset.setY( NumberQuantity.of(yOffset.asBigDecimal(), tinyg.getCurrentUnit() ) );
		offset.setZ( NumberQuantity.of(zOffset.asBigDecimal(), tinyg.getCurrentUnit() ) );
		if(aOffset != null){
			offset.setA(  NumberQuantity.of(aOffset.asBigDecimal(), SI.DEGREE_ANGLE )  );
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

	private void handleGCodeResponse(JsonValue jsonValue) throws GkException {
		String 	receivedCommand = jsonValue.asString();
		tinyg.handleGCodeResponse(receivedCommand);
	}

	private void handleQueueReport(JsonValue queueReport) throws GkException {
		tinyg.setAvailableBuffer(queueReport.asInt());
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
					probePosition.setX( NumberQuantity.of(xProbeResult.asBigDecimal(), tinyg.getCurrentUnit()) );
				}
				if(yProbeResult != null){
					probePosition.setY( NumberQuantity.of(yProbeResult.asBigDecimal(), tinyg.getCurrentUnit()) );
				}
				if(zProbeResult != null){
					probePosition.setZ( NumberQuantity.of(zProbeResult.asBigDecimal(), tinyg.getCurrentUnit()) );
				}
				if(aProbeResult != null){
					probePosition.setA( NumberQuantity.of(aProbeResult.asBigDecimal(), SI.DEGREE_ANGLE) );
				}
				if(bProbeResult != null){
					probePosition.setB( NumberQuantity.of(bProbeResult.asBigDecimal(), SI.DEGREE_ANGLE) );
				}
				if(cProbeResult != null){
					probePosition.setC( NumberQuantity.of(cProbeResult.asBigDecimal(), SI.DEGREE_ANGLE) );
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

			Tuple6b 					 workPosition 	= findWorkPosition(statusReportObject);
			MachineState 				 state 			= findState(statusReportObject);
			EnumGCodeCommandDistanceMode distanceMode 	= findDistanceMode(statusReportObject);
			EnumGCodeCommandUnit 		 units 			= findUnits(statusReportObject);
			BigDecimal 					 velocity 		= findVelocity(statusReportObject);
			BigDecimal 					 feedrate 		= findFeedrate(statusReportObject);
			EnumCoordinateSystem 		 cs 			= findCoordinateSystem(statusReportObject);
			GCodeContext gcodeContext = new GCodeContext(tinyg.getCurrentGCodeContext());

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

	private BigDecimal findVelocity(JsonObject statusReport){
		JsonValue velocityReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_VELOCITY);
		if(velocityReport != null){
			return velocityReport.asBigDecimal().setScale(3, BigDecimal.ROUND_HALF_EVEN);
		}
		return null;
	}

	private BigDecimal findFeedrate(JsonObject feedrate){
		JsonValue feedrateReport = feedrate.get(TinyGJsonUtils.STATUS_REPORT_FEEDRATE);
		if(feedrateReport != null){
			return feedrateReport.asBigDecimal().setScale(3, BigDecimal.ROUND_HALF_EVEN);
		}
		return null;
	}

	private Tuple6b findWorkPosition(JsonObject statusReport) throws GkException{
		Tuple6b 	workPosition = tinyg.getCurrentGCodeContext().getPosition();
		workPosition = TinyGControllerUtility.updatePosition(workPosition, statusReport);
		return workPosition;
	}
	private EnumGCodeCommandDistanceMode findDistanceMode(JsonObject statusReport){
		JsonValue distReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_DISTANCE_MODE);
		if(distReport != null){
			int dist = distReport.asInt();
			if(dist == 0){
				return EnumGCodeCommandDistanceMode.ABSOLUTE;
			}else{
				return EnumGCodeCommandDistanceMode.RELATIVE;
			}
		}
		return null;
	}

	/**
	 * Finds the units declaration in the status report
	 * @param statusReport the status report
	 * @return {@link EnumGCodeCommandUnit}
	 */
	private EnumGCodeCommandUnit findUnits(JsonObject statusReport){
		JsonValue unitReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_UNITS);
		if(unitReport != null){
			int units = unitReport.asInt();
			if(units == 1){
				return EnumGCodeCommandUnit.MILLIMETERS;
			}else{
				return EnumGCodeCommandUnit.INCHES;
			}
		}
		return null;
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
	private EnumCoordinateSystem findCoordinateSystem(JsonObject statusReport){
		EnumCoordinateSystem coordinateSystem = null;
		JsonValue coordReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_COORDINATES);
		if(coordReport != null){
			int units = coordReport.asInt();
			switch(units){
			case 0: coordinateSystem = EnumCoordinateSystem.G53;
			break;
			case 1: coordinateSystem = EnumCoordinateSystem.G54;
			break;
			case 2: coordinateSystem = EnumCoordinateSystem.G55;
			break;
			case 3: coordinateSystem = EnumCoordinateSystem.G56;
			break;
			case 4: coordinateSystem = EnumCoordinateSystem.G57;
			break;
			case 5: coordinateSystem = EnumCoordinateSystem.G58;
			break;
			case 6: coordinateSystem = EnumCoordinateSystem.G59;
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
	
	protected void updateCoordinateSystem(EnumCoordinateSystem cs) throws GkException{
		send(GkUtils.toBytesList("{\""+cs.name()+"\":\"\"}"));		
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

	protected void error(String message){
		LOG.error(message);
		applicativeLogService.log(ApplicativeLogEvent.LOG_ERROR, message, "TinyG Communicator");
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
		//command.add(new Byte((byte) endLineCharDelimiter));
		list.add(new Byte((byte) endLineCharDelimiter));
	}

	protected void send(GCodeCommand gCodeCommand) throws GkException{
		JsonValue jsonStr = TinyGControllerUtility.toJson(new String(gcodeService.convert(gCodeCommand)));
		send(GkUtils.toBytesList(jsonStr.toString()));
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
	public IGCodeService getGcodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGcodeService(IGCodeService gcodeService) {
		this.gcodeService = gcodeService;
	}
}
