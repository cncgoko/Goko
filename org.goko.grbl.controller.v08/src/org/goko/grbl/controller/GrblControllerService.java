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
package org.goko.grbl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.vecmath.Point3d;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.gcode.bean.provider.GCodeExecutionToken;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
import org.goko.grbl.controller.bean.StatusReport;
import org.goko.grbl.controller.configuration.GrblConfiguration;
import org.goko.grbl.controller.configuration.GrblSetting;
import org.goko.grbl.controller.executionqueue.ExecutionQueue;
import org.goko.grbl.controller.executionqueue.GrblGCodeExecutionToken;

/**
 * GRBL Controller implementation
 *
 * @author PsyKo
 *
 */
public class GrblControllerService extends EventDispatcher implements IGrblControllerService, IThreeAxisControllerAdapter, ICoordinateSystemAdapter {
	/**  Service ID */
	public static final String SERVICE_ID = "Grbl Controller";
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
	/** applicative log service */
	private IApplicativeLogService applicativeLogService;
	private GrblState grblState;
	private GrblCommunicator communicator;


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
	//	initValueStore();
		grblActionFactory 	 = new GrblActionFactory(this);
		configuration 		 = new GrblConfiguration();
		grblState 			 = new GrblState();
		grblState.addListener(this);
		// Initiate execution queue
		executionQueue 				= new ExecutionQueue<GrblGCodeExecutionToken>();
		ExecutorService executor 	= Executors.newSingleThreadExecutor();
		grblStreamingRunnable 		= new GrblStreamingRunnable(executionQueue, this);
		executor.execute(grblStreamingRunnable);
		LOG.info("Successfully started "+getServiceId());

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
		List<Byte> byteCommand = GkUtils.toBytesList(getGCodeService().convert(command));
		int usedBufferCount = CollectionUtils.size(byteCommand);
		communicator.send( byteCommand );
		setUsedGrblBuffer(usedGrblBuffer + usedBufferCount);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getPosition()
	 */
	@Override
	public Point3d getPosition() throws GkException {
		return grblState.getWorkPosition().toPoint3d();
	}

	public Point3d getMachinePosition() throws GkException {
		return grblState.getMachinePosition().toPoint3d();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getX()
	 */
	@Override
	public Double getX() throws GkException {
		BigDecimal xPos = grblState.getWorkPosition().getX();
		if(xPos != null){
			return xPos.doubleValue();
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getY()
	 */
	@Override
	public Double getY() throws GkException {
		BigDecimal yPos = grblState.getWorkPosition().getY();
		if(yPos != null){
			return yPos.doubleValue();
		}
		return null;
	}

	/** (inheritDoc)
	 * @throws GkException
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getZ()
	 */
	@Override
	public Double getZ() throws GkException {
		BigDecimal zPos = grblState.getWorkPosition().getZ();
		if(zPos != null){
			return zPos.doubleValue();
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#executeGCode(org.goko.core.gcode.bean.IGCodeProvider)
	 */
	@Override
	public GCodeExecutionToken executeGCode(IGCodeProvider gcodeProvider) throws GkException {
		GrblGCodeExecutionToken token = new GrblGCodeExecutionToken(gcodeProvider);
	//	quand on stoppe et relance un fichier, ca fait n'imp
		executionQueue.add(token);
		return token;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#isReadyForFileStreaming()
	 */
	@Override
	public boolean isReadyForFileStreaming() throws GkException {
		return MachineState.READY.equals(getState());
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
		communicator.sendWithoutEndLineCharacter( GkUtils.toBytesList(Grbl.CURRENT_STATUS) );
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

			if(currentToken != null && currentToken.getSentCommandCount() > 0){
				GCodeCommand command = currentToken.markNextCommandAsError();
				LOG.error("Error with command '"+command.toString()+"' : "+ StringUtils.substringAfter(errorMessage, "error: "));
				getApplicativeLogService().error("Error with command '"+command.toString()+"' : "+ StringUtils.substringAfter(errorMessage, "error: "), SERVICE_ID);
			}else{
				LOG.error("Grbl Error : "+ StringUtils.substringAfter(errorMessage, "error: "));
				getApplicativeLogService().error(StringUtils.substringAfter(errorMessage, "error: "), SERVICE_ID);
			}
			handleOkResponse();
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
		grblState.setState(statusReport.getState());
		grblState.setMachinePosition(statusReport.getMachinePosition());
		grblState.setWorkPosition(statusReport.getWorkPosition());
	}

	public MachineState getState() throws GkException{
		return grblState.getState();
	}

	protected MachineState getGrblStateFromString(String code){
		switch(code){
			case "Alarm": return MachineState.ALARM;
			case "Idle" : return MachineState.READY;
			case "Queue" : return MachineState.MOTION_HOLDING;
			case "Run" : return MachineState.MOTION_RUNNING;
			case "Home" : return MachineState.HOMING;
			default: return MachineState.UNDEFINED;
		}
	}

	// Action related methods
	public void startHomingSequence() throws GkException{
		List<Byte> homeCommand = new ArrayList<Byte>();
		homeCommand.addAll(GkUtils.toBytesList(Grbl.HOME_COMMAND));
		communicator.send( homeCommand );
	}

	public void pauseMotion() throws GkException{
		List<Byte> pauseCommand = new ArrayList<Byte>();
		pauseCommand.add(Grbl.PAUSE_COMMAND);
		communicator.sendImmediately( pauseCommand );
	}

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

	public void startMotion() throws GkException{
		List<Byte> startResumeCommand = new ArrayList<Byte>();
		startResumeCommand.add(Grbl.RESUME_COMMAND);
		communicator.send( startResumeCommand );
	}

	public void startJog(EnumGrblAxis axis, String feed, String step) throws GkException{
		String command = "G91G1"+axis.getAxisCode();
		if(axis.isNegative()){
			command+="-";
		}
		command += step;
		command += "F"+feed;
		List<Byte> lstBytes = GkUtils.toBytesList(command);
		communicator.send(lstBytes);
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
	 * @see org.goko.grbl.controller.IGrblControllerService#getGrblState()
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

}
