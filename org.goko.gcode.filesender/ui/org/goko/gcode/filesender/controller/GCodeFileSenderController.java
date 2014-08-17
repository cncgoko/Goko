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
package org.goko.gcode.filesender.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.swing.Timer;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.goko.common.bindings.AbstractController;
import org.goko.common.events.GCodeCommandSelectionEvent;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.event.GokoEventBus;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.execution.IGCodeExecutionTimeService;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.provider.GCodeCommandExecutionEvent;
import org.goko.core.gcode.bean.provider.GCodeExecutionQueue;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
import org.goko.gcode.filesender.editor.GCodeEditor;

import com.google.common.eventbus.Subscribe;

/**
 * GCode file sender controller
 *
 * @author PsyKo
 *
 */
public class GCodeFileSenderController extends AbstractController<GCodeFileSenderBindings>{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GCodeFileSenderController.class);
	private static final String[] UNITS = {"bytes", "kB","mB","gB"};

	@Inject
	private IGCodeService gCodeService;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private IControllerService controllerService;
	private Timer timer;
	@Inject
	private IGCodeExecutionTimeService timeService;

	/**
	 * Constructor
	 * @param bindings the data model
	 */
	public GCodeFileSenderController(GCodeFileSenderBindings bindings) {
		super(bindings);
		getDataModel().setStreamingInProgress(false);
		GokoEventBus.getInstance().register(this);
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		getControllerService().addListener(this);
	}
	/**
	 * Action called when selecting file to send
	 * @param filepath filepath
	 */
	public void setGCodeFilepath(String filepath) {
		try{
			File file = new File(filepath);
			getDataModel().setFileName(file.getName());
			getDataModel().setFilePath(filepath);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			getDataModel().setFileLastUpdate( sdf.format( file.lastModified() ) );
			parseFile();
			setFileSize(file.length());

		}catch(GkException e){
			LOG.error(e);
			getDataModel().setFileName( StringUtils.EMPTY );
			getDataModel().setFilePath( StringUtils.EMPTY);
			getDataModel().setFileLastUpdate( StringUtils.EMPTY );
			getDataModel().setFileSize( StringUtils.EMPTY );
			notifyException(e);
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
					LOG.error(e);
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

		double sizeTmp = size;
		int i = 0;
		while(sizeTmp >= 1024 && i < 4){
			sizeTmp /= 1024;
			i++;
		}
		DecimalFormat format = new DecimalFormat("0.000");
		String fileSize = format.format( sizeTmp ) + " " + UNITS[i];
		getDataModel().setFileSize(fileSize);
	}

	protected void parseFile() throws GkException{
		IGCodeProvider gcodeFile = gCodeService.parse(getDataModel().getFilePath());
		long seconds = (long) timeService.evaluateExecutionTime(gcodeFile);

		getDataModel().setRemainingTime(getDurationAsString(seconds*1000));
		getDataModel().setgCodeDocument(new GCodeDocumentProvider(gcodeFile));
		eventBroker.post("gcodefile", gcodeFile);

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
			IGCodeProvider 		gcodeFile = gCodeService.parse(getDataModel().getFilePath());
			eventBroker.post("gcodefile", gcodeFile);
			GCodeExecutionQueue queue = controllerService.executeGCode(gcodeFile);


			getDataModel().setSentCommandCount( 0 );
			getDataModel().setTotalCommandCount( queue.getCommandCount() );
			getDataModel().setStartDate(new Date());

			queue.addListener(this);

			updateDisplayedTime();
			if(timer != null){
				timer.stop();
			}else{
				timer = new Timer(200, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						updateDisplayedTime();
					}
				});
			}
			timer.start();
			getDataModel().setStreamingInProgress(true);
		}catch(GkException e){
			e.printStackTrace();
		}
	}

	public void stopFileStreaming(){
		try {
			controllerService.cancelFileSending();
			getDataModel().setStreamingInProgress(false);
		} catch (GkException e) {
			e.printStackTrace();
		}
	}
	public void updateDisplayedTime(){
		long elapsedTime = new Date().getTime() - getDataModel().getStartDate().getTime();
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
		if(StringUtils.equals(definition.getId(), DefaultControllerValues.STATE)){
			newState = (MachineState) machineValue.getValue();

			if(MachineState.RUNNING.equals( newState)){
				getDataModel().setStreamingInProgress(true);

			}else if(MachineState.PROGRAM_END.equals( newState)
					|| MachineState.PROGRAM_STOP.equals( newState)
					|| MachineState.READY.equals( newState)){
				getDataModel().setStreamingInProgress(false);
				updateStreamingAllowed();
				if(timer != null){
					timer.stop();
				}
			}
		}
	}

	@EventListener(GCodeCommandExecutionEvent.class)
	public void onStreamStatusUpdate(GCodeCommandExecutionEvent event){
		GCodeExecutionQueue queue = event.getExecutionQueue();
		getDataModel().setSentCommandCount( queue.getExecutedCommandCount() );
		getDataModel().setTotalCommandCount( queue.getCommandCount() );

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
}
