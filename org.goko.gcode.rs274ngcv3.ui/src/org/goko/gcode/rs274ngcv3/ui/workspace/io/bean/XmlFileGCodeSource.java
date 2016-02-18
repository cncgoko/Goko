/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.bean;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
@DerivedType(parent=XmlGCodeProviderSource.class, name="source:file")
public class XmlFileGCodeSource extends XmlGCodeProviderSource{
	/** Paht of the file */
	@Attribute
	private String path;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
