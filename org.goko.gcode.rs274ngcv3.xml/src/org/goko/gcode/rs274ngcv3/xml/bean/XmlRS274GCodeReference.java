/**
 * 
 */
package org.goko.gcode.rs274ngcv3.xml.bean;

import org.goko.core.gcode.io.XmlGCodeProviderReference;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
@DerivedType(parent=XmlGCodeProviderReference.class, name=XmlRS274GCodeReference.REFERENCE_TYPE)
public class XmlRS274GCodeReference extends XmlGCodeProviderReference{
	/** Reference type */
	public static final String REFERENCE_TYPE ="reference:rs274";
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
