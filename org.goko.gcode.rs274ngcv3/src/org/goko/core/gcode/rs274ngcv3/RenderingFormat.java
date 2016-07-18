/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3;

import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.measure.units.Unit;

/**
 * @author PsyKo
 * @date 4 dec. 2015
 */
public class RenderingFormat {
	public static final RenderingFormat DEFAULT = new RenderingFormat(true, false, false,5);
	public static final RenderingFormat COMPLETE = new RenderingFormat(false, false, false, null);
	private boolean skipComments;
	private boolean skipLineNumbers;
	private boolean truncateDecimals;
	private Integer decimalCount;
	
	/**
	 * @param skipComments
	 * @param skipLineNumbers
	 * @param truncateDecimals
	 */
	public RenderingFormat(boolean skipComments, boolean skipLineNumbers, boolean truncateDecimals, Integer decimalCount) {
		super();
		this.skipComments = skipComments;
		this.skipLineNumbers = skipLineNumbers;
		this.truncateDecimals = truncateDecimals;
		this.decimalCount = decimalCount;
	}
	
	
	public <Q extends Quantity<Q>> String format(Q quantity, Unit<Q> unit){
		return QuantityUtils.format(quantity, decimalCount, false, false, unit);
	}
	
	/**
	 * @return the skipComments
	 */
	public boolean isSkipComments() {
		return skipComments;
	}
	/**
	 * @param skipComments the skipComments to set
	 */
	public void setSkipComments(boolean skipComments) {
		this.skipComments = skipComments;
	}
	/**
	 * @return the skipLineNumbers
	 */
	public boolean isSkipLineNumbers() {
		return skipLineNumbers;
	}
	/**
	 * @param skipLineNumbers the skipLineNumbers to set
	 */
	public void setSkipLineNumbers(boolean skipLineNumbers) {
		this.skipLineNumbers = skipLineNumbers;
	}
	/**
	 * @return the truncateDecimals
	 */
	public boolean isTruncateDecimals() {
		return truncateDecimals;
	}
	/**
	 * @param truncateDecimals the truncateDecimals to set
	 */
	public void setTruncateDecimals(boolean truncateDecimals) {
		this.truncateDecimals = truncateDecimals;
	}
	/**
	 * @return the decimalCount
	 */
	public int getDecimalCount() {
		return decimalCount;
	}
	/**
	 * @param decimalCount the decimalCount to set
	 */
	public void setDecimalCount(int decimalCount) {
		this.decimalCount = decimalCount;
	}
}
