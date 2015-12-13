/**
 *
 */
package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;
import org.simpleframework.xml.stream.InputNode;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
public interface IProjectLoadParticipant<T> {

	void load(InputNode input) throws GkException;

	String getProjectContainerType();
	
	T newContentInstance();
}
