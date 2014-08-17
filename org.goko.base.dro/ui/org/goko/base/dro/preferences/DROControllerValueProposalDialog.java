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
package org.goko.base.dro.preferences;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.goko.base.dro.controller.DROValueProposalController;
import org.goko.base.dro.controller.DROValueProposalModel;
import org.goko.common.GkUiDialogComponent;
import org.goko.common.elements.list.GkList;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.MachineValueDefinition;

public class DROControllerValueProposalDialog extends GkUiDialogComponent<DROValueProposalController, DROValueProposalModel> {

	protected MachineValueDefinition result;
	protected Shell shlChooseDisplayedValues;
	private Text text;
	private GkList<MachineValueDefinition> list;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */

	public DROControllerValueProposalDialog(Shell parent, IEclipseContext context) {
		super(parent, new DROValueProposalController(new DROValueProposalModel()));
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public java.util.List<MachineValueDefinition> openAndGetData(){
		setBlockOnOpen(true);
		open();
		return getDataModel().getSelectedValuesTypedList();
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
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) composite.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.horizontalSpacing = 5;

		try {
			createControls(composite);
		} catch (GkException e) {
			e.printStackTrace();
		}
		return composite;
	}

	public void createControls(Composite parent) throws GkException {

		list = new GkList<MachineValueDefinition>(parent, getDataModel().getAvailableValuesList(), MachineValueDefinition.class, "name");

		List list_1 = list.getList();
		GridData gd_list_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_list_1.heightHint = 120;
		list_1.setLayoutData(gd_list_1);
		GridData gd_list = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_list.heightHint = 120;

		Group composite_1 = new Group(parent, SWT.NONE);
		composite_1.setText("Description");
		FillLayout fl_composite_1 = new FillLayout(SWT.HORIZONTAL);
		fl_composite_1.marginWidth = 5;
		fl_composite_1.marginHeight = 5;
		composite_1.setLayout(fl_composite_1);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_1.heightHint = 80;
		composite_1.setLayoutData(gd_composite_1);

		text = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);

	}


	@Override
	protected Point getInitialSize() {
		return new Point(339, 383);
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		getController().saveSelectedElements(list.getSelectedElements());
		super.okPressed();
	}
}
