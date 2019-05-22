package org.goko.tools.autoleveler.modifier.ui;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.dialog.GkErrorDialog;
import org.goko.common.preferences.fieldeditor.ui.UiBigDecimalFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiComboFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiLengthFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiSpeedFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPropertiesPanel;
import org.goko.tools.autoleveler.modifier.GridAutoLevelerModifier;

public class AutoLevelerModifierConfigurationPanel extends AbstractModifierPropertiesPanel<GridAutoLevelerModifier, AutoLevelerModifierConfigurationController, AutoLevelerModifierConfigurationModel> implements IPropertiesPanel{
	private static final GkLog LOG = GkLog.getLogger(AutoLevelerModifierConfigurationPanel.class);

	/**
	 * Constructor
	 */
	public AutoLevelerModifierConfigurationPanel(IEclipseContext context) {
		super(context, new AutoLevelerModifierConfigurationController());		
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContent(Composite parent , IModifier<?> modifier) throws GkException {
		getController().setModifier((GridAutoLevelerModifier) modifier);
		Composite composite = new Composite(parent, SWT.NONE);
		if(!getController().isProbingService()){
			composite.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			GridLayout gl_composite = new GridLayout(1, false);		
			composite.setLayout(gl_composite);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label lblNewLabel = new Label(composite, SWT.NONE);
			lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			lblNewLabel.setText("Current target board does not support probing");
		
			return;
		}
		
		composite.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridLayout gl_composite = new GridLayout(1, false);		
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpAction = new Group(composite, SWT.NONE);
		grpAction.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpAction.setText("Action");
		grpAction.setLayout(new GridLayout(2, true));
		grpAction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnStartProbing = new Button(grpAction, SWT.NONE);
		btnStartProbing.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {					
					getController().startMapProbing();
				} catch (GkException e1) {
					GkErrorDialog.openDialog(null, e1);
				}
			}
		});
		btnStartProbing.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnStartProbing.setText("Start probing");
		
		Button btnClearProbedData = new Button(grpAction, SWT.NONE);
		btnClearProbedData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().clearProbedData();
			}
		});
		btnClearProbedData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnClearProbedData.setText("Clear probed data");

		Group grpArea = new Group(composite, SWT.NONE);
		grpArea.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpArea.setText("Area");
		grpArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpArea.setLayout(new GridLayout(1, false));

		UiComboFieldEditor<ICoordinateSystem> csFieldEditor = new UiComboFieldEditor<ICoordinateSystem>(grpArea, SWT.NONE);
		csFieldEditor.setLabelWidthInChar(15);
		csFieldEditor.setWidthInChars(6);
		csFieldEditor.setPropertyName(AutoLevelerModifierConfigurationModel.COORDINATE_SYSTEM);
		csFieldEditor.setLabel("Coordinate System");
		csFieldEditor.setInputPropertyName(AutoLevelerModifierConfigurationModel.COORDINATE_SYSTEM_LIST);
		
		Composite composite_1 = new Composite(grpArea, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);

		Label lblStartPoint = new Label(composite_1, SWT.NONE);
		lblStartPoint.setText("Start");

		UiQuantityFieldEditor<Length> xStartCoordinate = new UiLengthFieldEditor(composite_1, SWT.NONE);
		xStartCoordinate.setEmptyStringAllowed(false);
		xStartCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.X_START_COORDINATE);
		xStartCoordinate.setWidthInChars(6);
		xStartCoordinate.setUnit(GokoPreference.getInstance().getLengthUnit());		
		xStartCoordinate.setLabel("X");

		UiQuantityFieldEditor<Length> yStartCoordinate = new UiLengthFieldEditor(composite_1, SWT.NONE);
		yStartCoordinate.setEmptyStringAllowed(false);
		yStartCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Y_START_COORDINATE);
		yStartCoordinate.setWidthInChars(6);
		yStartCoordinate.setUnit(GokoPreference.getInstance().getLengthUnit());
		yStartCoordinate.setLabel("Y");

		Label lblEndPoint = new Label(composite_1, SWT.NONE);
		lblEndPoint.setText("End");

		UiQuantityFieldEditor<Length> xEndCoordinate = new UiLengthFieldEditor(composite_1, SWT.NONE);
		xEndCoordinate.setEmptyStringAllowed(false);
		xEndCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.X_END_COORDINATE);
		xEndCoordinate.setWidthInChars(6);
		xEndCoordinate.setUnit(GokoPreference.getInstance().getLengthUnit());
		xEndCoordinate.setLabel("X");

		UiQuantityFieldEditor<Length> yEndCoordinate = new UiLengthFieldEditor(composite_1, SWT.NONE);
		yEndCoordinate.setEmptyStringAllowed(false);
		yEndCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Y_END_COORDINATE);
		yEndCoordinate.setWidthInChars(6);
		yEndCoordinate.setUnit(GokoPreference.getInstance().getLengthUnit());
		yEndCoordinate.setLabel("Y");

		Label lblStep = new Label(composite_1, SWT.NONE);
		lblStep.setText("Step");

		UiBigDecimalFieldEditor xStep = new UiBigDecimalFieldEditor(composite_1, SWT.NONE);
		xStep.setEmptyStringAllowed(false);
		xStep.setPropertyName(AutoLevelerModifierConfigurationModel.X_STEP);
		xStep.setWidthInChars(6);		
		xStep.setLabel("X");

		UiBigDecimalFieldEditor yStep = new UiBigDecimalFieldEditor(composite_1, SWT.NONE);
		yStep.setEmptyStringAllowed(false);
		yStep.setPropertyName(AutoLevelerModifierConfigurationModel.Y_STEP);
		yStep.setWidthInChars(6);
		yStep.setLabel("Y");

		Button btnMatchBounds = new Button(grpArea, SWT.NONE);		
		btnMatchBounds.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnMatchBounds.setText("Match bounds");

		Group grpProbing = new Group(composite, SWT.NONE);
		grpProbing.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpProbing.setLayout(new GridLayout(1, false));
		grpProbing.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpProbing.setText("Probing");

		UiQuantityFieldEditor<Length> zClearanceCoordinate = new UiLengthFieldEditor(grpProbing, SWT.NONE);
		zClearanceCoordinate.setEmptyStringAllowed(false);
		zClearanceCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Z_CLEARANCE);
		zClearanceCoordinate.setLabelWidthInChar(12);
		zClearanceCoordinate.setWidthInChars(6);
		zClearanceCoordinate.setUnit(GokoPreference.getInstance().getLengthUnit());
		zClearanceCoordinate.setLabel("Clearance Z");

		UiQuantityFieldEditor<Length> zExpectedCoordinate = new UiLengthFieldEditor(grpProbing, SWT.NONE);
		zExpectedCoordinate.setEmptyStringAllowed(false);
		zExpectedCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Z_EXPECTED);
		zExpectedCoordinate.setLabelWidthInChar(12);
		zExpectedCoordinate.setWidthInChars(6);
		zExpectedCoordinate.setUnit(GokoPreference.getInstance().getLengthUnit());
		zExpectedCoordinate.setLabel("Expected Z");

		UiQuantityFieldEditor<Length> zProbeStartCoordinate = new UiLengthFieldEditor(grpProbing, SWT.NONE);
		zProbeStartCoordinate.setEmptyStringAllowed(false);
		zProbeStartCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Z_PROBE_START);
		zProbeStartCoordinate.setLabelWidthInChar(12);
		zProbeStartCoordinate.setWidthInChars(6);
		zProbeStartCoordinate.setUnit(GokoPreference.getInstance().getLengthUnit());
		zProbeStartCoordinate.setLabel("Probe start Z");

		UiQuantityFieldEditor<Length> zProbeLowerCoordinate = new UiLengthFieldEditor(grpProbing, SWT.NONE);
		zProbeLowerCoordinate.setEmptyStringAllowed(false);
		zProbeLowerCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Z_PROBE_LOWER);
		zProbeLowerCoordinate.setLabelWidthInChar(12);
		zProbeLowerCoordinate.setWidthInChars(6);
		zProbeLowerCoordinate.setUnit(GokoPreference.getInstance().getLengthUnit());
		zProbeLowerCoordinate.setLabel("Probe lower Z");

		UiQuantityFieldEditor<Speed> probeFeedrate = new UiSpeedFieldEditor(grpProbing, SWT.NONE);
		probeFeedrate.setEmptyStringAllowed(false);
		probeFeedrate.setPropertyName(AutoLevelerModifierConfigurationModel.PROBE_FEEDRATE);
		probeFeedrate.setLabelWidthInChar(12);
		probeFeedrate.setWidthInChars(6);
		probeFeedrate.setUnit(GokoPreference.getInstance().getSpeedUnit());
		probeFeedrate.setLabel("Probe feedrate");
		
		UiQuantityFieldEditor<Speed> moveFeedrate = new UiSpeedFieldEditor(grpProbing, SWT.NONE);
		moveFeedrate.setLabel("Move feedrate");
		moveFeedrate.setPropertyName(AutoLevelerModifierConfigurationModel.MOVE_FEEDRATE);
		moveFeedrate.setLabelWidthInChar(12);
		moveFeedrate.setWidthInChars(6);
		moveFeedrate.setUnit(GokoPreference.getInstance().getSpeedUnit());
		
		getController().addEnableBinding(csFieldEditor, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(xStartCoordinate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(yStartCoordinate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(xEndCoordinate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(yEndCoordinate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(xStep, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(yStep, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(zClearanceCoordinate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(zExpectedCoordinate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(zProbeStartCoordinate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(zProbeLowerCoordinate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(probeFeedrate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(moveFeedrate, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		
		getController().addEnableBinding(btnStartProbing, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		getController().addEnableBinding(btnMatchBounds, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		
		getController().addEnableReverseBinding(btnClearProbedData, AutoLevelerModifierConfigurationModel.MODIFICATION_ALLOWED);
		
		getController().addFieldEditor(csFieldEditor); 
		getController().addFieldEditor(xStartCoordinate);
		getController().addFieldEditor(yStartCoordinate);
		getController().addFieldEditor(xEndCoordinate);
		getController().addFieldEditor(yEndCoordinate);
		getController().addFieldEditor(xStep);
		getController().addFieldEditor(yStep);
		getController().addFieldEditor(zClearanceCoordinate);
		getController().addFieldEditor(zExpectedCoordinate);
		getController().addFieldEditor(zProbeStartCoordinate);
		getController().addFieldEditor(zProbeLowerCoordinate);
		getController().addFieldEditor(probeFeedrate);
		getController().addFieldEditor(moveFeedrate);

	}
}
