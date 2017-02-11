package org.goko.controller.g2core.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGBigDecimalSettingFieldEditor;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGComboFieldEditor;
import org.goko.core.common.exception.GkException;

public class G2CoreConfigurationMotorPage extends AbstractG2CoreConfigurationPage{
	private String groupIdentifier;
	
	public G2CoreConfigurationMotorPage(G2CoreConfiguration configuration, String title, String groupIdentifier) {
		super(configuration);
		this.groupIdentifier = groupIdentifier;
		setTitle(title);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
		TinyGBigDecimalSettingFieldEditor stepAngleFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		stepAngleFieldEditor.setWidthInChars(11);
		stepAngleFieldEditor.setLabelWidthInChar(16);
		stepAngleFieldEditor.setLabel("Step angle");
		stepAngleFieldEditor.setGroupIdentifier(groupIdentifier);
		stepAngleFieldEditor.setPreferenceName(G2Core.Configuration.Motors.STEP_ANGLE);
		
		
		TinyGBigDecimalSettingFieldEditor travelPerRevolutionFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		travelPerRevolutionFieldEditor.setWidthInChars(11);
		travelPerRevolutionFieldEditor.setLabelWidthInChar(16);
		travelPerRevolutionFieldEditor.setLabel("Travel per revolution");
		travelPerRevolutionFieldEditor.setGroupIdentifier(groupIdentifier);
		travelPerRevolutionFieldEditor.setPreferenceName(G2Core.Configuration.Motors.TRAVEL_PER_REVOLUTION);
		
		TinyGComboFieldEditor microstepsFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		microstepsFieldEditor.setLabelWidthInChar(16);
		microstepsFieldEditor.setLabel("Microsteps");
		microstepsFieldEditor.setGroupIdentifier(groupIdentifier);
		microstepsFieldEditor.setPreferenceName(G2Core.Configuration.Motors.MICROSTEPS);
		
		{
			String[][] microsteps = new String[][]{ 
					{"No microsteps (1)","1"},				
					{"Half stepping (2)","2"},				
					{"Quarter stepping (4)","4"},			
					{"Eighth stepping (8)","8"},
					{"Sixteenth stepping (16)","16"},
					{"Thirty second stepping (32)","32"}
				};     
			microstepsFieldEditor.setEntry(microsteps);
		}
		TinyGComboFieldEditor polarityFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		polarityFieldEditor.setLabelWidthInChar(16);
		polarityFieldEditor.setLabel("Polarity");
		polarityFieldEditor.setGroupIdentifier(groupIdentifier);
		polarityFieldEditor.setPreferenceName(G2Core.Configuration.Motors.POLARITY);
		
		{
			String[][] polarity = new String[][]{ // 0=off, 1=filtered, 2=verbose
					{"Normal","0"},							
					{"Reverse","1"}	
				};     
			polarityFieldEditor.setEntry(polarity);
		}
		
		TinyGComboFieldEditor powerManagementFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		powerManagementFieldEditor.setLabelWidthInChar(16);
		powerManagementFieldEditor.setLabel("Power management");
		powerManagementFieldEditor.setGroupIdentifier(groupIdentifier);
		powerManagementFieldEditor.setPreferenceName(G2Core.Configuration.Motors.POWER_MODE);
		
		{
			String[][] powerMode = new String[][]{ // 0=off, 1=filtered, 2=verbose
					{"Disabled","0"},							
					{"Always powered","1"},	
					{"During machining cycle","2"},
					{"When moving","3"}	
				};     
			powerManagementFieldEditor.setEntry(powerMode);					
		}
				
		TinyGBigDecimalSettingFieldEditor powerLevelFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.READ_ONLY);
		powerLevelFieldEditor.setWidthInChars(11);
		powerLevelFieldEditor.setLabel("Power level");
		powerLevelFieldEditor.setLabelWidthInChar(16);
		powerLevelFieldEditor.setGroupIdentifier(groupIdentifier);
		powerLevelFieldEditor.setPreferenceName(G2Core.Configuration.Motors.POWER_LEVEL);
		
		addField(stepAngleFieldEditor);
		addField(travelPerRevolutionFieldEditor);
		addField(microstepsFieldEditor);
		addField(polarityFieldEditor);
		addField(powerManagementFieldEditor);
		addField(powerLevelFieldEditor);
	}
}
