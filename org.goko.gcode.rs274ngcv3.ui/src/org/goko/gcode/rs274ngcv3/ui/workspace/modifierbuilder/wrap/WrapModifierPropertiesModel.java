/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.wrap;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.rs274ngcv3.modifier.wrap.WrapModifierAxis;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierModelObject;

/**
 * @author Psyko
 * @date 29 avr. 2016
 */
public class WrapModifierPropertiesModel extends AbstractModifierModelObject{
	public static final String AXIS = "axis";
	public static final String RADIUS = "radius";
	private Length radius;
	private WrapModifierAxis axis;

	/**
	 * @return the radius
	 */
	public Length getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(Length radius) {
		firePropertyChange(RADIUS, this.radius, this.radius = radius);
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
