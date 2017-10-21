/**
 *
 */
package org.goko.core.workspace.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.workspace.bean.IProjectMenuProvider;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.core.workspace.bean.ProjectContainerUiProviderComparator;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * @author PsyKo
 * @date 30 oct. 2015
 */
public class WorkspaceUIService extends AbstractGokoService implements IGokoService, IWorkspaceUIService, IWorkspaceListener {
	/** Service ID */
	private static final String SERVICE_ID ="org.goko.core.workspace.WorkspaceUIService";
	/** The map of registered UI providers */
	private Map<String, ProjectContainerUiProvider> mapProjectContainerUiProvider;
	/** The map of registered UI providers */
	private Map<String, List<IProjectMenuProvider>> mapProjectMenuProvider;
	/** Event admin for notification to the UI */
	private EventAdmin eventAdmin;
	/** The workspace service */
	private IWorkspaceService workspaceService;

	/**
	 * Constructor
	 */
	public WorkspaceUIService() {

	}
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
	public void startService() throws GkException {		
		this.mapProjectContainerUiProvider = new HashMap<String, ProjectContainerUiProvider>();		
		this.mapProjectMenuProvider = new HashMap<String, List<IProjectMenuProvider>>();		
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {

	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#findProjectContainerUiProvider(java.lang.String)
	 */
	@Override
	public ProjectContainerUiProvider findProjectContainerUiProvider(String type)  throws GkTechnicalException{
		return mapProjectContainerUiProvider.get(type);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#getProjectContainerUiProvider(java.lang.String)
	 */
	@Override
	public ProjectContainerUiProvider getProjectContainerUiProvider(String type) throws GkTechnicalException{
		ProjectContainerUiProvider result = findProjectContainerUiProvider(type);
		if(result == null){
			throw new GkTechnicalException("ProjectContainerUiProvider not found for type ["+type+"]");
		}
		return result;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#getProjectMenuProvider(java.lang.String)
	 */
	@Override
	public List<IProjectMenuProvider> getProjectMenuProvider(String type) throws GkTechnicalException {
		List<IProjectMenuProvider> result = new ArrayList<>();
		if(mapProjectMenuProvider.containsKey(type)){
			result.addAll(mapProjectMenuProvider.get(type));
		}
		return result;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#addProjectMenuProvider(java.lang.String, org.goko.core.workspace.bean.IProjectMenuProvider)
	 */
	@Override
	public void addProjectMenuProvider(String type, IProjectMenuProvider menuProvider) throws GkTechnicalException {
		if(!mapProjectMenuProvider.containsKey(type)){
			mapProjectMenuProvider.put(type, new ArrayList<IProjectMenuProvider>());
		}
		mapProjectMenuProvider.get(type).add(menuProvider);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#existProjectContainerUiProvider(java.lang.String)
	 */
	@Override
	public boolean existProjectContainerUiProvider(String type)  throws GkTechnicalException{
		return mapProjectContainerUiProvider.containsKey(type);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#addProjectContainerUiProvider(org.goko.core.workspace.bean.ProjectContainerUiProvider)
	 */
	@Override
	public void addProjectContainerUiProvider(ProjectContainerUiProvider provider)  throws GkTechnicalException{
		mapProjectContainerUiProvider.put(provider.getType(), provider);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#getProjectContainerUiProvider()
	 */
	@Override
	public List<ProjectContainerUiProvider> getProjectContainerUiProvider() throws GkTechnicalException {
		List<ProjectContainerUiProvider> content = new ArrayList<ProjectContainerUiProvider>(mapProjectContainerUiProvider.values());
		Collections.sort(content, new ProjectContainerUiProviderComparator());
		return content;
	}

	public void refreshWorkspaceUi(){
		eventAdmin.postEvent(new Event(WorkspaceUIEvent.TOPIC_WORKSPACE_REFRESH, new HashMap<String, String>()));
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
	 * @param eventAdmin the eventAdmin to set
	 */
	public void unsetEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = null;
	}
	/**
	 * @return the workspaceService
	 */
	public IWorkspaceService getWorkspaceService() {
		return workspaceService;
	}
	/**
	 * @param workspaceService the workspaceService to set
	 * @throws GkException GkException
	 */
	public void setWorkspaceService(IWorkspaceService workspaceService) throws GkException {
		this.workspaceService = workspaceService;
		if(this.workspaceService != null){
			this.workspaceService.addWorkspaceListener(this);
		}
	}

	/**
	 * @param workspaceService the workspaceService to remove
	 * @throws GkException GkException
	 */
	public void unsetWorkspaceService(IWorkspaceService workspaceService) throws GkException {
		if(this.workspaceService != null){
			this.workspaceService.removeWorkspaceListener(this);
		}
		this.workspaceService = null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceListener#onWorkspaceEvent(org.goko.core.workspace.service.IWorkspaceEvent)
	 */
	@Override
	public void onWorkspaceEvent(IWorkspaceEvent event) throws GkException {
		refreshWorkspaceUi();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#select(java.lang.Object)
	 */
	@Override
	public void select(Object obj) throws GkException {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(IEventBroker.DATA, obj);
		eventAdmin.postEvent(new Event(WorkspaceUIEvent.TOPIC_WORKSPACE_SELECT, data));
	}

}
