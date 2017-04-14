/**
 * 
 */
package org.goko.core.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Psyko
 * @date 9 avr. 2017
 */
public class NumberUtils {

	public static Integer parseIntegerOrNull(String value){
		Integer result = null;
		if(StringUtils.isNumeric(value)){
			result = Integer.valueOf(value);
		}
		return result;
	}
}
