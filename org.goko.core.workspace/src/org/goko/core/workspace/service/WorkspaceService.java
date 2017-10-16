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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.io.IProjectInputLocation;
import org.goko.core.workspace.io.IProjectLocation;
import org.goko.core.workspace.io.IResourceLocation;
import org.goko.core.workspace.io.ProjectLocation;
import org.goko.core.workspace.io.XmlGkProject;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.goko.core.workspace.io.xml.XmlURIResourceLocation;
import org.goko.core.workspace.mapper.URIResourceLocationExporter;
import org.goko.core.workspace.mapper.URIResourceLocationLoader;

/**
 * Default implementation of the workspace service
 *
 * @author PsyKo
 *
 */
public class WorkspaceService extends AbstractGokoService implements IWorkspaceService{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(WorkspaceService.class);
	/** Service ID */
	private static final String SERVICE_ID ="org.goko.core.workspace.WorkspaceService";
	/** The list of listener */
	private List<IWorkspaceListener> listenerList;
	/** The list of listener for the project lifecycle  */
	private List<IProjectLifecycleListener> projectLifecycleListenerList;
	/** The known save participants */
	private List<IProjectSaveParticipant<?>> saveParticipants;
	/** The known load participants */
	private List<IProjectLoadParticipant> loadParticipants;
	// Temporary project storage
	private GkProject project;
	/** The xml persistence service */
	private IXmlPersistenceService xmlPersistenceService;
	/** The mapper service */
	private IMapperService mapperService;

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
		this.listenerList = new ArrayList<IWorkspaceListener>();
		this.project = new GkProject();
		this.project.setLocation( new ProjectLocation() );
		this.projectLifecycleListenerList = new ArrayList<IProjectLifecycleListener>();
		
		this.mapperService.addLoader(new URIResourceLocationLoader(this));
		this.mapperService.addExporter(new URIResourceLocationExporter());
		
		this.xmlPersistenceService.register(XmlURIResourceLocation.class);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {

	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#addResource(java.net.URI)
	 */
	@Override
	public IResourceLocation addResource(URI uri) throws GkException {
		return project.getLocation().addResource(uri.toString(), uri);
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

	@Override
	public void notifyWorkspaceEvent(IWorkspaceEvent event) throws GkException{
		notifyListeners(event);
	}
	/**
	 * Notify the registered listeners with the given event
	 * @param event the event
	 * @throws GkException GkException
	 */
	private void notifyListeners(IWorkspaceEvent event) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IWorkspaceListener workspaceListener : listenerList) {
				workspaceListener.onWorkspaceEvent(event);
			}
		}
	}

	/**
	 * @return the project
	 */
	@Override
	public GkProject getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(GkProject project) {
		this.project = project;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#addProjectSaveParticipant(org.goko.core.workspace.service.IProjectSaveParticipant)
	 */
	@Override
	public void addProjectSaveParticipant(IProjectSaveParticipant<?> participant) throws GkException {
		this.getSaveParticipants().add(participant);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#addProjectLoadParticipant(org.goko.core.workspace.service.IProjectLoadParticipant)
	 */
	@Override
	public void addProjectLoadParticipant(IProjectLoadParticipant participant) throws GkException {
		this.getLoadParticipants().add(participant);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#saveProject()
	 */
	@Override
	public void saveProject(IProjectLocation output, IProgressMonitor monitor) throws GkException {		
		LOG.info("Saving project to "+output.getLocation());
		// Notify listeners
		notifyProjectBeforeSave();
				
		XmlGkProject xmlProject = new XmlGkProject();
		
		project.getLocation().importProjectDependencies();
		
		for (IProjectSaveParticipant<?> saveParticipant : getSaveParticipants()) {
			xmlProject.getProjectContainer().addAll(saveParticipant.save(output));
		}
		
		try {
			ByteArrayOutputStream oStream = new ByteArrayOutputStream();
			xmlPersistenceService.write(xmlProject, oStream);			
			output.setProjectDescriptor(new ByteArrayInputStream(oStream.toByteArray()));
			// Persist the output
			output.write();
		} catch (GkException e) {
			rollbackSaveProject();
			throw e;
		}
		project.setLocation(output);
		project.setName(project.getLocation().getName());
		
		completeSaveProject();
		// Notify listeners
		notifyProjectAfterSave();
		// Mark project as not dirty
		setProjectDirty(false);
		LOG.info("Project save complete.");
	}

	/**
	 * Utility method to change the project dirty state and notify the listeners
	 * @param dirty dirty state
	 * @throws GkException GkException
	 */
	private void setProjectDirty(boolean dirty) throws GkException {
		this.project.setDirty(dirty);
		notifyProjectDirtyStateChange();
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#markProjectDirty()
	 */
	@Override
	public void markProjectDirty() throws GkException {
		setProjectDirty(true);
	}

	/**
	 * Notifies all the save participant that the saved occurred without issue
	 */
	private void completeSaveProject(){
		for (IProjectSaveParticipant<?> saveParticipant : getSaveParticipants()) {
			saveParticipant.saveComplete();
		}
	}

	/**
	 * Notifies all the save participant that the saved failed
	 */
	private void rollbackSaveProject(){
		for (IProjectSaveParticipant<?> saveParticipant : getSaveParticipants()) {
			saveParticipant.rollback();
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#createNewProject()
	 */
	@Override
	public void createNewProject() throws GkException {
		
		if(CollectionUtils.isNotEmpty(getLoadParticipants())){
			// Clear stored data first
			for (IProjectLoadParticipant loadParticipant : getLoadParticipants()) {
				loadParticipant.clearContent();
			}
		}
		
		project = new GkProject();
		project.setLocation( new ProjectLocation() );
				
		setProjectDirty(false);
		// Notify listeners
		notifyProjectAfterLoad();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#loadProject(IProjectInputLocation)
	 */
	@Override
	public void loadProject(IProjectLocation input, IProgressMonitor monitor) throws GkException {
		try {			
			input.read();
			// Notify listeners
			notifyProjectBeforeLoad();
			
			// Read the project description file 
			InputStream projectFileInputStream = input.getProjectDescriptor();
			XmlGkProject xmlGkProject = xmlPersistenceService.read(XmlGkProject.class, projectFileInputStream);
			projectFileInputStream.close();
			
			project = new GkProject();			
			project.setName(input.getName());
			project.setLocation(input);
			
			Map<String, XmlProjectContainer> mapContainerByType = new HashMap<>();	
			if(CollectionUtils.isNotEmpty(xmlGkProject.getProjectContainer())){
				for (XmlProjectContainer projectContainer : xmlGkProject.getProjectContainer()) {
					mapContainerByType.put(projectContainer.getType(), projectContainer);					
				}
			}
			
			// Sort by priority			
			Collections.sort(getLoadParticipants(), new ProjectLoadParticipantComparator());
			
			if(CollectionUtils.isNotEmpty(getLoadParticipants())){
				// Clear stored data first
				for (IProjectLoadParticipant loadParticipant : getLoadParticipants()) {
					loadParticipant.clearContent();
				}
				// Load new data
				for (IProjectLoadParticipant loadParticipant : getLoadParticipants()) {
					if(mapContainerByType.containsKey(loadParticipant.getContainerType())){
						loadParticipant.load(mapContainerByType.get(loadParticipant.getContainerType()), input, monitor);	
					}					
				}
			}
			setProjectDirty(false);
			// Notify listeners
			notifyProjectAfterLoad();
		} catch (Exception e) {
			throw new GkTechnicalException(e);
		}
	}

	protected void notifyProjectBeforeSave() throws GkException{
		if(CollectionUtils.isNotEmpty(projectLifecycleListenerList)){
			for (IProjectLifecycleListener listener : projectLifecycleListenerList) {
				listener.beforeSave();
			}
		}
	}

	protected void notifyProjectAfterSave() throws GkException{
		if(CollectionUtils.isNotEmpty(projectLifecycleListenerList)){
			for (IProjectLifecycleListener listener : projectLifecycleListenerList) {
				listener.afterSave();
			}
		}
	}

	protected void notifyProjectBeforeLoad() throws GkException{
		if(CollectionUtils.isNotEmpty(projectLifecycleListenerList)){
			for (IProjectLifecycleListener listener : projectLifecycleListenerList) {
				listener.beforeLoad();
			}
		}
	}

	protected void notifyProjectDirtyStateChange() throws GkException{
		if(CollectionUtils.isNotEmpty(projectLifecycleListenerList)){
			for (IProjectLifecycleListener listener : projectLifecycleListenerList) {
				listener.onProjectDirtyStateChange();
			}
		}
	}

	protected void notifyProjectAfterLoad() throws GkException{
		if(CollectionUtils.isNotEmpty(projectLifecycleListenerList)){
			for (IProjectLifecycleListener listener : projectLifecycleListenerList) {
				listener.afterLoad();
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#addProjectLifecycleListener(org.goko.core.workspace.service.IProjectLifecycleListener)
	 */
	@Override
	public void addProjectLifecycleListener(IProjectLifecycleListener listener) throws GkException {
		this.projectLifecycleListenerList.add(listener);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#removeProjectLifecycleListener(org.goko.core.workspace.service.IProjectLifecycleListener)
	 */
	@Override
	public void removeProjectLifecycleListener(IProjectLifecycleListener listener) throws GkException {
		this.projectLifecycleListenerList.remove(listener);
	}

	/**
	 * @return the xmlPersistenceService
	 */
	public IXmlPersistenceService getXmlPersistenceService() {
		return xmlPersistenceService;
	}

	/**
	 * @param xmlPersistenceService the xmlPersistenceService to set
	 */
	public void setXmlPersistenceService(IXmlPersistenceService xmlPersistenceService) {
		this.xmlPersistenceService = xmlPersistenceService;
	}
	
	/**
	 * @return the ssaveParticipants
	 */
	public List<IProjectSaveParticipant<?>> getSaveParticipants() {
		if(saveParticipants == null){
			saveParticipants = new ArrayList<IProjectSaveParticipant<?>>();
		}
		return saveParticipants;
	}
	
	/**
	 * @return the loadParticipants
	 */
	private List<IProjectLoadParticipant> getLoadParticipants() {
		if(loadParticipants == null){
			loadParticipants = new ArrayList<IProjectLoadParticipant>();
		}
		return loadParticipants;
	}

	/**
	 * @return the mapperService
	 */
	public IMapperService getMapperService() {
		return mapperService;
	}

	/**
	 * @param mapperService the mapperService to set
	 */
	public void setMapperService(IMapperService mapperService) {
		this.mapperService = mapperService;
	}
}
