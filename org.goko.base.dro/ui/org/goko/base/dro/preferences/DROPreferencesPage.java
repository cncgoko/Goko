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

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.base.dro.controller.DROPreferencesPageController;
import org.goko.base.dro.controller.DROPreferencesPageModel;
import org.goko.common.GkUiPreferencePageComponent;
import org.goko.common.elements.list.GkList;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.MachineValueDefinition;

public class DROPreferencesPage extends GkUiPreferencePageComponent<DROPreferencesPageController, DROPreferencesPageModel> {
	private Button btnRemove;
	private GkList<MachineValueDefinition> listViewer;



	IEclipseContext context;
	/**
	 * Create the preference page.
	 */
	@Inject
	public DROPreferencesPage(IEclipseContext context) {
		super(new DROPreferencesPageController(new DROPreferencesPageModel()));
		ContextInjectionFactory.inject(getController(), context);
		setDescription("Configure digital read out view.");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("org.goko.base.dro", "icons/compass.png"));
		setTitle("Digital read out");
		this.context = context;
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));

		Group grpDisplayedValues = new Group(container, SWT.NONE);
		grpDisplayedValues.setText("Displayed values");
		grpDisplayedValues.setLayout(new GridLayout(1, true));
		grpDisplayedValues.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite_1 = new Composite(grpDisplayedValues, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		listViewer = new GkList<MachineValueDefinition>(composite_1, getDataModel().getDisplayedValuesList(), MachineValueDefinition.class, "name");

		org.eclipse.swt.widgets.List valueList = listViewer.getList();
		GridData gd_valueList = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_valueList.heightHint = 150;
		valueList.setLayoutData(gd_valueList);

		Composite composite = new Composite(composite_1, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DROControllerValueProposalDialog dialog = new DROControllerValueProposalDialog(getShell(), context);
				ContextInjectionFactory.inject(DROControllerValueProposalDialog.class, context);
				List<MachineValueDefinition> lstToAdd = dialog.openAndGetData();

				getController().addValuesToDisplay(lstToAdd);
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnNewButton.setText("Add");

		btnRemove = new Button(composite, SWT.NONE);
		btnRemove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				IStructuredSelection structuredSelection = (IStructuredSelection) listViewer.getSelection();
				getController().removeDisplayedValues(structuredSelection.toList());
			}
		});
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnRemove.setText("Remove");



		Button btnUp = new Button(composite, SWT.NONE);
		btnUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				IStructuredSelection structuredSelection = (IStructuredSelection) listViewer.getSelection();
				getController().moveDisplayedValuesUp(structuredSelection.toList());
				listViewer.setSelection(structuredSelection);
			}
		});
		btnUp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnUp.setText("Up");

		Button btnDown = new Button(composite, SWT.NONE);
		btnDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnDown.setText("Down");
		btnDown.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				IStructuredSelection structuredSelection = (IStructuredSelection) listViewer.getSelection();
				getController().moveDisplayedValuesDown(structuredSelection.toList());
				listViewer.setSelection(structuredSelection);
			}
		});

		try {
			initBindings();
		} catch (GkException e) {
			displayMessage(e);
		}
		return container;
	}

	private void initBindings() throws GkException {
		getController().refreshDisplayedValuesList();
	}

	@Override
	public boolean performOk() {
		getController().savedDisplayedValues();
		return super.performOk();
	}

	/**
	 * Initialize the preference page.
	 */
	@Override
	public void init(IWorkbench workbench) {

	}

}


