/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.source;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
@Root(name="file")
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
