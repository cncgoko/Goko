/**
 * 
 */
package org.goko.common.preferences.fieldeditor.preference.quantity;

import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.QuantityFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class LengthFieldEditor extends QuantityFieldEditor<Length> {

	/**
	 * @param parent
	 * @param style
	 */
	public LengthFieldEditor(Composite parent, int style) {
		super(parent, style);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.QuantityFieldEditor#createQuantity(java.lang.String, org.goko.core.common.measure.units.Unit)
	 */
	@Override
	protected Length createQuantity(String value) throws GkException {
		return Length.parse(value);
	}

}
