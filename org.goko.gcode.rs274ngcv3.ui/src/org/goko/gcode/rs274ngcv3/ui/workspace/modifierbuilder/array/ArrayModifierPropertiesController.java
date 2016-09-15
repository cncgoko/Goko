package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.array;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.array.ArrayModifier;
import org.goko.core.math.Tuple6b;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController;

/**
 *  Controller for this configuration panel
 */
public class ArrayModifierPropertiesController extends AbstractModifierPanelController<ArrayModifierPropertiesModel, ArrayModifier>{

	public ArrayModifierPropertiesController() {
		super(new ArrayModifierPropertiesModel());
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {
		ArrayModifier lclModifier = getModifier();
		getDataModel().setTranslationX(lclModifier.getOffset().getX());
		getDataModel().setTranslationY(lclModifier.getOffset().getY());
		getDataModel().setTranslationZ(lclModifier.getOffset().getZ());
		getDataModel().setTranslationA(lclModifier.getOffset().getA());
		getDataModel().setTranslationB(lclModifier.getOffset().getB());
		getDataModel().setTranslationC(lclModifier.getOffset().getC());
		getDataModel().setCount(lclModifier.getCount());
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#updateModifier()
	 */
	@Override
	protected ArrayModifier updateModifier() throws GkException {
		ArrayModifier lclModifier = getModifier();		
		lclModifier.setOffset(new Tuple6b(getDataModel().getTranslationX(),
		                                  getDataModel().getTranslationY(),
		                                  getDataModel().getTranslationZ(),
		                                  getDataModel().getTranslationA(),
		                                  getDataModel().getTranslationB(),
		                                  getDataModel().getTranslationC()));		
		
		lclModifier.setCount(getDataModel().getCount());
		return lclModifier;
	}
	
}