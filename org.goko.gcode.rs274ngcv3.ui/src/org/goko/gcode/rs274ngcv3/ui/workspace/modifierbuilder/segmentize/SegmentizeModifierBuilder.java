package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.segmentize;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.segmentize.SegmentizeModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;

public class SegmentizeModifierBuilder extends AbstractModifierUiProvider<SegmentizeModifier> implements IModifierUiProvider<SegmentizeModifier>{

	/**
	 * Constructor
	 */
	public SegmentizeModifierBuilder() {
		super(SegmentizeModifier.class);
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createDefaultModifier(java.lang.Integer)
	 */
	@Override
	public SegmentizeModifier createDefaultModifier(Integer idTargetGCodeProvider) throws GkException {
		SegmentizeModifier modifier = new SegmentizeModifier(idTargetGCodeProvider);
		return modifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#getModifierName()
	 */
	@Override
	public String getModifierName() {
		return SegmentizeModifier.MODIFIER_NAME;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#providesConfigurationPanelFor(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public boolean providesConfigurationPanelFor(IModifier<?> modifier) throws GkException {
		return modifier instanceof SegmentizeModifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider#createPropertiesPanelForModifier(org.eclipse.swt.widgets.Composite, org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	protected IModifierPropertiesPanel<SegmentizeModifier> createPropertiesPanelForModifier(Composite parent, SegmentizeModifier modifier) throws GkException {		
		SegmentizePropertiesPanel panel = new SegmentizePropertiesPanel(getContext());
		panel.createContent(parent);
		parent.layout();
		return panel;
	}
}
