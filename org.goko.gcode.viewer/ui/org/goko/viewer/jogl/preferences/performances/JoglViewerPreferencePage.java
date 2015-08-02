package org.goko.viewer.jogl.preferences.performances;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.goko.viewer.jogl.preferences.JoglViewerPreference;

/**
 * Jogl viewer preferences page
 * 
 * @author PsyKo
 *
 */
public class JoglViewerPreferencePage extends GkFieldEditorPreferencesPage {
	private GkLog LOG = GkLog.getLogger(JoglViewerPreferencePage.class);			
	
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
		comboFieldEditor.setPreferenceName("performances.multisampling");
		comboFieldEditor.setEntry(lstMultiSampling);
		
		Unit<Length> lengthUnit = GokoPreference.getInstance().getLengthUnit();
		addField(comboFieldEditor);
	}

	
}
