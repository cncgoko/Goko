package org.goko.core.workspace.io.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;

/**
 * @author PsyKo
 * @date 19 mars 2016
 */
@DerivedType(parent=XmlResourceLocation.class, name="resource:uri")
public class XmlURIResourceLocation extends XmlResourceLocation{
	/** The URI of the target resource */
	@Attribute
	private String uri;

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
}
