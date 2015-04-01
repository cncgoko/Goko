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

package org.goko.viewer.jogl.preferences.performances;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import org.goko.core.log.GkLog;

/**
 * Jogl performances settings
 *
 * @author PsyKo
 *
 */
public class JoglPerformancesPreferences extends GkUiPreferencePageComponent<JoglPerformancesPreferencesController, JoglPerformancesPreferencesModel>{
	private static final GkLog LOG = GkLog.getLogger(JoglPerformancesPreferences.class);
	/** Combo for multisampling selection */
	private GkCombo<LabeledValue<Integer>> multisamplingCombo;
	private Text txtMajorGrid;
	private Text txtMinorGrid;
	private Label lblMajorGridUnit;
	private Label lblMinorGridUnit;

	/**
	 * Constructor
	 */
	public JoglPerformancesPreferences() {
		super(new JoglPerformancesPreferencesController());
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
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite control = new Composite(parent, NONE);
		control.setLayout(new GridLayout(1, false));

		Group grpPerformances = new Group(control, SWT.NONE);
		grpPerformances.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpPerformances.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpPerformances.setText("Performances");
		FormLayout fl_grpPerformances = new FormLayout();
		fl_grpPerformances.marginWidth = 5;
		fl_grpPerformances.marginHeight = 5;
		fl_grpPerformances.marginTop = 15;
		grpPerformances.setLayout(fl_grpPerformances);

		Label lblMultisampling = new Label(grpPerformances, SWT.NONE);
		FormData fd_lblMultisampling = new FormData();
		fd_lblMultisampling.top = new FormAttachment(0);
		fd_lblMultisampling.left = new FormAttachment(0);
		lblMultisampling.setLayoutData(fd_lblMultisampling);
		lblMultisampling.setText("Multisampling :");

		multisamplingCombo = new GkCombo<LabeledValue<Integer>>(grpPerformances, SWT.NONE | SWT.READ_ONLY);
		Combo combo = multisamplingCombo.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.left = new FormAttachment(lblMultisampling, 12);
		fd_combo.top = new FormAttachment(0, -3);
		fd_combo.width = 65;
		combo.setLayoutData(fd_combo);

		Group grpGrid = new Group(control, SWT.NONE);
		FormLayout fl_grpGrid = new FormLayout();
		fl_grpGrid.marginWidth = 5;
		fl_grpGrid.marginTop = 15;
		fl_grpGrid.marginHeight = 5;
		grpGrid.setLayout(fl_grpGrid);
		grpGrid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpGrid.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpGrid.setText("Grid");

		Label lblMajorGrid = new Label(grpGrid, SWT.NONE);
		FormData fd_lblMajorGrid = new FormData();
		fd_lblMajorGrid.top = new FormAttachment(0);
		fd_lblMajorGrid.left = new FormAttachment(0);
		lblMajorGrid.setLayoutData(fd_lblMajorGrid);
		lblMajorGrid.setText("Major grid :");

		txtMajorGrid = new Text(grpGrid, SWT.BORDER | SWT.RIGHT);
		FormData fd_txtMajorGrid = new FormData();
		fd_txtMajorGrid.width = 40;
		fd_txtMajorGrid.left = new FormAttachment(lblMajorGrid, 12);
		fd_txtMajorGrid.top = new FormAttachment(0, -3);
		txtMajorGrid.setLayoutData(fd_txtMajorGrid);

		lblMajorGridUnit = new Label(grpGrid, SWT.NONE);
		FormData fd_lblMajorGridUnit = new FormData();
		fd_lblMajorGridUnit.bottom = new FormAttachment(lblMajorGrid, 0, SWT.BOTTOM);
		fd_lblMajorGridUnit.left = new FormAttachment(txtMajorGrid, 6);
		lblMajorGridUnit.setLayoutData(fd_lblMajorGridUnit);
		lblMajorGridUnit.setText("New Label");

		Label lblMinorGrid = new Label(grpGrid, SWT.NONE);
		lblMinorGrid.setText("Minor grid :");
		FormData fd_lblMinorGrid = new FormData();
		fd_lblMinorGrid.right = new FormAttachment(lblMajorGrid, 0, SWT.RIGHT);
		fd_lblMinorGrid.top = new FormAttachment(lblMajorGrid, 12);
		lblMinorGrid.setLayoutData(fd_lblMinorGrid);

		txtMinorGrid = new Text(grpGrid, SWT.BORDER | SWT.RIGHT);
		FormData fd_txtMinorGrid = new FormData();
		fd_txtMinorGrid.width = 40;
		fd_txtMinorGrid.top = new FormAttachment(lblMinorGrid, -3, SWT.TOP);
		fd_txtMinorGrid.left = new FormAttachment(lblMinorGrid, 12);
		txtMinorGrid.setLayoutData(fd_txtMinorGrid);

		lblMinorGridUnit = new Label(grpGrid, SWT.NONE);
		FormData fd_lblMinorGridUnit = new FormData();
		fd_lblMinorGridUnit.bottom = new FormAttachment(lblMinorGrid, 0, SWT.BOTTOM);
		fd_lblMinorGridUnit.left = new FormAttachment(lblMajorGridUnit, 0, SWT.LEFT);
		lblMinorGridUnit.setLayoutData(fd_lblMinorGridUnit);
		lblMinorGridUnit.setText("New Label");
		try {
			enableBindings();
		} catch (GkException e) {
			LOG.error(e);
		}
		return control;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		getController().savePreferences();
		return super.performOk();
	}

	private void enableBindings() throws GkException{
		getController().addItemsBinding(multisamplingCombo, "multisamplingChoice");
		getController().addItemSelectionBinding(multisamplingCombo, "multisampling");

		getController().addTextDisplayBinding(lblMinorGridUnit, "units");
		getController().addTextDisplayBinding(lblMajorGridUnit, "units");

		getController().addBigDecimalModifyBinding(txtMajorGrid, "majorGridSpacing");
		getController().addBigDecimalModifyBinding(txtMinorGrid, "minorGridSpacing");
	}
}
