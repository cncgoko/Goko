/**
 * 
 */
package org.goko.common.preferences.fieldeditor.ui.converter;

import java.math.BigDecimal;

import org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.units.Unit;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class StringToAngleConverter extends StringToQuantityConverter<Angle> {

	/**
	 * @param editor
	 */
	public StringToAngleConverter(UiQuantityFieldEditor<Angle> editor) {
		super(editor, Angle.class);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.converter.StringToQuantityConverter#createQuantity(java.math.BigDecimal, org.goko.core.common.measure.units.Unit)
	 */
	@Override
	protected Angle createQuantity(BigDecimal value, Unit<Angle> unit) {
		return Angle.valueOf(value, unit);
	}

}
