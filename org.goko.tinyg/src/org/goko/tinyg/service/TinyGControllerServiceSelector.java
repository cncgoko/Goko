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
package org.goko.tinyg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.vecmath.Point3d;

import org.goko.core.common.event.Event;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.provider.GCodeExecutionToken;
import org.goko.core.log.GkLog;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;

/**
 * TinyG Controller service selector
 * Allows to change service implementation according to the version of the firmware being used
 *
 * @author PsyKo
 *
 */
public class TinyGControllerServiceSelector implements ITinyGControllerServiceSelector, ITinyGControllerFirmwareService, IControllerService{
	private static final GkLog LOG = GkLog.getLogger(TinyGControllerServiceSelector.class);
	private String firmwareVersion = "380.05";
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
	 * @see org.goko.tinyg.service.ITinyGControllerServiceSelector#registerFirmwareService(org.goko.tinyg.service.ITinyGControllerFirmwareService)
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
	public Point3d getPosition() throws GkException {
		return getCurrentService().getPosition();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IControllerService#executeGCode(org.goko.core.gcode.bean.IGCodeProvider)
	 */
	@Override
	public GCodeExecutionToken executeGCode(IGCodeProvider gcodeProvider) throws GkException {
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
	 * @see org.goko.tinyg.service.ITinyGControllerServiceSelector#setFirmware(java.lang.String)
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
	 * @see org.goko.tinyg.service.ITinyGControllerServiceSelector#getFirmware()
	 */
	@Override
	public String getFirmware() throws GkException {
		return firmwareVersion;
	}

	/** (inheritDoc)
	 * @see org.goko.tinyg.service.ITinyGControllerFirmwareService#getMinimalSupportedFirmwareVersion()
	 */
	@Override
	public String getMinimalSupportedFirmwareVersion() throws GkException {
		return getCurrentService().getMinimalSupportedFirmwareVersion();
	}

	/** (inheritDoc)
	 * @see org.goko.tinyg.service.ITinyGControllerFirmwareService#getMaximalSupportedFirmwareVersion()
	 */
	@Override
	public String getMaximalSupportedFirmwareVersion() throws GkException {
		return getCurrentService().getMaximalSupportedFirmwareVersion();
	}

	/** (inheritDoc)
	 * @see org.goko.tinyg.service.ITinyGControllerFirmwareService#getConfiguration()
	 */
	@Override
	public TinyGConfiguration getConfiguration() throws GkException {
		return getCurrentService().getConfiguration();
	}

	/** (inheritDoc)
	 * @see org.goko.tinyg.service.ITinyGControllerFirmwareService#setConfiguration(org.goko.tinyg.controller.configuration.TinyGConfiguration)
	 */
	@Override
	public void setConfiguration(TinyGConfiguration configuration) throws GkException {
		getCurrentService().setConfiguration(configuration);
	}

	/** (inheritDoc)
	 * @see org.goko.tinyg.service.ITinyGControllerFirmwareService#refreshConfiguration()
	 */
	@Override
	public void refreshConfiguration() throws GkException {
		getCurrentService().refreshConfiguration();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IProbingService#probe(org.goko.core.controller.bean.EnumControllerAxis, double, double)
	 */
	@Override
	public Future<Tuple6b> probe(EnumControllerAxis axis, double feedrate, double maximumPosition) throws GkException {
		return getCurrentService().probe(axis, feedrate, maximumPosition);
	}

	@Override
	public void moveToAbsolutePosition(Tuple6b position) throws GkException {
		getCurrentService().moveToAbsolutePosition(position);
	}


}
