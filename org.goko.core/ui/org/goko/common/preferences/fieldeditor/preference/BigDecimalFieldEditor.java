package org.goko.common.preferences.fieldeditor.preference;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.BigDecimalUtils;

public class BigDecimalFieldEditor extends StringFieldEditor {
		
	public BigDecimalFieldEditor(Composite parent, int style) {
		super(parent, style);
		setErrorMessage("A valid number is required");		
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
	 * @see org.goko.common.preferences.fieldeditor.preference.StringFieldEditor#isValidInputValue()
	 */
	@Override
	protected boolean isValidInputValue() {
		boolean valid = true;
		if(StringUtils.isNotBlank(getText())){			
			valid = BigDecimalUtils.isBigDecimal(getText());
		}
		return valid;
	}
	
	@Override
	protected void loadValue() throws GkException {
		BigDecimal decimalValue =  new BigDecimal(getPreferenceStore().getString(getPreferenceName()));
		getControl().setText( BigDecimalUtils.toString(decimalValue));
		refreshValidState();
	}
	
	protected BigDecimal getBigDecimalValue(){
		return new BigDecimal(getPreferenceStore().getString(getPreferenceName()));
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.StringFieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {
		BigDecimal decimalValue = BigDecimalUtils.parse(getControl().getText());
		getPreferenceStore().setValue(getPreferenceName(), decimalValue.toString());
	}
}
