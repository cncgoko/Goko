/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.modifier.wrap;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * @author Psyko
 * @date 30 avr. 2016
 */
public enum WrapModifierAxis {
	// FIXME : add i18n support
	X_TO_A_AXIS("X_TO_A_AXIS"," X to A Axis"),
	Y_TO_A_AXIS("Y_TO_A_AXIS"," Y to A Axis");
	/** Code of the enum */
	private String code;
	/** Label of the enum */
	private String label;
	
	/**
	 * Constructor
	 * @param code the code 
	 * @param label the label
	 */
	private WrapModifierAxis(String code, String label) {
		this.code = code;
		this.label = label;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Return the enum corresponding to the given code 
	 * @param axis the axis code 
	 * @return WrapModifierAxis
	 * @throws GkException GkException 
	 */
	public static WrapModifierAxis getEnum(String code) throws GkException {
		for (WrapModifierAxis enumAxis: values()) {
			if(StringUtils.equals(code, enumAxis.getCode())){
				return enumAxis;
			}
		}
		throw new GkTechnicalException("No WrapModifierAxis for code ["+code+"]");
	}
	
}
