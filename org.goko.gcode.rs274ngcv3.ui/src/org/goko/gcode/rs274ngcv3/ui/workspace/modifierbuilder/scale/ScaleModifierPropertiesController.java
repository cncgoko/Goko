/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.scale;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.scale.ScaleModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class ScaleModifierPropertiesController extends AbstractModifierPanelController<ScaleModifierPropertiesModel, ScaleModifier> {

	/**
	 * Constructo
	 */
	public ScaleModifierPropertiesController() {
		super(new ScaleModifierPropertiesModel());
	}
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initialize()
	 */
	@Override
	public void initialize() throws GkException {		
		getDataModel().setScaleFactor(new BigDecimal("100"));
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {
		getDataModel().setScaleFactor(getModifier().getScaleFactor());
		
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#updateModifier()
	 */
	@Override
	protected ScaleModifier updateModifier() throws GkException {
		getModifier().setScaleFactor(getDataModel().getScaleFactor());
		return getModifier();
	}

}
