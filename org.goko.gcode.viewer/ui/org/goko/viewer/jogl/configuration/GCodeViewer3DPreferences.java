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
package org.goko.viewer.jogl.configuration;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.goko.common.GkUiPreferencePageComponent;

public class GCodeViewer3DPreferences extends GkUiPreferencePageComponent<GCodeViewer3DPreferencesController, GCodeViewer3DPreferencesModel> {

	@Inject
	public GCodeViewer3DPreferences(IEclipseContext context) {
		super(new GCodeViewer3DPreferencesController());
		ContextInjectionFactory.inject(getController(), context);

	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Control createContents(Composite parent) {
		Composite control = new Composite(parent, NONE);
		control.setLayout(new GridLayout(1, false));
//
//		//Composite composite = new Composite(control, SWT.NONE);
//		PropertyTable table = new PropertyTable(control, SWT.NONE);
//		table.hideButtons();
//		table.viewAsCategories();
//		table.showDescription();
//		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		table.addProperty(new PTProperty("id", "Identifier", "Description for identifier", "My id")).setCategory("General");
//		table.addProperty(new PTProperty("text", "Description", "Description for the description field", "blahblah...")).setCategory("General");
//		table.addProperty(new PTProperty("color", "Description", "Description for the description field", new RGB(254,232,0))).setCategory("General").setEditor(new PTColorEditor());
//		table.addProperty(new PTProperty("url", "URL:", "This is a nice <b>URL</b>", "http://www.google.com").setCategory("General")).setEditor(new PTURLEditor());
//		table.addProperty(new PTProperty("password", "Password", "Enter your <i>password</i> and keep it secret...", "password")).setCategory("General").setEditor(new PTPasswordEditor());
		return control;
	}
}
