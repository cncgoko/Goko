package org.goko.common.preferences.fieldeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;

/**
 * Field editor using a combo control
 * @author PsyKo
 */
/**
 * @author PsyKo
 *
 */
/**
 * @author PsyKo
 *
 * @param <T>
 */
public class ComboFieldEditor extends LabeledFieldEditor<Combo> {	
	/**
	 * The value (not the name) of the currently selected item in the Combo widget.
	 */
	private String fValue;
	
	/**
	 * The names (labels) and underlying values to populate the combo widget.  These should be
	 * arranged as: { {name1, value1}, {name2, value2}, ...}
	 */
	private String[][] fEntryNamesAndValues;


	public ComboFieldEditor(Composite parent, int style) {
		super(parent, style);		
		createControls(parent, style);
	}

	public void setEntry(String[][] entry){
		this.fEntryNamesAndValues = entry; 	
		control.removeAll();
		for (int i = 0; i < fEntryNamesAndValues.length; i++) {
			control.add(fEntryNamesAndValues[i][0], i);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#createControls(org.eclipse.swt.widgets.Composite, int)
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
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		updateComboForValue(getPreferenceStore().getString(getPreferenceName()));
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#setDefaultValue()
	 */
	@Override
	protected void setDefaultValue() throws GkException {
		updateComboForValue(getPreferenceStore().getDefaultString(getPreferenceName()));
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {
		if (fValue == null) {
			getPreferenceStore().setToDefault(getPreferenceName());
			return;
		}
		getPreferenceStore().setValue(getPreferenceName(), fValue);
	}	
	
	/*
	 * Given the name (label) of an entry, return the corresponding value.
	 */
	private String getValueForName(String name) {
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
	private void updateComboForValue(String value) {
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

}
