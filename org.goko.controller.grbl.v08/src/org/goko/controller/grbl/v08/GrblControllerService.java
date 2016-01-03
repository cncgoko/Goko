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
package org.goko.controller.grbl.v08;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.controller.grbl.v08.bean.GrblExecutionError;
import org.goko.controller.grbl.v08.bean.StatusReport;
import org.goko.controller.grbl.v08.configuration.GrblConfiguration;
import org.goko.controller.grbl.v08.configuration.GrblSetting;
import org.goko.controller.grbl.v08.executionqueue.GrblGCodeExecutionToken;
import org.goko.controller.grbl.v08.topic.GrblExecutionErrorTopic;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.event.EventBrokerUtils;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandDistanceMode;
import org.goko.core.gcode.bean.execution.ExecutionQueue;
import org.goko.core.gcode.bean.provider.GCodeExecutionToken;
import org.goko.core.gcode.service.IGCodeExecutionMonitorService;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * GRBL v0.8 Controller implementation
 *
 * @author PsyKo
 *
 */
public class GrblControllerService extends EventDispatcher implements IGrblControllerService, IThreeAxisControllerAdapter, ICoordinateSystemAdapter {
	/**  Service ID */
	public static final String SERVICE_ID = "Grbl v0.8 Controller";
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(GrblControllerService.class);
	/** GCode service*/
	private IGCodeService gcodeService;
	/** The execution queue */
	private ExecutionQueue<GrblGCodeExecutionToken> executionQueue;
	/** Sending runnable */
	private GrblStreamingRunnable grblStreamingRunnable;
	/** Status polling */
	private Timer statusPollingTimer;
	/** Controller action factory*/
	private GrblActionFactory grblActionFactory;
	/** Grbl used buffer spaces */
	private int usedGrblBuffer;
	/** Grbl configuration */
	private GrblConfiguration configuration;
	/** Applicative log service */
	private IApplicativeLogService applicativeLogService;
	private GrblState grblState;
	private GrblCommunicator communicator;
	private IGCodeExecutionMonitorService monitorService;
	private EventAdmin eventAdmin;
	/** Jog step in use */
	private BigDecimalQuantity<Length> jogStep;

	/**
	 * Constructor
	 */
	public GrblControllerService() {
		communicator		 = new GrblCommunicator(this);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		grblActionFactory 	 = new GrblActionFactory(this);
		configuration 		 = new GrblConfiguration();
		grblState 			 = new GrblState();
		grblState.addListener(this);
		// Initiate execution queue
		executionQueue 				= new ExecutionQueue<GrblGCodeExecutionToken>();
		ExecutorService executor 	= Executors.newSingleThreadExecutor();
		grblStreamingRunnable 		= new GrblStreamingRunnable(executionQueue, this);
		executor.execute(grblStreamingRunnable);
		this.jogStep = NumberQuantity.of(BigDecimal.ZERO, SI.MILLIMETRE);
	}

	protected void stopStatusPolling(){
		statusPollingTimer.cancel();
	}

	public void startStatusPolling() {
		statusPollingTimer = new Timer();
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				try {
					refreshStatus();
				} catch (GkException e) {
					LOG.error(e);
				}
			}

		};
		statusPollingTimer.scheduleAtFixedRate(task, new Date(), 100);
	}


	/**
	 * @param evt
	 */
	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineValueUpdate(MachineValueUpdateEvent evt){
		notifyListeners(evt);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	public void sendCommand(GCodeCommand command) throws GkException{
		String cmd = command.getStringCommand();
		if(StringUtils.isEmpty(cmd)){
			cmd = new String(getGCodeService().convert(command));
		}		
		List<Byte> byteCommand = GkUtils.toBytesList(cmd);
		int usedBufferCount = CollectionUtils.size(byteCommand);
		communicator.send( byteCommand );
		setUsedGrblBuffer(usedGrblBuffer + usedBufferCount);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getPosition()
	 */
	@Override
	public Tuple6b getPosition() throws GkException {
		return grblState.getWorkPosition();
	}


	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getX()
	 */
	@Override
	public Quantity<Length> getX() throws GkException {
		BigDecimalQuantity<Length> xPos = grblState.getWorkPosition().getX();		
		return xPos;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getY()
	 */
	@Override
	public Quantity<Length> getY() throws GkException {
		BigDecimalQuantity<Length> yPos = grblState.getWorkPosition().getY();		
		return yPos;
	}

	/** (inheritDoc)
	 * @throws GkException
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getZ()
	 */
	@Override
	public Quantity<Length> getZ() throws GkException {
		BigDecimalQuantity<Length> zPos = grblState.getWorkPosition().getZ();
		return zPos;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#executeGCode(org.goko.core.gcode.bean.IGCodeProvider)
	 */
	@Override
	public GCodeExecutionToken executeGCode(IGCodeProvider gcodeProvider) throws GkException {
		GrblGCodeExecutionToken token = new GrblGCodeExecutionToken(gcodeProvider);
		token.setMonitorService(monitorService);
	//	quand on stoppe et relance un fichier, ca fait n'imp
		executionQueue.add(token);
		return token;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#isReadyForFileStreaming()
	 */
	@Override
	public boolean isReadyForFileStreaming() throws GkException {
		return GrblMachineState.READY.equals(getState()) || GrblMachineState.CHECK.equals(getState());
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getControllerAction(java.lang.String)
	 */
	@Override
	public IGkControllerAction getControllerAction(String actionId) throws GkException {
		IGkControllerAction action = grblActionFactory.findAction(actionId);
		if(action == null){
			throw new GkFunctionalException("Action '"+actionId+"' is not supported by this controller ("+getServiceId()+")");
		}
		return action;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#isControllerAction(java.lang.String)
	 */
	@Override
	public boolean isControllerAction(String actionId) throws GkException {
		return grblActionFactory.findAction(actionId) != null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValue(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> MachineValue<T> getMachineValue(String name, Class<T> clazz) throws GkException {
		return getGrblState().getValue(name, clazz);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueType(java.lang.String)
	 */
	@Override
	public Class<?> getMachineValueType(String name) throws GkException {
		return getGrblState().getControllerValueType(name);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition()
	 */
	@Override
	public List<MachineValueDefinition> getMachineValueDefinition() throws GkException {
		return getGrblState().getMachineValueDefinition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#findMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition findMachineValueDefinition(String id) throws GkException {
		return getGrblState().findMachineValueDefinition(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition getMachineValueDefinition(String id) throws GkException {
		return getGrblState().getMachineValueDefinition(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#cancelFileSending()
	 */
	@Override
	public void cancelFileSending() throws GkException {
		stopMotion();
	}

	/**
	 * @param connectionService the connectionService to set
	 * @throws GkException GkException
	 */
	public void setConnectionService(IConnectionService connectionService) throws GkException {
		this.communicator.setConnectionService(connectionService);
	}

	/**
	 * Refresh the status of the remote Grbl controller
	 * @throws GkException GkException
	 */
	public void refreshStatus() throws GkException{
		if(isActivePollingEnabled()){
			communicator.sendWithoutEndLineCharacter( GkUtils.toBytesList(Grbl.CURRENT_STATUS) );
		}
	}

	public void refreshSpaceCoordinates() throws GkException{
		communicator.send( GkUtils.toBytesList(Grbl.VIEW_PARAMETERS) );
	}

	public void refreshParserState() throws GkException{
		communicator.send( GkUtils.toBytesList(Grbl.PARSER_STATE) );
	}

	public void refreshConfiguration() throws GkException{
		communicator.send( GkUtils.toBytesList(Grbl.CONFIGURATION) );
	}

	protected void handleConfigurationReading(String cofigurationMessage) throws GkException{
		String identifier = StringUtils.substringBefore(cofigurationMessage, "=").trim();
		String value 	  = StringUtils.substringBetween(cofigurationMessage, "=","(").trim();
		configuration.setValue(identifier, value);
		LOG.info("Updating setting '"+identifier+"' with value '"+value+"'");
	}

	protected void receiveParserState(String parserState) throws GkException {
		String[] commands = StringUtils.split(parserState," ");
		GCodeContext context = new GCodeContext();
		if(commands != null){
			for (String strCommand : commands) {
				GCodeCommand command = gcodeService.parseCommand(strCommand, context);
				gcodeService.update(context, command);
			}
		}
		grblState.setCurrentContext(context);

	}

	protected void handleError(String errorMessage) throws GkException{
		if(executionQueue != null){
			GrblGCodeExecutionToken currentToken = executionQueue.getCurrentToken();
			String formattedErrorMessage = StringUtils.EMPTY;
			if(currentToken != null && currentToken.getSentCommandCount() > 0){
				 // Error occured during GCode programm execution, let's give the source command
				GCodeCommand command = currentToken.markNextCommandAsError();
				formattedErrorMessage = "Error with command '"+command.toString()+"' : "+ StringUtils.substringAfter(errorMessage, "error: ");				
			}else{
				formattedErrorMessage = "Grbl Error : "+ StringUtils.substringAfter(errorMessage, "error: ");
			}
			LOG.error(formattedErrorMessage);
			getApplicativeLogService().error(formattedErrorMessage, SERVICE_ID);
			// If not in check mode, let's pause the execution (disabled in check mode because check mode can't handle paused state and buffer would be flooded with commands)
			if(!ObjectUtils.equals(GrblMachineState.CHECK, getState())){	
				pauseMotion();
				EventBrokerUtils.send(eventAdmin, new GrblExecutionErrorTopic(), new GrblExecutionError("Error reported durring execution", "Execution was paused after Grbl reported an error. You can resume, or stop the execution at your own risk.", formattedErrorMessage));
			}
		}else{
			LOG.error("Grbl Error : "+ StringUtils.substringAfter(errorMessage, "error: "));
			getApplicativeLogService().error(StringUtils.substringAfter(errorMessage, "error: "), SERVICE_ID);
		}
	}

	
	protected void handleOkResponse() throws GkException{
		GrblGCodeExecutionToken currentToken = executionQueue.getCurrentToken();
		if(currentToken != null){
			GCodeCommand command = currentToken.markNextCommandAsConfirmed();
			if(command != null ){
				String strCommand = new String(getGCodeService().convert(command));
				setUsedGrblBuffer(usedGrblBuffer - StringUtils.length(strCommand));
			}
			grblStreamingRunnable.releaseBufferSpaceMutex();
		}
	}

	protected void initialiseConnectedState() throws GkException{
		setUsedGrblBuffer(0);
		refreshConfiguration();
		refreshSpaceCoordinates();
		refreshParserState();
	}
		
	protected void handleStatusReport(StatusReport statusReport) throws GkException{
		GrblMachineState previousState = getState();
		grblState.setState(statusReport.getState());		
		grblState.setMachinePosition(statusReport.getMachinePosition(), getConfiguration().getReportUnit());
		grblState.setWorkPosition(statusReport.getWorkPosition(), getConfiguration().getReportUnit());
		
		if(!ObjectUtils.equals(previousState, statusReport.getState())){			
			eventAdmin.sendEvent(new Event(CONTROLLER_TOPIC_STATE_UPDATE, (Map<String, ?>)null));
		}
	}

	public GrblMachineState getState() throws GkException{
		return grblState.getState();
	}
	
	public void setState(GrblMachineState state) throws GkException{
		grblState.setState(state);
		eventAdmin.sendEvent(new Event(CONTROLLER_TOPIC_STATE_UPDATE, (Map<String, ?>)null));
	}
	
	protected GrblMachineState getGrblStateFromString(String code){
		switch(code){
			case "Alarm": return GrblMachineState.ALARM;
			case "Idle" : return GrblMachineState.READY;
			case "Queue" : return GrblMachineState.MOTION_HOLDING;
			case "Run" : return GrblMachineState.MOTION_RUNNING;
			case "Home" : return GrblMachineState.HOMING;
			case "Check" : return GrblMachineState.CHECK;
			default: return GrblMachineState.UNDEFINED;
		}
	}

	/*
	 *  Action related methods
	 */
	
	public void startHomingSequence() throws GkException{
		List<Byte> homeCommand = new ArrayList<Byte>();
		homeCommand.addAll(GkUtils.toBytesList(Grbl.HOME_COMMAND));
		communicator.send( homeCommand );
	}
	
	/**
	 * Pause the motion by sending a pause character to Grbl
	 * If the execution queue is not empty, it is also paused
	 * @throws GkException GkException
	 */
	public void pauseMotion() throws GkException{
		List<Byte> pauseCommand = new ArrayList<Byte>();
		pauseCommand.add(Grbl.PAUSE_COMMAND);
		communicator.sendImmediately( pauseCommand );
		executionQueue.setPaused(true);
	}

	/**
	 * Stop the motion by sending a pause and a flush character to Grbl
	 * If the execution queue is not empty, it is also stopped and emptied
	 * @throws GkException GkException
	 */
	public void stopMotion() throws GkException{
		List<Byte> stopCommand = new ArrayList<Byte>();
		stopCommand.add(Grbl.PAUSE_COMMAND);
		stopCommand.add(Grbl.RESET_COMMAND); // TODO : it seems that resetting while in motion causes the GRBL to go back to alarm state. Wait motion to be complete before resetting
		communicator.sendImmediately(stopCommand);

		if(executionQueue != null){
			executionQueue.clear();
			grblStreamingRunnable.releaseBufferSpaceMutex();
		}
		setUsedGrblBuffer(0);
	}

	/**
	 * Start the motion by sending a resume character to Grbl
	 * If the execution queue is paused, it is also resumed
	 * @throws GkException GkException
	 */
	public void startMotion() throws GkException{
		List<Byte> startResumeCommand = new ArrayList<Byte>();
		startResumeCommand.add(Grbl.RESUME_COMMAND);
		communicator.sendWithoutEndLineCharacter( startResumeCommand );						
		if(executionQueue != null){
			executionQueue.setPaused(false);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IStepJogService#setJogStep(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setJogStep(BigDecimalQuantity<Length> step) throws GkException {
		this.jogStep = step;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IStepJogService#getJogStep()
	 */
	@Override
	public BigDecimalQuantity<Length> getJogStep() throws GkException {
		return jogStep;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#startJog(org.goko.core.controller.bean.EnumControllerAxis, java.math.BigDecimal)
	 */
	@Override	
	public void startJog(EnumControllerAxis axis, BigDecimal feedrate) throws GkException {		
		String oldDistanceMode = "G90";
		if(grblState.getDistanceMode() == EnumGCodeCommandDistanceMode.RELATIVE){
			oldDistanceMode = "G91";
		}
		String command = "G91G1"+axis.getAxisCode();
		if(axis.isNegative()){
			command+="-";
		}
		command += GokoPreference.getInstance().format(jogStep.to(getCurrentGCodeContext().getUnit().getUnit()), true, false);
		if(feedrate != null){
			command += "F"+feedrate;
		}
		List<Byte> lstBytes = GkUtils.toBytesList(command);
		communicator.send(lstBytes);
		List<Byte> distanceModeBackup = GkUtils.toBytesList(oldDistanceMode);
		communicator.send(distanceModeBackup);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IStepJogService#stopJog()
	 */
	@Override
	public void stopJog() throws GkException {
		// Nothing to do in stop jog since it's a step jog
	}

	public void resetZero(List<String> axes) throws GkException{
		List<Byte> lstBytes = GkUtils.toBytesList("G92");
		if(CollectionUtils.isNotEmpty(axes)){
			for (String axe : axes) {
				lstBytes.addAll(GkUtils.toBytesList(axe+"0"));
			}
		}else{
			lstBytes.addAll( GkUtils.toBytesList("X0Y0Z0"));
		}
		communicator.send(lstBytes);
	}

	public void killAlarm() throws GkException{
		List<Byte> lstBytes = GkUtils.toBytesList("$X");
		communicator.send(lstBytes);
	}

	/**
	 * @return the usedGrblBuffer
	 * @throws GkException  GkException
	 */
	public int getUsedGrblBuffer() throws GkException {
		return grblState.getUsedGrblBuffer();
	}

	/**
	 * @param usedGrblBuffer the usedGrblBuffer to set
	 * @throws GkException GkException
	 */
	public void setUsedGrblBuffer(int usedGrblBuffer) throws GkException {
		this.usedGrblBuffer = usedGrblBuffer;
		grblState.setUsedGrblBuffer(usedGrblBuffer);
	}

	/**
	 * @return the configuration
	 */
	@Override
	public GrblConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 * @throws GkException GkException
	 */
	@Override
	public void setConfiguration(GrblConfiguration configuration) throws GkException {
		this.configuration = configuration;
		if(CollectionUtils.isNotEmpty( configuration.getLstGrblSetting() )){
			List<GrblSetting<?>> lstSetting = configuration.getLstGrblSetting();
			List<Byte> cfgCommand = new ArrayList<Byte>();
			for (GrblSetting<?> grblSetting : lstSetting) {
				cfgCommand.addAll(GkUtils.toBytesList(grblSetting.getIdentifier()+"="+grblSetting.getValueAsString() ));
				communicator.send( cfgCommand );
				cfgCommand.clear();
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#moveToAbsolutePosition(org.goko.core.gcode.bean.Tuple6b)
	 */
	@Override
	public void moveToAbsolutePosition(Tuple6b position) throws GkException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the gcodeService
	 */
	public IGCodeService getGCodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGCodeService(IGCodeService gcodeService) {
		this.gcodeService = gcodeService;
	}

	/**
	 * @return the applicativeLogService
	 */
	public IApplicativeLogService getApplicativeLogService() {
		return applicativeLogService;
	}

	/**
	 * @param applicativeLogService the applicativeLogService to set
	 */
	public void setApplicativeLogService(IApplicativeLogService applicativeLogService) {
		this.applicativeLogService = applicativeLogService;
	}


	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.IGrblControllerService#getGrblState()
	 */
	@Override
	public GrblState getGrblState() {
		return grblState;
	}

	protected void setOffsetCoordinate(String offsetIdentifier, Tuple6b value) throws GkException{
		getGrblState().setOffset(EnumCoordinateSystem.valueOf(offsetIdentifier), value);
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getCurrentGCodeContext()
	 */
	@Override
	public GCodeContext getCurrentGCodeContext() throws GkException {
		return grblState.getCurrentContext();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCoordinateSystemOffset(org.goko.core.gcode.bean.commands.EnumCoordinateSystem)
	 */
	@Override
	public Tuple6b getCoordinateSystemOffset(EnumCoordinateSystem cs) throws GkException {
		return grblState.getOffset(cs);
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCoordinateSystem()
	 */
	@Override
	public List<EnumCoordinateSystem> getCoordinateSystem() throws GkException {
		return new ArrayList<EnumCoordinateSystem>(Arrays.asList(EnumCoordinateSystem.values()));
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCurrentCoordinateSystem()
	 */
	@Override
	public EnumCoordinateSystem getCurrentCoordinateSystem() throws GkException {
		return grblState.getCurrentContext().getCoordinateSystem();
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#setCurrentCoordinateSystem(org.goko.core.gcode.bean.commands.EnumCoordinateSystem)
	 */
	@Override
	public void setCurrentCoordinateSystem(EnumCoordinateSystem cs) throws GkException {
		communicator.send( GkUtils.toBytesList( String.valueOf(cs)) );
		communicator.send( GkUtils.toBytesList( "$G" ) );
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#resetCurrentCoordinateSystem()
	 */
	@Override
	public void resetCurrentCoordinateSystem() throws GkException {
		EnumCoordinateSystem cs = getGrblState().getCurrentContext().getCoordinateSystem();
		String cmd = "G10";
		switch (cs) {
		case G54: cmd +="P1";			
		break;
		case G55: cmd +="P2";			
		break;
		case G56: cmd +="P3";			
		break;
		case G57: cmd +="P4";			
		break;
		case G58: cmd +="P5";			
		break;
		case G59: cmd +="P6";			
		break;
		default: throw new GkFunctionalException("GRBL-002", cs.name());			
		}
		Tuple6b offsets = getCoordinateSystemOffset(getCurrentCoordinateSystem());
		Tuple6b mPos = new Tuple6b(getPosition());
		mPos = mPos.add(offsets);
		cmd += "L2";
		cmd += "X"+getPositionAsString(mPos.getX());
		cmd += "Y"+getPositionAsString(mPos.getY());
		cmd += "Z"+getPositionAsString(mPos.getZ());
		communicator.send( GkUtils.toBytesList( cmd ) );
		communicator.send( GkUtils.toBytesList( Grbl.VIEW_PARAMETERS ) );
	}	
	
	/**
	 * Returns the given Length quantity as a String, formatted using the goko preferences for decimal numbers 	
	 * @param q the quantity to format
	 * @return a String
	 * @throws GkException GkException
	 */
	protected String getPositionAsString(BigDecimalQuantity<Length> q) throws GkException{	
		return GokoPreference.getInstance().format( q.to(getCurrentUnit()), true, false);
	}
	
	/**
	 * Returns the current unit in the Grbl Conteext. It can be different from the unit in the goko preferences
	 * @return Unit
	 */
	private Unit<Length> getCurrentUnit() {
		return grblState.getContextUnit().getUnit();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.IGrblControllerService#setActivePollingEnabled(boolean)
	 */
	@Override
	public void setActivePollingEnabled(boolean enabled) throws GkException {
		grblState.setActivePolling(enabled);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.IGrblControllerService#isActivePollingEnabled()
	 */
	@Override
	public boolean isActivePollingEnabled() throws GkException {
		return grblState.isActivePolling();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.IGrblControllerService#setCheckModeEnabled(boolean)
	 */
	@Override
	public void setCheckModeEnabled(boolean enabled) throws GkException {		
		if((enabled && ObjectUtils.equals(GrblMachineState.READY, getState())) || // Check mode is disabled and we want to enable it
			(!enabled && ObjectUtils.equals(GrblMachineState.CHECK, getState())) ){ // Check mode is enabled and we want to disable it
			communicator.send(GkUtils.toBytesList(Grbl.CHECK_MODE));
		}else{
			throw new GkFunctionalException("GRBL-001", String.valueOf(enabled), getState().getLabel());
		}
	}
	
	/**
	 * @return the eventAdmin
	 */
	public EventAdmin getEventAdmin() {
		return eventAdmin;
	}
	/**
	 * @param eventAdmin the eventAdmin to set
	 */
	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	/**
	 * @return the monitorService
	 */
	public IGCodeExecutionMonitorService getMonitorService() {
		return monitorService;
	}

	/**
	 * @param monitorService the monitorService to set
	 */
	public void setMonitorService(IGCodeExecutionMonitorService monitorService) {
		this.monitorService = monitorService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#getFileExtension()
	 */
	@Override
	public String getFileExtension() {
		return "grbl.cfg";
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#canExport()
	 */
	@Override
	public boolean canExport() throws GkException {
		return GrblMachineState.READY.equals(getState());
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#exportTo(java.io.OutputStream)
	 */
	@Override
	public void exportTo(OutputStream stream) throws GkException {
		GrblConfiguration config = getConfiguration();
		StringBuffer buffer = new StringBuffer();
		for (GrblSetting<?> setting : config.getLstGrblSetting()) {
			buffer.append(setting.getIdentifier()+"="+setting.getValueAsString());
			buffer.append(System.lineSeparator());
		}
		try {
			stream.write(buffer.toString().getBytes());
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#canImport()
	 */
	@Override
	public boolean canImport() throws GkException {	
		return GrblMachineState.READY.equals(getState());
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#importFrom(java.io.InputStream)
	 */
	@Override
	public void importFrom(InputStream inputStream) throws GkException {
		GrblConfiguration cfg = getConfiguration();
		Scanner scanner = new Scanner(inputStream);
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] tokens = line.split("=");
			if(tokens != null && tokens.length == 2){
				cfg.setValue(tokens[0], tokens[1]);
			}else{
				LOG.warn("Ignoring configuration line ["+line+"] because it's malformatted.");
			}
		}
		scanner.close();
		setConfiguration(cfg);
	}
	
}
