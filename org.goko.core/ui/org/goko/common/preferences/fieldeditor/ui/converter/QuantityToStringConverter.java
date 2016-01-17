package org.goko.common.preferences.fieldeditor.ui.converter;

import org.eclipse.core.databinding.conversion.Converter;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.AbstractQuantity;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;

public class QuantityToStringConverter extends Converter {
	private static final GkLog LOG = GkLog.getLogger(QuantityToStringConverter.class);
		
	public QuantityToStringConverter() {
		super(AbstractQuantity.class, String.class);
	}

	/**
	 * (inheritDoc)
	 * 
	 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
	 */
	@Override
	public Object convert(Object fromObject) {
		try {
			return GokoPreference.getInstance().format((AbstractQuantity) fromObject, false, false);
		} catch (GkException e) {
			LOG.error(e);
			return "ERROR";
		}
	}

}