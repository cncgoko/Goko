package org.goko.common.preferences.fieldeditor.ui.converter;

import java.math.BigDecimal;

import org.goko.common.preferences.fieldeditor.ui.AbstractConverter;
import org.goko.core.common.utils.BigDecimalUtils;

public class BigDecimalToStringConverter extends AbstractConverter<BigDecimal, String> {

	public BigDecimalToStringConverter() {
		super(BigDecimal.class, String.class);		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.AbstractConverter#convertObject(java.lang.Object)
	 */
	@Override
	public String convertObject(BigDecimal fromObject) {
		return BigDecimalUtils.toString(fromObject);
	}

}
