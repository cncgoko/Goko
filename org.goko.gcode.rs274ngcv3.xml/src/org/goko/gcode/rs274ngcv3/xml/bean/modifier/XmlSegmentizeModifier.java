package org.goko.gcode.rs274ngcv3.xml.bean.modifier;

import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.gcode.rs274ngcv3.xml.bean.XmlGCodeModifier;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

@DerivedType(parent=XmlGCodeModifier.class, name="modifier:segmentize")
public class XmlSegmentizeModifier extends XmlGCodeModifier {
	@Attribute
	private XmlLength chordalTolerance;

	/**
	 * @return the chordalTolerance
	 */
	public XmlLength getChordalTolerance() {
		return chordalTolerance;
	}

	/**
	 * @param chordalTolerance the chordalTolerance to set
	 */
	public void setChordalTolerance(XmlLength chordalTolerance) {
		this.chordalTolerance = chordalTolerance;
	}

}
