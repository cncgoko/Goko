/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.preferences.units;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiPreferencePageComponent;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.EnumGokoUnit;
import org.goko.core.log.GkLog;

/**
 * Goko general units configuration page
 *
 * @author PsyKo
 *
 */
public class GokoUnitsPreferences extends GkUiPreferencePageComponent<GokoUnitsPreferencesController, GokoUnitsPreferencesModel>{
	private static final GkLog LOG = GkLog.getLogger(GokoUnitsPreferences.class);
	/** The number of decimal to display */
	private Text decimalCount;
	/** The unit of length */
	private GkCombo<LabeledValue<EnumGokoUnit>>  lengthCombo;

	/** Constructor */
	public GokoUnitsPreferences() {
		super(new GokoUnitsPreferencesController());
		try {
			getController().initialize();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite control = new Composite(parent, NONE);
		control.setLayout(new FillLayout(SWT.HORIZONTAL));

		Group grpUnits = new Group(control, SWT.NONE);
		grpUnits.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpUnits.setText("Units");
		FormLayout fl_grpUnits = new FormLayout();
		fl_grpUnits.marginTop = 15;
		fl_grpUnits.marginWidth = 5;
		fl_grpUnits.marginHeight = 5;
		grpUnits.setLayout(fl_grpUnits);

		Label lengthLabel = new Label(grpUnits, SWT.NONE);
		FormData fd_lengthLabel = new FormData();
		fd_lengthLabel.top = new FormAttachment(0);
		fd_lengthLabel.left = new FormAttachment(0);
		lengthLabel.setLayoutData(fd_lengthLabel);
		lengthLabel.setText("Length");


		lengthCombo = new GkCombo<LabeledValue<EnumGokoUnit>>(grpUnits, SWT.NONE | SWT.READ_ONLY);
		Combo combo = lengthCombo.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.width = 65;
		fd_combo.left = new FormAttachment(lengthLabel, 12);
		fd_combo.top = new FormAttachment(lengthLabel, -3, SWT.TOP);
		combo.setLayoutData(fd_combo);

		Label decimalsLabel = new Label(grpUnits, SWT.NONE);
		FormData fd_decimalsLabel = new FormData();
		fd_decimalsLabel.top = new FormAttachment(combo, 15);
		fd_decimalsLabel.left = new FormAttachment(0);
		decimalsLabel.setLayoutData(fd_decimalsLabel);
		decimalsLabel.setText("Decimal");

		decimalCount = new Text(grpUnits, SWT.BORDER);
		FormData fd_decimalCount = new FormData();
		fd_decimalCount.width = 35;
		fd_decimalCount.top = new FormAttachment(decimalsLabel, -3, SWT.TOP);
		fd_decimalCount.left = new FormAttachment(combo, 0, SWT.LEFT);
		decimalCount.setLayoutData(fd_decimalCount);
		try {
			enableBindings();
		} catch (GkException e) {
			LOG.error(e);
		}
		return control;
	}

	private void enableBindings() throws GkException{
		getController().addIntegerModifyBinding(decimalCount, "decimalCount");
		getController().addItemsBinding(lengthCombo, "lengthUnitChoice");
		getController().addItemSelectionBinding(lengthCombo, "lengthUnit");
	}

	@Override
	public boolean performOk() {
		try {
			getController().savePreferences();
		} catch (GkException e) {
			LOG.error(e);
		}
		return super.performOk();
	}
	@Override
	protected void performApply() {
		try {
			getController().savePreferences();
		} catch (GkException e) {
			LOG.error(e);
		}
		super.performApply();
	}
}
