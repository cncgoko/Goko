package org.goko.controller.g2core.configuration;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGBigDecimalSettingFieldEditor;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGComboFieldEditor;
import org.goko.core.common.exception.GkException;

public class G2CoreConfigurationAxisPage extends AbstractG2CoreConfigurationPage{
	private String groupIdentifier;
	private boolean rotaryAxis;
	
	public G2CoreConfigurationAxisPage(G2CoreConfiguration configuration, String title, String groupIdentifier) {
		super(configuration);
		this.groupIdentifier = groupIdentifier;
		this.rotaryAxis = StringUtils.equals(groupIdentifier, G2Core.Configuration.Groups.A_AXIS) || StringUtils.equals(groupIdentifier, G2Core.Configuration.Groups.B_AXIS) || StringUtils.equals(groupIdentifier, G2Core.Configuration.Groups.C_AXIS);
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
		velocityMaxFieldEditor.setPreferenceName(G2Core.Configuration.Axes.VELOCITY_MAXIMUM);
		new Label(grpMotion, SWT.NONE);
		
		TinyGBigDecimalSettingFieldEditor travelMinFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		travelMinFieldEditor.setLabelWidthInChar(10);
		travelMinFieldEditor.setEmptyStringAllowed(false);
		travelMinFieldEditor.setWidthInChars(6);
		travelMinFieldEditor.setLabel("Travel min.");
		travelMinFieldEditor.setGroupIdentifier(groupIdentifier);
		travelMinFieldEditor.setPreferenceName(G2Core.Configuration.Axes.TRAVEL_MINIMUM);
		
		TinyGBigDecimalSettingFieldEditor feedrateMaxFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		feedrateMaxFieldEditor.setEmptyStringAllowed(false);
		feedrateMaxFieldEditor.setWidthInChars(8);
		feedrateMaxFieldEditor.setLabelWidthInChar(14);
		feedrateMaxFieldEditor.setLabel("Feedrate max.");		
		feedrateMaxFieldEditor.setGroupIdentifier(groupIdentifier);
		feedrateMaxFieldEditor.setPreferenceName(G2Core.Configuration.Axes.FEEDRATE_MAXIMUM);
		
		new Label(grpMotion, SWT.NONE);
		TinyGBigDecimalSettingFieldEditor travelMaxFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		travelMaxFieldEditor.setLabelWidthInChar(10);
		travelMaxFieldEditor.setEmptyStringAllowed(false);
		travelMaxFieldEditor.setWidthInChars(6);
		travelMaxFieldEditor.setLabel("Travel max.");
		travelMaxFieldEditor.setGroupIdentifier(groupIdentifier);
		travelMaxFieldEditor.setPreferenceName(G2Core.Configuration.Axes.TRAVEL_MAXIMUM);
		
		TinyGBigDecimalSettingFieldEditor jerkMaxFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
		jerkMaxFieldEditor.setEmptyStringAllowed(false);
		jerkMaxFieldEditor.setWidthInChars(8);
		jerkMaxFieldEditor.setLabelWidthInChar(14);
		jerkMaxFieldEditor.setLabel("Jerk max.");
		jerkMaxFieldEditor.setGroupIdentifier(groupIdentifier);
		jerkMaxFieldEditor.setPreferenceName(G2Core.Configuration.Axes.JERK_MAXIMUM);
		
		new Label(grpMotion, SWT.NONE);
		if(rotaryAxis){
			TinyGBigDecimalSettingFieldEditor radiusValueFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpMotion, SWT.NONE);
			radiusValueFieldEditor.setEmptyStringAllowed(false);
			radiusValueFieldEditor.setWidthInChars(6);
			radiusValueFieldEditor.setLabelWidthInChar(10);
			radiusValueFieldEditor.setLabel("Radius value");
			radiusValueFieldEditor.setGroupIdentifier(groupIdentifier);
			radiusValueFieldEditor.setPreferenceName(G2Core.Configuration.Axes.RADIUS_SETTING);
			addField(radiusValueFieldEditor);
		}else{
			new Label(grpMotion, SWT.NONE);	
		}

		
		Group grpHoming = new Group(parent, SWT.NONE);
		grpHoming.setLayout(new GridLayout(1, false));
		grpHoming.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpHoming.setText("Homing");

		
		TinyGBigDecimalSettingFieldEditor jerkHomingFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		jerkHomingFieldEditor.setLabel("Jerk high");
		jerkHomingFieldEditor.setWidthInChars(11);
		jerkHomingFieldEditor.setLabelWidthInChar(15);
		jerkHomingFieldEditor.setGroupIdentifier(groupIdentifier);
		jerkHomingFieldEditor.setPreferenceName(G2Core.Configuration.Axes.JERK_HOMING);
		
		TinyGBigDecimalSettingFieldEditor homingInputFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		homingInputFieldEditor.setWidthInChars(11);
		homingInputFieldEditor.setLabel("Homing input");
		homingInputFieldEditor.setLabelWidthInChar(15);
		homingInputFieldEditor.setGroupIdentifier(groupIdentifier);
		homingInputFieldEditor.setPreferenceName(G2Core.Configuration.Axes.HOMING_INPUT);
		
		TinyGComboFieldEditor homingDirectionFieldEditor = new TinyGComboFieldEditor(grpHoming, SWT.READ_ONLY);
		homingDirectionFieldEditor.setLabelWidthInChar(15);
		homingDirectionFieldEditor.setLabel("Homing direction");
		homingDirectionFieldEditor.setGroupIdentifier(groupIdentifier);
		homingDirectionFieldEditor.setPreferenceName(G2Core.Configuration.Axes.HOMING_DIRECTION);
		homingDirectionFieldEditor.setEntry(new String[][]{ 
			{"Toward negative","0"},			
			{"Toward positive","1"}});
		
		TinyGBigDecimalSettingFieldEditor searchVelocityFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		searchVelocityFieldEditor.setEmptyStringAllowed(false);
		searchVelocityFieldEditor.setWidthInChars(11);
		searchVelocityFieldEditor.setLabelWidthInChar(15);
		searchVelocityFieldEditor.setLabel("Search velocity");
		searchVelocityFieldEditor.setGroupIdentifier(groupIdentifier);
		searchVelocityFieldEditor.setPreferenceName(G2Core.Configuration.Axes.SEARCH_VELOCITY);
		
		TinyGBigDecimalSettingFieldEditor latchVelocityFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		latchVelocityFieldEditor.setEmptyStringAllowed(false);
		latchVelocityFieldEditor.setWidthInChars(11);
		latchVelocityFieldEditor.setLabelWidthInChar(15);
		latchVelocityFieldEditor.setLabel("Latch velocity");
		latchVelocityFieldEditor.setGroupIdentifier(groupIdentifier);
		latchVelocityFieldEditor.setPreferenceName(G2Core.Configuration.Axes.LATCH_VELOCITY);
		
		TinyGBigDecimalSettingFieldEditor latchBackoffFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		latchBackoffFieldEditor.setEmptyStringAllowed(false);
		latchBackoffFieldEditor.setWidthInChars(11);
		latchBackoffFieldEditor.setLabelWidthInChar(15);
		latchBackoffFieldEditor.setLabel("Latch backoff");
		latchBackoffFieldEditor.setGroupIdentifier(groupIdentifier);
		latchBackoffFieldEditor.setPreferenceName(G2Core.Configuration.Axes.LATCH_BACKOFF);
		
		TinyGBigDecimalSettingFieldEditor zeroBackoffFieldEditor = new TinyGBigDecimalSettingFieldEditor(grpHoming, SWT.NONE);
		zeroBackoffFieldEditor.setEmptyStringAllowed(false);
		zeroBackoffFieldEditor.setWidthInChars(11);
		zeroBackoffFieldEditor.setLabelWidthInChar(15);
		zeroBackoffFieldEditor.setLabel("Zero backoff");
		zeroBackoffFieldEditor.setGroupIdentifier(groupIdentifier);
		zeroBackoffFieldEditor.setPreferenceName(G2Core.Configuration.Axes.ZERO_BACKOFF);
		
				
		addField(velocityMaxFieldEditor);		
		addField(feedrateMaxFieldEditor);
		addField(travelMinFieldEditor);
		addField(travelMaxFieldEditor);
		addField(jerkMaxFieldEditor);
		new Label(grpMotion, SWT.NONE);
		addField(homingDirectionFieldEditor);
		addField(homingInputFieldEditor);
		addField(jerkHomingFieldEditor);
		addField(searchVelocityFieldEditor);
		addField(latchVelocityFieldEditor);
		addField(latchBackoffFieldEditor);
		addField(zeroBackoffFieldEditor);
	}
}
