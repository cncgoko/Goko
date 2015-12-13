/**
 *
 */
package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
public interface IProjectSaveParticipant<T> {

	boolean isDirty();

    T save() throws GkException;

	String getProjectContainerType();
}
