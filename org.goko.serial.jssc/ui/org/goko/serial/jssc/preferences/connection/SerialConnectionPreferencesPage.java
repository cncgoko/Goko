package org.goko.serial.jssc.preferences.connection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.serial.jssc.service.JsscParameter;

import jssc.SerialPort;

/**
 * Serial connection preference page
 * @author PsyKo
 *
 */
public class SerialConnectionPreferencesPage extends GkFieldEditorPreferencesPage {
	
	public SerialConnectionPreferencesPage() {
		setDescription("Configure your connection settings");		
		setTitle("Serial");
		setPreferenceStore(SerialConnectionPreference.getInstance());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) {
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		
		ComboFieldEditor baudrateField = new ComboFieldEditor(parent, SWT.READ_ONLY);
		((GridData) baudrateField.getControl().getLayoutData()).widthHint = 60;
		baudrateField.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		baudrateField.setLabelWidthInChar(10);
		baudrateField.setPreferenceName(JsscParameter.BAUDRATE.toString());
		baudrateField.setLabel("Baudrate");
		initBaudrateChoices(baudrateField);
		
		ComboFieldEditor databitsField = new ComboFieldEditor(parent, SWT.READ_ONLY);
		((GridData) databitsField.getControl().getLayoutData()).widthHint = 60;
		databitsField.setPreferenceName(JsscParameter.DATABITS.toString());
		databitsField.setLabelWidthInChar(10);
		databitsField.setLabel("Data bits");
		initDataBitsChoices(databitsField);
	
		ComboFieldEditor parityField = new ComboFieldEditor(parent, SWT.READ_ONLY);
		((GridData) parityField.getControl().getLayoutData()).widthHint = 60;
		parityField.setPreferenceName(JsscParameter.PARITY.toString());
		parityField.setLabelWidthInChar(10);
		parityField.setLabel("Parity");
		parityField.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		initParityChoices(parityField);
		
		ComboFieldEditor stopbitField = new ComboFieldEditor(parent, SWT.READ_ONLY);
		((GridData) stopbitField.getControl().getLayoutData()).widthHint = 60;
		stopbitField.setPreferenceName(JsscParameter.STOPBITS.toString());
		stopbitField.setLabelWidthInChar(10);
		stopbitField.setLabel("Stop bits");
		initStopBitsChoices(stopbitField);
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.widthHint = 100;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(1, false));
		
		BooleanFieldEditor rtsCtsField = new BooleanFieldEditor(composite, SWT.NONE);
		GridData gridData = (GridData) rtsCtsField.getControl().getLayoutData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		rtsCtsField.setPreferenceName(JsscParameter.RCSCTS.toString());
		GridData gd_rtsCtsField = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_rtsCtsField.horizontalIndent = 70;
		rtsCtsField.setLayoutData(gd_rtsCtsField);
		rtsCtsField.setSize(109, 20);
		rtsCtsField.setLabel("Enable RTS/CTS");
		
		BooleanFieldEditor xonXoffField = new BooleanFieldEditor(composite, SWT.NONE);
		((GridData) xonXoffField.getControl().getLayoutData()).grabExcessHorizontalSpace = true;
		GridData gd_xonXoffField = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_xonXoffField.horizontalIndent = 70;
		xonXoffField.setLayoutData(gd_xonXoffField);
		xonXoffField.setSize(121, 20);
		xonXoffField.setLabel("Enable XON/XOFF");
		xonXoffField.setPreferenceName(JsscParameter.XONXOFF.toString());
		// TODO Auto-generated method stub
		
		addField(xonXoffField);
		addField(rtsCtsField);
		addField(stopbitField);
		addField(parityField);
		addField(databitsField);
		addField(baudrateField);
	}
	
	private void initBaudrateChoices(ComboFieldEditor field){
		String[][] baudrateItems = new String[][]{
				{String.valueOf(SerialPort.BAUDRATE_110)	,"5"},
			    {String.valueOf(SerialPort.BAUDRATE_110) 	,"110"},
			    {String.valueOf(SerialPort.BAUDRATE_300) 	,"300"},
			    {String.valueOf(SerialPort.BAUDRATE_600) 	,"600"},
			    {String.valueOf(SerialPort.BAUDRATE_1200) 	,"1200"},
			    {String.valueOf(SerialPort.BAUDRATE_4800) 	,"4800"},
			    {String.valueOf(SerialPort.BAUDRATE_9600) 	,"9600"},
			    {String.valueOf(SerialPort.BAUDRATE_14400) 	,"14400"},
			    {String.valueOf(SerialPort.BAUDRATE_19200) 	,"19200"},
			    {String.valueOf(SerialPort.BAUDRATE_38400) 	,"38400"},
			    {String.valueOf(SerialPort.BAUDRATE_57600) 	,"57600"},
			    {String.valueOf(SerialPort.BAUDRATE_115200) ,"115200"},
			    {String.valueOf(SerialPort.BAUDRATE_128000) ,"128000"},
			    {String.valueOf(230400)  ,"230400"},
			    {String.valueOf(SerialPort.BAUDRATE_256000) ,"256000"}};
		field.setEntry(baudrateItems);
	}

	private void initDataBitsChoices(ComboFieldEditor field){		
		String[][] databitsItems = new String[][]{
				{"5", String.valueOf(SerialPort.DATABITS_5)},
				{"6", String.valueOf(SerialPort.DATABITS_6)},
				{"7", String.valueOf(SerialPort.DATABITS_7)},
				{"8", String.valueOf(SerialPort.DATABITS_8)},
		};	
		field.setEntry(databitsItems);
	}

	private void initParityChoices(ComboFieldEditor field){
		String[][] parityItems = new String[][]{
				{"None", String.valueOf(SerialPort.PARITY_NONE)},
				{"Even", String.valueOf(SerialPort.PARITY_EVEN)},
				{"Odd" , String.valueOf(SerialPort.PARITY_ODD)}};
		field.setEntry(parityItems);
	}

	private void initStopBitsChoices(ComboFieldEditor field){
		String[][] stopBitItems = new String[][]{		
				{"1",String.valueOf(SerialPort.STOPBITS_1)},
				{"1.5",String.valueOf(SerialPort.STOPBITS_1_5)},
				{"2",String.valueOf(SerialPort.STOPBITS_2)}};
		field.setEntry(stopBitItems);
	}

}
