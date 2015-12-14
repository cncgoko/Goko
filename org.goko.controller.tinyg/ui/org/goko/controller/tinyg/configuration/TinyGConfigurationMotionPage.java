package org.goko.controller.tinyg.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

public class TinyGConfigurationMotionPage extends AbstractTinyGConfigurationPage{
	
	public TinyGConfigurationMotionPage(TinyGConfiguration configuration) {
		super(configuration);
		setTitle("Motion");		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
		TinyGBigDecimalSettingFieldEditor junctionAccelerationFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		junctionAccelerationFieldEditor.setLabelWidthInChar(17);
		junctionAccelerationFieldEditor.setWidthInChars(12);
		junctionAccelerationFieldEditor.setLabel("Junction acceleration");
		junctionAccelerationFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		junctionAccelerationFieldEditor.setPreferenceName(TinyGConfiguration.JUNCTION_ACCELERATION);
		
		TinyGBigDecimalSettingFieldEditor chordalToleranceFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		chordalToleranceFieldEditor.setWidthInChars(8);
		chordalToleranceFieldEditor.setLabelWidthInChar(17);
		chordalToleranceFieldEditor.setLabel("Chordal tolerance");
		chordalToleranceFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		chordalToleranceFieldEditor.setPreferenceName(TinyGConfiguration.CHORDAL_TOLERANCE);
		
		TinyGComboFieldEditor switchTypeFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		switchTypeFieldEditor.setLabelWidthInChar(17);
		switchTypeFieldEditor.setLabel("Switch type");
		switchTypeFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		switchTypeFieldEditor.setPreferenceName(TinyGConfiguration.SWITCH_TYPE);
		{
			String[][] values = new String[][]{
					{"Normally open","0"},
					{"Normally closed","1"}
			};
			switchTypeFieldEditor.setEntry(values);
		}
		TinyGBigDecimalSettingFieldEditor motorTimeoutFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		motorTimeoutFieldEditor.setWidthInChars(8);
		motorTimeoutFieldEditor.setLabelWidthInChar(17);
		motorTimeoutFieldEditor.setLabel("Motor disable timeout");
		motorTimeoutFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		motorTimeoutFieldEditor.setPreferenceName(TinyGConfiguration.MOTOR_DISABLE_TIMEOUT);
		
		addField(junctionAccelerationFieldEditor);
		addField(chordalToleranceFieldEditor);
		addField(switchTypeFieldEditor);
		addField(motorTimeoutFieldEditor);
	}
}
