package org.goko.gcode.rs274ngcv3.ui.workspace.io.bean;

import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

@DerivedType(parent=XmlGCodeModifier.class, name="modifier:translate")
public class XmlTranslateModifier extends XmlGCodeModifier {
	@Attribute
	private XmlLength x;
	@Attribute
	private XmlLength y;
	@Attribute
	private XmlLength z;

	/**
	 * @return the x
	 */
	public XmlLength getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(XmlLength x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public XmlLength getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(XmlLength y) {
		this.y = y;
	}
	/**
	 * @return the z
	 */
	public XmlLength getZ() {
		return z;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(XmlLength z) {
		this.z = z;
	}

}
