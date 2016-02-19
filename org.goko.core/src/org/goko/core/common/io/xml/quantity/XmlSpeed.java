package org.goko.core.common.io.xml.quantity;

import org.goko.core.common.measure.quantity.Speed;

public class XmlSpeed {
	/** The quantity */
	private Speed quantity;

	/**
	 * Constructor
	 * @param quantity the underlying quantity
	 */
	public XmlSpeed(Speed quantity) {
		super();
		this.quantity = quantity;
	}

	/**
	 * @return the quantity
	 */
	public Speed getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Speed quantity) {
		this.quantity = quantity;
	}

	public static Speed valueOf(XmlSpeed xmlSpeed){
		if(xmlSpeed != null){
			return xmlSpeed.getQuantity();
		}
		return null;
	}
	
	public static XmlSpeed valueOf(Speed speed){
		if(speed != null){
			return new XmlSpeed(speed);
		}
		return null;
	}
}
