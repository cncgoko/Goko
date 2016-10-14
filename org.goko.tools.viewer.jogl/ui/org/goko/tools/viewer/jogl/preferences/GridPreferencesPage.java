/**
 * 
 */
package org.goko.tools.viewer.jogl.preferences;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.ColorFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.IntegerFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.quantity.LengthFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.IWorkVolumeProvider;
import org.goko.core.log.GkLog;

/**
 * @author PsyKo
 *
 */
public class GridPreferencesPage extends GkFieldEditorPreferencesPage {
	private GkLog LOG = GkLog.getLogger(GridPreferencesPage.class);	
	private LengthFieldEditor majorSpacingFieldEditor;
	private LengthFieldEditor minorSpacingFieldEditor;
	private LengthFieldEditor startXFieldEditor;
	private LengthFieldEditor startYFieldEditor;
	private LengthFieldEditor startZFieldEditor;
	private LengthFieldEditor endXFieldEditor;
	private LengthFieldEditor endYFieldEditor;
	private LengthFieldEditor endZFieldEditor;
	private LengthFieldEditor graduationSizeFieldEditor;
	@Inject
	@Optional
	private IWorkVolumeProvider workVolumeProvider;
	
	public GridPreferencesPage() {
		setTitle("Grid");
		setPreferenceStore(JoglViewerPreference.getInstance());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		Unit<Length> lengthUnit = GokoPreference.getInstance().getLengthUnit();
		
		Group grpSettings = new Group(parent, SWT.NONE);
		grpSettings.setText("Settings");
		grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpSettings.setLayout(new GridLayout(2, false));
		
		majorSpacingFieldEditor = new LengthFieldEditor(grpSettings, SWT.NONE);
		majorSpacingFieldEditor.setLabelWidthInChar(15);
		majorSpacingFieldEditor.setPreferenceName("grid.majorSpacing");
		majorSpacingFieldEditor.setWidthInChars(5);
		majorSpacingFieldEditor.setLabel("Major grid spacing");		
		majorSpacingFieldEditor.setEmptyStringAllowed(false);
		majorSpacingFieldEditor.setPreferenceName(JoglViewerPreference.MAJOR_GRID_SPACING);
		majorSpacingFieldEditor.setUnit(lengthUnit);
		new Label(grpSettings, SWT.NONE);
		
		minorSpacingFieldEditor = new LengthFieldEditor(grpSettings, SWT.NONE);
		minorSpacingFieldEditor.setLabelWidthInChar(15);
		minorSpacingFieldEditor.setEmptyStringAllowed(false);
		minorSpacingFieldEditor.setPreferenceName("grid.minorSpacing");
		minorSpacingFieldEditor.setWidthInChars(5);
		minorSpacingFieldEditor.setLabel("Minor grid spacing");
		minorSpacingFieldEditor.setPreferenceName(JoglViewerPreference.MINOR_GRID_SPACING);
		minorSpacingFieldEditor.setUnit(lengthUnit);
		new Label(grpSettings, SWT.NONE);
		
		ColorFieldEditor majorColorFieldEditor = new ColorFieldEditor(grpSettings, SWT.NONE);		
		majorColorFieldEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));		
		majorColorFieldEditor.setLabel("Major grid color");
		majorColorFieldEditor.setPreferenceName(JoglViewerPreference.MAJOR_GRID_COLOR);
		
		IntegerFieldEditor majorGridOpacityFieldEditor = new IntegerFieldEditor(grpSettings, SWT.NONE);
		majorGridOpacityFieldEditor.setLabel("Opacity");
		majorGridOpacityFieldEditor.setWidthInChars(4);
		majorGridOpacityFieldEditor.setLabelWidthInChar(6);
		majorGridOpacityFieldEditor.setPreferenceName(JoglViewerPreference.MAJOR_GRID_OPACITY);
		addField(majorGridOpacityFieldEditor);
		
		ColorFieldEditor minorColorFieldEditor = new ColorFieldEditor(grpSettings, SWT.NONE);		
		minorColorFieldEditor.setLabel("Minor grid color");		
		minorColorFieldEditor.setPreferenceName(JoglViewerPreference.MINOR_GRID_COLOR);
		
		IntegerFieldEditor minorGridOpacityFieldEditor = new IntegerFieldEditor(grpSettings, SWT.NONE);
		minorGridOpacityFieldEditor.setWidthInChars(4);
		minorGridOpacityFieldEditor.setPreferenceName(JoglViewerPreference.MINOR_GRID_OPACITY);
		minorGridOpacityFieldEditor.setLabelWidthInChar(6);
		minorGridOpacityFieldEditor.setLabel("Opacity");
		
		Group grpLimits = new Group(parent, SWT.NONE);
		grpLimits.setLayout(new GridLayout(1, false));
		grpLimits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpLimits.setText("Limits");

		Composite composite = new Composite(grpLimits, SWT.NONE);
		GridLayout gl_composite = new GridLayout(5, false);
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblStart = new Label(composite, SWT.NONE);
		GridData gd_lblStart = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblStart.widthHint = 35;
		lblStart.setLayoutData(gd_lblStart);
		lblStart.setBounds(0, 0, 55, 15);
		lblStart.setText("Start");
		
		startXFieldEditor = new LengthFieldEditor(composite, SWT.NONE);
		startXFieldEditor.setEmptyStringAllowed(false);
		startXFieldEditor.setWidthInChars(6);
		startXFieldEditor.setLabel("X");
		startXFieldEditor.setPreferenceName(JoglViewerPreference.GRID_START_X);
		startXFieldEditor.setUnit(lengthUnit);
		
		Label lblNewLabel = new Label(composite, SWT.HORIZONTAL);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblEnd = new Label(composite, SWT.NONE);
		lblEnd.setText("End");
		
		endXFieldEditor = new LengthFieldEditor(composite, SWT.NONE);
		endXFieldEditor.setEmptyStringAllowed(false);
		endXFieldEditor.setWidthInChars(6);
		endXFieldEditor.setLabel("X");
		endXFieldEditor.setPreferenceName(JoglViewerPreference.GRID_END_X);
		endXFieldEditor.setUnit(lengthUnit);
		new Label(composite, SWT.NONE);
		
		startYFieldEditor = new LengthFieldEditor(composite, SWT.NONE);
		startYFieldEditor.setEmptyStringAllowed(false);
		startYFieldEditor.setWidthInChars(6);
		startYFieldEditor.setLabel("Y");
		startYFieldEditor.setPreferenceName(JoglViewerPreference.GRID_START_Y);
		startYFieldEditor.setUnit(lengthUnit);
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		endYFieldEditor = new LengthFieldEditor(composite, SWT.NONE);
		endYFieldEditor.setEmptyStringAllowed(false);
		endYFieldEditor.setWidthInChars(6);
		endYFieldEditor.setLabel("Y");
		endYFieldEditor.setPreferenceName(JoglViewerPreference.GRID_END_Y);
		endYFieldEditor.setUnit(lengthUnit);
		
		new Label(composite, SWT.NONE);
		startZFieldEditor = new LengthFieldEditor(composite, SWT.NONE);
		startZFieldEditor.setEmptyStringAllowed(false);
		startZFieldEditor.setWidthInChars(6);
		startZFieldEditor.setLabel("Z");
		startZFieldEditor.setPreferenceName(JoglViewerPreference.GRID_START_Z);
		startZFieldEditor.setUnit(lengthUnit);
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		endZFieldEditor = new LengthFieldEditor(composite, SWT.NONE);
		endZFieldEditor.setEmptyStringAllowed(false);
		endZFieldEditor.setWidthInChars(6);
		endZFieldEditor.setLabel("Z");
		endZFieldEditor.setPreferenceName(JoglViewerPreference.GRID_END_Z);
		endZFieldEditor.setUnit(lengthUnit);
		

		IntegerFieldEditor axisGridOpacityFieldEditor = new IntegerFieldEditor(grpSettings, SWT.NONE);
		axisGridOpacityFieldEditor.setWidthInChars(4);
		axisGridOpacityFieldEditor.setPreferenceName(JoglViewerPreference.GRID_AXIS_OPACITY);
		axisGridOpacityFieldEditor.setLabelWidthInChar(13);
		axisGridOpacityFieldEditor.setLabel("Axis opacity");
		
		// Adding fields
		addField(majorSpacingFieldEditor);
		addField(minorSpacingFieldEditor);
		
		addField(majorColorFieldEditor);
		addField(minorColorFieldEditor);		
		
		addField(minorGridOpacityFieldEditor);
		addField(majorGridOpacityFieldEditor);
		addField(axisGridOpacityFieldEditor);
		
		new Label(grpSettings, SWT.NONE);
		
		graduationSizeFieldEditor = new LengthFieldEditor(grpSettings, SWT.NONE);
		graduationSizeFieldEditor.setWidthInChars(4);
		graduationSizeFieldEditor.setLabel("Graduation size");
		graduationSizeFieldEditor.setLabelWidthInChar(13);
		graduationSizeFieldEditor.setUnit(lengthUnit);
		graduationSizeFieldEditor.setPreferenceName(JoglViewerPreference.GRID_GRADUATION_SIZE);
		
		new Label(grpSettings, SWT.NONE);
		addField(startXFieldEditor);
		addField(startYFieldEditor);
		addField(startZFieldEditor);
		addField(endXFieldEditor);
		addField(endYFieldEditor);
		addField(endZFieldEditor);
		addField(graduationSizeFieldEditor);
		
	}
	
	@Inject
	public void onUnitPreferenceChange(@Preference(nodePath = GokoPreference.NODE_ID, value = GokoPreference.KEY_DISTANCE_UNIT) String unit) {			
		try {
			Unit<Length> lengthUnit = GokoPreference.getInstance().getLengthUnit();
			if(majorSpacingFieldEditor != null && minorSpacingFieldEditor != null){
				majorSpacingFieldEditor.setUnit(lengthUnit);
				minorSpacingFieldEditor.setUnit(lengthUnit);
				startXFieldEditor.setUnit(lengthUnit);
				startYFieldEditor.setUnit(lengthUnit);
				startZFieldEditor.setUnit(lengthUnit);
				endXFieldEditor.setUnit(lengthUnit);
				endYFieldEditor.setUnit(lengthUnit);
				endZFieldEditor.setUnit(lengthUnit);
				graduationSizeFieldEditor.setUnit(lengthUnit);
			}
		} catch (GkException e) {
			LOG.error(e);
		}					
	}
}
