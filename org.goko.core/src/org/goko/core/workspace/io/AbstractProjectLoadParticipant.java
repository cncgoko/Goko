/**
 * 
 */
package org.goko.core.workspace.io;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.workspace.service.IProjectLoadParticipant;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.InputNode;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public abstract class AbstractProjectLoadParticipant<T> implements IProjectLoadParticipant<T>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#load(org.simpleframework.xml.stream.InputNode)
	 */
	@Override
	public final void load(InputNode input) throws GkException {
		try {
			Serializer s = new Persister();
			T content = s.read(getProjectContainerContentClass(), input);
			loadContent(content);
		} catch (Exception e) {
			throw new GkTechnicalException(e);
		}
	}
	
	protected abstract Class<T> getProjectContainerContentClass();
	
	protected abstract void loadContent(T content) throws GkException;
}
