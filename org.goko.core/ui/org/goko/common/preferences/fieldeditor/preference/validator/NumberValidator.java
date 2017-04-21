/**
 * 
 */
package org.goko.common.preferences.fieldeditor.preference.validator;

import org.apache.commons.lang3.ObjectUtils;

/**
 * @author Psyko
 * @date 21 avr. 2017
 */
public abstract class NumberValidator<N extends Number & Comparable<N>> extends AbstractValidator {
	private N minValue;
	private N maxValue;
	private boolean minIncluded;
	private boolean maxIncluded;
	
	
	public NumberValidator(N min, boolean minIncluded, N max, boolean maxIncluded, String errorMessage) {
		super(errorMessage);
		this.minValue = min;
		this.maxValue = max;
		this.minIncluded = minIncluded;
		this.maxIncluded = maxIncluded;
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.IStringValidator#isValid(java.lang.String)
	 */
	@Override
	public boolean isValid(String strValue) {
		boolean result = true;
		N value = getValue(strValue);
		if(value == null){
			return false;
		}
		if(minValue != null){
			if( (minIncluded && ObjectUtils.compare(minValue, value) > 0)
			|| (!minIncluded && ObjectUtils.compare(minValue, value) >= 0)){
				result = false;
			}
		}
		if(maxValue != null){
			if( (maxIncluded && ObjectUtils.compare(maxValue, value) < 0)
			|| (!maxIncluded && ObjectUtils.compare(maxValue, value) <= 0)){
				result = false;
			}
		}
		return result;
	}
	
	protected abstract N getValue(String strValue);

}
