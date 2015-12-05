/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3;

/**
 * @author PsyKo
 * @date 4 déc. 2015
 */
public class RenderingFormat {
	public static final RenderingFormat DEFAULT = new RenderingFormat(true, false, false);
	private boolean skipComments;
	private boolean skipLineNumbers;
	private boolean truncateDecimals;
	
	/**
	 * @param skipComments
	 * @param skipLineNumbers
	 * @param truncateDecimals
	 */
	public RenderingFormat(boolean skipComments, boolean skipLineNumbers, boolean truncateDecimals) {
		super();
		this.skipComments = skipComments;
		this.skipLineNumbers = skipLineNumbers;
		this.truncateDecimals = truncateDecimals;
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
}
