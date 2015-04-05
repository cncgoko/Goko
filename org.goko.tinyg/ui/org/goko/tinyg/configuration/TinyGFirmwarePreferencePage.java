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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiPreferencePageComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.tinyg.configuration.bindings.TinyGFirmwarePreferencePageController;
import org.goko.tinyg.configuration.bindings.TinyGFirmwarePreferencePageModel;

public class TinyGFirmwarePreferencePage extends GkUiPreferencePageComponent<TinyGFirmwarePreferencePageController, TinyGFirmwarePreferencePageModel> {
	private static final GkLog LOG = GkLog.getLogger(TinyGFirmwarePreferencePage.class);
	private Button btnCheckXAxis;
	private Button btnCheckYAxis;
	private Button btnCheckZAxis;
	private Button btnCheckAAxis;

	@Inject
	public TinyGFirmwarePreferencePage(IEclipseContext context) {
		super("TinyG", new TinyGFirmwarePreferencePageController() );
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
		GridLayout gl_control = new GridLayout(1, false);
		gl_control.marginWidth = 0;
		gl_control.marginHeight = 0;
		control.setLayout(gl_control);
		
		Composite composite = new Composite(control, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpHoming = new Group(composite, SWT.NONE);
		grpHoming.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		FormLayout fl_grpHoming = new FormLayout();
		fl_grpHoming.marginBottom = 5;
		fl_grpHoming.marginWidth = 5;
		fl_grpHoming.marginTop = 15;
		grpHoming.setLayout(fl_grpHoming);
		grpHoming.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpHoming.setText("Homing");
		
		Label lblNewLabel = new Label(grpHoming, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0);
		fd_lblNewLabel.left = new FormAttachment(0);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("Homing enabled axes");
		
		btnCheckXAxis = new Button(grpHoming, SWT.CHECK);
		FormData fd_btnCheckXAxis = new FormData();
		fd_btnCheckXAxis.top = new FormAttachment(lblNewLabel, 4);
		btnCheckXAxis.setLayoutData(fd_btnCheckXAxis);
		btnCheckXAxis.setText("X axis");
		
		btnCheckYAxis = new Button(grpHoming, SWT.CHECK);
		FormData fd_btnCheckYAxis = new FormData();
		fd_btnCheckYAxis.bottom = new FormAttachment(btnCheckXAxis, 0, SWT.BOTTOM);
		fd_btnCheckYAxis.left = new FormAttachment(btnCheckXAxis, 25);
		btnCheckYAxis.setLayoutData(fd_btnCheckYAxis);
		btnCheckYAxis.setText("Y axis");
		
		btnCheckZAxis = new Button(grpHoming, SWT.CHECK);
		FormData fd_btnCheckZAxis = new FormData();
		fd_btnCheckZAxis.top = new FormAttachment(btnCheckXAxis, 0, SWT.TOP);
		fd_btnCheckZAxis.left = new FormAttachment(btnCheckYAxis, 25);
		btnCheckZAxis.setLayoutData(fd_btnCheckZAxis);
		btnCheckZAxis.setText("Z axis");
		
		btnCheckAAxis = new Button(grpHoming, SWT.CHECK);
		btnCheckAAxis.setText("A axis");
		FormData fd_btnCheckAAxis = new FormData();
		fd_btnCheckAAxis.left = new FormAttachment(btnCheckZAxis, 25);
		fd_btnCheckAAxis.top = new FormAttachment(btnCheckXAxis, 0, SWT.TOP);
		btnCheckAAxis.setLayoutData(fd_btnCheckAAxis);
		try {
			initCustomBindings();
			getController().initialize();
		} catch (GkException e) {
			LOG.error(e);
		}
		return control;
	}

	private void initCustomBindings() throws GkException{
		getController().addSelectionBinding(btnCheckXAxis, "homingEnabledAxisX");
		getController().addSelectionBinding(btnCheckYAxis, "homingEnabledAxisY");
		getController().addSelectionBinding(btnCheckZAxis, "homingEnabledAxisZ");
		getController().addSelectionBinding(btnCheckAAxis, "homingEnabledAxisA");
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	@Override
	protected void performApply() {		
		try {
			getController().performApply();
		} catch (GkException e) {
			displayMessage(e);
		}
	}
	
	@Override
	public boolean performOk() {
		performApply();
		return super.performOk();
	}
}
