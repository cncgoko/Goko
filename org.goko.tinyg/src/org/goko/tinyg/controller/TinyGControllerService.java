package org.goko.tinyg.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.vecmath.Point3d;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.GkUtils;
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.provider.GCodeExecutionQueue;
import org.goko.core.gcode.bean.provider.GCodeStreamedExecutionQueue;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.tinyg.controller.configuration.TinyGGroupSettings;
import org.goko.tinyg.controller.configuration.TinyGSetting;
import org.goko.tinyg.controller.events.ConfigurationUpdateEvent;
import org.goko.tinyg.controller.values.MachineValueStore;
import org.goko.tinyg.json.TinyGJsonUtils;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * Implementation of the TinyG controller
 *
 * @author PsyKo
 *
 */
public class TinyGControllerService extends EventDispatcher implements IControllerService, IConnectionDataListener, IConnectionListener {
	static final GkLog LOG = GkLog.getLogger(TinyGControllerService.class);
	/**  Service ID */
	public static final String SERVICE_ID = "TinyG Controller";
	private static final String JOG_SIMULATION_DISTANCE = "1000.0";
	/** Stored configuration */
	private TinyGConfiguration configuration;
	/** Connection service */
	private IConnectionService connectionService;
	/** GCode service */
	private IGCodeService gcodeService;
	/** Buffer for incoming data	 */
	private ByteCommandBuffer incomingBuffer;
	/** Current position of the machine	 */
	private Tuple6b position;
	/** Current velocity	 */
	private BigDecimal velocity;
	/** Line broker for sent commands */
	private byte lineBroker;
	/** The sending thread	 */
	private GCodeSendingRunnable currentSendingRunnable;
	/** The tread used to handle incoming data */
	private ExecutorService incomingDataThread;
	/** The current StreamStatus */
	private GCodeStreamedExecutionQueue executionQueue;
	/** Action factory */
	private TinyGActionFactory actionFactory;
	/** Storage object for machine values (speed, position, etc...) */
	private MachineValueStore valueStore;
	/** The count of available buffer in the TinyG board*/
	private int availableBuffer;

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
		configuration 			= new TinyGConfiguration();
		incomingBuffer  		= new ByteCommandBuffer((byte) '\n');
		position 				= new Tuple6b();
		lineBroker				= (byte) '\n';
		actionFactory 			= new TinyGActionFactory(this);
		incomingDataThread 		= Executors.newSingleThreadExecutor();

		valueStore = new MachineValueStore();
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.STATE, "State", "The state of TinyG controller board", MachineState.class),
								new MachineValue<MachineState>(DefaultControllerValues.STATE, MachineState.UNDEFINED));
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.POSITION, "Pos", "The position of the machine", Point3d.class),
				new MachineValue<Point3d>(DefaultControllerValues.POSITION, new Point3d()));
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.POSITION_X, "X", "The X position of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(DefaultControllerValues.POSITION_X, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.POSITION_Y, "Y", "The Y position of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(DefaultControllerValues.POSITION_Y, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.POSITION_Z, "Z", "The Z position of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(DefaultControllerValues.POSITION_Z, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.POSITION_A, "A", "The A position of the machine", BigDecimal.class),
				new MachineValue<BigDecimal>(DefaultControllerValues.POSITION_A, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.VELOCITY, "Velocity", "The current velocity of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(DefaultControllerValues.VELOCITY, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.SPINDLE_STATE, "Spindle", "The current state of the spindle", Boolean.class),
				new MachineValue<Boolean>(DefaultControllerValues.SPINDLE_STATE, new Boolean(false)));

		valueStore.addListener(this);
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getPosition()
	 */
	@Override
	public Point3d getPosition() throws GkException {
		return new Point3d( position.getX().doubleValue(),position.getY().doubleValue(),position.getZ().doubleValue());
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#executeGCode(org.goko.core.gcode.bean.IGCodeProvider)
	 */
	@Override
	public GCodeExecutionQueue executeGCode(IGCodeProvider gcodeProvider) throws GkException{
		if(!getConnectionService().isConnected()){
			throw new GkFunctionalException("Cannot send command. Connection service is not connected");
		}
		executionQueue = new GCodeStreamedExecutionQueue(gcodeProvider);
		ExecutorService executor 	= Executors.newSingleThreadExecutor();
		currentSendingRunnable 	= new GCodeSendingRunnable(executionQueue, this);
		executionQueue.start();
		executor.execute(currentSendingRunnable);
		return executionQueue;
	}

	public void sendTogether(List<GCodeCommand> commands) throws GkException{
		StringBuffer commandBuffer = new StringBuffer();
		for (GCodeCommand gCodeCommand : commands) {
			JsonValue jsonCommand = TinyGControllerUtility.toJson(gCodeCommand);
			commandBuffer.append(jsonCommand.toString() + (char)getLineBroker() );
		}
		getConnectionService().send( GkUtils.toBytesList( commandBuffer.toString() ));
	}

	@Override
	public boolean isReadyForFileStreaming() throws GkException {
		return MachineState.READY.equals(getState())
			|| MachineState.PROGRAM_END.equals(getState())
			|| MachineState.PROGRAM_STOP.equals(getState());
	}
	/**
	 * Refresh the TinyG configuration by sending all the Groups as empty groups
	 * Update is done by event handling
	 *
	 * @throws GkException GkException
	 */
	public void refreshConfiguration() throws GkException{
		for(TinyGGroupSettings group : configuration.getGroups()){
			JsonObject groupEmpty = new JsonObject();
			groupEmpty.add(group.getGroupIdentifier(), StringUtils.EMPTY);
			getConnectionService().send( buildByteList(groupEmpty.toString()));
		}
	}
	public void refreshStatus() throws GkException{
		JsonObject statusQuery = new JsonObject();
		statusQuery.add("sr", StringUtils.EMPTY);
		getConnectionService().send( buildByteList(statusQuery.toString()) );
		updateQueueReport();
	}

	protected void updateQueueReport()throws GkException{
		JsonObject queueQuery = new JsonObject();
		queueQuery.add("qr", StringUtils.EMPTY);
		getConnectionService().send( buildByteList(queueQuery.toString()) );
	}

	/**
	 * Return a copy of the current stored configuration
	 * @return a copy of {@link TinyGConfiguration}
	 * @throws GkException GkException
	 */
	public TinyGConfiguration getConfiguration() throws GkException{
		return TinyGControllerUtility.getConfigurationCopy(configuration);
	}

	/**
	 * Returned the available {@link IConnectionService}
	 * @return the connectionService
	 */
	public IConnectionService getConnectionService() {
		return connectionService;
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
	 * (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataReceived(java.util.List)
	 */
	@Override
	public void onDataReceived(List<Byte> data) throws GkException {
		incomingBuffer.addAll(data);
		//LOG.info("Adding data to ByteBuffer "+GkUtils.toStringReplaceCRLF(data));

		incomingDataThread.execute( new Runnable() {
			@Override
			public void run() {
				try {
					handleIncomingCommands();
				} catch (GkException e) {
					LOG.error(e);
				}
			}
		});
	}
	/**
	 * Handle the received commands
	 * @throws GkException GkException
	 */
	protected void handleIncomingCommands() throws GkException{
		while(incomingBuffer.hasNext()){
			List<Byte> command = incomingBuffer.unstackNextCommand();
			String stringCommand = GkUtils.toString(command).trim();
			LOG.info("Handling command " + GkUtils.toStringReplaceCRLF(command));
			if(TinyGJsonUtils.isJsonFormat(stringCommand)){
				JsonObject response = null;
				try{
					response = JsonObject.readFrom(stringCommand);
				}catch(Exception e){
					LOG.error("Error while parsing JSon for string '"+stringCommand+"'"+System.lineSeparator()+e.getMessage());

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
		System.err.println("End of handleIncomingCommands");
	}
	/**
	 * Verify the response using the header
	 * @param jsonValue the parsed response
	 * @throws GkException GkException
	 */
	private void handleResponseFooter(JsonValue jsonValue) throws GkException {
		//TODO
		if(currentSendingRunnable != null){
			currentSendingRunnable.ack();
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
			}else{
				handleConfigurationModification(responseEnvelope);
			}
		}
	}

	/**
	 * Handling configuration response from TinyG
	 * @param jsonValue
	 */
	private void handleConfigurationModification(JsonObject responseEnvelope) throws GkException {
		TinyGControllerUtility.handleConfigurationModification(configuration, responseEnvelope);
		notifyListeners(new ConfigurationUpdateEvent());
	}

	private void handleQueueReport(JsonValue queueReport) throws GkException {
		this.availableBuffer = queueReport.asInt();
		if(currentSendingRunnable != null){
			currentSendingRunnable.ackBuffer();
		}
	}
	/**
	 * Handling status report from TinyG
	 * @param jsonValue
	 */
	private void handleStatusReport(JsonValue statusReport) throws GkException {
		if(statusReport.isObject()){
			JsonObject statusReportObject = (JsonObject) statusReport;
			this.position = TinyGControllerUtility.updatePosition(position, statusReportObject);

			valueStore.updateValue(DefaultControllerValues.POSITION, getPosition());
			valueStore.updateValue(DefaultControllerValues.POSITION_X, position.getX().setScale(3, BigDecimal.ROUND_HALF_EVEN));
			valueStore.updateValue(DefaultControllerValues.POSITION_Y, position.getY().setScale(3, BigDecimal.ROUND_HALF_EVEN));
			valueStore.updateValue(DefaultControllerValues.POSITION_Z, position.getZ().setScale(3, BigDecimal.ROUND_HALF_EVEN));
			valueStore.updateValue(DefaultControllerValues.POSITION_A, position.getA().setScale(3, BigDecimal.ROUND_HALF_EVEN));
			/*
			 * Update velocity
			 */
			JsonValue velocityReport = statusReportObject.get(TinyGJsonUtils.STATUS_REPORT_VELOCITY);
			if(velocityReport != null){
				velocity = velocityReport.asBigDecimal().setScale(3, BigDecimal.ROUND_HALF_EVEN);
				valueStore.updateValue(DefaultControllerValues.VELOCITY, velocity);
			}
			/*
			 * Update state
			 */
			JsonValue statReport = statusReportObject.get(TinyGJsonUtils.STATUS_REPORT_STATE);
			if(statReport != null){
				setState(TinyGControllerUtility.getState(statReport.asInt()));
			}
		}
	}

	/**
	 * Handling GCode response from TinyG
	 * @param jsonValue
	 * @throws GkTechnicalException
	 */
	private void handleGCodeResponse(JsonValue jsonValue) throws GkException {
		if(executionQueue != null){
			String 			receivedCommand = jsonValue.asString();
			GCodeCommand 	parsedCommand 	= getGcodeService().parseCommand(receivedCommand);
			executionQueue.confirmCommand(parsedCommand);
		}
		/*if(executionQueue != null && executionQueue.hasPendingCommand()){
			GCodeCommand pendingCommand = executionQueue.getNextPendingCommand();
			GCodeCommand parsedCommand = getGcodeService().parseCommand(receivedCommand);
			if( ObjectUtils.equals(pendingCommand, parsedCommand)){
				GCodeCommand command = executionQueue.unstackNextPendingCommand();
				executionQueue.addAcknowledgedCommand(command);
				command.setState(new GCodeCommandState(GCodeCommandState.EXECUTED));
				notifyListeners(new StreamStatusUpdate(executionQueue));
			}else{
				LOG.debug("  /!\\  Cannot confirm GCode command "+receivedCommand);
			}
		}*/
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
			getConnectionService().addInputDataListener(this);
			LOG.info("Registering "+getClass()+" as input data listener...");
			refreshStatus();
			refreshConfiguration();
		}
	}

	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineValueUpdate(MachineValueUpdateEvent evt){
		notifyListeners(evt);
	}

	public void updateConfiguration(TinyGConfiguration cfg) throws GkException{
		for(TinyGGroupSettings group: cfg.getGroups()){
			if(StringUtils.equals(group.getGroupIdentifier(), TinyGConfiguration.SYSTEM_SETTINGS)){
				for(TinyGSetting setting : group.getSettings()){
					JsonObject jsonSetting = TinyGJsonUtils.toJson(setting);
					if(jsonSetting != null){
						getConnectionService().send( GkUtils.toBytesList(jsonSetting.toString() + "\r\n") );
					}
				}
			}else{
				JsonObject jsonGroup = TinyGJsonUtils.toCompleteJson(group);
				getConnectionService().send( GkUtils.toBytesList(jsonGroup.toString() + "\r\n") );
			}

		}

	}

	/**
	 * @return the lineBroker
	 */
	public byte getLineBroker() {
		return lineBroker;
	}

	/**
	 * @param lineBroker the lineBroker to set
	 */
	public void setLineBroker(byte lineBroker) {
		this.lineBroker = lineBroker;
	}

	/**
	 * @return the gcodeService
	 */
	public synchronized IGCodeService getGcodeService() {
		return gcodeService;
	}

	/**
	 * @param gCodeService the gcodeService to set
	 */
	public synchronized void setGCodeService(IGCodeService gCodeService) {
		this.gcodeService = gCodeService;
	}
	/**
	 * Build the byte list for the requested command, and automatically add the line broker byte
	 * @param command the command to send
	 * @return
	 * @throws GkException GkException
	 */
	protected List<Byte> buildByteList(String command) throws GkException{
		List<Byte> byteList = GkUtils.toBytesList(command);
		byteList.add(getLineBroker());
		return byteList;
	}


	public MachineState getState() throws GkException {
		MachineState state = MachineState.UNDEFINED;
		MachineValue<MachineState> storedState = valueStore.getValue(DefaultControllerValues.STATE, MachineState.class);
		if(storedState != null && storedState.getValue() != null){
			state = storedState.getValue();
		}
		return state;
	}

	public void setState(MachineState state) throws GkException {
		valueStore.updateValue(DefaultControllerValues.STATE, state);
	}

	public boolean isSpindleOn() throws GkException{
		return valueStore.getBooleanValue(DefaultControllerValues.SPINDLE_STATE).getValue() == true;
	}

	public boolean isSpindleOff() throws GkException{
		return valueStore.getBooleanValue(DefaultControllerValues.SPINDLE_STATE).getValue() == false;
	}

	/**
	 * Initiate TinyG homing sequence
	 * @throws GkException GkException
	 */
	public void startHomingSequence() throws GkException{
		String 		homingCommand 		= "G28.2 X0 Y0";// Z0";
		List<Byte> 	homingCommandBytes 	= GkUtils.toBytesList(homingCommand);
		homingCommandBytes.add(getLineBroker());
		getConnectionService().send( homingCommandBytes );
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getControllerAction(java.lang.String)
	 */
	@Override
	public IGkControllerAction getControllerAction(String actionId) throws GkException {
		IGkControllerAction action = actionFactory.findAction(actionId);
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
		return actionFactory.findAction(actionId) != null;
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

	@Override
	public List<MachineValueDefinition> getMachineValueDefinition() throws GkException {
		return valueStore.getMachineValueDefinition();
	}
	@Override
	public MachineValueDefinition getMachineValueDefinition(String id) throws GkException {
		return valueStore.getMachineValueDefinition(id);
	}

	/* ************************************************
	 *  CONTROLLER ACTIONS
	 * ************************************************/


	public void pauseMotion() throws GkException{
		List<Byte> pauseCommand = new ArrayList<Byte>();
		pauseCommand.add(TinyGConstants.FEED_HOLD);
		pauseCommand.add(getLineBroker());
		getConnectionService().send(pauseCommand, DataPriority.IMPORTANT);
	}

	public void resumeMotion() throws GkException{
		List<Byte> resumeCommand = new ArrayList<Byte>();
		resumeCommand.add(TinyGConstants.CYCLE_START);
		resumeCommand.add(getLineBroker());
		getConnectionService().send(resumeCommand, DataPriority.IMPORTANT);
	}

	public void stopMotion() throws GkException{
		List<Byte> stopCommand = new ArrayList<Byte>();
		stopCommand.add(TinyGConstants.FEED_HOLD);
		stopCommand.add(TinyGConstants.QUEUE_FLUSH);
		stopCommand.add(getLineBroker());
		getConnectionService().send(stopCommand, DataPriority.IMPORTANT);
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
		lstBytes.add(getLineBroker());
		getConnectionService().send(lstBytes, DataPriority.IMPORTANT);
	}

	public void startJog(EnumTinyGAxis axis, String feed) throws GkException{
		String command = "G1"+axis.getAxisCode();
		if(axis.isNegative()){
			command+="-";
		}
		command += JOG_SIMULATION_DISTANCE;
		command += "F"+feed;
		List<Byte> lstBytes = GkUtils.toBytesList(command);
		lstBytes.add(getLineBroker());

		getConnectionService().send(lstBytes, DataPriority.IMPORTANT);
	}

	public void turnSpindleOn() throws GkException{
		List<Byte> lstBytes = GkUtils.toBytesList("M3");
		lstBytes.add(getLineBroker());
		getConnectionService().send(lstBytes, DataPriority.IMPORTANT);
	}
	public void turnSpindleOff() throws GkException{
		List<Byte> lstBytes = GkUtils.toBytesList("M5");
		lstBytes.add(getLineBroker());
		getConnectionService().send(lstBytes, DataPriority.IMPORTANT);
	}

	/**
	 * @return the availableBuffer
	 */
	public int getAvailableBuffer() {
		return availableBuffer;
	}

	@Override
	public void cancelFileSending() throws GkException {
		if(executionQueue != null){
			executionQueue.stop();
		}
		stopMotion();
	}

	public boolean requireAcknowledgement(GCodeCommand currentCommand) {
		return false;
	}

}
