/**
 * 
 */
package org.goko.common.preferences.fieldeditor.preference.validator;

/**
 * @author Psyko
 * @date 21 avr. 2017
 */
public class IntegerValidator extends NumberValidator<Integer>{

	
	/**
	 * @param min
	 * @param minIncluded
	 * @param max
	 * @param maxIncluded
	 */
	public IntegerValidator(Integer min, boolean minIncluded, Integer max, boolean maxIncluded, String errorMessage) {
		super(min, minIncluded, max, maxIncluded, errorMessage);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.validator.NumberValidator#getValue(java.lang.String)
	 */
	@Override
	protected Integer getValue(String strValue) {		
		return Integer.valueOf(strValue);
	}
	
}
