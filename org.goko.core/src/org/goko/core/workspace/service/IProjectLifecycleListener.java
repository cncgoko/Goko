package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;

public interface IProjectLifecycleListener {

	void beforeCreate() throws GkException;

	void afterCreate() throws GkException;

	void beforeSave() throws GkException;

	void afterSave() throws GkException;

	void beforeLoad() throws GkException;

	void afterLoad() throws GkException;

	void onProjectDirtyStateChange() throws GkException;
}
