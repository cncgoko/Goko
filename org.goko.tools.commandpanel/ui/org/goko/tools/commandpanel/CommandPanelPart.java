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
package org.goko.tools.commandpanel;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.IGkConstants;
import org.goko.core.controller.action.DefaultControllerAction;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
import org.goko.core.log.GkLog;
import org.goko.tools.commandpanel.controller.CommandPanelController;
import org.goko.tools.commandpanel.controller.CommandPanelModel;
import org.goko.tools.commandpanel.preferences.CommandPanelPreference;

public class CommandPanelPart extends GkUiComponent<CommandPanelController, CommandPanelModel>
		implements IPropertyChangeListener {
	private static final String JOG_PRECISE = "org.goko.tools.commandpanel.jogPrecise";
	private static final String JOG_STEP = "org.goko.tools.commandpanel.jogStep";
	private static final String JOG_FEEDRATE = "org.goko.tools.commandpanel.jogFeedrate";
	private static GkLog LOG = GkLog.getLogger(CommandPanelPart.class);
	private Button btnHome;
	private Button btnStop;
	private Button btnPause;
	private Button btnStart;
	private Button btnResetZero;
	private Button btnJogZPos;
	private Button btnSpindleOnCw;
	private Button btnSpindleOnCcw;
	private Button btnSpindleOff;
	private Button btnJogXNeg;
	private Button btnJogXPos;
	private Button btnJogYNeg;
	private Button btnJogYPos;
	private Button btnJogZNeg;
	private Button btnResetX;
	private Button btnResetY;
	private Button btnResetZ;
	private Button btnJogANeg;
	private Button btnJogAPos;
	private Button btnResetA;
	private Button btnJogBNeg;
	private Button btnJogBPos;
	private Button btnResetB;
	private Button btnJogCNeg;
	private Button btnJogCPos;
	private Button btnResetC;

	@Inject
	@Preference
	IEclipsePreferences prefs;
	private Button btnPreciseJog;
	private Button btnKillAlarm;
	private Spinner jogStepSpinner;
	private Spinner jogSpeedSpinner;
	private Composite composite_10;
	private Group grpCoordinatesSystem;
	private Button btnCSG54;
	private Button btnCSG55;
	private Button btnCSG56;
	private Button btnCSG57;
	private Button btnCSG58;
	private Button btnCSG59;
	private Button btnResetCsZero;
	private Label lblLengthUnit;
	private Label lblJogStep;
	private Button btnReset;
	private Label lblSpeedUnit;
	private Composite composite_13;
	private Composite composite_14;
	private Composite composite_1;

	@Inject
	public CommandPanelPart(IEclipseContext context) throws GkException {
		super(context, new CommandPanelController(new CommandPanelModel()));
	}

	/**
	 * Create contents of the view part.
	 *
	 * @throws GkException
	 */
	@PostConstruct
	public void createControls(Composite parent, MPart part) throws GkException {
		parent.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		GridLayout gl_composite = new GridLayout(3, true);
		gl_composite.verticalSpacing = 3;
		gl_composite.marginHeight = 3;
		gl_composite.marginWidth = 3;
		composite.setLayout(gl_composite);

		Group grpManualJog = new Group(composite, SWT.NONE);
		grpManualJog.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpManualJog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpManualJog.setText("Manual jog");

		GridLayout gl_grpManualJog = new GridLayout(1, false);
		gl_grpManualJog.verticalSpacing = 2;
		gl_grpManualJog.marginWidth = 4;
		gl_grpManualJog.marginHeight = 2;
		gl_grpManualJog.horizontalSpacing = 4;
		grpManualJog.setLayout(gl_grpManualJog);

		btnPreciseJog = new Button(grpManualJog, SWT.CHECK);
		btnPreciseJog.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnPreciseJog.setEnabled(false);
		btnPreciseJog.setText("Precise jog");

		Composite composite_5 = new Composite(grpManualJog, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		GridLayout gl_composite_5 = new GridLayout(3, false);
		gl_composite_5.marginWidth = 0;
		composite_5.setLayout(gl_composite_5);

		lblJogStep = new Label(composite_5, SWT.NONE);
		lblJogStep.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJogStep.setText("Step");

		jogStepSpinner = new Spinner(composite_5, SWT.BORDER);
		jogStepSpinner.setMaximum(100000);
		jogStepSpinner.setMinimum(1);
		jogStepSpinner.setDigits(GokoPreference.getInstance().getDigitCount());
		GridData gd_jogSpinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_jogSpinner.widthHint = 60;
		jogStepSpinner.setLayoutData(gd_jogSpinner);

		lblLengthUnit = new Label(composite_5, SWT.NONE);

		lblLengthUnit.setText("mm");

		Label lblJogSpeed = new Label(composite_5, SWT.NONE);
		lblJogSpeed.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJogSpeed.setBounds(0, 0, 55, 15);

		lblJogSpeed.setText("Speed");

		jogSpeedSpinner = new Spinner(composite_5, SWT.BORDER);
		jogSpeedSpinner.setMaximum(10000);
		GridData gd_jogSpeedSpinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_jogSpeedSpinner.widthHint = 60;
		jogSpeedSpinner.setLayoutData(gd_jogSpeedSpinner);

		lblSpeedUnit = new Label(composite_5, SWT.NONE);
		lblSpeedUnit.setText("mm/min");

		composite_14 = new Composite(grpManualJog, SWT.NONE);
		composite_14.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_14.setLayout(new GridLayout(4, true));

		btnJogZPos = new Button(composite_14, SWT.NONE);
		btnJogZPos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnJogZPos.setText("+Z");
		new Label(composite_14, SWT.NONE);

		btnJogYPos = new Button(composite_14, SWT.NONE);
		btnJogYPos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnJogYPos.setText("+Y");
		new Label(composite_14, SWT.NONE);
		new Label(composite_14, SWT.NONE);

		btnJogXNeg = new Button(composite_14, SWT.NONE);
		btnJogXNeg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnJogXNeg.setText("-X");
		new Label(composite_14, SWT.NONE);

		btnJogXPos = new Button(composite_14, SWT.NONE);
		btnJogXPos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnJogXPos.setText("+X");

		btnJogZNeg = new Button(composite_14, SWT.NONE);
		btnJogZNeg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnJogZNeg.setText("-Z");
		new Label(composite_14, SWT.NONE);

		btnJogYNeg = new Button(composite_14, SWT.NONE);
		btnJogYNeg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnJogYNeg.setText("-Y");
		new Label(composite_14, SWT.NONE);
		new Label(composite_14, SWT.NONE);
		boolean axisA = CommandPanelPreference.getInstance().isAAxisEnabled();
		boolean axisB = CommandPanelPreference.getInstance().isBAxisEnabled();
		boolean axisC = CommandPanelPreference.getInstance().isCAxisEnabled();
		if (axisA) {
			btnJogAPos = new Button(composite_14, SWT.NONE);
			btnJogAPos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
			btnJogAPos.setText("A+");			
		}
		if (CommandPanelPreference.getInstance().isBAxisEnabled()) {
			if (!axisC && axisA) {
				new Label(composite_14, SWT.NONE);		
			}
			btnJogBPos = new Button(composite_14, SWT.NONE);
			btnJogBPos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
			btnJogBPos.setText("B+");			
		}
		if (CommandPanelPreference.getInstance().isCAxisEnabled()) {
			if (!axisA && axisB || axisA && !axisB) {
				new Label(composite_14, SWT.NONE);		
			}
			btnJogCPos = new Button(composite_14, SWT.NONE);
			btnJogCPos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
			btnJogCPos.setText("C+");	
		}	
		new Label(composite_14, SWT.NONE);
	
		if (CommandPanelPreference.getInstance().isAAxisEnabled()) {			
			btnJogANeg = new Button(composite_14, SWT.NONE);
			btnJogANeg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
			btnJogANeg.setText("A-");
		}
		if (CommandPanelPreference.getInstance().isBAxisEnabled()) {			
			if (!axisC && axisA) {
				new Label(composite_14, SWT.NONE);		
			}
			btnJogBNeg = new Button(composite_14, SWT.NONE);
			btnJogBNeg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
			btnJogBNeg.setText("B-");	
		}
		if (CommandPanelPreference.getInstance().isCAxisEnabled()) {
			if (!axisA && axisB || axisA && !axisB) {
				new Label(composite_14, SWT.NONE);		
			}
			btnJogCNeg = new Button(composite_14, SWT.NONE);
			btnJogCNeg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
			btnJogCNeg.setText("C-");	
		}
		Composite composite_9 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_9 = new GridLayout(1, false);
		gl_composite_9.horizontalSpacing = 4;
		gl_composite_9.verticalSpacing = 4;
		gl_composite_9.marginWidth = 0;
		gl_composite_9.marginHeight = 0;
		composite_9.setLayout(gl_composite_9);
		composite_9.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Group grpCommands = new Group(composite_9, SWT.NONE);
		grpCommands.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpCommands.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_grpCommands = new GridLayout(1, false);
		gl_grpCommands.verticalSpacing = 2;
		gl_grpCommands.marginWidth = 2;
		gl_grpCommands.marginHeight = 2;
		gl_grpCommands.horizontalSpacing = 2;
		grpCommands.setLayout(gl_grpCommands);
		grpCommands.setText("Homing");

		btnHome = new Button(grpCommands, SWT.NONE);
		btnHome.setToolTipText("Trigger the homing routine");
		btnHome.setText("Home");
		GridData gd_btnNewButton_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_btnNewButton_1.widthHint = 140;
		gd_btnNewButton_1.heightHint = 35;
		btnHome.setLayoutData(gd_btnNewButton_1);

		Label label = new Label(grpCommands, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_label.verticalIndent = 2;
		label.setLayoutData(gd_label);

		composite_1 = new Composite(grpCommands, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 2;
		composite_1.setLayout(gl_composite_1);

		btnResetZero = new Button(composite_1, SWT.NONE);
		btnResetZero.setToolTipText("Set the current position as zero on all axis");
		btnResetZero.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnResetZero.setText("Zero all axis");

		Composite composite_6 = new Composite(grpCommands, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		int column = ((axisA && !axisB && !axisC) || (!axisA && axisB && !axisC) || (!axisA && !axisB && axisC)) ? 4: 3;
		GridLayout gl_composite_6 = new GridLayout(column, true);
		gl_composite_6.horizontalSpacing = 4;
		gl_composite_6.marginHeight = 2;
		gl_composite_6.verticalSpacing = 0;
		gl_composite_6.marginWidth = 0;
		composite_6.setLayout(gl_composite_6);

		btnResetX = new Button(composite_6, SWT.NONE);
		btnResetX.setToolTipText("Set the current position as zero on X axis");
		btnResetX.setText("X");
		btnResetX.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		GridData gd_btnResetX = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_btnResetX.heightHint = 30;
		btnResetX.setLayoutData(gd_btnResetX);

		btnResetY = new Button(composite_6, SWT.NONE);
		btnResetY.setToolTipText("Set the current position as zero on Y axis");
		btnResetY.setText("Y");
		btnResetY.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		GridData gd_btnNewButton = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_btnNewButton.heightHint = 30;
		btnResetY.setLayoutData(gd_btnNewButton);

		btnResetZ = new Button(composite_6, SWT.NONE);
		btnResetZ.setToolTipText("Set the current position as zero on Z axis");
		btnResetZ.setText("Z");
		btnResetZ.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		GridData gd_btnNewButton_21 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_btnNewButton_21.heightHint = 30;
		btnResetZ.setLayoutData(gd_btnNewButton_21);

		if (axisA) {
			btnResetA = new Button(composite_6, SWT.NONE);
			btnResetA.setToolTipText("Set the current position as zero on A axis");
			GridData gd_btnResetA = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
			gd_btnResetA.heightHint = 30;
			btnResetA.setLayoutData(gd_btnResetA);
			btnResetA.setText("A");
		}
		
		if (axisB) {
			btnResetB = new Button(composite_6, SWT.NONE);
			btnResetB.setToolTipText("Set the current position as zero on B axis");
			GridData gd_btnResetB = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
			gd_btnResetB.heightHint = 30;
			btnResetB.setLayoutData(gd_btnResetB);
			btnResetB.setText("B");
		}
		
		if (axisC) {
			btnResetC = new Button(composite_6, SWT.NONE);
			btnResetC.setToolTipText("Set the current position as zero on C axis");
			GridData gd_btnResetC = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
			gd_btnResetC.heightHint = 30;
			btnResetC.setLayoutData(gd_btnResetC);
			btnResetC.setText("C");
		}

		grpCoordinatesSystem = new Group(composite_9, SWT.NONE);
		grpCoordinatesSystem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridLayout gl_grpCoordinatesSystem = new GridLayout(1, false);
		gl_grpCoordinatesSystem.verticalSpacing = 2;
		gl_grpCoordinatesSystem.marginWidth = 2;
		gl_grpCoordinatesSystem.marginHeight = 2;
		gl_grpCoordinatesSystem.horizontalSpacing = 2;
		grpCoordinatesSystem.setLayout(gl_grpCoordinatesSystem);
		grpCoordinatesSystem.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		grpCoordinatesSystem.setText("Coordinates system");

		Composite composite_11 = new Composite(grpCoordinatesSystem, SWT.NONE);
		composite_11.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		GridLayout gl_composite_11 = new GridLayout(3, true);
		gl_composite_11.verticalSpacing = 2;
		gl_composite_11.horizontalSpacing = 2;
		gl_composite_11.marginWidth = 0;
		gl_composite_11.marginHeight = 0;
		composite_11.setLayout(gl_composite_11);

		btnCSG54 = new Button(composite_11, SWT.NONE);
		btnCSG54.setToolTipText("Activate G54 coordinate system");
		btnCSG54.setText("G54");
		btnCSG54.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().setCoordinateSystem(CoordinateSystem.G54);
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		GridData gd_btnCSG54 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_btnCSG54.heightHint = 30;
		btnCSG54.setLayoutData(gd_btnCSG54);

		btnCSG55 = new Button(composite_11, SWT.NONE);
		btnCSG55.setToolTipText("Activate G55 coordinate system");
		btnCSG55.setText("G55");
		btnCSG55.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().setCoordinateSystem(CoordinateSystem.G55);
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		GridData gd_btnCSG55 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_btnCSG55.heightHint = 30;
		btnCSG55.setLayoutData(gd_btnCSG55);

		btnCSG56 = new Button(composite_11, SWT.NONE);
		btnCSG56.setToolTipText("Activate G56 coordinate system");
		btnCSG56.setText("G56");
		btnCSG56.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().setCoordinateSystem(CoordinateSystem.G56);
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		GridData gd_btnCSG56 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_btnCSG56.heightHint = 30;
		btnCSG56.setLayoutData(gd_btnCSG56);

		btnCSG57 = new Button(composite_11, SWT.NONE);
		btnCSG57.setToolTipText("Activate G57 coordinate system");
		btnCSG57.setText("G57");
		btnCSG57.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().setCoordinateSystem(CoordinateSystem.G57);
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		GridData gd_btnCSG57 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_btnCSG57.heightHint = 30;
		btnCSG57.setLayoutData(gd_btnCSG57);

		btnCSG58 = new Button(composite_11, SWT.NONE);
		btnCSG58.setToolTipText("Activate G58 coordinate system");
		btnCSG58.setText("G58");
		btnCSG58.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().setCoordinateSystem(CoordinateSystem.G58);
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		GridData gd_btnCSG58 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_btnCSG58.heightHint = 30;
		btnCSG58.setLayoutData(gd_btnCSG58);

		btnCSG59 = new Button(composite_11, SWT.NONE);
		btnCSG59.setToolTipText("Activate G59 coordinate system");
		btnCSG59.setText("G59");
		btnCSG59.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().setCoordinateSystem(CoordinateSystem.G59);
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		GridData gd_btnCSG59 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_btnCSG59.heightHint = 30;
		btnCSG59.setLayoutData(gd_btnCSG59);

		Composite composite_12 = new Composite(grpCoordinatesSystem, SWT.NONE);
		composite_12.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		GridLayout gl_composite_12 = new GridLayout(1, false);
		gl_composite_12.verticalSpacing = 2;
		gl_composite_12.marginHeight = 2;
		gl_composite_12.marginWidth = 0;
		gl_composite_12.horizontalSpacing = 0;
		composite_12.setLayout(gl_composite_12);

		btnResetCsZero = new Button(composite_12, SWT.NONE);
		btnResetCsZero.setToolTipText("Reset current coordinate system zero to be at the current position");
		btnResetCsZero.setText("Set current zero");
		btnResetCsZero.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().zeroCoordinateSystem();
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		GridData gd_btnResetCsZero = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_btnResetCsZero.heightHint = 35;
		gd_btnNewButton_1.heightHint = 35;
		btnResetCsZero.setLayoutData(gd_btnResetCsZero);
		btnResetCsZero.setBounds(0, 0, 75, 25);

		composite_10 = new Composite(composite, SWT.NONE);
		GridData gd_composite_10 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite_10.widthHint = 140;
		composite_10.setLayoutData(gd_composite_10);

		GridLayout gl_composite_10 = new GridLayout(1, false);
		gl_composite_10.verticalSpacing = 4;
		gl_composite_10.marginWidth = 2;
		gl_composite_10.marginHeight = 2;
		gl_composite_10.horizontalSpacing = 4;
		composite_10.setLayout(gl_composite_10);

		Group grpControls = new Group(composite_10, SWT.NONE);
		grpControls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpControls.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpControls.setLayout(new GridLayout(1, false));
		grpControls.setText("Controls");

		Composite composite_8 = new Composite(grpControls, SWT.NONE);
		composite_8.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		GridLayout gl_composite_8 = new GridLayout(2, true);
		gl_composite_8.verticalSpacing = 2;
		gl_composite_8.horizontalSpacing = 2;
		gl_composite_8.marginWidth = 0;
		gl_composite_8.marginHeight = 0;
		composite_8.setLayout(gl_composite_8);

		btnStart = new Button(composite_8, SWT.NONE);
		btnStart.setToolTipText("Resume the controller execution");
		GridData gd_btnStart = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_btnStart.heightHint = 37;
		btnStart.setLayoutData(gd_btnStart);

		btnStart.setText("Resume");

		btnPause = new Button(composite_8, SWT.NONE);
		btnPause.setToolTipText("Pause the controller execution");
		GridData gd_btnPause = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_btnPause.heightHint = 37;
		btnPause.setLayoutData(gd_btnPause);

		btnPause.setText("Pause");

		btnStop = new Button(grpControls, SWT.NONE);
		btnStop.setToolTipText("Stop the controller execution");
		btnStop.setImage(ResourceManager.getPluginImage("org.goko.tools.commandpanel", "icons/stop.png"));
		btnStop.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		GridData gd_btnStop = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_btnStop.heightHint = 37;
		btnStop.setLayoutData(gd_btnStop);

		btnStop.setText("Stop");

		btnKillAlarm = new Button(grpControls, SWT.NONE);
		btnKillAlarm.setToolTipText("Kill the alarm on the controller");
		btnKillAlarm.setImage(ResourceManager.getPluginImage("org.goko.tools.commandpanel", "icons/bell--minus.png"));
		GridData gd_btnKillAlarm = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_btnKillAlarm.heightHint = 37;
		btnKillAlarm.setLayoutData(gd_btnKillAlarm);

		btnKillAlarm.setText("Kill alarm");

		btnReset = new Button(grpControls, SWT.NONE);
		btnReset.setToolTipText("Reset the controller");
		GridData gd_btnReset = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_btnReset.heightHint = 37;
		btnReset.setLayoutData(gd_btnReset);
		btnReset.setText("Reset");
		btnReset.setImage(ResourceManager.getPluginImage("org.goko.tools.commandpanel", "icons/reset.png"));

		Group grpSpindle = new Group(composite_10, SWT.NONE);
		grpSpindle.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpSpindle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		grpSpindle.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpSpindle.setText("Spindle");

		Composite composite_3 = new Composite(grpSpindle, SWT.NONE);

		GridLayout gl_composite_3 = new GridLayout(1, true);
		gl_composite_3.marginWidth = 2;
		gl_composite_3.horizontalSpacing = 2;
		gl_composite_3.marginHeight = 2;
		composite_3.setLayout(gl_composite_3);

		composite_13 = new Composite(composite_3, SWT.NONE);
		GridLayout gl_composite_13 = new GridLayout(2, true);
		gl_composite_13.verticalSpacing = 0;
		gl_composite_13.marginWidth = 0;
		gl_composite_13.marginHeight = 0;
		composite_13.setLayout(gl_composite_13);
		composite_13.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		btnSpindleOnCw = new Button(composite_13, SWT.NONE);
		btnSpindleOnCw.setToolTipText("Activate the spindle clockwise");
		btnSpindleOnCw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnSpindleOnCw.setText("CW");

		btnSpindleOnCcw = new Button(composite_13, SWT.NONE);
		btnSpindleOnCcw.setToolTipText("Activate the spindle counterclockwise");
		btnSpindleOnCcw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnSpindleOnCcw.setText("CCW");

		btnSpindleOff = new Button(composite_3, SWT.NONE);
		btnSpindleOff.setToolTipText("Turn the spindle off");
		btnSpindleOff.setText("Off");
		GridData gd_btnSpindleOff = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_btnSpindleOff.heightHint = 38;
		btnSpindleOff.setLayoutData(gd_btnSpindleOff);

		initFromPersistedState(part);

		if (getDataModel().getJogSpeed() != null) {
			jogSpeedSpinner.setSelection(
					(int) (getDataModel().getJogSpeed().doubleValue(GokoPreference.getInstance().getSpeedUnit())
							* Math.pow(10, jogSpeedSpinner.getDigits())));
		}
		initCustomBindings(part);
		enableAdaptiveSpinner();
	}

	protected void enableAdaptiveSpinner() throws GkException {
		if (jogStepSpinner != null) {
			BigDecimal selection = getDataModel().getJogIncrement()
					.multiply(new BigDecimal(10).pow(jogStepSpinner.getDigits()))
					.value(GokoPreference.getInstance().getLengthUnit());

			jogStepSpinner.setSelection(selection.intValue());
			jogStepSpinner.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					int selection = jogStepSpinner.getSelection();
					if (selection < 100) {
						jogStepSpinner.setIncrement(10);
					} else if (selection < 1000) {
						jogStepSpinner.setIncrement(100);
					} else if (selection < 10000) {
						jogStepSpinner.setIncrement(1000);
					}
					BigDecimal step = new BigDecimal(selection)
							.divide(new BigDecimal(10).pow(jogStepSpinner.getDigits()));
					try {
						getDataModel()
								.setJogIncrement(Length.valueOf(step, GokoPreference.getInstance().getLengthUnit()));
					} catch (GkException e) {
						LOG.error(e);
					}
				}
			});
		}

		jogSpeedSpinner.setSelection(
				(int) (getDataModel().getJogSpeed().doubleValue(GokoPreference.getInstance().getSpeedUnit())
						* Math.pow(10, jogSpeedSpinner.getDigits())));

		jogSpeedSpinner.setIncrement(10);
		jogSpeedSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					getDataModel().setJogSpeed(
							Speed.valueOf(jogSpeedSpinner.getSelection(), GokoPreference.getInstance().getSpeedUnit()));
				} catch (GkException e) {
					LOG.error(e);
				}
			}
		});

	}

	protected void initCustomBindings(MPart part) throws GkException {
		getController().addTextDisplayBinding(lblLengthUnit, "lengthUnitSymbol");
		getController().addTextDisplayBinding(lblSpeedUnit, "speedUnitSymbol");

		getController().bindEnableControlWithAction(btnHome, DefaultControllerAction.HOME);
		getController().bindButtonToExecuteAction(btnHome, DefaultControllerAction.HOME);
		getController().bindEnableControlWithAction(btnStart, DefaultControllerAction.START);
		getController().bindButtonToExecuteAction(btnStart, DefaultControllerAction.START);
		getController().bindEnableControlWithAction(btnPause, DefaultControllerAction.PAUSE);
		getController().bindButtonToExecuteAction(btnPause, DefaultControllerAction.PAUSE);
		getController().bindEnableControlWithAction(btnStop, DefaultControllerAction.STOP);
		getController().bindButtonToExecuteAction(btnStop, DefaultControllerAction.STOP);
		getController().bindEnableControlWithAction(btnResetZero, DefaultControllerAction.RESET_ZERO);
		getController().bindButtonToExecuteAction(btnResetZero, DefaultControllerAction.RESET_ZERO);
		getController().bindEnableControlWithAction(btnResetX, DefaultControllerAction.RESET_ZERO);
		getController().bindButtonToExecuteAction(btnResetX, DefaultControllerAction.RESET_ZERO, IGkConstants.X_AXIS);
		getController().bindEnableControlWithAction(btnResetY, DefaultControllerAction.RESET_ZERO);
		getController().bindButtonToExecuteAction(btnResetY, DefaultControllerAction.RESET_ZERO, IGkConstants.Y_AXIS);
		getController().bindEnableControlWithAction(btnResetZ, DefaultControllerAction.RESET_ZERO);
		getController().bindButtonToExecuteAction(btnResetZ, DefaultControllerAction.RESET_ZERO, IGkConstants.Z_AXIS);
		if (btnResetA != null) {
			getController().bindEnableControlWithAction(btnResetA, DefaultControllerAction.RESET_ZERO);
			getController().bindButtonToExecuteAction(btnResetA, DefaultControllerAction.RESET_ZERO, IGkConstants.A_AXIS);
		}
		if (btnResetB != null) {
			getController().bindEnableControlWithAction(btnResetB, DefaultControllerAction.RESET_ZERO);
			getController().bindButtonToExecuteAction(btnResetB, DefaultControllerAction.RESET_ZERO, IGkConstants.B_AXIS);
		}
		if (btnResetC != null) {
			getController().bindEnableControlWithAction(btnResetC, DefaultControllerAction.RESET_ZERO);
			getController().bindButtonToExecuteAction(btnResetC, DefaultControllerAction.RESET_ZERO, IGkConstants.C_AXIS);
		}
		getController().bindEnableControlWithAction(btnSpindleOnCw, DefaultControllerAction.SPINDLE_ON_CW);
		getController().bindButtonToExecuteAction(btnSpindleOnCw, DefaultControllerAction.SPINDLE_ON_CW);
		getController().bindEnableControlWithAction(btnSpindleOnCcw, DefaultControllerAction.SPINDLE_ON_CCW);
		getController().bindButtonToExecuteAction(btnSpindleOnCcw, DefaultControllerAction.SPINDLE_ON_CCW);
		getController().bindEnableControlWithAction(btnSpindleOff, DefaultControllerAction.SPINDLE_OFF);
		getController().bindButtonToExecuteAction(btnSpindleOff, DefaultControllerAction.SPINDLE_OFF);
		getController().bindEnableControlWithAction(btnKillAlarm, DefaultControllerAction.KILL_ALARM);
		getController().bindButtonToExecuteAction(btnKillAlarm, DefaultControllerAction.KILL_ALARM);
		getController().bindEnableControlWithAction(btnReset, DefaultControllerAction.HARD_RESET);
		getController().bindButtonToExecuteAction(btnReset, DefaultControllerAction.HARD_RESET);

		getController().addSelectionBinding(btnPreciseJog, CommandPanelModel.PRECISE_JOG_ENABLED);
		if (getDataModel().isPreciseJogForced()) {
			btnPreciseJog.setEnabled(false);
		} else {
			getController().bindEnableControlWithAction(btnPreciseJog, DefaultControllerAction.JOG_START);
		}

		// getController().bindEnableControlWithAction(jogStepSpinner,
		// DefaultControllerAction.JOG_START);
		getController().addEnableBinding(jogStepSpinner, CommandPanelModel.PRECISE_JOG_ENABLED);
		// getController().addStepSpinnerListener(jogStepSpinner,
		// DefaultControllerAction.JOG_START);
		// getController().bindEnableControlWithAction(jogStepSpinner,
		// DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(jogSpeedSpinner, DefaultControllerAction.JOG_START);

		getController().bindEnableControlWithAction(btnJogYPos, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogYNeg, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogXPos, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogXNeg, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogZPos, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogZNeg, DefaultControllerAction.JOG_START);
		if (CommandPanelPreference.getInstance().isAAxisEnabled()) {
			getController().bindEnableControlWithAction(btnJogAPos, DefaultControllerAction.JOG_START);
			getController().bindEnableControlWithAction(btnJogANeg, DefaultControllerAction.JOG_START);
		}
		if (CommandPanelPreference.getInstance().isBAxisEnabled()) {
			getController().bindEnableControlWithAction(btnJogBPos, DefaultControllerAction.JOG_START);
			getController().bindEnableControlWithAction(btnJogBNeg, DefaultControllerAction.JOG_START);
		}
		if (CommandPanelPreference.getInstance().isCAxisEnabled()) {
			getController().bindEnableControlWithAction(btnJogCPos, DefaultControllerAction.JOG_START);
			getController().bindEnableControlWithAction(btnJogCNeg, DefaultControllerAction.JOG_START);
		}
		getController().bindEnableControlWithAction(btnCSG54, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnCSG55, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnCSG56, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnCSG57, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnCSG58, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnCSG59, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnResetCsZero, DefaultControllerAction.JOG_START);

		getController().bindJogButton(btnJogYPos, EnumControllerAxis.Y_POSITIVE);
		getController().bindJogButton(btnJogYNeg, EnumControllerAxis.Y_NEGATIVE);
		getController().bindJogButton(btnJogXPos, EnumControllerAxis.X_POSITIVE);
		getController().bindJogButton(btnJogXNeg, EnumControllerAxis.X_NEGATIVE);
		getController().bindJogButton(btnJogZPos, EnumControllerAxis.Z_POSITIVE);
		getController().bindJogButton(btnJogZNeg, EnumControllerAxis.Z_NEGATIVE);
		if (CommandPanelPreference.getInstance().isAAxisEnabled()) {
			getController().bindJogButton(btnJogAPos, EnumControllerAxis.A_POSITIVE);
			getController().bindJogButton(btnJogANeg, EnumControllerAxis.A_NEGATIVE);
		}
		if (CommandPanelPreference.getInstance().isBAxisEnabled()) {
			getController().bindJogButton(btnJogBPos, EnumControllerAxis.B_POSITIVE);
			getController().bindJogButton(btnJogBNeg, EnumControllerAxis.B_NEGATIVE);
		}
		if (CommandPanelPreference.getInstance().isCAxisEnabled()) {
			getController().bindJogButton(btnJogCPos, EnumControllerAxis.C_POSITIVE);
			getController().bindJogButton(btnJogCNeg, EnumControllerAxis.C_NEGATIVE);
		}
		GokoPreference.getInstance().addPropertyChangeListener(this);
		CommandPanelPreference.getInstance().addPropertyChangeListener(this);
	}

	@PreDestroy
	public void dispose() {
		GokoPreference.getInstance().removePropertyChangeListener(this);
		CommandPanelPreference.getInstance().removePropertyChangeListener(this);
	}

	@Focus
	public void setFocus() throws GkException {
		getController().refreshExecutableAction();
	}

	/**
	 * @param event
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					lblLengthUnit.setText(GokoPreference.getInstance().getLengthUnit().getSymbol());
					lblLengthUnit.pack();
										
				} catch (GkException e) {
					LOG.error(e);
				}
			}
		});
	}

	@PersistState
	public void persistState(MPart part) {
		if (getDataModel() != null) {
			part.getPersistedState().put(JOG_PRECISE, String.valueOf(getDataModel().isPreciseJog()));
			part.getPersistedState().put(JOG_STEP, QuantityUtils.format(getDataModel().getJogIncrement(), 4, false, true));
			part.getPersistedState().put(JOG_FEEDRATE, QuantityUtils.format(getDataModel().getJogSpeed(), 4, false, true));
		}
	}

	private void initFromPersistedState(MPart part) throws GkException {
		Map<String, String> state = part.getPersistedState();
		String jogPrecise = state.get(JOG_PRECISE);
		if (StringUtils.isNotEmpty(jogPrecise)) {
			getDataModel().setPreciseJog(BooleanUtils.toBoolean(jogPrecise));
		} else {
			getDataModel().setPreciseJog(false);
		}
		String jogStepStr = state.get(JOG_STEP);
		if (StringUtils.isNotEmpty(jogStepStr)) {
			getDataModel().setJogIncrement(Length.parse(jogStepStr));
		} else {
			getDataModel().setJogIncrement(Length.valueOf(1, LengthUnit.MILLIMETRE));
		}

		String feedrateStr = state.get(JOG_FEEDRATE);
		if (StringUtils.isNotEmpty(feedrateStr)) {
			getDataModel().setJogSpeed(Speed.parse(feedrateStr));
		} else {
			getDataModel().setJogSpeed(Speed.valueOf(1000, SpeedUnit.MILLIMETRE_PER_MINUTE));
		}
	}
}
