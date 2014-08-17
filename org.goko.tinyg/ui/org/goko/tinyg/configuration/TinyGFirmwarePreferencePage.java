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
package org.goko.tinyg.configuration;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.goko.common.GkUiPreferencePageComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.tinyg.configuration.bindings.TinyGFirmwarePreferencePageController;
import org.goko.tinyg.configuration.bindings.TinyGFirmwarePreferencePageModel;

public class TinyGFirmwarePreferencePage extends GkUiPreferencePageComponent<TinyGFirmwarePreferencePageController, TinyGFirmwarePreferencePageModel> {
	private static final GkLog LOG = GkLog.getLogger(TinyGFirmwarePreferencePage.class);
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFirmwareVersion;

	@Inject
	public TinyGFirmwarePreferencePage(IEclipseContext context) {
		super("TinyG", new TinyGFirmwarePreferencePageController() );
		setTitle("TinyG Firmware");
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected Control createContents(Composite parent) {
		Composite control = new Composite(parent, NONE);
		control.setLayout(new GridLayout(2, false));

		Label lblFirmware = new Label(control, SWT.NONE);
		lblFirmware.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFirmware.setText("Firmware");

		txtFirmwareVersion = formToolkit.createText(control, "New Text", SWT.NONE);
		GridData gd_txtFirmwareVersion = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtFirmwareVersion.widthHint = 100;
		txtFirmwareVersion.setLayoutData(gd_txtFirmwareVersion);
		try {
			initCustomBindings();
			getController().initialize();
		} catch (GkException e) {
			LOG.error(e);
		}
		return control;
	}

	private void initCustomBindings() throws GkException{
		getController().addBigDecimalModifyBinding(txtFirmwareVersion, "firmwareVersion");
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	@Override
	protected void performApply() {
		try {
			getController().performApply();
		} catch (GkException e) {
			LOG.error(e);
		}
	}
}
