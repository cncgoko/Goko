package org.goko.tinyg.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.BooleanFieldEditor;
import org.goko.tinyg.controller.prefs.TinyGPreferences;

/**
 * Preferences for TinyG
 * @author PsyKo
 *
 */
public class TinyGPreferencesPage extends GkFieldEditorPreferencesPage{
	public TinyGPreferencesPage() {
		setDescription("Configure TinyG features");
		setTitle("TinyG");
		setPreferenceStore(TinyGPreferences.getInstance().getPreferenceStore());
	}

	@Override
	protected void createPreferencePage(Composite parent) {
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.marginTop = 10;
		
		Group grpHoming = new Group(parent, SWT.NONE);
		grpHoming.setText("Homing");
		
		GridLayout gl_grpHoming = new GridLayout(1, true);
		grpHoming.setLayout(gl_grpHoming);
		grpHoming.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblConfigure = new Label(grpHoming, SWT.NONE);
		lblConfigure.setText("Enable homing on the following axes");
		
		Composite composite = new Composite(grpHoming, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(4, true);
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		
		BooleanFieldEditor xAxisField = new BooleanFieldEditor(composite, SWT.NONE);
		xAxisField.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		xAxisField.setLabel("X axis");
		xAxisField.setPreferenceName(TinyGPreferences.HOMING_ENABLED_AXIS_X);
		
		BooleanFieldEditor yAxisField = new BooleanFieldEditor(composite, SWT.NONE);
		yAxisField.setLabel("Y axis");
		yAxisField.setPreferenceName(TinyGPreferences.HOMING_ENABLED_AXIS_Y);
		
		BooleanFieldEditor zAxisField = new BooleanFieldEditor(composite, SWT.NONE);
		zAxisField.setLabel("Z axis");
		zAxisField.setPreferenceName(TinyGPreferences.HOMING_ENABLED_AXIS_Z);
				
		BooleanFieldEditor aAxisField = new BooleanFieldEditor(composite, SWT.NONE);
		aAxisField.setLabel("A axis");
		aAxisField.setPreferenceName(TinyGPreferences.HOMING_ENABLED_AXIS_A);
		// TODO Auto-generated method stub
		
		addField(xAxisField);
		addField(yAxisField);
		addField(zAxisField);
		addField(aAxisField);
		
		Label lblHomingShouldBe = new Label(grpHoming, SWT.NONE);
		lblHomingShouldBe.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		lblHomingShouldBe.setText("Homing should be enabled in the board configuration");
	}

}
