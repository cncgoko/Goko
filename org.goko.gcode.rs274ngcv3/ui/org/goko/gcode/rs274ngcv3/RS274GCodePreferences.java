/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.gcode.rs274ngcv3;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiPreferencePageComponent;
import org.goko.core.common.exception.GkException;

public class RS274GCodePreferences extends GkUiPreferencePageComponent<RS274GCodePreferencesController, RS274GCodePreferencesModel>{
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtDecimalCount;
	private Button btnTruncateDecimal;

	/**
	 * Constructor
	 */
	public RS274GCodePreferences( ) {
		super(new RS274GCodePreferencesController());
	}

	/** (inheritDoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite control = new Composite(parent, NONE);
		control.setLayout(new GridLayout(1, false));

		Group grpGcodeParsing = new Group(control, SWT.NONE);
		grpGcodeParsing.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpGcodeParsing.setLayout(new GridLayout(1, false));
		grpGcodeParsing.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpGcodeParsing.setText("GCode parsing");
		new Label(grpGcodeParsing, SWT.NONE);

		btnTruncateDecimal = new Button(grpGcodeParsing, SWT.CHECK);
		btnTruncateDecimal.setText("Truncate decimal");

		Composite composite = new Composite(grpGcodeParsing, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);

		Label lblDecimalCount = new Label(composite, SWT.NONE);
		lblDecimalCount.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDecimalCount.setSize(83, 15);
		lblDecimalCount.setText("Decimal count :");

		txtDecimalCount = formToolkit.createText(composite, "New Text", SWT.NONE);
		GridData gd_txtDecimalCount = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtDecimalCount.widthHint = 60;
		txtDecimalCount.setLayoutData(gd_txtDecimalCount);
		GridLayout gl_control = new GridLayout(1, false);

		try {
			enableDataBindings();
		} catch (GkException e) {
			displayMessage(e);
		}
		return control;
	}

	@Override
	public boolean performOk() {
		try {
			getController().savePreferences();
		} catch (GkException e) {
			displayMessage(e);
			return false;
		}
		return super.performOk();
	}


	private void enableDataBindings() throws GkException {
		getController().addSelectionBinding(btnTruncateDecimal, "truncateEnabled");
		getController().addEnableBinding(txtDecimalCount, "truncateEnabled");
		getController().addTextModifyBinding(txtDecimalCount, "decimalCount");

	}
}
