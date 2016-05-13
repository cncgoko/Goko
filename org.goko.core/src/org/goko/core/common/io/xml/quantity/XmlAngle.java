package org.goko.core.common.io.xml.quantity;

import org.goko.core.common.measure.quantity.Angle;

public class XmlAngle {
	/** The quantity */
	private Angle quantity;

	/**
	 * Constructor
	 * @param quantity the underlying quantity
	 */
	public XmlAngle(Angle quantity) {
		super();
		this.quantity = quantity;
	}

	/**
	 * @return the quantity
	 */
	public Angle getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Angle quantity) {
		this.quantity = quantity;
	}

	public static Angle valueOf(XmlAngle xmlLength){
		if(xmlLength != null){
			return xmlLength.getQuantity();
		}
		return null;
	}
	
	public static XmlAngle valueOf(Angle length){
		if(length != null){
			return new XmlAngle(length);
		}
		return null;
	}
}
