package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.modifier.TranslateModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

public class TranslateModifierBuilder implements IModifierUiProvider<GCodeProvider, TranslateModifier>{

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createDefaultModifier(java.lang.Integer)
	 */
	@Override
	public TranslateModifier createDefaultModifier(Integer idTargetGCodeProvider) throws GkException {
		return new TranslateModifier(idTargetGCodeProvider);
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#getModifierName()
	 */
	@Override
	public String getModifierName() throws GkException {
		return "Translate";
	}

}
