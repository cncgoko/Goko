package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.rotate;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.rotate.RotateModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;

public class RotateModifierBuilder extends AbstractModifierUiProvider<RotateModifier> implements IModifierUiProvider<RotateModifier>{

	/**
	 * Constructor
	 */
	public RotateModifierBuilder() {
		super(RotateModifier.class);
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createDefaultModifier(java.lang.Integer)
	 */
	@Override
	public RotateModifier createDefaultModifier(Integer idTargetGCodeProvider) throws GkException {
		RotateModifier modifier = new RotateModifier();
		modifier.setIdGCodeProvider(idTargetGCodeProvider);
		return modifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#getModifierName()
	 */
	@Override
	public String getModifierName() {
		return "Rotate";
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#providesConfigurationPanelFor(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public boolean providesConfigurationPanelFor(IModifier<?> modifier) throws GkException {
		return modifier instanceof RotateModifier;
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider#createPropertiesPanelForModifier(org.eclipse.swt.widgets.Composite, org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	protected IModifierPropertiesPanel<RotateModifier> createPropertiesPanelForModifier(Composite parent, RotateModifier modifier) throws GkException {
		RotatePropertiesPanel content = new RotatePropertiesPanel(getContext());
		content.createContent(parent);		
		parent.layout();
		return content;
	}
}
