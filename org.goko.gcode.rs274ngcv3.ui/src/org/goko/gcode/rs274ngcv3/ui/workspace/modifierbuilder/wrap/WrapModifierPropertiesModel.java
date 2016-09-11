/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.wrap;

import java.math.BigDecimal;

import org.goko.core.gcode.rs274ngcv3.modifier.wrap.WrapModifierAxis;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierModelObject;

/**
 * @author Psyko
 * @date 29 avr. 2016
 */
public class WrapModifierPropertiesModel extends AbstractModifierModelObject{
	public static final String AXIS = "axis";
	public static final String RATIO = "ratio";
	private BigDecimal ratio;
	private WrapModifierAxis axis;

	/**
	 * @return the radius
	 */
	public BigDecimal getRatio() {
		return ratio;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRatio(BigDecimal ratio) {
		firePropertyChange(RATIO, this.ratio, this.ratio = ratio);
	}

	/**
	 * @return the axis
	 */
	public WrapModifierAxis getAxis() {
		return axis;
	}

	/**
	 * @param axis the axis to set
	 */
	public void setAxis(WrapModifierAxis axis) {
		firePropertyChange(AXIS, this.axis, this.axis = axis);
	}
	
}
