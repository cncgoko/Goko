/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.autoleveler;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.autoleveler.model.AutoLevelerController;
import org.goko.autoleveler.model.AutoLevelerModel;
import org.goko.autoleveler.service.IAutoLevelerService;
import org.goko.common.GkUiComponent;
import org.goko.common.preferences.fieldeditor.ui.UiIntegerFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.workspace.service.IWorkspaceService;

public class AutoLevelerPart extends GkUiComponent<AutoLevelerController, AutoLevelerModel> {
	private static final String PERSITED_EXPECTED_Z = "org.goko.autoleveler.persisted.expectedz";
	private static final String PERSITED_SAFE_Z = "org.goko.autoleveler.persisted.safez";
	private static final String PERSITED_MAX_Z = "org.goko.autoleveler.persisted.maxz";
	private static final String PERSITED_START_Z = "org.goko.autoleveler.persisted.startz";
	private static final String PERSITED_STEP_X = "org.goko.autoleveler.persisted.stepx";
	private static final String PERSITED_STEP_Y = "org.goko.autoleveler.persisted.stepy";

	private UiQuantityFieldEditor<Length> startXEditor;	
	private UiQuantityFieldEditor<Length> startYEditor;		
	private UiQuantityFieldEditor<Length> endXEditor;		
	private UiQuantityFieldEditor<Length> endYEditor;				
	private UiQuantityFieldEditor<Length> expectedZEditor;	
	private UiQuantityFieldEditor<Length> probeMaxZEditor;	
	private UiQuantityFieldEditor<Length> safeZEditor;		
	private UiQuantityFieldEditor<Length> probeStartZEditor;
	private Unit<Length> lengthUnit;
	private Composite parent;
	@Inject
	@Optional
	IAutoLevelerService levelerService;
	@Inject
	@Optional
	IWorkspaceService workspaceService;
	
	@Inject
	public AutoLevelerPart(IEclipseContext context) throws GkException {
		super(new AutoLevelerController());
		ContextInjectionFactory.inject(getController(), context);
		getController().initialize();
	}

	@Inject
	@Optional
	private void getNotified(@UIEventTopic("gcodefile") IGCodeProvider file) throws GkException {
		getController().setGCodeFile(file);
	}

	@PostConstruct
	public void createControls(Composite parent, MPart part) throws GkException {
		lengthUnit = GokoPreference.getInstance().getLengthUnit();		
		this.parent = parent;
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		if (!getController().isAutolevelerService()) {
			Label lblAutolevelerIsDeactivated = new Label(composite, SWT.NONE);
			lblAutolevelerIsDeactivated.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			lblAutolevelerIsDeactivated.setText("Autoleveler is deactivated because there is no probing support.");

			return;
		}

		Group grpSettings = new Group(composite, SWT.NONE);
		grpSettings.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridLayout gl_grpSettings = new GridLayout(1, false);
		gl_grpSettings.verticalSpacing = 0;
		grpSettings.setLayout(gl_grpSettings);
		grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSettings.setText("Probe area");

		Composite composite_3 = new Composite(grpSettings, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_composite_3 = new GridLayout(2, false);
		gl_composite_3.verticalSpacing = 0;
		gl_composite_3.marginWidth = 0;
		gl_composite_3.marginHeight = 0;
		gl_composite_3.horizontalSpacing = 0;
		composite_3.setLayout(gl_composite_3);

		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayout(new GridLayout(4, false));

		Label lblStartPoint = new Label(composite_4, SWT.NONE);
		lblStartPoint.setAlignment(SWT.RIGHT);
		GridData gd_lblStartPoint = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblStartPoint.widthHint = 60;
		lblStartPoint.setLayoutData(gd_lblStartPoint);
		lblStartPoint.setText("Start point");

		startXEditor = new UiQuantityFieldEditor<Length>(composite_4, SWT.NONE);
		startXEditor.setPropertyName("startx");
		startXEditor.setWidthInChars(8);
		startXEditor.setLabel("X");
		startXEditor.setUnit(lengthUnit);
		
		startYEditor = new UiQuantityFieldEditor<Length>(composite_4, SWT.NONE);
		startYEditor.setPropertyName("starty");
		startYEditor.setLabel("  Y");
		startYEditor.setWidthInChars(8);
		startYEditor.setUnit(lengthUnit);
		
		Button btnGrabStartPosition = new Button(composite_4, SWT.NONE);
		btnGrabStartPosition.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().grabStartPointFromPosition();
				} catch (GkException exception) {
					displayMessage(exception);
				}
			}
		});
		btnGrabStartPosition.setToolTipText("Grab point from current position");
		btnGrabStartPosition.setImage(ResourceManager.getPluginImage("org.goko.autoleveller", "icons/grab-point.png"));

		Label lblEndPoint = new Label(composite_4, SWT.NONE);
		lblEndPoint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblEndPoint.setAlignment(SWT.RIGHT);
		lblEndPoint.setText("End point");

		endXEditor = new UiQuantityFieldEditor<Length>(composite_4, SWT.NONE);
		endXEditor.setPropertyName("endx");
		endXEditor.setLabel("X");
		endXEditor.setWidthInChars(8);
		endXEditor.setUnit(lengthUnit);

		endYEditor = new UiQuantityFieldEditor<Length>(composite_4, SWT.NONE);
		endYEditor.setPropertyName("endy");
		endYEditor.setWidthInChars(8);
		endYEditor.setLabel("  Y");
		endYEditor.setUnit(lengthUnit);
		
		Button btnGrabEndPosition = new Button(composite_4, SWT.NONE);
		btnGrabEndPosition.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().grabEndPointFromPosition();
				} catch (GkException exception) {
					displayMessage(exception);
				}
			}
		});
		btnGrabEndPosition.setToolTipText("Grab point from current position");
		btnGrabEndPosition.setImage(ResourceManager.getPluginImage("org.goko.autoleveller", "icons/grab-point.png"));

		Label lblXStep = new Label(composite_4, SWT.NONE);
		lblXStep.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblXStep.setAlignment(SWT.RIGHT);
		lblXStep.setText("Step");

		UiIntegerFieldEditor stepXEditor = new UiIntegerFieldEditor(composite_4, SWT.NONE);
		stepXEditor.setPropertyName("stepX");
		stepXEditor.setWidthInChars(6);
		stepXEditor.setLabel("X");

		UiIntegerFieldEditor stepYEditor = new UiIntegerFieldEditor(composite_4, SWT.NONE);
		stepYEditor.setPropertyName("stepY");
		stepYEditor.setWidthInChars(6);
		stepYEditor.setLabel("  Y");
		new Label(composite_4, SWT.NONE);

		Composite composite_5 = new Composite(composite_3, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		composite_5.setLayout(new GridLayout(1, false));

		Button btnNewButton = new Button(composite_5, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().setBoundsFromGCodeFile();
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		btnNewButton.setImage(ResourceManager.getPluginImage("org.goko.autoleveller", "icons/document-bounds-32.png"));
		btnNewButton.setToolTipText("Get from GCode bounds");
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

		Group grpProbeSettings = new Group(composite, SWT.NONE);
		grpProbeSettings.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpProbeSettings.setLayout(new GridLayout(2, false));
		grpProbeSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpProbeSettings.setText("Probe settings");

		expectedZEditor = new UiQuantityFieldEditor<Length>(grpProbeSettings, SWT.NONE);
		expectedZEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		expectedZEditor.setPropertyName("expectedZ");
		expectedZEditor.setLabelWidthInChar(9);
		expectedZEditor.setLabel("Exepcted Z");
		expectedZEditor.setWidthInChars(8);
		expectedZEditor.setUnit(lengthUnit);

		probeStartZEditor = new UiQuantityFieldEditor<Length>(grpProbeSettings, SWT.NONE);
		probeStartZEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		probeStartZEditor.setPropertyName("startProbe");
		probeStartZEditor.setLabel("  Probe start Z");
		probeStartZEditor.setLabelWidthInChar(11);
		probeStartZEditor.setWidthInChars(8);
		probeStartZEditor.setUnit(lengthUnit);
		
		safeZEditor = new UiQuantityFieldEditor<Length>(grpProbeSettings, SWT.NONE);
		safeZEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		safeZEditor.setPropertyName("safeZ");
		safeZEditor.setWidthInChars(8);
		safeZEditor.setLabelWidthInChar(9);
		safeZEditor.setLabel("Safe Z");
		safeZEditor.setUnit(lengthUnit);
		
		probeMaxZEditor = new UiQuantityFieldEditor<Length>(grpProbeSettings, SWT.NONE);
		probeMaxZEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		probeMaxZEditor.setPropertyName("maxZProbe");
		probeMaxZEditor.setWidthInChars(8);
		probeMaxZEditor.setLabelWidthInChar(11);
		probeMaxZEditor.setLabel("  Probe max Z");
		probeMaxZEditor.setUnit(lengthUnit);
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(4, false));

		Button btnEnablePreview = new Button(composite_2, SWT.CHECK);
		btnEnablePreview.setText("Enable preview");

		Button btnPreviewProbePattern = new Button(composite_2, SWT.NONE);
		btnPreviewProbePattern.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().previewPattern();
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		btnPreviewProbePattern.setText("Preview probe pattern");

		Button btnStartAutolevel = new Button(composite_2, SWT.NONE);
		btnStartAutolevel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {				
				try {
					getController().probe();
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		btnStartAutolevel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnStartAutolevel.setText("Start autolevel");
		
		Button btnDebug = new Button(composite_2, SWT.NONE);
		btnDebug.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getDataModel().setGcodeProvider(workspaceService.getCurrentGCodeProvider());
					getController().debugCurrentPosition();
				} catch (GkException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnDebug.setText("Debug");
		try {			
			getController().addFieldEditor(startXEditor);			
			getController().addFieldEditor(startYEditor);			
			getController().addFieldEditor(endXEditor);			
			getController().addFieldEditor(endYEditor);			
			getController().addFieldEditor(stepXEditor);			
			getController().addFieldEditor(stepYEditor);			
			getController().addFieldEditor(expectedZEditor);			
			getController().addFieldEditor(probeMaxZEditor);			
			getController().addFieldEditor(safeZEditor);			
			getController().addFieldEditor(probeStartZEditor);			
			initCustomBindings();
		} catch (GkException e1) {
			displayMessage(e1);
		}
		retrievePersistedState(part);
	}

	protected void initCustomBindings() throws GkException {
		
		getDataModel().setStartx(NumberQuantity.of(BigDecimal.ZERO, GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setStarty(NumberQuantity.of(BigDecimal.ZERO, GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setEndx(NumberQuantity.of(BigDecimal.ZERO, GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setEndy(NumberQuantity.of(BigDecimal.ZERO, GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setSafeZ(NumberQuantity.of(BigDecimal.ZERO, GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setMaxZProbe(NumberQuantity.of(BigDecimal.ZERO, GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setStartProbe(NumberQuantity.of(BigDecimal.ZERO, GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setExpectedZ(NumberQuantity.of(BigDecimal.ZERO, GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setStepX(BigDecimal.ZERO);
		getDataModel().setStepY(BigDecimal.ZERO);
	}

	public void retrievePersistedState(MPart part) throws GkException {
		Map<String, String> states = part.getPersistedState();
		getDataModel().setExpectedZ(NumberQuantity.of(new BigDecimal(states.get(PERSITED_EXPECTED_Z)), GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setMaxZProbe(NumberQuantity.of(new BigDecimal(states.get(PERSITED_MAX_Z)), GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setSafeZ(NumberQuantity.of(new BigDecimal(states.get(PERSITED_SAFE_Z)), GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setStartProbe(NumberQuantity.of(new BigDecimal(states.get(PERSITED_START_Z)), GokoPreference.getInstance().getLengthUnit()));
		getDataModel().setStepX(new BigDecimal(states.get(PERSITED_STEP_X)));
		getDataModel().setStepY(new BigDecimal(states.get(PERSITED_STEP_Y)));
	}

	@Persist
	public void persist(MPart part) {
		// A rajouted dans la definition de la part
		part.getPersistedState().put(PERSITED_EXPECTED_Z, String.valueOf(getDataModel().getExpectedZ()));
		part.getPersistedState().put(PERSITED_MAX_Z, String.valueOf(getDataModel().getMaxZProbe()));
		part.getPersistedState().put(PERSITED_SAFE_Z, String.valueOf(getDataModel().getSafeZ()));
		part.getPersistedState().put(PERSITED_START_Z, String.valueOf(getDataModel().getStartProbe()));
		part.getPersistedState().put(PERSITED_STEP_X, String.valueOf(getDataModel().getStepX()));
		part.getPersistedState().put(PERSITED_STEP_Y, String.valueOf(getDataModel().getStepY()));
	}
	
	@Inject
	@Optional	
	protected void onGokoUnitChange(@Preference(nodePath = GokoPreference.NODE_ID, value = GokoPreference.KEY_DISTANCE_UNIT) String distanceUnit) throws GkException{
		lengthUnit = GokoPreference.getInstance().getLengthUnit();
		if(startXEditor != null){
			startXEditor.setUnit(lengthUnit);	
			startYEditor.setUnit(lengthUnit);		
			endXEditor.setUnit(lengthUnit);			
			endYEditor.setUnit(lengthUnit);					
			expectedZEditor.setUnit(lengthUnit);	
			probeMaxZEditor.setUnit(lengthUnit);	
			safeZEditor.setUnit(lengthUnit);		
			probeStartZEditor.setUnit(lengthUnit);
			this.parent.layout();
			this.parent.redraw();
		}		
	}
}
