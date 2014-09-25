/*
 *
 *   Goko
 *   Copyright (C) 2014  PsyKo
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
package org.goko.viewer.jogl;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiPreferencePageComponent;
import org.goko.common.bindings.ErrorEvent;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.viewer.jogl.model.JoglViewerPreferencesController;
import org.goko.viewer.jogl.model.JoglViewerPreferencesModel;
import org.goko.viewer.jogl.service.JoglViewerSettings.EnumRotaryAxisDirection;

public class JoglViewerPreferences extends GkUiPreferencePageComponent<JoglViewerPreferencesController, JoglViewerPreferencesModel>{
	private static final GkLog LOG = GkLog.getLogger(JoglViewerPreferences.class);
	private Text txtPositionX;
	private Text txtPositionY;
	private Text txtPositionZ;
	private Button btnEnable4axis;
	private GkCombo<LabeledValue<EnumRotaryAxisDirection>> directionCombo;
	private Text text;

	@Inject
	public JoglViewerPreferences(IEclipseContext context) {
		super(new JoglViewerPreferencesController());
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			displayError( new ErrorEvent(e, "blah"));
			LOG.error(e);
		}
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite control = new Composite(parent, NONE);
		GridLayout gl_control = new GridLayout(1, false);
		gl_control.marginWidth = 0;
		gl_control.marginHeight = 0;
		control.setLayout(gl_control);

		Group grpthAxisSettings = new Group(control, SWT.NONE);
		grpthAxisSettings.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		FormLayout fl_grpthAxisSettings = new FormLayout();
		fl_grpthAxisSettings.marginHeight = 5;
		fl_grpthAxisSettings.marginWidth = 5;
		fl_grpthAxisSettings.marginTop = 15;
		fl_grpthAxisSettings.spacing = 10;
		grpthAxisSettings.setLayout(fl_grpthAxisSettings);
		grpthAxisSettings.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		grpthAxisSettings.setText("4th Axis Settings");

		btnEnable4axis = new Button(grpthAxisSettings, SWT.CHECK);
		FormData fd_btnEnable4axis = new FormData();
		fd_btnEnable4axis.top = new FormAttachment(0);
		btnEnable4axis.setLayoutData(fd_btnEnable4axis);
		btnEnable4axis.setText("Enable rotary axis display");

		Label lblAxisDirection = new Label(grpthAxisSettings, SWT.NONE);
		FormData fd_lblAxisDirection = new FormData();
		fd_lblAxisDirection.top = new FormAttachment(btnEnable4axis,6);
		lblAxisDirection.setLayoutData(fd_lblAxisDirection);
		lblAxisDirection.setText("Rotary axis direction");

		directionCombo = new GkCombo<LabeledValue<EnumRotaryAxisDirection>>(grpthAxisSettings, SWT.NONE);
		Combo combo = directionCombo.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.width = 65;
		fd_combo.left = new FormAttachment(lblAxisDirection, 12);
		fd_combo.top = new FormAttachment(lblAxisDirection,-3, SWT.TOP);
		combo.setLayoutData(fd_combo);

		Label lblAxisPosition = new Label(grpthAxisSettings, SWT.NONE);
		lblAxisPosition.setText("Rotary axis position");
		FormData fd_lblAxisPosition = new FormData();
		fd_lblAxisPosition.top = new FormAttachment(lblAxisDirection,6);
		lblAxisPosition.setLayoutData(fd_lblAxisPosition);


		Label lblPositionX = new Label(grpthAxisSettings, SWT.NONE);
		FormData fd_lblPositionX = new FormData();
		fd_lblPositionX.top = new FormAttachment(lblAxisPosition, 0, SWT.TOP);
		fd_lblPositionX.left = new FormAttachment(lblAxisPosition);
		lblPositionX.setLayoutData(fd_lblPositionX);

		lblPositionX.setText("X");

		txtPositionX = new Text(grpthAxisSettings, SWT.BORDER);
		FormData fd_txtPositionX = new FormData();
		fd_txtPositionX.width = 80;
		fd_txtPositionX.left = new FormAttachment(lblPositionX,0, SWT.RIGHT);
		fd_txtPositionX.top = new FormAttachment(lblPositionX,-3, SWT.TOP);

		txtPositionX.setLayoutData(fd_txtPositionX);

		Label lblPositionY = new Label(grpthAxisSettings, SWT.NONE);
		FormData fd_lblPositionY = new FormData();
		fd_lblPositionY.top = new FormAttachment(lblPositionX, 0, SWT.BOTTOM);
		fd_lblPositionY.left = new FormAttachment(lblAxisPosition);
		lblPositionY.setLayoutData(fd_lblPositionY);

		lblPositionY.setText("Y");

		Label lblPositionZ = new Label(grpthAxisSettings, SWT.NONE);
		FormData fd_lblPositionZ = new FormData();
		fd_lblPositionZ.top = new FormAttachment(lblPositionY, 0, SWT.BOTTOM);
		fd_lblPositionZ.left = new FormAttachment(lblAxisPosition);
		lblPositionZ.setLayoutData(fd_lblPositionZ);
		lblPositionZ.setText("Z");

		txtPositionY = new Text(grpthAxisSettings, SWT.BORDER);
		FormData fd_txtPositionY = new FormData();
		fd_txtPositionY.width = 80;
		fd_txtPositionY.left = new FormAttachment(lblPositionY,0, SWT.RIGHT);
		fd_txtPositionY.top = new FormAttachment(lblPositionY,-3, SWT.TOP);
		txtPositionY.setLayoutData(fd_txtPositionY);

		txtPositionZ = new Text(grpthAxisSettings, SWT.BORDER);
		FormData fd_txtPositionZ = new FormData();
		fd_txtPositionZ.width = 80;
		fd_txtPositionZ.left = new FormAttachment(lblPositionZ,0, SWT.RIGHT);
		fd_txtPositionZ.top = new FormAttachment(lblPositionZ,-3, SWT.TOP);
		txtPositionZ.setLayoutData(fd_txtPositionZ);

		Label label = new Label(grpthAxisSettings, SWT.NONE);
		label.setText("Rotary axis position");
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(lblPositionZ, 3);
		fd_label.bottom = new FormAttachment(100, -10);
		fd_label.left = new FormAttachment(btnEnable4axis, 0, SWT.LEFT);
		label.setLayoutData(fd_label);

		text = new Text(grpthAxisSettings, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(label, -3, SWT.TOP);
		fd_text.width = 80;
		fd_text.left = new FormAttachment(combo, 0, SWT.LEFT);
		text.setLayoutData(fd_text);

		try {
			enableBindings();
		} catch (GkException e) {
			LOG.error(e);
		}
		return control;
	}

	/** {@inheritDoc}
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		getController().savePreferences();
		return super.performOk();
	}


	private void enableBindings() throws GkException{
		getController().addBigDecimalModifyBinding(txtPositionX, "rotaryAxisPositionX");
		getController().addBigDecimalModifyBinding(txtPositionY, "rotaryAxisPositionY");
		getController().addBigDecimalModifyBinding(txtPositionZ, "rotaryAxisPositionZ");
		getController().addSelectionBinding(btnEnable4axis, "rotaryAxisEnabled");
		getController().addItemsBinding(directionCombo, "rotaryAxisDirectionChoice");
		getController().addItemSelectionBinding(directionCombo, "rotaryAxisDirection");

		getController().addEnableBinding(txtPositionX, "rotaryAxisEnabled");
		getController().addEnableBinding(txtPositionY, "rotaryAxisEnabled");
		getController().addEnableBinding(txtPositionZ, "rotaryAxisEnabled");
		getController().addEnableBinding(directionCombo, "rotaryAxisEnabled");
	}
}
