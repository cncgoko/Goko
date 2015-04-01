package org.goko.tinyg.configuration;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
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
import org.goko.common.elements.combo.v2.GkCombo2;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.tinyg.configuration.bindings.ConfigurationBindings;
import org.goko.tinyg.configuration.bindings.ConfigurationController;
import org.goko.tinyg.configuration.bindings.wrapper.TinyGAAxisSettingsWrapper;
import org.goko.tinyg.configuration.bindings.wrapper.TinyGLinearAxisSettingsWrapper;
import org.goko.tinyg.controller.configuration.TinyGLinearAxisSettings;

public class TinyGConfigurationDialog extends Dialog{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGConfigurationDialog.class);

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
	private ConfigurationController controller;
	private GkCombo<LabeledValue> motor1mapping;
	private GkCombo<LabeledValue> motor2mapping;
	private GkCombo<LabeledValue> motor3mapping;
	private GkCombo<LabeledValue> motor4mapping;
	private GkCombo<LabeledValue> comboMicrostepsM1;
	private GkCombo<LabeledValue> comboMicrostepsM2;
	private GkCombo<LabeledValue> comboMicrostepsM3;
	private GkCombo<LabeledValue> comboMicrostepsM4;
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
	private Text xvm;
	private Text xfr;
	private Text xtn;
	private Text xtm;
	private Text xjm;
	private Text xjh;
	private Text xjd;
	private Text xsv;
	private Text xlv;
	private Text xlb;
	private Text xzb;
	private Text yvm;
	private Text zvm;
	private Text avm;
	private Text yfr;
	private Text ytn;
	private Text ytm;
	private Text yjm;
	private Text yjh;
	private Text yjd;
	private Text ara;
	private Text ysv;
	private Text ylv;
	private Text ylb;
	private Text yzb;
	private Text zfr;
	private Text ztn;
	private Text ztm;
	private Text zjm;
	private Text zjh;
	private Text zjd;
	private Text zsv;
	private Text zlv;
	private Text zlb;
	private Text zzb;
	private Text asv;
	private Text alv;
	private Text alb;
	private Text azb;
	private Text afr;
	private Text atn;
	private Text atm;
	private Text ajm;
	private Text ajh;
	private Text ajd;

	private GkCombo2<BigDecimal> xamCombo;

	private GkCombo2<BigDecimal> yamCombo;

	private GkCombo2<BigDecimal> zamCombo;

	private GkCombo2<BigDecimal> aamCombo;

	private GkCombo2<BigDecimal> xsnCombo;

	private GkCombo2<BigDecimal> ysnCombo;

	private GkCombo2<BigDecimal> zsnCombo;

	private GkCombo2<BigDecimal> asnCombo;

	private GkCombo2<BigDecimal> xsmCombo;

	private GkCombo2<BigDecimal> ysmCombo;

	private GkCombo2<BigDecimal> zsmCombo;

	private GkCombo2<BigDecimal> asmCombo;


	public TinyGConfigurationDialog(Shell shell, IEclipseContext context) {
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
				controller.requestConfigurationRefresh();
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
		GridLayout gl_composite_4 = new GridLayout(1, false);
		gl_composite_4.marginWidth = 10;
		gl_composite_4.marginHeight = 10;
		composite_4.setLayout(gl_composite_4);
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

		txtFirmwareversion = formToolkit.createText(composite, "", SWT.READ_ONLY);
		txtFirmwareversion.setEditable(false);
		txtFirmwareversion.setText("firmwareVersion");
		GridData gd_txtFirmwareversion = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtFirmwareversion.widthHint = 80;
		txtFirmwareversion.setLayoutData(gd_txtFirmwareversion);

		Label lblFirmwareBuild = formToolkit.createLabel(composite, "Firmware build :", SWT.NONE);
		lblFirmwareBuild.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtFirmwarebuild = formToolkit.createText(composite, "", SWT.READ_ONLY);
		txtFirmwarebuild.setEditable(false);
		txtFirmwarebuild.setText("firmwareBuild");
		GridData gd_txtFirmwarebuild = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtFirmwarebuild.widthHint = 80;
		txtFirmwarebuild.setLayoutData(gd_txtFirmwarebuild);

		Label lblStatusInterval = formToolkit.createLabel(composite, "Device ID :", SWT.NONE);
		lblStatusInterval.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtDeviceId = formToolkit.createText(composite, "New Text", SWT.READ_ONLY);
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
		sctnNewSection_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(sctnNewSection_2);
		sctnNewSection_2.setText("Axis");
		sctnNewSection_2.setExpanded(true);

		Composite composite_14 = formToolkit.createComposite(sctnNewSection_2, SWT.NONE);
		formToolkit.paintBordersFor(composite_14);
		sctnNewSection_2.setClient(composite_14);
		composite_14.setLayout(new GridLayout(1, false));

		Composite composite_1 = new Composite(composite_14, SWT.NONE);
		composite_1.setLayout(new GridLayout(5, false));
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1);
		gd_composite_1.heightHint = 124;
		composite_1.setLayoutData(gd_composite_1);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		new Label(composite_1, SWT.NONE);

		Label lblXAxis = new Label(composite_1, SWT.NONE);
		lblXAxis.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblXAxis.setAlignment(SWT.CENTER);
		GridData gd_lblXAxis = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblXAxis.widthHint = 80;
		lblXAxis.setLayoutData(gd_lblXAxis);
		formToolkit.adapt(lblXAxis, true, true);
		lblXAxis.setText("X axis");

		Label lblYAxis = new Label(composite_1, SWT.NONE);
		GridData gd_lblYAxis = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblYAxis.widthHint = 80;
		lblYAxis.setLayoutData(gd_lblYAxis);
		lblYAxis.setText("Y axis");
		lblYAxis.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblYAxis.setAlignment(SWT.CENTER);
		formToolkit.adapt(lblYAxis, true, true);

		Label lblZAxis = new Label(composite_1, SWT.NONE);
		GridData gd_lblZAxis = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblZAxis.widthHint = 80;
		lblZAxis.setLayoutData(gd_lblZAxis);
		lblZAxis.setText("Z axis");
		lblZAxis.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblZAxis.setAlignment(SWT.CENTER);
		formToolkit.adapt(lblZAxis, true, true);

		Label lblAAxis = new Label(composite_1, SWT.NONE);
		GridData gd_lblAAxis = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblAAxis.widthHint = 80;
		lblAAxis.setLayoutData(gd_lblAAxis);
		lblAAxis.setText("A axis");
		lblAAxis.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblAAxis.setAlignment(SWT.CENTER);
		formToolkit.adapt(lblAAxis, true, true);

		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("Axis mode");

		xamCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo xam = xamCombo.getCombo();
		xam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.paintBordersFor(xam);

		yamCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo yam = yamCombo.getCombo();
		yam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.paintBordersFor(yam);

		zamCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo zam = zamCombo.getCombo();
		zam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(zam);

		aamCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo aam = aamCombo.getCombo();
		aam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(aam);

		Label lblVelocityMax = new Label(composite_1, SWT.NONE);
		lblVelocityMax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblVelocityMax, true, true);
		lblVelocityMax.setText("Velocity max.");

		xvm = new Text(composite_1, SWT.BORDER);
		xvm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xvm, true, true);

		yvm = new Text(composite_1, SWT.BORDER);
		yvm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(yvm, true, true);

		zvm = new Text(composite_1, SWT.BORDER);
		zvm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(zvm, true, true);

		avm = new Text(composite_1, SWT.BORDER);
		avm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(avm, true, true);

		Label lblFeedrateMax = new Label(composite_1, SWT.NONE);
		lblFeedrateMax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblFeedrateMax, true, true);
		lblFeedrateMax.setText("Feedrate max.");

		xfr = new Text(composite_1, SWT.BORDER);
		xfr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xfr, true, true);

		yfr = new Text(composite_1, SWT.BORDER);
		yfr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(yfr, true, true);

		zfr = new Text(composite_1, SWT.BORDER);
		zfr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(zfr, true, true);

		afr = new Text(composite_1, SWT.BORDER);
		afr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(afr, true, true);

		Label lblTravelMin = new Label(composite_1, SWT.NONE);
		lblTravelMin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblTravelMin, true, true);
		lblTravelMin.setText("Travel min.");

		xtn = new Text(composite_1, SWT.BORDER);
		xtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xtn, true, true);

		ytn = new Text(composite_1, SWT.BORDER);
		ytn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ytn, true, true);

		ztn = new Text(composite_1, SWT.BORDER);
		ztn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ztn, true, true);

		atn = new Text(composite_1, SWT.BORDER);
		atn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(atn, true, true);

		Label lblTravelMax = new Label(composite_1, SWT.NONE);
		lblTravelMax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblTravelMax, true, true);
		lblTravelMax.setText("Travel max.");

		xtm = new Text(composite_1, SWT.BORDER);
		xtm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xtm, true, true);

		ytm = new Text(composite_1, SWT.BORDER);
		ytm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ytm, true, true);

		ztm = new Text(composite_1, SWT.BORDER);
		ztm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ztm, true, true);

		atm = new Text(composite_1, SWT.BORDER);
		atm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(atm, true, true);

		Label lblJerkMax = new Label(composite_1, SWT.NONE);
		lblJerkMax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblJerkMax, true, true);
		lblJerkMax.setText("Jerk max.");

		xjm = new Text(composite_1, SWT.BORDER);
		xjm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xjm, true, true);

		yjm = new Text(composite_1, SWT.BORDER);
		yjm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(yjm, true, true);

		zjm = new Text(composite_1, SWT.BORDER);
		zjm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(zjm, true, true);

		ajm = new Text(composite_1, SWT.BORDER);
		ajm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ajm, true, true);

		Label lblJerkHoming = new Label(composite_1, SWT.NONE);
		lblJerkHoming.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblJerkHoming, true, true);
		lblJerkHoming.setText("Jerk homing");

		xjh = new Text(composite_1, SWT.BORDER);
		xjh.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xjh, true, true);

		yjh = new Text(composite_1, SWT.BORDER);
		yjh.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(yjh, true, true);

		zjh = new Text(composite_1, SWT.BORDER);
		zjh.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(zjh, true, true);

		ajh = new Text(composite_1, SWT.BORDER);
		ajh.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ajh, true, true);

		Label lblJunctionDeviation = new Label(composite_1, SWT.NONE);
		lblJunctionDeviation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblJunctionDeviation, true, true);
		lblJunctionDeviation.setText("Junction deviation");

		xjd = new Text(composite_1, SWT.BORDER);
		xjd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xjd, true, true);

		yjd = new Text(composite_1, SWT.BORDER);
		yjd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(yjd, true, true);

		zjd = new Text(composite_1, SWT.BORDER);
		zjd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(zjd, true, true);

		ajd = new Text(composite_1, SWT.BORDER);
		ajd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ajd, true, true);

		Label lblRadiusSetting = new Label(composite_1, SWT.NONE);
		lblRadiusSetting.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblRadiusSetting, true, true);
		lblRadiusSetting.setText("Radius setting");
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);

		ara = new Text(composite_1, SWT.BORDER);
		ara.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ara, true, true);

		Label lblMinSwitchMode = new Label(composite_1, SWT.NONE);
		lblMinSwitchMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblMinSwitchMode, true, true);
		lblMinSwitchMode.setText("Min. switch mode");

		xsnCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo xsn = xsnCombo.getCombo();
		xsn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.paintBordersFor(xsn);

		ysnCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo ysn = ysnCombo.getCombo();
		ysn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(ysn);

		zsnCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo zsn = zsnCombo.getCombo();
		zsn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(zsn);

		asnCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo asn = asnCombo.getCombo();
		asn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(asn);

		Label lblMaxSwitchMode = new Label(composite_1, SWT.NONE);
		lblMaxSwitchMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblMaxSwitchMode, true, true);
		lblMaxSwitchMode.setText("Max. switch mode");

		xsmCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo xsm = xsmCombo.getCombo();
		xsm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.paintBordersFor(xsm);

		ysmCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo ysm = ysmCombo.getCombo();
		ysm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(ysm);

		zsmCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo zsm = zsmCombo.getCombo();
		zsm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(zsm);

		asmCombo = new GkCombo2<BigDecimal>(composite_1, SWT.READ_ONLY);
		Combo asm = asmCombo.getCombo();
		asm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(asm);

		Label lblSearchVelocity = new Label(composite_1, SWT.NONE);
		lblSearchVelocity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblSearchVelocity, true, true);
		lblSearchVelocity.setText("Search velocity");

		xsv = new Text(composite_1, SWT.BORDER);
		xsv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xsv, true, true);

		ysv = new Text(composite_1, SWT.BORDER);
		ysv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ysv, true, true);

		zsv = new Text(composite_1, SWT.BORDER);
		zsv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(zsv, true, true);

		asv = new Text(composite_1, SWT.BORDER);
		asv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(asv, true, true);

		Label lblLatchVelocity = new Label(composite_1, SWT.NONE);
		lblLatchVelocity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblLatchVelocity, true, true);
		lblLatchVelocity.setText("Latch velocity");

		xlv = new Text(composite_1, SWT.BORDER);
		xlv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xlv, true, true);

		ylv = new Text(composite_1, SWT.BORDER);
		ylv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ylv, true, true);

		zlv = new Text(composite_1, SWT.BORDER);
		zlv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(zlv, true, true);

		alv = new Text(composite_1, SWT.BORDER);
		alv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(alv, true, true);

		Label lblLatchBackoff = new Label(composite_1, SWT.NONE);
		lblLatchBackoff.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblLatchBackoff, true, true);
		lblLatchBackoff.setText("Latch backoff");

		xlb = new Text(composite_1, SWT.BORDER);
		xlb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xlb, true, true);

		ylb = new Text(composite_1, SWT.BORDER);
		ylb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(ylb, true, true);

		zlb = new Text(composite_1, SWT.BORDER);
		zlb.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		formToolkit.adapt(zlb, true, true);

		alb = new Text(composite_1, SWT.BORDER);
		alb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(alb, true, true);

		Label lblZeroBackoff = new Label(composite_1, SWT.NONE);
		lblZeroBackoff.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblZeroBackoff, true, true);
		lblZeroBackoff.setText("Zero backoff");

		xzb = new Text(composite_1, SWT.BORDER);
		xzb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(xzb, true, true);

		yzb = new Text(composite_1, SWT.BORDER);
		yzb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(yzb, true, true);

		zzb = new Text(composite_1, SWT.BORDER);
		zzb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(zzb, true, true);

		azb = new Text(composite_1, SWT.BORDER);
		azb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(azb, true, true);
		labelValidationMessages = new CLabel(frmNewForm.getHead(), SWT.NONE);
		frmNewForm.setHeadClient(labelValidationMessages);
		labelValidationMessages.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		labelValidationMessages.setImage(ResourceManager.getPluginImage("org.goko.core", "icons/cross-circle.png"));
		labelValidationMessages.setForeground(SWTResourceManager.getColor(0, 0, 0));
		initCustomDataBindings();
		controller.refreshConfiguration();
	}



	protected void createColumn(TableViewer tableViewer, String title, int columnIndex){
	}

	protected void initCustomDataBindings() throws GkException {
		this.controller.addValidationMessagesBinding(labelValidationMessages);


		this.controller.addBigDecimalModifyBinding(txtJunctionAcceleration, 	"junctionAcceleration");

		this.controller.addBigDecimalModifyBinding(txtFirmwarebuild, 		"firmwareBuild");
		this.controller.addBigDecimalModifyBinding(txtFirmwareversion, 		"firmwareVersion");

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

		this.controller.addItemsBinding(comboBaudrate, 	"choicesBaudrate");
		this.controller.addItemSelectionBinding(comboBaudrate, "baudrate");
		this.controller.addTextModifyBinding(txtDeviceId, 			"uniqueId");

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
		//this.controller.addBigDecimalModifyBinding(txtTravelPerRevM1, 	"motor1TravelPerRevolution");
		this.controller.addItemsBinding(comboPolarityM1, "choicesPolarity");
		this.controller.addItemSelectionBinding(comboPolarityM1, "motor1Polarity");
		this.controller.addItemsBinding(comboMicrostepsM1, "choicesMicrosteps");
		this.controller.addItemSelectionBinding(comboMicrostepsM1, "motor1Microsteps");
		this.controller.addItemsBinding(comboPowerModeM1, "choicesPowerManagement");
		this.controller.addItemSelectionBinding(comboPowerModeM1, "motor1PowerManagement");

		// Motor 2
		this.controller.addBigDecimalModifyBinding(txtStepAngleM2, 		"motor2StepAngle");
		//this.controller.addBigDecimalModifyBinding(txtTravelPerRevM2, 	"motor2TravelPerRevolution");
		this.controller.addItemsBinding(comboPolarityM2, "choicesPolarity");
		this.controller.addItemSelectionBinding(comboPolarityM2, "motor2Polarity");
		this.controller.addItemsBinding(comboMicrostepsM2, "choicesMicrosteps");
		this.controller.addItemSelectionBinding(comboMicrostepsM2, "motor2Microsteps");
		this.controller.addItemsBinding(comboPowerModeM2, "choicesPowerManagement");
		this.controller.addItemSelectionBinding(comboPowerModeM2, "motor2PowerManagement");

		// Motor 3
		this.controller.addBigDecimalModifyBinding(txtStepAngleM3, 		"motor3StepAngle");
		//this.controller.addBigDecimalModifyBinding(txtTravelPerRevM3, 	"motor3TravelPerRevolution");
		this.controller.addItemsBinding(comboPolarityM3, "choicesPolarity");
		this.controller.addItemSelectionBinding(comboPolarityM3, "motor3Polarity");
		this.controller.addItemsBinding(comboMicrostepsM3, "choicesMicrosteps");
		this.controller.addItemSelectionBinding(comboMicrostepsM3, "motor3Microsteps");
		this.controller.addItemsBinding(comboPowerModeM3, "choicesPowerManagement");
		this.controller.addItemSelectionBinding(comboPowerModeM3, "motor3PowerManagement");

		// Motor 4
		this.controller.addBigDecimalModifyBinding(txtStepAngleM4, 		"motor4StepAngle");
		//this.controller.addBigDecimalModifyBinding(txtTravelPerRevM4, 	"motor4TravelPerRevolution");
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

		initAxisBindings();
	}


	private void initAxisBindings() throws GkException{
		{
			TinyGLinearAxisSettingsWrapper modelObject = controller.getDataModel().getxAxisWrapper();

			this.controller.addItemsBinding(xamCombo, "choicesAxisMode");
			this.controller.addItemValueSelectionBinding(xamCombo, modelObject, "am");

			this.controller.addItemsBinding(xsmCombo, "choicesSwitchModes");
			this.controller.addItemValueSelectionBinding(xsmCombo, modelObject, "sx");
			this.controller.addItemsBinding(xsnCombo, "choicesSwitchModes");
			this.controller.addItemValueSelectionBinding(xsnCombo, modelObject, "sn");

			this.controller.addBigDecimalModifyBinding(xvm, modelObject, TinyGLinearAxisSettings.VELOCITY_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(xfr, modelObject, TinyGLinearAxisSettings.FEEDRATE_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(xtn, modelObject, TinyGLinearAxisSettings.TRAVEL_MINIMUM);
			this.controller.addBigDecimalModifyBinding(xtm, modelObject, TinyGLinearAxisSettings.TRAVEL_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(xjm, modelObject, TinyGLinearAxisSettings.JERK_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(xjh, modelObject, TinyGLinearAxisSettings.JERK_HOMING);
			this.controller.addBigDecimalModifyBinding(xjd, modelObject, TinyGLinearAxisSettings.JUNCTION_DEVIATION);
			this.controller.addBigDecimalModifyBinding(xsv, modelObject, TinyGLinearAxisSettings.SEARCH_VELOCITY);
			this.controller.addBigDecimalModifyBinding(xlv, modelObject, TinyGLinearAxisSettings.LATCH_VELOCITY);
			this.controller.addBigDecimalModifyBinding(xlb, modelObject, TinyGLinearAxisSettings.LATCH_BACKOFF);
			this.controller.addBigDecimalModifyBinding(xzb, modelObject, TinyGLinearAxisSettings.ZERO_BACKOFF);
		}

		{
			TinyGLinearAxisSettingsWrapper modelObject = controller.getDataModel().getyAxisWrapper();

			this.controller.addItemsBinding(yamCombo, "choicesAxisMode");
			this.controller.addItemValueSelectionBinding(yamCombo, modelObject, "am");

			this.controller.addItemsBinding(ysmCombo, "choicesSwitchModes");
			this.controller.addItemValueSelectionBinding(ysmCombo, modelObject, "sx");
			this.controller.addItemsBinding(ysnCombo, "choicesSwitchModes");
			this.controller.addItemValueSelectionBinding(ysnCombo, modelObject, "sn");

			this.controller.addBigDecimalModifyBinding(yvm, modelObject, TinyGLinearAxisSettings.VELOCITY_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(yfr, modelObject, TinyGLinearAxisSettings.FEEDRATE_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(ytn, modelObject, TinyGLinearAxisSettings.TRAVEL_MINIMUM);
			this.controller.addBigDecimalModifyBinding(ytm, modelObject, TinyGLinearAxisSettings.TRAVEL_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(yjm, modelObject, TinyGLinearAxisSettings.JERK_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(yjh, modelObject, TinyGLinearAxisSettings.JERK_HOMING);
			this.controller.addBigDecimalModifyBinding(yjd, modelObject, TinyGLinearAxisSettings.JUNCTION_DEVIATION);
			this.controller.addBigDecimalModifyBinding(ysv, modelObject, TinyGLinearAxisSettings.SEARCH_VELOCITY);
			this.controller.addBigDecimalModifyBinding(ylv, modelObject, TinyGLinearAxisSettings.LATCH_VELOCITY);
			this.controller.addBigDecimalModifyBinding(ylb, modelObject, TinyGLinearAxisSettings.LATCH_BACKOFF);
			this.controller.addBigDecimalModifyBinding(yzb, modelObject, TinyGLinearAxisSettings.ZERO_BACKOFF);
		}

		{
			TinyGLinearAxisSettingsWrapper modelObject = controller.getDataModel().getzAxisWrapper();

			this.controller.addItemsBinding(zamCombo, "choicesAxisMode");
			this.controller.addItemValueSelectionBinding(zamCombo, modelObject, "am");

			this.controller.addItemsBinding(zsmCombo, "choicesSwitchModes");
			this.controller.addItemValueSelectionBinding(zsmCombo, modelObject, "sx");
			this.controller.addItemsBinding(zsnCombo, "choicesSwitchModes");
			this.controller.addItemValueSelectionBinding(zsnCombo, modelObject, "sn");

			this.controller.addBigDecimalModifyBinding(zvm, modelObject, TinyGLinearAxisSettings.VELOCITY_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(zfr, modelObject, TinyGLinearAxisSettings.FEEDRATE_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(ztn, modelObject, TinyGLinearAxisSettings.TRAVEL_MINIMUM);
			this.controller.addBigDecimalModifyBinding(ztm, modelObject, TinyGLinearAxisSettings.TRAVEL_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(zjm, modelObject, TinyGLinearAxisSettings.JERK_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(zjh, modelObject, TinyGLinearAxisSettings.JERK_HOMING);
			this.controller.addBigDecimalModifyBinding(zjd, modelObject, TinyGLinearAxisSettings.JUNCTION_DEVIATION);
			this.controller.addBigDecimalModifyBinding(zsv, modelObject, TinyGLinearAxisSettings.SEARCH_VELOCITY);
			this.controller.addBigDecimalModifyBinding(zlv, modelObject, TinyGLinearAxisSettings.LATCH_VELOCITY);
			this.controller.addBigDecimalModifyBinding(zlb, modelObject, TinyGLinearAxisSettings.LATCH_BACKOFF);
			this.controller.addBigDecimalModifyBinding(zzb, modelObject, TinyGLinearAxisSettings.ZERO_BACKOFF);
		}

		{
			TinyGAAxisSettingsWrapper modelObject = controller.getDataModel().getaAxisWrapper();

			this.controller.addItemsBinding(aamCombo, "choicesAxisMode");
			this.controller.addItemValueSelectionBinding(aamCombo, modelObject, "am");

			this.controller.addItemsBinding(asmCombo, "choicesSwitchModes");
			this.controller.addItemValueSelectionBinding(asmCombo, modelObject, "sx");
			this.controller.addItemsBinding(asnCombo, "choicesSwitchModes");
			this.controller.addItemValueSelectionBinding(asnCombo, modelObject, "sn");

			this.controller.addBigDecimalModifyBinding(avm, modelObject, TinyGLinearAxisSettings.VELOCITY_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(afr, modelObject, TinyGLinearAxisSettings.FEEDRATE_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(atn, modelObject, TinyGLinearAxisSettings.TRAVEL_MINIMUM);
			this.controller.addBigDecimalModifyBinding(atm, modelObject, TinyGLinearAxisSettings.TRAVEL_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(ajm, modelObject, TinyGLinearAxisSettings.JERK_MAXIMUM);
			this.controller.addBigDecimalModifyBinding(ajh, modelObject, TinyGLinearAxisSettings.JERK_HOMING);
			this.controller.addBigDecimalModifyBinding(ajd, modelObject, TinyGLinearAxisSettings.JUNCTION_DEVIATION);
			this.controller.addBigDecimalModifyBinding(asv, modelObject, TinyGLinearAxisSettings.SEARCH_VELOCITY);
			this.controller.addBigDecimalModifyBinding(alv, modelObject, TinyGLinearAxisSettings.LATCH_VELOCITY);
			this.controller.addBigDecimalModifyBinding(alb, modelObject, TinyGLinearAxisSettings.LATCH_BACKOFF);
			this.controller.addBigDecimalModifyBinding(azb, modelObject, TinyGLinearAxisSettings.ZERO_BACKOFF);
			this.controller.addBigDecimalModifyBinding(ara, modelObject, TinyGLinearAxisSettings.RADIUS_SETTING);
		}
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

	@Override
	protected Point getInitialSize() {
		return new Point(611, 772);
	}
}
