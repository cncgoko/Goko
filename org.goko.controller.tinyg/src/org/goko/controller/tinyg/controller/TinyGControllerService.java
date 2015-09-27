package org.goko.controller.tinyg.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.controller.tinyg.controller.bean.TinyGExecutionError;
import org.goko.controller.tinyg.controller.configuration.TinyGAxisSettings;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.controller.tinyg.controller.configuration.TinyGConfigurationValue;
import org.goko.controller.tinyg.controller.configuration.TinyGGroupSettings;
import org.goko.controller.tinyg.controller.prefs.TinyGPreferences;
import org.goko.controller.tinyg.controller.probe.ProbeCallable;
import org.goko.controller.tinyg.controller.topic.TinyGExecutionErrorTopic;
import org.goko.controller.tinyg.json.TinyGJsonUtils;
import org.goko.controller.tinyg.service.ITinyGControllerFirmwareService;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.ApplicativeLogEvent;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.event.EventBrokerUtils;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.connection.IConnectionService;
import org.goko.core.connection.serial.ISerialConnection;
import org.goko.core.connection.serial.ISerialConnectionService;
import org.goko.core.connection.serial.SerialParameter;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.gcode.bean.execution.ExecutionQueue;
import org.goko.core.gcode.bean.provider.GCodeExecutionToken;
import org.goko.core.gcode.service.IGCodeExecutionMonitorService;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
import org.osgi.service.event.EventAdmin;

import com.eclipsesource.json.JsonObject;

/**
 * Implementation of the TinyG controller
 *
 * @author PsyKo
 *
 */
public class TinyGControllerService extends EventDispatcher implements ITinyGControllerFirmwareService, ITinygControllerService{
	static final GkLog LOG = GkLog.getLogger(TinyGControllerService.class);
	/**  Service ID */
	public static final String SERVICE_ID = "TinyG Controller";
//	private static final String JOG_SIMULATION_DISTANCE = "10000.0";
	private static final String JOG_SIMULATION_DISTANCE = "1";
	private static final double JOG_SIMULATION_DISTANCE_DOUBLE = 10000.0;

	/** Stored configuration */
	private TinyGConfiguration configuration;
	/** Connection service */
	private ISerialConnectionService connectionService;
	/** GCode service */
	private IGCodeService gcodeService;
	/** The sending thread	 */
	private GCodeSendingRunnable currentSendingRunnable;
	/** The current execution queue */
	private ExecutionQueue<TinyGExecutionToken> executionQueue;
	/** The monitor service */
	private IGCodeExecutionMonitorService monitorService;
	/** Action factory */
	private TinyGActionFactory actionFactory;
	/** Storage object for machine values (speed, position, etc...) */
	private TinyGState tinygState;
	/** Waiting probe result */
	private ProbeCallable futureProbeResult;
	/** Communicator */
	private TinyGCommunicator communicator;
	/** Applicative log service */
	private IApplicativeLogService applicativeLogService;
	/** Event admin service */
	private EventAdmin eventAdmin;
	private TinyGJoggingRunnable jogRunnable;
	
	public TinyGControllerService() {
		communicator = new TinyGCommunicator(this);	
		tinygState = new TinyGState();
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
		configuration 			= new TinyGConfiguration();
		actionFactory 			= new TinyGActionFactory(this);
		
		tinygState.addListener(this);

		TinyGPreferences.getInstance();
		
		// Initiate execution queue
		executionQueue 				= new ExecutionQueue<TinyGExecutionToken>();
		
		ExecutorService executor 	= Executors.newSingleThreadExecutor();
		currentSendingRunnable 		= new GCodeSendingRunnable(executionQueue, this);				
		executor.execute(currentSendingRunnable);
		
		jogRunnable = new TinyGJoggingRunnable(this, communicator);
		ExecutorService jogExecutor 	= Executors.newSingleThreadExecutor();
		jogExecutor.execute(jogRunnable);
		
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		if(currentSendingRunnable != null){
			currentSendingRunnable.stop();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getPosition()
	 */
	@Override
	public Tuple6b getPosition() throws GkException {
		return tinygState.getWorkPosition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#executeGCode(org.goko.core.gcode.bean.IGCodeProvider)
	 */
	@Override
	public GCodeExecutionToken executeGCode(IGCodeProvider gcodeProvider) throws GkException{
		if(!isReadyForFileStreaming()){
			throw new GkFunctionalException("TNG-003");
		}
		checkExecutionControl();
		checkVerbosity(configuration);
		updateQueueReport();
		TinyGExecutionToken token = new TinyGExecutionToken(gcodeProvider);
		token.setMonitorService(getMonitorService());
		executionQueue.add(token);

		return token;
	}

	private void checkVerbosity(TinyGConfiguration cfg) throws GkException{
		BigDecimal jsonVerbosity = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.JSON_VERBOSITY, BigDecimal.class);
		if(!ObjectUtils.equals(jsonVerbosity, TinyGConfigurationValue.JSON_VERBOSITY_VERBOSE)){
			throw new GkFunctionalException("TNG-007");
		}

		BigDecimal qrVerbosity = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.QUEUE_REPORT_VERBOSITY, BigDecimal.class);
		
		if(isPlannerBufferSpaceCheck()){
			if(ObjectUtils.equals(qrVerbosity, TinyGConfigurationValue.QUEUE_REPORT_OFF)){
				throw new GkFunctionalException("TNG-002");
			}
		}
	}
	private void checkExecutionControl() throws GkException{
		BigDecimal flowControl = configuration.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_FLOW_CONTROL, BigDecimal.class);

		// We always need to use flow control
		if(ObjectUtils.equals(flowControl, TinyGConfigurationValue.FLOW_CONTROL_OFF)){				
			throw new GkFunctionalException("TNG-001");				
		}
		
		// Make sure the current connection use the same flow control
		ISerialConnection connexion = getConnectionService().getCurrentConnection();
		TinyGConfiguration cfg = getConfiguration();
		BigDecimal configuredFlowControl = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_FLOW_CONTROL, BigDecimal.class);
		
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
	
	public void send(GCodeCommand gCodeCommand) throws GkException{
		communicator.send(gCodeCommand);
	}

	public void sendTogether(List<GCodeCommand> commands) throws GkException{
		for (GCodeCommand gCodeCommand : commands) {
			communicator.send(gCodeCommand);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#isReadyForFileStreaming()
	 */
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
	@Override
	public void refreshConfiguration() throws GkException{
		for(TinyGGroupSettings group : configuration.getGroups()){
			JsonObject groupEmpty = new JsonObject();
			groupEmpty.add(group.getGroupIdentifier(), StringUtils.EMPTY);
			communicator.send(GkUtils.toBytesList(groupEmpty.toString()));
		}
	}
	public void refreshStatus() throws GkException{
		JsonObject statusQuery = new JsonObject();
		statusQuery.add("sr", StringUtils.EMPTY);
		communicator.send(GkUtils.toBytesList(statusQuery.toString()));
		updateQueueReport();
	}

	protected void updateQueueReport()throws GkException{
		JsonObject queueQuery = new JsonObject();
		queueQuery.add("qr", StringUtils.EMPTY);
		communicator.send(GkUtils.toBytesList(queueQuery.toString()));
	}

	/**
	 * Update the current GCodeContext with the given one
	 * @param updatedGCodeContext the updated GCodeContext
	 */
	protected void updateCurrentGCodeContext(GCodeContext updatedGCodeContext){
		GCodeContext current = tinygState.getGCodeContext();
		if(updatedGCodeContext.getPosition() != null){
			current.setPosition(updatedGCodeContext.getPosition());
		}
		if(updatedGCodeContext.getCoordinateSystem() != null){
			current.setCoordinateSystem(updatedGCodeContext.getCoordinateSystem());
		}
		if(updatedGCodeContext.getMotionMode() != null){
			current.setMotionMode(updatedGCodeContext.getMotionMode());
		}
		if(updatedGCodeContext.getMotionType() != null){
			current.setMotionType(updatedGCodeContext.getMotionType());
		}
		if(updatedGCodeContext.getFeedrate() != null){
			current.setFeedrate(updatedGCodeContext.getFeedrate());
		}
		if(updatedGCodeContext.getUnit() != null){
			current.setUnit(updatedGCodeContext.getUnit());
		}
		if(updatedGCodeContext.getDistanceMode() != null){
			current.setDistanceMode(updatedGCodeContext.getDistanceMode());
		}
		if(updatedGCodeContext.getPlane() != null){
			current.setPlane(updatedGCodeContext.getPlane());
		}
		if(updatedGCodeContext.getToolNumber() != null){
			current.setToolNumber(updatedGCodeContext.getToolNumber());
		}
		tinygState.setGCodeContext(current);
	}

	/**
	 * Return a copy of the current stored configuration
	 * @return a copy of {@link TinyGConfiguration}
	 * @throws GkException GkException
	 */
	@Override
	public TinyGConfiguration getConfiguration() throws GkException{
		return TinyGControllerUtility.getConfigurationCopy(configuration);
	}

	/**
	 * Returned the available {@link IConnectionService}
	 * @return the connectionService
	 */
	public ISerialConnectionService getConnectionService() {
		return connectionService;
	}

	/**
	 * @param connectionService the connectionService to set
	 * @throws GkException GkException
	 */
	public void setConnectionService(ISerialConnectionService connectionService) throws GkException {
		this.connectionService = connectionService;
		communicator.setConnectionService(connectionService);
	}

	/**
	 * Handling GCode response from TinyG
	 * @param jsonValue
	 * @throws GkTechnicalException
	 */
	protected void handleGCodeResponse(String receivedCommand, TinyGStatusCode status) throws GkException {
		if(executionQueue.getCurrentToken() != null){
			if(status == TinyGStatusCode.TG_OK){
				GCodeCommand 	parsedCommand 	= getGcodeService().parseCommand(receivedCommand, getCurrentGCodeContext());
				executionQueue.getCurrentToken().markAsConfirmed(parsedCommand);
				this.currentSendingRunnable.confirmCommand();
			}else{
				handleError(status, receivedCommand);
			}
		}
	}

	protected void handleError(TinyGStatusCode status, String receivedCommand) throws GkException {
		String message = StringUtils.EMPTY;
		if(status == null){
			message = " Unknown error status";
		}else{
			message = " Error status returned : "+status.getValue() +" - "+status.getLabel();
		}
		
		if(executionQueue != null){
			TinyGExecutionToken currentToken = executionQueue.getCurrentToken();
			
			if(currentToken != null && currentToken.getSentCommandCount() > 0){
				 // Error occured during GCode programm execution, let's give the source command
				GCodeCommand command = currentToken.markNextCommandAsError();
				message += " on command ["+command.getStringCommand()+"]";
			}
			// Security pause
			pauseMotion();
			// Still confirm that the command was received
			this.currentSendingRunnable.confirmCommand();
			EventBrokerUtils.send(eventAdmin, new TinyGExecutionErrorTopic(), new TinyGExecutionError("Error reported durring execution", "Execution was paused after TinyG reported an error. You can resume, or stop the execution at your own risk.", message));			
		}
		
		LOG.error(message);
		applicativeLogService.log(ApplicativeLogEvent.LOG_ERROR, message, "TinyG");		
	}
	
	protected void error(String message){
		LOG.error(message);
		applicativeLogService.log(ApplicativeLogEvent.LOG_ERROR, message, "TinyG Communicator");
	}
	
	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineValueUpdate(MachineValueUpdateEvent evt){
		notifyListeners(evt);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerFirmwareService#setConfiguration(org.goko.controller.tinyg.controller.configuration.TinyGConfiguration)
	 */
	@Override
	public void setConfiguration(TinyGConfiguration cfg) throws GkException{
		this.configuration = TinyGControllerUtility.getConfigurationCopy(cfg);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerFirmwareService#updateConfiguration(org.goko.controller.tinyg.controller.configuration.TinyGConfiguration)
	 */
	@Override
	public void updateConfiguration(TinyGConfiguration cfg) throws GkException {
		checkVerbosity(cfg);
		// Let's only change the new values
		// The new value will be applied directly to TinyG. The changes will be reported in the data model when TinyG sends them back for confirmation.
		TinyGConfiguration diffConfig = TinyGControllerUtility.getDifferentialConfiguration(getConfiguration(), cfg);
		
		// TODO : perform sending in communicator
		for(TinyGGroupSettings group: diffConfig.getGroups()){
			JsonObject jsonGroup = TinyGJsonUtils.toCompleteJson(group);
			if(jsonGroup != null){					
				communicator.send( GkUtils.toBytesList(jsonGroup.toString()) );
			}
		}
	}


	/**
	 * @return the gcodeService
	 */
	public IGCodeService getGcodeService() {
		return gcodeService;
	}

	/**
	 * @param gCodeService the gcodeService to set
	 */
	public void setGCodeService(IGCodeService gCodeService) {
		this.gcodeService = gCodeService;
		this.communicator.setGcodeService(gCodeService);
	}

	@Override
	public MachineState getState() throws GkException {
		return tinygState.getState();
	}

	public void setState(MachineState state) throws GkException {
		tinygState.setState(state);
	}

	public boolean isSpindleOn() throws GkException{
		return tinygState.isSpindleOn();
	}

	public boolean isSpindleOff() throws GkException{
		return tinygState.isSpindleOff();
	}

	/**
	 * Initiate TinyG homing sequence
	 * @throws GkException GkException
	 */
	public void startHomingSequence() throws GkException{
		String 		homingCommand 		= "G28.2";
		if(TinyGPreferences.getInstance().isHomingEnabledAxisX()){
			homingCommand += " X0";
		}
		if(TinyGPreferences.getInstance().isHomingEnabledAxisY()){
			homingCommand += " Y0";
		}
		if(TinyGPreferences.getInstance().isHomingEnabledAxisZ()){
			homingCommand += " Z0";
		}
		if(TinyGPreferences.getInstance().isHomingEnabledAxisA()){
			homingCommand += " A0";
		}		
		communicator.send(GkUtils.toBytesList(homingCommand));
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getControllerAction(java.lang.String)
	 */
	@Override
	public IGkControllerAction getControllerAction(String actionId) throws GkException {
		IGkControllerAction action = actionFactory.findAction(actionId);
		if(action == null){
			throw new GkFunctionalException("TNG-004", actionId, getServiceId());
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
		return tinygState.getValue(name, clazz);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueType(java.lang.String)
	 */
	@Override
	public Class<?> getMachineValueType(String name) throws GkException {
		return tinygState.getControllerValueType(name);
	}

	@Override
	public List<MachineValueDefinition> getMachineValueDefinition() throws GkException {
		return tinygState.getMachineValueDefinition();
	}
	@Override
	public MachineValueDefinition getMachineValueDefinition(String id) throws GkException {
		return tinygState.getMachineValueDefinition(id);
	}
	@Override
	public MachineValueDefinition findMachineValueDefinition(String id) throws GkException {
		return tinygState.findMachineValueDefinition(id);
	}


	/* ************************************************
	 *  CONTROLLER ACTIONS
	 * ************************************************/


	public void pauseMotion() throws GkException{
		communicator.send(GkUtils.toBytesList(TinyG.FEED_HOLD));
		if(executionQueue != null){
			executionQueue.setPaused(true);
		}
	}

	public void resumeMotion() throws GkException{
		communicator.sendImmediately(GkUtils.toBytesList(TinyG.CYCLE_START));
		if(executionQueue != null){
			executionQueue.setPaused(false);
		}
	}

	public void stopMotion() throws GkException{
		getConnectionService().clearOutputBuffer();
		communicator.sendImmediately(GkUtils.toBytesList(TinyG.FEED_HOLD, TinyG.QUEUE_FLUSH));


		if(executionQueue != null){
			executionQueue.clear();
		}
		if(currentSendingRunnable != null){
			currentSendingRunnable.stop();
		}
		// Force a queue report update
		//	updateQueueReport();
		//	this.resetAvailableBuffer();
	}

	public void resetZero(List<String> axes) throws GkException{
		List<Byte> lstBytes = GkUtils.toBytesList("G28.3");
		if(CollectionUtils.isNotEmpty(axes)){
			for (String axe : axes) {
				lstBytes.addAll(GkUtils.toBytesList(axe+"0"));
			}
		}else{
			lstBytes.addAll( GkUtils.toBytesList("X0Y0Z0"));
		}
		communicator.send(lstBytes);
	}
	
	public void turnSpindleOn() throws GkException{
		communicator.send(GkUtils.toBytesList("M3"));
	}
	
	public void turnSpindleOff() throws GkException{
		communicator.send(GkUtils.toBytesList("M5"));
	}

	/**
	 * @return the availableBuffer
	 * @throws GkException
	 */
	public int getAvailableBuffer() throws GkException {
		return tinygState.getAvailableBuffer();
	}
	/**
	 * @param availableBuffer the availableBuffer to set
	 * @throws GkException exception
	 */
	public void setAvailableBuffer(int availableBuffer) throws GkException {
		tinygState.updateValue(TinyG.TINYG_BUFFER_COUNT, availableBuffer);
		if(currentSendingRunnable != null){
			currentSendingRunnable.notifyBufferSpace();
		}
	}

	public void resetAvailableBuffer() throws GkException {
		setAvailableBuffer(28);
	}

	@Override
	public void cancelFileSending() throws GkException {
		stopMotion();
	}


	@Override
	public String getMinimalSupportedFirmwareVersion() throws GkException {
		return "435.10";
	}

	@Override
	public String getMaximalSupportedFirmwareVersion() throws GkException {
		return "440.18";
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#probe(org.goko.core.controller.bean.EnumControllerAxis, double, double)
	 */
	@Override
	public Future<ProbeResult> probe(EnumControllerAxis axis, double feedrate, double maximumPosition) throws GkException {
		futureProbeResult = new ProbeCallable();
		String strCommand = "G38.2 "+axis.getAxisCode()+String.valueOf(maximumPosition)+" F"+feedrate;
		IGCodeProvider command = gcodeService.parse(strCommand, getCurrentGCodeContext());
		executeGCode(command);
		return Executors.newSingleThreadExecutor().submit(futureProbeResult);
	}

	protected void handleProbeResult(boolean probed, Tuple6b position){
		if(this.futureProbeResult != null){
			ProbeResult probeResult = new ProbeResult();
			probeResult.setProbed(probed);
			probeResult.setProbedPosition(position);
			this.futureProbeResult.setProbeResult(probeResult);
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#moveToAbsolutePosition(org.goko.core.gcode.bean.Tuple6b)
	 */
	@Override
	public void moveToAbsolutePosition(Tuple6b position) throws GkException {
		String cmd = "G1F800";
		if(position.getX() != null){			
			cmd += "X"+getPositionAsString(position.getX());
		}
		if(position.getY() != null){
			cmd += "Y"+getPositionAsString(position.getY());
		}
		if(position.getZ() != null){
			cmd += "Z"+getPositionAsString(position.getZ());
		}
		IGCodeProvider command = gcodeService.parse(cmd, getCurrentGCodeContext());
		executeGCode(command);
	}

	protected String getPositionAsString(BigDecimalQuantity<Length> q) throws GkException{
		return GokoPreference.getInstance().format(q.to(getCurrentUnit()), true, false);
	}
	/**
	 * @param applicativeLogService the IApplicativeLogService to set
	 */
	public void setApplicativeLogService(IApplicativeLogService applicativeLogService) {
		this.applicativeLogService = applicativeLogService;
		this.communicator.setApplicativeLogService(applicativeLogService);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getCurrentGCodeContext()
	 */
	@Override
	public GCodeContext getCurrentGCodeContext() throws GkException {
		return tinygState.getGCodeContext();
	}

	public void setVelocity(BigDecimal velocity) throws GkException {
		tinygState.setVelocity(velocity);
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getX()
	 */
	@Override
	public BigDecimalQuantity<Length> getX() throws GkException {
		return tinygState.getX();
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getY()
	 */
	@Override
	public BigDecimalQuantity<Length> getY() throws GkException {
		return tinygState.getY();
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getZ()
	 */
	@Override
	public BigDecimalQuantity<Length> getZ() throws GkException {
		return tinygState.getZ();
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IFourAxisControllerAdapter#getA()
	 */
	@Override
	public BigDecimalQuantity<Angle> getA() throws GkException {
		return tinygState.getA();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCoordinateSystemOffset(org.goko.core.gcode.bean.commands.EnumCoordinateSystem)
	 */
	@Override
	public Tuple6b getCoordinateSystemOffset(EnumCoordinateSystem cs) throws GkException {
		return tinygState.getCoordinateSystemOffset(cs);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCurrentCoordinateSystem()
	 */
	@Override
	public EnumCoordinateSystem getCurrentCoordinateSystem() throws GkException {
		return tinygState.getGCodeContext().getCoordinateSystem();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCoordinateSystem()
	 */
	@Override
	public List<EnumCoordinateSystem> getCoordinateSystem() throws GkException {
		return Arrays.asList(EnumCoordinateSystem.values());
	}
	public void setCoordinateSystemOffset(EnumCoordinateSystem cs, Tuple6b offset) throws GkException {
		tinygState.setCoordinateSystemOffset(cs, offset);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IContinuousJogService#stopJog()
	 */
	@Override
	public void stopJog() throws GkException {
//		if(!MachineState.ALARM.equals(getState())){ // Only stop if not alarmed
//			stopMotion();
//		}
		jogRunnable.disableJogging();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#setCurrentCoordinateSystem(org.goko.core.gcode.bean.commands.EnumCoordinateSystem)
	 */
	@Override
	public void setCurrentCoordinateSystem(EnumCoordinateSystem cs) throws GkException {
		communicator.send( GkUtils.toBytesList( String.valueOf(cs)) );
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#resetCurrentCoordinateSystem()
	 */
	@Override
	public void resetCurrentCoordinateSystem() throws GkException {
		EnumCoordinateSystem current = getCurrentCoordinateSystem();
		Tuple6b offsets = getCoordinateSystemOffset(current);
		Tuple6b mPos = new Tuple6b(tinygState.getWorkPosition());
		mPos = mPos.add(offsets);
		String cmd = "{\""+String.valueOf(current) +"\":{";
		
		cmd += "\"x\":"+ getPositionAsString(mPos.getX()) +", ";		
		cmd += "\"y\":"+ getPositionAsString(mPos.getY())+", ";
		cmd += "\"z\":"+ getPositionAsString(mPos.getZ())+"}} ";
		communicator.send( GkUtils.toBytesList( cmd ) );
		communicator.updateCoordinateSystem(current);		
	}

	/**
	 * @return
	 */
	@Override
	public boolean isPlannerBufferSpaceCheck() {
		return TinyGPreferences.getInstance().isPlannerBufferSpaceCheck();
	}
	/**
	 * @param plannerBufferSpaceCheck the plannerBufferSpaceCheck to set
	 * @throws GkTechnicalException
	 */
	@Override
	public void setPlannerBufferSpaceCheck(boolean value) throws GkTechnicalException {
		TinyGPreferences.getInstance().setPlannerBufferSpaceCheck(value);
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

	public Unit<Length> getCurrentUnit(){
		return tinygState.getCurrentUnit();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#getWorkVolumeMaximalPosition()
	 */
	@Override
	public Tuple6b getWorkVolumeMaximalPosition() throws GkException {
		Tuple6b max = findWorkVolumeMaximalPosition();
		if(max == null){
			 throw new GkTechnicalException("No maximal position currently defined for the travel max position");
		}	
		return max;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#getWorkVolumeMinimalPosition()
	 */
	@Override
	public Tuple6b getWorkVolumeMinimalPosition() throws GkException {
		Tuple6b min = findWorkVolumeMinimalPosition();
		if(min == null){
			 throw new GkTechnicalException("No minimal position currently defined for the travel max position");
		}
		return min;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#findWorkVolumeMaximalPosition()
	 */
	@Override
	public Tuple6b findWorkVolumeMaximalPosition() throws GkException {
		TinyGConfiguration cfg = getConfiguration();
		Tuple6b max = null;
		if(cfg != null){
			max = new Tuple6b(SI.MILLIMETRE, SI.DEGREE_ANGLE);
			max.setX( NumberQuantity.of( cfg.getSetting(TinyGConfiguration.X_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MAXIMUM, BigDecimal.class), SI.MILLIMETRE));
			max.setY( NumberQuantity.of( cfg.getSetting(TinyGConfiguration.Y_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MAXIMUM, BigDecimal.class), SI.MILLIMETRE));
			max.setZ( NumberQuantity.of( cfg.getSetting(TinyGConfiguration.Z_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MAXIMUM, BigDecimal.class), SI.MILLIMETRE));
		}
		return max;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#findWorkVolumeMinimalPosition()
	 */
	@Override
	public Tuple6b findWorkVolumeMinimalPosition() throws GkException {
		TinyGConfiguration cfg = getConfiguration();
		Tuple6b min = null;
		if(cfg != null){
			min = new Tuple6b(SI.MILLIMETRE, SI.DEGREE_ANGLE);
			min.setX( NumberQuantity.of( cfg.getSetting(TinyGConfiguration.X_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MINIMUM, BigDecimal.class), SI.MILLIMETRE));
			min.setY( NumberQuantity.of( cfg.getSetting(TinyGConfiguration.Y_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MINIMUM, BigDecimal.class), SI.MILLIMETRE));
			min.setZ( NumberQuantity.of( cfg.getSetting(TinyGConfiguration.Z_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MINIMUM, BigDecimal.class), SI.MILLIMETRE));
		}
		return min;
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#getFileExtension()
	 */
	@Override
	public String getFileExtension() {	
		return "tinyg.cfg";
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#canExport()
	 */
	@Override
	public boolean canExport() throws GkException {
		return MachineState.READY.equals(getState())
				|| MachineState.PROGRAM_STOP.equals(getState())
				|| MachineState.PROGRAM_END.equals(getState());
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#exportTo(java.io.OutputStream)
	 */
	@Override
	public void exportTo(OutputStream stream) throws GkException {
		JsonObject json = TinyGJsonUtils.toJson(getConfiguration());
		try {
			stream.write(json.toString().getBytes());
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#canImport()
	 */
	@Override
	public boolean canImport() throws GkException {
		return canExport();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#importFrom(java.io.InputStream)
	 */
	@Override
	public void importFrom(InputStream inputStream) throws GkException {
		TinyGConfiguration backupCurrentConfiguration = getConfiguration();
		TinyGConfiguration currentConfiguration = getConfiguration();
		try {
			JsonObject jsonCfg = JsonObject.readFrom(new InputStreamReader(inputStream));
			TinyGControllerUtility.handleConfigurationModification(currentConfiguration, jsonCfg);
			updateConfiguration(currentConfiguration);
		} catch (IOException e) {
			throw new GkTechnicalException(e);
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
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#killAlarm()
	 */
	@Override
	public void killAlarm() throws GkException {
		communicator.send(GkUtils.toBytesList("{\"clear\":\"\"}"));
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IStepJogService#setJogStep(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setJogStep(BigDecimalQuantity<Length> step) throws GkException {
		jogRunnable.setStep(step);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IStepJogService#getJogStep()
	 */
	@Override
	public BigDecimalQuantity<Length> getJogStep() throws GkException {
		return jogRunnable.getStep();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#setJogFeedrate(java.math.BigDecimal)
	 */
	@Override
	public void setJogFeedrate(BigDecimal feed) throws GkException {
		jogRunnable.setFeed(feed);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#getJogFeedrate()
	 */
	@Override
	public BigDecimal getJogFeedrate() throws GkException {
		return jogRunnable.getFeed();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#setJogPrecise(boolean)
	 */
	@Override
	public void setJogPrecise(boolean precise) throws GkException {
		jogRunnable.setPrecise(precise);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#isJogPrecise()
	 */
	@Override
	public boolean isJogPrecise() throws GkException {		
		return jogRunnable.isPrecise();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#startJog(org.goko.core.controller.bean.EnumControllerAxis, java.math.BigDecimal, boolean)
	 */
	@Override
	public void startJog(EnumControllerAxis axis) throws GkException {
		jogRunnable.setAxis(EnumTinyGAxis.getEnum(axis.getCode()));
		jogRunnable.enableJogging();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#isJogPreciseForced()
	 */
	@Override
	public boolean isJogPreciseForced() throws GkException {		
		return false;
	}
}
