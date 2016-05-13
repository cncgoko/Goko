/**
 * 
 */
package org.goko.common.preferences.fieldeditor.ui;

import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.ui.converter.StringToAngleConverter;
import org.goko.common.preferences.fieldeditor.ui.converter.StringToQuantityConverter;
import org.goko.core.common.measure.quantity.Angle;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class UiAngleFieldEditor extends UiQuantityFieldEditor<Angle> {

	/**
	 * @param parent
	 * @param style
	 */
	public UiAngleFieldEditor(Composite parent, int style) {
		super(parent, style);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor#getQuantityConverter()
	 */
	@Override
	protected StringToQuantityConverter<Angle> getQuantityConverter() {
		return new StringToAngleConverter(this);
	}

}
