package org.goko.common.preferences.fieldeditor.ui.converter;

import org.eclipse.core.databinding.conversion.Converter;
import org.goko.common.preferences.fieldeditor.ui.IBindingConverter;

public class BindingConverter implements IBindingConverter {
	private Converter targetToModelConverter;
	private Converter modelToTargetConverter;
	
	public BindingConverter(Converter targetToModelConverter, Converter modelToTargetConverter) {
		super();
		this.targetToModelConverter = targetToModelConverter;
		this.modelToTargetConverter = modelToTargetConverter;
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.IBindingConverter#getTargetToModelConverter()
	 */
	@Override
	public Converter getTargetToModelConverter() {
		return targetToModelConverter;
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.IBindingConverter#getModelToTargetConverter()
	 */
	@Override
	public Converter getModelToTargetConverter() {
		return modelToTargetConverter;
	}

}
