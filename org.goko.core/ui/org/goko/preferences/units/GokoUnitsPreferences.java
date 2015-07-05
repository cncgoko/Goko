package org.goko.preferences.units;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.ComboFieldEditor;
import org.goko.common.preferences.fieldeditor.IntegerFieldEditor;
import org.goko.core.config.EnumGokoUnit;
import org.goko.core.config.GokoPreference;

/**
 * Configuration of the units used in Goko
 * 
 * @author PsyKo
 *
 */
public class GokoUnitsPreferences extends GkFieldEditorPreferencesPage {
	
	public GokoUnitsPreferences() {
		setDescription("Configure the units used in the application");
		setTitle("Units");
		setPreferenceStore(GokoPreference.getInstance());
	}

	@Override
	protected void createPreferencePage(Composite parent) {		
		ComboFieldEditor comboFieldEditor = new ComboFieldEditor(parent, SWT.READ_ONLY);
		comboFieldEditor.setLabelWidthInChar(7);
		comboFieldEditor.setLabel("Length");
		String[][] values = new String[EnumGokoUnit.values().length][2];
		
		int i = 0;
		for (EnumGokoUnit gUnit : EnumGokoUnit.values()) {			
			values[i][0] = gUnit.getCode();					
			values[i][1] = gUnit.getCode();					
			i++;
		};
		comboFieldEditor.setEntry(values);
		comboFieldEditor.setPreferenceName(GokoPreference.KEY_DISTANCE_UNIT);
		
		IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(parent, SWT.NONE);
		integerFieldEditor.setWidthInChars(5);
		integerFieldEditor.setLabelWidthInChar(7);
		integerFieldEditor.setLabel("Digits");
		integerFieldEditor.setPreferenceName(GokoPreference.KEY_DISTANCE_DIGIT_COUNT);
		
		addField(comboFieldEditor);
		addField(integerFieldEditor);
		

	}

}
