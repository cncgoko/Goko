/**
 * 
 */
package org.goko.common.preferences.fieldeditor.ui.converter;

import java.math.BigDecimal;

import org.eclipse.core.databinding.conversion.Converter;
import org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.units.Unit;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public abstract class StringToQuantityConverter<Q extends Quantity<Q>> extends Converter{
	private UiQuantityFieldEditor<Q> editor;
	
	/**
	 * @param fromType
	 * @param toType
	 */
	public StringToQuantityConverter(UiQuantityFieldEditor<Q> editor, Class<Q> toType) {
		super(String.class, toType);
		this.editor = editor;
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
	 */
	@Override
	public Object convert(Object fromObject) {		
		return createQuantity(new BigDecimal((String)fromObject), editor.getUnit());
	}
	
	protected abstract Q createQuantity(BigDecimal value, Unit<Q> unit);

}
