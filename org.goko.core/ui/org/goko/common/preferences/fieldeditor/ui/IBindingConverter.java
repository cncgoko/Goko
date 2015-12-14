package org.goko.common.preferences.fieldeditor.ui;

import org.eclipse.core.databinding.conversion.Converter;

public interface IBindingConverter {
	
	Converter getTargetToModelConverter();
	
	Converter getModelToTargetConverter();
}
