package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;

public interface IModifierUiProvider<T extends IModifier<GCodeProvider>> {

	T createDefaultModifier(Integer idTargetGCodeProvider) throws GkException;
	
	boolean providesConfigurationPanelFor(IModifier<?> modifier) throws GkException;
	
	IModifierPropertiesPanel<T> createConfigurationPanelFor(Composite parent, IModifier<?> modifier) throws GkException;
			
	String getModifierName();
}
