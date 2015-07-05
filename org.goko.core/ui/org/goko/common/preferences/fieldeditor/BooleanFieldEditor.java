package org.goko.common.preferences.fieldeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;

/**
 * A field editor for a boolean type preference.
 */
public class BooleanFieldEditor extends FieldEditor<Button> {
	
	public BooleanFieldEditor(Composite parent, int style) {
		super(parent, style);	
		createControls(parent, style);
	}

	@Override
	protected void createLayout(Composite parent) {		
    	GridLayout layout = new GridLayout(1, false);
    	layout.marginHeight = 2;
    	layout.marginWidth = 2;
    	setLayout(layout);  
	}
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#createControls(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void createControls(Composite parent, int style) {	
		super.createControls(parent, style);
		control = new Button(this, SWT.CHECK);
		control.setText(getLabel());
		control.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		control.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BooleanFieldEditor.this.fireStateChanged(VALUE, !getControl().getSelection(), getControl().getSelection());				
			}
		});
		control.pack();
		pack();		
	}
	
	public boolean isSelected(){
		return getControl().getSelection();
	}
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#setDefaultValue()
	 */
	@Override
	protected void setDefaultValue() throws GkException {	
		boolean wasSelected = getControl().getSelection();
		getControl().setSelection( getPreferenceStore().getDefaultBoolean(getPreferenceName()) );
		fireStateChanged(VALUE, wasSelected, getControl().getSelection());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		boolean wasSelected = getControl().getSelection();
		getControl().setSelection( getPreferenceStore().getBoolean(getPreferenceName()) );
		fireStateChanged(VALUE, wasSelected, getControl().getSelection());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {
		getPreferenceStore().setValue(getPreferenceName(), getControl().getSelection());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {		
		super.setLabel(label);
		getControl().setText(label);
	}
}
