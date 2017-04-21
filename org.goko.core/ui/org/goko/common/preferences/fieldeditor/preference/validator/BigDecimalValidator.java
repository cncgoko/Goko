/**
 * 
 */
package org.goko.common.preferences.fieldeditor.preference.validator;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.BigDecimalUtils;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 21 avr. 2017
 */
public class BigDecimalValidator extends NumberValidator<BigDecimal>{
	private static final GkLog LOG = GkLog.getLogger(IntegerValidator.class);
	
	/**
	 * @param min
	 * @param minIncluded
	 * @param max
	 * @param maxIncluded
	 */
	public BigDecimalValidator(BigDecimal min, boolean minIncluded, BigDecimal max, boolean maxIncluded, String errorMessage) {
		super(min, minIncluded, max, maxIncluded, errorMessage);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.validator.NumberValidator#getValue(java.lang.String)
	 */
	@Override
	protected BigDecimal getValue(String strValue) {		
		try {
			return BigDecimalUtils.parse(strValue);
		} catch (GkException e) {
			LOG.error(e);
		}
		return null;
	}

}
