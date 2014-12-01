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
package org.goko.tinyg.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.ApplicativeLogEvent;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandDistanceMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandUnit;
import org.goko.core.log.GkLog;
import org.goko.tinyg.json.TinyGJsonUtils;

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
		while(incomingBuffer.hasNext()){
			handleIncomingData(GkUtils.toString(incomingBuffer.unstackNextCommand()));
		}
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
		}else if(event == EnumConnectionEvent.DISCONNECTED){
			getConnectionService().removeInputDataListener(this);
			incomingBuffer.clear();
		}
	}

	private void handleIncomingCommands(String data) throws GkException {
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
		if(status == null){
			if(status != TinyGStatusCode.TG_OK){
				error(" Error status returned : "+status.getValue() +" - "+status.getLabel());
			}
		}else{
			error(" Unknown error status "+statusCodeIntValue);

		}
		//TODO
		if(currentSendingRunnable != null){
			currentSendingRunnable.confirmCommand();
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

			}else{
				handleConfigurationModification(responseEnvelope);
			}
		}
	}

	private void handleConfigurationModification(JsonObject responseEnvelope) throws GkException {
		TinyGControllerUtility.handleConfigurationModification(tinyg.getConfiguration(), responseEnvelope);
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
			Tuple6b result = new Tuple6b();
			JsonObject probeReportObject = (JsonObject) probeReport;
			JsonValue xProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_X);
			JsonValue yProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_Y);
			JsonValue zProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_Z);
			JsonValue aProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_A);
			JsonValue bProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_B);
			JsonValue cProbeResult = probeReportObject.get(TinyGJsonUtils.PROBE_REPORT_POSITION_C);
			if(xProbeResult != null){
				result.setX( xProbeResult.asBigDecimal() );
			}
			if(yProbeResult != null){
				result.setY( yProbeResult.asBigDecimal() );
			}
			if(zProbeResult != null){
				result.setZ( zProbeResult.asBigDecimal() );
			}
			if(aProbeResult != null){
				result.setA( aProbeResult.asBigDecimal() );
			}
			if(bProbeResult != null){
				result.setB( bProbeResult.asBigDecimal() );
			}
			if(cProbeResult != null){
				result.setC( cProbeResult.asBigDecimal() );
			}
			this.futureProbeResult.setProbeResult(result);
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
			extractCoordinateSystem(statusReportObject);
			GCodeContext gcodeContext = tinyg.getCurrentGCodeContext();
			if(workPosition != null){
				gcodeContext.setPosition(workPosition);
			}
			if(distanceMode != null){
				gcodeContext.setDistanceMode(distanceMode);
			}
			if(units != null){
				gcodeContext.setUnit(units);
			}
			if(state != null){
				tinyg.setState(state);
			}

		}
	}

	private BigDecimal findVelocity(JsonObject statusReport){
		JsonValue velocityReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_VELOCITY);
		if(velocityReport != null){
			return velocityReport.asBigDecimal().setScale(3, BigDecimal.ROUND_HALF_EVEN);
		}
		return null;
	}
	private Tuple6b findWorkPosition(JsonObject statusReport){
		Tuple6b 	workPosition = new Tuple6b().setNull();
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
	private void extractCoordinateSystem(JsonObject statusReport){
		JsonValue coordReport = statusReport.get(TinyGJsonUtils.STATUS_REPORT_COORDINATES);
		if(coordReport != null){
			int units = coordReport.asInt();
			String coordinateSystem = StringUtils.EMPTY;
			switch(units){
			case 0: coordinateSystem = CoordinatesSystem.G53;
			break;
			case 1: coordinateSystem = CoordinatesSystem.G54;
			break;
			case 2: coordinateSystem = CoordinatesSystem.G55;
			break;
			case 3: coordinateSystem = CoordinatesSystem.G56;
			break;
			case 4: coordinateSystem = CoordinatesSystem.G57;
			break;
			case 5: coordinateSystem = CoordinatesSystem.G58;
			break;
			case 6: coordinateSystem = CoordinatesSystem.G59;
			break;
			}
			return StringUtils.upperCase(coordinateSystem);
		}
	}
	/**
	 * @return the connectionService
	 */
	protected IConnectionService getConnectionService() {
		return connectionService;
	}

	/**
	 * @param connectionService the connectionService to set
	 */
	protected void setConnectionService(IConnectionService connectionService) {
		this.connectionService = connectionService;
	}

	protected void error(String message){
		LOG.error(message);
		applicativeLogService.log(ApplicativeLogEvent.LOG_ERROR, message, tinyg.SERVICE_ID);
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
}
