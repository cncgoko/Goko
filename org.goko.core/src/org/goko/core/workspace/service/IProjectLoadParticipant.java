package org.goko.core.workspace.service;

import org.eclipse.core.runtime.IProgressMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.io.IProjectLocation;
import org.goko.core.workspace.io.XmlProjectContainer;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
public interface IProjectLoadParticipant {
	
	int getPriority();
	
	void clearContent() throws GkException;
	
	void load(XmlProjectContainer container, IProjectLocation input, IProgressMonitor monitor) throws GkException;
	
	String getContainerType();

}
