/**
 * 
 */
package org.goko.controller.g2core.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;

import org.apache.commons.lang3.StringUtils;
import org.goko.controller.g2core.configuration.G2CoreConfiguration;
import org.goko.controller.g2core.controller.action.G2CoreActionFactory;
import org.goko.controller.g2core.controller.preferences.G2CorePreferences;
import org.goko.controller.g2core.controller.topic.G2CoreExecutionErrorTopic;
import org.goko.controller.tinyg.commons.AbstractTinyGControllerService;
import org.goko.controller.tinyg.commons.ITinyGStatus;
import org.goko.controller.tinyg.commons.bean.EnumTinyGAxis;
import org.goko.controller.tinyg.commons.bean.TinyGExecutionError;
import org.goko.controller.tinyg.commons.probe.ProbeUtility;
import org.goko.core.common.applicative.logging.ApplicativeLogEvent;
import org.goko.core.common.event.EventBrokerUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.action.ControllerActionFactory;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystemFactory;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;
import org.osgi.service.event.EventAdmin;

import com.eclipsesource.json.JsonObject;

/**
 * @author Psyko
 * @date 8 janv. 2017
 */
public class G2CoreControllerService extends AbstractTinyGControllerService<G2CoreControllerService, G2CoreState, G2CoreConfiguration, G2CoreCommunicator, G2CoreJogger, G2CoreExecutor> implements IG2CoreControllerService{
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(G2CoreControllerService.class);
	/**  Service ID */
	public static final String SERVICE_ID = "org.goko.controller.g2core.v099";	
	/** Event admin service */
	private EventAdmin eventAdmin;
	/** The probe utility */
	private ProbeUtility probeUtility;	
	
	/**
	 * @param communicator 
	 * @throws GkException GkException 
	 */
	public G2CoreControllerService(G2CoreCommunicator communicator) throws GkException {
		super(new G2CoreState(), new G2CoreConfiguration(), communicator);
		communicator.setControllerService(this);
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
		return new G2CoreActionFactory(this);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#createJogger()
	 */
	@Override
	protected G2CoreJogger createJogger() {		
		return new G2CoreJogger(this, getCommunicator());
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#createExecutor()
	 */
	@Override
	protected G2CoreExecutor createExecutor() {		
		return new G2CoreExecutor(this);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#setCurrentCoordinateSystem(org.goko.core.gcode.element.ICoordinateSystem)
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

	/**
	 * Returns the given length as a string
	 * @param q the length
	 * @return Double
	 * @throws GkException GkException
	 */
	protected double getPositionAsDouble(Length q) throws GkException{
		return Double.valueOf(GokoPreference.getInstance().format(q.to(getCurrentUnit()), true, false));
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#killAlarm()
	 */
	@Override
	public void killAlarm() throws GkException {
		getCommunicator().killAlarm();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#stopMotion()
	 */
	@Override
	public void stopMotion() throws GkException {
		if(probeUtility != null && probeUtility.isProbingInProgress()){
			probeUtility.cancelActiveProbing();
		}
		getCommunicator().stopMotion();
		getExecutionService().stopQueueExecution();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#pauseMotion()
	 */
	@Override
	public void pauseMotion() throws GkException {
		getCommunicator().pauseMotion();
		getExecutionService().pauseQueueExecution();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#resumeMotion()
	 */
	@Override
	public void resumeMotion() throws GkException {
		getCommunicator().resumeMotion();
		getExecutionService().resumeQueueExecution();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#startMotion()
	 */
	@Override
	public void startMotion() throws GkException {
		getCommunicator().startMotion();
		getExecutionService().beginQueueExecution(ExecutionQueueType.DEFAULT);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#resetTinyG()
	 */
	@Override
	public void resetTinyG() throws GkException {		
		getCommunicator().resetG2Core();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#turnSpindleOn()
	 */
	@Override
	public void turnSpindleOn() throws GkException {
		getCommunicator().turnSpindleOn();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#turnSpindleOff()
	 */
	@Override
	public void turnSpindleOff() throws GkException {
		getCommunicator().turnSpindleOff();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#resetZero(java.util.List)
	 */
	@Override
	public void resetZero(List<String> axes) throws GkException {
		getCommunicator().resetZero(axes);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#startHomingSequence()
	 */
	@Override
	public void startHomingSequence() throws GkException {
		String 		homingCommand 		= "G28.2";
		if(G2CorePreferences.getInstance().isHomingEnabledAxisX()){
			homingCommand += " X0";
		}
		if(G2CorePreferences.getInstance().isHomingEnabledAxisY()){
			homingCommand += " Y0";
		}
		if(G2CorePreferences.getInstance().isHomingEnabledAxisZ()){
			homingCommand += " Z0";
		}
		if(G2CorePreferences.getInstance().isHomingEnabledAxisA()){
			homingCommand += " A0";
		}
		getCommunicator().send(homingCommand, true);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#getWorkVolumeProviderName()
	 */
	@Override
	public String getWorkVolumeProviderName() {		
		return "G2 Core";
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#findWorkVolumeMinimalPosition()
	 */
	@Override
	public Tuple6b findWorkVolumeMinimalPosition() throws GkException {
		G2CoreConfiguration cfg = getConfiguration();
		Tuple6b min = null;
		if(cfg != null){
			min = new Tuple6b(Units.MILLIMETRE, AngleUnit.DEGREE_ANGLE);
			min.setX( Length.valueOf( cfg.getSetting(G2Core.Configuration.Groups.X_AXIS, G2Core.Configuration.Axes.TRAVEL_MINIMUM, BigDecimal.class), Units.MILLIMETRE));
			min.setY( Length.valueOf( cfg.getSetting(G2Core.Configuration.Groups.Y_AXIS, G2Core.Configuration.Axes.TRAVEL_MINIMUM, BigDecimal.class), Units.MILLIMETRE));
			min.setZ( Length.valueOf( cfg.getSetting(G2Core.Configuration.Groups.Z_AXIS, G2Core.Configuration.Axes.TRAVEL_MINIMUM, BigDecimal.class), Units.MILLIMETRE));
		}
		return min;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#findWorkVolumeMaximalPosition()
	 */
	@Override
	public Tuple6b findWorkVolumeMaximalPosition() throws GkException {
		G2CoreConfiguration cfg = getConfiguration();
		Tuple6b min = null;
		if(cfg != null){
			min = new Tuple6b(Units.MILLIMETRE, AngleUnit.DEGREE_ANGLE);
			min.setX( Length.valueOf( cfg.getSetting(G2Core.Configuration.Groups.X_AXIS, G2Core.Configuration.Axes.TRAVEL_MAXIMUM, BigDecimal.class), Units.MILLIMETRE));
			min.setY( Length.valueOf( cfg.getSetting(G2Core.Configuration.Groups.Y_AXIS, G2Core.Configuration.Axes.TRAVEL_MAXIMUM, BigDecimal.class), Units.MILLIMETRE));
			min.setZ( Length.valueOf( cfg.getSetting(G2Core.Configuration.Groups.Z_AXIS, G2Core.Configuration.Axes.TRAVEL_MAXIMUM, BigDecimal.class), Units.MILLIMETRE));
		}
		return min;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#detectWorkVolumeUpdate(org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration, org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration)
	 */
	@Override
	protected boolean detectWorkVolumeUpdate(G2CoreConfiguration currentConfiguration, G2CoreConfiguration newConfiguration) {
		return newConfiguration.isCompletelyLoaded() && 
			(detectWorkVolumeUpdateOnAxis(G2Core.Configuration.Groups.X_AXIS, currentConfiguration, newConfiguration)
			|| detectWorkVolumeUpdateOnAxis(G2Core.Configuration.Groups.Y_AXIS, currentConfiguration, newConfiguration)
			|| detectWorkVolumeUpdateOnAxis(G2Core.Configuration.Groups.Z_AXIS, currentConfiguration, newConfiguration)
			|| detectWorkVolumeUpdateOnAxis(G2Core.Configuration.Groups.A_AXIS, currentConfiguration, newConfiguration)
			|| detectWorkVolumeUpdateOnAxis(G2Core.Configuration.Groups.B_AXIS, currentConfiguration, newConfiguration)
			|| detectWorkVolumeUpdateOnAxis(G2Core.Configuration.Groups.C_AXIS, currentConfiguration, newConfiguration));
	}
	
	/**
	 * Detect any work volume update on the given axis 
	 * @param axis the axis 
	 * @param currentConfiguration the current configuration
	 * @param newConfiguration the new configuration
	 * @return <code>true</code> if an update was detected, <code>false</code> otherwise
	 */
	protected boolean detectWorkVolumeUpdateOnAxis(String axis, G2CoreConfiguration currentConfiguration, G2CoreConfiguration newConfiguration){		
		try {
			BigDecimal oldMinValue = currentConfiguration.getSetting(axis, G2Core.Configuration.Axes.TRAVEL_MINIMUM, BigDecimal.class);
			BigDecimal newMinValue = newConfiguration.getSetting(axis, G2Core.Configuration.Axes.TRAVEL_MINIMUM, BigDecimal.class);
			if(oldMinValue != null && newMinValue != null && oldMinValue.compareTo(newMinValue) != 0){
				return true;
			}
			
			BigDecimal oldMaxValue = currentConfiguration.getSetting(axis, G2Core.Configuration.Axes.TRAVEL_MAXIMUM, BigDecimal.class);
			BigDecimal newMaxValue = newConfiguration.getSetting(axis, G2Core.Configuration.Axes.TRAVEL_MAXIMUM, BigDecimal.class);
			if(oldMaxValue != null && newMaxValue != null && oldMaxValue.compareTo(newMaxValue) != 0){
				return true;
			}
		} catch (GkException e) {
			LOG.error(e);
		}
		return false;
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#getFileExtension()
	 */
	@Override
	public String getFileExtension() {
		return "g2v099.cfg";
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#canExport()
	 */
	@Override
	public boolean canExport() throws GkException {
		return G2Core.State.READY.equals(getState())
			|| G2Core.State.PROGRAM_STOP.equals(getState())
			|| G2Core.State.PROGRAM_END.equals(getState());
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#canImport()
	 */
	@Override
	public boolean canImport() throws GkException {
		return canExport();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#getAvailablePlannerBuffer()
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
	
	/** (inheritDoc)
	 * @see org.goko.controller.g2core.controller.IG2CoreControllerService#setAvailablePlannerBuffer(int)
	 */
	@Override
	public void setAvailablePlannerBuffer(int available) {
		try {
			getInternalState().setAvailableBuffer(available);
			// TODO : find a better way to deal with this 
			getExecutor().onBufferSpaceAvailableChange(available);
		} catch (GkException e) {
			LOG.error(e);			
		}		
	}

	/**
	 * Handle a GCode response
	 * @param command the confirmed command. Can be <code>null</code> depending on board verbosity
	 * @param status the returned status
	 */
	public void handleGCodeResponse(String command, ITinyGStatus status) throws GkException{
		ExecutionState state = getExecutionService().getExecutionState();
		
		if(state == ExecutionState.RUNNING
			|| state == ExecutionState.PAUSED
			|| state == ExecutionState.ERROR ){
			if(status == G2CoreStatusCode.STAT_OK){
				getExecutor().confirmNextLineExecution();			
			}else{
				notifyNonOkStatus(status, StringUtils.EMPTY);
				getExecutor().handleNonOkStatus(status);
			}
		}else{
			if(status != G2CoreStatusCode.STAT_OK){
				notifyNonOkStatus(status, command);
			}
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException {		
		super.onQueueExecutionComplete();
		if(probeUtility != null){
			probeUtility.clearProbingGCode();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionCanceled()
	 */
	@Override
	public void onQueueExecutionCanceled() throws GkException {
		super.onQueueExecutionCanceled();
		if(probeUtility != null){
			probeUtility.clearProbingGCode();
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
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#handleProbeResult(boolean, org.goko.core.math.Tuple6b)
	 */
	@Override
	public void handleProbeResult(boolean probeSuccess, Tuple6b probePosition) throws GkException {
		probeUtility.handleProbeResult(probeSuccess, probePosition);
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
				getApplicativeLogService().log(ApplicativeLogEvent.LOG_ERROR, message, "G2Core");
				EventBrokerUtils.send(eventAdmin, new G2CoreExecutionErrorTopic(), new TinyGExecutionError("Error reported durring execution", "Execution was paused after TinyG reported an error. You can resume, or stop the execution at your own risk.", message));
			}else if(status.isWarning()){
				// Warning report
				message = "Warning status returned : "+status.getValue() +" - "+status.getLabel();
				LOG.warn(message);
				getApplicativeLogService().log(ApplicativeLogEvent.LOG_WARNING, message, "G2Core");
			}
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
	 * Set the last received message 
	 * @param message the message
	 * @throws GkException GkException 
	 */
	public void setMessage(String message) throws GkException{
		getInternalState().setMessage(message);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#resetConfiguration()
	 */
	@Override
	public void resetConfiguration() {
		setConfiguration( new G2CoreConfiguration() );		
	}
}
