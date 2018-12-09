/**
 * 
 */
package org.goko.tools.commandpanel.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.core.common.exception.GkException;

/**
 * Preference page for command panel
 * @author Psyko
 * @date 8 déc. 2018
 */
public class CommandPanelPreferencePage extends GkFieldEditorPreferencesPage {
	
	public CommandPanelPreferencePage() {
		setTitle("Command panel");		
		setPreferenceStore(CommandPanelPreference.getInstance());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {		
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite.widthHint = 200;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(1, false));
		Group grpAxis = new Group(composite, SWT.NONE);
		grpAxis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpAxis.setLayout(new GridLayout(1, false));
		grpAxis.setText("Axis");
		
		BooleanFieldEditor booleanFieldEditor = new BooleanFieldEditor(grpAxis, SWT.NONE);
		booleanFieldEditor.setLabel("Enable A axis controls");
		booleanFieldEditor.setPreferenceName(CommandPanelPreference.A_AXIS_ENABLED);
		
		BooleanFieldEditor booleanFieldEditor_2 = new BooleanFieldEditor(grpAxis, SWT.NONE);
		booleanFieldEditor_2.setLabel("Enable B axis controls");
		booleanFieldEditor_2.setPreferenceName(CommandPanelPreference.B_AXIS_ENABLED);
		
		BooleanFieldEditor booleanFieldEditor_1 = new BooleanFieldEditor(grpAxis, SWT.NONE);
		booleanFieldEditor_1.setLabel("Enable C axis controls");
		booleanFieldEditor_1.setPreferenceName(CommandPanelPreference.C_AXIS_ENABLED);
		
		addField(booleanFieldEditor);
		addField(booleanFieldEditor_2);
		addField(booleanFieldEditor_1);
		
		Label lblNewLabel = new Label(composite, SWT.WRAP);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		lblNewLabel.setText("To apply the changes, you need to close the Command Panel part and reopen it using the Windows > View menu");
	}
	
	

}
