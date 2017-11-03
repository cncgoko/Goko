/**
 * 
 */
package org.goko.controller.grbl.v11;

import java.util.List;
import java.util.concurrent.CompletionService;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.goko.controller.grbl.commons.AbstractGrblControllerService;
import org.goko.controller.grbl.commons.IGrblStatus;
import org.goko.controller.grbl.commons.probe.ProbeUtility;
import org.goko.controller.grbl.v11.bean.GrblMachineState;
import org.goko.controller.grbl.v11.bean.StatusReport;
import org.goko.controller.grbl.v11.configuration.GrblConfiguration;
import org.goko.controller.grbl.v11.jog.GrblJogger;
import org.goko.controller.grbl.v11.preferences.Grblv11Preferences;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.controller.action.ControllerActionFactory;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.execution.IGCodeExecutionTimeService;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

/**
 * @author Psyko
 * @date 5 avr. 2017
 */
public class GrblControllerService extends AbstractGrblControllerService<GrblMachineState,
																		GrblControllerService,
																		GrblState,
																		GrblConfiguration, 
																		GrblCommunicator,
																		GrblJogger,
																		GrblExecutor> implements IGrbl11ControllerService, IPropertyChangeListener{
	private static final GkLog LOG = GkLog.getLogger(GrblControllerService.class);
	/**  Service ID */
	public static final String SERVICE_ID = "Grbl v1.1 Controller";
	/** Execution time service - keep updated with maximum feed */
	private IGCodeExecutionTimeService gcodeExecutionTimeService;
	/** Probe utility */
	private ProbeUtility probeUtility;
	/**
	 * Constructor
	 * @param internalState
	 * @param configuration
	 * @param communicator
	 * @param gcodeService
	 * @throws GkException
	 */
	public GrblControllerService(GrblState internalState, GrblConfiguration configuration) throws GkException {
		super(internalState, configuration);
		addConfigurationListener(this);
		Grblv11Preferences.getInstance().addPropertyChangeListener(this);
	}
	//forcer le format du status report qqpart ?
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#setConfigurationSetting(java.lang.String, java.lang.String)
	 */
	@Override
	public void setConfigurationSetting(String identifier, String value) throws GkException {		
		String oldValue = configuration.getSettingStringValue(identifier);
		getConfiguration().setValue(identifier, value);		
		notifyConfigurationSettingChanged(identifier, oldValue, value);
	}

	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#getReportUnit()
	 */
	@Override
	public Unit<Length> getReportUnit() {
		if(getConfiguration().isCompletelyLoaded()){
			Boolean	reportInches = getConfiguration().getReportInches().getValue();			
			if(reportInches){
				return LengthUnit.INCH;
			}else{
				return LengthUnit.MILLIMETRE;
			}
		}
		return LengthUnit.MILLIMETRE;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#updateGCodeContext(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void updateGCodeContext(GCodeContext updatedGCodeContext) throws GkException {
		getInternalState().setGCodeContext(updatedGCodeContext);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#setCoordinateSystemOffset(org.goko.core.gcode.element.ICoordinateSystem, org.goko.core.math.Tuple6b)
	 */
	@Override
	public void setCoordinateSystemOffset(ICoordinateSystem coordinateSystem, Tuple6b offset) throws GkException {
		getInternalState().setCoordinateSystemOffset(coordinateSystem, offset);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#getFileExtension()
	 */
	@Override
	public String getFileExtension() {		
		return "grbl11.cfg";
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#probe(java.util.List)
	 */
	@Override
	public CompletionService<ProbeResult> probe(List<ProbeRequest> lstProbeRequest) throws GkException {
		probeUtility = new ProbeUtility(this, getGCodeService());
		probeUtility.prepare(lstProbeRequest);
		
		IGCodeProvider probeGCodeProvider = probeUtility.getProbeGCodeProvider();
		getExecutionService().clearExecutionQueue(ExecutionQueueType.SYSTEM);
		getExecutionService().addToExecutionQueue(ExecutionQueueType.SYSTEM, probeGCodeProvider);
		getExecutionService().beginQueueExecution(ExecutionQueueType.SYSTEM);
		
		return probeUtility.getExecutorCompletionService();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#createActionFactory()
	 */
	@Override
	protected ControllerActionFactory createActionFactory() throws GkException {
		return new GrblActionFactory(this);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#createJogger()
	 */
	@Override
	protected GrblJogger createJogger() {
		return new GrblJogger(this);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#createExecutor()
	 */
	@Override
	protected GrblExecutor createExecutor() {		
		return new GrblExecutor(this);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#startGrblService()
	 */
	@Override
	protected void startGrblService() {
		getStatusPoller().setPeriod( Grblv11Preferences.getInstance().getPollingPeriod() );
		getInternalState().addListener(this);
	}

	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineValueUpdate(MachineValueUpdateEvent evt){
		notifyListeners(evt);
	}
	
	protected void handleError(IGrblStatus error) throws GkException {
		releaseRxBuffer();
		
		String formattedErrorMessage = "Error: "+error.getLabel();
		if(getExecutionService().getExecutionState() == ExecutionState.RUNNING ||
			getExecutionService().getExecutionState() == ExecutionState.PAUSED ||
			getExecutionService().getExecutionState() == ExecutionState.ERROR ){		
			GCodeLine line = getExecutor().markNextLineAsError();
			logError(formattedErrorMessage, line);
		}else{
			logError(formattedErrorMessage, null);
		}
	}
	
	protected void logError(String errorMessage, GCodeLine line) throws GkException{
		String formattedErrorMessage = StringUtils.EMPTY;

		if(line != null){
			String lineStr = getGCodeService().render(line);
			formattedErrorMessage = "Error with command '"+lineStr+"' : "+ StringUtils.substringAfter(errorMessage, "error: ");
		}else{
			formattedErrorMessage = "Grbl "+ errorMessage;
		}

		LOG.error(formattedErrorMessage);
		getApplicativeLogService().error(formattedErrorMessage, getServiceId());
	}
		
	protected void handleOkResponse() throws GkException {
		releaseRxBuffer();	
		decreasePendingCommands();
		getInternalState().setAvailablePlannerBuffer( getInternalState().getAvailablePlannerBuffer() - 1);
		ExecutionState state = getExecutionService().getExecutionState();
		if( state == ExecutionState.RUNNING ||
			state == ExecutionState.PAUSED ||
			state == ExecutionState.ERROR ){
			getExecutor().confirmNextLineExecution();
		}
	}

	protected void handleToolLengthOffset(Length toolLengthOffset) throws GkException {
		getInternalState().setToolLengthOffset(toolLengthOffset);
	}
	
	protected void handleStatusReport(StatusReport statusReport) throws GkException {		
		getInternalState().setState(statusReport.getState());
		
		if ( getPlannerBufferCapacity() == null
				&& statusReport.getState() == GrblMachineState.IDLE
				&& statusReport.getAvailablePlannerBuffer() != null) {
			setPlannerBufferCapacity( statusReport.getAvailablePlannerBuffer() );
		}
		if (statusReport.getState() != GrblMachineState.ALARM) {
			getInternalState().setAlarmMessage(StringUtils.EMPTY);
		}
		if(statusReport.getCurrentWorkCoordinateOffset() != null){
			getInternalState().setCurrentWorkCoordinateOffset(statusReport.getCurrentWorkCoordinateOffset());
		}
		if(statusReport.getMachinePosition() != null){
			getInternalState().setMachinePosition(statusReport.getMachinePosition());
			if(getInternalState().getCurrentWorkCoordinateOffset() != null){
				getInternalState().setWorkPosition(statusReport.getMachinePosition().subtract(getInternalState().getCurrentWorkCoordinateOffset()));
			}
		}
		if(statusReport.getWorkPosition() != null){
			getInternalState().setWorkPosition(statusReport.getWorkPosition());	
			if(getInternalState().getCurrentWorkCoordinateOffset() != null){
				getInternalState().setMachinePosition(statusReport.getWorkPosition().add(getInternalState().getCurrentWorkCoordinateOffset()));
			}
		}
		if(statusReport.getAvailablePlannerBuffer() != null){
			getInternalState().setAvailablePlannerBuffer(statusReport.getAvailablePlannerBuffer());
		}
		if(statusReport.getAvailableRxBuffer() != null){
			getInternalState().setAvailableRxBuffer(statusReport.getAvailableRxBuffer());
		}
		if(statusReport.getOverrideFeed() != null){
			getInternalState().setOverrideFeed(statusReport.getOverrideFeed());
		}
		if(statusReport.getOverrideRapid() != null){
			getInternalState().setOverrideRapid(statusReport.getOverrideRapid());
		}
		if(statusReport.getOverrideSpindle() != null){
			getInternalState().setOverrideSpindle(statusReport.getOverrideSpindle());
		}	
		if(statusReport.getVelocity() != null){
			getInternalState().setVelocity(statusReport.getVelocity());
		}
		if(statusReport.getSpindleSpeed() != null){
			getInternalState().setSpindleSpeed(statusReport.getSpindleSpeed());
			getInternalState().setSpindleEnabled(statusReport.getSpindleSpeed() > 0);
		}		
		if(StringUtils.isNotBlank(statusReport.getSpindleDirection())){
			getInternalState().setSpindleDirection(statusReport.getSpindleDirection());
		}
		if(statusReport.getMistCoolantState() != null){
			getInternalState().setMistCoolantState(statusReport.getMistCoolantState());
		}
		if(statusReport.getFloodCoolantState() != null){
			getInternalState().setFloodCoolantState(statusReport.getFloodCoolantState());
		}
	}

	protected void handleAlarm(GrblMachineState alarmState) throws GkException{
		setState(GrblMachineState.ALARM);
		getInternalState().setAlarmMessage(alarmState.getLabel());
		
		if(getExecutionService().getExecutionState() != ExecutionState.IDLE){
			stopMotion();
		}		
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#stopMotion()
	 */
	@Override
	public void stopMotion() throws GkException {		
		super.stopMotion();
		
		if(probeUtility != null && probeUtility.isProbingInProgress()){
			probeUtility.cancelActiveProbing();			
			probeUtility.clearProbingGCode();
		}
//				la queue system n'est pas bien vidée ?
//				Protocole :
//					- faire un probe zero qui ne trigger pas
//					- charger un GCode et l'ajouter à la queue
//					- voir si les boutons de controle de la queue sont actifs ou non
		getExecutionService().stopQueueExecution();
	}
	
	protected void handleProbeResult(boolean probeSuccess, Tuple6b probePosition) throws GkException {
		if(probeUtility != null){
			probeUtility.handleProbeResult(probeSuccess, probePosition);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#setCheckModeEnabled(boolean)
	 */
	@Override
	public void setCheckModeEnabled(boolean enabled) throws GkException {
		if((enabled && ObjectUtils.equals(GrblMachineState.IDLE, getState())) || // Check mode is disabled and we want to enable it
			(!enabled && ObjectUtils.equals(GrblMachineState.CHECK, getState())) ){ // Check mode is enabled and we want to disable it
			getCommunicator().send(Grbl.CHECK_MODE, true);
		}else{
			throw new GkFunctionalException("GRBL-001", String.valueOf(enabled), getState().getLabel());
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#canExport()
	 */
	@Override
	public boolean canExport() throws GkException {		
		return canImport();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#canImport()
	 */
	@Override
	public boolean canImport() throws GkException {		
		return getCommunicator().isConnected() && GrblMachineState.IDLE.equals(getState());
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#isReadyToProbe()
	 */
	@Override
	public boolean isReadyToProbe() {		
		try {
			return getCommunicator().isConnected() && GrblMachineState.IDLE.equals(getState());
		} catch (GkException e) {
			LOG.error(e);
		}
		return false;
	}
	
	/**
	 * Handle the reception of the parser state 
	 * @param strParserState the string representation of the parser state 
	 * @throws getGCodeService() 
	 */
	public void handleParserState(String strParserState) throws GkException {
		GCodeContext context = getGCodeContext();
		String[] commands = StringUtils.split(strParserState, " ");
		if(commands != null){
			for (String strCommand : commands) {
				IGCodeProvider provider = getGCodeService().parse(strCommand);
				InstructionProvider instructions = getGCodeService().getInstructions(context, provider);
				context = getGCodeService().update(context, instructions);
			}
		}
		getInternalState().setGCodeContext(context);		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#requestStatus()
	 */
	@Override
	public void requestStatus() throws GkException {
		getCommunicator().requestStatus();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#onConnected()
	 */
	@Override
	public void onConnected() throws GkException {
		schedule().whenState(GrblMachineState.IDLE).execute(new Runnable() {
			
			@Override
			public void run() {
				try{
					GrblControllerService.this.refreshParserState();
					GrblControllerService.this.refreshConfiguration();
					GrblControllerService.this.refreshSpaceCoordinates();
				}catch(GkException e){
					LOG.error(e);
				}
			}
		}).timeout(5, TimeUnit.SECOND).begin();
		super.onConnected();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#onDisconnected()
	 */
	@Override
	public void onDisconnected() throws GkException {
		setState(GrblMachineState.UNDEFINED);
		setConfiguration( new GrblConfiguration() );
		super.onDisconnected();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#increaseSpindleSpeed1()
	 */
	@Override
	public void increaseSpindleSpeed1() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.SPINDLE_OVERRIDE_PLUS_1 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#increaseSpindleSpeed10()
	 */
	@Override
	public void increaseSpindleSpeed10() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.SPINDLE_OVERRIDE_PLUS_10 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#decreaseSpindleSpeed1()
	 */
	@Override
	public void decreaseSpindleSpeed1() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.SPINDLE_OVERRIDE_MINUS_1 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#decreaseSpindleSpeed10()
	 */
	@Override
	public void decreaseSpindleSpeed10() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.SPINDLE_OVERRIDE_MINUS_10 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#resetSpindleSpeed()
	 */
	@Override
	public void resetSpindleSpeed() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.SPINDLE_OVERRIDE_100 ,false);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#increaseFeedSpeed1()
	 */
	@Override
	public void increaseFeedSpeed1() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.FEED_OVERRIDE_PLUS_1 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#increaseFeedSpeed10()
	 */
	@Override
	public void increaseFeedSpeed10() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.FEED_OVERRIDE_PLUS_10 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#decreaseFeedSpeed1()
	 */
	@Override
	public void decreaseFeedSpeed1() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.FEED_OVERRIDE_MINUS_1 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#decreaseFeedSpeed10()
	 */
	@Override
	public void decreaseFeedSpeed10() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.FEED_OVERRIDE_MINUS_10 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#resetFeedSpeed()
	 */
	@Override
	public void resetFeedSpeed() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.FEED_OVERRIDE_100 ,false);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#setRapidSpeed25()
	 */
	@Override
	public void setRapidSpeed25() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.RAPID_OVERRIDE_25 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#setRapidSpeed50()
	 */
	@Override
	public void setRapidSpeed50() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.RAPID_OVERRIDE_50 ,false);
	}
	
	/**
	 * @throws GkException
	 */
	@Override
	public void resetRapidSpeed() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.RAPID_OVERRIDE_100 ,false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#toggleSpindle()
	 */
	@Override
	public void toggleSpindle() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.TOGGLE_SPINDLE ,false);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#toggleFloodCoolant()
	 */
	@Override
	public void toggleFloodCoolant() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.TOGGLE_FLOOD_COOLANT ,false);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblOverrideService#toggleMistCoolant()
	 */
	@Override
	public void toggleMistCoolant() throws GkException {
		getCommunicator().sendImmediately(Grbl.Commands.TOGGLE_MIST_COOLANT ,false);		
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

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.configuration.IGrblConfigurationListener#onConfigurationChanged(org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration)
	 */
	@Override
	public void onConfigurationChanged(GrblConfiguration configurhation) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.configuration.IGrblConfigurationListener#onConfigurationSettingChanged(org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onConfigurationSettingChanged(GrblConfiguration configuration, String identifier, String oldValue, String newValue) {
		Boolean	reportInches = getConfiguration().getReportInches().getValue();
		Unit<Speed> unit = SpeedUnit.MILLIMETRE_PER_MINUTE;
		if(reportInches != null && reportInches){
			unit = SpeedUnit.INCH_PER_MINUTE;	
		}
		if(StringUtils.equals(identifier, Grbl.Configuration.MAX_RATE_X)){
			if(StringUtils.isNotBlank(newValue)){
				gcodeExecutionTimeService.getExecutionConstraint().setXAxisMaximumFeed( Speed.valueOf(newValue, unit));
			}
		}
		if(StringUtils.equals(identifier, Grbl.Configuration.MAX_RATE_Y)){
			if(StringUtils.isNotBlank(newValue)){
				gcodeExecutionTimeService.getExecutionConstraint().setYAxisMaximumFeed( Speed.valueOf(newValue, unit));
			}
		}
		if(StringUtils.equals(identifier, Grbl.Configuration.MAX_RATE_Z)){
			if(StringUtils.isNotBlank(newValue)){
				gcodeExecutionTimeService.getExecutionConstraint().setZAxisMaximumFeed( Speed.valueOf(newValue, unit));
			}
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

	/**
	 * @param message
	 * @throws GkException 
	 */
	public void handleMessage(String message) throws GkException {
		getInternalState().setMessage(message);
	}
	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(StringUtils.equals(Grblv11Preferences.POLLING_PERIOD_MS, event.getProperty())){
			if(getStatusPoller() != null){
				getStatusPoller().setPeriod( Grblv11Preferences.getInstance().getPollingPeriod() );
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#onExecutionComplete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionComplete(IExecutionToken<ExecutionTokenState> token) throws GkException {
		if(probeUtility != null){
			probeUtility.clearProbingGCode();			
		}		
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#onExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(IExecutionToken<ExecutionTokenState> token) throws GkException {
		if(probeUtility != null){
			probeUtility.clearProbingGCode();
			probeUtility.cancelActiveProbing();
			probeUtility = null;
		}
	}
}
