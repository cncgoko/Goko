package org.goko.core.execution.monitor.uiprovider;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.log.GkLog;

public class ExecutionQueueContainerLabelProvider extends LabelProvider implements IStyledLabelProvider {
	private static final GkLog LOG = GkLog.getLogger(ExecutionQueueContainerLabelProvider.class);
	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		if(element instanceof ExecutionQueueContainerUiProvider){
			StyledString styleString = new StyledString();
			styleString.append("Execution queue ");
			return styleString;
		}else if(element instanceof ExecutionToken){
			ExecutionToken<?> executionToken = (ExecutionToken<?>) element;
			StyledString styleString = new StyledString();
			try {
				styleString.append(executionToken.getGCodeProvider().getCode());
			} catch (GkException e) {
				styleString.append("ERROR");
				LOG.error(e);
			}
			return styleString;
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if(element instanceof ExecutionQueueContainerUiProvider){
			return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/blue-documents-stack.png");
		}else if(element instanceof ExecutionToken){
			ExecutionToken<?> token = (ExecutionToken<?>) element;
			if(token.getState() == ExecutionState.COMPLETE){
				return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/tick.png");
			}else if(token.getState() == ExecutionState.RUNNING){
				return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/control-running.png");
			}else if(token.getState() == ExecutionState.PAUSED){
				return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/pause.gif");
			}else if(token.getState() == ExecutionState.ERROR){
				return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/cross.png");
			}else if(token.getState() == ExecutionState.STOPPED){
				return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/stop.gif");
			}

		}
		return null;
	}
}
