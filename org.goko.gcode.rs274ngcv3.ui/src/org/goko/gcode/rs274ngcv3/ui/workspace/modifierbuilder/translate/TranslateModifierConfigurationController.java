package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.translate;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.translate.TranslateModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController;

/**
 *  Controller for this configuration panel
 */
public class TranslateModifierConfigurationController extends AbstractModifierPanelController<TranslateModifierConfigurationModel, TranslateModifier>{

	public TranslateModifierConfigurationController(TranslateModifierConfigurationModel binding) {
		super(binding);
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {
		TranslateModifier lclModifier = getModifier();
		getDataModel().setTranslationX(lclModifier.getTranslationX());
		getDataModel().setTranslationY(lclModifier.getTranslationY());
		getDataModel().setTranslationZ(lclModifier.getTranslationZ());
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#updateModifier()
	 */
	@Override
	protected TranslateModifier updateModifier() throws GkException {
		TranslateModifier lclModifier = getModifier();
		lclModifier.setTranslationX(getDataModel().getTranslationX());
		lclModifier.setTranslationY(getDataModel().getTranslationY());
		lclModifier.setTranslationZ(getDataModel().getTranslationZ());
		return lclModifier;
	}
	
}