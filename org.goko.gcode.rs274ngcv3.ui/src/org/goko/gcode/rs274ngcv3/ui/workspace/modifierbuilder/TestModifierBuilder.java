package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.TestModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

public class TestModifierBuilder implements IModifierUiProvider<GCodeProvider, TestModifier> {

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createDefaultModifier(java.lang.Integer)
	 */
	@Override
	public TestModifier createDefaultModifier(Integer idTargetGCodeProvider) throws GkException {		
		return new TestModifier(idTargetGCodeProvider);
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#getModifierName()
	 */
	@Override
	public String getModifierName() {
		return "Test";
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#providesConfigurationPanelFor(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public boolean providesConfigurationPanelFor(IModifier<?> modifier) throws GkException {
		return modifier instanceof TestModifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createConfigurationPanelFor(org.eclipse.swt.widgets.Composite, org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public void createConfigurationPanelFor(Composite parent, IModifier<?> modifier) throws GkException {
		// TODO Auto-generated method stub
		
	}

}
