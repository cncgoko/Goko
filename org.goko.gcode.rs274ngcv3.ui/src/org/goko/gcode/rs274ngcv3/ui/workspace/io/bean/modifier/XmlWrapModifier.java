/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier;

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
	@Attribute
	private XmlLength radius;
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
	public XmlLength getRadius() {
		return radius;
	}
	/**
	 * @param radius the radius to set
	 */
	public void setRadius(XmlLength radius) {
		this.radius = radius;
	}
	
	
}
