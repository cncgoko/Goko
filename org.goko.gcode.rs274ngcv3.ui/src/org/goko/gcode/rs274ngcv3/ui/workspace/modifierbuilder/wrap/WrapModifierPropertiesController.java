/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.wrap;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.wrap.WrapModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController;

/**
 * @author Psyko
 * @date 29 avr. 2016
 */
public class WrapModifierPropertiesController extends AbstractModifierPanelController<WrapModifierPropertiesModel, WrapModifier>{

	/**
	 * Constructeur
	 */
	public WrapModifierPropertiesController() {
		super(new WrapModifierPropertiesModel());
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {
		WrapModifier lclModifier = getModifier();
		getDataModel().setAxis(lclModifier.getAxis());
		getDataModel().setRatio(lclModifier.getRatio());
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#updateModifier()
	 */
	@Override
	protected WrapModifier updateModifier() throws GkException {
		WrapModifier lclModifier = getModifier();
		lclModifier.setRatio(getDataModel().getRatio());
		lclModifier.setAxis(getDataModel().getAxis());		
		return lclModifier;
	}

}
