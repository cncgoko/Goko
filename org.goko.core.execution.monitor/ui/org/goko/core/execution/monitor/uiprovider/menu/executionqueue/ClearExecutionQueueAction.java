package org.goko.core.execution.monitor.uiprovider.menu.executionqueue;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;

/**
 * Action to clear the execution queue 
 * 
 * @author PsyKo
 * @date 29 nov. 2015
 */
public class ClearExecutionQueueAction extends Action {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ClearExecutionQueueAction.class);
	/** Target execution service */
	private IExecutionService<?,?> executionService;

	/**
	 * @param idTarget
	 */
	public ClearExecutionQueueAction(IExecutionService<?,?> executionService) {
		super("Clear execution queue");		
		this.executionService = executionService;
	}
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		try {
			executionService.clearExecutionQueue(ExecutionQueueType.DEFAULT);			
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		Image image = ResourceManager.getPluginImage("org.goko.core.workspace", "icons/menu/cross.png");	        	
    	return ImageDescriptor.createFromImage(image);
	}
}
