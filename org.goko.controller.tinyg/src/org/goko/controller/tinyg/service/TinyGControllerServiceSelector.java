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
package org.goko.controller.tinyg.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;

import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.event.Event;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.controller.event.IGCodeContextListener;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

/**
 * TinyG Controller service selector
 * Allows to change service implementation according to the version of the firmware being used
 *
 * @author PsyKo
 *
 */
/**
 * @author PsyKo
 * @date 18 oct. 2015
 */
public class TinyGControllerServiceSelector implements ITinyGControllerServiceSelector, ITinyGControllerFirmwareService{
	private static final GkLog LOG = GkLog.getLogger(TinyGControllerServiceSelector.class);
	private String firmwareVersion = "435.10";
	private Map<VersionRange, ITinyGControllerFirmwareService> mapServiceByFirmware;
	private ITinyGControllerFirmwareService currentService;

	/**
	 * Constructor
	 */
	public TinyGControllerServiceSelector() {
		super();
		mapServiceByFirmware = new HashMap<VersionRange, ITinyGControllerFirmwareService>();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerServiceSelector#registerFirmwareService(org.goko.controller.tinyg.service.ITinyGControllerFirmwareService)
	 */
	@Override
	public void registerFirmwareService(ITinyGControllerFirmwareService service) throws GkException{
		VersionRange range = new VersionRange(service.getMinimalSupportedFirmwareVersion(), service.getMaximalSupportedFirmwareVersion());
		this.mapServiceByFirmware.put(range , service);
		LOG.info("Registering ITinyGControllerFirmwareService +"+service.getClass());
	}

	protected ITinyGControllerFirmwareService getCurrentService(){
		if(currentService == null){
			for (VersionRange versionRange : mapServiceByFirmware.keySet()) {
				if(versionRange.contains(firmwareVersion)){
					currentService = mapServiceByFirmware.get(versionRange);
					break;
				}
			}
		}
		return currentService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return getCurrentService().getServiceId();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		getCurrentService().start();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		getCurrentService().stop();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.event.IEventDispatcher#addListener(java.lang.Object)
	 */
	@Override
	public void addListener(Object listener) {
		getCurrentService().addListener(listener);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.event.IEventDispatcher#removeListener(java.lang.Object)
	 */
	@Override
	public void removeListener(Object listener) {
		getCurrentService().removeListener(listener);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.event.IEventDispatcher#notifyListeners(org.goko.core.common.event.Event)
	 */
	@Override
	public <T extends Event> void notifyListeners(T event) {
		getCurrentService().notifyListeners(event);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getPosition()
	 */
	@Override
	public Tuple6b getPosition() throws GkException {
		return getCurrentService().getPosition();
	}


	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#executeGCode(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public IExecutionToken executeGCode(IGCodeProvider gcodeProvider) throws GkException {
		return getCurrentService().executeGCode(gcodeProvider);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#isReadyForFileStreaming()
	 */
	@Override
	public boolean isReadyForFileStreaming() throws GkException {
		return getCurrentService().isReadyForFileStreaming();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getControllerAction(java.lang.String)
	 */
	@Override
	public IGkControllerAction getControllerAction(String actionId) throws GkException {
		return getCurrentService().getControllerAction(actionId);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#isControllerAction(java.lang.String)
	 */
	@Override
	public boolean isControllerAction(String actionId) throws GkException {
		return getCurrentService().isControllerAction(actionId);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValue(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> MachineValue<T> getMachineValue(String name, Class<T> clazz) throws GkException {
		return getCurrentService().getMachineValue(name, clazz);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueType(java.lang.String)
	 */
	@Override
	public Class<?> getMachineValueType(String name) throws GkException {
		return getCurrentService().getMachineValueType(name);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition()
	 */
	@Override
	public List<MachineValueDefinition> getMachineValueDefinition() throws GkException {
		return getCurrentService().getMachineValueDefinition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition getMachineValueDefinition(String id) throws GkException {
		return getCurrentService().getMachineValueDefinition(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#findMachineValueDefinition(java.lang.String)
	 */
	@Override
	public MachineValueDefinition findMachineValueDefinition(String id) throws GkException {
		return getCurrentService().findMachineValueDefinition(id);
	}


	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#cancelFileSending()
	 */
	@Override
	public void cancelFileSending() throws GkException {
		getCurrentService().cancelFileSending();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerServiceSelector#setFirmware(java.lang.String)
	 */
	@Override
	public void setFirmware(String firmware) throws GkException {
		this.firmwareVersion = firmware;
		if(currentService != null){
			currentService.stop();
		}
		currentService = getCurrentService();
		currentService.start();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerServiceSelector#getFirmware()
	 */
	@Override
	public String getFirmware() throws GkException {
		return firmwareVersion;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerFirmwareService#getMinimalSupportedFirmwareVersion()
	 */
	@Override
	public String getMinimalSupportedFirmwareVersion() throws GkException {
		return getCurrentService().getMinimalSupportedFirmwareVersion();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerFirmwareService#getMaximalSupportedFirmwareVersion()
	 */
	@Override
	public String getMaximalSupportedFirmwareVersion() throws GkException {
		return getCurrentService().getMaximalSupportedFirmwareVersion();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerFirmwareService#getConfiguration()
	 */
	@Override
	public TinyGConfiguration getConfiguration() throws GkException {
		return getCurrentService().getConfiguration();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerFirmwareService#setConfiguration(org.goko.controller.tinyg.controller.configuration.TinyGConfiguration)
	 */
	@Override
	public void setConfiguration(TinyGConfiguration configuration) throws GkException {
		getCurrentService().setConfiguration(configuration);
	}
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerFirmwareService#updateConfiguration(org.goko.controller.tinyg.controller.configuration.TinyGConfiguration)
	 */
	@Override
	public void updateConfiguration(TinyGConfiguration configuration) throws GkException {
		getCurrentService().updateConfiguration(configuration);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.service.ITinyGControllerFirmwareService#refreshConfiguration()
	 */
	@Override
	public void refreshConfiguration() throws GkException {
		getCurrentService().refreshConfiguration();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#probe(org.goko.core.controller.bean.EnumControllerAxis, double, double)
	 */
	@Override
	public CompletionService<ProbeResult> probe(ProbeRequest request) throws GkException {
		return getCurrentService().probe(request);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#probe(java.util.List)
	 */
	@Override
	public CompletionService<ProbeResult> probe(List<ProbeRequest> probeRequest) throws GkException {		
		return getCurrentService().probe(probeRequest);
	}
	
	@Override
	public void moveToAbsolutePosition(Tuple6b position) throws GkException {
		getCurrentService().moveToAbsolutePosition(position);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#getGCodeContext()
	 */
	@Override
	public GCodeContext getGCodeContext() throws GkException {
		return getCurrentService().getGCodeContext();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.event.IObservable#addObserver(java.lang.Object)
	 */
	@Override
	public void addObserver(IGCodeContextListener<GCodeContext> observer) {
		getCurrentService().addObserver(observer);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.event.IObservable#removeObserver(java.lang.Object)
	 */
	@Override
	public boolean removeObserver(IGCodeContextListener<GCodeContext> observer) {		
		return getCurrentService().removeObserver(observer);
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getX()
	 */
	@Override
	public Length getX() throws GkException {
		return getCurrentService().getX();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getY()
	 */
	@Override
	public Length getY() throws GkException {
		return getCurrentService().getY();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getZ()
	 */
	@Override
	public Length getZ() throws GkException {
		return getCurrentService().getZ();
	}

	@Override
	public Angle getA() throws GkException {
		return getCurrentService().getA();
	}

	@Override
	public Tuple6b getCoordinateSystemOffset(ICoordinateSystem cs) throws GkException {
		return getCurrentService().getCoordinateSystemOffset(cs);
	}

	@Override
	public ICoordinateSystem getCurrentCoordinateSystem() throws GkException {
		return getCurrentService().getCurrentCoordinateSystem();
	}

	@Override
	public List<ICoordinateSystem> getCoordinateSystem() throws GkException {
		return getCurrentService().getCoordinateSystem();
	}

	@Override
	public void stopJog() throws GkException {
		getCurrentService().stopJog();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.ICoordinateSystemAdapter#setCurrentCoordinateSystem(org.goko.core.gcode.element.ICoordinateSystem)
	 */
	@Override
	public void setCurrentCoordinateSystem(ICoordinateSystem cs) throws GkException {
		getCurrentService().setCurrentCoordinateSystem(cs);
	}

	@Override
	public void resetCurrentCoordinateSystem() throws GkException {
		getCurrentService().resetCurrentCoordinateSystem();
	}

	@Override
	public void setPlannerBufferSpaceCheck(boolean plannerBufferSpaceCheck) throws GkException {
		getCurrentService().setPlannerBufferSpaceCheck(plannerBufferSpaceCheck);
	}

	@Override
	public MachineState getState() throws GkException {
		return getCurrentService().getState();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#isPlannerBufferSpaceCheck()
	 */
	@Override
	public boolean isPlannerBufferSpaceCheck() {
		return getCurrentService().isPlannerBufferSpaceCheck();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#getWorkVolumeMinimalPosition()
	 */
	@Override
	public Tuple6b getWorkVolumeMinimalPosition() throws GkException {
		return getCurrentService().getWorkVolumeMinimalPosition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#findWorkVolumeMinimalPosition()
	 */
	@Override
	public Tuple6b findWorkVolumeMinimalPosition() throws GkException {
		return getCurrentService().findWorkVolumeMinimalPosition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#getWorkVolumeMaximalPosition()
	 */
	@Override
	public Tuple6b getWorkVolumeMaximalPosition() throws GkException {
		return getCurrentService().getWorkVolumeMaximalPosition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IWorkVolumeProvider#findWorkVolumeMaximalPosition()
	 */
	@Override
	public Tuple6b findWorkVolumeMaximalPosition() throws GkException {
		return getCurrentService().findWorkVolumeMaximalPosition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#getFileExtension()
	 */
	@Override
	public String getFileExtension() {		
		return getCurrentService().getFileExtension();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#canExport()
	 */
	@Override
	public boolean canExport() throws GkException {	
		return getCurrentService().canExport();		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileExporter#exportTo(java.io.OutputStream)
	 */
	@Override
	public void exportTo(OutputStream stream) throws GkException {
		getCurrentService().exportTo(stream);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#canImport()
	 */
	@Override
	public boolean canImport() throws GkException {
		return getCurrentService().canImport();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerConfigurationFileImporter#importFrom(java.io.InputStream)
	 */
	@Override
	public void importFrom(InputStream inputStream) throws GkException {
		getCurrentService().importFrom(inputStream);		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#killAlarm()
	 */
	@Override
	public void killAlarm() throws GkException {
		getCurrentService().killAlarm();		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#getAvailableBuffer()
	 */
	@Override
	public int getAvailableBuffer() throws GkException {
		return getCurrentService().getAvailableBuffer();	
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#setAvailableBuffer(int)
	 */
	@Override
	public void setAvailableBuffer(int availableBuffer) throws GkException {
		getCurrentService().setAvailableBuffer(availableBuffer);	
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IStepJogService#getJogStep()
	 */
	@Override
	public Length getJogStep() throws GkException {		
		return getCurrentService().getJogStep();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IStepJogService#setJogStep(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setJogStep(Length step) throws GkException {
		getCurrentService().setJogStep(step);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#setJogFeedrate(java.math.BigDecimal)
	 */
	@Override
	public void setJogFeedrate(Speed feed) throws GkException {
		getCurrentService().setJogFeedrate(feed);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#getJogFeedrate()
	 */
	@Override
	public Speed getJogFeedrate() throws GkException {
		return getCurrentService().getJogFeedrate();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#setJogPrecise(boolean)
	 */
	@Override
	public void setJogPrecise(boolean precise) throws GkException {
		getCurrentService().setJogPrecise(precise);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#isJogPrecise()
	 */
	@Override
	public boolean isJogPrecise() throws GkException {
		return getCurrentService().isJogPrecise();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#startJog(org.goko.core.controller.bean.EnumControllerAxis)
	 */
	@Override
	public void startJog(EnumControllerAxis axis) throws GkException {
		getCurrentService().startJog(axis);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IJogService#isJogPreciseForced()
	 */
	@Override
	public boolean isJogPreciseForced() throws GkException {		
		return getCurrentService().isJogPreciseForced();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#send(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	public void send(GCodeLine gCodeLine) throws GkException {
		getCurrentService().send(gCodeLine);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#stopMotion()
	 */
	@Override
	public void stopMotion() throws GkException {
		getCurrentService().stopMotion();
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#startMotion()
	 */
	@Override
	public void startMotion() throws GkException {
		getCurrentService().startMotion();		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#verifyReadyForExecution()
	 */
	@Override
	public void verifyReadyForExecution() throws GkException {
		getCurrentService().verifyReadyForExecution();		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionStart()
	 */
	@Override
	public void onQueueExecutionStart() throws GkException {
		getCurrentService().onQueueExecutionStart();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionStart(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionStart(IExecutionToken<ExecutionTokenState> token) throws GkException {
		getCurrentService().onExecutionStart(token);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(IExecutionToken<ExecutionTokenState> token) throws GkException {
		getCurrentService().onExecutionCanceled(token);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionPause(IExecutionToken<ExecutionTokenState> token) throws GkException {
		getCurrentService().onExecutionPause(token);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionResume(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionResume(IExecutionToken<ExecutionTokenState> token) throws GkException {
		getCurrentService().onExecutionResume(token);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionComplete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionComplete(IExecutionToken<ExecutionTokenState> token) throws GkException {
		getCurrentService().onExecutionComplete(token);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException {
		getCurrentService().onQueueExecutionComplete();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionCanceled()
	 */
	@Override
	public void onQueueExecutionCanceled() throws GkException {
		getCurrentService().onQueueExecutionCanceled();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeLineExecutionListener#onLineStateChanged(org.goko.core.gcode.execution.IExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onLineStateChanged(IExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {
		getCurrentService().onLineStateChanged(token, idLine);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#pauseMotion()
	 */
	@Override
	public void pauseMotion() throws GkException {
		getCurrentService().pauseMotion();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinygControllerService#resumeMotion()
	 */
	@Override
	public void resumeMotion() throws GkException {
		getCurrentService().resumeMotion();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#checkReadyToProbe()
	 */
	@Override
	public void checkReadyToProbe() throws GkException {
		getCurrentService().checkReadyToProbe();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#isReadyToProbe()
	 */
	@Override
	public boolean isReadyToProbe() {
		return getCurrentService().isReadyToProbe();
	}
}
