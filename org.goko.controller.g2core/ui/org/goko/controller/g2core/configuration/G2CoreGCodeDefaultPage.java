/**
 * 
 */
package org.goko.controller.g2core.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGComboFieldEditor;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 30 janv. 2017
 */
public class G2CoreGCodeDefaultPage extends AbstractG2CoreConfigurationPage{

	/**
	 * @param configuration
	 */
	public G2CoreGCodeDefaultPage(G2CoreConfiguration configuration) {
		super(configuration);
		setTitle("GCode defaults");
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		parent.setLayout(new GridLayout(1, false));
		
		TinyGComboFieldEditor planeSelectionFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		planeSelectionFieldEditor.setLabelWidthInChar(16);
		planeSelectionFieldEditor.setLabel("Plane selection");
		planeSelectionFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		planeSelectionFieldEditor.setPreferenceName(G2Core.Configuration.System.DEFAULT_PLANE_SELECTION);
		planeSelectionFieldEditor.setEntry(new String[][]{ 
				{"XY Plane (G17)","0"},			
				{"XZ Plane (G18)","1"},
				{"YZ Plane (G19)","2"}});
		
		TinyGComboFieldEditor unitsModeFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		unitsModeFieldEditor.setLabelWidthInChar(16);
		unitsModeFieldEditor.setLabel("Units mode");
		unitsModeFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		unitsModeFieldEditor.setPreferenceName(G2Core.Configuration.System.DEFAULT_UNITS_MODE);
		unitsModeFieldEditor.setEntry(new String[][]{ 
									{"Inches (G20)","0"},			
									{"Millimeters (G21)","1"}});
		
		TinyGComboFieldEditor coordinateSystemFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		coordinateSystemFieldEditor.setLabelWidthInChar(16);
		coordinateSystemFieldEditor.setLabel("Coordinate system");
		coordinateSystemFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		coordinateSystemFieldEditor.setPreferenceName(G2Core.Configuration.System.DEFAULT_COORDINATE_SYSTEM);
		coordinateSystemFieldEditor.setEntry(new String[][]{ 
											{"G54","1"},			
											{"G55","2"},
											{"G56","3"},
											{"G57","4"},
											{"G58","5"},
											{"G59","6"}});
	
		
		TinyGComboFieldEditor pathControlFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		pathControlFieldEditor.setLabelWidthInChar(16);
		pathControlFieldEditor.setLabel("Path control");
		pathControlFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		pathControlFieldEditor.setPreferenceName(G2Core.Configuration.System.DEFAULT_PATH_CONTROL);
		pathControlFieldEditor.setEntry(new String[][]{ 
											{"Exact path mode (G61)","0"},			
											{"Exact stop mode (G61.1)","1"},
											{"Continuous mode (G64)","2"}});
		
		TinyGComboFieldEditor distanceModeFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		distanceModeFieldEditor.setLabelWidthInChar(16);
		distanceModeFieldEditor.setLabel("Distance mode");
		distanceModeFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		distanceModeFieldEditor.setPreferenceName(G2Core.Configuration.System.DEFAULT_DISTANCE_MODE);
		distanceModeFieldEditor.setEntry(new String[][]{ 
									{"Absolute mode (G90)","0"},			
									{"Relative mode (G91)","1"}});
		
		addField(planeSelectionFieldEditor);
		addField(unitsModeFieldEditor);
		addField(coordinateSystemFieldEditor);
		addField(pathControlFieldEditor);
		addField(distanceModeFieldEditor);
	}

}
