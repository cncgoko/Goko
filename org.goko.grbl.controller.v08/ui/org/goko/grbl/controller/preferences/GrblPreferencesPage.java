package org.goko.grbl.controller.preferences;

import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.goko.common.preferences.fieldeditor.BooleanFieldEditor;
import org.eclipse.wb.swt.SWTResourceManager;

public class GrblPreferencesPage extends GkFieldEditorPreferencesPage{
	public GrblPreferencesPage() {
		setDescription("Configure GRBL preferences");
		setTitle("GRBL");
		setPreferenceStore(GrblPreferences.getInstance().getPreferenceStore());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) {		
		Group grpHoing = new Group(parent, SWT.NONE);
		grpHoing.setLayout(new GridLayout(1, false));
		grpHoing.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpHoing.setText("Homing");
		
		Label lblNewLabel = new Label(grpHoing, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("Enable homing on the following axes");
		
		Composite composite = new Composite(grpHoing, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, true);
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		BooleanFieldEditor xAxisFieldEditor = new BooleanFieldEditor(composite, SWT.NONE);
		xAxisFieldEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		((GridData) xAxisFieldEditor.getControl().getLayoutData()).horizontalAlignment = SWT.FILL;
		xAxisFieldEditor.setLabel("X Axis");
		xAxisFieldEditor.setPreferenceName(GrblPreferences.HOMING_ENABLED_AXIS_X);
		
		BooleanFieldEditor yAxisFieldEditor = new BooleanFieldEditor(composite, SWT.NONE);
		yAxisFieldEditor.setLabel("Y Axis");
		yAxisFieldEditor.setPreferenceName(GrblPreferences.HOMING_ENABLED_AXIS_Y);
		
		BooleanFieldEditor zAxisFieldEditor = new BooleanFieldEditor(composite, SWT.NONE);
		zAxisFieldEditor.setLabel("Z Axis");
		zAxisFieldEditor.setPreferenceName(GrblPreferences.HOMING_ENABLED_AXIS_Z);
		Label label = new Label(grpHoing, SWT.NONE);
		label.setText("Homing should be enabled in the board configuration");
		label.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		// TODO Auto-generated method stub
		
		addField(xAxisFieldEditor);
		addField(yAxisFieldEditor);
		addField(zAxisFieldEditor);		
	}

}
