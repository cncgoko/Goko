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
package org.goko.grbl.controller.configuration;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiDialogComponent;
import org.goko.core.common.exception.GkException;
import org.goko.grbl.controller.configuration.model.GrblConfigurationController;
import org.goko.grbl.controller.configuration.model.GrblConfigurationModel;

/**
 *
 * @author PsyKo
 *
 */
public class GrblConfigurationDialog extends GkUiDialogComponent<GrblConfigurationController, GrblConfigurationModel> {
	private Shell shlGrblConfiguration;
	private final FormToolkit formToolkit = new FormToolkit( Display.getDefault());
	private Text p0txt;
	private Text p1txt;
	private Text p2txt;
	private Text p3txt;
	private Text p7txt;
	private Text p8txt;
	private Text p19txt;
	private Text p20txt;
	private Text p21txt;
	private Text p22txt;
	private Text p9txt;
	private Text p10txt;
	private Text p12txt;
	private Text p11txt;
	private CLabel labelValidationMessages;
	private Button p13check;
	private Button p14check;
	private Button p15check;
	private Button p16check;
	private Button p18xcheck;
	private Button p18ycheck;
	private Button p18zcheck;
	private Button p17check;
	private Button p6xcheck;
	private Button p6ycheck;
	private Button p6zcheck;

	public GrblConfigurationDialog(Shell shell, IEclipseContext context) throws GkException {
		super(shell, new GrblConfigurationController());
		ContextInjectionFactory.inject(getController(), context);
		getController().initialize();

	}

	/*public Object open() {
		createContents();

		shlGrblConfiguration.open();
		shlGrblConfiguration.layout();
		Display display = getParent().getDisplay();
		while (!shlGrblConfiguration.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return true;
	}*/

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Grbl Configuration");
	}
	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				close();
			}
		});
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) composite.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 5;
		gridLayout.horizontalSpacing = 5;

		try {
			createControls(composite);
		} catch (GkException e) {
			displayMessage(e);
		}
		return composite;
	}


	public void createControls(Composite parent) throws GkException {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.horizontalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);

		Form frmGrblConfiguration = formToolkit.createForm(composite);
		frmGrblConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(frmGrblConfiguration);
		frmGrblConfiguration.setText("Grbl configuration");

		frmGrblConfiguration.getBody().setLayout(new GridLayout(1, false));

		CTabFolder tabFolder = new CTabFolder(frmGrblConfiguration.getBody(),SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		formToolkit.adapt(tabFolder);
		formToolkit.paintBordersFor(tabFolder);



		CTabItem tbtmGrblBoard = new CTabItem(tabFolder, SWT.NONE);
		tbtmGrblBoard.setText("Grbl Board");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmGrblBoard.setControl(composite_1);
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.marginWidth = 10;
		gl_composite_1.verticalSpacing = 10;
		gl_composite_1.marginHeight = 10;
		gl_composite_1.marginRight = 5;
		composite_1.setLayout(gl_composite_1);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);

		Section sctnAxisConfiguration = formToolkit.createSection(composite_1, Section.TWISTIE | Section.TITLE_BAR);
		sctnAxisConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnAxisConfiguration);
		sctnAxisConfiguration.setText("Axis configuration");
		sctnAxisConfiguration.setExpanded(true);

		Composite grpAxisConfiguration = new Composite(sctnAxisConfiguration, SWT.NONE);
		sctnAxisConfiguration.setClient(grpAxisConfiguration);
		grpAxisConfiguration.setFont(SWTResourceManager.getFont("Segoe UI", 9,
				SWT.BOLD));
		formToolkit.adapt(grpAxisConfiguration);
		formToolkit.paintBordersFor(grpAxisConfiguration);
		GridLayout gl_grpAxisConfiguration = new GridLayout(1, false);
		gl_grpAxisConfiguration.horizontalSpacing = 0;
		grpAxisConfiguration.setLayout(gl_grpAxisConfiguration);

		Composite composite_2 = new Composite(grpAxisConfiguration, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		composite_2.setLayout(new GridLayout(6, false));

		Label lblstepsmm = new Label(composite_2, SWT.NONE);
		lblstepsmm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(lblstepsmm, true, true);
		lblstepsmm.setText("$0 (x, steps/mm)");

		p0txt = new Text(composite_2, SWT.BORDER);
		p0txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p0txt = new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1);
		gd_p0txt.widthHint = 80;
		p0txt.setLayoutData(gd_p0txt);
		formToolkit.adapt(p0txt, true, true);

		Label lblyStepsmm = new Label(composite_2, SWT.NONE);
		lblyStepsmm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(lblyStepsmm, true, true);
		lblyStepsmm.setText("$1 (y, steps/mm)");

		p1txt = new Text(composite_2, SWT.BORDER);
		p1txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p1txt = new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1);
		gd_p1txt.widthHint = 80;
		p1txt.setLayoutData(gd_p1txt);
		formToolkit.adapt(p1txt, true, true);

		Label lblzStepsmm = new Label(composite_2, SWT.NONE);
		lblzStepsmm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(lblzStepsmm, true, true);
		lblzStepsmm.setText("$2 (z, steps/mm)");

		p2txt = new Text(composite_2, SWT.BORDER);
		p2txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p2txt = new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1);
		gd_p2txt.widthHint = 80;
		p2txt.setLayoutData(gd_p2txt);
		formToolkit.adapt(p2txt, true, true);

		Composite composite_3 = new Composite(grpAxisConfiguration, SWT.NONE);
		composite_3.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		composite_3.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.adapt(composite_3);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(7, false));

		Label lblstepPort = new Label(composite_3, SWT.NONE);
		formToolkit.adapt(lblstepPort, true, true);
		lblstepPort.setText("$6 (step port invert mask)");
		new Label(composite_3, SWT.NONE);

		p6xcheck = new Button(composite_3, SWT.CHECK);
		formToolkit.adapt(p6xcheck, true, true);
		p6xcheck.setText("Invert X axis");
		new Label(composite_3, SWT.NONE);

		p6ycheck = new Button(composite_3, SWT.CHECK);
		p6ycheck.setText("Invert Y axis");
		formToolkit.adapt(p6ycheck, true, true);
		new Label(composite_3, SWT.NONE);

		p6zcheck = new Button(composite_3, SWT.CHECK);
		p6zcheck.setText("Invert Z axis");
		formToolkit.adapt(p6zcheck, true, true);

		Section sctnGeneral = formToolkit.createSection(composite_1, Section.TWISTIE | Section.TITLE_BAR);
		sctnGeneral.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.paintBordersFor(sctnGeneral);
		sctnGeneral.setText("General");
		sctnGeneral.setExpanded(true);

		Composite grpGeneral = new Composite(sctnGeneral, SWT.NONE);
		sctnGeneral.setClient(grpGeneral);
		grpGeneral.setLayout(new GridLayout(1, false));
		grpGeneral.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		formToolkit.adapt(grpGeneral);
		formToolkit.paintBordersFor(grpGeneral);

		Composite composite_4 = new Composite(grpGeneral, SWT.NONE);
		composite_4.setLayout(new GridLayout(5, false));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.adapt(composite_4);
		formToolkit.paintBordersFor(composite_4);

		Label lblstepPulse = new Label(composite_4, SWT.NONE);
		lblstepPulse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(lblstepPulse, true, true);
		lblstepPulse.setText("$3 (step pulse, \u00B5sec)");

		p3txt = new Text(composite_4, SWT.BORDER);
		p3txt.setOrientation(SWT.LEFT_TO_RIGHT);
		p3txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p3txt = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1);
		gd_p3txt.widthHint = 100;
		p3txt.setLayoutData(gd_p3txt);
		formToolkit.adapt(p3txt, true, true);
		new Label(composite_4, SWT.NONE);

		Label lblstepIdle = new Label(composite_4, SWT.NONE);
		lblstepIdle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(lblstepIdle, true, true);
		lblstepIdle.setText("$7 (step idle delay, msec)");

		p7txt = new Text(composite_4, SWT.BORDER);
		p7txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p7txt = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1);
		gd_p7txt.widthHint = 100;
		p7txt.setLayoutData(gd_p7txt);
		formToolkit.adapt(p7txt, true, true);

		Label lblaccelerationMmsec = new Label(composite_4, SWT.NONE);
		lblaccelerationMmsec.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		formToolkit.adapt(lblaccelerationMmsec, true, true);
		lblaccelerationMmsec.setText("$8 (acceleration, mm/sec^2)");

		p8txt = new Text(composite_4, SWT.BORDER);
		p8txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p8txt = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1);
		gd_p8txt.widthHint = 100;
		p8txt.setLayoutData(gd_p8txt);
		formToolkit.adapt(p8txt, true, true);
		new Label(composite_4, SWT.NONE);

		Label lbljunctionDeviation = new Label(composite_4, SWT.NONE);
		lbljunctionDeviation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		formToolkit.adapt(lbljunctionDeviation, true, true);
		lbljunctionDeviation.setText("$9 (junction deviation, mm)");

		p9txt = new Text(composite_4, SWT.BORDER);
		p9txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p9txt = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_p9txt.widthHint = 100;
		p9txt.setLayoutData(gd_p9txt);
		formToolkit.adapt(p9txt, true, true);

		Label lblarcMmsegment = new Label(composite_4, SWT.NONE);
		lblarcMmsegment.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblarcMmsegment.setText("$10 (arc, mm/segment)");
		formToolkit.adapt(lblarcMmsegment, true, true);

		p10txt = new Text(composite_4, SWT.BORDER);
		p10txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p10txt = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_p10txt.widthHint = 100;
		p10txt.setLayoutData(gd_p10txt);
		formToolkit.adapt(p10txt, true, true);
		new Label(composite_4, SWT.NONE);

		Label lblnarcCorrection = new Label(composite_4, SWT.NONE);
		lblnarcCorrection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblnarcCorrection.setText("$11 (n-arc correction, int)");
		formToolkit.adapt(lblnarcCorrection, true, true);

		p11txt = new Text(composite_4, SWT.BORDER);
		p11txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p11txt = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_p11txt.widthHint = 100;
		p11txt.setLayoutData(gd_p11txt);
		formToolkit.adapt(p11txt, true, true);

		Label lblndecimalsInt = new Label(composite_4, SWT.NONE);
		lblndecimalsInt.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblndecimalsInt.setText("$12 (n-decimals, int)");
		formToolkit.adapt(lblndecimalsInt, true, true);

		p12txt = new Text(composite_4, SWT.BORDER);
		p12txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p12txt = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_p12txt.widthHint = 100;
		p12txt.setLayoutData(gd_p12txt);
		formToolkit.adapt(p12txt, true, true);
		new Label(composite_4, SWT.NONE);
		new Label(composite_4, SWT.NONE);
		new Label(composite_4, SWT.NONE);

		Composite composite_8 = new Composite(grpGeneral, SWT.NONE);
		formToolkit.adapt(composite_8);
		formToolkit.paintBordersFor(composite_8);
		composite_8.setLayout(new GridLayout(3, false));

		Label lblreportInches = new Label(composite_8, SWT.NONE);
		lblreportInches.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblreportInches.setBounds(0, 0, 55, 15);
		formToolkit.adapt(lblreportInches, true, true);
		lblreportInches.setText("$13 (report inches, bool)");
		new Label(composite_8, SWT.NONE);

		p13check = new Button(composite_8, SWT.CHECK);
		p13check.setText("Enabled");
		formToolkit.adapt(p13check, true, true);

		Label lblautoStart = new Label(composite_8, SWT.NONE);
		lblautoStart.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblautoStart.setText("$14 (auto start, bool)");
		formToolkit.adapt(lblautoStart, true, true);
		new Label(composite_8, SWT.NONE);

		p14check = new Button(composite_8, SWT.CHECK);
		p14check.setText("Enabled");
		formToolkit.adapt(p14check, true, true);

		Label lblinvertSter = new Label(composite_8, SWT.NONE);
		lblinvertSter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblinvertSter.setText("$15 (invert ster enable, bool)");
		formToolkit.adapt(lblinvertSter, true, true);
		new Label(composite_8, SWT.NONE);

		p15check = new Button(composite_8, SWT.CHECK);
		p15check.setText("Enabled");
		formToolkit.adapt(p15check, true, true);

		Section sctnHoming = formToolkit.createSection(composite_1, Section.TWISTIE | Section.TITLE_BAR);
		sctnHoming.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnHoming);
		sctnHoming.setText("Homing");
		sctnHoming.setExpanded(true);

		Composite grpHoming = new Composite(sctnHoming, SWT.NONE);
		sctnHoming.setClient(grpHoming);
		grpHoming.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpHoming.setLayout(new GridLayout(1, false));
		formToolkit.adapt(grpHoming);
		formToolkit.paintBordersFor(grpHoming);

		Composite composite_5 = new Composite(grpHoming, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		formToolkit.adapt(composite_5);
		formToolkit.paintBordersFor(composite_5);
		composite_5.setLayout(new GridLayout(8, false));

		Label lblhardLimits = new Label(composite_5, SWT.NONE);
		formToolkit.adapt(lblhardLimits, true, true);
		lblhardLimits.setText("$16 (hard limits)");
		new Label(composite_5, SWT.NONE);

		p16check = new Button(composite_5, SWT.CHECK);
		formToolkit.adapt(p16check, true, true);
		p16check.setText("Enabled");
		new Label(composite_5, SWT.NONE);
								new Label(composite_5, SWT.NONE);
								new Label(composite_5, SWT.NONE);

								Label lblhomingCycle = new Label(composite_5, SWT.NONE);
								formToolkit.adapt(lblhomingCycle, true, true);
								lblhomingCycle.setText("$17 (homing cycle)");

				p17check = new Button(composite_5, SWT.CHECK);
				p17check.setText("Enabled");
				formToolkit.adapt(p17check, true, true);

		Composite composite_6 = new Composite(grpHoming, SWT.NONE);
		composite_6.setLayout(new GridLayout(7, false));
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.adapt(composite_6);
		formToolkit.paintBordersFor(composite_6);

		Label lblhomingDir = new Label(composite_6, SWT.NONE);
		formToolkit.adapt(lblhomingDir, true, true);
		lblhomingDir.setText("$18 (homing dir invert mask)");
		new Label(composite_6, SWT.NONE);

		p18xcheck = new Button(composite_6, SWT.CHECK);
		p18xcheck.setText("Invert X axis");
		formToolkit.adapt(p18xcheck, true, true);
		new Label(composite_6, SWT.NONE);

		p18ycheck = new Button(composite_6, SWT.CHECK);
		p18ycheck.setText("Invert Y axis");
		formToolkit.adapt(p18ycheck, true, true);
		new Label(composite_6, SWT.NONE);

		p18zcheck = new Button(composite_6, SWT.CHECK);
		p18zcheck.setText("Invert Z axis");
		formToolkit.adapt(p18zcheck, true, true);

		Composite composite_7 = new Composite(grpHoming, SWT.NONE);
		composite_7.setLayout(new GridLayout(5, false));
		composite_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.adapt(composite_7);
		formToolkit.paintBordersFor(composite_7);

		Label lblhomingFeed = new Label(composite_7, SWT.NONE);
		lblhomingFeed.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(lblhomingFeed, true, true);
		lblhomingFeed.setText("$19 (homing feed, mm/min)");

		p19txt = new Text(composite_7, SWT.BORDER);
		p19txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p19txt = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_p19txt.widthHint = 100;
		p19txt.setLayoutData(gd_p19txt);
		formToolkit.adapt(p19txt, true, true);
		new Label(composite_7, SWT.NONE);

		Label lblhomingSeek = new Label(composite_7, SWT.NONE);
		lblhomingSeek.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblhomingSeek.setText("$20 (homing seek, mm/min)");
		formToolkit.adapt(lblhomingSeek, true, true);

		p20txt = new Text(composite_7, SWT.BORDER);
		p20txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p20txt = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_p20txt.widthHint = 100;
		p20txt.setLayoutData(gd_p20txt);
		formToolkit.adapt(p20txt, true, true);

		Label lblhomingDebounce = new Label(composite_7, SWT.NONE);
		lblhomingDebounce.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		formToolkit.adapt(lblhomingDebounce, true, true);
		lblhomingDebounce.setText("$21 (homing debounce, msec)");

		p21txt = new Text(composite_7, SWT.BORDER);
		p21txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p21txt = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_p21txt.widthHint = 100;
		p21txt.setLayoutData(gd_p21txt);
		formToolkit.adapt(p21txt, true, true);
		new Label(composite_7, SWT.NONE);

		Label lblhomingPulloff = new Label(composite_7, SWT.NONE);
		lblhomingPulloff.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblhomingPulloff.setText("$22 (homing pull-off, mm)");
		formToolkit.adapt(lblhomingPulloff, true, true);

		p22txt = new Text(composite_7, SWT.BORDER);
		p22txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_p22txt = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_p22txt.widthHint = 100;
		p22txt.setLayoutData(gd_p22txt);
		formToolkit.adapt(p22txt, true, true);

		labelValidationMessages = new CLabel(frmGrblConfiguration.getHead(), SWT.NONE);

		labelValidationMessages.setImage(ResourceManager.getPluginImage("org.goko.core", "icons/cross-circle.png"));
		frmGrblConfiguration.setHeadClient(labelValidationMessages);
		formToolkit.adapt(labelValidationMessages, true, true);
		try {
			initCustomDataBindings();
		} catch (GkException e) {
			displayMessage(e);
		}
	}

	protected void initCustomDataBindings() throws GkException {
		getController().addValidationMessagesBinding(labelValidationMessages);

		getController().addDoubleModifyBinding(p0txt, "valueParam0");
		getController().addDoubleModifyBinding(p1txt, "valueParam1");
		getController().addDoubleModifyBinding(p2txt, "valueParam2");
		getController().addIntegerModifyBinding(p3txt, "valueParam3");
		getController().addIntegerModifyBinding(p7txt, "valueParam7");
		getController().addDoubleModifyBinding(p8txt, "valueParam8");
		getController().addDoubleModifyBinding(p9txt, "valueParam9");
		getController().addDoubleModifyBinding(p10txt, "valueParam10");
		getController().addDoubleModifyBinding(p11txt, "valueParam11");
		getController().addIntegerModifyBinding(p12txt, "valueParam12");

		getController().addSelectionBinding(p13check, "valueParam13");
		getController().addSelectionBinding(p14check, "valueParam14");
		getController().addSelectionBinding(p15check, "valueParam15");
		getController().addSelectionBinding(p16check, "valueParam16");
		getController().addSelectionBinding(p17check, "valueParam17");

		getController().addDoubleModifyBinding(p19txt, "valueParam19");
		getController().addDoubleModifyBinding(p20txt, "valueParam20");
		getController().addDoubleModifyBinding(p21txt, "valueParam21");
		getController().addDoubleModifyBinding(p22txt, "valueParam22");
	}


	@Override
	protected Point getInitialSize() {
		return new Point(650, 733);
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		super.okPressed();
		getController().applyChange();
	}
}
