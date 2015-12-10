package org.goko.gcode.rs274ngcv3.ui.workspace.io;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="xmlGCodeProvider")
public class XmlGCodeProvider {

	@Attribute
	private String code;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
