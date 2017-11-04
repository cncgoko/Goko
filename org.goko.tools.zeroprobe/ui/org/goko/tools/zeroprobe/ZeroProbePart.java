/**
 * 
 */
package org.goko.tools.zeroprobe;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
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
import org.goko.common.GkUiComponent;
import org.goko.common.dialog.GkDialog;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.common.preferences.fieldeditor.ui.UiBooleanFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiComboFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiLengthFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiRadioGroupFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiSpeedFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.tools.zeroprobe.controller.ZeroProbeController;
import org.goko.tools.zeroprobe.controller.ZeroProbeModel;

/**
 * @author Psyko
 * @date 30 juil. 2017
 */
public class ZeroProbePart extends GkUiComponent<ZeroProbeController, ZeroProbeModel>{

	
	@Inject
	public ZeroProbePart(IEclipseContext context) throws GkException {
		super(context, new ZeroProbeController());
		ContextInjectionFactory.inject(getController(), context);
		getController().initialize();
	}

	@PostConstruct
	public void createControls(final Composite parent, MPart part) throws GkException {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBounds(0, 0, 448, 526);
		composite.setLayout(new GridLayout(1, false));
		
		Group grpProbeSettings = new Group(composite, SWT.NONE);
		grpProbeSettings.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpProbeSettings.setLayout(new GridLayout(1, false));
		grpProbeSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpProbeSettings.setText("Probe settings");
		
		Label lblInfo = new Label(grpProbeSettings, SWT.CENTER);
		lblInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblInfo.setText("New Label");
		lblInfo.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		lblInfo.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		
		Label lblError = new Label(grpProbeSettings, SWT.CENTER);
		lblError.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblError.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblError.setText("New Label");
		lblError.setForeground(SWTResourceManager.getColor(255, 0, 0));
		
		getController().addVisibleBinding(lblError, ZeroProbeModel.IS_ERROR);
		getController().addTextDisplayBinding(lblError, ZeroProbeModel.ERROR_MESSAGE);
		
		getController().addVisibleReverseBinding(lblInfo, ZeroProbeModel.IS_ERROR);
		getController().addTextDisplayBinding(lblInfo, ZeroProbeModel.INFO_MESSAGE);
		
		UiRadioGroupFieldEditor<EnumControllerAxis> radioGroupFieldEditor = new UiRadioGroupFieldEditor<EnumControllerAxis>(grpProbeSettings, SWT.NONE);		
		radioGroupFieldEditor.setNbColumns(2);		
		radioGroupFieldEditor.setLabel("Axis");
		radioGroupFieldEditor.addOption(EnumControllerAxis.X_NEGATIVE.getCode()+"   ", EnumControllerAxis.X_NEGATIVE);		
		radioGroupFieldEditor.addOption(EnumControllerAxis.X_POSITIVE.getCode()+"   ", EnumControllerAxis.X_POSITIVE);
		radioGroupFieldEditor.addOption(EnumControllerAxis.Y_NEGATIVE.getCode()+"   ", EnumControllerAxis.Y_NEGATIVE);		
		radioGroupFieldEditor.addOption(EnumControllerAxis.Y_POSITIVE.getCode()+"   ", EnumControllerAxis.Y_POSITIVE);
		radioGroupFieldEditor.addOption(EnumControllerAxis.Z_NEGATIVE.getCode()+"   ", EnumControllerAxis.Z_NEGATIVE);		
		radioGroupFieldEditor.addOption(EnumControllerAxis.Z_POSITIVE.getCode()+"   ", EnumControllerAxis.Z_POSITIVE);
		radioGroupFieldEditor.setPropertyName(ZeroProbeModel.AXIS);
		radioGroupFieldEditor.setLabelWidthInChar(12);
		
		UiSpeedFieldEditor feedrateEditor = new UiSpeedFieldEditor(grpProbeSettings, SWT.NONE);
		feedrateEditor.setLabelWidthInChar(12);
		feedrateEditor.setWidthInChars(8);
		feedrateEditor.setSize(160, 35);
		feedrateEditor.setLabel("Feedrate");
		feedrateEditor.setPropertyName(ZeroProbeModel.FEEDRATE);
		feedrateEditor.setUnit(GokoPreference.getInstance().getSpeedUnit());
		
		UiLengthFieldEditor expectedEditor = new UiLengthFieldEditor(grpProbeSettings, SWT.NONE);
		expectedEditor.setWidthInChars(8);
		expectedEditor.setLabelWidthInChar(12);
		expectedEditor.setLabel("Expected");
		expectedEditor.setPropertyName(ZeroProbeModel.EXPECTED);
		expectedEditor.setUnit(GokoPreference.getInstance().getLengthUnit());
		
		UiLengthFieldEditor maxDistanceEditor = new UiLengthFieldEditor(grpProbeSettings, SWT.NONE);
		maxDistanceEditor.setWidthInChars(8);
		maxDistanceEditor.setLabelWidthInChar(12);
		maxDistanceEditor.setLabel("Max distance");
		maxDistanceEditor.setPropertyName(ZeroProbeModel.MAX_DISTANCE);
		maxDistanceEditor.setUnit(GokoPreference.getInstance().getLengthUnit());
		
		Group grpCompensation = new Group(composite, SWT.NONE);
		grpCompensation.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpCompensation.setLayout(new GridLayout(1, false));
		grpCompensation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpCompensation.setText("Compensation");
		
		UiBooleanFieldEditor booleanFieldEditor = new UiBooleanFieldEditor(grpCompensation, SWT.NONE);
		booleanFieldEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		booleanFieldEditor.setLabel("Enable tool radius compensation");
		booleanFieldEditor.setPropertyName(ZeroProbeModel.TOOL_DIAMETER_COMPENSATION);
		
		UiLengthFieldEditor toolDiameterEditor = new UiLengthFieldEditor(grpCompensation, SWT.NONE);
		toolDiameterEditor.setWidthInChars(8);
		toolDiameterEditor.setLabelWidthInChar(12);
		toolDiameterEditor.setLabel("Tool diameter");
		toolDiameterEditor.setPropertyName(ZeroProbeModel.TOOL_DIAMETER);
		toolDiameterEditor.setUnit(GokoPreference.getInstance().getLengthUnit());
		
		UiComboFieldEditor<ICoordinateSystem> comboFieldEditor = new UiComboFieldEditor<ICoordinateSystem>(grpProbeSettings, SWT.NONE);
		comboFieldEditor.setLabelWidthInChar(12);
		comboFieldEditor.setWidthInChars(6);
		comboFieldEditor.setPropertyName(ZeroProbeModel.COORDINATE_SYSTEM);
		comboFieldEditor.setLabel("Target CS");
		comboFieldEditor.setInputPropertyName(ZeroProbeModel.COORDINATE_SYSTEM_LIST);
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite_2.setLayout(new GridLayout(1, false));
		
		Button btnNewButton = new Button(composite_2, SWT.CENTER);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().executeProbing();
				} catch (GkException e1) {
					GkDialog.openDialog(e1);
				}
			}
		});
		GridData gd_btnNewButton = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 100;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("Probe");
		
		
		getController().addFieldEditor(radioGroupFieldEditor);
		getController().addFieldEditor(feedrateEditor);
		getController().addFieldEditor(expectedEditor);
		getController().addFieldEditor(maxDistanceEditor);
		getController().addFieldEditor(comboFieldEditor);
		getController().addFieldEditor(booleanFieldEditor);
		getController().addFieldEditor(toolDiameterEditor);
		// Dirty hack to have the default selected value
		LabeledValue<ICoordinateSystem> cs = getDataModel().getCoordinateSystem();
		getDataModel().setCoordinateSystem(null);
		getDataModel().setCoordinateSystem(cs);
		// End of dirty hack
		getController().addEnableBinding(toolDiameterEditor, ZeroProbeModel.TOOL_DIAMETER_COMPENSATION);
	
		initFromPersistedState(part);
	}
	
	@PersistState
	public void persistState(MPart part){
		if(getDataModel() != null){
			part.getPersistedState().put(ZeroProbeModel.FEEDRATE, QuantityUtils.format(getDataModel().getFeedrate(), 4, false, true));
			part.getPersistedState().put(ZeroProbeModel.MAX_DISTANCE, QuantityUtils.format(getDataModel().getMaxDistance(), 4, false, true));
			part.getPersistedState().put(ZeroProbeModel.TOOL_DIAMETER_COMPENSATION, BooleanUtils.toStringTrueFalse(getDataModel().isToolDiameterCompensation()));
			part.getPersistedState().put(ZeroProbeModel.TOOL_DIAMETER, QuantityUtils.format(getDataModel().getToolDiameter(), 4, false, true));
		}
	}
	
	private void initFromPersistedState(MPart part) throws GkException{
		Map<String, String> state = part.getPersistedState();
		String feedrate = state.get(ZeroProbeModel.FEEDRATE);
		if(StringUtils.isNotEmpty(feedrate)){
			getDataModel().setFeedrate(Speed.parse(feedrate));
		}
		String maxDistance = state.get(ZeroProbeModel.MAX_DISTANCE);
		if(StringUtils.isNotEmpty(maxDistance)){
			getDataModel().setMaxDistance(Length.parse(maxDistance));
		}
		String toolCompensation = state.get(ZeroProbeModel.TOOL_DIAMETER_COMPENSATION);
		if(StringUtils.isNotEmpty(toolCompensation)){
			getDataModel().setToolDiameterCompensation(BooleanUtils.toBoolean(toolCompensation));
		}
		String toolDiameter = state.get(ZeroProbeModel.TOOL_DIAMETER);
		if(StringUtils.isNotEmpty(toolDiameter)){
			getDataModel().setToolDiameter(Length.parse(toolDiameter));
		}
	}
}
