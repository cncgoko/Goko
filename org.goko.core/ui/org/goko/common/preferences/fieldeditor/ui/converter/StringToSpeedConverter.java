/**
 * 
 */
package org.goko.common.preferences.fieldeditor.ui.converter;

import java.math.BigDecimal;

import org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.units.Unit;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class StringToSpeedConverter extends StringToQuantityConverter<Speed> {

	/**
	 * @param editor
	 */
	public StringToSpeedConverter(UiQuantityFieldEditor<Speed> editor) {
		super(editor, Speed.class);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.converter.StringToQuantityConverter#createQuantity(java.math.BigDecimal, org.goko.core.common.measure.units.Unit)
	 */
	@Override
	protected Speed createQuantity(BigDecimal value, Unit<Speed> unit) {
		return Speed.valueOf(value, unit);
	}

}
