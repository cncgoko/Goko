/**
 * 
 */
package org.goko.core.workspace.service;

import org.eclipse.core.runtime.IProgressMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.io.LoadContext;
import org.goko.core.workspace.io.XmlProjectContainer;

/**
 * @author PsyKo
 * @date 21 févr. 2016
 */
public abstract class AbstractProjectLoadParticipant<T extends XmlProjectContainer> implements IProjectLoadParticipant {
	/** The class of the container */
	private Class<T> containerClass;

	/**
	 * @param containerClass
	 */
	public AbstractProjectLoadParticipant(Class<T> containerClass) {
		super();
		this.containerClass = containerClass;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#load(org.goko.core.workspace.io.LoadContext, org.goko.core.workspace.io.XmlProjectContainer, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void load(LoadContext context, XmlProjectContainer container, IProgressMonitor monitor) throws GkException {
		loadContainer(context, (T)container, monitor);
	}
	
	abstract protected void loadContainer(LoadContext context, T container, IProgressMonitor monitor) throws GkException ;
	
	public Class<T> getContainerClass() {
		return containerClass;
	}

}
