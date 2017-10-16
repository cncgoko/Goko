/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.preferences.renderingformat;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.config.GkPreference;
import org.goko.core.log.GkLog;

/**
 * Very (too) simple rendering preferences for GCode
 * 
 * @author Psyko
 * @date 20 juil. 2016
 */
public class GCodePreference extends GkPreference{	
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GCodePreference.class);
	/** Node id */ 
	public static final String NODE_ID = "org.goko.gcode.rs274ngcv3.ui";
	/** Singleton */
	private static GCodePreference instance;
	
	public static final String SKIP_COMMENT 		= "renderingFormat.skipComments";
	public static final String SKIP_LINE_NUMBER 	= "renderingFormat.skipLineNumber";
	public static final String TRUNCATE_DECIMAL 	= "renderingFormat.truncateDecimal";
	public static final String DECIMAL_DIGIT_COUNT	 	= "renderingFormat.decimalDigitCount";
	
	public static final String ARC_TOLERANCE_CHECK_ENABLED	 	= "gcode.testArcTolerance.enabled";
	public static final String ARC_TOLERANCE_THRESHOLD		 	= "gcode.testArcTolerance.threshold";
	
	/**
	 * Constructor
	 */
	public GCodePreference() {
		super(NODE_ID);
	}
	
	/**
	 * Singleton access
	 */
	public static GCodePreference getInstance() {
		if(instance == null){
			instance = new GCodePreference();
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
	
	/**
	 * Returns the state of the arc tolerance check
	 * @return the state of the arc tolerance check
	 */
	public boolean isArcToleranceCheckEnabled(){
		return getBoolean(ARC_TOLERANCE_CHECK_ENABLED);
	}
	
	/**
	 * Returns the threshold of the arc tolerance check
	 * @return the threshold of the arc tolerance check
	 * @throws GkException GkException 
	 */
	public Length getArcToleranceCheckThreshold(){		
		try {
			return Length.parse(getString(ARC_TOLERANCE_THRESHOLD));
		} catch (GkException e) {			
			LOG.error(e);
			return Length.valueOf("0.002", LengthUnit.MILLIMETRE);
		}
	}
}
