/**
 *
 */
package org.goko.core.workspace.io;

import org.simpleframework.xml.Root;

/**
 * Defines an Xml Project container allowing to link to internal resources
 *
 * @author PsyKo
 * @date 9 déc. 2015
 */
@Root(name="container")
public abstract class XmlProjectContainer {
	private String type;
	
	/**
	 * @param type
	 */
	public XmlProjectContainer(String type) {
		super();
		this.type = type;
	}

	public String getType(){
		return type;
	}
}
