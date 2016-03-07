/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.scale;

import java.math.BigDecimal;

import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierModelObject;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class ScaleModifierPropertiesModel extends AbstractModifierModelObject {
	public static final String SCALE_FACTOR = "scaleFactor";
	/** The scale factor */
	private BigDecimal scaleFactor;

	/**
	 * @return the scaleFactor
	 */
	public BigDecimal getScaleFactor() {
		return scaleFactor;
	}
	/**
	 * @param scaleFactor the scaleFactor to set
	 */
	public void setScaleFactor(BigDecimal scaleFactor) {
		firePropertyChange(SCALE_FACTOR, this.scaleFactor, this.scaleFactor = scaleFactor);
	}
	
	
}
