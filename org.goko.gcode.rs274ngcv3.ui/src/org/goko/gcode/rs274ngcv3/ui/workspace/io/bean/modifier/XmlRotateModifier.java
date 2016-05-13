package org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier;

import org.goko.core.common.io.xml.quantity.XmlAngle;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeModifier;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

@DerivedType(parent=XmlGCodeModifier.class, name="modifier:rotate")
public class XmlRotateModifier extends XmlGCodeModifier {
	@Attribute
	private String axis;
	@Attribute
	private XmlAngle rotationAngle;
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
	 * @return the rotationAngle
	 */
	public XmlAngle getRotationAngle() {
		return rotationAngle;
	}
	/**
	 * @param rotationAngle the rotationAngle to set
	 */
	public void setRotationAngle(XmlAngle rotationAngle) {
		this.rotationAngle = rotationAngle;
	}	
	
	
}
