package org.goko.gcode.rs274ngcv3.ui.workspace.io.bean;

import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

@DerivedType(parent=XmlGCodeModifier.class, name="modifier:scale")
public class XmlScaleModifier extends XmlGCodeModifier {
	@Attribute
	private String scaleFActor;

	/**
	 * @return the scaleFActor
	 */
	public String getScaleFActor() {
		return scaleFActor;
	}

	/**
	 * @param scaleFActor the scaleFActor to set
	 */
	public void setScaleFActor(String scaleFActor) {
		this.scaleFActor = scaleFActor;
	}
	
}
