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
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.DerivedTreeStrategy;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.element.ProjectContainer;
import org.goko.core.workspace.io.XmlGkProject;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Registry;
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
	/** The known save participants */
	private List<IProjectSaveParticipant<?>> saveParticipants;
	/** The known load participants */
	private List<IProjectLoadParticipant<?>> loadParticipants;
	// Temporary project storage
	private GkProject project;
	Registry registry;

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
		this.project.addProjectContainer(new ProjectContainer("TEST")); // FIXME : remove from workspace service
		this.project.addProjectContainer(new ProjectContainer("EXECUTIONQUEUE")); // FIXME : remove from workspace service
		this.saveParticipants = new ArrayList<IProjectSaveParticipant<?>>();
		this.loadParticipants = new ArrayList<IProjectLoadParticipant<?>>();
		registry = new Registry();
		try {
			registry.bind(XmlProjectContainer.class, new TestConverter(this));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		for (IProjectLoadParticipant<?> participant : loadParticipants) {
			if(StringUtils.equals(participant.getProjectContainerType(), type)){
				return participant;
			}
		}
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
	public void saveProject() throws GkException {
		XmlGkProject xmlProject = new XmlGkProject();

		ArrayList<XmlProjectContainer> lstProjectContainer = new ArrayList<XmlProjectContainer>();

		for (IProjectSaveParticipant<?> saveParticipant : saveParticipants) {
			xmlProject.getTestList().add(saveParticipant.save());
			lstProjectContainer.add(new XmlProjectContainer(saveParticipant.getProjectContainerType(), saveParticipant.save()));
		}

		xmlProject.setLstProjectContainer(lstProjectContainer);
		try {

			//Strategy strategy = new RegistryStrategy(registry);
			Serializer p = new Persister(new DerivedTreeStrategy());
			p.write(xmlProject, new File("C:/testgk.xml"));
			p.write(xmlProject, System.out);

		//	XmlGkProject t = p.read(XmlGkProject.class, new File("C:/Users/PsyKo/Documents/testgk.xml"));

//			JAXBContext context = JAXBContext.newInstance("org.goko");
//		    Marshaller m = context.createMarshaller();
//		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		    // Write to System.out
		//    m.marshal(xmlProject, System.out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#loadProject(java.io.File)
	 */
	@Override
	public void loadProject(File projectFile) throws GkException {
		try {

			//Strategy strategy = new RegistryStrategy(registry);
			Serializer p = new Persister(new DerivedTreeStrategy());
			XmlGkProject tmpProject = p.read(XmlGkProject.class, new File("C:/testgk.xml"));

			System.err.println("");
		//	p.read(XmlGkProject.class, System.out);

		//	XmlGkProject t = p.read(XmlGkProject.class, new File("C:/Users/PsyKo/Documents/testgk.xml"));

//			JAXBContext context = JAXBContext.newInstance("org.goko");
//		    Marshaller m = context.createMarshaller();
//		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		    // Write to System.out
		//    m.marshal(xmlProject, System.out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
