/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.workspace.bean.IPropertiesPanel;

/**
 * @author PsyKo
 * @date 16 janv. 2016
 */
public interface IModifierPropertiesPanel<T extends IModifier<GCodeProvider>> extends IPropertiesPanel {

	void setModifier(T modifier);
	
	public abstract void initializeFromModifier() throws GkException;
}
