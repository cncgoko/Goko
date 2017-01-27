package org.goko.controller.tinyg.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGComboFieldEditor;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

public class TinyGConfigurationMotorMappingPage extends AbstractTinyGConfigurationPage{
	
	public TinyGConfigurationMotorMappingPage(TinyGConfiguration configuration) {
		super(configuration);
		setTitle("Motors");		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
		Group grpMapping = new Group(parent, SWT.NONE);
		grpMapping.setLayout(new GridLayout(1, false));
		grpMapping.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpMapping.setText("Mapping");
		
		TinyGComboFieldEditor motor1MappingFieldEditor = new TinyGComboFieldEditor(grpMapping, SWT.READ_ONLY);
		motor1MappingFieldEditor.setLabel("Motor 1");
		motor1MappingFieldEditor.setGroupIdentifier(TinyGConfiguration.MOTOR_1_SETTINGS);
		motor1MappingFieldEditor.setPreferenceName(TinyGConfiguration.MOTOR_MAPPING);
		
		TinyGComboFieldEditor motor2MappingFieldEditor = new TinyGComboFieldEditor(grpMapping, SWT.READ_ONLY);
		motor2MappingFieldEditor.setLabel("Motor 2");
		motor2MappingFieldEditor.setGroupIdentifier(TinyGConfiguration.MOTOR_2_SETTINGS);
		motor2MappingFieldEditor.setPreferenceName(TinyGConfiguration.MOTOR_MAPPING);
		
		TinyGComboFieldEditor motor3MappingFieldEditor = new TinyGComboFieldEditor(grpMapping, SWT.READ_ONLY);
		motor3MappingFieldEditor.setLabel("Motor 3");
		motor3MappingFieldEditor.setGroupIdentifier(TinyGConfiguration.MOTOR_3_SETTINGS);
		motor3MappingFieldEditor.setPreferenceName(TinyGConfiguration.MOTOR_MAPPING);
		
		TinyGComboFieldEditor motor4MappingFieldEditor = new TinyGComboFieldEditor(grpMapping, SWT.READ_ONLY);
		motor4MappingFieldEditor.setLabel("Motor 4");
		motor4MappingFieldEditor.setGroupIdentifier(TinyGConfiguration.MOTOR_4_SETTINGS);
		motor4MappingFieldEditor.setPreferenceName(TinyGConfiguration.MOTOR_MAPPING);
		
		String[][] values = new String[][]{ // 0=off, 1=filtered, 2=verbose
				{"X Axis","0"},				
				{"Y Axis","1"},				
				{"Z Axis","2"},				
				{"A Axis","3"},				
				{"B Axis","4"},				
				{"C Axis","5"}				
			};
		motor1MappingFieldEditor.setEntry(values);
		motor2MappingFieldEditor.setEntry(values);
		motor3MappingFieldEditor.setEntry(values);
		motor4MappingFieldEditor.setEntry(values);
		
		addField(motor1MappingFieldEditor);
		addField(motor2MappingFieldEditor);
		addField(motor3MappingFieldEditor);
		addField(motor4MappingFieldEditor);
	}
}
