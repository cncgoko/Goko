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

import javax.vecmath.Point3d;

import org.goko.core.common.event.Event;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.provider.GCodeExecutionQueue;
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

	@Override
	public String getServiceId() throws GkException {
		return getCurrentService().getServiceId();
	}

	@Override
	public void start() throws GkException {
		getCurrentService().start();
	}

	@Override
	public void stop() throws GkException {
		getCurrentService().stop();
	}

	@Override
	public void addListener(Object listener) {
		getCurrentService().addListener(listener);
	}

	@Override
	public void removeListener(Object listener) {
		getCurrentService().removeListener(listener);
	}

	@Override
	public <T extends Event> void notifyListeners(T event) {
		getCurrentService().notifyListeners(event);
	}

	@Override
	public Point3d getPosition() throws GkException {
		return getCurrentService().getPosition();
	}

	@Override
	public GCodeExecutionQueue executeGCode(IGCodeProvider gcodeProvider) throws GkException {
		return getCurrentService().executeGCode(gcodeProvider);
	}

	@Override
	public boolean isReadyForFileStreaming() throws GkException {
		return getCurrentService().isReadyForFileStreaming();
	}

	@Override
	public IGkControllerAction getControllerAction(String actionId) throws GkException {
		return getCurrentService().getControllerAction(actionId);
	}

	@Override
	public boolean isControllerAction(String actionId) throws GkException {
		return getCurrentService().isControllerAction(actionId);
	}

	@Override
	public <T> MachineValue<T> getMachineValue(String name, Class<T> clazz) throws GkException {
		return getCurrentService().getMachineValue(name, clazz);
	}

	@Override
	public Class<?> getMachineValueType(String name) throws GkException {
		return getCurrentService().getMachineValueType(name);
	}

	@Override
	public List<MachineValueDefinition> getMachineValueDefinition() throws GkException {
		return getCurrentService().getMachineValueDefinition();
	}

	@Override
	public MachineValueDefinition getMachineValueDefinition(String id) throws GkException {
		return getCurrentService().getMachineValueDefinition(id);
	}

	@Override
	public MachineValueDefinition findMachineValueDefinition(String id) throws GkException {
		return getCurrentService().findMachineValueDefinition(id);
	}


	@Override
	public void cancelFileSending() throws GkException {
		getCurrentService().cancelFileSending();
	}

	@Override
	public void setFirmware(String firmware) throws GkException {
		this.firmwareVersion = firmware;
		if(currentService != null){
			currentService.stop();
		}
		currentService = getCurrentService();
		currentService.start();
	}

	@Override
	public String getFirmware() throws GkException {
		return firmwareVersion;
	}

	@Override
	public String getMinimalSupportedFirmwareVersion() throws GkException {
		return getCurrentService().getMinimalSupportedFirmwareVersion();
	}

	@Override
	public String getMaximalSupportedFirmwareVersion() throws GkException {
		return getCurrentService().getMaximalSupportedFirmwareVersion();
	}

	@Override
	public TinyGConfiguration getConfiguration() throws GkException {
		return getCurrentService().getConfiguration();
	}

	@Override
	public void setConfiguration(TinyGConfiguration configuration) throws GkException {
		getCurrentService().setConfiguration(configuration);
	}

	@Override
	public void refreshConfiguration() throws GkException {
		getCurrentService().refreshConfiguration();
	}


}
