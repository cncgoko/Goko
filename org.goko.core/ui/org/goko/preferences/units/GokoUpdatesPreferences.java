/**
 * 
 */
package org.goko.preferences.units;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.core.config.EnumUpdateCheckFrequency;
import org.goko.core.config.GokoPreference;

/**
 * @author PsyKo
 *
 */
public class GokoUpdatesPreferences extends GkFieldEditorPreferencesPage {
	
	public GokoUpdatesPreferences() {		
		setTitle("Update");
		setPreferenceStore(GokoPreference.getInstance());
	}

	@Override
	protected void createPreferencePage(Composite parent) {
		
		BooleanFieldEditor automaticUpdateFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		automaticUpdateFieldEditor.setLabel("Automatically check for updates on startup");
		automaticUpdateFieldEditor.setPreferenceName(GokoPreference.KEY_CHECK_UPDATE);
		addField(automaticUpdateFieldEditor);
		
		ComboFieldEditor checkFrequencyFieldEditor = new ComboFieldEditor(parent, SWT.READ_ONLY);
		checkFrequencyFieldEditor.setLabel("Check for updates ");
		checkFrequencyFieldEditor.setPreferenceName(GokoPreference.KEY_CHECK_UPDATE_FREQUENCY);
		initComboContent(checkFrequencyFieldEditor);		
		addField(checkFrequencyFieldEditor);		
		
	}

	private void initComboContent(ComboFieldEditor checkFrequencyFieldEditor){
		String[][] entry = new String[EnumUpdateCheckFrequency.values().length][];
		int i = 0;
		for (EnumUpdateCheckFrequency frequency : EnumUpdateCheckFrequency.values()) {
			entry[i] = new String[]{frequency.getLabel(), frequency.getCode()};
			i = i + 1;
		}
		checkFrequencyFieldEditor.setInput(entry);		
	}

}
