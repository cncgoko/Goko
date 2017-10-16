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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.controller.tinyg.commons.AbstractTinyGCommunicator;
import org.goko.controller.tinyg.commons.ITinyGStatus;
import org.goko.controller.tinyg.commons.TinyG;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.controller.tinyg.controller.configuration.TinyGConfigurationValue;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.connection.serial.ISerialConnection;
import org.goko.core.connection.serial.SerialParameter;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystemFactory;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
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
public class TinyGCommunicator extends AbstractTinyGCommunicator<TinyGConfiguration, TinyGControllerService> {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGCommunicator.class);

	/**
	 * Constructor
	 * @param tinyg the tinyG service
	 */
	public TinyGCommunicator() {		
		setEndLineCharacters('\n');				
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#onConnected()
	 */
	@Override
	protected void onConnected() throws GkException {
		getIncomingBuffer().clear();
		getConnectionService().addInputDataListener(this);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				// Force strict JSon mode		
				try {
					forceJsonMode();
					Thread.sleep(100); // Dirty hack to give TinyG the time to process commands
					forceStatusReportFormat();
					Thread.sleep(500);
					requestStatusReport();
					Thread.sleep(100);
					requestQueueReport();
					Thread.sleep(100);
					requestConfigurationUpdate();
					Thread.sleep(100);
					requestCoordinateSystemUpdate();
				} catch (InterruptedException | GkException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				
			}
		});		
	}
		
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#onDisconnected()
	 */
	@Override
	protected void onDisconnected() throws GkException {
		getConnectionService().removeInputDataListener(this);
		getControllerService().setState(MachineState.UNDEFINED);
		getControllerService().resetConfiguration();
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
		CoordinateSystem cs = new CoordinateSystemFactory().get(StringUtils.upperCase(offsetName));
		JsonObject offsetObj = (JsonObject) jsonOffset;
		JsonValue xOffset = offsetObj.get("x");
		JsonValue yOffset = offsetObj.get("y");
		JsonValue zOffset = offsetObj.get("z");
		JsonValue aOffset = offsetObj.get("a");
		Tuple6b offset = new Tuple6b().setZero();
		offset.setX( Length.valueOf(xOffset.asBigDecimal(), getControllerService().getCurrentUnit() ));
		offset.setY( Length.valueOf(yOffset.asBigDecimal(), getControllerService().getCurrentUnit() ) );
		offset.setZ( Length.valueOf(zOffset.asBigDecimal(), getControllerService().getCurrentUnit() ) );
		if(aOffset != null){
			offset.setA(  Angle.valueOf(aOffset.asBigDecimal(), AngleUnit.DEGREE_ANGLE )  );
		}
		getControllerService().setCoordinateSystemOffset(cs, offset);
	}
	/**
	 * Handle the configuration changes received from the TinyG device
	 * @param responseEnvelope the response envelope
	 * @throws GkException GkException
	 */
	private void handleConfigurationModification(JsonObject responseEnvelope) throws GkException {
		TinyGConfiguration cfg = getControllerService().getConfiguration();
		cfg.setFromJson(responseEnvelope);
		getControllerService().setConfiguration(cfg);
	}

	private void handleGCodeResponse(JsonValue jsonValue, ITinyGStatus status) throws GkException {
		String 	receivedCommand = jsonValue.asString();
		getControllerService().handleGCodeResponse(receivedCommand, status);
	}

	protected void handleQueueReport(JsonValue queueReport) throws GkException {
		getControllerService().setAvailablePlannerBuffer(queueReport.asInt());
	}

	private void handleMessage(JsonValue message) throws GkException {
		getApplicativeLogService().warning(message.asString(), "TinyG");
		getControllerService().setMessage(message.asString());
	}
	
	private void handleProbeReport(JsonValue probeReport) throws GkException {
		if(probeReport.isObject()){
			Tuple6b 	probePosition 		= null;
			JsonObject 	probeReportObject 	= (JsonObject) probeReport;
			JsonValue 	eProbeResult 		= probeReportObject.get(TinyGv097.PROBE_REPORT_SUCCESS);
			boolean 	probeSuccess 		= (eProbeResult.asInt() == 1);
			
			JsonValue xProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_X);
			JsonValue yProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_Y);
			JsonValue zProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_Z);
			JsonValue aProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_A);
			JsonValue bProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_B);
			JsonValue cProbeResult = probeReportObject.get(TinyGv097.PROBE_REPORT_POSITION_C);
			probePosition = new Tuple6b();
			if(xProbeResult != null){
				probePosition.setX( Length.valueOf(xProbeResult.asBigDecimal(), getControllerService().getCurrentUnit()) );
			}
			if(yProbeResult != null){
				probePosition.setY( Length.valueOf(yProbeResult.asBigDecimal(), getControllerService().getCurrentUnit()) );
			}
			if(zProbeResult != null){
				probePosition.setZ( Length.valueOf(zProbeResult.asBigDecimal(), getControllerService().getCurrentUnit()) );
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
		
			getControllerService().handleProbeResult(probeSuccess, probePosition);
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
			Tuple6b 		 machinePosition= findMachinePosition(statusReportObject, units);
			Speed 		 	 velocity 		= findVelocity(statusReportObject, units);
			Speed 			 feedrate 		= findFeedrate(statusReportObject, units);
			CoordinateSystem cs 			= findCoordinateSystem(statusReportObject);
			EnumPlane 		 plane 			= findPlane(statusReportObject);
			EnumMotionMode 	 motionMode 	= findMotionMode(statusReportObject);
			
			GCodeContext gcodeContext = new GCodeContext(getControllerService().getGCodeContext());

			gcodeContext.setPosition(workPosition);		
			gcodeContext.setMachinePosition(machinePosition);
			gcodeContext.setDistanceMode(distanceMode);
			gcodeContext.setUnit(units);
			gcodeContext.setCoordinateSystem(cs);
			gcodeContext.setFeedrate(feedrate);
			gcodeContext.setPlane(plane);
			gcodeContext.setMotionMode(motionMode);
			
			if(state != null){
				getControllerService().setState(state);
			}
			if(velocity != null){
				getControllerService().setVelocity(velocity);
			}
			getControllerService().updateGCodeContext(gcodeContext);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleErrorReport(com.eclipsesource.json.JsonObject)
	 */
	@Override
	protected void handleErrorReport(JsonObject errorReportBody) throws GkException {
		// TODO
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
		// FIXME : add a condition so if TinyG continues to answer as non json, there is no infinite loop
		// tinyg.refreshStatus();
	}

	
	/**
	 * Extract state from status report
	 * @param statusReport
	 * @return
	 */
	private MachineState findState(JsonObject statusReport){
		JsonValue statReport = statusReport.get(TinyGv097.STATUS_REPORT_STATE);
		if(statReport != null && statReport.isNumber()){
			return TinyGControllerUtility.getState(statReport.asInt());
		}
		return null;
	}
	
	protected void updateCoordinateSystem(ICoordinateSystem cs) throws GkException{
		send("{\""+cs.getCode()+"\":\"\"}", true);		
	}
	
	/**
	 * Check if the current configured flow control matches the one for the connection
	 * @throws GkException GkException
	 */
	protected void checkExecutionControl() throws GkException{
		TinyGConfiguration configuration = getControllerService().getConfiguration();
		BigDecimal flowControl = configuration.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_FLOW_CONTROL, BigDecimal.class);

		// We always need to use flow control
		if(ObjectUtils.equals(flowControl, TinyGConfigurationValue.FLOW_CONTROL_OFF)){
			throw new GkFunctionalException("TNG-001");
		}

		// Make sure the current connection use the same flow control
		ISerialConnection connexion = getConnectionService().getCurrentConnection();		
		BigDecimal configuredFlowControl = configuration.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_FLOW_CONTROL, BigDecimal.class);

		if(configuredFlowControl.equals(TinyGConfigurationValue.FLOW_CONTROL_RTS_CTS)){ // TinyG expects RtsCts but the serial connection does not use it
			if((connexion.getFlowControl() & SerialParameter.FLOWCONTROL_RTSCTS) != SerialParameter.FLOWCONTROL_RTSCTS){
				throw new GkFunctionalException("TNG-005");
			}
		}else if(configuredFlowControl.equals(TinyGConfigurationValue.FLOW_CONTROL_XON_XOFF)){ // TinyG expects XonXoff but the serial connection does not use it
			if((connexion.getFlowControl() & SerialParameter.FLOWCONTROL_XONXOFF) != SerialParameter.FLOWCONTROL_XONXOFF){
				throw new GkFunctionalException("TNG-006");
			}
		}
	}
	
	/**
	 * Entry point for Kill Alarm action
	 * @throws GkException GkException
	 */
	public void killAlarm() throws GkException{
		send(buildJsonQuery(TinyGv097.KILL_ALARM_HEADER), true);
	}
	
	/**
	 * Entry point for Stop Motion action
	 * @throws GkException GkException
	 */
	public void stopMotion() throws GkException{	
		getConnectionService().clearOutputBuffer(); 
		sendImmediately(TinyGv097.FEED_HOLD, true);
		sendImmediately(TinyGv097.QUEUE_FLUSH, true);		
	}
	
	/**
	 * Entry point for Pause Motion action
	 * @throws GkException GkException
	 */
	public void pauseMotion() throws GkException{
		sendImmediately(TinyGv097.FEED_HOLD, false);
	}
	
	/**
	 * Entry point for Resume motion action
	 * @throws GkException GkException
	 */
	public void resumeMotion() throws GkException{
		sendImmediately(TinyGv097.CYCLE_START, false);
	}
	
	/**
	 * Entry point for Start Motion action
	 * @throws GkException GkException
	 */
	public void startMotion() throws GkException{
		sendImmediately(TinyGv097.CYCLE_START, false);
	}
	
	/**
	 * Entry point for Reset action
	 * @throws GkException GkException
	 */
	public void resetTinyG() throws GkException{
		sendImmediately(TinyGv097.RESET_COMMAND, false);
	}
	
	/**
	 * Entry point for Turn Spindle On action
	 * @throws GkException GkException
	 */
	public void turnSpindleOnCw() throws GkException{
		send(TinyGv097.TURN_SPINDLE_ON_CW_GCODE, true);
	}
	
	/**
	 * Entry point for Turn Spindle On action
	 * @throws GkException GkException
	 */
	public void turnSpindleOnCcw() throws GkException{
		send(TinyGv097.TURN_SPINDLE_ON_CCW_GCODE, true);
	}
	
	/**
	 * Entry point for Turn Spindle Off action
	 * @throws GkException GkException
	 */
	public void turnSpindleOff() throws GkException{
		send(TinyGv097.TURN_SPINDLE_OFF_GCODE, true);
	}
	
	/**
	 * Entry point for Reset Zero action
	 * @throws GkException GkException
	 */
	public void resetZero(List<String> axes) throws GkException{
		List<Byte> lstBytes = GkUtils.toBytesList("G28.3");
		if(CollectionUtils.isNotEmpty(axes)){
			for (String axe : axes) {
				lstBytes.addAll(GkUtils.toBytesList(axe+"0"));
			}
		}else{
			lstBytes.addAll( GkUtils.toBytesList("X0Y0Z0"));
		}
		send(lstBytes, true);
	}
	
	/**
	 * Sends a JSon command to set the format of the returned status report
	 * @throws GkException GkException
	 */
	public void forceStatusReportFormat() throws GkException{
		JsonObject statusReportFormat = new JsonObject();		
		statusReportFormat.add(TinyG.STATUS_REPORT_STATE, true);
		statusReportFormat.add(TinyG.STATUS_REPORT_VELOCITY, true);
		statusReportFormat.add(TinyG.STATUS_REPORT_FEEDRATE, true);
		statusReportFormat.add(TinyG.STATUS_REPORT_UNITS, true);
		statusReportFormat.add(TinyG.STATUS_REPORT_COORDINATES, true);
		statusReportFormat.add(TinyG.STATUS_REPORT_MOTION_MODE, true);
		statusReportFormat.add(TinyG.STATUS_REPORT_PLANE, true);
		statusReportFormat.add(TinyG.STATUS_REPORT_PATH_CONTROL, true);
		statusReportFormat.add(TinyG.STATUS_REPORT_DISTANCE_MODE, true);
		statusReportFormat.add(TinyG.STATUS_REPORT_FEEDRATE_MODE, true);
		addPositionReport(TinyG.STATUS_REPORT_MACHINE_POSITION, statusReportFormat);
		addPositionReport(TinyG.STATUS_REPORT_WORK_POSITION, statusReportFormat);
		JsonObject statusReportEnveloppe= new JsonObject();
		statusReportEnveloppe.add("sr", statusReportFormat);
		//send(statusReportEnveloppe, true);
		String statusReportEnveloppeStr = statusReportEnveloppe.toString().replaceAll("true", "t"); // Make it shorter
		send(statusReportEnveloppeStr, true);
	}
	
	/**
	 * Builds the position report mask based on G2Core preferences
	 * @param positionPrefix the prefix for the position report (mpos, pos, ...)
	 * @param statusReportFormat the target json object for SR format
	 */
	private void addPositionReport(String positionPrefix, JsonObject statusReportFormat){
		statusReportFormat.add(positionPrefix+"x", true);
		statusReportFormat.add(positionPrefix+"y", true);
		statusReportFormat.add(positionPrefix+"z", true);
		statusReportFormat.add(positionPrefix+"a", true);
		statusReportFormat.add(positionPrefix+"b", true);
		statusReportFormat.add(positionPrefix+"c", true);
	}
	
}
