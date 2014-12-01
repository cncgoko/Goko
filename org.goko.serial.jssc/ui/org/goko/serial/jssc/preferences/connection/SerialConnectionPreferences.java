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
package org.goko.serial.jssc.preferences.connection;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiPreferencePageComponent;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;


/**
 * Preferences page for the serial connection
 *
 * @author PsyKo
 *
 */
public class SerialConnectionPreferences extends GkUiPreferencePageComponent<SerialConnectionPreferencesController, SerialConnectionPreferencesModel> {
	private static final GkLog LOG = GkLog.getLogger(SerialConnectionPreferences.class);
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private GkCombo<LabeledValue<Integer>> gkComboStopBits;
	private GkCombo<LabeledValue<Integer>> gkComboDataBits;
	private GkCombo<LabeledValue<Integer>> gkComboParity;
	private Button btnRtscts;
	private Button btnXonxoff;
	private GkCombo<LabeledValue<Integer>> gkComboBaudrate;

	/**
	 * Constructor
	 * @param context IEclipseContext
	 */
	@Inject
	public SerialConnectionPreferences(IEclipseContext context) {
		super(new SerialConnectionPreferencesController());
		formToolkit.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		ContextInjectionFactory.inject(getController(), context);
	}


	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite control = new Composite(parent, NONE);
		GridLayout gl_control = new GridLayout(1, false);
		gl_control.marginWidth = 0;
		gl_control.marginHeight = 0;
		control.setLayout(gl_control);

		Group grpSerialPortOptions = new Group(control, SWT.NONE);
		grpSerialPortOptions.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpSerialPortOptions.setLayout(new GridLayout(2, false));
		grpSerialPortOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSerialPortOptions.setText("Serial port options");

		Label lblBaudrate = new Label(grpSerialPortOptions, SWT.NONE);
		lblBaudrate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBaudrate.setText("Baudrate");
		formToolkit.adapt(lblBaudrate, true, true);

		gkComboBaudrate = new GkCombo<LabeledValue<Integer>>(grpSerialPortOptions, SWT.NONE);
		Combo comboBaudrate = gkComboBaudrate.getCombo();
		GridData gd_comboBaudrate = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_comboBaudrate.widthHint = 100;
		comboBaudrate.setLayoutData(gd_comboBaudrate);
		formToolkit.paintBordersFor(comboBaudrate);

		Label lblNewLabel = new Label(grpSerialPortOptions, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Data bits");

		gkComboDataBits = new GkCombo<LabeledValue<Integer>>(grpSerialPortOptions, SWT.NONE);
		Combo comboDataBits = gkComboDataBits.getCombo();
		GridData gd_comboDataBits = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_comboDataBits.widthHint = 100;
		comboDataBits.setLayoutData(gd_comboDataBits);
		formToolkit.paintBordersFor(comboDataBits);

		Label lblParity = new Label(grpSerialPortOptions, SWT.NONE);
		lblParity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblParity, true, true);
		lblParity.setText("Parity");

		gkComboParity = new GkCombo<LabeledValue<Integer>>(grpSerialPortOptions, SWT.NONE);
		Combo comboParityBits = gkComboParity.getCombo();
		GridData gd_comboParityBits = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_comboParityBits.widthHint = 100;
		comboParityBits.setLayoutData(gd_comboParityBits);
		formToolkit.paintBordersFor(comboParityBits);

		Label lblStopBits = new Label(grpSerialPortOptions, SWT.NONE);
		lblStopBits.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblStopBits, true, true);
		lblStopBits.setText("Stop bits");

		gkComboStopBits = new GkCombo<LabeledValue<Integer>>(grpSerialPortOptions, SWT.NONE);
		Combo comboStopBits = gkComboStopBits.getCombo();
		GridData gd_comboStopBits = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_comboStopBits.widthHint = 100;
		comboStopBits.setLayoutData(gd_comboStopBits);
		formToolkit.paintBordersFor(comboStopBits);

		Label lblFlowControl = new Label(grpSerialPortOptions, SWT.NONE);
		formToolkit.adapt(lblFlowControl, true, true);
		lblFlowControl.setText("Flow control");

		btnRtscts = new Button(grpSerialPortOptions, SWT.CHECK);
		formToolkit.adapt(btnRtscts, true, true);
		btnRtscts.setText("RTS/CTS");
		new Label(grpSerialPortOptions, SWT.NONE);

		btnXonxoff = new Button(grpSerialPortOptions, SWT.CHECK);
		formToolkit.adapt(btnXonxoff, true, true);
		btnXonxoff.setText("XON/XOFF");
		try {
			initCustomBindings();
			getController().initialize();
		} catch (GkException e) {
			LOG.error(e);
		}

		return control;
	}

	private void initCustomBindings() throws GkException{
		getController().addItemsBinding(gkComboBaudrate, "choiceBaudrate");
		getController().addItemsBinding(gkComboDataBits, "choiceDataBits");
		getController().addItemsBinding(gkComboStopBits, "choiceStopBits");
		getController().addItemsBinding(gkComboParity, 	 "choiceParity");

		getController().addItemSelectionBinding(gkComboBaudrate, "baudrate");
		getController().addItemSelectionBinding(gkComboDataBits, "dataBits");
		getController().addItemSelectionBinding(gkComboStopBits, "stopBits");
		getController().addItemSelectionBinding(gkComboParity, 	 "parity");

		getController().addSelectionBinding(btnRtscts, 	 "rcsCts");
		getController().addSelectionBinding(btnXonxoff,  "xonXoff");
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	@Override
	protected void performApply() {
		super.performApply();
		getController().savePreferences();
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

}
