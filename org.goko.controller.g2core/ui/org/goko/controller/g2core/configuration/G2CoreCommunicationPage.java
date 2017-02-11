/**
 * 
 */
package org.goko.controller.g2core.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGBigDecimalSettingFieldEditor;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGComboFieldEditor;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 30 janv. 2017
 */
public class G2CoreCommunicationPage extends AbstractG2CoreConfigurationPage{

	/**
	 * @param configuration
	 */
	public G2CoreCommunicationPage(G2CoreConfiguration configuration) {
		super(configuration);
		setTitle("Communication");
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		parent.setLayout(new GridLayout(2, false));
		
		TinyGComboFieldEditor jsonModeFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		jsonModeFieldEditor.setLabelWidthInChar(20);
		jsonModeFieldEditor.setLabel("Enable JSON mode");
		jsonModeFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		jsonModeFieldEditor.setPreferenceName(G2Core.Configuration.System.JSON_MODE);
		{
			/*
			{ej:0}  TEXT: Responses provided as Text (input commands are accepted in either format)
			{ej:1}  JSON: Responses provided as JSON (input commands are accepted in either format)
			{ej:2}  AUTO: Responses provided in the format of the request
			 */
			String[][] values = new String[][]{ // 0=off, 1=filtered, 2=verbose
				{"Text mode","0"},			
				{"JSon mode","1"},
				{"Auto","2"}
			};
			jsonModeFieldEditor.setEntry(values);
		}
		new Label(parent, SWT.NONE);
		
		TinyGComboFieldEditor jsonVerbosityFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		jsonVerbosityFieldEditor.setLabelWidthInChar(20);
		jsonVerbosityFieldEditor.setLabel("JSON verbosity");
		jsonVerbosityFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		jsonVerbosityFieldEditor.setPreferenceName(G2Core.Configuration.System.JSON_MODE_VERBOSITY);
		{
			/*
			$jv=0      - Silent   - No response is provided for any command
			$jv=1      - Footer   - Returns footer only - no command echo, gcode blocks or messages
			$jv=2      - Messages - Returns footers, exception messages and gcode comment messages
			$jv=3      - Configs  - Returns footer, messages, config command body
			$jv=4      - Linenum  - Returns footer, messages, config command body, and gcode line numbers if present
			$jv=5      - Verbose  - Returns footer, messages, config command body, and gcode blocks
			 */
			String[][] values = new String[][]{ // 0=off, 1=filtered, 2=verbose
				{"Silent","0"},			
				{"Footer","1"},
				{"Messages","2"},
				{"Configs","3"},
				{"Linenum","4"},
				{"Verbose","5"}
			};
			jsonVerbosityFieldEditor.setEntry(values);
		}
		new Label(parent, SWT.NONE);
		
		TinyGComboFieldEditor textVerbosityFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		textVerbosityFieldEditor.setLabelWidthInChar(20);
		textVerbosityFieldEditor.setLabel("Text mode verbosity");
		textVerbosityFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		textVerbosityFieldEditor.setPreferenceName(G2Core.Configuration.System.TEXT_MODE_VERBOSITY);
		{		
			/*
			{tv:0}  Silent - no response is provided
			{tv:1}  Verbose - returns OK and error responses
			*/
			String[][] values = new String[][]{ 
				{"Silent","0"},	
				{"Verbose","1"}
			};
			textVerbosityFieldEditor.setEntry(values);			
		}
		new Label(parent, SWT.NONE);
		
		TinyGComboFieldEditor queueReportVerbosity = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		queueReportVerbosity.setLabelWidthInChar(20);
		queueReportVerbosity.setLabel("Queue report verbosity");
		queueReportVerbosity.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		queueReportVerbosity.setPreferenceName(G2Core.Configuration.System.QUEUE_REPORT_VERBOSITY);
		new Label(parent, SWT.NONE);
		
		TinyGComboFieldEditor statusReportVerbosity = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		statusReportVerbosity.setLabelWidthInChar(20);
		statusReportVerbosity.setLabel("Status report verbosity");
		statusReportVerbosity.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		statusReportVerbosity.setPreferenceName(G2Core.Configuration.System.STATUS_REPORT_VERBOSITY);
		new Label(parent, SWT.NONE);
		
		{			
			String[][] values = new String[][]{ // 0=off, 1=filtered, 2=verbose
				{"Off","0"},			
				{"Filtered","1"},
				{"Verbose","2"}
			};
			queueReportVerbosity.setEntry(values);
			statusReportVerbosity.setEntry(values);
		}
		
		TinyGBigDecimalSettingFieldEditor statusReportIntervalFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		statusReportIntervalFieldEditor.setWidthInChars(11);
		statusReportIntervalFieldEditor.setLabelWidthInChar(20);
		statusReportIntervalFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		statusReportIntervalFieldEditor.setLabel("Status report interval");
		statusReportIntervalFieldEditor.setPreferenceName(G2Core.Configuration.System.STATUS_REPORT_INTERVAL);
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("milliseconds");

		addField(jsonModeFieldEditor);
		addField(jsonVerbosityFieldEditor);
		addField(textVerbosityFieldEditor);
		addField(queueReportVerbosity);
		addField(statusReportVerbosity);
		addField(statusReportIntervalFieldEditor);
	}

}
