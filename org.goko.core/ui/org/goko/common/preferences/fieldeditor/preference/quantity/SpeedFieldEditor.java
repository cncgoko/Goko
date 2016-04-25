/**
 * 
 */
package org.goko.common.preferences.fieldeditor.preference.quantity;

import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.QuantityFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Speed;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class SpeedFieldEditor extends QuantityFieldEditor<Speed> {

	/**
	 * @param parent
	 * @param style
	 */
	public SpeedFieldEditor(Composite parent, int style) {
		super(parent, style);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.QuantityFieldEditor#createQuantity(java.lang.String, org.goko.core.common.measure.units.Unit)
	 */
	@Override
	protected Speed createQuantity(String value) throws GkException {
		return Speed.parse(value);
	}

}
