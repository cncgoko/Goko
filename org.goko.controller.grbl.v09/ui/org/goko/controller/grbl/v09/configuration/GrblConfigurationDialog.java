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
package org.goko.controller.grbl.v09.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.controller.grbl.v09.configuration.editors.GrblBigDecimalSettingFieldEditor;
import org.goko.controller.grbl.v09.configuration.editors.GrblBitMaskFieldEditor;
import org.goko.controller.grbl.v09.configuration.editors.GrblBooleanFieldEditor;
import org.goko.controller.grbl.v09.configuration.editors.GrblIntegerSettingFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 *
 * @author PsyKo
 *
 */
public class GrblConfigurationDialog extends AbstractGrblConfigurationPage {
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
		grpAxisConfiguration.setLayout(new GridLayout(1, false));
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
		
		GrblBigDecimalSettingFieldEditor stepXField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		stepXField.setWidthInChars(10);
		stepXField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		stepXField.setLabel("$100  x");
		stepXField.setPreferenceName("$100");
		
		GrblBigDecimalSettingFieldEditor maxRateXField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		maxRateXField.setPreferenceName("$110");
		maxRateXField.setWidthInChars(10);
		maxRateXField.setLabel("$110  x");
		
		GrblBigDecimalSettingFieldEditor accelXField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		accelXField.setPreferenceName("$120");
		accelXField.setLabel("$120  x");
		accelXField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor maxTravelXField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		maxTravelXField.setPreferenceName("$130");
		maxTravelXField.setLabel("$130  x");
		maxTravelXField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor stepYField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		stepYField.setPreferenceName("$101");
		stepYField.setWidthInChars(10);
		stepYField.setLabel("$101  y");
		
		GrblBigDecimalSettingFieldEditor maxRateYField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		maxRateYField.setPreferenceName("$111");
		maxRateYField.setWidthInChars(10);
		maxRateYField.setLabel("$111  y");
		
		GrblBigDecimalSettingFieldEditor accelYField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		accelYField.setPreferenceName("$121");
		accelYField.setLabel("$121  y");
		accelYField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor maxTravelYField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		maxTravelYField.setPreferenceName("$131");
		maxTravelYField.setLabel("$131  y");
		maxTravelYField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor stepZField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		stepZField.setPreferenceName("$102");
		stepZField.setWidthInChars(10);
		stepZField.setLabel("$102  z");
		
		GrblBigDecimalSettingFieldEditor maxRateZField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		maxRateZField.setPreferenceName("$112");
		maxRateZField.setWidthInChars(10);
		maxRateZField.setLabel("$112  z");
		
		GrblBigDecimalSettingFieldEditor accelZField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		accelZField.setPreferenceName("$122");
		accelZField.setLabel("$122  z");
		accelZField.setWidthInChars(10);
		
		GrblBigDecimalSettingFieldEditor maxTravelZField = new GrblBigDecimalSettingFieldEditor(composite_10, SWT.NONE);
		maxTravelZField.setPreferenceName("$132");
		maxTravelZField.setLabel("$132  z");
		maxTravelZField.setWidthInChars(10);
		
		Composite composite_3 = new Composite(grpAxisConfiguration, SWT.NONE);
		composite_3.setLayout(new GridLayout(6, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblstepPort = new Label(composite_3, SWT.NONE);
		lblstepPort.setText("$2 (step port invert mask)");
		lblstepPort.setBounds(0, 0, 134, 15);
		
		GrblBitMaskFieldEditor stepPortInvertXField = new GrblBitMaskFieldEditor(composite_3, SWT.NONE);
		stepPortInvertXField.setPreferenceName("$2");
		stepPortInvertXField.setLabel("Invert X");
		new Label(composite_3, SWT.NONE);
		
		GrblBitMaskFieldEditor stepPortInvertYField = new GrblBitMaskFieldEditor(composite_3, SWT.NONE);
		stepPortInvertYField.setBitPosition(1);
		stepPortInvertYField.setPreferenceName("$2");
		stepPortInvertYField.setLabel("Invert Y");
		new Label(composite_3, SWT.NONE);
		
		GrblBitMaskFieldEditor stepPortInvertZField = new GrblBitMaskFieldEditor(composite_3, SWT.NONE);
		stepPortInvertZField.setPreferenceName("$2");
		stepPortInvertZField.setLabel("Invert Z");
		stepPortInvertZField.setBitPosition(2);
		
		Group grpGeneral = new Group(composite, SWT.NONE);
		grpGeneral.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpGeneral.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpGeneral.setLayout(new GridLayout(1, false));
		grpGeneral.setText("General");
		
		Composite composite_5 = new Composite(grpGeneral, SWT.NONE);
		composite_5.setLayout(new GridLayout(3, false));
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		GrblIntegerSettingFieldEditor stepPulseField = new GrblIntegerSettingFieldEditor(composite_5, SWT.NONE);
		stepPulseField.setLabelWidthInChar(23);
		stepPulseField.setWidthInChars(8);
		stepPulseField.setPreferenceName("$0");
		stepPulseField.setLabel("$0 (step pulse, usec)");
		new Label(composite_5, SWT.NONE);
		
		GrblIntegerSettingFieldEditor stepIdleField = new GrblIntegerSettingFieldEditor(composite_5, SWT.NONE);
		stepIdleField.setLabelWidthInChar(19);
		stepIdleField.setWidthInChars(8);
		stepIdleField.setPreferenceName("$1");
		stepIdleField.setLabel("$1 (step idle delay, msec)");
		
		GrblBigDecimalSettingFieldEditor junctionDeviationField = new GrblBigDecimalSettingFieldEditor(composite_5, SWT.NONE);
		junctionDeviationField.setLabelWidthInChar(23);
		junctionDeviationField.setWidthInChars(8);
		junctionDeviationField.setPreferenceName("$11");
		junctionDeviationField.setLabel("$11 (junction deviation, mm)");
		new Label(composite_5, SWT.NONE);
		
		GrblBigDecimalSettingFieldEditor arcToleranceField = new GrblBigDecimalSettingFieldEditor(composite_5, SWT.NONE);
		arcToleranceField.setLabelWidthInChar(19);
		arcToleranceField.setWidthInChars(8);
		arcToleranceField.setPreferenceName("$12");
		arcToleranceField.setLabel("$12 (arc tolerance, mm)");
		
		GrblBooleanFieldEditor stepEnableInvertField = new GrblBooleanFieldEditor(composite_5, SWT.NONE);
		stepEnableInvertField.setLabel("$4 (step enable invert)");
		stepEnableInvertField.setPreferenceName("$4");
		new Label(composite_5, SWT.NONE);
		new Label(composite_5, SWT.NONE);
		
		GrblBooleanFieldEditor limitPinInvertField = new GrblBooleanFieldEditor(composite_5, SWT.NONE);
		limitPinInvertField.setPreferenceName("$5");
		limitPinInvertField.setLabel("$5 (limit pins invert)");
		new Label(composite_5, SWT.NONE);
		
		GrblBooleanFieldEditor reportInchesField = new GrblBooleanFieldEditor(composite_5, SWT.NONE);
		reportInchesField.setPreferenceName("$13");
		reportInchesField.setLabel("$13 (report inches)");
		
		GrblBooleanFieldEditor probePinInvertField = new GrblBooleanFieldEditor(composite_5, SWT.NONE);
		probePinInvertField.setPreferenceName("$6");
		probePinInvertField.setLabel("$6 (probe pin invert)");
		new Label(composite_5, SWT.NONE);
		new Label(composite_5, SWT.NONE);
		
		Group grpHoming = new Group(composite, SWT.NONE);
		grpHoming.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpHoming.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpHoming.setLayout(new GridLayout(1, false));
		grpHoming.setText("Homing");
		
		Composite composite_6 = new Composite(grpHoming, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_6.setLayout(new GridLayout(5, false));
		
		GrblBooleanFieldEditor softLimitsField = new GrblBooleanFieldEditor(composite_6, SWT.NONE);
		softLimitsField.setLabel("$20 (soft limits)");
		softLimitsField.setPreferenceName("$20");
		new Label(composite_6, SWT.NONE);
		
		GrblBooleanFieldEditor hardLimitsField = new GrblBooleanFieldEditor(composite_6, SWT.NONE);
		hardLimitsField.setPreferenceName("$21");
		hardLimitsField.setLabel("$21 (hard limits)");
		new Label(composite_6, SWT.NONE);
		
		GrblBooleanFieldEditor homingCycleField = new GrblBooleanFieldEditor(composite_6, SWT.NONE);
		homingCycleField.setLabel("$22 (homing cycle)");
		homingCycleField.setPreferenceName("$22");
		
		Composite composite_7 = new Composite(grpHoming, SWT.NONE);
		composite_7.setLayout(new GridLayout(6, false));
		composite_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblhomingDir = new Label(composite_7, SWT.NONE);
		lblhomingDir.setText("$23 (homing dir invert mask)");
		
		GrblBitMaskFieldEditor homingInvertXField = new GrblBitMaskFieldEditor(composite_7, SWT.NONE);
		homingInvertXField.setPreferenceName("$23");
		homingInvertXField.setLabel("Invert X");
		new Label(composite_7, SWT.NONE);
		
		GrblBitMaskFieldEditor homingInvertYField = new GrblBitMaskFieldEditor(composite_7, SWT.NONE);
		homingInvertYField.setBitPosition(1);
		homingInvertYField.setPreferenceName("$23");
		homingInvertYField.setLabel("Invert Y");
		new Label(composite_7, SWT.NONE);
		
		GrblBitMaskFieldEditor homingInvertZField = new GrblBitMaskFieldEditor(composite_7, SWT.NONE);
		homingInvertZField.setBitPosition(2);
		homingInvertZField.setPreferenceName("$23");
		homingInvertZField.setLabel("Invert Z");
		
		Composite composite_8 = new Composite(grpHoming, SWT.NONE);
		composite_8.setLayout(new GridLayout(3, false));
		composite_8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		GrblBigDecimalSettingFieldEditor homingFeedField = new GrblBigDecimalSettingFieldEditor(composite_8, SWT.NONE);
		homingFeedField.setWidthInChars(8);
		homingFeedField.setLabelWidthInChar(22);
		homingFeedField.setPreferenceName("$24");
		homingFeedField.setLabel("$24 (homing feed, mm/min)");
		new Label(composite_8, SWT.NONE);
		
		GrblBigDecimalSettingFieldEditor homingDebounceField = new GrblBigDecimalSettingFieldEditor(composite_8, SWT.NONE);
		homingDebounceField.setWidthInChars(8);
		homingDebounceField.setLabelWidthInChar(24);
		homingDebounceField.setPreferenceName("$26");
		homingDebounceField.setLabel("$26 (homing debounce, msec)");
		
		GrblBigDecimalSettingFieldEditor homingSeekField = new GrblBigDecimalSettingFieldEditor(composite_8, SWT.NONE);
		homingSeekField.setWidthInChars(8);
		homingSeekField.setLabelWidthInChar(22);
		homingSeekField.setPreferenceName("$25");
		homingSeekField.setLabel("$25 (homing seek, mm/min)");
		new Label(composite_8, SWT.NONE);
		
		GrblBigDecimalSettingFieldEditor homingPulloffField = new GrblBigDecimalSettingFieldEditor(composite_8, SWT.NONE);
		homingPulloffField.setWidthInChars(8);
		homingPulloffField.setLabelWidthInChar(24);
		homingPulloffField.setLabel("$27 (homing pull-off, mm)");
		homingPulloffField.setPreferenceName("$27");
		
		addField(stepXField);
		addField(maxRateXField);
		addField(accelXField);
		addField(maxTravelXField);
		addField(stepYField);
		addField(maxRateYField);
		addField(accelYField);
		addField(maxTravelYField);
		addField(stepZField);
		addField(maxRateZField);
		addField(accelZField);
		addField(maxTravelZField);
		addField(stepPortInvertXField);
		addField(stepPortInvertYField);
		addField(stepPortInvertZField);
		addField(stepPulseField);
		addField(stepIdleField);
		addField(junctionDeviationField);
		addField(arcToleranceField);
		addField(stepEnableInvertField);
		addField(limitPinInvertField);
		addField(reportInchesField);
		addField(probePinInvertField);
		addField(softLimitsField);
		addField(hardLimitsField);
		addField(homingCycleField);
		addField(homingInvertXField);
		addField(homingInvertYField);
		addField(homingInvertZField);
		addField(homingFeedField);
		addField(homingDebounceField);
		addField(homingSeekField);
		addField(homingPulloffField);
	}

	protected Point getInitialSize() {
		return new Point(627, 650);
	}
}
