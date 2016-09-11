/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier;

import org.goko.core.common.io.xml.math.XmlBigDecimal;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeModifier;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

/**
 * XML description of the wrap modifier
 * @author Psyko
 * @date 30 avr. 2016
 */
@DerivedType(parent=XmlGCodeModifier.class, name="modifier:wrap")
public class XmlWrapModifier extends XmlGCodeModifier {
	@Attribute
	private String axis;
	/** Radius element remains only for backward compatibility (maybe not needed when not in strict mode) */
	@Attribute
	@Deprecated
	private XmlLength radius;
	@Attribute	
	private XmlBigDecimal ratio;
	/**
	 * @return the axis
	 */
	public String getAxis() {
		return axis;
	}
	/**
	 * @param axis the axis to set
	 */
	public void setAxis(String axis) {
		this.axis = axis;
	}
	/**
	 * @return the radius
	 */
	@Deprecated
	public XmlLength getRadius() {
		return radius;
	}
	/**
	 * @param radius the radius to set
	 */
	@Deprecated
	public void setRadius(XmlLength radius) {
		this.radius = radius;
	}
	/**
	 * @return the ratio
	 */
	public XmlBigDecimal getRatio() {
		return ratio;
	}
	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(XmlBigDecimal ratio) {
		this.ratio = ratio;
	}
	
	
}
