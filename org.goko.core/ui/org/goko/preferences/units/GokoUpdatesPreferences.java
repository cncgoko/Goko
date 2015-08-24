/**
 * 
 */
package org.goko.preferences.units;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.core.config.GokoPreference;

/**
 * @author PsyKo
 *
 */
public class GokoUpdatesPreferences extends GkFieldEditorPreferencesPage {
	
	public GokoUpdatesPreferences() {
		setDescription("");
		setTitle("Update");
		setPreferenceStore(GokoPreference.getInstance());
	}

	@Override
	protected void createPreferencePage(Composite parent) {
		
		BooleanFieldEditor booleanFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		booleanFieldEditor.setLabel("Automatically check for updates on startup");
		booleanFieldEditor.setPreferenceName(GokoPreference.KEY_CHECK_UPDATE_STARTUP);
		addField(booleanFieldEditor);
	}


}
