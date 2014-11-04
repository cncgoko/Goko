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
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.bean.MachineValueStore;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.provider.GCodeExecutionToken;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
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
public class GrblControllerService extends EventDispatcher implements IGrblControllerService, IThreeAxisControllerAdapter {
	/**  Service ID */
	public static final String SERVICE_ID = "Grbl Controller";
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(GrblControllerService.class);
	/** Current position of the machine	 */
	private Tuple6b position;
	/** Current work position of the machine	 */
	private Tuple6b workPosition;
	/** Storage object for machine values (speed, position, etc...) */
	private MachineValueStore valueStore;
	/** Connection service*/
	private IConnectionService connectionService;
	/** GCode service*/
	private IGCodeService gcodeService;
	/** Grbl end line delimiter */
	private char endLineCharDelimiter;
	/** Buffer for incoming data	 */
	private ByteCommandBuffer incomingBuffer;
	/** The execution queue */
	private ExecutionQueue<GrblGCodeExecutionToken> executionQueue;
	/** Sending runnable */
	private GrblStreamingRunnable grblStreamingRunnable;
	/** Status polling */
	private Timer statusPollingTimer;
	/** Controller action factory*/
	private GrblActionFactory grblActionFactory;
	/** The count of unconfirmed status request */
	private int unconfirmedStatusRequest;
	/** Grbl used buffer spaces */
	private int usedGrblBuffer;
	/** Grbl configuration */
	private GrblConfiguration configuration;
	/** applicative log service */
	private IApplicativeLogService applicativeLogService;
	private GrblState grblState;
	/* CONSTANTS  */
	/** Current status command */
	private static final String CURRENT_STATUS = "?";
	/** View parameters request */
	private static final String VIEW_PARAMETERS = "$#";

	/** Response ok from Grbl */
	private static final CharSequence OK_RESPONSE = "ok";
	/** Pause command */
    public static final byte PAUSE_COMMAND = '!';
    /** Resume command */
    public static final byte RESUME_COMMAND = '~';
    /** Status command */
    public static final byte STATUS_COMMAND = '?';
    /** Reset command */
    public static final byte RESET_COMMAND = 0x18;
    /** Kill alarm command*/
    public static final String KILL_ALARM_COMMAND = "$X";
    /** Home sequence command*/
    public static final String HOME_COMMAND = "$H";
	/** Constant for Grbl used buffer value in value store */
	private static final String GRBL_USED_BUFFER = "GrblControllerUsedRxBuffer";
	/** Grbl buffer size */
	public static final int GRBL_BUFFER_SIZE = 120;


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
		endLineCharDelimiter = '\n';
		valueStore 			 = new MachineValueStore();
		position 			 = new Tuple6b();
		workPosition		 = new Tuple6b();
		incomingBuffer 		 = new ByteCommandBuffer((byte) endLineCharDelimiter);
		initValueStore();
		grblActionFactory 	 = new GrblActionFactory(this);
		configuration 		 = new GrblConfiguration();
		grblState 			 = new GrblState();
		// Initiate execution queue
		executionQueue 				= new ExecutionQueue<GrblGCodeExecutionToken>();
		ExecutorService executor 	= Executors.newSingleThreadExecutor();
		grblStreamingRunnable 		= new GrblStreamingRunnable(executionQueue, this);
		executor.execute(grblStreamingRunnable);
		LOG.info("Successfully started "+getServiceId());

	}

	private void startStatusPollingTimer(){
		statusPollingTimer = new Timer();
		TimerTask task = new TimerTask(){

			@Override
			public void run() {

				try {
					if(connectionService != null && connectionService.isConnected()){
						if(unconfirmedStatusRequest <= 0){
							refreshStatus();
						}
					}
				} catch (GkException e) {
					LOG.error(e);
				}

			}

		};
		statusPollingTimer.scheduleAtFixedRate(task, new Date(), 100);
	}
	private void stopStatusPollingTimer(){
		statusPollingTimer.cancel();
	}
	/**
	 * Initialization of the controller value store
	 * @throws GkException GkException
	 */
	protected void initValueStore() throws GkException{
		valueStore = new MachineValueStore();
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.STATE, "State", "The state of Grbl controller board", MachineState.class),
								new MachineValue<MachineState>(DefaultControllerValues.STATE, MachineState.UNDEFINED));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.MACHINE_POSITION_X, "X", "The X position of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(GrblControllerValues.MACHINE_POSITION_X, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.MACHINE_POSITION_Y, "Y", "The Y position of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(GrblControllerValues.MACHINE_POSITION_Y, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.MACHINE_POSITION_Z, "Z", "The Z position of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(GrblControllerValues.MACHINE_POSITION_Z, new BigDecimal("0.000")));

		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.WORK_POSITION_X, "Wor. X", "The X work position", BigDecimal.class),
				new MachineValue<BigDecimal>(GrblControllerValues.WORK_POSITION_X, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.WORK_POSITION_Y, "Wor. Y", "The Y work position", BigDecimal.class),
						new MachineValue<BigDecimal>(GrblControllerValues.WORK_POSITION_Y, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.WORK_POSITION_Z, "Wor. Z", "The Z work position", BigDecimal.class),
				new MachineValue<BigDecimal>(GrblControllerValues.WORK_POSITION_Z, new BigDecimal("0.000")));

		valueStore.storeValue(new MachineValueDefinition(GRBL_USED_BUFFER, "Grbl Buffer", "The space used in Grbl buffer", Integer.class), new MachineValue<Integer>(GRBL_USED_BUFFER, 0));

		valueStore.addListener(this);
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
		byteCommand.add(new Byte((byte)endLineCharDelimiter));
		connectionService.send( byteCommand );
		setUsedGrblBuffer(usedGrblBuffer + usedBufferCount);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getPosition()
	 */
	@Override
	public Point3d getPosition() throws GkException {
		return workPosition.toPoint3d();
	}

	public Point3d getMachinePosition() throws GkException {
		return position.toPoint3d();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getX()
	 */
	@Override
	public Double getX() {
		BigDecimal xPos = workPosition.getX();
		if(xPos != null){
			return xPos.doubleValue();
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getY()
	 */
	@Override
	public Double getY() {
		BigDecimal yPos = workPosition.getY();
		if(yPos != null){
			return yPos.doubleValue();
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getZ()
	 */
	@Override
	public Double getZ() {
		BigDecimal zPos = workPosition.getZ();
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
		return valueStore.getValue(name, clazz);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueType(java.lang.String)
	 */
	@Override
	public Class<?> getMachineValueType(String name) throws GkException {
		return valueStore.getControllerValueType(name);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition()
	 */
	@Override
	public List<MachineValueDefinition> getMachineValueDefinition() throws GkException {
		return valueStore.getMachineValueDefinition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#findMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition findMachineValueDefinition(String id) throws GkException {
		return valueStore.findMachineValueDefinition(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition getMachineValueDefinition(String id) throws GkException {
		return valueStore.getMachineValueDefinition(id);
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
		this.connectionService = connectionService;
		getConnectionService().addConnectionListener(this);
	}

	/**
	 * @return the connectionService
	 */
	public IConnectionService getConnectionService() {
		return connectionService;
	}

	@Override
	public void onConnectionEvent(EnumConnectionEvent event) throws GkException {
		if(event == EnumConnectionEvent.CONNECTED){
			incomingBuffer.clear();
			getConnectionService().addInputDataListener(this);
			upddateSpaceCoordinates();
			startStatusPollingTimer();
		}else if(event == EnumConnectionEvent.DISCONNECTED){
			getConnectionService().removeInputDataListener(this);
			statusPollingTimer.cancel();
			stopStatusPollingTimer();
			incomingBuffer.clear();
		}
	}
	/**
	 * Refresh the status of the remote Grbl controller
	 * @throws GkException GkException
	 */
	public void refreshStatus() throws GkException{
		getConnectionService().send( GkUtils.toBytesList(CURRENT_STATUS) );
	}

	public void upddateSpaceCoordinates() throws GkException{
		getConnectionService().send( GkUtils.toBytesList(VIEW_PARAMETERS) );
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataReceived(java.util.List)
	 */
	@Override
	public synchronized void onDataReceived(List<Byte> data) throws GkException {
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

	}

	/**
	 * Process the data received from the remote Grbl controller
	 * @param data the data to process
	 * @throws GkException GkException
	 */
	protected void handleIncomingData(String data) throws GkException{
		String trimmedData = StringUtils.trim(data);
		if(StringUtils.isNotEmpty(trimmedData)){
			if(StringUtils.equals(trimmedData, OK_RESPONSE)){
				handleOkResponse();
			}else if(StringUtils.startsWith(trimmedData, "error:")){
				handleError(trimmedData);
			}else if(StringUtils.startsWith(trimmedData, "<") && StringUtils.endsWith(trimmedData, ">")){
				handleStatusReport(trimmedData);
			}else if(StringUtils.startsWith(trimmedData, "Grbl")){
				handleGrblHeader(trimmedData);
				refreshStatus();
			}else if(StringUtils.defaultString(trimmedData).matches("\\$[0-9]*=.*")){
				handleConfigurationReading(trimmedData);
			}else if(StringUtils.defaultString(trimmedData).matches("\\[G.*\\]")){
				handleViewCoordinates(trimmedData);
			}else{
				System.out.println("Ignoring received data "+ trimmedData);
				getApplicativeLogService().warning("Ignoring received data "+ trimmedData, SERVICE_ID);
			}
		}
	}

	protected void handleConfigurationReading(String cofigurationMessage) throws GkException{
		String identifier = StringUtils.substringBefore(cofigurationMessage, "=").trim();
		String value 	  = StringUtils.substringBetween(cofigurationMessage, "=","(").trim();
		configuration.setValue(identifier, value);
		LOG.info("Updating setting '"+identifier+"' with value '"+value+"'");
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
		}
	}

	protected void handleOkResponse() throws GkException{
		if(unconfirmedStatusRequest > 0){
			unconfirmedStatusRequest = 0;
		}else{
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
	}

	protected void handleGrblHeader(String grblHeader) throws GkException{
		String[] tokens = StringUtils.split(grblHeader, " ");
		setUsedGrblBuffer(0);
		if(tokens != null && tokens.length >= 2){
			LOG.info("Grbl version is "+tokens[1]);
		}
	}
	protected void handleViewCoordinates(String message){
		String identifier = StringUtils.substringBetween(message, "[", ":");
		String valuesGroup = StringUtils.substringBetween(message, ":", "]");
		String[] values = StringUtils.split(valuesGroup, ",");

		if(values != null){
			BigDecimal x = new BigDecimal(values[0]);
			BigDecimal y = new BigDecimal(values[1]);
			BigDecimal z = new BigDecimal(values[2]);

			if(StringUtils.equals("G54", identifier)){
				grblState.setG54Offset(new Tuple6b(x,y,z));
			}
		}
	}
	protected void handleStatusReport(String statusReport) throws GkException{
		int comma = StringUtils.indexOf(statusReport, ",");
		String state = StringUtils.substring(statusReport, 1, comma);
		MachineState grblState = getGrblStateFromString (state);
		valueStore.updateValue(DefaultControllerValues.STATE, grblState);

		// Looking for MPosition
		String mpos = StringUtils.substringBetween(statusReport, "MPos:", ",WPos");
		String wpos = StringUtils.substringBetween(statusReport, "WPos:",">");
		String[] machineCoordinates = StringUtils.split(mpos,",");
		if(machineCoordinates != null){
			position.setX(new BigDecimal(machineCoordinates[0]));
			position.setY(new BigDecimal(machineCoordinates[1]));
			position.setZ(new BigDecimal(machineCoordinates[2]));
			valueStore.updateValue(GrblControllerValues.MACHINE_POSITION_X, position.getX());
			valueStore.updateValue(GrblControllerValues.MACHINE_POSITION_Y, position.getY());
			valueStore.updateValue(GrblControllerValues.MACHINE_POSITION_Z, position.getZ());
		}
		String[] workCoordinates = StringUtils.split(wpos,",");
		if(workCoordinates != null){
			workPosition.setX(new BigDecimal(workCoordinates[0]));
			workPosition.setY(new BigDecimal(workCoordinates[1]));
			workPosition.setZ(new BigDecimal(workCoordinates[2]));
			valueStore.updateValue(GrblControllerValues.WORK_POSITION_X, workPosition.getX());
			valueStore.updateValue(GrblControllerValues.WORK_POSITION_Y, workPosition.getY());
			valueStore.updateValue(GrblControllerValues.WORK_POSITION_Z, workPosition.getZ());
		}
	}

	public MachineState getState() throws GkException{
		MachineState state = MachineState.UNDEFINED;
		MachineValue<MachineState> storedState = valueStore.getValue(DefaultControllerValues.STATE, MachineState.class);
		if(storedState != null && storedState.getValue() != null){
			state = storedState.getValue();
		}
		return state;
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
	private void addEndLineCharacter(List<Byte> command){
		//command.add(new Byte((byte) endLineCharDelimiter));
		command.add(new Byte((byte) endLineCharDelimiter));
	}
	// Action related methods
	public void startHomingSequence() throws GkException{
		List<Byte> homeCommand = new ArrayList<Byte>();
		homeCommand.addAll(GkUtils.toBytesList(HOME_COMMAND));
		addEndLineCharacter(homeCommand);
		getConnectionService().send( homeCommand );
	}

	public void pauseMotion() throws GkException{
		List<Byte> pauseCommand = new ArrayList<Byte>();
		pauseCommand.add(PAUSE_COMMAND);
		getConnectionService().send( pauseCommand );
	}

	public void stopMotion() throws GkException{
		List<Byte> stopCommand = new ArrayList<Byte>();
		stopCommand.add(PAUSE_COMMAND);
		addEndLineCharacter(stopCommand);
		getConnectionService().send(stopCommand, DataPriority.IMPORTANT);
		stopCommand.clear();
		stopCommand.add(RESET_COMMAND); // TODO : it seems that resetting while in motion causes the GRBL to go back to alarm state. Wait motion to be complete before resetting
		addEndLineCharacter(stopCommand);
		getConnectionService().send(stopCommand, DataPriority.IMPORTANT);
		if(executionQueue != null){
			executionQueue.clear();
			grblStreamingRunnable.releaseBufferSpaceMutex();
		}
		setUsedGrblBuffer(0);
	}

	public void startMotion() throws GkException{
		List<Byte> startResumeCommand = new ArrayList<Byte>();
		startResumeCommand.add(RESUME_COMMAND);
		getConnectionService().send( startResumeCommand );
	}

	public void startJog(EnumGrblAxis axis, String feed, String step) throws GkException{
		String command = "G91G1"+axis.getAxisCode();
		if(axis.isNegative()){
			command+="-";
		}
		command += step;
		command += "F"+feed;
		List<Byte> lstBytes = GkUtils.toBytesList(command);
		addEndLineCharacter(lstBytes);
		lstBytes.addAll(GkUtils.toBytesList("G90"));
		addEndLineCharacter(lstBytes);

		getConnectionService().send(lstBytes, DataPriority.IMPORTANT);
	}

	public void resetZero(List<String> axes) throws GkException{
		List<Byte> lstBytes = GkUtils.toBytesList("G92"); // TODO : fix this because g28.3 is not supported in Grbl
		if(CollectionUtils.isNotEmpty(axes)){
			for (String axe : axes) {
				lstBytes.addAll(GkUtils.toBytesList(axe+"0"));
			}
		}else{
			lstBytes.addAll( GkUtils.toBytesList("X0Y0Z0"));
		}
		addEndLineCharacter(lstBytes);
		getConnectionService().send(lstBytes, DataPriority.IMPORTANT);
	}

	/**
	 * @return the usedGrblBuffer
	 */
	public int getUsedGrblBuffer() {
		return usedGrblBuffer;
	}

	/**
	 * @param usedGrblBuffer the usedGrblBuffer to set
	 * @throws GkException GkException
	 */
	public void setUsedGrblBuffer(int usedGrblBuffer) throws GkException {
		this.usedGrblBuffer = usedGrblBuffer;
		this.valueStore.updateValue(GRBL_USED_BUFFER, usedGrblBuffer);
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
				addEndLineCharacter(cfgCommand);
				getConnectionService().send( cfgCommand );
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
	public void setApplicativeLogService(
			IApplicativeLogService applicativeLogService) {
		this.applicativeLogService = applicativeLogService;
	}


	/** (inheritDoc)
	 * @see org.goko.grbl.controller.IGrblControllerService#getGrblState()
	 */
	@Override
	public GrblState getGrblState() {
		return grblState;
	}

}
