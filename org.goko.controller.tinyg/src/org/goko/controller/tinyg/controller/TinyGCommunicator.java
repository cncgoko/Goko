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

import org.apache.commons.lang3.StringUtils;
import org.goko.controller.tinyg.commons.AbstractTinyGCommunicator;
import org.goko.controller.tinyg.commons.ITinyGStatus;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.EnumCoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * @author Psyko
 * @date 8 janv. 2017
 */
public class TinyGCommunicator extends AbstractTinyGCommunicator {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGCommunicator.class);
	/** The target TinyG service */
	private TinyGControllerService tinyg;
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
		setEndLineCharacters('\n');				
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#onConnected()
	 */
	@Override
	protected void onConnected() throws GkException {
		getIncomingBuffer().clear();
		getConnectionService().addInputDataListener(this);
		// Force strict JSon mode
		send(GkUtils.toBytesList("{\"js\":1}"), true);			
		tinyg.refreshStatus();
		tinyg.refreshConfiguration();
		updateCoordinateSystem();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#onDisconnected()
	 */
	@Override
	protected void onDisconnected() throws GkException {
		getConnectionService().removeInputDataListener(this);
		tinyg.setState(MachineState.UNDEFINED);
		tinyg.setConfiguration(new TinyGConfiguration());
		getIncomingBuffer().clear();		
	}
	/**
	 * Verify the response using the footer
	 * @param jsonFooter the parsed response
	 * @throws GkException GkException
	 */
	protected ITinyGStatus getResponseFooter(JsonValue jsonFooter) throws GkException {
		JsonArray footerArray = jsonFooter.asArray();
		int statusCodeIntValue = footerArray.get(TinyGv097.FOOTER_STATUS_CODE_INDEX).asInt();
		ITinyGStatus status = TinyGStatusCode.findEnum(statusCodeIntValue);
		return status;
	}
	
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleResponse(com.eclipsesource.json.JsonObject, org.goko.controller.tinyg.commons.ITinyGStatus)
	 */
	@Override
	protected void handleResponse(JsonObject responseEnvelope, ITinyGStatus status) throws GkException {
		for(String name : responseEnvelope.names()){

			if(StringUtils.equals(name, TinyGv097.GCODE_COMMAND)){				
				handleGCodeResponse(responseEnvelope.get(TinyGv097.GCODE_COMMAND), status);

			}else if(StringUtils.equals(name, TinyGv097.STATUS_REPORT)){
				handleStatusReport((JsonObject)responseEnvelope.get(TinyGv097.STATUS_REPORT));
			
			}else if(StringUtils.equals(name, TinyGv097.QUEUE_REPORT)){
				handleQueueReport(responseEnvelope.get(TinyGv097.QUEUE_REPORT));

			}else if(StringUtils.equals(name, TinyGv097.LINE_REPORT)){
		//		LOG.info("Skipping line report "+String.valueOf(responseEnvelope.get(name)));

			}else if(StringUtils.equals(name, TinyGv097.PROBE_REPORT)){
				handleProbeReport(responseEnvelope.get(TinyGv097.PROBE_REPORT));
			
			}else if(StringUtils.equals(name, TinyGv097.MESSAGE_REPORT)){
				handleMessage(responseEnvelope.get(TinyGv097.MESSAGE_REPORT));
				
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

	private void handleGCodeResponse(JsonValue jsonValue, ITinyGStatus status) throws GkException {
		String 	receivedCommand = jsonValue.asString();
		tinyg.handleGCodeResponse(receivedCommand, status);
	}

	protected void handleQueueReport(JsonValue queueReport) throws GkException {
		tinyg.setAvailableBuffer(queueReport.asInt());
	}

	private void handleMessage(JsonValue message) throws GkException {
		applicativeLogService.warning(message.asString(), "TinyG");		
	}
	
	private void handleProbeReport(JsonValue probeReport) throws GkException {
		if(probeReport.isObject()){
			Tuple6b 	probePosition 		= null;
			JsonObject 	probeReportObject 	= (JsonObject) probeReport;
			JsonValue 	eProbeResult 		= probeReportObject.get(TinyGv097.PROBE_REPORT_SUCCESS);
			boolean 	probeSuccess 		= (eProbeResult.asInt() == 1);
			if(probeSuccess){
				JsonValue xProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_X);
				JsonValue yProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_Y);
				JsonValue zProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_Z);
				JsonValue aProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_A);
				JsonValue bProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_B);
				JsonValue cProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_C);
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
	
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleStatusReport(com.eclipsesource.json.JsonObject)
	 */
	@Override
	protected void handleStatusReport(JsonObject statusReport) throws GkException {
		if(statusReport.isObject()){
			JsonObject 	statusReportObject = (JsonObject) statusReport;

			EnumUnit 		 units 			= findUnits(statusReportObject);
			Tuple6b 		 workPosition 	= findWorkPosition(statusReportObject, units);
			MachineState 	 state 			= findState(statusReportObject);
			EnumDistanceMode distanceMode 	= findDistanceMode(statusReportObject);			

			Speed 		 	 velocity 		= findVelocity(statusReportObject, units);
			Speed 			 feedrate 		= findFeedrate(statusReportObject, units);
			EnumCoordinateSystem cs 		= findCoordinateSystem(statusReportObject);

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
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleErrorReport(com.eclipsesource.json.JsonObject)
	 */
	@Override
	protected void handleErrorReport(JsonObject errorReportBody) throws GkException {
				
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleMalformedJson(java.lang.String)
	 */
	@Override
	protected void handleMalformedJson(String data) {
		
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleNonJsonData(java.lang.String)
	 */
	@Override
	protected void handleNonJsonData(String data) throws GkException {
		tinyg.refreshStatus();
	}

	
	/**
	 * Extract state from status report
	 * @param statusReport
	 * @return
	 */
	private MachineState findState(JsonObject statusReport){
		JsonValue statReport = statusReport.get(TinyGv097.STATUS_REPORT_STATE);
		if(statReport != null){
			return TinyGControllerUtility.getState(statReport.asInt());
		}
		return null;
	}


	protected void updateCoordinateSystem() throws GkException{
		send(GkUtils.toBytesList("{\"G55\":\"\"}"), true);
		send(GkUtils.toBytesList("{\"G56\":\"\"}"), true);
		send(GkUtils.toBytesList("{\"G57\":\"\"}"), true);
		send(GkUtils.toBytesList("{\"G58\":\"\"}"), true);
		send(GkUtils.toBytesList("{\"G59\":\"\"}"), true);
	}
	
	protected void updateCoordinateSystem(ICoordinateSystem cs) throws GkException{
		send(GkUtils.toBytesList("{\""+cs.getCode()+"\":\"\"}"), true);		
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
