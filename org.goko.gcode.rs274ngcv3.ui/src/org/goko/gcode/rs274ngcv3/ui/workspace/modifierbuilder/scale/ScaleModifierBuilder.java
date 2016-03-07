/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.scale;

import java.math.BigDecimal;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.scale.ScaleModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;

/**
 * @author PsyKo
 * @date 7 mars 2016
 */
public class ScaleModifierBuilder extends AbstractModifierUiProvider<ScaleModifier> implements IModifierUiProvider<ScaleModifier>{

	/**
	 * Constructor
	 */
	public ScaleModifierBuilder() {
		super(ScaleModifier.class);
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createDefaultModifier(java.lang.Integer)
	 */
	@Override
	public ScaleModifier createDefaultModifier(Integer idTargetGCodeProvider) throws GkException {
		ScaleModifier modifier = new ScaleModifier();
		modifier.setScaleFactor(BigDecimal.ONE);
		modifier.setIdGCodeProvider(idTargetGCodeProvider);
		return modifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#providesConfigurationPanelFor(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public boolean providesConfigurationPanelFor(IModifier<?> modifier) throws GkException {
		return modifier instanceof ScaleModifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#getModifierName()
	 */
	@Override
	public String getModifierName() {
		return "Scale";
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider#createPropertiesPanelForModifier(org.eclipse.swt.widgets.Composite, org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	protected IModifierPropertiesPanel<ScaleModifier> createPropertiesPanelForModifier(Composite parent, ScaleModifier modifier) throws GkException {
		ScalePropertiesPanel panel = new ScalePropertiesPanel(getContext());
		panel.createContent(parent);
		parent.layout();
		return panel;
	}

}
