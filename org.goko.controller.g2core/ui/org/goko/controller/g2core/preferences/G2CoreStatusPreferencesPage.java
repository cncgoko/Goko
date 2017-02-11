/**
 * 
 */
package org.goko.controller.g2core.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 2 févr. 2017
 */
public class G2CoreStatusPreferencesPage extends GkFieldEditorPreferencesPage{
	
	public G2CoreStatusPreferencesPage() {		
		setTitle("Status report");
		setDescription("Configure G2 Core status report content");
		setPreferenceStore(G2CorePreferences.getInstance());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.marginHeight = 5;
		gridLayout.verticalSpacing = 4;
		gridLayout.numColumns = 5;
		
		BooleanFieldEditor machineStateFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		machineStateFieldEditor.setLabel("Machine state");
		machineStateFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_MACHINE_STATE);
		new Label(parent, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(parent, SWT.NONE);
		lblNewLabel_1.setText("Report positions");
		
		BooleanFieldEditor mposxFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		mposxFieldEditor.setLabel("X");
		mposxFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_POS_X);
		
		BooleanFieldEditor mposaFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		mposaFieldEditor.setLabel("A");
		mposaFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_POS_A);
		addField(mposaFieldEditor);
		
		BooleanFieldEditor velocityFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		velocityFieldEditor.setLabel("Velocity");
		velocityFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_VELOCITY);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor mposyFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		mposyFieldEditor.setLabel("Y");
		mposyFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_POS_Y);
		addField(mposyFieldEditor);
		
		BooleanFieldEditor mposbFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		mposbFieldEditor.setLabel("B");
		mposbFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_POS_B);
		
		BooleanFieldEditor feedrateFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		feedrateFieldEditor.setLabel("Feedrate");
		feedrateFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_FEEDRATE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor mposzFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		mposzFieldEditor.setLabel("Z");
		mposzFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_POS_Z);
		addField(mposzFieldEditor);
		
		BooleanFieldEditor mposcFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		mposcFieldEditor.setLabel("C");
		mposcFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_POS_C);
		addField(mposcFieldEditor);
		
		BooleanFieldEditor unitFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		unitFieldEditor.setLabel("Unit");
		unitFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_UNITS);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor csFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		csFieldEditor.setLabel("Coordinate system");
		csFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_COORDINATE_SYSTEM);
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		
		BooleanFieldEditor workPositionFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		workPositionFieldEditor.setLabel("Work position");
		workPositionFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_WPOS);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor motionModeFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		motionModeFieldEditor.setLabel("Motion mode");
		motionModeFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_MOTION_MODE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor machinePositionFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		machinePositionFieldEditor.setLabel("Machine position");
		machinePositionFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_MPOS);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor planeFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		planeFieldEditor.setLabel("Plane");
		planeFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_PLANE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor pathControlFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		pathControlFieldEditor.setLabel("Path control");
		pathControlFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_PATH_CONTROL);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor distanceModeFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		distanceModeFieldEditor.setLabel("Distance mode");
		distanceModeFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_DISTANCE_MODE);		
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor arcDistanceFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		arcDistanceFieldEditor.setLabel("Arc distance mode");
		arcDistanceFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_ARC_DISTANCE_MODE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor feedrateModeFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		feedrateModeFieldEditor.setLabel("Feedrate mode");
		feedrateModeFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_FEEDRATE_MODE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor toolFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		toolFieldEditor.setLabel("Tool");
		toolFieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_TOOL);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		BooleanFieldEditor g92FieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		g92FieldEditor.setLabel("G92");
		g92FieldEditor.setPreferenceName(G2CorePreferences.STATUS_REPORT_G92);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		addField(machineStateFieldEditor);
		addField(mposxFieldEditor);
		addField(mposbFieldEditor);
		addField(velocityFieldEditor);
		addField(feedrateFieldEditor);
		addField(unitFieldEditor);
		addField(csFieldEditor);
		addField(motionModeFieldEditor);
		addField(planeFieldEditor);
		addField(pathControlFieldEditor);
		addField(distanceModeFieldEditor);
		addField(arcDistanceFieldEditor);
		addField(feedrateModeFieldEditor);
		addField(toolFieldEditor);
		addField(g92FieldEditor);
		addField(workPositionFieldEditor);
		addField(machinePositionFieldEditor);

	}

}
