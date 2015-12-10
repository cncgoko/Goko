/**
 *
 */
package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.io.XmlProjectContainer;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
public interface IProjectSaveParticipant<T> {

	boolean isDirty();

    XmlProjectContainer<T> save() throws GkException;

	String getProjectContainerType();
}
