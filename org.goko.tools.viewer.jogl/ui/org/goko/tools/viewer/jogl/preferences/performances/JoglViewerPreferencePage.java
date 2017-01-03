package org.goko.tools.viewer.jogl.preferences.performances;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.ColorFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;
import org.goko.tools.viewer.jogl.camera.AbstractCamera;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.tools.viewer.jogl.service.IJoglViewerService;

/**
 * Jogl viewer preferences page
 * 
 * @author PsyKo
 *
 */
public class JoglViewerPreferencePage extends GkFieldEditorPreferencesPage {
	@Inject
	private IJoglViewerService joglviewerService;
	
	public JoglViewerPreferencePage() {
		setDescription("Configure the 3D viewer component.");
		setTitle("Viewer");
		setPreferenceStore(JoglViewerPreference.getInstance());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
		Group grpPerformances = new Group(parent, SWT.NONE);
		grpPerformances.setLayout(new GridLayout(1, false));
		grpPerformances.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpPerformances.setText("Performances");
						
		ComboFieldEditor comboFieldEditor = new ComboFieldEditor(grpPerformances, SWT.READ_ONLY);
		comboFieldEditor.setLabel("Multisampling :");
		String[][] lstMultiSampling = new String[][]{{"1x (Fastest)", "1"} ,
										 			{"2x","2"},
													{"4x","4"},
													{"8x (Nicest)","8"}};
		comboFieldEditor.setPreferenceName(JoglViewerPreference.MULTISAMPLING);
		comboFieldEditor.setEntry(lstMultiSampling);
		
		GokoPreference.getInstance().getLengthUnit();
				
		BooleanFieldEditor showFpsFieldEditor = new BooleanFieldEditor(grpPerformances, SWT.NONE);
		showFpsFieldEditor.setPreferenceName(JoglViewerPreference.SHOW_FPS);
		showFpsFieldEditor.setLabel("Show FPS");
		
		Group grpMisc = new Group(parent, SWT.NONE);
		grpMisc.setLayout(new GridLayout(1, false));
		grpMisc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpMisc.setText("Misc.");
		
		ColorFieldEditor backgroundColorFieldEditor = new ColorFieldEditor(grpMisc, SWT.NONE);
		backgroundColorFieldEditor.setPreferenceName(JoglViewerPreference.BACKGROUND_COLOR);
		backgroundColorFieldEditor.setLabel("Background");

		ComboFieldEditor defaultCameraFieldEditor = new ComboFieldEditor(grpMisc, SWT.READ_ONLY);
		defaultCameraFieldEditor.setLabel("Default view");
		defaultCameraFieldEditor.setEntry(getAvailableCamera());
		defaultCameraFieldEditor.setPreferenceName(JoglViewerPreference.DEFAULT_CAMERA);

		addField(comboFieldEditor);
		addField(showFpsFieldEditor);
		addField(backgroundColorFieldEditor);
		addField(defaultCameraFieldEditor);
	}	
	
	private String[][] getAvailableCamera() throws GkException{
		List<AbstractCamera> supportedCamera = joglviewerService.getSupportedCamera();		
		int cameraCount = CollectionUtils.size(supportedCamera);
		String[][] result = new String[cameraCount][2];		
		int i = 0;
		for (AbstractCamera camera : supportedCamera) {
			result[i][0] = camera.getLabel();
			result[i][1] = camera.getId();
			i++;
		}
		return result;
	}
}
