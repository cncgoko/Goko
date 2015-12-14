package org.goko.common.preferences.fieldeditor.ui;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.ui.converter.BigDecimalToStringConverter;
import org.goko.common.preferences.fieldeditor.ui.converter.BindingConverter;
import org.goko.common.preferences.fieldeditor.ui.converter.StringToBigDecimalConverter;
import org.goko.core.common.utils.BigDecimalUtils;

public class UiBigDecimalFieldEditor extends UiStringFieldEditor {

	public UiBigDecimalFieldEditor(Composite parent, int style) {
		super(parent, style);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiStringFieldEditor#isValidValue()
	 */
	@Override
	protected boolean isValidValue() {
		boolean valid = true;
		if(StringUtils.isNotBlank(getText())){			
			valid = BigDecimalUtils.isBigDecimal(getText());
		}
		return valid;
	}
		
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiStringFieldEditor#getConverter()
	 */
	@Override
	public IBindingConverter getConverter() {		
		return new BindingConverter(new StringToBigDecimalConverter(), new BigDecimalToStringConverter());
	}
}
