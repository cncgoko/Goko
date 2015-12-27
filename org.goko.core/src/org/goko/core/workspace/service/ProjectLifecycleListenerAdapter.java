/**
 *
 */
package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;

/**
 * IProjectLifecycleListener empty implementation
 *
 * @author PsyKo
 * @date 16 déc. 2015
 */
public class ProjectLifecycleListenerAdapter implements IProjectLifecycleListener {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#beforeCreate()
	 */
	@Override
	public void beforeCreate() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#afterCreate()
	 */
	@Override
	public void afterCreate() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#beforeSave()
	 */
	@Override
	public void beforeSave() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#afterSave()
	 */
	@Override
	public void afterSave() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#beforeLoad()
	 */
	@Override
	public void beforeLoad() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#afterLoad()
	 */
	@Override
	public void afterLoad() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#onProjectDirtyStateChange()
	 */
	@Override
	public void onProjectDirtyStateChange() throws GkException {}

}
