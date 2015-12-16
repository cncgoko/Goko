package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.io.LoadContext;
import org.goko.core.workspace.io.XmlProjectContainer;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
public interface IProjectLoadParticipant<T> {

	void load(LoadContext context, XmlProjectContainer container) throws GkException;

	boolean canLoad(XmlProjectContainer container) throws GkException;

}
