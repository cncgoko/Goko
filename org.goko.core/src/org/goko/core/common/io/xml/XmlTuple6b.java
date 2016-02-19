/**
 * 
 */
package org.goko.core.common.io.xml;

import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.math.Tuple6b;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author PsyKo
 * @date 19 févr. 2016
 */
@Root(name="tuple6b")
public class XmlTuple6b {
	@Attribute
	private XmlLength x;
	@Attribute
	private XmlLength y;
	@Attribute
	private XmlLength z;
	
	public static XmlTuple6b valueOf(Tuple6b tuple){
		XmlTuple6b xml = new XmlTuple6b();
		xml.setX( XmlLength.valueOf(tuple.getX()));
		xml.setY( XmlLength.valueOf(tuple.getY()));
		xml.setZ( XmlLength.valueOf(tuple.getZ()));
		return xml;
	}
	
	public static Tuple6b valueOf(XmlTuple6b tuple){
		return new Tuple6b( XmlLength.valueOf(tuple.getX()), XmlLength.valueOf(tuple.getY()), XmlLength.valueOf(tuple.getZ()));
	}
	/**
	 * @return the x
	 */
	public XmlLength getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(XmlLength x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public XmlLength getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(XmlLength y) {
		this.y = y;
	}
	/**
	 * @return the z
	 */
	public XmlLength getZ() {
		return z;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(XmlLength z) {
		this.z = z;
	}
	
}
