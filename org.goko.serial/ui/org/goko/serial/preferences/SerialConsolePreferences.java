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
package org.goko.serial.preferences;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.internal.SerialActivator;

public class SerialConsolePreferences extends PreferencePage implements IWorkbenchPreferencePage {
	private Text txtConsoleBufferSize;
	private Button btnLimitConsoleOutput;
	public static final String SERIAL_CONSOLE_MAX_CHARACTER = "org.goko.serial.console.maxCharCount";
	public static final String SERIAL_CONSOLE_LIMIT_CHARACTER = "org.goko.serial.console.limitConsole";

	/**
	 * Create the preference page.
	 */
	public SerialConsolePreferences() {
		setDescription("Serial console settings.");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("org.goko.serial", "icons/terminal-medium.png"));
		setTitle("Serial console");

	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		btnLimitConsoleOutput = new Button(composite, SWT.CHECK);
		btnLimitConsoleOutput.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateMaxCharInputField();
			}
		});
		btnLimitConsoleOutput.setText("Limit console output");
		new Label(composite, SWT.NONE);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Console buffer size (characters) :");

		txtConsoleBufferSize = new Text(composite, SWT.BORDER);
		txtConsoleBufferSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		init();
		return container;
	}

	protected void updateMaxCharInputField() {
		if(btnLimitConsoleOutput.getSelection()){
			String maxCharPref = String.valueOf(SerialActivator.getPreferenceStore().getInt(SERIAL_CONSOLE_MAX_CHARACTER));

			txtConsoleBufferSize.setText(maxCharPref);
			txtConsoleBufferSize.setEnabled(true);
		}else{
			txtConsoleBufferSize.setText(StringUtils.EMPTY);
			txtConsoleBufferSize.setEnabled(false);
		}
	}

	protected void init(){
		btnLimitConsoleOutput.setSelection( SerialActivator.getPreferenceStore().getBoolean(SERIAL_CONSOLE_LIMIT_CHARACTER) );
		updateMaxCharInputField();
	}
	/**
	 * Initialize the preference page.
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore( SerialActivator.getPreferenceStore() );

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		super.performOk();
		setPreferenceStore( SerialActivator.getPreferenceStore() );
		if(btnLimitConsoleOutput != null){
			getPreferenceStore().setValue(SERIAL_CONSOLE_LIMIT_CHARACTER, btnLimitConsoleOutput.getSelection() );

			if(btnLimitConsoleOutput.getSelection()){
				int maxChar = Integer.valueOf(txtConsoleBufferSize.getText()) ;
				getPreferenceStore().setValue(SERIAL_CONSOLE_MAX_CHARACTER, maxChar );
			}
		}
		return true;
	}
}
