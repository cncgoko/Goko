package org.goko.core.common.io.xml.quantity;

import org.goko.core.common.measure.quantity.Length;

public class XmlLength {
	/** The quantity */
	private Length quantity;

	/**
	 * Constructor
	 * @param quantity the underlying quantity
	 */
	public XmlLength(Length quantity) {
		super();
		this.quantity = quantity;
	}

	/**
	 * @return the quantity
	 */
	public Length getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Length quantity) {
		this.quantity = quantity;
	}


}
