/**
 *
 */
package org.goko.core.workspace.service;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.io.SaveContext;
import org.goko.core.workspace.io.XmlProjectContainer;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
public interface IProjectSaveParticipant<T> {

	boolean isDirty();

	/**
	 * Returns the list of nodes that need to be saved
	 * @param context the save context
	 * @return the list of nodes that need to be saved
	 * @throws GkException GkException
	 */
    List<XmlProjectContainer> save(SaveContext context) throws GkException;

    /**
     * Tells this participant to rollback its changes because the save of th eproject failed
     */
    void rollback();

    /**
     * Tells this participant that the saving process completed without error
     */
    void saveComplete();
}
