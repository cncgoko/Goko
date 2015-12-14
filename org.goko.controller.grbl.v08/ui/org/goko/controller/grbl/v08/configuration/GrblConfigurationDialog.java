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
package org.goko.controller.grbl.v08.configuration;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiDialogComponent;
import org.goko.controller.grbl.v08.configuration.model.GrblConfigurationController;
import org.goko.controller.grbl.v08.configuration.model.GrblConfigurationModel;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 *
 * @author PsyKo
 *
 */
public class GrblConfigurationDialog extends GkUiDialogComponent<GrblConfigurationController, GrblConfigurationModel> {
	private static final GkLog LOG = GkLog.getLogger(GrblConfigurationDialog.class);
	private Text p0txt;	
	private Text p1txt;
	private Text p2txt;
	private Text p3txt;
	private Text p8txt;
	private Text p10txt;
	private Text p12txt;
	private Text p7txt;
	private Text p9txt;
	private Text p11txt;
	private Text p19txt;
	private Text p21txt;
	private Text p20txt;
	private Text p22txt;
	private Label errorLabel;
	private Button p13check;
	private Button p14check;
	private Button p15check;
	private Button p16check;
	private Button p17check;
	
	public GrblConfigurationDialog(Shell shell, IEclipseContext context) throws GkException {
		super(shell, new GrblConfigurationController());		
		ContextInjectionFactory.inject(getController(), context);
		getController().initialize();

	}

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
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 5;

			
			Composite titleArea = new Composite(composite, SWT.NONE);
			titleArea.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			titleArea.setLayout(new GridLayout(1, false));
			GridData gd_titleArea = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
			gd_titleArea.heightHint = 60;
			titleArea.setLayoutData(gd_titleArea);
			
			Label lblGrblConfiguration = new Label(titleArea, SWT.NONE);
			lblGrblConfiguration.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			lblGrblConfiguration.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			lblGrblConfiguration.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
			lblGrblConfiguration.setText("GRBL Configuration");
			
			errorLabel = new Label(titleArea, SWT.NONE);
			errorLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			errorLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
			
			Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Composite composite_2 = new Composite(composite, SWT.NONE);
			composite_2.setLayout(new GridLayout(1, false));
			composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			
			Group grpAxisConfiguration = new Group(composite_2, SWT.NONE);
			grpAxisConfiguration.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			grpAxisConfiguration.setLayout(new GridLayout(1, false));
			grpAxisConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			grpAxisConfiguration.setText("Axis configuration");
			
			Composite composite_1 = new Composite(grpAxisConfiguration, SWT.NONE);
			composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			composite_1.setLayout(new GridLayout(6, true));
			
			Label label_1 = new Label(composite_1, SWT.NONE);
			label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			label_1.setText("$0 (x, steps/mm)");
			
			p0txt = new Text(composite_1, SWT.BORDER);
			p0txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			p0txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			
			Label label_2 = new Label(composite_1, SWT.NONE);
			label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_2.setText("$1 (y, steps/mm)");
			
			p1txt = new Text(composite_1, SWT.BORDER);
			p1txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			p1txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			
			Label label_3 = new Label(composite_1, SWT.NONE);
			label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_3.setText("$2 (z, steps/mm)");
			
			p2txt = new Text(composite_1, SWT.BORDER);
			p2txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			p2txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			
			Composite composite_3 = new Composite(grpAxisConfiguration, SWT.NONE);
			composite_3.setLayout(new GridLayout(7, false));
			composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			
			Label label_4 = new Label(composite_3, SWT.NONE);
			label_4.setText("$6 (step port invert mask)");
			label_4.setBounds(0, 0, 134, 15);
			new Label(composite_3, SWT.NONE);
			
			Button p6xcheck = new Button(composite_3, SWT.CHECK);
			p6xcheck.setText("Invert X axis");
			new Label(composite_3, SWT.NONE);
			
			Button p6ycheck = new Button(composite_3, SWT.CHECK);
			p6ycheck.setText("Invert Y axis");
			new Label(composite_3, SWT.NONE);
			
			Button p6zcheck = new Button(composite_3, SWT.CHECK);
			p6zcheck.setText("Invert Z axis");
			
			Group grpGeneral = new Group(composite_2, SWT.NONE);
			grpGeneral.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			grpGeneral.setLayout(new GridLayout(1, false));
			grpGeneral.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			grpGeneral.setText("General");
			
			Composite composite_4 = new Composite(grpGeneral, SWT.NONE);
			composite_4.setLayout(new GridLayout(5, false));
			composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label label_5 = new Label(composite_4, SWT.NONE);
			label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_5.setText("$3 (step pulse, \u00B5sec)");
			
			p3txt = new Text(composite_4, SWT.BORDER);
			p3txt.setOrientation(SWT.LEFT_TO_RIGHT);
			p3txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p3txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label label_11 = new Label(composite_4, SWT.NONE);
			
			Label label_9 = new Label(composite_4, SWT.NONE);
			label_9.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_9.setText("$7 (step idle delay, msec)");
			
			p7txt = new Text(composite_4, SWT.BORDER);
			p7txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p7txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label label_6 = new Label(composite_4, SWT.NONE);
			label_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_6.setText("$8 (acceleration, mm/sec^2)");
			
			p8txt = new Text(composite_4, SWT.BORDER);
			p8txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p8txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			new Label(composite_4, SWT.NONE);
			
			Label label_10 = new Label(composite_4, SWT.NONE);
			label_10.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_10.setText("$9 (junction deviation, mm)");
			
			p9txt = new Text(composite_4, SWT.BORDER);
			p9txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p9txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label label_7 = new Label(composite_4, SWT.NONE);
			label_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_7.setText("$10 (arc, mm/segment)");
			
			p10txt = new Text(composite_4, SWT.BORDER);
			p10txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p10txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			new Label(composite_4, SWT.NONE);
			
			Label label_12 = new Label(composite_4, SWT.NONE);
			label_12.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_12.setText("$11 (n-arc correction, int)");
			
			p11txt = new Text(composite_4, SWT.BORDER);
			p11txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p11txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label label_8 = new Label(composite_4, SWT.NONE);
			label_8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_8.setText("$12 (n-decimals, int)");
			
			p12txt = new Text(composite_4, SWT.BORDER);
			p12txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p12txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			new Label(composite_4, SWT.NONE);
			new Label(composite_4, SWT.NONE);
			new Label(composite_4, SWT.NONE);
			
			Composite composite_5 = new Composite(grpGeneral, SWT.NONE);
			composite_5.setLayout(new GridLayout(2, false));
			composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			
			Label label_13 = new Label(composite_5, SWT.NONE);
			label_13.setText("$13 (report inches, bool)");
			
			p13check = new Button(composite_5, SWT.CHECK);
			p13check.setText("Enabled");
			
			Label label_14 = new Label(composite_5, SWT.NONE);
			label_14.setText("$14 (auto start, bool)");
			
			p14check = new Button(composite_5, SWT.CHECK);
			p14check.setText("Enabled");
			
			Label label_15 = new Label(composite_5, SWT.NONE);
			label_15.setText("$15 (invert ster enable, bool)");
			
			p15check = new Button(composite_5, SWT.CHECK);
			p15check.setText("Enabled");
			
			Group grpHoming = new Group(composite_2, SWT.NONE);
			grpHoming.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			grpHoming.setLayout(new GridLayout(1, false));
			grpHoming.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			grpHoming.setText("Homing");
			
			Composite composite_6 = new Composite(grpHoming, SWT.NONE);
			composite_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			composite_6.setLayout(new GridLayout(7, false));
			
			Label label_16 = new Label(composite_6, SWT.NONE);
			label_16.setText("$16 (hard limits)");
			
			p16check = new Button(composite_6, SWT.CHECK);
			p16check.setText("Enabled");
			new Label(composite_6, SWT.NONE);
			new Label(composite_6, SWT.NONE);
			new Label(composite_6, SWT.NONE);
			
			Label label_17 = new Label(composite_6, SWT.NONE);
			label_17.setText("$17 (homing cycle)");
			
			p17check = new Button(composite_6, SWT.CHECK);
			p17check.setText("Enabled");
			
			Composite composite_7 = new Composite(grpHoming, SWT.NONE);
			composite_7.setLayout(new GridLayout(7, false));
			composite_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			
			Label label_18 = new Label(composite_7, SWT.NONE);
			label_18.setText("$18 (homing dir invert mask)");
			new Label(composite_7, SWT.NONE);
			
			Button p18xcheck = new Button(composite_7, SWT.CHECK);
			p18xcheck.setText("Invert X axis");
			new Label(composite_7, SWT.NONE);
			
			Button p18ycheck = new Button(composite_7, SWT.CHECK);
			p18ycheck.setText("Invert Y axis");
			new Label(composite_7, SWT.NONE);
			
			Button p18zcheck = new Button(composite_7, SWT.CHECK);
			p18zcheck.setText("Invert Z axis");
			
			Composite composite_8 = new Composite(grpHoming, SWT.NONE);
			composite_8.setLayout(new GridLayout(4, false));
			composite_8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			
			Label label_19 = new Label(composite_8, SWT.NONE);
			label_19.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_19.setText("$19 (homing feed, mm/min)");
			
			p19txt = new Text(composite_8, SWT.BORDER);
			p19txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p19txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label label_21 = new Label(composite_8, SWT.NONE);
			label_21.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_21.setText("$20 (homing seek, mm/min)");
			
			p20txt = new Text(composite_8, SWT.BORDER);
			p20txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p20txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label label_20 = new Label(composite_8, SWT.NONE);
			label_20.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_20.setText("$21 (homing debounce, msec)");
			
			p21txt = new Text(composite_8, SWT.BORDER);
			p21txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p21txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label label_22 = new Label(composite_8, SWT.NONE);
			label_22.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label_22.setText("$22 (homing pull-off, mm)");
			
			p22txt = new Text(composite_8, SWT.BORDER);
			p22txt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			p22txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			try {
				initCustomDataBindings();
			} catch (GkException e1) {
				LOG.error(e1);
			}
		return composite;
	}

	protected void initCustomDataBindings() throws GkException {
		getController().addValidationMessagesBinding(errorLabel);

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
	protected Point getInitialSize() {
		return new Point(601, 608);
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
