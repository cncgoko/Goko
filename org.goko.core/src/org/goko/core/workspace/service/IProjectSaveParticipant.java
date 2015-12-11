/**
 *
 */
package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.simpleframework.xml.transform.Transform;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
public interface IProjectSaveParticipant<T extends XmlProjectContainer> {

	boolean isDirty();

	Transform<T> getTransform();

    T save() throws GkException;

	Class<T> getProjectContainerClass();
}
