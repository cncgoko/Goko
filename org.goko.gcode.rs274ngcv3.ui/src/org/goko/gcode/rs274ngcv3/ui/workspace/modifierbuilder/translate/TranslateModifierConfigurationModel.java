package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.translate;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.rs274ngcv3.modifier.TranslateModifier;

/**
 * Model for this configuration panel
 */
public class TranslateModifierConfigurationModel extends AbstractModelObject{
	private TranslateModifier modifier;
	
	public TranslateModifier getModifier() {
		return modifier;
	}

	public void setModifier(TranslateModifier modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the translationX
	 */
	public BigDecimalQuantity<Length> getTranslationX() {
		return modifier.getTranslationX();
	}

	/**
	 * @param translationX the translationX to set
	 */
	public void setTranslationX(BigDecimalQuantity<Length> translationX) {
		BigDecimalQuantity<Length> oldValue = modifier.getTranslationX();
		modifier.setTranslationX(translationX);
		firePropertyChange("translationX", oldValue, modifier.getTranslationX());
		
	}

	/**
	 * @return the translationY
	 */
	public BigDecimalQuantity<Length> getTranslationY() {
		return modifier.getTranslationY();
	}

	/**
	 * @param translationY the translationY to set
	 */
	public void setTranslationY(BigDecimalQuantity<Length> translationY) {
		BigDecimalQuantity<Length> oldValue = modifier.getTranslationY();
		modifier.setTranslationY(translationY);
		firePropertyChange("translationY", oldValue, modifier.getTranslationY());
	}

	/**
	 * @return the translationZ
	 */
	public BigDecimalQuantity<Length> getTranslationZ() {
		return modifier.getTranslationZ();
	}

	/**
	 * @param translationZ the translationZ to set
	 */
	public void setTranslationZ(BigDecimalQuantity<Length> translationZ) {
		BigDecimalQuantity<Length> oldValue = modifier.getTranslationZ();
		modifier.setTranslationZ(translationZ);
		firePropertyChange("translationZ", oldValue, modifier.getTranslationZ());
	}
	
	
}