/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.controller.grbl.v11.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfigurationPage;
import org.goko.controller.grbl.commons.configuration.editors.GrblBigDecimalSettingFieldEditor;
import org.goko.controller.grbl.commons.configuration.editors.GrblBitMaskFieldEditor;
import org.goko.controller.grbl.commons.configuration.editors.GrblBooleanFieldEditor;
import org.goko.controller.grbl.commons.configuration.editors.GrblIntegerSettingFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 *
 * @author PsyKo
 *
 */
public class GrblConfigurationDialog extends AbstractGrblConfigurationPage<GrblConfiguration> {
	private static final GkLog LOG = GkLog.getLogger(GrblConfigurationDialog.class);
	
	public GrblConfigurationDialog(GrblConfiguration configuration) {
		super(configuration);
		setTitle("Grbl configuration");		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
	
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		Group grpAxisConfiguration = new Group(composite, SWT.NONE);
		grpAxisConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpAxisConfiguration.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridLayout gl_grpAxisConfiguration = new GridLayout(1, false);
		gl_grpAxisConfiguration.marginWidth = 0;
		gl_grpAxisConfiguration.marginHeight = 0;
		grpAxisConfiguration.setLayout(gl_grpAxisConfiguration);
		grpAxisConfiguration.setText("Axis configuration");
		
		Composite composite_1 = new Composite(grpAxisConfiguration, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(4, true);
		gl_composite_1.marginTop = 3;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("step/mm");
		
		Label lblNewLabel_1 = new Label(composite_1, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("max rate");
		
		Label lblNewLabel_2 = new Label(composite_1, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("accel");
		
		Label lblNewLabel_3 = new Label(composite_1, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("max travel");
		
		Composite composite_10 = new Composite(grpAxisConfiguration, SWT.NONE);
		composite_10.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_10 = new GridLayout(4, true);
		gl_composite_10.verticalSpacing = 2;
		gl_composite_10.marginHeight = 0;
		composite_10.setLayout(gl_composite_10);
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> stepXField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		stepXField.setWidthInChars(10);
		stepXField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		stepXField.setLabel("$100  x");
		stepXField.setPreferenceName("$100");
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> maxRateXField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		maxRateXField.setPreferenceName("$110");
		maxRateXField.setWidthInChars(10);
		maxRateXField.setLabel("$110  x");
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> accelXField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		accelXField.setPreferenceName("$120");
		accelXField.setLabel("$120  x");
		accelXField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> maxTravelXField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		maxTravelXField.setPreferenceName("$130");
		maxTravelXField.setLabel("$130  x");
		maxTravelXField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> stepYField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		stepYField.setPreferenceName("$101");
		stepYField.setWidthInChars(10);
		stepYField.setLabel("$101  y");
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> maxRateYField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		maxRateYField.setPreferenceName("$111");
		maxRateYField.setWidthInChars(10);
		maxRateYField.setLabel("$111  y");
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> accelYField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		accelYField.setPreferenceName("$121");
		accelYField.setLabel("$121  y");
		accelYField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> maxTravelYField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		maxTravelYField.setPreferenceName("$131");
		maxTravelYField.setLabel("$131  y");
		maxTravelYField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> stepZField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		stepZField.setPreferenceName("$102");
		stepZField.setWidthInChars(10);
		stepZField.setLabel("$102  z");
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> maxRateZField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		maxRateZField.setPreferenceName("$112");
		maxRateZField.setWidthInChars(10);
		maxRateZField.setLabel("$112  z");
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> accelZField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		accelZField.setPreferenceName("$122");
		accelZField.setLabel("$122  z");
		accelZField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> maxTravelZField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_10, SWT.NONE);
		maxTravelZField.setPreferenceName("$132");
		maxTravelZField.setLabel("$132  z");
		maxTravelZField.setWidthInChars(10);
		
		Composite composite_3 = new Composite(grpAxisConfiguration, SWT.NONE);
		composite_3.setLayout(new GridLayout(6, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblstepPort = new Label(composite_3, SWT.NONE);
		lblstepPort.setText("$2 (step port invert mask)");
		lblstepPort.setBounds(0, 0, 134, 15);
		
		GrblBitMaskFieldEditor<GrblConfiguration> stepPortInvertXField = new GrblBitMaskFieldEditor<GrblConfiguration>(composite_3, SWT.NONE);
		stepPortInvertXField.setPreferenceName("$2");
		stepPortInvertXField.setLabel("Invert X");
		new Label(composite_3, SWT.NONE);
		
		GrblBitMaskFieldEditor<GrblConfiguration> stepPortInvertYField = new GrblBitMaskFieldEditor<GrblConfiguration>(composite_3, SWT.NONE);
		stepPortInvertYField.setBitPosition(1);
		stepPortInvertYField.setPreferenceName("$2");
		stepPortInvertYField.setLabel("Invert Y");
		new Label(composite_3, SWT.NONE);
		
		GrblBitMaskFieldEditor<GrblConfiguration> stepPortInvertZField = new GrblBitMaskFieldEditor<GrblConfiguration>(composite_3, SWT.NONE);
		stepPortInvertZField.setPreferenceName("$2");
		stepPortInvertZField.setLabel("Invert Z");
		stepPortInvertZField.setBitPosition(2);
		
		Group grpGeneral = new Group(composite, SWT.NONE);
		grpGeneral.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpGeneral.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridLayout gl_grpGeneral = new GridLayout(1, false);
		gl_grpGeneral.marginHeight = 0;
		gl_grpGeneral.marginWidth = 0;
		grpGeneral.setLayout(gl_grpGeneral);
		grpGeneral.setText("General");
		
		Composite composite_5 = new Composite(grpGeneral, SWT.NONE);
		composite_5.setLayout(new GridLayout(3, false));
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		GrblIntegerSettingFieldEditor<GrblConfiguration> stepPulseField = new GrblIntegerSettingFieldEditor<GrblConfiguration>(composite_5, SWT.NONE);
		stepPulseField.setLabelWidthInChar(23);
		stepPulseField.setWidthInChars(8);
		stepPulseField.setPreferenceName("$0");
		stepPulseField.setLabel("$0 (step pulse, usec)");
		new Label(composite_5, SWT.NONE);
		
		GrblIntegerSettingFieldEditor<GrblConfiguration> stepIdleField = new GrblIntegerSettingFieldEditor<GrblConfiguration>(composite_5, SWT.NONE);
		stepIdleField.setLabelWidthInChar(19);
		stepIdleField.setWidthInChars(8);
		stepIdleField.setPreferenceName("$1");
		stepIdleField.setLabel("$1 (step idle delay, msec)");
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> junctionDeviationField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_5, SWT.NONE);
		junctionDeviationField.setLabelWidthInChar(23);
		junctionDeviationField.setWidthInChars(8);
		junctionDeviationField.setPreferenceName("$11");
		junctionDeviationField.setLabel("$11 (junction deviation, mm)");
		new Label(composite_5, SWT.NONE);
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> arcToleranceField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_5, SWT.NONE);
		arcToleranceField.setLabelWidthInChar(19);
		arcToleranceField.setWidthInChars(8);
		arcToleranceField.setPreferenceName("$12");
		arcToleranceField.setLabel("$12 (arc tolerance, mm)");
		
		GrblBooleanFieldEditor<GrblConfiguration> stepEnableInvertField = new GrblBooleanFieldEditor<GrblConfiguration>(composite_5, SWT.NONE);
		stepEnableInvertField.setLabel("$4 (step enable invert)");
		stepEnableInvertField.setPreferenceName("$4");
		new Label(composite_5, SWT.NONE);
		new Label(composite_5, SWT.NONE);
		
		GrblBooleanFieldEditor<GrblConfiguration> limitPinInvertField = new GrblBooleanFieldEditor<GrblConfiguration>(composite_5, SWT.NONE);
		limitPinInvertField.setPreferenceName("$5");
		limitPinInvertField.setLabel("$5 (limit pins invert)");
		new Label(composite_5, SWT.NONE);
		
		GrblBooleanFieldEditor<GrblConfiguration> reportInchesField = new GrblBooleanFieldEditor<GrblConfiguration>(composite_5, SWT.NONE);
		reportInchesField.setPreferenceName("$13");
		reportInchesField.setLabel("$13 (report inches)");
		
		GrblBooleanFieldEditor<GrblConfiguration> probePinInvertField = new GrblBooleanFieldEditor<GrblConfiguration>(composite_5, SWT.NONE);
		probePinInvertField.setPreferenceName("$6");
		probePinInvertField.setLabel("$6 (probe pin invert)");
		new Label(composite_5, SWT.NONE);
		new Label(composite_5, SWT.NONE);

		GrblBooleanFieldEditor<GrblConfiguration> laserModeField = new GrblBooleanFieldEditor<GrblConfiguration>(composite_5, SWT.NONE);
		laserModeField.setPreferenceName("$32");
		laserModeField.setLabel("$32 (laser mode)");
		new Label(composite_5, SWT.NONE);
		new Label(composite_5, SWT.NONE);
		
		Group grpSpindle = new Group(composite, SWT.NONE);
		grpSpindle.setLayout(new GridLayout(2, false));
		grpSpindle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpSpindle.setText("Spindle");
		
		GrblIntegerSettingFieldEditor<GrblConfiguration> minSpindleSpeedField = new GrblIntegerSettingFieldEditor<GrblConfiguration>(grpSpindle, SWT.NONE);
		minSpindleSpeedField.setWidthInChars(8);
		minSpindleSpeedField.setPreferenceName("$31");
		minSpindleSpeedField.setLabelWidthInChar(20);
		minSpindleSpeedField.setLabel("$31 (min spindle speed)");
		
		GrblIntegerSettingFieldEditor<GrblConfiguration> maxSpindleSpeedField = new GrblIntegerSettingFieldEditor<GrblConfiguration>(grpSpindle, SWT.NONE);
		maxSpindleSpeedField.setWidthInChars(8);
		maxSpindleSpeedField.setPreferenceName("$30");
		maxSpindleSpeedField.setLabelWidthInChar(20);
		maxSpindleSpeedField.setLabel("$30 (max spindle speed)");
		
		Group grpHoming = new Group(composite, SWT.NONE);
		grpHoming.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpHoming.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridLayout gl_grpHoming = new GridLayout(1, false);
		gl_grpHoming.marginWidth = 0;
		gl_grpHoming.marginHeight = 0;
		grpHoming.setLayout(gl_grpHoming);
		grpHoming.setText("Homing");
		
		Composite composite_6 = new Composite(grpHoming, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_6.setLayout(new GridLayout(5, false));
		
		GrblBooleanFieldEditor<GrblConfiguration> softLimitsField = new GrblBooleanFieldEditor<GrblConfiguration>(composite_6, SWT.NONE);
		softLimitsField.setLabel("$20 (soft limits)");
		softLimitsField.setPreferenceName("$20");
		new Label(composite_6, SWT.NONE);
		
		GrblBooleanFieldEditor<GrblConfiguration> hardLimitsField = new GrblBooleanFieldEditor<GrblConfiguration>(composite_6, SWT.NONE);
		hardLimitsField.setPreferenceName("$21");
		hardLimitsField.setLabel("$21 (hard limits)");
		new Label(composite_6, SWT.NONE);
		
		GrblBooleanFieldEditor<GrblConfiguration> homingCycleField = new GrblBooleanFieldEditor<GrblConfiguration>(composite_6, SWT.NONE);
		homingCycleField.setLabel("$22 (homing cycle)");
		homingCycleField.setPreferenceName("$22");
		
		Composite composite_7 = new Composite(grpHoming, SWT.NONE);
		GridLayout gl_composite_7 = new GridLayout(6, false);
		gl_composite_7.marginWidth = 0;
		composite_7.setLayout(gl_composite_7);
		composite_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblhomingDir = new Label(composite_7, SWT.NONE);
		lblhomingDir.setText("$23 (homing dir invert mask)");
		
		GrblBitMaskFieldEditor<GrblConfiguration> homingInvertXField = new GrblBitMaskFieldEditor<GrblConfiguration>(composite_7, SWT.NONE);
		homingInvertXField.setPreferenceName("$23");
		homingInvertXField.setLabel("Invert X");
		new Label(composite_7, SWT.NONE);
		
		GrblBitMaskFieldEditor<GrblConfiguration> homingInvertYField = new GrblBitMaskFieldEditor<GrblConfiguration>(composite_7, SWT.NONE);
		homingInvertYField.setBitPosition(1);
		homingInvertYField.setPreferenceName("$23");
		homingInvertYField.setLabel("Invert Y");
		new Label(composite_7, SWT.NONE);
		
		GrblBitMaskFieldEditor<GrblConfiguration> homingInvertZField = new GrblBitMaskFieldEditor<GrblConfiguration>(composite_7, SWT.NONE);
		homingInvertZField.setBitPosition(2);
		homingInvertZField.setPreferenceName("$23");
		homingInvertZField.setLabel("Invert Z");
		
		Composite composite_8 = new Composite(grpHoming, SWT.NONE);
		composite_8.setLayout(new GridLayout(3, false));
		composite_8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> homingFeedField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_8, SWT.NONE);
		homingFeedField.setWidthInChars(8);
		homingFeedField.setLabelWidthInChar(22);
		homingFeedField.setPreferenceName("$24");
		homingFeedField.setLabel("$24 (homing feed, mm/min)");
		new Label(composite_8, SWT.NONE);
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> homingDebounceField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_8, SWT.NONE);
		homingDebounceField.setWidthInChars(8);
		homingDebounceField.setLabelWidthInChar(24);
		homingDebounceField.setPreferenceName("$26");
		homingDebounceField.setLabel("$26 (homing debounce, msec)");
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> homingSeekField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_8, SWT.NONE);
		homingSeekField.setWidthInChars(8);
		homingSeekField.setLabelWidthInChar(22);
		homingSeekField.setPreferenceName("$25");
		homingSeekField.setLabel("$25 (homing seek, mm/min)");
		new Label(composite_8, SWT.NONE);
		
		GrblBigDecimalSettingFieldEditor<GrblConfiguration> homingPulloffField = new GrblBigDecimalSettingFieldEditor<GrblConfiguration>(composite_8, SWT.NONE);
		homingPulloffField.setWidthInChars(8);
		homingPulloffField.setLabelWidthInChar(24);
		homingPulloffField.setLabel("$27 (homing pull-off, mm)");
		homingPulloffField.setPreferenceName("$27");
		
		Label lbldirPort = new Label(composite_3, SWT.NONE);
		lbldirPort.setText("$3 (dir port invert mask)");
		
		GrblBitMaskFieldEditor<GrblConfiguration> directionPortInvertXField = new GrblBitMaskFieldEditor<GrblConfiguration>(composite_3, SWT.NONE);
		directionPortInvertXField.setPreferenceName("$2");
		directionPortInvertXField.setLabel("Invert X");
		new Label(composite_3, SWT.NONE);
		
		GrblBitMaskFieldEditor<GrblConfiguration> directionPortInvertYField = new GrblBitMaskFieldEditor<GrblConfiguration>(composite_3, SWT.NONE);
		directionPortInvertYField.setPreferenceName("$2");
		directionPortInvertYField.setLabel("Invert Y");
		directionPortInvertYField.setBitPosition(1);
		new Label(composite_3, SWT.NONE);
		
		GrblBitMaskFieldEditor<GrblConfiguration> directionPortInvertZField = new GrblBitMaskFieldEditor<GrblConfiguration>(composite_3, SWT.NONE);
		directionPortInvertZField.setPreferenceName("$2");
		directionPortInvertZField.setLabel("Invert Z");
		directionPortInvertZField.setBitPosition(2);
		

		addField(stepXField);                     // STEPS_PER_MM_X 				
		addField(maxRateXField);                  // MAX_RATE_X 			
		addField(accelXField);                    // ACCELERATION_X	 		
		addField(maxTravelXField);                // MAX_TRAVEL_X	
		addField(stepYField);                     // STEPS_PER_MM_Y 		
		addField(maxRateYField);                  // MAX_RATE_Y 		
		addField(accelYField);                    // ACCELERATION_Y 		
		addField(maxTravelYField);                // MAX_TRAVEL_Y 			
		addField(stepZField);                     // STEPS_PER_MM_Z 		
		addField(maxRateZField);                  // MAX_RATE_Z 			
		addField(accelZField);                    // ACCELERATION_Z 			
		addField(maxTravelZField);                // MAX_TRAVEL_Z 		
		addField(stepPortInvertXField);           
		addField(stepPortInvertYField);            
		addField(stepPortInvertZField);            	
		addField(directionPortInvertXField);      // DIRECTION_PORT_INVERT    
		addField(directionPortInvertYField);      // DIRECTION_PORT_INVERT      
		addField(directionPortInvertZField);	  // DIRECTION_PORT_INVERT
		addField(stepPulseField);                 // STEP_PULSE 				
		addField(stepIdleField);                  // STEP_IDLE_DELAY 				
		addField(junctionDeviationField);         // JUNCTION_DEVIATION			
		addField(arcToleranceField);              // ARC_TOLERANCE 			
		addField(stepEnableInvertField);          // STEP_ENABLE_INVERT 		
		addField(limitPinInvertField);            // LIMIT_PIN_INVERT 		
		addField(reportInchesField);              // REPORT_INCHES	 			
		addField(probePinInvertField);            // PROBE_PIN_INVERT 			
		addField(softLimitsField);                // SOFT_LIMITS_ENABLED 			
		addField(hardLimitsField);                // HARD_LIMITS_ENABLED 			
		addField(homingCycleField);               // HOMING_CYCLE_ENABLED  				
		addField(homingInvertXField);             // HOMING_DIRECTION_INVERT  				
		addField(homingInvertYField);             // HOMING_DIRECTION_INVERT  				
		addField(homingInvertZField);             // HOMING_DIRECTION_INVERT 			
		addField(homingFeedField);                // HOMING_FEED 			
		addField(homingDebounceField);            // HOMING_DEBOUNCE 			
		addField(homingSeekField);                // HOMING_SEEK 			
		addField(homingPulloffField);             // HOMING_PULL_OFF 			
		addField(laserModeField);				  // LASER_MODE
		addField(minSpindleSpeedField);			  // MIN_SPINDLE_SPEED
		addField(maxSpindleSpeedField);			  // MAX_SPINDLE_SPEED
		
		// STATUS_REPORT
	}

	protected Point getInitialSize() {
		return new Point(627, 650);
	}
}
