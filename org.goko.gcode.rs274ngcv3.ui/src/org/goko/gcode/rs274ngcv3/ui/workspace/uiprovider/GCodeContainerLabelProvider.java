/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.workspace.bean.ProjectContainer;


/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public class GCodeContainerLabelProvider extends LabelProvider implements IStyledLabelProvider {
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		if(element instanceof IGCodeProvider){
			return new StyledString(((IGCodeProvider) element).getCode());
		}else if(element instanceof ProjectContainer){
			StyledString styleString = new StyledString();			
			styleString.append("GCode ");			
			return styleString;
		}else if(element instanceof IModifier){
			StyledString styleString = new StyledString();			
			styleString.append("Modifier ");			
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
			return ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/resource_persp.gif");
		}else if(element instanceof IGCodeProvider){
			return ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/gcode-provider-icon.png");
		}else if(element instanceof IModifier){
			return ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/modifier-on.png");
		}
		return super.getImage(element);
	}
}