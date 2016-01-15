package org.goko.autoleveler.modifier.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.autoleveler.modifier.AutoLevelerModifier;
import org.goko.common.GkUiComponent;
import org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;

public class AutoLevelerModifierConfigurationPanel extends GkUiComponent<AutoLevelerModifierConfigurationController, AutoLevelerModifierConfigurationModel> {
	private static final GkLog LOG = GkLog.getLogger(AutoLevelerModifierConfigurationPanel.class);

	/**
	 * Constructor
	 */
	public AutoLevelerModifierConfigurationPanel() {
		super(new AutoLevelerModifierConfigurationController());
		try {
			getController().initialize();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContent(Composite parent , IModifier<?> modifier) throws GkException {
		getController().setModifier((AutoLevelerModifier) modifier);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginRight = 20;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Group grpArea = new Group(composite, SWT.NONE);
		grpArea.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpArea.setText("Area");
		grpArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpArea.setLayout(new GridLayout(1, false));

		Composite composite_1 = new Composite(grpArea, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);

		Label lblStartPoint = new Label(composite_1, SWT.NONE);
		lblStartPoint.setText("Start");

		UiQuantityFieldEditor<Length> xStartCoordinate = new UiQuantityFieldEditor<Length>(composite_1, SWT.NONE);
		xStartCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.X_START_COORDINATE);
		xStartCoordinate.setWidthInChars(6);
		xStartCoordinate.setLabel("X");



		UiQuantityFieldEditor<Length> yStartCoordinate = new UiQuantityFieldEditor<Length>(composite_1, SWT.NONE);
		yStartCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Y_START_COORDINATE);
		yStartCoordinate.setWidthInChars(6);
		yStartCoordinate.setLabel("Y");

		Label lblEndPoint = new Label(composite_1, SWT.NONE);
		lblEndPoint.setText("End");

		UiQuantityFieldEditor<Length> xEndCoordinate = new UiQuantityFieldEditor<Length>(composite_1, SWT.NONE);
		xEndCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.X_END_COORDINATE);
		xEndCoordinate.setWidthInChars(6);
		xEndCoordinate.setLabel("X");

		UiQuantityFieldEditor<Length> yEndCoordinate = new UiQuantityFieldEditor<Length>(composite_1, SWT.NONE);
		yEndCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Y_END_COORDINATE);
		yEndCoordinate.setWidthInChars(6);
		yEndCoordinate.setLabel("Y");

		Label lblStep = new Label(composite_1, SWT.NONE);
		lblStep.setText("Step");

		UiQuantityFieldEditor<Length> xStep = new UiQuantityFieldEditor<Length>(composite_1, SWT.NONE);
		xStep.setPropertyName(AutoLevelerModifierConfigurationModel.X_STEP);
		xStep.setWidthInChars(6);
		xStep.setLabel("X");

		UiQuantityFieldEditor<Length> yStep = new UiQuantityFieldEditor<Length>(composite_1, SWT.NONE);
		yStep.setPropertyName(AutoLevelerModifierConfigurationModel.Y_STEP);
		yStep.setWidthInChars(6);
		yStep.setLabel("Y");

		Button btnNewButton = new Button(grpArea, SWT.NONE);
		btnNewButton.setImage(ResourceManager.getPluginImage("org.goko.autoleveller", "icons/document-bounds-32.png"));
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		btnNewButton.setText("GCode bounds");

		Group grpProbing = new Group(composite, SWT.NONE);
		grpProbing.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpProbing.setLayout(new GridLayout(1, false));
		grpProbing.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpProbing.setText("Probing");

		UiQuantityFieldEditor<Length> zClearanceCoordinate = new UiQuantityFieldEditor<Length>(grpProbing, SWT.NONE);
		zClearanceCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Z_CLEARANCE);
		zClearanceCoordinate.setLabelWidthInChar(12);
		zClearanceCoordinate.setWidthInChars(6);
		zClearanceCoordinate.setLabel("Clearance Z");

		UiQuantityFieldEditor<Length> zExpectedCoordinate = new UiQuantityFieldEditor<Length>(grpProbing, SWT.NONE);
		zExpectedCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Z_EXPECTED);
		zExpectedCoordinate.setLabelWidthInChar(12);
		zExpectedCoordinate.setWidthInChars(6);
		zExpectedCoordinate.setLabel("Expected Z");

		UiQuantityFieldEditor<Length> zProbeStartCoordinate = new UiQuantityFieldEditor<Length>(grpProbing, SWT.NONE);
		zProbeStartCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Z_PROBE_START);
		zProbeStartCoordinate.setLabelWidthInChar(12);
		zProbeStartCoordinate.setWidthInChars(6);
		zProbeStartCoordinate.setLabel("Probe start Z");

		UiQuantityFieldEditor<Length> zProbeLowerCoordinate = new UiQuantityFieldEditor<Length>(grpProbing, SWT.NONE);
		zProbeLowerCoordinate.setPropertyName(AutoLevelerModifierConfigurationModel.Z_PROBE_LOWER);
		zProbeLowerCoordinate.setLabelWidthInChar(12);
		zProbeLowerCoordinate.setWidthInChars(6);
		zProbeLowerCoordinate.setLabel("Probe lower Z");

		UiQuantityFieldEditor<Length> probeFeedrate = new UiQuantityFieldEditor<Length>(grpProbing, SWT.NONE);
		probeFeedrate.setPropertyName(AutoLevelerModifierConfigurationModel.PROBE_FEEDRATE);
		probeFeedrate.setLabelWidthInChar(12);
		probeFeedrate.setWidthInChars(6);
		probeFeedrate.setLabel("Probe feedrate");

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

		getController().getModifier().setHeightMap(getController().getBuilder().getMap());
	}
}
