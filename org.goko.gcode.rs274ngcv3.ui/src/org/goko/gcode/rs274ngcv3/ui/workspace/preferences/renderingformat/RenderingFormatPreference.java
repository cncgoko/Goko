/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.preferences.renderingformat;

import org.goko.core.config.GkPreference;

/**
 * Very (too) simple rendering preferences for GCode
 * 
 * @author Psyko
 * @date 20 juil. 2016
 */
public class RenderingFormatPreference extends GkPreference{	
	/** Node id */ 
	public static final String NODE = "org.goko.gcode.rs274ngcv3.ui";
	/** Singleton */
	private static RenderingFormatPreference instance;
	
	public static final String SKIP_COMMENT 		= "renderingFormat.skipComments";
	public static final String SKIP_LINE_NUMBER 	= "renderingFormat.skipLineNumber";
	public static final String TRUNCATE_DECIMAL 	= "renderingFormat.truncateDecimal";
	public static final String DECIMAL_DIGIT_COUNT	 	= "renderingFormat.decimalDigitCount";
	
	/**
	 * Constructor
	 */
	public RenderingFormatPreference() {
		super(NODE);
	}
	
	/**
	 * Singleton access
	 */
	public static RenderingFormatPreference getInstance() {
		if(instance == null){
			instance = new RenderingFormatPreference();
		}
		return instance;
	}
	
	/**
	 * Returns if this rendering format skips comments
	 * @return <code>true</code> if comments should be skipped, <code>false</code> otherwise
	 */
	public boolean isSkipComment(){
		return getBoolean(SKIP_COMMENT);
	}
	
	/**
	 * Returns if this rendering format skips line number
	 * @return <code>true</code> if line number should be skipped, <code>false</code> otherwise
	 */
	public boolean isSkipLineNumber(){
		return getBoolean(SKIP_LINE_NUMBER);
	}
	
	/**
	 * Returns if this rendering format truncates decimal
	 * @return <code>true</code> if decimal should be truncated, <code>false</code> otherwise
	 */
	public boolean isTruncateDecimal(){
		return getBoolean(TRUNCATE_DECIMAL);
	}
	
	/**
	 * Returns the count of digit to keep after comma when truncating decimals
	 * @return the count of decimal digits
	 */
	public int getDecimalDigitCount(){
		return getInt(DECIMAL_DIGIT_COUNT);
	}
}
