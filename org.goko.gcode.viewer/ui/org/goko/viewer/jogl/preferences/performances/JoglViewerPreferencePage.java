package org.goko.viewer.jogl.preferences.performances;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.ComboFieldEditor;
import org.goko.common.preferences.fieldeditor.QuantityFieldEditor;
import org.goko.core.common.measure.quantity.Length;
import org.goko.viewer.jogl.service.JoglViewerSettings;

/**
 * Jogl viewer preferences page
 * 
 * @author PsyKo
 *
 */
public class JoglViewerPreferencePage extends GkFieldEditorPreferencesPage {
	
	public JoglViewerPreferencePage() {
		setDescription("Configure the 3D viewer component.");
		setTitle("Viewer");
		setPreferenceStore(JoglViewerSettings.getInstance().getPreferences());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) {
		
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
		
		
		Group grpGrid = new Group(parent, SWT.NONE);
		grpGrid.setLayout(new GridLayout(1, false));
		grpGrid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpGrid.setText("Grid");
		
		QuantityFieldEditor<Length> quantityFieldEditor = new QuantityFieldEditor<Length>(grpGrid, SWT.NONE);
		quantityFieldEditor.setPreferenceName("grid.majorSpacing");
		quantityFieldEditor.setWidthInChars(5);
		quantityFieldEditor.setLabel("Major grid spacing");		
		quantityFieldEditor.setEmptyStringAllowed(false);
		
		QuantityFieldEditor<Length> quantityFieldEditor_1 = new QuantityFieldEditor<Length>(grpGrid, SWT.NONE);
		quantityFieldEditor_1.setEmptyStringAllowed(false);
		quantityFieldEditor_1.setPreferenceName("grid.minorSpacing");
		quantityFieldEditor_1.setWidthInChars(5);
		quantityFieldEditor_1.setLabel("Minor grid spacing");
		
		addField(quantityFieldEditor);
		addField(quantityFieldEditor_1);
		addField(comboFieldEditor);
	}

}
