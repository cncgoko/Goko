/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.array;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.array.ArrayModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;

/**
 * @author Psyko
 * @date 15 sept. 2016
 */
public class ArrayModifierBuilder extends AbstractModifierUiProvider<ArrayModifier> implements IModifierUiProvider<ArrayModifier>{

	/**
	 * Constructor
	 */
	public ArrayModifierBuilder() {
		super(ArrayModifier.class);
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createDefaultModifier(java.lang.Integer)
	 */
	@Override
	public ArrayModifier createDefaultModifier(Integer idTargetGCodeProvider) throws GkException {
		ArrayModifier modifier = new ArrayModifier();
		modifier.setIdGCodeProvider(idTargetGCodeProvider);
		return modifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#getModifierName()
	 */
	@Override
	public String getModifierName() {
		return "Array";
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#providesConfigurationPanelFor(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public boolean providesConfigurationPanelFor(IModifier<?> modifier) throws GkException {
		return modifier instanceof ArrayModifier;
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider#createPropertiesPanelForModifier(org.eclipse.swt.widgets.Composite, org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	protected IModifierPropertiesPanel<ArrayModifier> createPropertiesPanelForModifier(Composite parent, ArrayModifier modifier) throws GkException {
		ArrayPropertiesPanel content = new ArrayPropertiesPanel(getContext());
		content.createContent(parent);		
		parent.layout();
		return content;
	}
}
