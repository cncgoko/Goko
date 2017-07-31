/**
 * 
 */
package org.goko.core.common.i18n;

/**
 * Simple alias
 * @author Psyko
 * @date 5 juin 2017
 */
public class I18n {
	
	public static String get(String key, String... params){
		return MessageResource.get(key, params);
	}	
}
