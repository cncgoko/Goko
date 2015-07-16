/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.gcode.filesender.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.widgets.Display;
import org.goko.common.bindings.AbstractController;
import org.goko.common.events.GCodeCommandSelectionEvent;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.event.GokoEventBus;
import org.goko.core.common.event.GokoTopic;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.execution.IGCodeExecutionTimeService;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.execution.IGCodeExecutionToken;
import org.goko.core.gcode.bean.provider.GCodeCommandExecutionEvent;
import org.goko.core.gcode.bean.provider.GCodeExecutionToken;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.gcode.service.IGCodeExecutionMonitorService;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.IWorkspaceService;
import org.goko.gcode.filesender.editor.GCodeEditor;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.google.common.eventbus.Subscribe;

/**
 * GCode file sender controller
 *
 * @author PsyKo
 *
 */
public class GCodeFileSenderController extends AbstractController<GCodeFileSenderBindings> implements IGCodeExecutionListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GCodeFileSenderController.class);
	private static final String[] UNITS = {"bytes", "kB","mB","gB"};

	@Inject
	private IGCodeService gCodeService;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private IControllerService controllerService;
	@Inject
	private IGCodeExecutionTimeService timeService;
	@Inject
	private IWorkspaceService workspaceService;
	@Inject
	private IGCodeExecutionMonitorService monitorService;

	private Runnable elapsedTimeRunnable;

	/**
	 * Constructor
	 * @param bindings the data model
	 */
	public GCodeFileSenderController(GCodeFileSenderBindings bindings) {
		super(bindings);
		getDataModel().setStreamingInProgress(false);
		GokoEventBus.getInstance().register(this);
		createElapsedTimeRunnable();
		Display.getCurrent().timerExec(400, elapsedTimeRunnable);		
	}

	@Inject
	public void test(){
		System.err.println("");
	}
	/**
	 * Create the runnable used to display elapsed time
	 */
	private void createElapsedTimeRunnable(){
		elapsedTimeRunnable = new Runnable() {
			@Override
			public void run() {
				if(getDataModel().getStartDate() != null){
					updateDisplayedTime();
				}else{
					resetElapsedTime();
				}
				Display.getCurrent().timerExec(400, elapsedTimeRunnable);
			}
		};
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		getControllerService().addListener(this);
		monitorService.addExecutionListener(this);		
		EventHandler eventHandler = new EventHandler() {			
			@Override
			public void handleEvent(Event event) {
				try {
					String filepath = (String) event.getProperty(GokoTopic.File.PROPERTY_FILEPATH);
					setGCodeFilepath(filepath);					
				} catch (GkException e) {					
					LOG.error(e);
				}
			}
		};
		eventBroker.subscribe(GokoTopic.File.Open.TOPIC, eventHandler);
	}
	/**
	 * Action called when selecting file to send
	 * @param filepath filepath
	 * @throws GkException GkException
	 */
	public void setGCodeFilepath(String filepath) throws GkException {
		try{
			File file = new File(filepath);
			getDataModel().setFileName(file.getName());
			getDataModel().setFilePath(filepath);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			getDataModel().setFileLastUpdate( sdf.format( file.lastModified() ) );
			parseFile();
			setFileSize(file.length());

		}catch(GkException e){
			getDataModel().setFileName( StringUtils.EMPTY );
			getDataModel().setFilePath( StringUtils.EMPTY);
			getDataModel().setFileLastUpdate( StringUtils.EMPTY );
			getDataModel().setFileSize( StringUtils.EMPTY );
			getDataModel().setgCodeDocument(null);
			throw e;
		}
		updateStreamingAllowed();
	}

	/**
	 * Determine if streaming is allowed
	 */
	protected void updateStreamingAllowed(){
		boolean streamingAllowed = false;
		if (!getDataModel().isStreamingInProgress()) {
			if(StringUtils.isNotBlank(getDataModel().getFilePath())){
				try {

					if( controllerService.isReadyForFileStreaming() ){
						streamingAllowed = true;
					}
				} catch (GkException e) {
					log(e);
					getDataModel().setStreamingAllowed(false);
				}
			}
		}
		getDataModel().setStreamingAllowed(streamingAllowed);
	}

	/**
	 * Computes the file size with the correct unit
	 * @param size the actual size
	 */
	protected void setFileSize(long size){
		String fileSize = humanReadableByteCount(size, true);
		getDataModel().setFileSize(fileSize);
	}

	public String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) {
			return bytes + " B";
		}
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	protected void parseFile() throws GkException{
		if(getDataModel().getGcodeProvider() != null){
			workspaceService.deleteGCodeProvider(getDataModel().getGcodeProvider().getId());
		}
		GCodeContext currentContext = controllerService.getCurrentGCodeContext();
		IGCodeProvider gcodeFile = gCodeService.parseFile(getDataModel().getFilePath(), currentContext);
		getDataModel().setGcodeProvider(gcodeFile);
		long seconds = (long) timeService.evaluateExecutionTime(gcodeFile);

		getDataModel().setTotalCommandCount(CollectionUtils.size(gcodeFile.getGCodeCommands()));
		getDataModel().setRemainingTime(getDurationAsString(seconds*1000));		
		workspaceService.addGCodeProvider(gcodeFile);

	}

	public void addGCodeViewerBinding(GCodeEditor gCodeTextDisplay) {

		IObservableValue widgetObserver = PojoProperties.value("input").observe(gCodeTextDisplay);
		IObservableValue modelObserver  = BeanProperties.value("gCodeDocument").observe(getDataModel());
		UpdateValueStrategy strategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);

		getBindings().add( getBindingContext().bindValue(widgetObserver, modelObserver,null,strategy) );

		initCommandSelectionBindings(gCodeTextDisplay);
	}

	private void initCommandSelectionBindings(GCodeEditor gCodeTextDisplay) {
		IObservableValue widgetObserver = PojoProperties.value("selectedCommand").observe(gCodeTextDisplay);
		IObservableValue modelObserver  = BeanProperties.value("selectedCommand").observe(getDataModel());

		UpdateValueStrategy strategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);

		getBindings().add( getBindingContext().bindValue(widgetObserver, modelObserver,null,strategy) );
	}

	public void startFileStreaming(){
		try{
			GCodeExecutionToken token = controllerService.executeGCode(getDataModel().getGcodeProvider());
			if(getDataModel().getGcodeProvider() != null){
				workspaceService.deleteGCodeProvider(getDataModel().getGcodeProvider().getId());
			}

			getDataModel().setSentCommandCount( 0 );
			getDataModel().setTotalCommandCount( token.getCommandCount() );

			token.addListener(this);
			getDataModel().setGcodeProvider(token);
			getDataModel().setStreamingInProgress(true);
			startElapsedTimer();
		}catch(GkException e){
			log(e);
		}

	}

	public void stopFileStreaming(){
		try {
			controllerService.cancelFileSending();
			getDataModel().setStreamingInProgress(false);
			stopElapsedTimer();
			resetElapsedTime();
		} catch (GkException e) {
			log(e);
		}
	}

	private void resetElapsedTime(){
		getDataModel().setElapsedTime("--");
	}

	public void updateDisplayedTime(){
		long elapsedTime = 0;
		if(getDataModel().getEndDate() != null){
			elapsedTime = getDataModel().getEndDate().getTime() - getDataModel().getStartDate().getTime();
		}else{
			elapsedTime = new Date().getTime() - getDataModel().getStartDate().getTime();
		}

		getDataModel().setElapsedTime(getDurationAsString(elapsedTime));
	}

	protected String getDurationAsString(long milliseconds){
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) ((milliseconds / (1000*60)) % 60);
		int hours   = (int) ((milliseconds / (1000*60*60)) % 24);

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	@EventListener(MachineValueUpdateEvent.class)
	public void onControllerStatusUpdate(MachineValueUpdateEvent updateEvt) throws GkException{
		updateStreamingAllowed();
		MachineState newState = MachineState.UNDEFINED;
		MachineValue machineValue = updateEvt.getTarget();
		MachineValueDefinition definition = controllerService.getMachineValueDefinition(machineValue.getIdDescriptor());
//		if(StringUtils.equals(definition.getId(), DefaultControllerValues.STATE)){
//			newState = (MachineState) machineValue.getValue();
//
//			if(MachineState.RUNNING.equals( newState)){
//				getDataModel().setStreamingInProgress(true);
//
//			}else if(MachineState.PROGRAM_END.equals( newState)
//					|| MachineState.PROGRAM_STOP.equals( newState)
//					|| MachineState.READY.equals( newState)){
//				
//			}
//		}
	}

	public void onExecutionComplete(){
		getDataModel().setStreamingInProgress(false);
		updateStreamingAllowed();
		stopElapsedTimer();
	}
	/**
	 * Listener for stream status update
	 * @param event the GCodeCommandExecutionEvent
	 */
	@EventListener(GCodeCommandExecutionEvent.class)
	public void onStreamStatusUpdate(GCodeCommandExecutionEvent event){
		GCodeExecutionToken token = event.getExecutionToken();
		try {
			getDataModel().setSentCommandCount( token.getExecutedCommandCount()+ token.getErrorCommandCount() );
			getDataModel().setTotalCommandCount( token.getCommandCount() );
			if(token.isComplete()){
				onExecutionComplete();
			}
		} catch (GkException e) {
			log(e);
		}
	}

	/**
	 * @return the controllerService
	 */
	public IControllerService getControllerService() {
		return controllerService;
	}

	/**
	 * @param controllerService the controllerService to set
	 */
	public void setControllerService(IControllerService controllerService) {
		this.controllerService = controllerService;
	}

	@Subscribe
	public void onGCodeCommandSelection(GCodeCommandSelectionEvent evt){
		getDataModel().setSelectedCommand(evt.getGCodeCommand().getId());

	}

	private void startElapsedTimer(){
		getDataModel().setStartDate(new Date());
		getDataModel().setEndDate(null);
	}

	private void stopElapsedTimer(){
		getDataModel().setEndDate(new Date());
	}

	@Override
	public void onExecutionStart(IGCodeExecutionToken token) throws GkException {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(IGCodeExecutionToken token) throws GkException {
		onExecutionComplete();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void onExecutionPause(IGCodeExecutionToken token) throws GkException {
		
	}

	@Override
	public void onExecutionComplete(IGCodeExecutionToken token) throws GkException {
		onExecutionComplete();
	}

	@Override
	public void onCommandStateChanged(IGCodeExecutionToken token, Integer idCommand) throws GkException {
		getDataModel().setSentCommandCount( token.getExecutedCommandCount()+ token.getErrorCommandCount() );
		getDataModel().setTotalCommandCount( token.getCommandCount() );		
	}
}
