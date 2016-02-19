/**
 * 
 */
package org.goko.tools.autoleveler.io.xml;

import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;
import org.simpleframework.xml.Element;

/**
 * @author PsyKo
 * @date 19 févr. 2016
 */
@DerivedType(parent=XmlGCodeModifier.class, name="modifier:autoleveler")
public class XmlGridAutoLevelerModifier extends XmlGCodeModifier{
	@Attribute
	private XmlLength theoricHeight;
	@Element
	private AbstractXmlHeightMap heightMap;
	/**
	 * @return the theoricHeight
	 */
	public XmlLength getTheoricHeight() {
		return theoricHeight;
	}
	/**
	 * @param theoricHeight the theoricHeight to set
	 */
	public void setTheoricHeight(XmlLength theoricHeight) {
		this.theoricHeight = theoricHeight;
	}
	/**
	 * @return the heightMap
	 */
	public AbstractXmlHeightMap getHeightMap() {
		return heightMap;
	}
	/**
	 * @param heightMap the heightMap to set
	 */
	public void setHeightMap(AbstractXmlHeightMap heightMap) {
		this.heightMap = heightMap;
	}
	
		
}
