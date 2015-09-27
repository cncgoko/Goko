/**
 * 
 */
package org.goko.core.config;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * @author PsyKo
 *
 */
public enum EnumUpdateCheckFrequency {
	EVERY_START("everyStart", "At every start"),
	ONCE_A_DAY("onceDay", "Once a day"),
	ONCE_A_WEEK("onceWeek", "Once a week");
	
	/** Code */ 
	private String code;
	/** Label */ 
	private String label; // FIXME add support for i18n
	/**
	 * @param code
	 */
	private EnumUpdateCheckFrequency(String code, String label) {
		this.code = code;
		this.label = label;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	public static EnumUpdateCheckFrequency getValue(String code) throws GkException{
		for (EnumUpdateCheckFrequency enumFrequency : values()) {
			if(StringUtils.equals(code, enumFrequency.getCode())){
				return enumFrequency;
			}
		}
		throw new GkTechnicalException("Unknown EnumUpdateCheckFrequency code requested ("+code+")...");
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
}
