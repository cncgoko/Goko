/**
 * 
 */
package org.goko.core.common.io.xml.math;

import java.math.BigDecimal;

/**
 * @author Psyko
 * @date 10 sept. 2016
 */
public class XmlBigDecimal {
	private BigDecimal value;
	
	/**
	 * Constructor
	 */
	public XmlBigDecimal(String strValue) {
		this.value = new BigDecimal(strValue);
	}
		

	/**
	 * Constructor
	 * @param value
	 */
	public XmlBigDecimal(BigDecimal value) {
		super();
		this.value = value;
	}



	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
