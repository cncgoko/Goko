package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;

public interface IModifierUiProvider<G extends GCodeProvider, T extends IModifier<G>> {

	T createDefaultModifier(Integer idTargetGCodeProvider) throws GkException;
	
	boolean providesConfigurationPanelFor(IModifier<?> modifier) throws GkException;
	
	void createConfigurationPanelFor(Composite parent, IModifier<?> modifier) throws GkException;
		
	String getModifierName();
}
