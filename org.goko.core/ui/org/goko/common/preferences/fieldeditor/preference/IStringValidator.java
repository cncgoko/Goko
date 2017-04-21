/**
 * 
 */
package org.goko.common.preferences.fieldeditor.preference;

/**
 * @author Psyko
 * @date 20 avr. 2017
 */
public interface IStringValidator {

	boolean isValid(String value);
	
	String getErrorMessage();
}
