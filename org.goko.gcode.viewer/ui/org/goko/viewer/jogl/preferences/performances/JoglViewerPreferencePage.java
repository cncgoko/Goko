package org.goko.viewer.jogl.preferences.performances;

import javax.inject.Inject;

import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.ComboFieldEditor;
import org.goko.common.preferences.fieldeditor.QuantityFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.goko.viewer.jogl.service.JoglViewerSettings;

/**
 * Jogl viewer preferences page
 * 
 * @author PsyKo
 *
 */
public class JoglViewerPreferencePage extends GkFieldEditorPreferencesPage {
	private GkLog LOG = GkLog.getLogger(JoglViewerPreferencePage.class);			
	private QuantityFieldEditor<Length> majorSpacingFieldEditor;
	private QuantityFieldEditor<Length> minorSpacingFieldEditor;
	
	public JoglViewerPreferencePage() {
		setDescription("Configure the 3D viewer component.");
		setTitle("Viewer");
		setPreferenceStore(JoglViewerSettings.getInstance());
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
		
		
		Group grpGrid = new Group(parent, SWT.NONE);
		grpGrid.setLayout(new GridLayout(1, false));
		grpGrid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpGrid.setText("Grid");
		
		majorSpacingFieldEditor = new QuantityFieldEditor<Length>(grpGrid, SWT.NONE);
		majorSpacingFieldEditor.setPreferenceName("grid.majorSpacing");
		majorSpacingFieldEditor.setWidthInChars(5);
		majorSpacingFieldEditor.setLabel("Major grid spacing");		
		majorSpacingFieldEditor.setEmptyStringAllowed(false);
		
		minorSpacingFieldEditor = new QuantityFieldEditor<Length>(grpGrid, SWT.NONE);
		minorSpacingFieldEditor.setEmptyStringAllowed(false);
		minorSpacingFieldEditor.setPreferenceName("grid.minorSpacing");
		minorSpacingFieldEditor.setWidthInChars(5);
		minorSpacingFieldEditor.setLabel("Minor grid spacing");
		
		Unit<Length> lengthUnit;lengthUnit = GokoPreference.getInstance().getLengthUnit();
		
		majorSpacingFieldEditor.setUnit(lengthUnit);
		minorSpacingFieldEditor.setUnit(lengthUnit);
		
		addField(majorSpacingFieldEditor);
		addField(minorSpacingFieldEditor);
		addField(comboFieldEditor);
	}

	@Inject
	public void onUnitPreferenceChange(@Preference(nodePath = GokoPreference.NODE_ID, value = GokoPreference.KEY_DISTANCE_UNIT) String unit) {			
		try {
			Unit<Length> lengthUnit;lengthUnit = GokoPreference.getInstance().getLengthUnit();
			if(majorSpacingFieldEditor != null && minorSpacingFieldEditor != null){
				majorSpacingFieldEditor.setUnit(lengthUnit);
				minorSpacingFieldEditor.setUnit(lengthUnit);
			}
		} catch (GkException e) {
			LOG.error(e);
		}					
	}
}
