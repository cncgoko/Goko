/**
 *
 */
package org.goko.core.workspace.io;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
@Root
public class XmlProjectContainer {
	/** The type of this container */
	@Attribute
	private String type;
	@Transient
	private Object content;
	
	/**
	 * @param type
	 * @param content
	 */
	public XmlProjectContainer(String type, Object content) {
		super();
		this.type = type;
		this.content = content;
	}

	/**
	 * @return the content
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Object content) {
		this.content = content;
	}

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
}
