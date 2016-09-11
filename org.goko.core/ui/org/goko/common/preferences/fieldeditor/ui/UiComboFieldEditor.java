package org.goko.common.preferences.fieldeditor.ui;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.exception.GkException;

/**
 * Field editor using a combo control
 * @author PsyKo
 */
public class UiComboFieldEditor extends UiLabeledFieldEditor<Combo> {	
	/**
	 * The value (not the name) of the currently selected item in the Combo widget.
	 */
	private String fValue;
	
	/**
	 * The names (labels) and underlying values to populate the combo widget.  These should be
	 * arranged as: { {name1, value1}, {name2, value2}, ...}
	 */
	private String[][] fEntryNamesAndValues;


	public UiComboFieldEditor(Composite parent, int style) {
		super(parent, style);		
		createControls(parent, style);
	}
	
	public void setInput(String[][] entry){
		setEntry(entry);
	}
	/**
	 * @param entry The names (labels) and underlying values to populate the combo widget.  These should be arranged as: { {name1, value1}, {name2, value2}, ...}
	 */	
	public void setEntry(String[][] entry){
		this.fEntryNamesAndValues = entry; 	
		control.removeAll();
		for (int i = 0; i < fEntryNamesAndValues.length; i++) {
			control.add(fEntryNamesAndValues[i][0], i);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#createControls(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void createControls(Composite parent, int style) {
		super.createControls(parent, style);		
		control = new Combo(this, style & SWT.READ_ONLY);    	
		control.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				String oldValue = fValue;
				String name = control.getText();
				fValue = getValueForName(name);				
				fireValueChanged(VALUE, oldValue, fValue);					
			}
		});
    	control.pack();
    	this.pack();
	}
		
	/*
	 * Given the name (label) of an entry, return the corresponding value.
	 */
	protected String getValueForName(String name) {
		for (int i = 0; i < fEntryNamesAndValues.length; i++) {
			String[] entry = fEntryNamesAndValues[i];
			if (name.equals(entry[0])) {
				return entry[1];
			}
		}
		return fEntryNamesAndValues[0][0];
	}
	
	/*
	 * Set the name in the combo widget to match the specified value.
	 */
	protected void updateComboForValue(String value) {
		fValue = value;
		for (int i = 0; i < fEntryNamesAndValues.length; i++) {
			if (value.equals(fEntryNamesAndValues[i][1])) {
				control.setText(fEntryNamesAndValues[i][0]);
				return;
			}
		}
		if (fEntryNamesAndValues.length > 0) {
			fValue = fEntryNamesAndValues[0][1];
			control.setText(fEntryNamesAndValues[0][0]);
		}
	}

	public String getValue() {
		return fValue;
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		return ValidationStatus.OK_STATUS;		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiFieldEditor#getFieldEditorBinding(org.eclipse.core.databinding.DataBindingContext, org.goko.common.bindings.AbstractModelObject)
	 */
	@Override
	protected Binding getFieldEditorBinding(DataBindingContext bindingContext, AbstractModelObject modelObject) throws GkException {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not implemented");
	}
}
