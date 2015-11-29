package org.goko.core.execution.monitor.uiprovider.menu.executiontoken;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.workspace.action.AbstractDeleteAction;

public class DeleteExecutionTokenAction extends AbstractDeleteAction {
	/** Target execution service */
	private IExecutionService<?,?> executionService;

	/**
	 * @param idTarget
	 */
	public DeleteExecutionTokenAction(IExecutionService<?,?> executionService, Integer idTarget) {
		super(idTarget);
		this.executionService = executionService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.action.AbstractDeleteAction#deleteById(java.lang.Integer)
	 */
	@Override
	protected void deleteById(Integer id) throws GkException {
		executionService.removeFromExecutionQueue(id);
	}
	

}
