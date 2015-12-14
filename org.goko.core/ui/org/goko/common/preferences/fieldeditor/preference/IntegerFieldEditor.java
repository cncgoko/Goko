package org.goko.common.preferences.fieldeditor.preference;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.IntegerValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * An Integer field editor 
 * @author PsyKo
 *
 */
public class IntegerFieldEditor extends StringFieldEditor {
	/** Validator used for integer validation */
	private IntegerValidator validator;
	
	public IntegerFieldEditor(Composite parent, int style) {
		super(parent, style | SWT.RIGHT);
		setErrorMessage("A valid integer is required");
		validator = new IntegerValidator();
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.StringFieldEditor#isValidValue()
	 */
	@Override
	protected boolean isValidValue() {
		boolean valid = true;
		if(StringUtils.isNotBlank(getText())){
			valid = validator.isValid(getText());
		}
		return valid;
	}
}
