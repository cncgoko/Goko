/**
 * 
 */
package org.goko.common.preferences.fieldeditor.ui;

import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.ui.converter.StringToQuantityConverter;
import org.goko.common.preferences.fieldeditor.ui.converter.StringToSpeedConverter;
import org.goko.core.common.measure.quantity.Speed;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class UiSpeedFieldEditor extends UiQuantityFieldEditor<Speed> {

	/**
	 * @param parent
	 * @param style
	 */
	public UiSpeedFieldEditor(Composite parent, int style) {
		super(parent, style);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor#getQuantityConverter()
	 */
	@Override
	protected StringToQuantityConverter<Speed> getQuantityConverter() {
		return new StringToSpeedConverter(this);
	}

}
