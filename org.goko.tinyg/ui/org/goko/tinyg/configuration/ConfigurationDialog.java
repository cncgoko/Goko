package org.goko.tinyg.configuration;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.tinyg.configuration.bindings.ConfigurationBindings;
import org.goko.tinyg.configuration.bindings.ConfigurationController;
import org.goko.tinyg.controller.events.ConfigurationUpdateEvent;

public class ConfigurationDialog extends Dialog{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ConfigurationDialog.class);

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFirmwareversion;
	private Text txtFirmwarebuild;
	private Text txtStatusinterval;
	private Text txtJunctionAcceleration;
	private Text txtChordalTolerance;
	private Text txtMotorDisableTimeout;
	private Text txtStepAngleM1;
	private Text txtTravelPerRevM1;
	private Text txtStepAngleM2;
	private Text txtTravelPerRevM2;
	private Text txtStepAngleM3;
	private Text txtTravelPerRevM3;
	private Text txtStepAngleM4;
	private Text txtTravelPerRevM4;
	private Text txtMaxVelocity;
	private Text txtMaxFeedrate;
	private Text txtMaxTravel;
	private Text txtMaxJerk;
	private Text txtJunctionDeviation;
	private Text txtRadiusValue;
	private ConfigurationController controller;
	private GkCombo<LabeledValue> motor1mapping;
	private GkCombo<LabeledValue> motor2mapping;
	private GkCombo<LabeledValue> motor3mapping;
	private GkCombo<LabeledValue> motor4mapping;
	private GkCombo<LabeledValue> comboMicrostepsM1;
	private GkCombo<LabeledValue> comboMicrostepsM2;
	private GkCombo<LabeledValue> comboMicrostepsM3;
	private GkCombo<LabeledValue> comboMicrostepsM4;
	private GkCombo<LabeledValue> axisSelectionCombo;
	private GkCombo<LabeledValue> comboMinSwitchMode;
	private GkCombo<LabeledValue> comboMaxSwitchMode;
	private Text txtJerkHoming;
	private Text txtSearchVelocity;
	private Text txtLatchVelocity;
	private Text txtZeroBackoff;
	private GkCombo<LabeledValue> comboPolarityM1;
	private GkCombo<LabeledValue> comboPolarityM2;
	private GkCombo<LabeledValue> comboPolarityM3;
	private GkCombo<LabeledValue> comboPolarityM4;

	private GkCombo<LabeledValue> comboPowerModeM1;
	private GkCombo<LabeledValue> comboPowerModeM2;
	private GkCombo<LabeledValue> comboPowerModeM3;
	private GkCombo<LabeledValue> comboPowerModeM4;

	private GkCombo<LabeledValue> comboPlaneSelection;
	private GkCombo<LabeledValue> comboCoordinateSystem;
	private GkCombo<LabeledValue> comboDistanceMode;
	private GkCombo<LabeledValue> comboUnits;
	private GkCombo<LabeledValue> comboPathControl;
	private Text txtDeviceId;
	private Button chckboxIgnoreCrRx;
	private Button chckboxIgnoreLfRx;
	private Button chckboxEnableEcho;
	private Button chckboxEnableXonXoff;
	private Button chckboxEnableCrTx;
	private GkCombo<LabeledValue> comboTextModeVerbsity;
	private GkCombo<LabeledValue> comboJSONVerbosity;
	private GkCombo<LabeledValue> comboQueueReportVerbosity;
	private GkCombo<LabeledValue> comboStatusReportVerbosity;
	private GkCombo<LabeledValue> comboSwitchType;
	private CLabel labelValidationMessages;
	private GkCombo<LabeledValue> comboBaudrate;


	public ConfigurationDialog(Shell shell, IEclipseContext context) {
		super(shell);
		controller = new ConfigurationController(new ConfigurationBindings());
		ContextInjectionFactory.inject(controller, context);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("TinyG Configuration");
	}
	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		try {
			createControls(composite);
		} catch (GkException e) {
			e.printStackTrace();
		}
		return composite;
	}

	protected void createToolbar(final Form form){
		form.getToolBarManager().add(new Action("Refresh configuration", ImageDescriptor.createFromImage(ResourceManager.getPluginImage("org.goko.tinyg", "icons/arrow-circle-double.png") )) {
			@Override
			public void run() {
				try {
					controller.refreshConfiguration();
				} catch (GkException e) {
					e.printStackTrace();
				}
			}
		});
		form.getToolBarManager().update(true);
	}
	/**
	 * Create contents of the view part.
	 * @throws GkException
	 */
	@PostConstruct
	public void createControls(Composite parent) throws GkException {
		parent.setLayout(new GridLayout(1, false));

		Form frmNewForm = formToolkit.createForm(parent);
		createToolbar(frmNewForm);
		frmNewForm.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText("TinyG Configuration");

		CTabFolder settingsTabFolder = new CTabFolder(frmNewForm.getBody(), SWT.BORDER | SWT.BOTTOM);
		settingsTabFolder.setSize(595, 614);
		settingsTabFolder.setTabPosition(SWT.TOP);
		formToolkit.adapt(settingsTabFolder);
		formToolkit.paintBordersFor(settingsTabFolder);
		settingsTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tbtmSystem = new CTabItem(settingsTabFolder, SWT.NONE);
		tbtmSystem.setText("System");
		settingsTabFolder.setSelection(0);
		ScrolledForm formSystem = formToolkit.createScrolledForm(settingsTabFolder);
		tbtmSystem.setControl(formSystem);
		formToolkit.paintBordersFor(formSystem);
		formSystem.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite_4 = new Composite(formSystem.getBody(), SWT.NONE);
		composite_4.setLayout(new GridLayout(1, false));
		formToolkit.adapt(composite_4);
		formToolkit.paintBordersFor(composite_4);


		Section sctnDevice = formToolkit.createSection(composite_4, Section.TWISTIE | Section.TITLE_BAR);
		sctnDevice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnDevice);
		sctnDevice.setText("Device");
		sctnDevice.setExpanded(true);

		Composite composite = formToolkit.createComposite(sctnDevice, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		sctnDevice.setClient(composite);
		composite.setLayout(new GridLayout(2, false));

		Label lblFirmwareVersion = formToolkit.createLabel(composite, "Firmware version :", SWT.NONE);
		lblFirmwareVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtFirmwareversion = formToolkit.createText(composite, "", SWT.NONE);
		txtFirmwareversion.setEditable(false);
		txtFirmwareversion.setText("firmwareVersion");
		GridData gd_txtFirmwareversion = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtFirmwareversion.widthHint = 80;
		txtFirmwareversion.setLayoutData(gd_txtFirmwareversion);

		Label lblFirmwareBuild = formToolkit.createLabel(composite, "Firmware build :", SWT.NONE);
		lblFirmwareBuild.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtFirmwarebuild = formToolkit.createText(composite, "", SWT.NONE);
		txtFirmwarebuild.setEditable(false);
		txtFirmwarebuild.setText("firmwareBuild");
		GridData gd_txtFirmwarebuild = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtFirmwarebuild.widthHint = 80;
		txtFirmwarebuild.setLayoutData(gd_txtFirmwarebuild);

		Label lblStatusInterval = formToolkit.createLabel(composite, "Device ID :", SWT.NONE);
		lblStatusInterval.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtDeviceId = formToolkit.createText(composite, "New Text", SWT.NONE);
		txtDeviceId.setText("Device ID");
		GridData gd_txtDeviceId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtDeviceId.widthHint = 80;
		txtDeviceId.setLayoutData(gd_txtDeviceId);

		Section sctnCommunication = formToolkit.createSection(composite_4, Section.TWISTIE | Section.TITLE_BAR);

		sctnCommunication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnCommunication);
		sctnCommunication.setText("Communication");
		sctnCommunication.setExpanded(true);

		Composite composite_2 = formToolkit.createComposite(sctnCommunication, SWT.NONE);
		formToolkit.paintBordersFor(composite_2);
		sctnCommunication.setClient(composite_2);
		composite_2.setLayout(new GridLayout(6, false));

		Label lblJsonVerbosity = formToolkit.createLabel(composite_2, "JSON Verbosity :", SWT.NONE);
		lblJsonVerbosity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJsonVerbosity.setBounds(0, 0, 55, 15);

		comboJSONVerbosity = new GkCombo<LabeledValue>(composite_2, SWT.NONE | SWT.READ_ONLY);
		Combo combo = comboJSONVerbosity.getCombo();
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 80;
		combo.setLayoutData(gd_combo);
		formToolkit.paintBordersFor(combo);
		new Label(composite_2, SWT.NONE);

		chckboxIgnoreCrRx = new Button(composite_2, SWT.RADIO);
		formToolkit.adapt(chckboxIgnoreCrRx, true, true);
		chckboxIgnoreCrRx.setText("Ignore CR on RX");
		new Label(composite_2, SWT.NONE);

		chckboxIgnoreLfRx = new Button(composite_2, SWT.RADIO);
		chckboxIgnoreLfRx.setText("Ignore LF on RX");
		formToolkit.adapt(chckboxIgnoreLfRx, true, true);

		Label lblTextModeVerbosity = formToolkit.createLabel(composite_2, "Text mode verbosity :", SWT.NONE);
		lblTextModeVerbosity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboTextModeVerbsity = new GkCombo<LabeledValue>(composite_2, SWT.READ_ONLY);
		Combo combo_1 = comboTextModeVerbsity.getCombo();
		GridData gd_combo_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_1.widthHint = 80;
		combo_1.setLayoutData(gd_combo_1);
		formToolkit.paintBordersFor(combo_1);
		new Label(composite_2, SWT.NONE);

		chckboxEnableCrTx = new Button(composite_2, SWT.CHECK);
		formToolkit.adapt(chckboxEnableCrTx, true, true);
		chckboxEnableCrTx.setText("Enable CR on TX");
		new Label(composite_2, SWT.NONE);

		chckboxEnableXonXoff = new Button(composite_2, SWT.CHECK);
		formToolkit.adapt(chckboxEnableXonXoff, true, true);
		chckboxEnableXonXoff.setText("Enable XON/XOFF");

		Label lblNewLabel_1 = formToolkit.createLabel(composite_2, "Queue report verbosity :", SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboQueueReportVerbosity = new GkCombo<LabeledValue>(composite_2, SWT.READ_ONLY);
		Combo combo_2 = comboQueueReportVerbosity.getCombo();
		GridData gd_combo_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_2.widthHint = 80;
		combo_2.setLayoutData(gd_combo_2);
		formToolkit.paintBordersFor(combo_2);
		new Label(composite_2, SWT.NONE);

		chckboxEnableEcho = new Button(composite_2, SWT.CHECK);
		formToolkit.adapt(chckboxEnableEcho, true, true);
		chckboxEnableEcho.setText("Enable echo");
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);

		Label lblStatusReportVerbosity = formToolkit.createLabel(composite_2, "Status report verbosity :", SWT.NONE);
		lblStatusReportVerbosity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboStatusReportVerbosity = new GkCombo<LabeledValue>(composite_2, SWT.READ_ONLY);
		Combo combo_3 = comboStatusReportVerbosity.getCombo();
		GridData gd_combo_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_3.widthHint = 80;
		combo_3.setLayoutData(gd_combo_3);
		formToolkit.paintBordersFor(combo_3);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);

		Label lblNewLabel_2 = formToolkit.createLabel(composite_2, "Status report interval :", SWT.NONE);

		txtStatusinterval = formToolkit.createText(composite_2, "", SWT.NONE);
		GridData gd_txtStatusinterval = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtStatusinterval.widthHint = 40;
		txtStatusinterval.setLayoutData(gd_txtStatusinterval);
		txtStatusinterval.setText("statusInterval");

		Label lblMs = formToolkit.createLabel(composite_2, "ms", SWT.NONE);
		GridData gd_lblMs = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblMs.widthHint = 50;
		lblMs.setLayoutData(gd_lblMs);

		Label lblBaudrate = formToolkit.createLabel(composite_2, "Baudrate :", SWT.NONE);
		lblBaudrate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		new Label(composite_2, SWT.NONE);

		comboBaudrate = new GkCombo<LabeledValue>(composite_2, SWT.READ_ONLY);
		Combo comboBaudrate_1 = comboBaudrate.getCombo();
		comboBaudrate_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(comboBaudrate_1);

		CTabItem tbtmMotor = new CTabItem(settingsTabFolder, SWT.NONE);
		tbtmMotor.setText("Motor");

		ScrolledForm formMotors = formToolkit.createScrolledForm(settingsTabFolder);
		tbtmMotor.setControl(formMotors);
		formToolkit.paintBordersFor(formMotors);
		formMotors.setText("Motor settings");
		formMotors.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite_7 = formToolkit.createComposite(formMotors.getBody(), SWT.NONE);
		composite_7.setBounds(0, 0, 64, 64);
		formToolkit.paintBordersFor(composite_7);
		composite_7.setLayout(new GridLayout(1, false));


		Section sctnGcodeDefault = formToolkit.createSection(composite_4, Section.TWISTIE | Section.TITLE_BAR);
		sctnGcodeDefault.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnGcodeDefault);
		sctnGcodeDefault.setText("GCode default");
		sctnGcodeDefault.setExpanded(true);

		Composite composite_5 = formToolkit.createComposite(sctnGcodeDefault, SWT.NONE);
		formToolkit.paintBordersFor(composite_5);
		sctnGcodeDefault.setClient(composite_5);
		composite_5.setLayout(new GridLayout(4, false));

		Label lblPlaneSelection = formToolkit.createLabel(composite_5, "Plane selection :", SWT.NONE);
		lblPlaneSelection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboPlaneSelection = new GkCombo<LabeledValue>(composite_5, SWT.READ_ONLY);
		Combo combo_4 = comboPlaneSelection.getCombo();
		GridData gd_combo_4 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_4.widthHint = 80;
		combo_4.setLayoutData(gd_combo_4);

		Label lblPathControl = formToolkit.createLabel(composite_5, "Path control :", SWT.NONE);
		lblPathControl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboPathControl = new GkCombo<LabeledValue>(composite_5, SWT.READ_ONLY);
		Combo combo_6 = comboPathControl.getCombo();
		GridData gd_combo_6 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_6.widthHint = 80;
		combo_6.setLayoutData(gd_combo_6);

		Label lblUnits = formToolkit.createLabel(composite_5, "Units :", SWT.NONE);
		lblUnits.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboUnits = new GkCombo<LabeledValue>(composite_5, SWT.READ_ONLY);
		Combo combo_5 = comboUnits.getCombo();
		GridData gd_combo_5 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_5.widthHint = 80;
		combo_5.setLayoutData(gd_combo_5);

		Label lblDistanceMode = formToolkit.createLabel(composite_5, "Distance mode :", SWT.NONE);
		lblDistanceMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboDistanceMode = new GkCombo<LabeledValue>(composite_5, SWT.READ_ONLY);
		Combo combo_8 = comboDistanceMode.getCombo();
		GridData gd_combo_8 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_8.widthHint = 80;
		combo_8.setLayoutData(gd_combo_8);


		Label lblCoordinateSystem = formToolkit.createLabel(composite_5, "Coordinate system :", SWT.NONE);
		lblCoordinateSystem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboCoordinateSystem = new GkCombo<LabeledValue>(composite_5, SWT.READ_ONLY);
		Combo combo_7 = comboCoordinateSystem.getCombo();
		GridData gd_combo_7 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_7.widthHint = 80;
		combo_7.setLayoutData(gd_combo_7);
		new Label(composite_5, SWT.NONE);
		new Label(composite_5, SWT.NONE);

		Section sctnMotion = formToolkit.createSection(composite_4, Section.TWISTIE | Section.TITLE_BAR);
		sctnMotion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnMotion);
		sctnMotion.setText("Motion");
		sctnMotion.setExpanded(true);

		Composite composite_6 = formToolkit.createComposite(sctnMotion, SWT.NONE);
		formToolkit.paintBordersFor(composite_6);
		sctnMotion.setClient(composite_6);
		composite_6.setLayout(new GridLayout(3, false));

		Label lblJunctionAcceleration = formToolkit.createLabel(composite_6, "Junction acceleration :", SWT.NONE);
		lblJunctionAcceleration.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtJunctionAcceleration = formToolkit.createText(composite_6, "", SWT.NONE);
		GridData gd_txtJunctionAcceleration = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtJunctionAcceleration.widthHint = 80;
		gd_txtJunctionAcceleration.minimumWidth = 80;
		txtJunctionAcceleration.setLayoutData(gd_txtJunctionAcceleration);

		Label lblMmmin = formToolkit.createLabel(composite_6, "mm/min\u00B2", SWT.NONE);

		Label lblMinimumLineSegment = formToolkit.createLabel(composite_6, "Chordal tolerance :", SWT.NONE);
		lblMinimumLineSegment.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtChordalTolerance = formToolkit.createText(composite_6, "", SWT.NONE);
		txtChordalTolerance.setText("");
		GridData gd_txtChordalTolerance = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_txtChordalTolerance.widthHint = 80;
		txtChordalTolerance.setLayoutData(gd_txtChordalTolerance);
		new Label(composite_6, SWT.NONE);

		Label lblMinimumArcSegment = formToolkit.createLabel(composite_6, "Switch type :", SWT.NONE);
		lblMinimumArcSegment.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboSwitchType = new GkCombo<LabeledValue>(composite_6, SWT.READ_ONLY);
		Combo combo_9 = comboSwitchType.getCombo();
		GridData gd_combo_9 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_9.widthHint = 80;
		combo_9.setLayoutData(gd_combo_9);

		new Label(composite_6, SWT.NONE);

		Label lblMinimumSegmentTime = formToolkit.createLabel(composite_6, "Motor disable timeout :", SWT.NONE);
		lblMinimumSegmentTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtMotorDisableTimeout = formToolkit.createText(composite_6, "", SWT.NONE);
		txtMotorDisableTimeout.setText("");
		GridData gd_txtMotorDisableTimeout = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtMotorDisableTimeout.widthHint = 80;
		txtMotorDisableTimeout.setLayoutData(gd_txtMotorDisableTimeout);
		new Label(composite_6, SWT.NONE);


		Section sctnNewSection_1 = formToolkit.createSection(composite_7, Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnNewSection_1);
		sctnNewSection_1.setText("Motor/axes mapping");
		sctnNewSection_1.setExpanded(true);

		Composite composite_8 = formToolkit.createComposite(sctnNewSection_1, SWT.NONE);
		formToolkit.paintBordersFor(composite_8);
		sctnNewSection_1.setClient(composite_8);
		composite_8.setLayout(new GridLayout(2, false));

		Label lblMotor = formToolkit.createLabel(composite_8, "Motor 1 :", SWT.NONE);
		lblMotor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		 motor1mapping = new GkCombo<LabeledValue>(composite_8, SWT.READ_ONLY);
		 Combo combo_11 = motor1mapping.getCombo();
		 GridData gd_combo_11 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		 gd_combo_11.widthHint = 80;
		 combo_11.setLayoutData(gd_combo_11);

		Label lblMotor_1 = formToolkit.createLabel(composite_8, "Motor 2 :", SWT.NONE);
		lblMotor_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		motor2mapping = new GkCombo<LabeledValue>(composite_8, SWT.READ_ONLY);
		Combo combo_12 = motor2mapping.getCombo();
		GridData gd_combo_12 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_12.widthHint = 80;
		combo_12.setLayoutData(gd_combo_12);

		Label lblMotor_2 = formToolkit.createLabel(composite_8, "Motor 3 :", SWT.NONE);
		lblMotor_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		motor3mapping = new GkCombo<LabeledValue>(composite_8, SWT.READ_ONLY);
		Combo combo_13 = motor3mapping.getCombo();
		GridData gd_combo_13 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_13.widthHint = 80;
		combo_13.setLayoutData(gd_combo_13);

		Label lblMotor_3 = formToolkit.createLabel(composite_8, "Motor 4 :", SWT.NONE);
		lblMotor_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		motor4mapping = new GkCombo<LabeledValue>(composite_8, SWT.READ_ONLY);
		Combo combo_14 = motor4mapping.getCombo();
		GridData gd_combo_14 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_14.widthHint = 80;
		combo_14.setLayoutData(gd_combo_14);


		Section sctnMotor_1 = formToolkit.createSection(composite_7, Section.TWISTIE | Section.TITLE_BAR);
		sctnMotor_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.paintBordersFor(sctnMotor_1);
		sctnMotor_1.setText("Motor 1");
		sctnMotor_1.setExpanded(true);

		Composite composite_9 = formToolkit.createComposite(sctnMotor_1, SWT.NONE);
		formToolkit.paintBordersFor(composite_9);
		sctnMotor_1.setClient(composite_9);
		composite_9.setLayout(new GridLayout(5, false));

		Label lblStepAngle = formToolkit.createLabel(composite_9, "Step angle :", SWT.NONE);
		lblStepAngle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtStepAngleM1 = formToolkit.createText(composite_9, "", SWT.NONE);
		GridData gd_txtStepAngleM1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtStepAngleM1.widthHint = 80;
		txtStepAngleM1.setLayoutData(gd_txtStepAngleM1);
		txtStepAngleM1.setText("");
		new Label(composite_9, SWT.NONE);

		Label lblPolarity = formToolkit.createLabel(composite_9, "Polarity :", SWT.NONE);
		lblPolarity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboPolarityM1 = new GkCombo<LabeledValue>(composite_9, SWT.READ_ONLY);
		Combo combo_15 = comboPolarityM1.getCombo();
		GridData gd_combo_15 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_15.widthHint = 80;
		combo_15.setLayoutData(gd_combo_15);

		Label lblTravelPerRevolution = formToolkit.createLabel(composite_9, "Travel per revolution :", SWT.NONE);
		lblTravelPerRevolution.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtTravelPerRevM1 = formToolkit.createText(composite_9, "", SWT.NONE);
		GridData gd_txtTravelPerRevM1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtTravelPerRevM1.widthHint = 80;
		txtTravelPerRevM1.setLayoutData(gd_txtTravelPerRevM1);
		txtTravelPerRevM1.setText("");
		new Label(composite_9, SWT.NONE);

		Label lblPowerManagement = formToolkit.createLabel(composite_9, "Power management :", SWT.NONE);
		lblPowerManagement.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboPowerModeM1 = new GkCombo<LabeledValue>(composite_9, SWT.READ_ONLY);
		Combo combo_16 = comboPowerModeM1.getCombo();
		GridData gd_combo_16 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_16.widthHint = 80;
		combo_16.setLayoutData(gd_combo_16);

		Label lblMicrosteps = formToolkit.createLabel(composite_9, "Microsteps", SWT.NONE);
		lblMicrosteps.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboMicrostepsM1 = new GkCombo<LabeledValue>(composite_9, SWT.READ_ONLY);
		Combo combo_10 = comboMicrostepsM1.getCombo();
		GridData gd_combo_10 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_10.widthHint = 80;
		combo_10.setLayoutData(gd_combo_10);

		new Label(composite_9, SWT.NONE);
		new Label(composite_9, SWT.NONE);
		new Label(composite_9, SWT.NONE);

		Section sctnMotor_2 = formToolkit.createSection(composite_7, Section.TWISTIE | Section.TITLE_BAR);
		sctnMotor_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.paintBordersFor(sctnMotor_2);
		sctnMotor_2.setText("Motor 2");
		sctnMotor_2.setExpanded(true);

		Composite composite_10 = formToolkit.createComposite(sctnMotor_2, SWT.NONE);
		formToolkit.paintBordersFor(composite_10);
		sctnMotor_2.setClient(composite_10);
		composite_10.setLayout(new GridLayout(5, false));

		Label lblStepAngle_1 = formToolkit.createLabel(composite_10, "Step angle :", SWT.NONE);
		lblStepAngle_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtStepAngleM2 = formToolkit.createText(composite_10, "", SWT.NONE);
		GridData gd_txtStepAngleM2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtStepAngleM2.widthHint = 80;
		txtStepAngleM2.setLayoutData(gd_txtStepAngleM2);
		new Label(composite_10, SWT.NONE);

		Label lblPolarity_1 = formToolkit.createLabel(composite_10, "Polarity :", SWT.NONE);
		lblPolarity_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboPolarityM2 = new GkCombo<LabeledValue>(composite_10, SWT.READ_ONLY);
		Combo combo_25 = comboPolarityM2.getCombo();
		GridData gd_combo_25 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_25.widthHint = 80;
		combo_25.setLayoutData(gd_combo_25);

		Label lblTravelPerRevolution_1 = formToolkit.createLabel(composite_10, "Travel per revolution :", SWT.NONE);
		lblTravelPerRevolution_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtTravelPerRevM2 = formToolkit.createText(composite_10, "", SWT.NONE);
		GridData gd_txtTravelPerRevM2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtTravelPerRevM2.widthHint = 80;
		txtTravelPerRevM2.setLayoutData(gd_txtTravelPerRevM2);
		new Label(composite_10, SWT.NONE);

		formToolkit.createLabel(composite_10, "Power management", SWT.NONE);

		comboPowerModeM2 = new GkCombo<LabeledValue>(composite_10, SWT.READ_ONLY);
		Combo combo_23 = comboPowerModeM2.getCombo();
		GridData gd_combo_23 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_23.widthHint = 80;
		combo_23.setLayoutData(gd_combo_23);

		Label lblMicrosteps_1 = formToolkit.createLabel(composite_10, "Microsteps", SWT.NONE);
		lblMicrosteps_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboMicrostepsM2 = new GkCombo<LabeledValue>(composite_10, SWT.READ_ONLY);
		Combo combo_24 = comboMicrostepsM2.getCombo();
		GridData gd_combo_24 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_24.widthHint = 80;
		combo_24.setLayoutData(gd_combo_24);

		new Label(composite_10, SWT.NONE);
		new Label(composite_10, SWT.NONE);
		new Label(composite_10, SWT.NONE);


		Section sctnMotor_3 = formToolkit.createSection(composite_7, Section.TWISTIE | Section.TITLE_BAR);
		sctnMotor_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 4));
		formToolkit.paintBordersFor(sctnMotor_3);
		sctnMotor_3.setText("Motor 3");
		sctnMotor_3.setExpanded(true);

		Composite composite_11 = formToolkit.createComposite(sctnMotor_3, SWT.NONE);
		formToolkit.paintBordersFor(composite_11);
		sctnMotor_3.setClient(composite_11);
		composite_11.setLayout(new GridLayout(5, false));

		Label lblStepAngle_2 = formToolkit.createLabel(composite_11, "Step angle :", SWT.NONE);
		lblStepAngle_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtStepAngleM3 = formToolkit.createText(composite_11, "", SWT.NONE);
		GridData gd_txtStepAngleM3 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtStepAngleM3.widthHint = 80;
		txtStepAngleM3.setLayoutData(gd_txtStepAngleM3);
		new Label(composite_11, SWT.NONE);

		Label lblPolarity_2 = formToolkit.createLabel(composite_11, "Polarity :", SWT.NONE);
		lblPolarity_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboPolarityM3 = new GkCombo<LabeledValue>(composite_11, SWT.READ_ONLY);
		Combo combo_22 = comboPolarityM3.getCombo();
		GridData gd_combo_22 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_22.widthHint = 80;
		combo_22.setLayoutData(gd_combo_22);

		Label lblTravelPerRevolution_2 = formToolkit.createLabel(composite_11, "Travel per revolution :", SWT.NONE);
		lblTravelPerRevolution_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtTravelPerRevM3 = formToolkit.createText(composite_11, "", SWT.NONE);
		GridData gd_txtTravelPerRevM3 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtTravelPerRevM3.widthHint = 80;
		txtTravelPerRevM3.setLayoutData(gd_txtTravelPerRevM3);
		new Label(composite_11, SWT.NONE);

		Label lblPowerManagement_2 = formToolkit.createLabel(composite_11, "Power management :", SWT.NONE);
		lblPowerManagement_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboPowerModeM3 = new GkCombo<LabeledValue>(composite_11, SWT.READ_ONLY);
		Combo combo_21 = comboPowerModeM3.getCombo();
		GridData gd_combo_21 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_21.widthHint = 80;
		combo_21.setLayoutData(gd_combo_21);

		Label lblMicrosteps_2 = formToolkit.createLabel(composite_11, "Microsteps :", SWT.NONE);
		lblMicrosteps_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboMicrostepsM3 = new GkCombo<LabeledValue>(composite_11, SWT.READ_ONLY);
		Combo combo_20 = comboMicrostepsM3.getCombo();
		GridData gd_combo_20 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_20.widthHint = 80;
		combo_20.setLayoutData(gd_combo_20);

		new Label(composite_11, SWT.NONE);
		new Label(composite_11, SWT.NONE);
		new Label(composite_11, SWT.NONE);



		Section sctnMotor_4 = formToolkit.createSection(composite_7, Section.TWISTIE | Section.TITLE_BAR);
		sctnMotor_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnMotor_4);
		sctnMotor_4.setText("Motor 4");
		sctnMotor_4.setExpanded(true);

		Composite composite_12 = formToolkit.createComposite(sctnMotor_4, SWT.NONE);
		formToolkit.paintBordersFor(composite_12);
		sctnMotor_4.setClient(composite_12);
		composite_12.setLayout(new GridLayout(5, false));

		Label lblStepAngle_3 = formToolkit.createLabel(composite_12, "Step angle :", SWT.NONE);
		lblStepAngle_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtStepAngleM4 = formToolkit.createText(composite_12, "", SWT.NONE);
		GridData gd_txtStepAngleM4 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtStepAngleM4.widthHint = 80;
		txtStepAngleM4.setLayoutData(gd_txtStepAngleM4);
		new Label(composite_12, SWT.NONE);

		Label lblPolarity_3 = formToolkit.createLabel(composite_12, "Polarity :", SWT.NONE);
		lblPolarity_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboPolarityM4 = new GkCombo<LabeledValue>(composite_12, SWT.READ_ONLY);
		Combo combo_19 = comboPolarityM4.getCombo();
		GridData gd_combo_19 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_19.widthHint = 80;
		combo_19.setLayoutData(gd_combo_19);

		Label lblTravelPerRevolution_3 = formToolkit.createLabel(composite_12, "Travel per revolution :", SWT.NONE);
		lblTravelPerRevolution_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtTravelPerRevM4 = formToolkit.createText(composite_12, "", SWT.NONE);
		GridData gd_txtTravelPerRevM4 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtTravelPerRevM4.widthHint = 80;
		txtTravelPerRevM4.setLayoutData(gd_txtTravelPerRevM4);
		new Label(composite_12, SWT.NONE);

		Label lblPowerManagement_3 = formToolkit.createLabel(composite_12, "Power management :", SWT.NONE);
		lblPowerManagement_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboPowerModeM4 = new GkCombo<LabeledValue>(composite_12, SWT.READ_ONLY);
		Combo combo_18 = comboPowerModeM4.getCombo();
		GridData gd_combo_18 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_18.widthHint = 80;
		combo_18.setLayoutData(gd_combo_18);

		Label lblMicrosteps_3 = formToolkit.createLabel(composite_12, "Microsteps :", SWT.NONE);
		lblMicrosteps_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboMicrostepsM4 = new GkCombo<LabeledValue>(composite_12, SWT.READ_ONLY);
		Combo combo_17 = comboMicrostepsM4.getCombo();
		GridData gd_combo_17 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_17.widthHint = 80;
		combo_17.setLayoutData(gd_combo_17);

		new Label(composite_12, SWT.NONE);
		new Label(composite_12, SWT.NONE);
		new Label(composite_12, SWT.NONE);

		CTabItem tbtmAxes = new CTabItem(settingsTabFolder, SWT.NONE);
		tbtmAxes.setText("Axes");

		ScrolledForm formAxes = formToolkit.createScrolledForm(settingsTabFolder);
		tbtmAxes.setControl(formAxes);
		formToolkit.paintBordersFor(formAxes);
		formAxes.setText("Axes settings");
		formAxes.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite_13 = formToolkit.createComposite(formAxes.getBody(), SWT.NONE);
		formToolkit.paintBordersFor(composite_13);
		composite_13.setLayout(new GridLayout(1, false));

		Section sctnNewSection_2 = formToolkit.createSection(composite_13, Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnNewSection_2);
		sctnNewSection_2.setText("Axis");
		sctnNewSection_2.setExpanded(true);

		Composite composite_14 = formToolkit.createComposite(sctnNewSection_2, SWT.NONE);
		formToolkit.paintBordersFor(composite_14);
		sctnNewSection_2.setClient(composite_14);
		composite_14.setLayout(new GridLayout(1, false));

		Composite composite_15 = formToolkit.createComposite(composite_14, SWT.NONE);
		formToolkit.paintBordersFor(composite_15);
		composite_15.setLayout(new GridLayout(2, false));

		formToolkit.createLabel(composite_15, "Axis selection :", SWT.NONE);

		axisSelectionCombo = new  GkCombo<LabeledValue>(composite_15, SWT.NONE | SWT.READ_ONLY);
		Combo combo_26 = axisSelectionCombo.getCombo();
		GridData gd_combo_26 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_26.widthHint = 80;
		combo_26.setLayoutData(gd_combo_26);

		Label label = formToolkit.createSeparator(composite_14, SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite_16 = formToolkit.createComposite(composite_14, SWT.NONE);
		composite_16.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		formToolkit.paintBordersFor(composite_16);
		composite_16.setLayout(new GridLayout(2, false));

		Label lblVelocityMaximum = formToolkit.createLabel(composite_16, "Velocity maximum :", SWT.NONE);
		lblVelocityMaximum.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtMaxVelocity = formToolkit.createText(composite_16, "", SWT.NONE);
		GridData gd_txtMaxVelocity = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtMaxVelocity.widthHint = 80;
		txtMaxVelocity.setLayoutData(gd_txtMaxVelocity);

		Label lblMaximumFeedRate = formToolkit.createLabel(composite_16, "Maximum feed rate :", SWT.NONE);
		lblMaximumFeedRate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtMaxFeedrate = formToolkit.createText(composite_16, "", SWT.NONE);
		GridData gd_txtMaxFeedrate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtMaxFeedrate.widthHint = 80;
		txtMaxFeedrate.setLayoutData(gd_txtMaxFeedrate);

		Label lblMaximumTravel = formToolkit.createLabel(composite_16, "Travel maximum :", SWT.NONE);
		lblMaximumTravel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtMaxTravel = formToolkit.createText(composite_16, "", SWT.NONE);
		GridData gd_txtMaxTravel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtMaxTravel.widthHint = 80;
		txtMaxTravel.setLayoutData(gd_txtMaxTravel);

		Label lblMaximumJerk = formToolkit.createLabel(composite_16, "Jerk maximum :", SWT.NONE);
		lblMaximumJerk.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtMaxJerk = formToolkit.createText(composite_16, "", SWT.NONE);
		GridData gd_txtMaxJerk = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtMaxJerk.widthHint = 80;
		txtMaxJerk.setLayoutData(gd_txtMaxJerk);

		Label lblJerkHoming = new Label(composite_16, SWT.NONE);
		lblJerkHoming.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblJerkHoming, true, true);
		lblJerkHoming.setText("Jerk homing :");

		txtJerkHoming = new Text(composite_16, SWT.BORDER);
		GridData gd_txtJerkHoming = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtJerkHoming.widthHint = 80;
		txtJerkHoming.setLayoutData(gd_txtJerkHoming);
		formToolkit.adapt(txtJerkHoming, true, true);

		Label lblJunctionDeviation = formToolkit.createLabel(composite_16, "Junction deviation :", SWT.NONE);
		lblJunctionDeviation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtJunctionDeviation = formToolkit.createText(composite_16, "", SWT.NONE);
		GridData gd_txtJunctionDeviation = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtJunctionDeviation.widthHint = 80;
		txtJunctionDeviation.setLayoutData(gd_txtJunctionDeviation);

		Label lblRadiusValue = formToolkit.createLabel(composite_16, "Radius value :", SWT.NONE);
		lblRadiusValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtRadiusValue = formToolkit.createText(composite_16, "", SWT.NONE);
		GridData gd_txtRadiusValue = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtRadiusValue.widthHint = 80;
		txtRadiusValue.setLayoutData(gd_txtRadiusValue);

		Label lblSwitchMode = formToolkit.createLabel(composite_16, "Minimum switch mode :", SWT.NONE);
		lblSwitchMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboMinSwitchMode = new GkCombo<LabeledValue>(composite_16, SWT.NONE | SWT.READ_ONLY);
		Combo combo_27 = comboMinSwitchMode.getCombo();
		GridData gd_combo_27 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_27.widthHint = 80;
		combo_27.setLayoutData(gd_combo_27);

		Label lblNewLabel = new Label(composite_16, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("Maximum switch mode :");

		comboMaxSwitchMode = new GkCombo<LabeledValue>(composite_16, SWT.NONE | SWT.READ_ONLY);
		Combo combo_28 = comboMaxSwitchMode.getCombo();
		GridData gd_combo_28 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_28.widthHint = 80;
		combo_28.setLayoutData(gd_combo_28);

		Label lblSearchVelocity = new Label(composite_16, SWT.NONE);
		lblSearchVelocity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblSearchVelocity, true, true);
		lblSearchVelocity.setText("Search velocity :");

		txtSearchVelocity = new Text(composite_16, SWT.BORDER);
		GridData gd_txtSearchVelocity = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtSearchVelocity.widthHint = 80;
		txtSearchVelocity.setLayoutData(gd_txtSearchVelocity);
		formToolkit.adapt(txtSearchVelocity, true, true);

		Label lblLatchVelocity = new Label(composite_16, SWT.NONE);
		lblLatchVelocity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblLatchVelocity, true, true);
		lblLatchVelocity.setText("Latch velocity :");

		txtLatchVelocity = new Text(composite_16, SWT.BORDER);
		GridData gd_txtLatchVelocity = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtLatchVelocity.widthHint = 80;
		txtLatchVelocity.setLayoutData(gd_txtLatchVelocity);
		formToolkit.adapt(txtLatchVelocity, true, true);

		Label lblZeroBackoff = new Label(composite_16, SWT.NONE);
		lblZeroBackoff.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblZeroBackoff, true, true);
		lblZeroBackoff.setText("Zero backoff :");

		txtZeroBackoff = new Text(composite_16, SWT.BORDER);
		GridData gd_txtZeroBackoff = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtZeroBackoff.widthHint = 80;
		txtZeroBackoff.setLayoutData(gd_txtZeroBackoff);
		formToolkit.adapt(txtZeroBackoff, true, true);

		Button btnApply = new Button(composite_13, SWT.NONE);
		btnApply.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(btnApply, true, true);
		btnApply.setText("Apply axis settings");
		labelValidationMessages = new CLabel(frmNewForm.getHead(), SWT.NONE);
		frmNewForm.setHeadClient(labelValidationMessages);
		labelValidationMessages.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelValidationMessages.setImage(ResourceManager.getPluginImage("org.goko.core", "icons/cross-circle.png"));
		labelValidationMessages.setForeground(SWTResourceManager.getColor(0, 0, 0));

		/*Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					if(controller.validate()){
						controller.applySettingChanges();
					}
				} catch (GkException e1) {
					GkLog.error(e1);
				}
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnNewButton.setText("Apply");

		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.setText("Cancel");

		m_bindingContext = initDataBindings();
*/
		initCustomDataBindings();
		controller.refreshConfiguration();
	}

	@EventListener(ConfigurationUpdateEvent.class)
	public void onControllerConfigurationRefreshed(ConfigurationUpdateEvent event){
		try {
			this.controller.refreshConfiguration();
		} catch (GkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void initCustomDataBindings() throws GkException {
		this.controller.addValidationMessagesBinding(labelValidationMessages);


		this.controller.addBigDecimalModifyBinding(txtJunctionAcceleration, 	"junctionAcceleration");

		this.controller.addTextModifyBinding(txtFirmwarebuild, 			"firmwareBuild");
		this.controller.addTextModifyBinding(txtFirmwareversion, 		"firmwareVersion");
		this.controller.addTextModifyBinding(txtJunctionDeviation, 		"junctionDeviation");
		this.controller.addBigDecimalModifyBinding(txtMaxJerk, 				"maxJerk");
		this.controller.addBigDecimalModifyBinding(txtMaxTravel, 				"maxTravel");
		this.controller.addBigDecimalModifyBinding(txtMaxVelocity, 			"maxVelocity");
		this.controller.addBigDecimalModifyBinding(txtRadiusValue, 			"radiusValue");

		this.controller.addBigDecimalModifyBinding(txtStatusinterval, 		"statusinterval");
		this.controller.addBigDecimalModifyBinding(txtStepAngleM1, 			"stepAngleM1");
		this.controller.addBigDecimalModifyBinding(txtTravelPerRevM1, 		"travelPerRevM1");
		this.controller.addBigDecimalModifyBinding(txtStepAngleM2, 			"stepAngleM2");
		this.controller.addBigDecimalModifyBinding(txtTravelPerRevM2, 		"travelPerRevM2");
		this.controller.addBigDecimalModifyBinding(txtStepAngleM3, 			"stepAngleM3");
		this.controller.addBigDecimalModifyBinding(txtTravelPerRevM3, 		"travelPerRevM3");
		this.controller.addBigDecimalModifyBinding(txtStepAngleM4, 			"stepAngleM4");
		this.controller.addBigDecimalModifyBinding(txtTravelPerRevM4, 		"travelPerRevM4");

		this.controller.addBigDecimalModifyBinding(txtChordalTolerance, 		"chordalTolerance");
		this.controller.addBigDecimalModifyBinding(txtMotorDisableTimeout, 	"motorDisableTimeout");

		this.controller.addItemsBinding(comboSwitchType, 		"choicesSwitchType");
		this.controller.addItemSelectionBinding(comboSwitchType, "switchType");
		this.controller.addItemsBinding(comboPlaneSelection		, "choicesPlaneSelection");
		this.controller.addItemSelectionBinding(comboPlaneSelection, "defaultPlaneSelection");

		this.controller.addItemsBinding(comboUnits				, "choicesUnits");
		this.controller.addItemSelectionBinding(comboUnits, "defaultUnitsMode");

		this.controller.addItemsBinding(comboCoordinateSystem	, 	"choicesCoordinateSystem");
		this.controller.addItemSelectionBinding(comboCoordinateSystem, "defaultCoordinateSystem");

		this.controller.addItemsBinding(comboPathControl		, "choicesPathControl");
		this.controller.addItemSelectionBinding(comboPathControl, "defaultPathControl");

		this.controller.addItemsBinding(comboDistanceMode		,  "choicesDistanceMode");
		this.controller.addItemSelectionBinding(comboDistanceMode, "defaultDistanceMode");

		this.controller.addItemsBinding(axisSelectionCombo, 	"choicesConfigurableAxes");
		this.controller.addItemSelectionBinding(axisSelectionCombo, "selectedAxis");

		this.controller.addItemsBinding(comboBaudrate, 	"choicesBaudrate");
		this.controller.addItemSelectionBinding(comboBaudrate, "baudrate");

		/*
		 * Axis values
		 */
		this.controller.addBigDecimalModifyBinding(txtMaxFeedrate, 		"maximumFeedrate");
		this.controller.addBigDecimalModifyBinding(txtMaxVelocity, 		"velocityMaximum");
		this.controller.addBigDecimalModifyBinding(txtMaxTravel, 			"travelMaximum");
		this.controller.addBigDecimalModifyBinding(txtMaxJerk, 			"jerkMaximum");
		this.controller.addBigDecimalModifyBinding(txtJunctionDeviation, 	"junctionDeviation");
		this.controller.addBigDecimalModifyBinding(txtRadiusValue, 		"radiusValue");
		this.controller.addBigDecimalModifyBinding(txtSearchVelocity, 	"searchVelocity");
		this.controller.addBigDecimalModifyBinding(txtLatchVelocity, 		"latchVelocity");
		this.controller.addBigDecimalModifyBinding(txtZeroBackoff, 		"zeroBackoff");
		this.controller.addBigDecimalModifyBinding(txtJerkHoming, 		"jerkHoming");
		this.controller.addBigDecimalModifyBinding(txtDeviceId, 			"uniqueId");
		this.controller.addItemsBinding(comboMaxSwitchMode, "choicesSwitchModes");
		this.controller.addItemSelectionBinding(comboMaxSwitchMode, "maxSwitchMode");
		this.controller.addItemsBinding(comboMinSwitchMode, "choicesSwitchModes");
		this.controller.addItemSelectionBinding(comboMinSwitchMode, "minSwitchMode");

		/*
		 *  Motor values
		 */
		this.controller.addItemsBinding(motor1mapping, "choicesMotorMapping");
		this.controller.addItemSelectionBinding(motor1mapping, "axisForMotor1");
		this.controller.addItemsBinding(motor2mapping, "choicesMotorMapping");
		this.controller.addItemSelectionBinding(motor2mapping, "axisForMotor2");
		this.controller.addItemsBinding(motor3mapping, "choicesMotorMapping");
		this.controller.addItemSelectionBinding(motor3mapping, "axisForMotor3");
		this.controller.addItemsBinding(motor4mapping, "choicesMotorMapping");
		this.controller.addItemSelectionBinding(motor4mapping, "axisForMotor4");

		// Motor 1
		this.controller.addBigDecimalModifyBinding(txtStepAngleM1, 		"motor1StepAngle");
		this.controller.addBigDecimalModifyBinding(txtTravelPerRevM1, 	"motor1TravelPerRevolution");
		this.controller.addItemsBinding(comboPolarityM1, "choicesPolarity");
		this.controller.addItemSelectionBinding(comboPolarityM1, "motor1Polarity");
		this.controller.addItemsBinding(comboMicrostepsM1, "choicesMicrosteps");
		this.controller.addItemSelectionBinding(comboMicrostepsM1, "motor1Microsteps");
		this.controller.addItemsBinding(comboPowerModeM1, "choicesPowerManagement");
		this.controller.addItemSelectionBinding(comboPowerModeM1, "motor1PowerManagement");

		// Motor 2
		this.controller.addBigDecimalModifyBinding(txtStepAngleM2, 		"motor2StepAngle");
		this.controller.addBigDecimalModifyBinding(txtTravelPerRevM2, 	"motor2TravelPerRevolution");
		this.controller.addItemsBinding(comboPolarityM2, "choicesPolarity");
		this.controller.addItemSelectionBinding(comboPolarityM2, "motor2Polarity");
		this.controller.addItemsBinding(comboMicrostepsM2, "choicesMicrosteps");
		this.controller.addItemSelectionBinding(comboMicrostepsM2, "motor2Microsteps");
		this.controller.addItemsBinding(comboPowerModeM2, "choicesPowerManagement");
		this.controller.addItemSelectionBinding(comboPowerModeM2, "motor2PowerManagement");

		// Motor 3
		this.controller.addBigDecimalModifyBinding(txtStepAngleM3, 		"motor3StepAngle");
		this.controller.addBigDecimalModifyBinding(txtTravelPerRevM3, 	"motor3TravelPerRevolution");
		this.controller.addItemsBinding(comboPolarityM3, "choicesPolarity");
		this.controller.addItemSelectionBinding(comboPolarityM3, "motor3Polarity");
		this.controller.addItemsBinding(comboMicrostepsM3, "choicesMicrosteps");
		this.controller.addItemSelectionBinding(comboMicrostepsM3, "motor3Microsteps");
		this.controller.addItemsBinding(comboPowerModeM3, "choicesPowerManagement");
		this.controller.addItemSelectionBinding(comboPowerModeM3, "motor3PowerManagement");

		// Motor 4
		this.controller.addBigDecimalModifyBinding(txtStepAngleM4, 		"motor4StepAngle");
		this.controller.addBigDecimalModifyBinding(txtTravelPerRevM4, 	"motor4TravelPerRevolution");
		this.controller.addItemsBinding(comboPolarityM4, "choicesPolarity");
		this.controller.addItemSelectionBinding(comboPolarityM4, "motor4Polarity");
		this.controller.addItemsBinding(comboMicrostepsM4, "choicesMicrosteps");
		this.controller.addItemSelectionBinding(comboMicrostepsM4, "motor4Microsteps");
		this.controller.addItemsBinding(comboPowerModeM4, "choicesPowerManagement");
		this.controller.addItemSelectionBinding(comboPowerModeM4, "motor4PowerManagement");

		// Communication
		this.controller.addItemsBinding(comboJSONVerbosity, "choicesJSonVerbosity");
		this.controller.addItemSelectionBinding(comboJSONVerbosity, "jsonVerbosity");

		this.controller.addItemsBinding(comboQueueReportVerbosity, "choicesExtendedVerbosity");
		this.controller.addItemSelectionBinding(comboQueueReportVerbosity, "queueReportVerbosity");

		this.controller.addItemsBinding(comboStatusReportVerbosity, "choicesExtendedVerbosity");
		this.controller.addItemSelectionBinding(comboStatusReportVerbosity, "statusReportVerbosity");

		this.controller.addItemsBinding(comboTextModeVerbsity, "choicesVerbosity");
		this.controller.addItemSelectionBinding(comboTextModeVerbsity, "textVerbosity");

		this.controller.addCheckboxSelectionBinding(chckboxIgnoreCrRx, "ignoreCrOnRx" );
		this.controller.addCheckboxSelectionBinding(chckboxIgnoreLfRx, "ignoreLfOnRx" );
		this.controller.addCheckboxSelectionBinding(chckboxEnableCrTx, "enableCrOnTx" );
		this.controller.addCheckboxSelectionBinding(chckboxEnableXonXoff, "enableXonXoff" );
		this.controller.addCheckboxSelectionBinding(chckboxEnableEcho, "enableEcho" );

	}


	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		super.okPressed();
		try{
			if(controller.validate()){
				controller.applySettingChanges();
				close();
			}
		}catch(GkException e){
			LOG.error(e);
		}

	}

}
