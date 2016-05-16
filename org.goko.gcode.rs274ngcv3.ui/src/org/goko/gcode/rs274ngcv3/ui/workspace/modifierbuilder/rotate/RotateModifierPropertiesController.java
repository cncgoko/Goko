/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.rotate;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.rotate.RotateModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class RotateModifierPropertiesController extends AbstractModifierPanelController<RotateModifierPropertiesModel, RotateModifier> {

	/**
	 * Constructo
	 */
	public RotateModifierPropertiesController() {
		super(new RotateModifierPropertiesModel());
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {
		getDataModel().setRotationAngle(getModifier().getRotationAngle());
		getDataModel().setRotationAxis(getModifier().getRotationAxis());		
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#updateModifier()
	 */
	@Override
	protected RotateModifier updateModifier() throws GkException {
		getModifier().setRotationAngle(getDataModel().getRotationAngle());
		getModifier().setRotationAxis(getDataModel().getRotationAxis());
		return getModifier();
	}

}
