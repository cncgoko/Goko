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
package org.goko.core.workspace.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.GCodeProviderEvent.GCodeProviderEventType;

/**
 * Default implementation of the workspace service
 *
 * @author PsyKo
 *
 */
/**
 * @author PsyKo
 *
 */
public class WorkspaceService implements IWorkspaceService{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(WorkspaceService.class);
	/** Service ID */
	private static final String SERVICE_ID ="org.goko.core.workspace.WorkspaceService";
	/** Map of the provider for the corresponding Ids*/
	private Map<Integer, IGCodeProvider> cacheProviderById;
	/** The list of listener */
	private List<IWorkspaceListener> listenerList;
	/** The id generator */
	private Integer sequenceId;
	/** The current provider */
	private Integer currentProviderId;

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		cacheProviderById = new HashMap<Integer, IGCodeProvider>();
		listenerList = new ArrayList<IWorkspaceListener>();
		sequenceId = 0;
		LOG.info("Successfully started : "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#addGCodeProvider(org.goko.core.gcode.bean.IGCodeProvider)
	 */
	@Override
	public void addGCodeProvider(IGCodeProvider provider) throws GkException {
		Integer newProviderId = sequenceId++;
		provider.setId(newProviderId);
		cacheProviderById.put(newProviderId, provider);
		if(currentProviderId == null){
			setCurrentGCodeProvider(newProviderId);
		}
		notifyListeners(new GCodeProviderEvent(GCodeProviderEventType.INSERT, newProviderId));
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#getGCodeProvider(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider getGCodeProvider(Integer id) throws GkException {
		IGCodeProvider provider = cacheProviderById.get(id);
		if(provider == null){
			throw new GkTechnicalException("Provider with internal id "+id+" does not exist.");
		}
		return provider;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#setCurrentGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void setCurrentGCodeProvider(Integer id) throws GkException {
		currentProviderId = id;
		notifyListeners(new GCodeProviderEvent(GCodeProviderEventType.CURRENT_UPDATE, id));
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#getCurrentGCodeProvider()
	 */
	@Override
	public IGCodeProvider getCurrentGCodeProvider() throws GkException {
		return cacheProviderById.get(currentProviderId);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#deleteGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void deleteGCodeProvider(Integer id) throws GkException {
		cacheProviderById.remove(id);
		if(ObjectUtils.equals(currentProviderId, id)){
			setCurrentGCodeProvider(null);
		}
		notifyListeners(new GCodeProviderEvent(GCodeProviderEventType.DELETE, id));
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#addWorkspaceListener(IWorkspaceListener)
	 */
	@Override
	public void addWorkspaceListener(IWorkspaceListener listener) throws GkException {
		this.listenerList.add(listener);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#removeWorkspaceListener(org.goko.core.workspace.service.IWorkspaceListener)
	 */
	@Override
	public void removeWorkspaceListener(IWorkspaceListener listener) throws GkException {
		this.listenerList.remove(listener);
	}

	/**
	 * Notify the registered listeners with the given event
	 * @param event the event
	 * @throws GkException GkException
	 */
	private void notifyListeners(GCodeProviderEvent event) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IWorkspaceListener workspaceListener : listenerList) {
				workspaceListener.onGCodeProviderEvent(event);
			}
		}
	}

}
