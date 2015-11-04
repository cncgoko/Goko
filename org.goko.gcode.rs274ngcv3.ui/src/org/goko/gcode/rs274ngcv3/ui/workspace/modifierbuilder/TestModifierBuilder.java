package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
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
	public String getModifierName() throws GkException {
		return "Test";
	}

}
