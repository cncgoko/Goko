package org.goko.core.workspace.service;

import org.eclipse.core.runtime.IProgressMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.io.LoadContext;
import org.goko.core.workspace.io.XmlProjectContainer;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
public interface IProjectLoadParticipant<T extends XmlProjectContainer> {

	void load(LoadContext context, T container, IProgressMonitor monitor) throws GkException;

	Class<T> getContainerClass();

}
