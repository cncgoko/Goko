/**
 * 
 */
package org.goko.tools.macro.io.bean;

import org.goko.core.gcode.io.XmlGCodeProviderReference;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
@DerivedType(parent=XmlGCodeProviderReference.class, name=XmlGCodeMacroReference.REFERENCE_TYPE)
public class XmlGCodeMacroReference extends XmlGCodeProviderReference{
	/** Reference type */
	public static final String REFERENCE_TYPE ="reference:macro";
	/** Code of the GCode provider */
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
