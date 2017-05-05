/**
 * 
 */
package org.goko.controller.tinyg.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.controller.tinyg.commons.configuration.ITinyGConfigurationListener;
import org.goko.controller.tinyg.commons.configuration.TinyGGroupSettings;
import org.goko.controller.tinyg.commons.configuration.TinyGSetting;
import org.goko.controller.tinyg.commons.jog.AbstractTinyGJogger;
import org.goko.controller.tinyg.commons.schedule.TinyGScheduler;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.event.ObservableDelegate;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.controller.action.ControllerActionFactory;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.controller.event.IGCodeContextListener;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.controller.listener.IWorkVolumeUpdateListener;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContextObservable;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

import com.eclipsesource.json.JsonObject;

/**
 * @author Psyko
 * @date 8 janv. 2017
 */
public abstract class AbstractTinyGControllerService<T extends ITinyGControllerService<C>,
													 S extends AbstractTinyGState,
													 C extends AbstractTinyGConfiguration<C>,
													 P extends AbstractTinyGCommunicator<C, T>,
													 J extends AbstractTinyGJogger<C, T, P>,
													 X extends AbstractTinyGExecutor<T>> extends EventDispatcher implements ITinyGControllerService<C>, ITinyGConfigurationListener<C>{
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(AbstractTinyGControllerService.class);
	/** Bean holding the internal state of the controller */
	private S internalState;
	/** Action factory */
	private ControllerActionFactory actionFactory;	
	/** The TinyG Configuration*/
	private C configuration;
	/** The used communicator */
	private P communicator;
	/** GCode context listener */
	private ObservableDelegate<IGCodeContextListener<GCodeContext>> gcodeContextListener;
	/** The configuration listeners */
	private List<ITinyGConfigurationListener<C>> configurationListener;
	/** Jogger utility*/
	private J jogger;
	/** Executor */
	private X executor;
	/** GCode service */
	private IRS274NGCService gcodeService;
	/** Planner buffer check for execution */
	private boolean plannerBufferCheck = true;
	/** The execution monitor service */
	private IExecutionService<ExecutionTokenState, IExecutionToken<ExecutionTokenState>> executionService;
	/** UI Log service */	
	private IApplicativeLogService applicativeLogService;
	/** The work volume listener */
	private List<IWorkVolumeUpdateListener> workVolumeUpdateListener;
	
	/**
	 * Constructor
	 * @param internalState the internal state object
	 * @param configuration the configuration object 
	 * @param communicator the communicator object
	 * @throws GkException GkException 
	 */
	public AbstractTinyGControllerService(S internalState, C configuration, P communicator) throws GkException {
		super();
		this.internalState = internalState;
		this.configuration = configuration;
		this.communicator = communicator;
		this.gcodeContextListener = new GCodeContextObservable();
		this.configurationListener = new CopyOnWriteArrayList<ITinyGConfigurationListener<C>>();
		this.workVolumeUpdateListener = new CopyOnWriteArrayList<IWorkVolumeUpdateListener>();
		this.actionFactory = createActionFactory();		
		this.actionFactory.createActions();
		this.jogger = createJogger();
		this.executor = createExecutor();
		this.addConfigurationListener(this);
	}

	/**
	 * Creation of the action factory for this controller
	 * @return ControllerActionFactory
	 * @throws GkException GkException
	 */
	abstract protected ControllerActionFactory createActionFactory() throws GkException;
	
	/**
	 * Returns the bean holding the internal state of the controller
	 * @return S
	 */
	protected S getInternalState(){
		return internalState;
	}
	
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getPosition()
	 */
	@Override
	public Tuple6b getPosition() throws GkException {
		return getInternalState().getWorkPosition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#isReadyForFileStreaming()
	 */
	@Override
	public boolean isReadyForFileStreaming() throws GkException {
		return communicator.isConnected();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#verifyReadyForExecution()
	 */
	@Override
	public void verifyReadyForExecution() throws GkException {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getControllerAction(java.lang.String)
	 */
	@Override
	public IGkControllerAction getControllerAction(String actionId) throws GkException {		
		return actionFactory.get(actionId);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#isControllerAction(java.lang.String)
	 */
	@Override
	public boolean isControllerAction(String actionId) throws GkException {
		return actionFactory.exist(actionId);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValue(java.lang.String, java.lang.Class)
	 */
	@Override
	public <V> MachineValue<V> getMachineValue(String name, Class<V> clazz) throws GkException {
		return getInternalState().getValue(name, clazz);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueType(java.lang.String)
	 */
	@Override
	public Class<?> getMachineValueType(String name) throws GkException {
		return getInternalState().getControllerValueType(name);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getGCodeContext()
	 */
	@Override
	public GCodeContext getGCodeContext() throws GkException {
		return getInternalState().getGCodeContext();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition()
	 */
	@Override
	public List<MachineValueDefinition> getMachineValueDefinition() throws GkException {
		return getInternalState().getMachineValueDefinition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition getMachineValueDefinition(String id) throws GkException {
		return getInternalState().getMachineValueDefinition(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#findMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition findMachineValueDefinition(String id) throws GkException {
		return getInternalState().findMachineValueDefinition(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#cancelFileSending()
	 */
	@Override
	public void cancelFileSending() throws GkException {
		stopMotion();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#moveToAbsolutePosition(org.goko.core.math.Tuple6b)
	 */
	@Override
	public void moveToAbsolutePosition(Tuple6b position) throws GkException {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		LOG.info("Starting service ["+getServiceId()+"] using implementation ["+getClass()+"]");
		internalState.addListener(this);
		LOG.info("Service ["+getServiceId()+"] is running");
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#probe(org.goko.core.controller.bean.ProbeRequest)
	 */
	@Override
	public CompletionService<ProbeResult> probe(ProbeRequest probeRequest) throws GkException {
		List<ProbeRequest> lstProbeRequest = new ArrayList<ProbeRequest>();
		lstProbeRequest.add(probeRequest);
		return probe(lstProbeRequest);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGControllerService#checkReadyToProbe()
	 */
	@Override
	public void checkReadyToProbe() throws GkException {
		if(!isReadyToProbe()){
			throw new GkFunctionalException("TNG-COMMON-002");
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IFourAxisControllerAdapter#getA()
	 */
	@Override
	public Angle getA() throws GkException {		
		return getInternalState().getA();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getX()
	 */
	@Override
	public Length getX() throws GkException {		
		return getInternalState().getX();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getY()
	 */
	@Override
	public Length getY() throws GkException {
		return getInternalState().getY();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getZ()
	 */
	@Override
	public Length getZ() throws GkException {
		return getInternalState().getZ();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCoordinateSystemOffset(org.goko.core.gcode.element.ICoordinateSystem)
	 */
	@Override
	public Tuple6b getCoordinateSystemOffset(ICoordinateSystem cs) throws GkException {
		return getInternalState().getCoordinateSystemOffset(cs);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCurrentCoordinateSystem()
	 */
	@Override
	public ICoordinateSystem getCurrentCoordinateSystem() throws GkException {
		return getInternalState().getGCodeContext().getCoordinateSystem();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#setCoordinateSystemOffset(org.goko.core.gcode.element.ICoordinateSystem, org.goko.core.math.Tuple6b)
	 */
	@Override
	public void setCoordinateSystemOffset(ICoordinateSystem coordinateSystem, Tuple6b offset) throws GkException {
		getInternalState().setCoordinateSystemOffset(coordinateSystem, offset);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#jog(org.goko.core.controller.bean.EnumControllerAxis, org.goko.core.common.measure.quantity.Length, org.goko.core.common.measure.quantity.Speed)
	 */
	@Override
	public void jog(EnumControllerAxis axis, Length step, Speed feedrate) throws GkException {
		jogger.jog(axis, step, feedrate);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#stopJog()
	 */
	@Override
	public void stopJog() throws GkException {
		jogger.stopJog();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#addUpdateListener(org.goko.core.controller.listener.IWorkVolumeUpdateListener)
	 */
	@Override
	public void addUpdateListener(IWorkVolumeUpdateListener listener) {
		this.workVolumeUpdateListener.add(listener);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#removeUpdateListener(org.goko.core.controller.listener.IWorkVolumeUpdateListener)
	 */
	@Override
	public void removeUpdateListener(IWorkVolumeUpdateListener listener) {
		this.workVolumeUpdateListener.remove(listener);
	}
	
	/**
	 * Notify the registered IWorkVolumeUpdateListener
	 */
	protected void notifyWorkVolumeUpdate(){
		for (IWorkVolumeUpdateListener listener : workVolumeUpdateListener) {
			listener.onWorkVolumeUpdate();
		}
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
	 * @see org.goko.core.controller.IWorkVolumeProvider#getWorkVolumeMaximalPosition()
	 */
	@Override
	public Tuple6b getWorkVolumeMaximalPosition() throws GkException {
		Tuple6b min = findWorkVolumeMaximalPosition();
		if(min == null){
			 throw new GkTechnicalException("No maximal position currently defined for the travel max position");
		}
		return min;
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
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#importFrom(java.io.InputStream)
	 */
	@Override
	public void importFrom(InputStream inputStream) throws GkException {
		C currentConfiguration = getConfiguration();
		try {
			JsonObject jsonCfg = JsonObject.readFrom(new InputStreamReader(inputStream));
			configuration.setFromJson(jsonCfg);			
			C differentialConfig = currentConfiguration.getDifferentialConfiguration(configuration);
			communicator.sendConfigurationUpdate(differentialConfig);			
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionStart()
	 */
	@Override
	public void onQueueExecutionStart() throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionStart(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionStart(IExecutionToken<ExecutionTokenState> token) throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(IExecutionToken<ExecutionTokenState> token) throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionPause(IExecutionToken<ExecutionTokenState> token) throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionResume(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionResume(IExecutionToken<ExecutionTokenState> token) throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionComplete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionComplete(IExecutionToken<ExecutionTokenState> token) throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionCanceled()
	 */
	@Override
	public void onQueueExecutionCanceled() throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeLineExecutionListener#onLineStateChanged(org.goko.core.gcode.execution.IExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onLineStateChanged(IExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.common.event.IObservable#addObserver(java.lang.Object)
	 */
	@Override
	public void addObserver(IGCodeContextListener<GCodeContext> observer) {
		gcodeContextListener.addObserver(observer);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.event.IObservable#removeObserver(java.lang.Object)
	 */
	@Override
	public boolean removeObserver(IGCodeContextListener<GCodeContext> observer) {
		return gcodeContextListener.removeObserver(observer);
	}

	/**
	 * @return the configuration
	 */
	public C getConfiguration() {
		return configuration.getCopy();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#applyConfiguration(org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration)
	 */
	@Override
	public void applyConfiguration(C newConfiguration) throws GkException {
		C differentialConfig = newConfiguration;
		if(this.configuration != null){
			differentialConfig = this.configuration.getDifferentialConfiguration(newConfiguration);
		}
		this.configuration = newConfiguration;
		getCommunicator().sendConfigurationUpdate(differentialConfig);
	}
	/**
	 * @return the communicator
	 */
	public P getCommunicator() {
		return communicator;
	}

	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineValueUpdate(MachineValueUpdateEvent evt){
		notifyListeners(evt);
	}
	
	/**
	 * @return the actionFactory
	 */
	public ControllerActionFactory getActionFactory() {
		return actionFactory;
	}
	

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#getCurrentUnit()
	 */
	@Override
	public Unit<Length> getCurrentUnit() {		
		return getInternalState().getGCodeContext().getUnit().getUnit();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#setState(org.goko.core.controller.bean.MachineState)
	 */
	@Override
	public void setState(MachineState state) throws GkException {
		getInternalState().setState(state);
		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#getState()
	 */
	@Override
	public MachineState getState() throws GkException {		
		return getInternalState().getState();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#setVelocity(org.goko.core.common.measure.quantity.Speed)
	 */
	@Override
	public void setVelocity(Speed velocity) throws GkException {
		getInternalState().setVelocity(velocity);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#getVelocity()
	 */
	@Override
	public Speed getVelocity() throws GkException {		
		return getInternalState().getVelocity();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#updateGCodeContext(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void updateGCodeContext(GCodeContext updatedGCodeContext) throws GkException{
		GCodeContext current = getInternalState().getGCodeContext();
		if(updatedGCodeContext.getPosition() != null){
			current.setPosition(updatedGCodeContext.getPosition());
		}
		if(updatedGCodeContext.getMachinePosition() != null){
			current.setMachinePosition(updatedGCodeContext.getMachinePosition());
		}
		if(updatedGCodeContext.getCoordinateSystem() != null){
			current.setCoordinateSystem(updatedGCodeContext.getCoordinateSystem());
		}
		if(updatedGCodeContext.getMotionMode() != null){
			current.setMotionMode(updatedGCodeContext.getMotionMode());
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
		if(updatedGCodeContext.getActiveToolNumber() != null){
			current.setActiveToolNumber(updatedGCodeContext.getActiveToolNumber());
		}
		if(updatedGCodeContext.getSelectedToolNumber() != null){
			current.setSelectedToolNumber(updatedGCodeContext.getSelectedToolNumber());
		}
		getInternalState().setGCodeContext(current);
		gcodeContextListener.getEventDispatcher().onGCodeContextEvent(current);
	}
	
	/**
	 * Adds the given {@link ITinyGConfigurationListener} as a listener to this service configuration event
	 * @param listener the listener to add
	 */	
	public void addConfigurationListener(ITinyGConfigurationListener<C> listener) {
		if(!configurationListener.contains(listener)){
			configurationListener.add(listener);
		}
	}
	
	/**
	 * Removes the given {@link ITinyGConfigurationListener} from the listener of this service configuration event
	 * @param listener the listener to remove
	 */
	public void removeConfigurationListener(ITinyGConfigurationListener<C> listener) {
		configurationListener.remove(listener);
	}
	
	/**
	 * Notifies the registered listeners for a configuration change
	 */
	private void notifyConfigurationChanged(C oldConfig, C newConfig){
		if(!newConfig.isCompletelyLoaded()){
			return;
		}
		for (ITinyGConfigurationListener<C> listener : configurationListener) {
			listener.onConfigurationChanged(oldConfig, newConfig);
		}
	}
	
	/**
	 * Notifies the registered listeners for a configuration change
	 */
	private void notifySettingsChanged(C oldConfig, C newConfig){
		if(!newConfig.isCompletelyLoaded()){
			return;
		}
		try{	
			boolean testo = oldConfig.isCompletelyLoaded();
			boolean testn = newConfig.isCompletelyLoaded();
			C cfg = oldConfig.getDifferentialConfiguration(newConfig);		
			List<TinyGGroupSettings> lstGroups = cfg.getGroups();
			newConfig.isCompletelyLoaded();
			for (TinyGGroupSettings group : lstGroups) {
				for (TinyGSetting<?> setting : group.getSettings()) {
					if(setting.getValue() != null){
						for (ITinyGConfigurationListener<C> listener : configurationListener) {
							listener.onConfigurationSettingChanged(oldConfig,
																	newConfig,
																	group.getGroupIdentifier(),
																	setting.getIdentifier());
																	
						}	
					}	
				}
			}			
		}catch(GkException e){
			LOG.error(e);
		}
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(C configuration) {
		C oldConfig = getConfiguration();
		C newConfig = configuration;		 
		this.configuration = configuration;
		// Notify changed settings
		this.notifySettingsChanged(oldConfig, newConfig);		
		// Notify global configuration change
		this.notifyConfigurationChanged(oldConfig, newConfig);				
	}
	
	/**
	 * Resets the configuration
	 */
	public abstract void resetConfiguration();
	
	
	/**
	 * Creates the jogging utility
	 * @return J
	 */
	abstract protected J createJogger();

	/**
	 * Creates the executor for this service
	 * @return X
	 */
	protected abstract X createExecutor() ;
	
	/**
	 * Handle probe result
	 * @param probeSuccess <code>true</code> if the probed succeed, <code>false</code> otherwise
	 * @param probePosition the probed position
	 * @throws GkException GkException
	 */
	public abstract void handleProbeResult(boolean probeSuccess, Tuple6b probePosition) throws GkException;
	
	/**
	 * @return the jogger
	 */
	public J getJogger() {
		return jogger;
	}
		
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#schedule()
	 */
	@Override
	public TinyGScheduler schedule(){
		return new TinyGScheduler(this);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#send(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	public void send(GCodeLine line) throws GkException {
		String gcodeString = gcodeService.render(line);
		if(StringUtils.isNotBlank(gcodeString)){
			communicator.sendGCode(gcodeString);
		}else{						
			getExecutor().confirmLineExecution(line);
		}		
	}

	/**
	 * @return the executor
	 */
	public X getExecutor() {
		return executor;
	}

	/**
	 * @return the gcodeService
	 */
	public IRS274NGCService getGCodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGCodeService(IRS274NGCService gcodeService) {
		this.gcodeService = gcodeService;
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#isPlannerBufferCheck()
	 */
	@Override
	public boolean isPlannerBufferCheck() {		
		return plannerBufferCheck;
	}
	

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.ITinyGControllerService#setPlannerBufferCheck(boolean)
	 */
	@Override
	public void setPlannerBufferCheck(boolean plannerBufferCheck) {
		this.plannerBufferCheck = plannerBufferCheck;
	}
	
	/**
	 * @return the executionService
	 */
	public IExecutionService<ExecutionTokenState, IExecutionToken<ExecutionTokenState>> getExecutionService() {
		return executionService;
	}

	/**
	 * @param executionService the executionService to set
	 * @throws GkException GkException 
	 */
	public void setExecutionService(IExecutionService<ExecutionTokenState, IExecutionToken<ExecutionTokenState>> executionService) throws GkException {
		this.executionService = executionService;
		if(this.executionService != null){
			this.executionService.setExecutor(getExecutor());
			this.executionService.addExecutionListener(ExecutionQueueType.DEFAULT, getExecutor());
		}
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
		
}
