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
package org.goko.controller.grbl.v11.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.preferences.fieldeditor.preference.BigDecimalFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.validator.IntegerValidator;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfigurationPage;
import org.goko.controller.grbl.v11.preferences.Grblv11Preferences;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 *
 * @author PsyKo
 *
 */
public class GrblPollingPreferencePage extends AbstractGrblConfigurationPage<GrblConfiguration> {
	private static final GkLog LOG = GkLog.getLogger(GrblPollingPreferencePage.class);
	
	public GrblPollingPreferencePage(GrblConfiguration configuration) {
		super(configuration);
		setTitle("Polling");
		setPreferenceStore(Grblv11Preferences.getInstance());
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
	
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		Group grpAxisConfiguration = new Group(composite, SWT.NONE);
		grpAxisConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpAxisConfiguration.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridLayout gl_grpAxisConfiguration = new GridLayout(2, false);
		gl_grpAxisConfiguration.marginWidth = 0;
		gl_grpAxisConfiguration.marginHeight = 0;
		grpAxisConfiguration.setLayout(gl_grpAxisConfiguration);
		grpAxisConfiguration.setText("Polling");
		
		BigDecimalFieldEditor pollingPeriodField = new BigDecimalFieldEditor(grpAxisConfiguration, SWT.NONE);
		pollingPeriodField.setWidthInChars(6);
		pollingPeriodField.setLabel("Period");
		pollingPeriodField.setPreferenceName(Grblv11Preferences.POLLING_PERIOD_MS);
		
		Label lblNewLabel = new Label(grpAxisConfiguration, SWT.NONE);
		lblNewLabel.setText("ms");
		
		pollingPeriodField.setValidator(new IntegerValidator(100, true, null, false, "Polling period should be longer than 100ms"));
		
		addField(pollingPeriodField);	
	}

	protected Point getInitialSize() {
		return new Point(627, 650);
	}
}
