package org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier;

import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeModifier;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

@DerivedType(parent=XmlGCodeModifier.class, name="modifier:scale")
public class XmlScaleModifier extends XmlGCodeModifier {
	@Attribute
	private String scaleFactor;

	/**
	 * @return the scaleFActor
	 */
	public String getScaleFactor() {
		return scaleFactor;
	}

	/**
	 * @param scaleFActor the scaleFActor to set
	 */
	public void setScaleFactor(String scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
	
}
