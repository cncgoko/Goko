package org.goko.tools.shuttlxpress.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.quantity.LengthFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.quantity.SpeedFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;

/**
 * Shuttle XPress integration preferences page
 * @author Psyko
 * @date 25 avr. 2016
 */
public class ShuttleXPressPreferencesPage extends GkFieldEditorPreferencesPage{
	
	/**
	 * Constructor
	 */
	public ShuttleXPressPreferencesPage() {
		setDescription("Configure Shuttle Xpress integration");
		setTitle("Shuttle Xpress");
		setPreferenceStore(ShuttleXPressPreferences.getInstance());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.marginTop = 10;
		
		Group grpRapidJog = new Group(parent, SWT.NONE);		
		grpRapidJog.setLayout(new GridLayout(1, false));
		grpRapidJog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpRapidJog.setText("Rapid jog");
		
		SpeedFieldEditor speedFieldEditor_1 = new SpeedFieldEditor(grpRapidJog, SWT.NONE);
		speedFieldEditor_1.setLabelWidthInChar(15);
		speedFieldEditor_1.setLabel("Minimum feedrate:");
		speedFieldEditor_1.setWidthInChars(8);
		speedFieldEditor_1.setPreferenceName(ShuttleXPressPreferences.RAPID_JOG_MIN_SPEED);
		speedFieldEditor_1.setUnit(GokoPreference.getInstance().getSpeedUnit());
		
		SpeedFieldEditor speedFieldEditor_2 = new SpeedFieldEditor(grpRapidJog, SWT.NONE);
		speedFieldEditor_2.setWidthInChars(8);
		speedFieldEditor_2.setLabelWidthInChar(15);
		speedFieldEditor_2.setLabel("Maximum feedrate:");
		speedFieldEditor_2.setPreferenceName(ShuttleXPressPreferences.RAPID_JOG_MAX_SPEED);
		speedFieldEditor_2.setUnit(GokoPreference.getInstance().getSpeedUnit());
		
		Group composite = new Group(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setText("Precise jog");
		composite.setLayout(new GridLayout(1, false));
		
		LengthFieldEditor uiLengthFieldEditor = new LengthFieldEditor(composite, SWT.NONE);
		uiLengthFieldEditor.setWidthInChars(8);
		uiLengthFieldEditor.setLabel("Step :");
		uiLengthFieldEditor.setLabelWidthInChar(8);
		uiLengthFieldEditor.setPreferenceName(ShuttleXPressPreferences.PRECISE_JOG_STEP);
		uiLengthFieldEditor.setUnit(GokoPreference.getInstance().getLengthUnit());
		
		SpeedFieldEditor speedFieldEditor = new SpeedFieldEditor(composite, SWT.NONE);
		speedFieldEditor.setWidthInChars(8);
		speedFieldEditor.setLabelWidthInChar(8);
		speedFieldEditor.setLabel("Feedrate :");
		speedFieldEditor.setPreferenceName(ShuttleXPressPreferences.PRECISE_JOG_SPEED);
		speedFieldEditor.setUnit(GokoPreference.getInstance().getSpeedUnit());
		
		
		addField(speedFieldEditor_1);
		addField(speedFieldEditor_2);
		addField(uiLengthFieldEditor);
		addField(speedFieldEditor);
	}
}
