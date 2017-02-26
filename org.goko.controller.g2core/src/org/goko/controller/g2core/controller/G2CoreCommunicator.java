/**
 * 
 */
package org.goko.controller.g2core.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.controller.g2core.configuration.G2CoreConfiguration;
import org.goko.controller.g2core.preferences.G2CorePreferences;
import org.goko.controller.tinyg.commons.AbstractTinyGCommunicator;
import org.goko.controller.tinyg.commons.ITinyGStatus;
import org.goko.controller.tinyg.commons.TinyG;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.controller.bean.MachineState;
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
 * G2 Core communicator 
 * 
 * @author Psyko
 * @date 8 janv. 2017
 */
public class G2CoreCommunicator extends AbstractTinyGCommunicator<G2CoreConfiguration, G2CoreControllerService> {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(G2CoreCommunicator.class);
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleNonJsonData(java.lang.String)
	 */
	@Override
	protected void handleNonJsonData(String data) throws GkException {
		LOG.info("Received non json data ["+data+"]");
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleMalformedJson(java.lang.String)
	 */
	@Override
	protected void handleMalformedJson(String data) {
		LOG.info("Received malformed json data ["+data+"]");
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#onConnected()
	 */
	@Override
	protected void onConnected() throws GkException {
		getIncomingBuffer().clear();
		getConnectionService().addInputDataListener(this);
		forceJsonMode();
		forceStatusReportFormat();
		requestStatusReport();
		requestQueueReport();	
		requestConfigurationUpdate();
		requestCoordinateSystemUpdate();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#onDisconnected()
	 */
	@Override
	protected void onDisconnected() throws GkException {
		getIncomingBuffer().clear();
		getConnectionService().removeInputDataListener(this);
		getControllerService().setState(G2Core.State.UNDEFINED);
		getControllerService().resetConfiguration();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#getResponseFooter(org.goko.controller.tinyg.commons.JsonValue)
	 */
	@Override
	protected ITinyGStatus getResponseFooter(JsonValue footerBody) throws GkException {
		JsonArray footerArray = footerBody.asArray();
		int statusCodeIntValue = footerArray.get(TinyG.FOOTER_STATUS_CODE_INDEX).asInt();
		ITinyGStatus status = G2CoreStatusCode.findEnum(statusCodeIntValue);
		return status;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleResponse(org.goko.controller.tinyg.commons.JsonObject, org.goko.controller.tinyg.commons.ITinyGStatus)
	 */
	@Override
	protected void handleResponse(JsonObject responseBody, ITinyGStatus status) throws GkException {
		if(CollectionUtils.isEmpty(responseBody.names())){
			// Response is empty, it's a confirmation of a GCode line
			handleGCodeResponse(null, status);
			
		}else{
			for(String name : responseBody.names()){
				if(StringUtils.equals(name, G2Core.STATUS_REPORT)){
					handleStatusReport((JsonObject)responseBody.get(G2Core.STATUS_REPORT));
				
				}else if(StringUtils.equals(name, G2Core.QUEUE_REPORT)){
					handleQueueReport(responseBody.get(G2Core.QUEUE_REPORT));
	
				}else if(StringUtils.equals(name, G2Core.GCODE_COMMAND)){
					handleGCodeResponse(responseBody.get(G2Core.GCODE_COMMAND).asString(), status);
					
				}else if(StringUtils.equals(name, G2Core.LINE_REPORT)){
			//		LOG.info("Skipping line report "+String.valueOf(responseEnvelope.get(name)));
	
				}else if(StringUtils.equals(name, G2Core.PROBE_REPORT)){
					handleProbeReport(responseBody.get(G2Core.PROBE_REPORT));
				
				}else if(StringUtils.equals(name, G2Core.MESSAGE_REPORT)){
					handleMessage(responseBody.get(G2Core.MESSAGE_REPORT));
					
				}else if(StringUtils.defaultString(name).matches("(g|G)5(4|5|6|7|8|9)")){
					handleCoordinateSystemOffsetReport(name, responseBody.get(name));
					
				}else{
					handleConfigurationModification(responseBody);
				}
			}
		}	
	}
	
	
	/**
	 * Handle the reception of a message
	 * @param jsonValue
	 * @throws GkException GkException 
	 */
	private void handleMessage(JsonValue message) throws GkException {
		getApplicativeLogService().info(message.asString(), "G2 Core");
		getControllerService().setMessage(message.asString());
	}

	/**
	 * Receives configuration data
	 * @param responseBody the JSON containing the configurations 
	 * @throws GkException GkException 
	 */
	private void handleConfigurationModification(JsonObject responseBody) throws GkException {
		G2CoreConfiguration cfg = getControllerService().getConfiguration().getCopy();
		cfg.setFromJson(responseBody);
		getControllerService().setConfiguration(cfg);
	}

	/**
	 * @param status
	 * @throws GkException 
	 */
	private void handleGCodeResponse(String command, ITinyGStatus status) throws GkException {
		getControllerService().handleGCodeResponse(command, status);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleStatusReport(org.goko.controller.tinyg.commons.JsonObject)
	 */
	@Override
	protected void handleStatusReport(JsonObject statusReportBody) throws GkException {
		if(statusReportBody.isObject()){
			JsonObject 	statusReportObject = (JsonObject) statusReportBody;

			EnumUnit 		 units 			= findUnits(statusReportObject);
			Tuple6b 		 workPosition 	= findWorkPosition(statusReportObject, units);
			Tuple6b 		 machinePosition= findMachinePosition(statusReportObject, units);
			MachineState 	 state 			= findState(statusReportObject);
			EnumDistanceMode distanceMode 	= findDistanceMode(statusReportObject);			
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
			gcodeContext.setMachinePosition(machinePosition);
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

	
	


	/**
	 * Extract state from status report
	 * @param statusReport
	 * @return
	 */
	private MachineState findState(JsonObject statusReport){
		JsonValue statReport = statusReport.get(G2Core.STATUS_REPORT_STATE);
		if(statReport != null){			
			switch(statReport.asInt()){
			case 0: return MachineState.INITIALIZING;
			case 1: return MachineState.READY;
			case 2: return MachineState.ALARM;
			case 3: return MachineState.PROGRAM_STOP;
			case 4: return MachineState.PROGRAM_END;
			case 5: return MachineState.MOTION_RUNNING;
			case 6: return MachineState.MOTION_HOLDING;
			case 7: return MachineState.PROBE_CYCLE;
			//case 8: return MachineState.CYCLE;
			case 9: return MachineState.HOMING;
			default: return MachineState.UNDEFINED;
			}
		}
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleQueueReport(org.goko.controller.tinyg.commons.JsonValue)
	 */
	@Override
	protected void handleQueueReport(JsonValue queueReportBody) throws GkException {
		getControllerService().setAvailablePlannerBuffer(queueReportBody.asInt());
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGCommunicator#handleErrorReport(org.goko.controller.tinyg.commons.JsonObject)
	 */
	@Override
	protected void handleErrorReport(JsonObject errorReportBody) throws GkException {		 
		//{"fb":100.17,"st":94,"msg":"Planner assertion failure - mp_exec_aline() zero length move"}
		JsonValue errorMessage = errorReportBody.get(G2Core.MESSAGE_REPORT);
		getApplicativeLogService().error(errorMessage.asString(), "G2 Core");
	}

	protected void handleProbeReport(JsonValue probeReport) throws GkException {
		if(probeReport.isObject()){
			Tuple6b 	probePosition 		= null;
			JsonObject 	probeReportObject 	= (JsonObject) probeReport;
			JsonValue 	eProbeResult 		= probeReportObject.get(G2Core.PROBE_REPORT_SUCCESS);
			boolean 	probeSuccess 		= (eProbeResult.asInt() == 1);
			if(probeSuccess){
				JsonValue xProbeResult = probeReportObject.get(G2Core.PROBE_REPORT_POSITION_X);
				JsonValue yProbeResult = probeReportObject.get(G2Core.PROBE_REPORT_POSITION_Y);
				JsonValue zProbeResult = probeReportObject.get(G2Core.PROBE_REPORT_POSITION_Z);
				JsonValue aProbeResult = probeReportObject.get(G2Core.PROBE_REPORT_POSITION_A);
				JsonValue bProbeResult = probeReportObject.get(G2Core.PROBE_REPORT_POSITION_B);
				JsonValue cProbeResult = probeReportObject.get(G2Core.PROBE_REPORT_POSITION_C);
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
			}
			getControllerService().handleProbeResult(probeSuccess, probePosition);
		}
	}

	/**
	 * Handle the reception of a coordinate offset report
	 * @param offsetName the name of the offset
	 * @param jsonOffset the JSON description
	 * @throws GkException GkException
	 */
	private void handleCoordinateSystemOffsetReport(String offsetName, JsonValue jsonOffset) throws GkException{
		CoordinateSystem cs = new CoordinateSystemFactory().get(StringUtils.upperCase(offsetName));
		JsonObject offsetObj = (JsonObject) jsonOffset;
		JsonValue xOffset = offsetObj.get("x");
		JsonValue yOffset = offsetObj.get("y");
		JsonValue zOffset = offsetObj.get("z");
		JsonValue aOffset = offsetObj.get("a");
		JsonValue bOffset = offsetObj.get("b");
		JsonValue cOffset = offsetObj.get("c");
		Tuple6b offset = new Tuple6b().setZero();
		offset.setX( Length.valueOf(xOffset.asBigDecimal(), getControllerService().getCurrentUnit() ));
		offset.setY( Length.valueOf(yOffset.asBigDecimal(), getControllerService().getCurrentUnit() ) );
		offset.setZ( Length.valueOf(zOffset.asBigDecimal(), getControllerService().getCurrentUnit() ) );
		if(aOffset != null){
			offset.setA(  Angle.valueOf(aOffset.asBigDecimal(), AngleUnit.DEGREE_ANGLE )  );
		}
		if(bOffset != null){
			offset.setB(  Angle.valueOf(bOffset.asBigDecimal(), AngleUnit.DEGREE_ANGLE )  );
		}
		if(cOffset != null){
			offset.setC(  Angle.valueOf(cOffset.asBigDecimal(), AngleUnit.DEGREE_ANGLE )  );
		}
		getControllerService().setCoordinateSystemOffset(cs, offset);
	}
	/**
	 * Entry point for Kill Alarm action
	 * @throws GkException GkException
	 */
	public void killAlarm() throws GkException{
		send(buildJsonQuery(G2Core.KILL_ALARM_HEADER), true);
	}
	
	/**
	 * Entry point for Stop Motion action
	 * @throws GkException GkException
	 */
	public void stopMotion() throws GkException{
		sendImmediately(G2Core.FEED_HOLD_COMMAND, true);
		sendImmediately(G2Core.QUEUE_FLUSH_COMMAND, true);
	}
	
	/**
	 * Entry point for Pause Motion action
	 * @throws GkException GkException
	 */
	public void pauseMotion() throws GkException{
		sendImmediately(G2Core.FEED_HOLD_COMMAND, false);
	}
	
	/**
	 * Entry point for Resume motion action
	 * @throws GkException GkException
	 */
	public void resumeMotion() throws GkException{
		sendImmediately(G2Core.CYCLE_START_COMMAND, false);
	}
	
	/**
	 * Entry point for Start Motion action
	 * @throws GkException GkException
	 */
	public void startMotion() throws GkException{
		sendImmediately(G2Core.CYCLE_START_COMMAND, false);
	}
	
	/**
	 * Entry point for Reset action
	 * @throws GkException GkException
	 */
	public void resetG2Core() throws GkException{
		sendImmediately(G2Core.RESET_COMMAND, false);
	}
	
	/**
	 * Entry point for Turn Spindle On action
	 * @throws GkException GkException
	 */
	public void turnSpindleOn() throws GkException{
		send(G2Core.TURN_SPINDLE_ON_GCODE, true);
	}
	
	/**
	 * Entry point for Turn Spindle Off action
	 * @throws GkException GkException
	 */
	public void turnSpindleOff() throws GkException{
		send(G2Core.TURN_SPINDLE_OFF_GCODE, true);
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
		G2CorePreferences prefs = G2CorePreferences.getInstance();
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_MACHINE_STATE)){
			statusReportFormat.add(G2Core.STATUS_REPORT_STATE, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_VELOCITY)){
			statusReportFormat.add(G2Core.STATUS_REPORT_VELOCITY, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_FEEDRATE)){
			statusReportFormat.add(G2Core.STATUS_REPORT_FEEDRATE, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_UNITS)){
			statusReportFormat.add(G2Core.STATUS_REPORT_UNITS, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_COORDINATE_SYSTEM)){
			statusReportFormat.add(G2Core.STATUS_REPORT_COORDINATES, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_MOTION_MODE)){
			statusReportFormat.add(G2Core.STATUS_REPORT_MOTION_MODE, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_PLANE)){
			statusReportFormat.add(G2Core.STATUS_REPORT_PLANE, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_PATH_CONTROL)){
			statusReportFormat.add(G2Core.STATUS_REPORT_PATH_CONTROL, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_DISTANCE_MODE)){
			statusReportFormat.add(G2Core.STATUS_REPORT_DISTANCE_MODE, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_ARC_DISTANCE_MODE)){
			statusReportFormat.add(G2Core.STATUS_REPORT_ARC_DISTANCE_MODE, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_FEEDRATE_MODE)){
			statusReportFormat.add(G2Core.STATUS_REPORT_FEEDRATE_MODE, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_TOOL)){
			statusReportFormat.add(G2Core.STATUS_REPORT_TOOL, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_G92)){
			statusReportFormat.add(G2Core.STATUS_REPORT_G92, true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_MPOS)){
			addPositionReport(G2Core.STATUS_REPORT_MACHINE_POSITION, statusReportFormat);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_WPOS)){
			addPositionReport(G2Core.STATUS_REPORT_WORK_POSITION, statusReportFormat);
		}
		JsonObject statusReportEnveloppe= new JsonObject();
		statusReportEnveloppe.add("sr", statusReportFormat);
		send(statusReportEnveloppe, true);
	}
	/**
	 * Builds the position report mask based on G2Core preferences
	 * @param positionPrefix the prefix for the position report (mpos, pos, ...)
	 * @param statusReportFormat the target json object for SR format
	 */
	private void addPositionReport(String positionPrefix, JsonObject statusReportFormat){
		G2CorePreferences prefs = G2CorePreferences.getInstance();
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_POS_X)){
			statusReportFormat.add(positionPrefix+"x", true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_POS_Y)){
			statusReportFormat.add(positionPrefix+"y", true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_POS_Z)){
			statusReportFormat.add(positionPrefix+"z", true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_POS_A)){
			statusReportFormat.add(positionPrefix+"a", true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_POS_B)){
			statusReportFormat.add(positionPrefix+"b", true);
		}
		if(prefs.getBoolean(G2CorePreferences.STATUS_REPORT_POS_C)){
			statusReportFormat.add(positionPrefix+"c", true);
		}
	}

}
