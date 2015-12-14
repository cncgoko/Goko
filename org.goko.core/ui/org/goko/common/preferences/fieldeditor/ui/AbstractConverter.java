package org.goko.common.preferences.fieldeditor.ui;

import org.eclipse.core.databinding.conversion.Converter;

public abstract class AbstractConverter<F, T> extends Converter{

	public AbstractConverter(Class<F> fromType, Class<T> toType) {
		super(fromType, toType);		
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
	 */
	@Override
	public final Object convert(Object fromObject) {		
		return convertObject((F) fromObject);
	}
	
	public abstract T convertObject(F fromObject) ;

	
}
