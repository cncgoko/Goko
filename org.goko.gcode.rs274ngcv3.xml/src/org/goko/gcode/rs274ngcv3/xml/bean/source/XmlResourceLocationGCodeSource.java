package org.goko.gcode.rs274ngcv3.xml.bean.source;

import org.goko.core.workspace.io.xml.XmlResourceLocation;
import org.simpleframework.xml.DerivedType;
import org.simpleframework.xml.Element;


/**
 * XML description of a ResourceLocationGCodeSource
 * @author PsyKo
 * @date 19 mars 2016
 */
@DerivedType(parent=XmlGCodeProviderSource.class, name="source:resource")
public class XmlResourceLocationGCodeSource extends XmlGCodeProviderSource{
	/** URI of the resource of the file */
	@Element
	private XmlResourceLocation resourceLocation;

	/**
	 * @return the resourceLocation
	 */
	public XmlResourceLocation getResourceLocation() {
		return resourceLocation;
	}

	/**
	 * @param resourceLocation the resourceLocation to set
	 */
	public void setResourceLocation(XmlResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation;
	}
}
