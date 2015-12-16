package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;

public interface IProjectLifecycleListener {

	void preSave() throws GkException;

	void afterSave() throws GkException;

	void preLoad() throws GkException;

	void afterLoad() throws GkException;
}
