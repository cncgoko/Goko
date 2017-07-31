package org.goko.controller.tinyg.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.controller.tinyg.commons.AbstractTinyGControllerService;
import org.goko.controller.tinyg.commons.ITinyGStatus;
import org.goko.controller.tinyg.commons.TinyGJsonUtils;
import org.goko.controller.tinyg.commons.bean.EnumTinyGAxis;
import org.goko.controller.tinyg.commons.bean.TinyGExecutionError;
import org.goko.controller.tinyg.commons.probe.ProbeUtility;
import org.goko.controller.tinyg.controller.configuration.TinyGAxisSettings;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.controller.tinyg.controller.configuration.TinyGConfigurationValue;
import org.goko.controller.tinyg.controller.prefs.TinyGPreferences;
import org.goko.controller.tinyg.controller.topic.TinyGExecutionErrorTopic;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.ApplicativeLogEvent;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.event.EventBrokerUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.action.ControllerActionFactory;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.execution.IGCodeExecutionTimeService;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystemFactory;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;
import org.osgi.service.event.EventAdmin;

import com.eclipsesource.json.JsonObject;

/**
 * Implementation of the TinyG controller
 *
 * @author PsyKo
 *
 */
public class TinyGControllerService extends AbstractTinyGControllerService<TinyGControllerService, TinyGState, TinyGConfiguration, TinyGCommunicator, TinyGJogging, TinyGExecutor> implements ITinygControllerService{
	static final GkLog LOG = GkLog.getLogger(TinyGControllerService.class);
	/**  Service ID */
	public static final String SERVICE_ID = "Controller for TinyG v0.97";			
	/** Applicative log service */
	private IApplicativeLogService applicativeLogService;
	/** Event admin service */
	private EventAdmin eventAdmin;
	/** The probe utility */
	private ProbeUtility probeUtility;		
	/** Execution time service - keep updated with maximum feed */
	private IGCodeExecutionTimeService gcodeExecutionTimeService;
	
	/**
	 * Constructor
	 * @throws GkException GkException
	 */
	public TinyGControllerService(TinyGCommunicator communicator) throws GkException {
		super(new TinyGState(), new TinyGConfiguration(), communicator);
		getCommunicator().setControllerService(this);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#createActionFactory()
	 */
	@Override
	protected ControllerActionFactory createActionFactory() throws GkException {		
		return new TinyGActionFactory(this);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#createJogger()
	 */
	@Override
	protected TinyGJogging createJogger() {		
		return new TinyGJogging(this, getCommunicator());
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#createExecutor()
	 */
	@Override
	protected TinyGExecutor createExecutor() {		
		return new TinyGExecutor(this);
	}


	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#verifyReadyForExecution()
	 */
	@Override
	public void verifyReadyForExecution() throws GkException{
		if(!isReadyForFileStreaming()){
			throw new GkFunctionalException("TNG-003");
		}
		getCommunicator().requestQueueReport();
		getCommunicator().checkExecutionControl();
		checkVerbosity();
	}

	private void checkVerbosity() throws GkException{
		BigDecimal jsonVerbosity = getConfiguration().getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.JSON_VERBOSITY, BigDecimal.class);
		if(!ObjectUtils.equals(jsonVerbosity, TinyGConfigurationValue.JSON_VERBOSITY_VERBOSE)){
			throw new GkFunctionalException("TNG-007");
		}

		BigDecimal qrVerbosity = getConfiguration().getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.QUEUE_REPORT_VERBOSITY, BigDecimal.class);

		if(isPlannerBufferCheck()){
			if(ObjectUtils.equals(qrVerbosity, TinyGConfigurationValue.QUEUE_REPORT_SILENT)){
				throw new GkFunctionalException("TNG-002");
			}
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
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#checkReadyToProbe()
	 */
	@Override
	public void checkReadyToProbe() throws GkException {
		if(!getCommunicator().isConnected()){
			throw new GkFunctionalException("TNG-008");
		}
		if(!isReadyForFileStreaming()){
			throw new GkFunctionalException("TNG-003");
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#isReadyToProbe()
	 */
	@Override
	public boolean isReadyToProbe() {		
		try {
			return getCommunicator().isConnected() && isReadyForFileStreaming();
		} catch (GkException e) {
			LOG.error(e);
			return false;
		}
	}

	/**
	 * Handling GCode response from TinyG
	 * @param jsonValue
	 * @throws GkTechnicalException
	 */
	protected void handleGCodeResponse(String receivedCommand, ITinyGStatus status) throws GkException {
		if(getExecutionService().getExecutionState() == ExecutionState.RUNNING ||
			getExecutionService().getExecutionState() == ExecutionState.PAUSED ||
			getExecutionService().getExecutionState() == ExecutionState.ERROR ){
			if(status == TinyGStatusCode.TG_OK){
				getExecutor().confirmNextLineExecution();
			}else{
				notifyNonOkStatus(status, receivedCommand);
				getExecutor().handleNonOkStatus(status);
			}
		}else{
			if(status != TinyGStatusCode.TG_OK){
				notifyNonOkStatus(status, receivedCommand);
			}
		}
	}

	/**
	 * Handles any TinyG Status that is not TG_OK
	 * @param status the received status or <code>null</code> if unknown
	 * @param receivedCommand the received command
	 * @throws GkException GkException
	 */
	protected void notifyNonOkStatus(ITinyGStatus status, String receivedCommand) throws GkException {
		String message = StringUtils.EMPTY;

		if(status == null){
			message = " Unknown error status";
		}else{
			if(status.isError()){
				// Error report
				message = "Error status returned : "+status.getValue() +" - "+status.getLabel();
				LOG.error(message);
				applicativeLogService.log(ApplicativeLogEvent.LOG_ERROR, message, "TinyG");
				EventBrokerUtils.send(eventAdmin, new TinyGExecutionErrorTopic(), new TinyGExecutionError("Error reported durring execution", "Execution was paused after TinyG reported an error. You can resume, or stop the execution at your own risk.", message));
			}else if(status.isWarning()){
				// Warning report
				message = "Warning status returned : "+status.getValue() +" - "+status.getLabel();
				LOG.warn(message);
				applicativeLogService.log(ApplicativeLogEvent.LOG_WARNING, message, "TinyG");
			}
		}
	}

	/**
	 * Initiate TinyG homing sequence
	 * @throws GkException GkException
	 */
	public void startHomingSequence() throws GkException{
		LOG.info("Homing...");
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
		getCommunicator().send(GkUtils.toBytesList(homingCommand), true);
	}


	/* ************************************************
	 *  CONTROLLER ACTIONS
	 * ************************************************/


	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#pauseMotion()
	 */
	@Override
	public void pauseMotion() throws GkException{
		getCommunicator().pauseMotion();
		getExecutionService().pauseQueueExecution();
	}
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#resumeMotion()
	 */
	@Override
	public void resumeMotion() throws GkException{
		getCommunicator().resumeMotion();
		getExecutionService().resumeQueueExecution();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#startMotion()
	 */
	@Override
	public void startMotion() throws GkException {
		getCommunicator().startMotion();
		getExecutionService().beginQueueExecution(ExecutionQueueType.DEFAULT);
	}
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#stopMotion()
	 */
	@Override
	public void stopMotion() throws GkException{
		getCommunicator().getConnectionService().clearOutputBuffer();
		
		if(probeUtility != null && probeUtility.isProbingInProgress()){
			probeUtility.cancelActiveProbing();
			// Probe active, let's hack over a TinyG bug
			getCommunicator().sendImmediately(GkUtils.toBytesList(TinyGv097.FEED_HOLD), true);
			schedule().send(TinyGv097.QUEUE_FLUSH).whenState(MachineState.MOTION_HOLDING).timeout(10, TimeUnit.SECOND).begin();			
		}else{
			getCommunicator().stopMotion();
		}
				
		getExecutionService().stopQueueExecution();
		
		//communicator.send(GkUtils.toBytesList(TinyG.QUEUE_FLUSH));
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
		getCommunicator().send(lstBytes, true);
	}

	public void turnSpindleOn() throws GkException{
		getCommunicator().turnSpindleOn();
	}

	public void turnSpindleOff() throws GkException{
		getCommunicator().turnSpindleOff();
	}

	/**
	 * @return the availableBuffer
	 * @throws GkException
	 */
	@Override
	public int getAvailablePlannerBuffer() {
		try {
			return getInternalState().getAvailableBuffer();
		} catch (GkException e) {
			LOG.error(e);
			return 0;			
		}
	}
	
	/**
	 * @param availableBuffer the availableBuffer to set
	 * @throws GkException exception
	 */
	@Override
	public void setAvailablePlannerBuffer(int availableBuffer)  {
		try {
			getInternalState().setAvailableBuffer(availableBuffer);
			// TODO : find a better way to deal with this 
			getExecutor().onBufferSpaceAvailableChange(availableBuffer);
		} catch (GkException e) {
			LOG.error(e);			
		}	
	}

	/**
	 * Set the last received message 
	 * @param message the message
	 * @throws GkException GkException 
	 */
	public void setMessage(String message) throws GkException{
		getInternalState().setMessage(message);
	}
	
	public void resetAvailableBuffer()  {
		setAvailablePlannerBuffer(28);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#probe(java.util.List)
	 */
	@Override
	public CompletionService<ProbeResult> probe(List<ProbeRequest> lstProbeRequest) throws GkException {		
		probeUtility = new ProbeUtility(this);
		probeUtility.prepare(lstProbeRequest);
		
		IGCodeProvider probeGCodeProvider = probeUtility.getProbeGCodeProvider();
		getExecutionService().clearExecutionQueue(ExecutionQueueType.SYSTEM);
		getExecutionService().addToExecutionQueue(ExecutionQueueType.SYSTEM, probeGCodeProvider);
		getExecutionService().beginQueueExecution(ExecutionQueueType.SYSTEM);
		
		return probeUtility.getExecutorCompletionService();
	}

	protected double getPositionAsDouble(Length q) throws GkException{
		return Double.valueOf(GokoPreference.getInstance().format(q.to(getCurrentUnit()), true, false));
	}
	/**
	 * @param applicativeLogService the IApplicativeLogService to set
	 */
	public void setApplicativeLogService(IApplicativeLogService applicativeLogService) {
		this.applicativeLogService = applicativeLogService;
		getCommunicator().setApplicativeLogService(applicativeLogService);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#setCurrentCoordinateSystem(org.goko.core.gcode.bean.commands.EnumCoordinateSystem)
	 */
	@Override
	public void setCurrentCoordinateSystem(ICoordinateSystem cs) throws GkException {
		getCommunicator().send( cs.getCode(), true );
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCoordinateSystem()
	 */
	@Override
	public List<ICoordinateSystem> getCoordinateSystem() throws GkException {
		List<ICoordinateSystem> lstCoordinateSystem = new ArrayList<>();
		lstCoordinateSystem.addAll(new CoordinateSystemFactory().get());
		return lstCoordinateSystem;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#resetCurrentCoordinateSystem()
	 */
	@Override
	public void resetCurrentCoordinateSystem() throws GkException {
		ICoordinateSystem current = getCurrentCoordinateSystem();
		Tuple6b offsets = getCoordinateSystemOffset(current);
		Tuple6b mPos = new Tuple6b(getInternalState().getWorkPosition());
		mPos = mPos.add(offsets);
		
		JsonObject xyzPosition = new JsonObject();
		xyzPosition.add(EnumTinyGAxis.X_POSITIVE.getAxisCode(), getPositionAsDouble(mPos.getX()));
		xyzPosition.add(EnumTinyGAxis.Y_POSITIVE.getAxisCode(), getPositionAsDouble(mPos.getY()));
		xyzPosition.add(EnumTinyGAxis.Z_POSITIVE.getAxisCode(), getPositionAsDouble(mPos.getZ()));
		JsonObject csObject = new JsonObject();
		csObject.add(current.getCode(), xyzPosition);
		
		getCommunicator().send( csObject , true );
		getCommunicator().requestCoordinateSystemUpdate(current);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#updateCoordinateSystemPosition(org.goko.core.gcode.element.ICoordinateSystem, org.goko.core.math.Tuple6b)
	 */
	@Override
	public void updateCoordinateSystemPosition(ICoordinateSystem coordinateSystem, Tuple6b position) throws GkException {
		JsonObject xyzPosition = new JsonObject();
		xyzPosition.add(EnumTinyGAxis.X_POSITIVE.getAxisCode(), getPositionAsDouble(position.getX()));
		xyzPosition.add(EnumTinyGAxis.Y_POSITIVE.getAxisCode(), getPositionAsDouble(position.getY()));
		xyzPosition.add(EnumTinyGAxis.Z_POSITIVE.getAxisCode(), getPositionAsDouble(position.getZ()));
		JsonObject csObject = new JsonObject();
		csObject.add(coordinateSystem.getCode(), xyzPosition);
		
		getCommunicator().send( csObject , true );
		getCommunicator().requestCoordinateSystemUpdate(coordinateSystem);		
	}

	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#isPlannerBufferCheck()
	 */
	@Override
	public boolean isPlannerBufferCheck() {
		return TinyGPreferences.getInstance().isPlannerBufferSpaceCheck();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#setPlannerBufferCheck(boolean)
	 */
	@Override
	public void setPlannerBufferCheck(boolean value) {
		TinyGPreferences.getInstance().setPlannerBufferSpaceCheck(value);
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
	 * @see org.goko.core.controller.IWorkVolumeProvider#getWorkVolumeProviderName()
	 */
	@Override
	public String getWorkVolumeProviderName() {		
		return "TinyG 0.97";
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#findWorkVolumeMaximalPosition()
	 */
	@Override
	public Tuple6b findWorkVolumeMaximalPosition() throws GkException {
		TinyGConfiguration cfg = getConfiguration();
		Tuple6b max = null;
		if(cfg != null){
			max = new Tuple6b(Units.MILLIMETRE, AngleUnit.DEGREE_ANGLE);
			max.setX( Length.valueOf( cfg.getSetting(TinyGConfiguration.X_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MAXIMUM, BigDecimal.class), Units.MILLIMETRE));
			max.setY( Length.valueOf( cfg.getSetting(TinyGConfiguration.Y_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MAXIMUM, BigDecimal.class), Units.MILLIMETRE));
			max.setZ( Length.valueOf( cfg.getSetting(TinyGConfiguration.Z_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MAXIMUM, BigDecimal.class), Units.MILLIMETRE));
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
			min = new Tuple6b(Units.MILLIMETRE, AngleUnit.DEGREE_ANGLE);
			min.setX( Length.valueOf( cfg.getSetting(TinyGConfiguration.X_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MINIMUM, BigDecimal.class), Units.MILLIMETRE));
			min.setY( Length.valueOf( cfg.getSetting(TinyGConfiguration.Y_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MINIMUM, BigDecimal.class), Units.MILLIMETRE));
			min.setZ( Length.valueOf( cfg.getSetting(TinyGConfiguration.Z_AXIS_SETTINGS, TinyGAxisSettings.TRAVEL_MINIMUM, BigDecimal.class), Units.MILLIMETRE));
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
		TinyGConfiguration currentConfiguration = getConfiguration();
		try {
			JsonObject jsonCfg = JsonObject.readFrom(new InputStreamReader(inputStream));
			currentConfiguration.setFromJson(jsonCfg);
			applyConfiguration(currentConfiguration);
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
		getCommunicator().send(GkUtils.toBytesList("{\"clear\":\"\"}"), true);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException {
		if(probeUtility != null){
			probeUtility.clearProbingGCode();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionCanceled()
	 */
	@Override
	public void onQueueExecutionCanceled() throws GkException {
		if(probeUtility != null){
			probeUtility.clearProbingGCode();
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.configuration.ITinyGConfigurationListener#onConfigurationChanged(org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration, org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration)
	 */
	@Override
	public void onConfigurationChanged(TinyGConfiguration oldConfig, TinyGConfiguration newConfig) {
		// TODO Auto-generated method stub
		
	}
		
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.configuration.ITinyGConfigurationListener#onConfigurationSettingChanged(org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration, org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration, java.lang.String, java.lang.String)
	 */
	@Override
	public void onConfigurationSettingChanged(TinyGConfiguration oldConfig, TinyGConfiguration newConfig, String groupIdentifier, String settingIdentifier) {
		if(StringUtils.equals(settingIdentifier, TinyGAxisSettings.TRAVEL_MINIMUM)
		|| StringUtils.equals(settingIdentifier, TinyGAxisSettings.TRAVEL_MAXIMUM)){
			notifyWorkVolumeUpdate();
		}	

		try{
			if(StringUtils.equals(settingIdentifier, TinyGAxisSettings.FEEDRATE_MAXIMUM)){			
				BigDecimal xMaxValue = newConfig.getSetting(TinyGConfiguration.X_AXIS_SETTINGS, TinyGAxisSettings.FEEDRATE_MAXIMUM, BigDecimal.class);
				BigDecimal yMaxValue = newConfig.getSetting(TinyGConfiguration.Y_AXIS_SETTINGS, TinyGAxisSettings.FEEDRATE_MAXIMUM, BigDecimal.class);
				BigDecimal zMaxValue = newConfig.getSetting(TinyGConfiguration.Z_AXIS_SETTINGS, TinyGAxisSettings.FEEDRATE_MAXIMUM, BigDecimal.class);
				BigDecimal aMaxValue = newConfig.getSetting(TinyGConfiguration.A_AXIS_SETTINGS, TinyGAxisSettings.FEEDRATE_MAXIMUM, BigDecimal.class);
				Unit<Speed> unit = getInternalState().getGCodeContext().getUnit().getFeedUnit();
				if(xMaxValue != null){
					gcodeExecutionTimeService.getExecutionConstraint().setXAxisMaximumFeed( Speed.valueOf(xMaxValue, unit));
				}
				if(yMaxValue != null){
					gcodeExecutionTimeService.getExecutionConstraint().setYAxisMaximumFeed( Speed.valueOf(yMaxValue, unit));
				}
				if(zMaxValue != null){
					gcodeExecutionTimeService.getExecutionConstraint().setZAxisMaximumFeed( Speed.valueOf(zMaxValue, unit));
				}
				if(aMaxValue != null){
					gcodeExecutionTimeService.getExecutionConstraint().setAAxisMaximumFeed( Speed.valueOf(aMaxValue, unit));
				}
			}
		}catch(GkException e){
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeLineExecutionListener#onLineStateChanged(org.goko.core.gcode.execution.IExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onLineStateChanged(IExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#resetTinyG()
	 */
	@Override
	public void resetTinyG() throws GkException {
		getInternalState().updateValue(TinyGv097.MESSAGE, "RESETING..."); // Dirty hack
		schedule().when(TinyGv097.MESSAGE, "SYSTEM READY").execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					getCommunicator().requestConfigurationUpdate();
				} catch (GkException e) {
					LOG.error(e);
				}				
			}
		}).timeout(10, TimeUnit.SECOND).begin();
		getCommunicator().resetTinyG();
		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#handleProbeResult(boolean, org.goko.core.math.Tuple6b)
	 */
	@Override
	public void handleProbeResult(boolean probeSuccess, Tuple6b probePosition) throws GkException {
		probeUtility.handleProbeResult(probeSuccess, probePosition);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#resetConfiguration()
	 */
	@Override
	public void resetConfiguration() {
		setConfiguration( new TinyGConfiguration() );		
	}

	/**
	 * @return the gcodeExecutionTimeService
	 */
	public IGCodeExecutionTimeService getGcodeExecutionTimeService() {
		return gcodeExecutionTimeService;
	}

	/**
	 * @param gcodeExecutionTimeService the gcodeExecutionTimeService to set
	 */
	public void setGcodeExecutionTimeService(IGCodeExecutionTimeService gcodeExecutionTimeService) {
		this.gcodeExecutionTimeService = gcodeExecutionTimeService;
	}
}
