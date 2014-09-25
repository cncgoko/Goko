package org.goko.tinyg.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.bean.MachineValueStore;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.provider.GCodeExecutionToken;
import org.goko.core.gcode.bean.provider.GCodeStreamedExecutionToken;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.tinyg.controller.configuration.TinyGGroupSettings;
import org.goko.tinyg.controller.configuration.TinyGSetting;
import org.goko.tinyg.controller.events.ConfigurationUpdateEvent;
import org.goko.tinyg.controller.probe.ProbeCallable;
import org.goko.tinyg.json.TinyGJsonUtils;
import org.goko.tinyg.service.ITinyGControllerFirmwareService;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * Implementation of the TinyG controller
 *
 * @author PsyKo
 *
 */
public class TinyGControllerService extends EventDispatcher implements ITinyGControllerFirmwareService, IConnectionDataListener, IConnectionListener {
	static final GkLog LOG = GkLog.getLogger(TinyGControllerService.class);
	/**  Service ID */
	public static final String SERVICE_ID = "TinyG Controller";
	private static final String JOG_SIMULATION_DISTANCE = "1000.0";
	private static final Object UNITS_MM = "mm";
	private static final Object UNITS_INCHES = "inches";
	private static final Object DISTANCE_MODE_ABSOLUTE = "Absolute";
	private static final Object DISTANCE_MODE_INCREMENTAL = "Relative";
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
	/** The current execution queue */
	private ExecutionQueue executionQueue;
	/** Action factory */
	private TinyGActionFactory actionFactory;
	/** Storage object for machine values (speed, position, etc...) */
	private MachineValueStore valueStore;
	/** The count of available buffer in the TinyG board*/
	private int availableBuffer;
	/** Waiting probe result */
	private ProbeCallable futureProbeResult;

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
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.UNITS, "Units", "The units in use", String.class),
				new MachineValue<String>(DefaultControllerValues.UNITS, StringUtils.EMPTY));
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.COORDINATES, "Coordinates", "The coordinate system", String.class),
				new MachineValue<String>(DefaultControllerValues.COORDINATES, StringUtils.EMPTY));
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.DISTANCE_MODE, "Distance mode", "The distance motion setting", String.class),
				new MachineValue<String>(DefaultControllerValues.DISTANCE_MODE, StringUtils.EMPTY));
		valueStore.addListener(this);


		// Initiate execution queue
		executionQueue = new ExecutionQueue();
		ExecutorService executor 	= Executors.newSingleThreadExecutor();
		currentSendingRunnable 	= new GCodeSendingRunnable(executionQueue, this);
		executor.execute(currentSendingRunnable);

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
	public GCodeExecutionToken executeGCode(IGCodeProvider gcodeProvider) throws GkException{
		if(!getConnectionService().isConnected()){
			throw new GkFunctionalException("Cannot send command. Connection service is not connected");
		}

		GCodeStreamedExecutionToken token = new GCodeStreamedExecutionToken(gcodeProvider);
		executionQueue.add(token);
		return token;
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
	@Override
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
	@Override
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

	}
	/**
	 * Verify the response using the header
	 * @param jsonValue the parsed response
	 * @throws GkException GkException
	 */
	private void handleResponseFooter(JsonValue jsonValue) throws GkException {
	//	LOG.info("handleResponseFooter "+String.valueOf(jsonValue));

		JsonArray footerArray = jsonValue.asArray();
		int statusCodeIntValue = footerArray.get(TinyGJsonUtils.FOOTER_STATUS_CODE_INDEX).asInt();
		TinyGStatusCode status = TinyGStatusCode.findEnum(statusCodeIntValue);
		if(status != null){
			if(status == TinyGStatusCode.TG_OK){

			}else{
				LOG.error(" Error status returned : "+status.getValue() +" - "+status.getLabel());
			}
		}else{
			LOG.error(" Unknown error status "+statusCodeIntValue);
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
	//	LOG.info("handleResponseEnvelope "+String.valueOf(responseEnvelope));
		for(String name : responseEnvelope.names()){
		//	System.err.println("Name = "+name);
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

	/**
	 * Handling configuration response from TinyG
	 * @param jsonValue
	 */
	private void handleConfigurationModification(JsonObject responseEnvelope) throws GkException {
		TinyGControllerUtility.handleConfigurationModification(configuration, responseEnvelope);
		notifyListeners(new ConfigurationUpdateEvent());
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

	private void handleQueueReport(JsonValue queueReport) throws GkException {
		this.availableBuffer = queueReport.asInt();
		if(currentSendingRunnable != null){
			currentSendingRunnable.notifyBufferSpace();
		}
	}
	/**
	 * Handling status report from TinyG
	 * @param jsonValue
	 */
	private void handleStatusReport(JsonValue statusReport) throws GkException {
	//	LOG.info("handleStatusReport "+String.valueOf(statusReport));
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
			/*
			 * Update units
			 */
			JsonValue unitReport = statusReportObject.get(TinyGJsonUtils.STATUS_REPORT_UNITS);
			if(unitReport != null){
				int units = unitReport.asInt();
				if(units == 1){
					valueStore.updateValue(DefaultControllerValues.UNITS, UNITS_MM);
				}else{
					valueStore.updateValue(DefaultControllerValues.UNITS, UNITS_INCHES);
				}
			}
			/*
			 * Update coordinates
			 * 0=g53, 1=g54, 2=g55, 3=g56, 4=g57, 5=g58, 6=g59
			 */
			JsonValue coordReport = statusReportObject.get(TinyGJsonUtils.STATUS_REPORT_COORDINATES);
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
				valueStore.updateValue(DefaultControllerValues.COORDINATES , coordinateSystem);
			}
			/*
			 * Update distance mode
			 */
			JsonValue distReport = statusReportObject.get(TinyGJsonUtils.STATUS_REPORT_DISTANCE_MODE);
			if(distReport != null){
				int dist = distReport.asInt();
				if(dist == 0){
					valueStore.updateValue(DefaultControllerValues.DISTANCE_MODE, DISTANCE_MODE_ABSOLUTE);
				}else{
					valueStore.updateValue(DefaultControllerValues.DISTANCE_MODE, DISTANCE_MODE_INCREMENTAL);
				}
			}
		}
	}

	/**
	 * Handling GCode response from TinyG
	 * @param jsonValue
	 * @throws GkTechnicalException
	 */
	private void handleGCodeResponse(JsonValue jsonValue) throws GkException {
		LOG.info("handleGCodeResponse "+String.valueOf(jsonValue));
		if(executionQueue.getCurrentToken() != null){
			String 			receivedCommand = jsonValue.asString();
			GCodeCommand 	parsedCommand 	= getGcodeService().parseCommand(receivedCommand);
			executionQueue.getCurrentToken().confirmCommand(parsedCommand);
			this.currentSendingRunnable.confirmCommand();
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
		}else if(event == EnumConnectionEvent.DISCONNECTED){
			getConnectionService().removeInputDataListener(this);
			setState(MachineState.UNDEFINED);
		}
	}

	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineValueUpdate(MachineValueUpdateEvent evt){
		notifyListeners(evt);
	}

	/** (inheritDoc)
	 * @see org.goko.tinyg.service.ITinyGControllerFirmwareService#setConfiguration(org.goko.tinyg.controller.configuration.TinyGConfiguration)
	 */
	@Override
	public void setConfiguration(TinyGConfiguration cfg) throws GkException{
		// Let's only change the new values
		TinyGConfiguration diffConfig = TinyGControllerUtility.getDifferentialConfiguration(getConfiguration(), cfg);

		for(TinyGGroupSettings group: diffConfig.getGroups()){
			if(StringUtils.equals(group.getGroupIdentifier(), TinyGConfiguration.SYSTEM_SETTINGS)){
				for(TinyGSetting<?> setting : group.getSettings()){
					JsonObject jsonSetting = TinyGJsonUtils.toJson(setting);
					if(jsonSetting != null){
						getConnectionService().send( GkUtils.toBytesList(jsonSetting.toString() + "\r\n") );
					}
				}
			}else{
				JsonObject jsonGroup = TinyGJsonUtils.toCompleteJson(group);
				if(jsonGroup != null){
					getConnectionService().send( GkUtils.toBytesList(jsonGroup.toString() + "\r\n") );
				}
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
	@Override
	public MachineValueDefinition findMachineValueDefinition(String id) throws GkException {
		return valueStore.findMachineValueDefinition(id);
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
		getConnectionService().clearOutputBuffer();
		getConnectionService().send(stopCommand, DataPriority.IMPORTANT);

		if(executionQueue != null){
			executionQueue.clear();
		}
		if(currentSendingRunnable != null){
			currentSendingRunnable.stop();
		}
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
		probe(EnumControllerAxis.Z_POSITIVE, 20, -10);
		if(true) {
			return;
		}
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
			executionQueue.clear();
		}
		stopMotion();
	}

	public boolean requireAcknowledgement(GCodeCommand currentCommand) {
		return false;
	}

	@Override
	public String getMinimalSupportedFirmwareVersion() throws GkException {
		return "380.05";
	}

	@Override
	public String getMaximalSupportedFirmwareVersion() throws GkException {
		return "380.05";
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#probe(org.goko.core.controller.bean.EnumControllerAxis, double, double)
	 */
	@Override
	public Future<Tuple6b> probe(EnumControllerAxis axis, double feedrate, double maximumPosition) throws GkException {
		futureProbeResult = new ProbeCallable();
		String strCommand = "G38.2 "+axis.getAxisCode()+String.valueOf(maximumPosition)+" F"+feedrate;
		IGCodeProvider command = gcodeService.parse(strCommand);
		executeGCode(command);
		return Executors.newSingleThreadExecutor().submit(futureProbeResult);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#moveToAbsolutePosition(org.goko.core.gcode.bean.Tuple6b)
	 */
	@Override
	public void moveToAbsolutePosition(Tuple6b position) throws GkException {
		String cmd = "G1F800";
		if(position.getX() != null){
			cmd += "X"+position.getX();
		}
		if(position.getY() != null){
			cmd += "Y"+position.getY();
		}
		if(position.getZ() != null){
			cmd += "Z"+position.getZ();
		}
		IGCodeProvider command = gcodeService.parse(cmd);
		executeGCode(command);
	}

}
