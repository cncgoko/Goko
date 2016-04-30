/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.wrap;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.wrap.WrapModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;

/**
 * @author Psyko
 * @date 28 avr. 2016
 */
public class WrapModifierBuilder extends AbstractModifierUiProvider<WrapModifier> implements IModifierUiProvider<WrapModifier>{

	/**
	 * @param modifierClass
	 */
	public WrapModifierBuilder() {
		super(WrapModifier.class);
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createDefaultModifier(java.lang.Integer)
	 */
	@Override
	public WrapModifier createDefaultModifier(Integer idTargetGCodeProvider) throws GkException {
		WrapModifier modifier = new WrapModifier();
		modifier.setIdGCodeProvider(idTargetGCodeProvider);
		return modifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#providesConfigurationPanelFor(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public boolean providesConfigurationPanelFor(IModifier<?> modifier) throws GkException {
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#getModifierName()
	 */
	@Override
	public String getModifierName() {
		return "Wrap";
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider#createPropertiesPanelForModifier(org.eclipse.swt.widgets.Composite, org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	protected IModifierPropertiesPanel<WrapModifier> createPropertiesPanelForModifier(Composite parent, WrapModifier modifier) throws GkException {
		WrapPropertiesPanel panel = new WrapPropertiesPanel(getContext());
		panel.createContent(parent);
		parent.layout();
		return panel;
	}

}
