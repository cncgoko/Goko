/**
 * 
 */
package org.goko.controller.grbl.v11;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.controller.grbl.commons.AbstractGrblCommunicator;
import org.goko.controller.grbl.commons.IGrblStatus;
import org.goko.controller.grbl.v11.bean.GrblMachineState;
import org.goko.controller.grbl.v11.bean.StatusReport;
import org.goko.controller.grbl.v11.configuration.GrblConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.common.utils.NumberUtils;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

/**
 * Grbl Communicator for v1.1
 * @author Psyko
 * @date 5 avr. 2017
 */
public class GrblCommunicator extends AbstractGrblCommunicator<GrblConfiguration, GrblMachineState, GrblControllerService> {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GrblCommunicator.class);
	/** Pattern for number decoding */
	private static String NUMBER_PATTERN = "(-?[0-9]*\\.[0-9]*)";
	/** Pattern for integer decoding */
	private static String INTEGER_PATTERN = "([0-9]*)";
	/** Pattern for decoding machine position in status report */
	private static Pattern PATTERN_MPOS = Pattern.compile(".*MPos:"+NUMBER_PATTERN+","+NUMBER_PATTERN+","+NUMBER_PATTERN+".*");
	/** Pattern for decoding work position in status report */
	private static Pattern PATTERN_WPOS = Pattern.compile(".*WPos:"+NUMBER_PATTERN+","+NUMBER_PATTERN+","+NUMBER_PATTERN+".*");
	/** Pattern for decoding planner buffer in status report */
	private static Pattern PATTERN_BUF = Pattern.compile(".*Bf:"+INTEGER_PATTERN+","+INTEGER_PATTERN+".*");
	/** Pattern for decoding override values in status report */
	private static Pattern PATTERN_OV = Pattern.compile(".*Ov:"+INTEGER_PATTERN+","+INTEGER_PATTERN+","+INTEGER_PATTERN+".*");
	/** Pattern for decoding the current work coordinate offset */
	private static Pattern PATTERN_WCO = Pattern.compile(".*WCO:"+NUMBER_PATTERN+","+NUMBER_PATTERN+","+NUMBER_PATTERN+".*");
	/** Pattern for decoding feed and speed in status report */
	private static Pattern PATTERN_FS = Pattern.compile(".*FS:"+INTEGER_PATTERN+","+INTEGER_PATTERN+".*");
	/** Pattern for decoding feed only in status report */
	private static Pattern PATTERN_F = Pattern.compile(".*F:"+INTEGER_PATTERN+".*");
	/** Pattern for decoding feed only in status report */
	private static Pattern PATTERN_A = Pattern.compile(".*A:([A-Z]+).*");
	/** Pattern for decoding tool length offset */
	private static Pattern PATTERN_TLO = Pattern.compile(".*TLO:"+NUMBER_PATTERN+".*");
	/** Pattern for decoding probe result */
	private static Pattern PATTERN_PRB = Pattern.compile("\\[PRB:"+NUMBER_PATTERN+","+NUMBER_PATTERN+","+NUMBER_PATTERN+":"+INTEGER_PATTERN+"\\]");
	/**
	 * The target controller service
	 * @param grblControllerService
	 */
	protected GrblCommunicator(GrblControllerService grblControllerService) {
		super(grblControllerService);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#onConnected()
	 */
	@Override
	protected void onConnected() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#onDisconnected()
	 */
	@Override
	protected void onDisconnected() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#handleProbeResult(java.lang.String)
	 */
	@Override
	protected void handleProbeResult(String trimmedData) throws GkException {
		Matcher probeMatcher = PATTERN_PRB.matcher(trimmedData);
		if(probeMatcher.matches()){
			boolean probeSuccess = BooleanUtils.toBoolean(probeMatcher.group(4));
			Tuple6b probePosition = new Tuple6b();
			parseTuple(probePosition, probeMatcher.group(1), probeMatcher.group(2), probeMatcher.group(3));
			getControllerService().handleProbeResult(probeSuccess, probePosition);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#handleParserState(java.lang.String)
	 */
	@Override
	protected void handleParserState(String trimmedData) throws GkException {
		getControllerService().handleParserState(StringUtils.substringBetween(trimmedData, "[GC:", "]"));
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#handleStatusReport(java.lang.String)
	 */
	@Override
	protected void handleStatusReport(String trimmedData) throws GkException {
		StatusReport statusReport = parseStatusReport(trimmedData);
		getControllerService().handleStatusReport(statusReport);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#handleMessage(java.lang.String)
	 */
	@Override
	protected void handleMessage(String trimmedData) throws GkException {
		String message = StringUtils.substringBetween(trimmedData, "[MSG:", "]");
		getControllerService().handleMessage(message);
	}
	/**
	 * Create a status report from the given string
	 * @param strStatusReport the String representing the status report
	 * @return {@link StatusReport}
	 * @throws GkException
	 */
	private StatusReport parseStatusReport(String strStatusReport) throws GkException{
		StatusReport result = new StatusReport();
		int comma = StringUtils.indexOf(strStatusReport, "|");
		String state = StringUtils.substring(strStatusReport, 1, comma);
		GrblMachineState grblState = getGrblStateFromString(state);
		result.setState(grblState);

		// Indicate a tool state report (if override is present, then the report also report tools state)
		boolean toolStateReport = false;
		
		Tuple6b machinePosition = null;
		Tuple6b workPosition = null;
		// Looking for MPosition
		Matcher mposMatcher = PATTERN_MPOS.matcher(strStatusReport);
		if(mposMatcher.matches()){
			machinePosition = new Tuple6b().setNull();
			String mposX = mposMatcher.group(1);
			String mposY = mposMatcher.group(2);
			String mposZ = mposMatcher.group(3);
			parseTuple(machinePosition, mposX, mposY, mposZ);
			result.setMachinePosition(machinePosition);
		}
		// Looking for WPosition
		Matcher wposMatcher = PATTERN_WPOS.matcher(strStatusReport);
		if(wposMatcher.matches()){
			workPosition = new Tuple6b().setNull();
			String wposX = wposMatcher.group(1);
			String wposY = wposMatcher.group(2);
			String wposZ = wposMatcher.group(3);
			parseTuple(workPosition, wposX, wposY, wposZ);
			result.setWorkPosition(workPosition);
		}
		
		// Looking for buffer planner occupation		
		Matcher bufMatcher = PATTERN_BUF.matcher(strStatusReport);
		if(bufMatcher.matches()){			
			Integer availablePlannerBuffer 	= NumberUtils.parseIntegerOrNull(bufMatcher.group(1));
			Integer availableRxBuffer 		= NumberUtils.parseIntegerOrNull(bufMatcher.group(2));
			result.setAvailablePlannerBuffer(availablePlannerBuffer);
			result.setAvailableRxBuffer(availableRxBuffer);
		}
		
		// Looking for the current work coordinate offset		
		Matcher wcoMatcher = PATTERN_WCO.matcher(strStatusReport);
		if(wcoMatcher.matches()){			
			Tuple6b wco = new Tuple6b().setNull();
			String wcoX = wcoMatcher.group(1);
			String wcoY = wcoMatcher.group(2);
			String wcoZ = wcoMatcher.group(3);
			parseTuple(wco, wcoX, wcoY, wcoZ);
			result.setCurrentWorkCoordinateOffset(wco);
		}
		
		// Looking for override values		
		Matcher overrideMatcher = PATTERN_OV.matcher(strStatusReport);
		if(overrideMatcher.matches()){
			toolStateReport = true;
			Integer overrideFeed 	=  NumberUtils.parseIntegerOrNull(overrideMatcher.group(1));
			Integer overrideRapid 	=  NumberUtils.parseIntegerOrNull(overrideMatcher.group(2));
			Integer overrideSpindle =  NumberUtils.parseIntegerOrNull(overrideMatcher.group(3));
			result.setOverrideFeed(overrideFeed);
			result.setOverrideRapid(overrideRapid);
			result.setOverrideSpindle(overrideSpindle);
		}
		
		// Looking for feed and speed		
		Matcher fsMatcher = PATTERN_FS.matcher(strStatusReport);
		if(fsMatcher.matches()){
			Unit<Length> unit = getControllerService().getReportUnit();
			Unit<Speed> feedUnit = SpeedUnit.MILLIMETRE_PER_MINUTE;
			if(LengthUnit.INCH.equals(unit)){
				feedUnit = SpeedUnit.INCH_PER_MINUTE;
			}
			Speed velocity = Speed.valueOf(fsMatcher.group(1), feedUnit);
			Integer spindleRpm = NumberUtils.parseIntegerOrNull(fsMatcher.group(2));
			result.setVelocity(velocity);
			result.setSpindleSpeed(spindleRpm);
		}else{
			Matcher fMatcher = PATTERN_F.matcher(strStatusReport);
			if(fMatcher.matches()){
				Unit<Length> unit = getControllerService().getReportUnit();
				Unit<Speed> feedUnit = SpeedUnit.MILLIMETRE_PER_MINUTE;
				if(LengthUnit.INCH.equals(unit)){
					feedUnit = SpeedUnit.INCH_PER_MINUTE;
				}
				Speed velocity = Speed.valueOf(fMatcher.group(1), feedUnit);
				result.setVelocity(velocity);
			}
		}
		
		// Looking for accessory state
		if(toolStateReport){
			result.setFloodCoolantState(false);
			result.setMistCoolantState(false);
		}
		
		Matcher aMatcher = PATTERN_A.matcher(strStatusReport);
		if(aMatcher.matches()){			
			String states = aMatcher.group(1);
			result.setMistCoolantState(false);
			result.setFloodCoolantState(false);
			for (int i = 0; i < states.length(); i++) {
				char letter = states.charAt(i);
				switch (letter) {
				case 'S': result.setSpindleDirection("CW");					
					break;
				case 'C': result.setSpindleDirection("CCW");
					break;
				case 'F': result.setFloodCoolantState(true);
					break;
				case 'M': result.setMistCoolantState(true);
					break;
				default: LOG.warn("Ignoring accessory state ["+letter+"]");
					break;
				}
			}
		}
		return result;
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#handleError(java.lang.String)
	 */
	@Override
	protected void handleError(String trimmedData) {
		String strErrorCode = StringUtils.remove(trimmedData, "error:");
		Integer errorCode = NumberUtils.parseIntegerOrNull(strErrorCode);
		IGrblStatus error = GrblGStatusCode.UNKNOWN_ERROR;
		if(errorCode != null){			
			IGrblStatus enumError = GrblGStatusCode.findEnum(errorCode);
			if(enumError != null){
				error = enumError;
			}
		}
		try {
			getControllerService().handleError(error);
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#handleOkResponse()
	 */
	@Override
	protected void handleOkResponse() {
		try {
			getControllerService().handleOkResponse();
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#handleAlarm(java.lang.String)
	 */
	@Override
	protected void handleAlarm(String trimmedData) throws GkException {
		GrblMachineState alarmState = GrblMachineState.ALARM;
		switch (trimmedData) {
		case "ALARM:1": alarmState = GrblMachineState.ALARM_1; break;
		case "ALARM:2": alarmState = GrblMachineState.ALARM_2; break;
		case "ALARM:3": alarmState = GrblMachineState.ALARM_3; break;
		case "ALARM:4": alarmState = GrblMachineState.ALARM_4; break;
		case "ALARM:5": alarmState = GrblMachineState.ALARM_5; break;
		case "ALARM:6": alarmState = GrblMachineState.ALARM_6; break;
		case "ALARM:7": alarmState = GrblMachineState.ALARM_7; break;
		case "ALARM:8": alarmState = GrblMachineState.ALARM_8; break;
		case "ALARM:9": alarmState = GrblMachineState.ALARM_9; break;
		default: alarmState = GrblMachineState.ALARM; break;
		}
		getControllerService().handleAlarm(alarmState);
		getApplicativeLogService().error(alarmState.getLabel(), GrblControllerService.SERVICE_ID);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#handleToolLengthOffset(java.lang.String)
	 */
	@Override
	protected void handleToolLengthOffset(String trimmedData) throws GkException {
		Matcher result = PATTERN_TLO.matcher(trimmedData);
		if(result.matches()){
			String strTlo = result.group(1);
			Length toolLengthOffset = Length.valueOf(strTlo, getControllerService().getReportUnit());
			getControllerService().handleToolLengthOffset(toolLengthOffset);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblCommunicator#getGrblStateFromString(java.lang.String)
	 */
	@Override
	protected GrblMachineState getGrblStateFromString(String state) {
		switch(state){
			case "Alarm": return GrblMachineState.ALARM;
			case "Idle" : return GrblMachineState.IDLE;
			case "Jog" : return GrblMachineState.JOG;
			case "Run" : return GrblMachineState.RUN;
			case "Home" : return GrblMachineState.HOME;
			case "Sleep" : return GrblMachineState.SLEEP;
			case "Door:0" : return GrblMachineState.DOOR; // Create multiple states for door
			case "Door:1" : return GrblMachineState.DOOR;
			case "Door:2" : return GrblMachineState.DOOR;
			case "Door:3" : return GrblMachineState.DOOR;
			case "Check" : return GrblMachineState.CHECK;
			case "Hold:0" : return GrblMachineState.HOLD;
			case "Hold:1" : return GrblMachineState.HOLDING;
			default: return GrblMachineState.UNDEFINED;
		}
	}

	/**
	 * Requests a status update
	 * @throws GkException GkException
	 */
	public void requestStatus() throws GkException {
		send(Grbl.CURRENT_STATUS, false);
	}
}
