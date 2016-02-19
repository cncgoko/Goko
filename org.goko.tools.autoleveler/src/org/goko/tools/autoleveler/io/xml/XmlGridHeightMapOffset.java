/**
 * 
 */
package org.goko.tools.autoleveler.io.xml;

import org.goko.core.common.io.xml.quantity.XmlLength;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author PsyKo
 * @date 19 févr. 2016
 */
@Root(name="offset")
public class XmlGridHeightMapOffset {
	@Attribute
	private Integer xCoord;
	@Attribute
	private Integer yCoord;
	@Attribute
	private XmlLength offset;
	/**
	 * @return the xCoord
	 */
	public Integer getxCoord() {
		return xCoord;
	}
	/**
	 * @param xCoord the xCoord to set
	 */
	public void setxCoord(Integer xCoord) {
		this.xCoord = xCoord;
	}
	/**
	 * @return the yCoord
	 */
	public Integer getyCoord() {
		return yCoord;
	}
	/**
	 * @param yCoord the yCoord to set
	 */
	public void setyCoord(Integer yCoord) {
		this.yCoord = yCoord;
	}
	/**
	 * @return the offset
	 */
	public XmlLength getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(XmlLength offset) {
		this.offset = offset;
	}
		
}
