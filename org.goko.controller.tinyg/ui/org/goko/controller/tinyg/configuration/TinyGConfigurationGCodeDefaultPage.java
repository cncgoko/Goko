package org.goko.controller.tinyg.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

public class TinyGConfigurationGCodeDefaultPage extends AbstractTinyGConfigurationPage{
		
	public TinyGConfigurationGCodeDefaultPage(TinyGConfiguration configuration) {
		super(configuration);
		setTitle("GCode defaults");		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
		TinyGComboFieldEditor defaultPlaneFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		defaultPlaneFieldEditor.setLabelWidthInChar(16);
		defaultPlaneFieldEditor.setLabel("Plane selection");
		defaultPlaneFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		defaultPlaneFieldEditor.setPreferenceName(TinyGConfiguration.DEFAULT_PLANE_SELECTION);
		{
			String[][] values = new String[][]{ 
					{"XY Plane - G17","0"},
					{"XZ Plane - G18","1"},
					{"YZ Plane - G19","2"}
				};
			defaultPlaneFieldEditor.setEntry(values);			
		}
		TinyGComboFieldEditor defaultUnitsFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		defaultUnitsFieldEditor.setLabelWidthInChar(16);
		defaultUnitsFieldEditor.setLabel("Units");
		defaultUnitsFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		defaultUnitsFieldEditor.setPreferenceName(TinyGConfiguration.DEFAULT_UNITS_MODE);
		{
			String[][] values = new String[][]{ 
					{"Inches - G20","0"},
					{"Millimeters - G21","1"}
				};
			defaultUnitsFieldEditor.setEntry(values);			
		}
		TinyGComboFieldEditor defaultCoordinateSystemFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		defaultCoordinateSystemFieldEditor.setLabelWidthInChar(16);
		defaultCoordinateSystemFieldEditor.setLabel("Coordinate system");
		defaultCoordinateSystemFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		defaultCoordinateSystemFieldEditor.setPreferenceName(TinyGConfiguration.DEFAULT_COORDINATE_SYSTEM);
		{
			String[][] values = new String[][]{ 
					{"G54", "1"},
					{"G55", "2"},
					{"G56", "3"},
					{"G57", "4"},
					{"G58", "5"},
					{"G59", "6"}
				};
			defaultCoordinateSystemFieldEditor.setEntry(values);			
		}
		TinyGComboFieldEditor defaultPathControlFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		defaultPathControlFieldEditor.setLabelWidthInChar(16);
		defaultPathControlFieldEditor.setLabel("Path control");
		defaultPathControlFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		defaultPathControlFieldEditor.setPreferenceName(TinyGConfiguration.DEFAULT_PATH_CONTROL);
		{
			String[][] values = new String[][]{ 
				{"Exact path - G61","0"},
				{"Exact stop - G61.1","1"},
				{"Continuous - G64","2"}
			};
			defaultPathControlFieldEditor.setEntry(values);			
		}
		TinyGComboFieldEditor defaultDistanceModeFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		defaultDistanceModeFieldEditor.setLabelWidthInChar(16);
		defaultDistanceModeFieldEditor.setLabel("Distance mode");
		defaultDistanceModeFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		defaultDistanceModeFieldEditor.setPreferenceName(TinyGConfiguration.DEFAULT_DISTANCE_MODE);
		{		
			String[][] values = new String[][]{ 
				{"Absolute - G60"   ,"0"},
				{"Incremental - G91","1"}
			};
			defaultDistanceModeFieldEditor.setEntry(values);
		}
		addField(defaultPlaneFieldEditor);
		addField(defaultUnitsFieldEditor);
		addField(defaultCoordinateSystemFieldEditor);
		addField(defaultPathControlFieldEditor);
		addField(defaultDistanceModeFieldEditor);
	}
}
