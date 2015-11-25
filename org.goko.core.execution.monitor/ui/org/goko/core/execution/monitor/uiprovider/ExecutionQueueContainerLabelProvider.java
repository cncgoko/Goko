package org.goko.core.execution.monitor.uiprovider;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.workspace.bean.ProjectContainer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;

public class ExecutionQueueContainerLabelProvider extends LabelProvider implements IStyledLabelProvider {

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		if(element instanceof ProjectContainer){
			StyledString styleString = new StyledString();			
			styleString.append("Execution queue ");			
			return styleString;
		}else if(element instanceof ExecutionToken){
			ExecutionToken<?> executionToken = (ExecutionToken<?>) element;
			StyledString styleString = new StyledString();			
			styleString.append(executionToken.getGCodeProvider().getCode());			
			return styleString;
		}
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if(element instanceof ProjectContainer){
			return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/category.png");
		}else if(element instanceof ExecutionToken){
			ExecutionToken<?> token = (ExecutionToken<?>) element;
			if(token.getState() == ExecutionState.COMPLETE){
				return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/tick.png");	
			}else if(token.getState() == ExecutionState.RUNNING){
				return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/control-running.png");				
			}
			
		}
		return null;
	}
}
