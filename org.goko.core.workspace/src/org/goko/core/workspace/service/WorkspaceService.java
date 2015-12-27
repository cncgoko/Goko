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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.io.LoadContext;
import org.goko.core.workspace.io.SaveContext;
import org.goko.core.workspace.io.XmlGkProject;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

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
	/** The list of listener */
	private List<IWorkspaceListener> listenerList;
	/** The list of listener for the project lifecycle  */
	private List<IProjectLifecycleListener> projectLifecycleListenerList;
	/** The known save participants */
	private List<IProjectSaveParticipant<?>> saveParticipants;
	/** The known load participants */
	private List<IProjectLoadParticipant<?>> loadParticipants;
	// Temporary project storage
	private GkProject project;
	/** The xml persistence service */
	private IXmlPersistenceService xmlPersistenceService;

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
		this.listenerList = new ArrayList<IWorkspaceListener>();
		this.project = new GkProject();
		this.saveParticipants = new ArrayList<IProjectSaveParticipant<?>>();
		this.loadParticipants = new ArrayList<IProjectLoadParticipant<?>>();
		this.projectLifecycleListenerList = new ArrayList<IProjectLifecycleListener>();
		LOG.info("Successfully started : "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

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
		this.saveParticipants.add(participant);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#findProjectLoadParticipantByType(java.lang.String)
	 */
	@Override
	public IProjectLoadParticipant<?> findProjectLoadParticipantByType(String type) throws GkException {
//		for (IProjectLoadParticipant<?> participant : loadParticipants) {
//			if(StringUtils.equals(participant.getProjectContainerType(), type)){
//				return participant;
//			}
//		}
		return null;
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#addProjectLoadParticipant(org.goko.core.workspace.service.IProjectLoadParticipant)
	 */
	@Override
	public void addProjectLoadParticipant(IProjectLoadParticipant<?> participant) throws GkException {
		this.loadParticipants.add(participant);
	}


	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#saveProject()
	 */
	@Override
	public void saveProject(File targetProjectFile) throws GkException {
		SaveContext context = new SaveContext();
		File projectFile = targetProjectFile;
		// Build complete path
		String path = projectFile.getAbsolutePath();
		String fullPath = FilenameUtils.getFullPath(path);
		String name     = FilenameUtils.getBaseName(path);
		String extension = FilenameUtils.getExtension(path);

		if(!StringUtils.equals(extension, "goko")){
			extension = "goko";
		}
		project.setName(name);
		project.setFilepath(fullPath+name+"."+extension);
		// Notify listeners
		notifyProjectBeforeSave();

		projectFile = new File(project.getFilepath());
		context.setProjectName(name);
		context.setProjectFile(projectFile);

		context.setResourcesFolderName(name+"Resources");
		context.setResourcesFolder(new File(context.getProjectFile().getParentFile(), context.getResourcesFolderName()));
		context.getResourcesFolder().mkdir();

		XmlGkProject xmlProject = new XmlGkProject();
		xmlProject.setResourcesFolderName(context.getResourcesFolderName());

		for (IProjectSaveParticipant<?> saveParticipant : saveParticipants) {
			xmlProject.getLstProjectContainer().addAll(saveParticipant.save(context));
		}

		try {
			xmlPersistenceService.write(xmlProject, context.getProjectFile());
			project.setFilepath(projectFile.getAbsolutePath());
		} catch (GkException e) {
			rollbackSaveProject();
			throw e;
		}
		completeSaveProject();
		// Notify listeners
		notifyProjectAfterSave();
		// Mark project as not dirty
		setProjectDirty(false);
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
		for (IProjectSaveParticipant<?> saveParticipant : saveParticipants) {
			saveParticipant.saveComplete();
		}
	}

	/**
	 * Notifies all the save participant that the saved failed
	 */
	private void rollbackSaveProject(){
		for (IProjectSaveParticipant<?> saveParticipant : saveParticipants) {
			saveParticipant.rollback();
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#loadProject(java.io.File)
	 */
	@Override
	public void loadProject(File projectFile) throws GkException {
		try {
			LoadContext context = new LoadContext();
			context.setProjectFile(projectFile);
			context.setProjectName(projectFile.getName());

			// Notify listeners
			notifyProjectBeforeLoad();

			Serializer p = new Persister();
			XmlGkProject xmlGkProject = xmlPersistenceService.read(XmlGkProject.class, projectFile);
			project = new GkProject();
			project.setFilepath(projectFile.getAbsolutePath());
			project.setName(projectFile.getName());

			context.setResourcesFolderName(xmlGkProject.getResourcesFolderName());
			context.setResourcesFolder(new File(projectFile.getParentFile(), xmlGkProject.getResourcesFolderName()));

			List<XmlProjectContainer> xmlContainers = xmlGkProject.getLstProjectContainer();

			if(CollectionUtils.isNotEmpty(xmlContainers)){
				for (XmlProjectContainer xmlProjectContainer : xmlContainers) {
					// Let's find the load participant that can handle this container
					for (IProjectLoadParticipant<?> loadParticipant : loadParticipants) {
						if(loadParticipant.canLoad(xmlProjectContainer)){
							loadParticipant.load(context, xmlProjectContainer);
							break;
						}
						LOG.warn("No IProjectLoadParticipant found for XmlProjectContainer of type ["+xmlProjectContainer.getType()+"]");
					}
				}
			}
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
}
