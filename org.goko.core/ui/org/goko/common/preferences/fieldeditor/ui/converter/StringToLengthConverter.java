/**
 * 
 */
package org.goko.common.preferences.fieldeditor.ui.converter;

import java.math.BigDecimal;

import org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class StringToLengthConverter extends StringToQuantityConverter<Length> {

	/**
	 * @param editor
	 */
	public StringToLengthConverter(UiQuantityFieldEditor<Length> editor) {
		super(editor, Length.class);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.converter.StringToQuantityConverter#createQuantity(java.math.BigDecimal, org.goko.core.common.measure.units.Unit)
	 */
	@Override
	protected Length createQuantity(BigDecimal value, Unit<Length> unit) {
		return Length.valueOf(value, unit);
	}

}
