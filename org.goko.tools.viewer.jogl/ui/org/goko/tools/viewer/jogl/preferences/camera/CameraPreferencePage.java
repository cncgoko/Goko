/**
 * 
 */
package org.goko.tools.viewer.jogl.preferences.camera;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.BigDecimalFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;

/**
 * @author Psyko
 * @date 19 sept. 2016
 */
public class CameraPreferencePage extends GkFieldEditorPreferencesPage{
	
	/**
	 * Default constructor
	 */
	public CameraPreferencePage() {
		setTitle("Camera");
		setDescription("Configure the camera");
		setPreferenceStore(JoglViewerPreference.getInstance());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginHeight = 0;
		gl_parent.marginWidth = 0;
		gl_parent.horizontalSpacing = 0;
		parent.setLayout(gl_parent);
		
		Group grpControls = new Group(parent, SWT.NONE);
		grpControls.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		grpControls.setText("Orbit");
		grpControls.setLayout(new GridLayout(5, false));
		
		BooleanFieldEditor orbitInvertXFieldEditor = new BooleanFieldEditor(grpControls, SWT.NONE);
		orbitInvertXFieldEditor.setPreferenceName(JoglViewerPreference.ORBIT_INVERT_X_AXIS);
		orbitInvertXFieldEditor.setLabel("Invert X axis ");
		new Label(grpControls, SWT.NONE);
		
		Label label = new Label(grpControls, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		BigDecimalFieldEditor orbitSensitivityEditor = new BigDecimalFieldEditor(grpControls, SWT.NONE);
		orbitSensitivityEditor.setPreferenceName(JoglViewerPreference.ORBIT_SENSITIVITY);
		orbitSensitivityEditor.setTextLimit(3);
		orbitSensitivityEditor.setWidthInChars(5);
		orbitSensitivityEditor.setLabel("Sensitivity");
		
		Label label_1 = new Label(grpControls, SWT.NONE);
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_1.widthHint = 50;
		label_1.setLayoutData(gd_label_1);
		
		BooleanFieldEditor orbitInvertYFieldEditor = new BooleanFieldEditor(grpControls, SWT.NONE);
		orbitInvertYFieldEditor.setPreferenceName(JoglViewerPreference.ORBIT_INVERT_Y_AXIS);
		orbitInvertYFieldEditor.setLabel("Invert Y axis");
		new Label(grpControls, SWT.NONE);
		new Label(grpControls, SWT.NONE);
		new Label(grpControls, SWT.NONE);
		new Label(grpControls, SWT.NONE);
		
		Group grpPan = new Group(parent, SWT.NONE);
		grpPan.setText("Pan");
		grpPan.setLayout(new GridLayout(5, false));
		grpPan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		BooleanFieldEditor panInvertXFieldEditor = new BooleanFieldEditor(grpPan, SWT.NONE);
		panInvertXFieldEditor.setPreferenceName(JoglViewerPreference.PAN_INVERT_X_AXIS);
		panInvertXFieldEditor.setLabel("Invert X axis");
		new Label(grpPan, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(grpPan, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		BigDecimalFieldEditor panSensitivityFieldEditor = new BigDecimalFieldEditor(grpPan, SWT.NONE);
		panSensitivityFieldEditor.setPreferenceName(JoglViewerPreference.PAN_SENSITIVITY);
		panSensitivityFieldEditor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		panSensitivityFieldEditor.setTextLimit(3);
		panSensitivityFieldEditor.setWidthInChars(5);
		panSensitivityFieldEditor.setLabel("Sensitivity");
		
		Label lblNewLabel = new Label(grpPan, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 50;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		BooleanFieldEditor panInvertYFieldEditor = new BooleanFieldEditor(grpPan, SWT.NONE);
		panInvertYFieldEditor.setPreferenceName(JoglViewerPreference.PAN_INVERT_Y_AXIS);
		panInvertYFieldEditor.setLabel("Invert Y axis");
		new Label(grpPan, SWT.NONE);
		new Label(grpPan, SWT.NONE);
		new Label(grpPan, SWT.NONE);
		new Label(grpPan, SWT.NONE);
		
		Group grpZoom = new Group(parent, SWT.NONE);
		grpZoom.setLayout(new GridLayout(4, false));
		grpZoom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpZoom.setText("Zoom");
		
		BooleanFieldEditor zoomInvertFieldEditor = new BooleanFieldEditor(grpZoom, SWT.NONE);
		zoomInvertFieldEditor.setPreferenceName(JoglViewerPreference.ZOOM_INVERT_AXIS);
		zoomInvertFieldEditor.setLabel("Invert zoom direction");
		
		Label lblNewLabel_2 = new Label(grpZoom, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		BigDecimalFieldEditor zoomSensitivityFieldEditor = new BigDecimalFieldEditor(grpZoom, SWT.NONE);
		zoomSensitivityFieldEditor.setPreferenceName(JoglViewerPreference.ZOOM_SENSITIVITY);
		zoomSensitivityFieldEditor.setWidthInChars(5);
		zoomSensitivityFieldEditor.setTextLimit(3);
		zoomSensitivityFieldEditor.setLabel("Sensitivity");
		
		Label lblNewLabel_3 = new Label(grpZoom, SWT.NONE);
		GridData gd_lblNewLabel_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_3.widthHint = 50;
		lblNewLabel_3.setLayoutData(gd_lblNewLabel_3);
	
		addField(orbitInvertXFieldEditor);
		addField(orbitInvertYFieldEditor);
		addField(orbitSensitivityEditor);
		addField(panInvertXFieldEditor);
		addField(panInvertYFieldEditor);
		addField(panSensitivityFieldEditor);
		addField(zoomInvertFieldEditor);
		addField(zoomSensitivityFieldEditor);		
	}
}
