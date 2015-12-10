/**
 *
 */
package org.goko.core.workspace.io;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
@Root(name="container")
public class XmlProjectContainer<T> {
	/** The type of this container */
	@Attribute(name="type")
	private String type;
	/** The content of this container*/
	@Element
	private T content;
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
	 * @return the content
	 */
	public T getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(T content) {
		this.content = content;
	}


}
