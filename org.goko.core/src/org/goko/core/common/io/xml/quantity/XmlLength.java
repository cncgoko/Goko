package org.goko.core.common.io.xml.quantity;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;

public class XmlLength {
	/** The quantity */
	private BigDecimalQuantity<Length> quantity;

	/**
	 * Constructor
	 * @param quantity the underlying quantity
	 */
	public XmlLength(BigDecimalQuantity<Length> quantity) {
		super();
		this.quantity = quantity;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimalQuantity<Length> getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(BigDecimalQuantity<Length> quantity) {
		this.quantity = quantity;
	}


}
