package org.goko.common.preferences.fieldeditor.ui.converter;

import java.math.BigDecimal;

import org.goko.common.preferences.fieldeditor.ui.AbstractConverter;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.BigDecimalUtils;

public class StringToBigDecimalConverter extends AbstractConverter<String, BigDecimal> {

	public StringToBigDecimalConverter() {
		super(String.class, BigDecimal.class);		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.AbstractConverter#convertObject(java.lang.Object)
	 */
	@Override
	public BigDecimal convertObject(String fromObject) {
		try {
			return BigDecimalUtils.parse(fromObject);
		} catch (GkException e) {
			return null;
		}
	}

}
