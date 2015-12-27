/**
 *
 */
package org.goko.core.workspace.io;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Defines an Xml Project container allowing to link to internal resources
 *
 * @author PsyKo
 * @date 9 déc. 2015
 */
@Root
public class XmlProjectContainer {
	/** The type of this container */
	@Attribute(name="content")
	private String type;
	/** The path to the data file */
	@Attribute
	private String path;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
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
