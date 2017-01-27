package org.goko.controller.tinyg.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGBigDecimalSettingFieldEditor;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGBooleanFieldEditor;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGComboFieldEditor;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

public class TinyGConfigurationCommunicationPage extends AbstractTinyGConfigurationPage{
	
	public TinyGConfigurationCommunicationPage(TinyGConfiguration configuration) {
		super(configuration);
		setTitle("Communication");		
	}
		
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
		TinyGComboFieldEditor jsonVerbosityFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		jsonVerbosityFieldEditor.setLabelWidthInChar(18);
		jsonVerbosityFieldEditor.setLabel("JSON Verbosity");
		jsonVerbosityFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		jsonVerbosityFieldEditor.setPreferenceName(TinyGConfiguration.JSON_VERBOSITY);
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
		TinyGComboFieldEditor textModeVerbosityFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		((GridData) textModeVerbosityFieldEditor.getControl().getLayoutData()).horizontalAlignment = SWT.FILL;
		textModeVerbosityFieldEditor.setLabelWidthInChar(18);
		textModeVerbosityFieldEditor.setLabel("Text mode verbosity");
		textModeVerbosityFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		textModeVerbosityFieldEditor.setPreferenceName(TinyGConfiguration.TEXT_MODE_VERBOSITY);
		{	
			
			String[][] values = new String[][]{ // 0=silent, 1=verbose
				{"Silent","0"},			
				{"Verbose","1"}
				
			};
			textModeVerbosityFieldEditor.setEntry(values);			
		}
		TinyGComboFieldEditor queueReportVerbosityFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		queueReportVerbosityFieldEditor.setLabelWidthInChar(18);
		queueReportVerbosityFieldEditor.setLabel("Queue report verbosity");
		queueReportVerbosityFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		queueReportVerbosityFieldEditor.setPreferenceName(TinyGConfiguration.QUEUE_REPORT_VERBOSITY);
		
		TinyGComboFieldEditor statusReportVerbosityFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);		
		statusReportVerbosityFieldEditor.setLabelWidthInChar(18);
		statusReportVerbosityFieldEditor.setLabel("Status report verbosity");
		statusReportVerbosityFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		statusReportVerbosityFieldEditor.setPreferenceName(TinyGConfiguration.STATUS_REPORT_VERBOSITY);
		
		{			
			String[][] values = new String[][]{ // 0=off, 1=filtered, 2=verbose
				{"Off","0"},			
				{"Filtered","1"},
				{"Verbose","2"}
			};
			statusReportVerbosityFieldEditor.setEntry(values);
			queueReportVerbosityFieldEditor.setEntry(values);
		}
		
		TinyGBigDecimalSettingFieldEditor statusReportIntervalFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		statusReportIntervalFieldEditor.setWidthInChars(6);
		statusReportIntervalFieldEditor.setTextLimit(5);
		statusReportIntervalFieldEditor.setLabelWidthInChar(18);
		statusReportIntervalFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		statusReportIntervalFieldEditor.setPreferenceName(TinyGConfiguration.STATUS_REPORT_INTERVAL);
		statusReportIntervalFieldEditor.setLabel("Status report interval");
		new Label(parent, SWT.NONE);
		
		TinyGComboFieldEditor baudrateFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		baudrateFieldEditor.setLabelWidthInChar(10);
		baudrateFieldEditor.setLabel("Baudrate");
		baudrateFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		baudrateFieldEditor.setPreferenceName(TinyGConfiguration.BAUD_RATE);
		
		{
//			choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "9600"));
//			choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "19200"));
//			choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("3"), "38400"));
//			choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("4"), "57600"));
//			choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("5"), "115200"));
//			choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("6"), "230400"));
			String[][] baudrate = new String[][]{ 
					{"9600","1"},			
					{"19200","2"},
					{"38400","3"},
					{"57600","4"},
					{"115200","5"},
					{"230400","6"}					
				};
			baudrateFieldEditor.setEntry(baudrate);			
		}
		
		TinyGComboFieldEditor flowControlFieldEditor = new TinyGComboFieldEditor(parent, SWT.READ_ONLY);
		flowControlFieldEditor.setLabelWidthInChar(10);
		flowControlFieldEditor.setLabel("Flow control");
		flowControlFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		flowControlFieldEditor.setPreferenceName(TinyGConfiguration.ENABLE_FLOW_CONTROL);
		{
			// 0=off, 1=XON/XOFF enabled, 2=RTS/CTS enabled
			String[][] flowControl = new String[][]{ 
					{"Off","0"},			
					{"XON/XOFF","1"},
					{"RTS/CTS","2"}										
				};
			flowControlFieldEditor.setEntry(flowControl);			
		}
		TinyGBooleanFieldEditor crOnTxFieldEditor = new TinyGBooleanFieldEditor(parent, SWT.NONE);
		crOnTxFieldEditor.setLabel("Enable CR on TX");
		crOnTxFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		crOnTxFieldEditor.setPreferenceName(TinyGConfiguration.ENABLE_CR_ON_TX);
				
		TinyGBooleanFieldEditor enableEchoFieldEditor = new TinyGBooleanFieldEditor(parent, SWT.NONE);
		enableEchoFieldEditor.setLabel("Enable character echo");
		enableEchoFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		enableEchoFieldEditor.setPreferenceName(TinyGConfiguration.ENABLE_CHARACTER_ECHO);
				
		addField(statusReportVerbosityFieldEditor);
		addField(queueReportVerbosityFieldEditor);
		addField(jsonVerbosityFieldEditor);
		addField(textModeVerbosityFieldEditor);		
		addField(baudrateFieldEditor);		
		addField(statusReportIntervalFieldEditor);
		addField(flowControlFieldEditor);
		addField(crOnTxFieldEditor);
		addField(enableEchoFieldEditor);
	}
}
