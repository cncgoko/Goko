package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;

public abstract class AbstractProjectLifecycleListenerAdapter implements IProjectLifecycleListener {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceListener#beforeCreate()
	 */
	@Override
	public void beforeCreate() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceListener#afterCreate()
	 */
	@Override
	public void afterCreate() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceListener#beforeSave()
	 */
	@Override
	public void beforeSave() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceListener#afterSave()
	 */
	@Override
	public void afterSave() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceListener#beforeLoad()
	 */
	@Override
	public void beforeLoad() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceListener#afterLoad()
	 */
	@Override
	public void afterLoad() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#onProjectDirtyStateChange()
	 */
	@Override
	public void onProjectDirtyStateChange() throws GkException {}
}
