/**
 * 
 */
package org.goko.tools.zeroprobe;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.quantity.LengthFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.quantity.SpeedFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.tools.zeroprobe.controller.ZeroProbeController;
import org.goko.tools.zeroprobe.controller.ZeroProbeModel;

/**
 * @author Psyko
 * @date 30 juil. 2017
 */
public class ZeroProbePart extends GkUiComponent<ZeroProbeController, ZeroProbeModel>{

	
	@Inject
	public ZeroProbePart(IEclipseContext context) {
		super(context, new ZeroProbeController());
	}

	@PostConstruct
	public void createControls(final Composite parent, MPart part) throws GkException {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Group grpProbeSettings = new Group(composite, SWT.NONE);
		grpProbeSettings.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpProbeSettings.setLayout(new GridLayout(1, false));
		grpProbeSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpProbeSettings.setText("Probe settings");
		
		Composite composite_1 = new Composite(grpProbeSettings, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_1.setSize(158, 35);
		GridLayout gl_composite_1 = new GridLayout(4, false);
		gl_composite_1.horizontalSpacing = 15;
		composite_1.setLayout(gl_composite_1);
		
		Label lblAxis = new Label(composite_1, SWT.NONE);
		lblAxis.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblAxis.setText("Axis");
		
		Button btnX = new Button(composite_1, SWT.RADIO);
		btnX.setText("X");
		
		Button btnY = new Button(composite_1, SWT.RADIO);
		btnY.setText("Y");
		
		Button btnZ = new Button(composite_1, SWT.RADIO);
		btnZ.setText("Z");
		
		SpeedFieldEditor feedrateEditor = new SpeedFieldEditor(grpProbeSettings, SWT.NONE);
		feedrateEditor.setLabelWidthInChar(12);
		feedrateEditor.setWidthInChars(8);
		feedrateEditor.setSize(160, 35);
		feedrateEditor.setLabel("Feedrate");
		feedrateEditor.setPreferenceName(ZeroProbeModel.FEEDRATE);
		
		LengthFieldEditor expectedEditor = new LengthFieldEditor(grpProbeSettings, SWT.NONE);
		expectedEditor.setWidthInChars(8);
		expectedEditor.setLabelWidthInChar(12);
		expectedEditor.setLabel("Expected");
		expectedEditor.setPreferenceName(ZeroProbeModel.EXPECTED);
		
		LengthFieldEditor maxDistanceEditor = new LengthFieldEditor(grpProbeSettings, SWT.NONE);
		maxDistanceEditor.setWidthInChars(8);
		maxDistanceEditor.setLabelWidthInChar(12);
		maxDistanceEditor.setLabel("Max distance");
		maxDistanceEditor.setPreferenceName(ZeroProbeModel.MAX_DISTANCE);
		
		Group grpCompensation = new Group(composite, SWT.NONE);
		grpCompensation.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpCompensation.setLayout(new GridLayout(1, false));
		grpCompensation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpCompensation.setText("Compensation");
		
		BooleanFieldEditor booleanFieldEditor = new BooleanFieldEditor(grpCompensation, SWT.NONE);
		booleanFieldEditor.setLabel("Enable tool radius compensation");
		booleanFieldEditor.setPreferenceName(ZeroProbeModel.TOOL_DIAMETER_COMPENSATION);
		
		LengthFieldEditor toolDiameterEditor = new LengthFieldEditor(grpCompensation, SWT.NONE);
		toolDiameterEditor.setWidthInChars(8);
		toolDiameterEditor.setLabelWidthInChar(12);
		toolDiameterEditor.setLabel("Tool diameter");
		toolDiameterEditor.setPreferenceName(ZeroProbeModel.TOOL_DIAMETER);
	}
}
