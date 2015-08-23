package org.goko.controller.tinyg.configuration;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.goko.controller.tinyg.controller.configuration.TinyGAxisSettings;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

public class TinyGConfigurationAxisPage extends AbstractTinyGConfigurationPage{
	private String groupIdentifier;
	private boolean rotaryAxis;
	
	public TinyGConfigurationAxisPage(TinyGConfiguration configuration, String title, String groupIdentifier) {
		super(configuration);
		this.groupIdentifier = groupIdentifier;
		this.rotaryAxis = StringUtils.equals(groupIdentifier, TinyGConfiguration.A_AXIS_SETTINGS) || StringUtils.equals(groupIdentifier, TinyGConfiguration.B_AXIS_SETTINGS) || StringUtils.equals(groupIdentifier, TinyGConfiguration.C_AXIS_SETTINGS);
		setTitle(title);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
		Group grpMotion = new Group(parent, SWT.NONE);
		grpMotion.setText("Motion");
		grpMotion.setLayout(new GridLayout(3, false));
		grpMotion.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		TinyGBigDecimalSettingFieldEditor velocityMaxFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		velocityMaxFieldEditor.setEmptyStringAllowed(false);
		velocityMaxFieldEditor.setWidthInChars(8);
		velocityMaxFieldEditor.setLabelWidthInChar(14);
		velocityMaxFieldEditor.setLabel("Velocity max.");
		velocityMaxFieldEditor.setGroupIdentifier(groupIdentifier);
		velocityMaxFieldEditor.setPreferenceName(TinyGAxisSettings.VELOCITY_MAXIMUM);
		new Label(grpMotion, SWT.NONE);
		
		TinyGBigDecimalSettingFieldEditor travelMinFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		travelMinFieldEditor.setLabelWidthInChar(10);
		travelMinFieldEditor.setEmptyStringAllowed(false);
		travelMinFieldEditor.setWidthInChars(6);
		travelMinFieldEditor.setLabel("Travel min.");
		travelMinFieldEditor.setGroupIdentifier(groupIdentifier);
		travelMinFieldEditor.setPreferenceName(TinyGAxisSettings.TRAVEL_MINIMUM);
		
		TinyGBigDecimalSettingFieldEditor feedrateMaxFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		feedrateMaxFieldEditor.setEmptyStringAllowed(false);
		feedrateMaxFieldEditor.setWidthInChars(8);
		feedrateMaxFieldEditor.setLabelWidthInChar(14);
		feedrateMaxFieldEditor.setLabel("Feedrate max.");		
		feedrateMaxFieldEditor.setGroupIdentifier(groupIdentifier);
		feedrateMaxFieldEditor.setPreferenceName(TinyGAxisSettings.FEEDRATE_MAXIMUM);
		
		new Label(grpMotion, SWT.NONE);
		TinyGBigDecimalSettingFieldEditor travelMaxFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		travelMaxFieldEditor.setLabelWidthInChar(10);
		travelMaxFieldEditor.setEmptyStringAllowed(false);
		travelMaxFieldEditor.setWidthInChars(6);
		travelMaxFieldEditor.setLabel("Travel max.");
		travelMaxFieldEditor.setGroupIdentifier(groupIdentifier);
		travelMaxFieldEditor.setPreferenceName(TinyGAxisSettings.TRAVEL_MAXIMUM);
		
		TinyGBigDecimalSettingFieldEditor jerkMaxFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		jerkMaxFieldEditor.setEmptyStringAllowed(false);
		jerkMaxFieldEditor.setWidthInChars(8);
		jerkMaxFieldEditor.setLabelWidthInChar(14);
		jerkMaxFieldEditor.setLabel("Jerk max.");
		jerkMaxFieldEditor.setGroupIdentifier(groupIdentifier);
		jerkMaxFieldEditor.setPreferenceName(TinyGAxisSettings.JERK_MAXIMUM);
		
		new Label(grpMotion, SWT.NONE);
		if(rotaryAxis){
			TinyGBigDecimalSettingFieldEditor radiusValueFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
			radiusValueFieldEditor.setEmptyStringAllowed(false);
			radiusValueFieldEditor.setWidthInChars(6);
			radiusValueFieldEditor.setLabelWidthInChar(10);
			radiusValueFieldEditor.setLabel("Radius value");
			radiusValueFieldEditor.setGroupIdentifier(groupIdentifier);
			radiusValueFieldEditor.setPreferenceName(TinyGAxisSettings.RADIUS_SETTING);
			addField(radiusValueFieldEditor);
		}else{
			new Label(grpMotion, SWT.NONE);	
		}
		TinyGBigDecimalSettingFieldEditor junctionDeviationFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		junctionDeviationFieldEditor.setEmptyStringAllowed(false);
		junctionDeviationFieldEditor.setWidthInChars(8);
		junctionDeviationFieldEditor.setLabelWidthInChar(14);
		junctionDeviationFieldEditor.setLabel("Junction deviation");
		junctionDeviationFieldEditor.setGroupIdentifier(groupIdentifier);
		junctionDeviationFieldEditor.setPreferenceName(TinyGAxisSettings.JUNCTION_DEVIATION);
		
		new Label(grpMotion, SWT.NONE);
		new Label(grpMotion, SWT.NONE);
		
		Group grpHoming = new Group(parent, SWT.NONE);
		grpHoming.setLayout(new GridLayout(1, false));
		grpHoming.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpHoming.setText("Homing");
		
		TinyGComboFieldEditor minSwitchModeFieldEditor = new TinyGComboFieldEditor(grpHoming, SWT.READ_ONLY);
		minSwitchModeFieldEditor.setLabelWidthInChar(15);
		minSwitchModeFieldEditor.setLabel("Min. switch mode");
		minSwitchModeFieldEditor.setGroupIdentifier(groupIdentifier);
		minSwitchModeFieldEditor.setPreferenceName(TinyGAxisSettings.MINIMUM_SWITCH_MODE);
		
		TinyGComboFieldEditor maxSwitchModeFieldEditor = new TinyGComboFieldEditor(grpHoming, SWT.READ_ONLY);
		maxSwitchModeFieldEditor.setLabelWidthInChar(15);
		maxSwitchModeFieldEditor.setLabel("Max. switch mode");
		maxSwitchModeFieldEditor.setGroupIdentifier(groupIdentifier);
		maxSwitchModeFieldEditor.setPreferenceName(TinyGAxisSettings.MAXIMUM_SWITCH_MODE);
		
		TinyGBigDecimalSettingFieldEditor jerkHomingFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		jerkHomingFieldEditor.setLabel("Jerk homing");
		jerkHomingFieldEditor.setWidthInChars(11);
		jerkHomingFieldEditor.setLabelWidthInChar(15);
		jerkHomingFieldEditor.setGroupIdentifier(groupIdentifier);
		jerkHomingFieldEditor.setPreferenceName(TinyGAxisSettings.JERK_HOMING);
		
		TinyGBigDecimalSettingFieldEditor searchVelocityFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		searchVelocityFieldEditor.setEmptyStringAllowed(false);
		searchVelocityFieldEditor.setWidthInChars(11);
		searchVelocityFieldEditor.setLabelWidthInChar(15);
		searchVelocityFieldEditor.setLabel("Search velocity");
		searchVelocityFieldEditor.setGroupIdentifier(groupIdentifier);
		searchVelocityFieldEditor.setPreferenceName(TinyGAxisSettings.SEARCH_VELOCITY);
		
		TinyGBigDecimalSettingFieldEditor latchVelocityFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		latchVelocityFieldEditor.setEmptyStringAllowed(false);
		latchVelocityFieldEditor.setWidthInChars(11);
		latchVelocityFieldEditor.setLabelWidthInChar(15);
		latchVelocityFieldEditor.setLabel("Latch velocity");
		latchVelocityFieldEditor.setGroupIdentifier(groupIdentifier);
		latchVelocityFieldEditor.setPreferenceName(TinyGAxisSettings.LATCH_VELOCITY);
		
		TinyGBigDecimalSettingFieldEditor latchBackoffFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		latchBackoffFieldEditor.setEmptyStringAllowed(false);
		latchBackoffFieldEditor.setWidthInChars(11);
		latchBackoffFieldEditor.setLabelWidthInChar(15);
		latchBackoffFieldEditor.setLabel("Latch backoff");
		latchBackoffFieldEditor.setGroupIdentifier(groupIdentifier);
		latchBackoffFieldEditor.setPreferenceName(TinyGAxisSettings.LATCH_BACKOFF);
		
		TinyGBigDecimalSettingFieldEditor zeroBackoffFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		zeroBackoffFieldEditor.setEmptyStringAllowed(false);
		zeroBackoffFieldEditor.setWidthInChars(11);
		zeroBackoffFieldEditor.setLabelWidthInChar(15);
		zeroBackoffFieldEditor.setLabel("Zero backoff");
		zeroBackoffFieldEditor.setGroupIdentifier(groupIdentifier);
		zeroBackoffFieldEditor.setPreferenceName(TinyGAxisSettings.ZERO_BACKOFF);
		
		{
			String[][] values = new String[][]{
					{"Disabled","0"},
					{"Homing only","1"},
					{"Limit only","2"},
					{"Homing and limit","3"}
			};
			minSwitchModeFieldEditor.setEntry(values);
			maxSwitchModeFieldEditor.setEntry(values);
		}
		
		addField(velocityMaxFieldEditor);		
		addField(feedrateMaxFieldEditor);
		addField(travelMinFieldEditor);
		addField(travelMaxFieldEditor);
		addField(jerkMaxFieldEditor);
		addField(junctionDeviationFieldEditor);
		addField(minSwitchModeFieldEditor);
		addField(maxSwitchModeFieldEditor);
		addField(jerkHomingFieldEditor);
		addField(searchVelocityFieldEditor);
		addField(latchVelocityFieldEditor);
		addField(latchBackoffFieldEditor);
		addField(zeroBackoffFieldEditor);
	}
}
