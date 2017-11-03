/*
 *
 *   Goko
 *   Copyright (C) 2013, 2016  PsyKo
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
package org.goko.controller.grbl.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;
import org.goko.controller.grbl.commons.configuration.IGrblConfigurationListener;
import org.goko.controller.grbl.commons.configuration.settings.GrblSetting;
import org.goko.controller.grbl.commons.jog.AbstractGrblJogger;
import org.goko.controller.grbl.commons.schedule.GrblScheduler;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.event.ObservableDelegate;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.connection.serial.ISerialConnectionService;
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
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystemFactory;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContextObservable;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;


/**
 * @author Psyko
 * @date 9 avr. 2017
 */
public abstract class AbstractGrblControllerService <M extends MachineState,
													 T extends IGrblControllerService<C, M>,													 
													 S extends AbstractGrblState<M>,
													 C extends AbstractGrblConfiguration<C>,
													 P extends AbstractGrblCommunicator<C, M, T>,
													 J extends AbstractGrblJogger<C, M, T>,
													 X extends AbstractGrblExecutor<T>> extends EventDispatcher implements IGrblControllerService<C, M>,IGCodeExecutionListener<ExecutionTokenState, IExecutionToken<ExecutionTokenState>> {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(AbstractGrblControllerService.class);
	/** GCode service*/
	private IRS274NGCService gcodeService;
	/** Status polling */
	private GrblStatusPoller statusPoller;
	/** Controller action factory*/
	private ControllerActionFactory actionFactory;
	/** Grbl configuration */
	protected C configuration;
	/** The configuration listeners */
	private List<IGrblConfigurationListener<C>> configurationListener;
	/** Applicative log service */
	private IApplicativeLogService applicativeLogService;
	/** Grbl state object */
	private S grblState;
	/** Grbl communicator */
	private P communicator;
	/** The monitor service */
	private IExecutionService<ExecutionTokenState, IExecutionToken<ExecutionTokenState>> executionService;
	/** The Grbl Executor */
	private X executor;
	/** The history of used buffer for the last sent command s*/
	private LinkedBlockingQueue<Integer> usedBufferStack;
	/** GCode context listener delegate */
	private ObservableDelegate<IGCodeContextListener<GCodeContext>> gcodeContextListener;
	/** Jog runnable */
	private J jogger;
	public AtomicInteger pendingCommandsCount;
	public Integer plannerBufferCapacity;
	
	/**
	 * Constructor
	 * @throws GkException GkException 
	 */
	public AbstractGrblControllerService(S internalState, C configuration) throws GkException {
		this.configuration = configuration;
		this.usedBufferStack = new LinkedBlockingQueue<Integer>();
		this.gcodeContextListener = new GCodeContextObservable();		
		this.configurationListener = new CopyOnWriteArrayList<IGrblConfigurationListener<C>>();
		this.grblState = internalState;
		this.actionFactory = createActionFactory();		
		this.actionFactory.createActions();
		this.jogger = createJogger();
		this.executor = createExecutor();
		this.statusPoller = new GrblStatusPoller(this);		
		getInternalState().setActivePolling(true);
		this.pendingCommandsCount = new AtomicInteger(0);
	}

	/**
	 * Creation of the action factory for this controller
	 * @return ControllerActionFactory
	 * @throws GkException GkException
	 */
	abstract protected ControllerActionFactory createActionFactory() throws GkException;
	
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
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		LOG.info("Starting " + getServiceId());
		startGrblService();
		LOG.info("Successfully started " + getServiceId());
	}

	/**
	 * Creates the executor for this service
	 * @return X
	 */
	protected abstract void startGrblService() ;
	
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
		//persistValues();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.IGrblControllerService#send(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	public void send(GCodeLine gCodeLine) throws GkException{
		String cmd = gcodeService.render(gCodeLine);
		List<Byte> byteCommand = GkUtils.toBytesList(cmd);		
		int usedBufferCount = CollectionUtils.size(byteCommand);
		// Increment before we even send, to make sure we have enough space
		consumeRxBuffer(usedBufferCount + 2); // Dirty hack for end of line chars
		increasePendingCommands();
		communicator.send( byteCommand, true);
		
	}

	/**
	 * Register a quantity of space buffer being used for the last sent data
	 * @param amount the amount of used space
	 * @throws GkException GkException
	 */
	protected void consumeRxBuffer(int amount) throws GkException{
		usedBufferStack.add(amount);
		setAvailableRxBuffer(getAvailableRxBuffer() - amount);		
	}

	/**
	 * Decrement the used serial buffer by depiling the size of the send data, in reverse order
	 * @throws GkException GkException
	 */
	protected void releaseRxBuffer() throws GkException{
		if(CollectionUtils.isNotEmpty(usedBufferStack)){
			Integer amount = usedBufferStack.poll();
			setAvailableRxBuffer(getAvailableRxBuffer() + amount);			
		}
	}
		
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getPosition()
	 */
	@Override
	public Tuple6b getPosition() throws GkException {
		return grblState.getWorkPosition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getPosition(org.goko.core.gcode.element.ICoordinateSystem)
	 */
	@Override
	public Tuple6b getPosition(ICoordinateSystem coordinateSystem) throws GkException {
		Tuple6b pos = getAbsolutePosition();
		pos.subtract(getCoordinateSystemOffset(coordinateSystem));
		return pos;	
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getAbsolutePosition()
	 */
	@Override
	public Tuple6b getAbsolutePosition() throws GkException {
		//return grblState.getWorkPosition();
		throw new GkTechnicalException("TO DO"); // FIXME
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getX()
	 */
	@Override
	public Length getX() throws GkException {
		Length xPos = grblState.getWorkPosition().getX();
		return xPos;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getY()
	 */
	@Override
	public Length getY() throws GkException {
		Length yPos = grblState.getWorkPosition().getY();
		return yPos;
	}

	/** (inheritDoc)
	 * @throws GkException
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getZ()
	 */
	@Override
	public Length getZ() throws GkException {
		Length zPos = grblState.getWorkPosition().getZ();
		return zPos;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#isReadyForFileStreaming()
	 */
	@Override
	public boolean isReadyForFileStreaming() throws GkException {
		return communicator.isConnected();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getControllerAction(java.lang.String)
	 */
	@Override
	public IGkControllerAction getControllerAction(String actionId) throws GkException {
		IGkControllerAction action = actionFactory.find(actionId);
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
		return actionFactory.find(actionId) != null;
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
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition()
	 */
	@Override
	public List<MachineValueDefinition> getMachineValueDefinition() throws GkException {
		return getInternalState().getMachineValueDefinition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#findMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition findMachineValueDefinition(String id) throws GkException {
		return getInternalState().findMachineValueDefinition(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition getMachineValueDefinition(String id) throws GkException {
		return getInternalState().getMachineValueDefinition(id);
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
	public void setConnectionService(ISerialConnectionService connectionService) throws GkException {
		this.communicator.setConnectionService(connectionService);
	}

	/**
	 * Refresh the status of the remote Grbl controller
	 * @throws GkException GkException
	 */
	public void refreshStatus() throws GkException{
		communicator.send( Grbl.CURRENT_STATUS, false );		
	}

	public void refreshSpaceCoordinates() throws GkException{
		communicator.send( Grbl.VIEW_PARAMETERS, true );
	}

	public void refreshParserState() throws GkException{
		communicator.send(Grbl.PARSER_STATE, true );
	}

	public void refreshConfiguration() throws GkException{
		communicator.send( Grbl.CONFIGURATION, true );
	}

	protected void receiveParserState(String parserState) throws GkException {
		String[] commands = StringUtils.split(parserState," ");
		GCodeContext context = getGCodeContext();
		if(commands != null){
			for (String strCommand : commands) {
				IGCodeProvider provider = gcodeService.parse(strCommand);
				InstructionProvider instructions = gcodeService.getInstructions(context, provider);
				context = gcodeService.update(context, instructions);
			}
		}
		getInternalState().setGCodeContext(context);
		gcodeContextListener.getEventDispatcher().onGCodeContextEvent(context);
	}

	protected void initialiseConnectedState() throws GkException{
		setAvailableRxBuffer(0);
		refreshConfiguration();
		refreshSpaceCoordinates();
		refreshParserState();
	}


	@Override
	public M getState() throws GkException{
		return grblState.getState();
	}

	public void setState(M state) throws GkException{
		getInternalState().setState(state);
	}

	/*
	 *  Action related methods
	 */

	public void startHomingSequence() throws GkException{
		List<Byte> homeCommand = new ArrayList<Byte>();
		homeCommand.addAll(GkUtils.toBytesList(Grbl.HOME_COMMAND));
		communicator.send( homeCommand, true );
	}

	/**
	 * Pause the motion by sending a pause character to Grbl
	 * If the execution queue is not empty, it is also paused
	 * @throws GkException GkException
	 */
	public void pauseMotion() throws GkException{
		List<Byte> pauseCommand = new ArrayList<Byte>();
		pauseCommand.add(Grbl.PAUSE_COMMAND);
		communicator.sendImmediately( pauseCommand, false );
		if(executionService.getExecutionState() != ExecutionState.IDLE){
			executionService.pauseQueueExecution();
		}
	}

	/**
	 * Stop the motion by sending a pause and a flush character to Grbl
	 * If the execution queue is not empty, it is also stopped and emptied
	 * @throws GkException GkException
	 */
	public void stopMotion() throws GkException{
		List<Byte> stopCommand = new ArrayList<Byte>();
		stopCommand.add(Grbl.PAUSE_COMMAND);
		stopCommand.add(Grbl.RESET_COMMAND);
		
		communicator.sendImmediately(stopCommand, true);
		if(executionService.getExecutionState() != ExecutionState.IDLE){
			executionService.stopQueueExecution();
		}
		setAvailableRxBuffer(0);
		usedBufferStack.clear();
	}

	/**
	 * Start the motion by sending a resume character to Grbl
	 * If the execution queue is paused, it is also resumed
	 * @throws GkException GkException
	 */
	public void startMotion() throws GkException{
		List<Byte> startResumeCommand = new ArrayList<Byte>();
		startResumeCommand.add(Grbl.RESUME_COMMAND);
		communicator.send( startResumeCommand, false );
		if(executionService.getExecutionState() == ExecutionState.PAUSED){
			executionService.resumeQueueExecution();
		}else if(executionService.getExecutionState() == ExecutionState.RUNNING){
			executionService.beginQueueExecution(ExecutionQueueType.DEFAULT);
		}
	}
	
	public void resumeMotion() throws GkException{
		List<Byte> startResumeCommand = new ArrayList<Byte>();
		startResumeCommand.add(Grbl.RESUME_COMMAND);
		communicator.send( startResumeCommand, false );		
		executionService.resumeQueueExecution();		
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IStepJogService#stopJog()
	 */
	@Override
	public void stopJog() throws GkException {
		jogger.stopJog();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblControllerService#resetZero(java.util.List)
	 */
	@Override
	public void resetZero(List<String> axes) throws GkException {
		List<Byte> lstBytes = GkUtils.toBytesList("G92");
		if(CollectionUtils.isNotEmpty(axes)){
			for (String axe : axes) {
				lstBytes.addAll(GkUtils.toBytesList(axe+"0"));
			}
		}else{
			lstBytes.addAll( GkUtils.toBytesList("X0Y0Z0"));
		}
		communicator.send(lstBytes, true);
	}

	public void killAlarm() throws GkException{
		communicator.send("$X", true);
	}

	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#getAvailableRxBuffer()
	 */
	@Override
	public int getAvailableRxBuffer() throws GkException {
		return grblState.getAvailableRxBuffer();
	}

	/**
	 * @param usedGrblBuffer the usedGrblBuffer to set
	 * @throws GkException GkException
	 */
	public void setAvailableRxBuffer(int usedGrblBuffer) throws GkException {
		grblState.setAvailableRxBuffer(usedGrblBuffer);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#getAvailableGrblPlannerBuffer()
	 */
	@Override
	public int getAvailablePlannerBuffer() throws GkException {		
		return grblState.getAvailablePlannerBuffer();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#isPlannerBufferEmpty()
	 */
	@Override
	public boolean isPlannerBufferEmpty() throws GkException {
		return getAvailablePlannerBuffer() >= plannerBufferCapacity;
	}
	
	/**
	 * @return 
	 * @return the configuration
	 */
	@Override
	public C getConfiguration() {
		return configuration;
	}


	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#setConfiguration(org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration)
	 */
	@Override
	public void setConfiguration(C configuration) throws GkException {
		this.configuration = configuration;
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#applyConfiguration(org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration)
	 */
	@Override
	public void applyConfiguration(C configuration) throws GkException {	
		if(CollectionUtils.isNotEmpty( configuration.getLstGrblSetting() )){
			List<GrblSetting<?>> lstSetting = configuration.getLstGrblSetting();
			
			for (GrblSetting<?> newGrblSetting : lstSetting) {				
				communicator.send( newGrblSetting.getIdentifier()+"="+newGrblSetting.getValueAsString() , true );			
				// Start of dirty hack to avoid flooding Grbl RX buffer. Need to work on a proper solution
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
				// End of dirty hack to avoid flooding Grbl RX buffer. Need to work on a proper solution
			}			
		}
		notifyConfigurationChanged();
		this.configuration = configuration;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#moveToAbsolutePosition(org.goko.core.math.Tuple6b)
	 */
	@Override
	public void moveToAbsolutePosition(Tuple6b position) throws GkException {

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

	public S getInternalState() {
		return grblState;
	}

	protected void setCoordinateSystemOffset(CoordinateSystem coordinateSystem, Tuple6b value) throws GkException{
		getInternalState().setCoordinateSystemOffset(coordinateSystem, value);
		gcodeContextListener.getEventDispatcher().onGCodeContextEvent(getGCodeContext());
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getGCodeContext()
	 */
	@Override
	public GCodeContext getGCodeContext() throws GkException {
		return getInternalState().getGCodeContext();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCoordinateSystemOffset(org.goko.core.gcode.element.ICoordinateSystem)
	 */
	@Override
	public Tuple6b getCoordinateSystemOffset(ICoordinateSystem cs) throws GkException {
		return getInternalState().getCoordinateSystemOffset(cs);
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
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#getCurrentCoordinateSystem()
	 */
	@Override
	public ICoordinateSystem getCurrentCoordinateSystem() throws GkException {
		return getInternalState().getGCodeContext().getCoordinateSystem();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#setCurrentCoordinateSystem(org.goko.core.gcode.element.ICoordinateSystem)
	 */
	@Override
	public void setCurrentCoordinateSystem(ICoordinateSystem cs) throws GkException {
		communicator.send( cs.getCode(), true );		
		communicator.send( "$G", true );
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#resetCurrentCoordinateSystem()
	 */
	@Override
	public void resetCurrentCoordinateSystem() throws GkException {
		ICoordinateSystem cs = getInternalState().getGCodeContext().getCoordinateSystem();
		String cmd = "G10";
		switch (cs.getCode()) {
		case "G54": cmd +="P1";
		break;
		case "G55": cmd +="P2";
		break;
		case "G56": cmd +="P3";
		break;
		case "G57": cmd +="P4";
		break;
		case "G58": cmd +="P5";
		break;
		case "G59": cmd +="P6";
		break;
		default: throw new GkFunctionalException("GRBL-002", cs.getCode());
		}
		Tuple6b offsets = getCoordinateSystemOffset(getCurrentCoordinateSystem());
		Tuple6b mPos = new Tuple6b(getPosition());
		mPos = mPos.add(offsets);
		cmd += "L2";
		cmd += "X"+getPositionAsString(mPos.getX());
		cmd += "Y"+getPositionAsString(mPos.getY());
		cmd += "Z"+getPositionAsString(mPos.getZ());
		communicator.send(cmd, true);
		communicator.send(Grbl.VIEW_PARAMETERS, true );
	}


	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#updateCoordinateSystemPosition(org.goko.core.gcode.element.ICoordinateSystem, org.goko.core.math.Tuple6b)
	 */
	@Override
	public void updateCoordinateSystemPosition(ICoordinateSystem cs, Tuple6b position) throws GkException {
		String cmd = "G10";
		switch (cs.getCode()) {
		case "G54": cmd +="P1";
		break;
		case "G55": cmd +="P2";
		break;
		case "G56": cmd +="P3";
		break;
		case "G57": cmd +="P4";
		break;
		case "G58": cmd +="P5";
		break;
		case "G59": cmd +="P6";
		break;
		default: throw new GkFunctionalException("GRBL-002", cs.getCode());
		}
		Tuple6b mPos = new Tuple6b(position);
		cmd += "L2";
		cmd += "X"+getPositionAsString(mPos.getX());
		cmd += "Y"+getPositionAsString(mPos.getY());
		cmd += "Z"+getPositionAsString(mPos.getZ());
		communicator.send(cmd, true);
		communicator.send(Grbl.VIEW_PARAMETERS, true );
	}
	
	/**
	 * Returns the given Length quantity as a String, formatted using the goko preferences for decimal numbers
	 * @param q the quantity to format
	 * @return a String
	 * @throws GkException GkException
	 */
	protected String getPositionAsString(Length q) throws GkException{
		return GokoPreference.getInstance().format( q.to(getCurrentUnit()), true, false);
	}

	/**
	 * Returns the current unit in the Grbl Conteext. It can be different from the unit in the goko preferences
	 * @return Unit
	 */
	public Unit<Length> getCurrentUnit() {
		return getInternalState().getContextUnit().getUnit();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.IGrblControllerService#setActivePollingEnabled(boolean)
	 */
	@Override
	public void setActivePollingEnabled(boolean enabled) throws GkException {
		getInternalState().setActivePolling(enabled);
		if(enabled){
			statusPoller.start();
		}else{
			statusPoller.stop();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.IGrblControllerService#isActivePollingEnabled()
	 */
	@Override
	public boolean isActivePollingEnabled() throws GkException {
		return getInternalState().isActivePolling();
	}

	/**
	 * @return the monitorService
	 */
	public IExecutionService<ExecutionTokenState, IExecutionToken<ExecutionTokenState>> getExecutionService() {
		return executionService;
	}
	/**
	 * @param monitorService the monitorService to set
	 * @throws GkException GkException
	 */
	public void setExecutionService(IExecutionService<ExecutionTokenState, IExecutionToken<ExecutionTokenState>> executionService) throws GkException {
		this.executionService = executionService;
		this.executor.setExecutionService(executionService);
		this.executionService.addExecutionListener(ExecutionQueueType.DEFAULT, getExecutor());
		this.executionService.addExecutionListener(ExecutionQueueType.SYSTEM, getExecutor());
		this.executionService.addExecutionListener(ExecutionQueueType.DEFAULT, this);
		this.executionService.addExecutionListener(ExecutionQueueType.SYSTEM, this);
		this.executionService.setExecutor(executor);//new GrblDebugExecutor(gcodeService));
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#exportTo(java.io.OutputStream)
	 */
	@Override
	public void exportTo(OutputStream stream) throws GkException {
		C config = getConfiguration();
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
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#importFrom(java.io.InputStream)
	 */
	@Override
	public void importFrom(InputStream inputStream) throws GkException {
		C cfg = getConfiguration();
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
		applyConfiguration(cfg);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#jog(org.goko.core.controller.bean.EnumControllerAxis, org.goko.core.common.measure.quantity.Length, org.goko.core.common.measure.quantity.Speed)
	 */
	@Override
	public void jog(EnumControllerAxis axis, Length step, Speed feedrate) throws GkException {
		jogger.jog(axis, step, feedrate);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#verifyReadyForExecution()
	 */
	@Override
	public void verifyReadyForExecution() throws GkException {
		if(!isReadyForFileStreaming()){
			throw new GkFunctionalException("GRBL-003");
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#probe(org.goko.core.controller.bean.EnumControllerAxis, double, double)
	 */
	@Override
	public CompletionService<ProbeResult> probe(ProbeRequest probeRequest) throws GkException {
		List<ProbeRequest> lstProbeRequest = new ArrayList<ProbeRequest>();
		lstProbeRequest.add(probeRequest);
		return probe(lstProbeRequest);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#checkReadyToProbe()
	 */
	@Override
	public void checkReadyToProbe() throws GkException {
		if(!isReadyToProbe()){
			throw new GkFunctionalException("GRBL-003");
		}
	}


	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#addConfigurationListener(org.goko.controller.grbl.commons.configuration.IGrblConfigurationListener)
	 */
	@Override
	public void addConfigurationListener(IGrblConfigurationListener<C> listener) {
		if(!configurationListener.contains(listener)){
			configurationListener.add(listener);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.IGrblControllerService#removeConfigurationListener(org.goko.controller.grbl.v09.configuration.IGrblConfigurationListener)
	 */
	@Override
	public void removeConfigurationListener(IGrblConfigurationListener<C> listener) {
		configurationListener.remove(listener);
	}
	
	/**
	 * Notifies the registered listeners for a configuration change
	 * @throws GkException GkException 
	 */
	private void notifyConfigurationChanged() throws GkException{
		C configurationCopy = getConfiguration();
		for (IGrblConfigurationListener<C> listener : configurationListener) {
			listener.onConfigurationChanged(configurationCopy);
		}
	}
	
	/**
	 * Notifies the registered listeners for a configuration setting change
	 * @param oldValue 
	 * @param newValue 
	 * @param the identifier of the setting that changed
	 * @throws GkException GkException 
	 */
	protected void notifyConfigurationSettingChanged(String identifier, String oldValue, String newValue) throws GkException{
		C configurationCopy = getConfiguration();
		for (IGrblConfigurationListener<C> listener : configurationListener) {
			listener.onConfigurationSettingChanged(configurationCopy, identifier, oldValue, newValue);
		}
	}
			
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.IGrblControllerService#resetGrbl()
	 */
	@Override
	public void resetGrbl() throws GkException {		
		communicator.sendImmediately(Grbl.RESET_COMMAND, false);
	}

	/**
	 * @return the executor
	 */
	public X getExecutor() {
		return executor;
	}

	/**
	 * @return the communicator
	 */
	public P getCommunicator() {
		return communicator;
	}

	/**
	 * @param communicator the communicator to set
	 */
	public void setCommunicator(P communicator) {
		this.communicator = communicator;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGcodeService(IRS274NGCService gcodeService) {
		this.gcodeService = gcodeService;
	}

	/**
	 * @return the statusPoller
	 */
	public GrblStatusPoller getStatusPoller() {
		return statusPoller;
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#schedule()
	 */
	@Override
	public GrblScheduler schedule() {		
		return new GrblScheduler(this);
	}
	

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#onConnected()
	 */
	@Override
	public void onConnected() throws GkException {		
		getStatusPoller().start();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.IGrblControllerService#onDisconnected()
	 */
	@Override
	public void onDisconnected() throws GkException {
		getStatusPoller().stop();
		plannerBufferCapacity = null;
	}

	/**
	 * @return the pendingCommandsCount
	 */
	public int getPendingCommandsCount() {
		return pendingCommandsCount.get();
	}
	
	protected void decreasePendingCommands(){
		pendingCommandsCount.set(Math.max(0, pendingCommandsCount.get() - 1));
	}
	
	protected void increasePendingCommands(){
		pendingCommandsCount.set(Math.max(0, pendingCommandsCount.get() + 1));
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionStart()
	 */
	@Override
	public void onQueueExecutionStart() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionStart(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionStart(IExecutionToken<ExecutionTokenState> token) throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(IExecutionToken<ExecutionTokenState> token) throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionPause(IExecutionToken<ExecutionTokenState> token) throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionResume(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionResume(IExecutionToken<ExecutionTokenState> token) throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionComplete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionComplete(IExecutionToken<ExecutionTokenState> token) throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionCanceled()
	 */
	@Override
	public void onQueueExecutionCanceled() throws GkException {
		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeLineExecutionListener#onLineStateChanged(org.goko.core.gcode.execution.IExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onLineStateChanged(IExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {
		
	}
	
	public void turnSpindleOnCw() throws GkException{
		getCommunicator().turnSpindleOnCw();
	}
	
	public void turnSpindleOnCcw() throws GkException{
		getCommunicator().turnSpindleOnCcw();
	}

	public void turnSpindleOff() throws GkException{
		getCommunicator().turnSpindleOff();
	}

	/**
	 * @return the plannerBufferCapacity
	 */
	public Integer getPlannerBufferCapacity() {
		return plannerBufferCapacity;
	}

	/**
	 * @param plannerBufferCapacity the plannerBufferCapacity to set
	 */
	public void setPlannerBufferCapacity(Integer plannerBufferCapacity) {
		this.plannerBufferCapacity = plannerBufferCapacity;
	}
}
