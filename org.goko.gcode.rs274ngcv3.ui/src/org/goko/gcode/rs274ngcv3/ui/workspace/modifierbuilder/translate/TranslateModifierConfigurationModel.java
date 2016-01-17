package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.translate;

import org.goko.core.common.measure.quantity.Length;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierModelObject;

/**
 * Model for this configuration panel
 */
public class TranslateModifierConfigurationModel extends AbstractModifierModelObject{
	private Length translationX;
	private Length translationY;
	private Length translationZ;
	
	/**
	 * @return the translationX
	 */
	public Length getTranslationX() {
		return translationX;
	}

	/**
	 * @param translationX the translationX to set
	 */
	public void setTranslationX(Length translationX) {		
		firePropertyChange("translationX", this.translationX, this.translationX = translationX);
		
	}

	/**
	 * @return the translationY
	 */
	public Length getTranslationY() {
		return translationY;
	}

	/**
	 * @param translationY the translationY to set
	 */
	public void setTranslationY(Length translationY) {
		firePropertyChange("translationY", this.translationY, this.translationY = translationY);
	}

	/**
	 * @return the translationZ
	 */
	public Length getTranslationZ() {
		return translationZ;
	}

	/**
	 * @param translationZ the translationZ to set
	 */
	public void setTranslationZ(Length translationZ) {
		firePropertyChange("translationZ", this.translationZ, this.translationZ = translationZ);
	}
	
	
}