package org.goko.common.preferences.fieldeditor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class BigDecimalFieldEditor extends StringFieldEditor {
	private BigDecimalValidator validator;
	
	public BigDecimalFieldEditor(Composite parent, int style) {
		super(parent, style);
		setErrorMessage("A valid number is required");
		validator = new BigDecimalValidator();
	}
	
	@Override
	protected void createControls(Composite parent, int style) {	
		createLayout(parent);
		labelControl = new Label(this, style);    	
    	labelControl.setText("Default");
    	labelControl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		control = new Text(this, SWT.BORDER | SWT.RIGHT);    	
    	control.setText("Text");
    	control.setLayoutData(new GridData(SWT.LEFT, SWT.RIGHT, true, false, 1, 1));
    	control.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyReleased(KeyEvent e) {    			
    			valueChanged();
    		}
		});
    	control.pack();
    	this.pack();
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.StringFieldEditor#isValidValue()
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
