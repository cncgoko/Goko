/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.segmentize;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.gcode.rs274ngcv3.modifier.segmentize.SegmentizeModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class SegmentizeModifierPropertiesController extends AbstractModifierPanelController<SegmentizeModifierPropertiesModel, SegmentizeModifier> {

	/**
	 * Constructo
	 */
	public SegmentizeModifierPropertiesController() {
		super(new SegmentizeModifierPropertiesModel());
	}
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initialize()
	 */
	@Override
	public void initialize() throws GkException {		
		getDataModel().setChordalTolerance(Length.valueOf(new BigDecimal("0.1"), LengthUnit.MILLIMETRE));
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {
		getDataModel().setChordalTolerance(getModifier().getChordalTolerance());
		
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#updateModifier()
	 */
	@Override
	protected SegmentizeModifier updateModifier() throws GkException {
		getModifier().setChordalTolerance(getDataModel().getChordalTolerance());
		return getModifier();
	}

}
