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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.autoleveler.model.AutoLevelerController;
import org.goko.autoleveler.model.AutoLevelerModel;
import org.goko.autoleveler.service.IAutoLevelerService;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.IGCodeProvider;

public class AutoLevelerPart extends GkUiComponent<AutoLevelerController, AutoLevelerModel> {
	private static final String PERSITED_EXPECTED_Z = "org.goko.autoleveler.persisted.expectedz";
	private static final String PERSITED_SAFE_Z 	= "org.goko.autoleveler.persisted.safez";
	private static final String PERSITED_MAX_Z 		= "org.goko.autoleveler.persisted.maxz";
	private static final String PERSITED_START_Z 	= "org.goko.autoleveler.persisted.startz";
	private static final String PERSITED_STEP_X 	= "org.goko.autoleveler.persisted.stepx";
	private static final String PERSITED_STEP_Y 	= "org.goko.autoleveler.persisted.stepy";
	private Text txtStartX;
	private Text txtStartY;
	private Text txtEndX;
	private Text txtEndY;
	private Text txtExpectedZ;
	private Text txtSafeZ;
	private Text txtMaxProbe;
	private Text txtStartProbe;
	private Text txtStepX;
	private Text txtStepY;

	@Inject
	@Optional
	IAutoLevelerService levelerService;

	@Inject
	public AutoLevelerPart(IEclipseContext context) throws GkException {
		super(new AutoLevelerController());
		ContextInjectionFactory.inject(getController(), context);
		getController().initialize();
	}

	@Inject
	@Optional
	private void getNotified(@UIEventTopic("gcodefile") IGCodeProvider file)
			throws GkException {
		getController().setGCodeFile(file);
	}

	@PostConstruct
	public void createControls(Composite parent, MPart part) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		if(!getController().isAutolevelerService()){
			Label lblAutolevelerIsDeactivated = new Label(composite, SWT.NONE);
			lblAutolevelerIsDeactivated.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			lblAutolevelerIsDeactivated.setText("Autoleveler is deactivated because there is no probing support.");

			return;
		}


		Group grpSettings = new Group(composite, SWT.NONE);
		GridLayout gl_grpSettings = new GridLayout(1, false);
		gl_grpSettings.verticalSpacing = 0;
		grpSettings.setLayout(gl_grpSettings);
		grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		grpSettings.setText("Settings");


		Composite composite_3 = new Composite(grpSettings, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		GridLayout gl_composite_3 = new GridLayout(2, false);
		gl_composite_3.verticalSpacing = 0;
		gl_composite_3.marginWidth = 0;
		gl_composite_3.marginHeight = 0;
		gl_composite_3.horizontalSpacing = 0;
		composite_3.setLayout(gl_composite_3);

		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayout(new GridLayout(6, false));

		Label lblStartPoint = new Label(composite_4, SWT.NONE);
		lblStartPoint.setAlignment(SWT.RIGHT);
		GridData gd_lblStartPoint = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblStartPoint.widthHint = 60;
		lblStartPoint.setLayoutData(gd_lblStartPoint);
		lblStartPoint.setText("Start point");

		Label lblX = new Label(composite_4, SWT.NONE);
		lblX.setText("X");

		txtStartX = new Text(composite_4, SWT.BORDER);
		GridData gd_txtStartX = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtStartX.minimumWidth = 60;
		gd_txtStartX.widthHint = 60;
		txtStartX.setLayoutData(gd_txtStartX);

		Label lblY = new Label(composite_4, SWT.NONE);
		lblY.setText("Y");

		txtStartY = new Text(composite_4, SWT.BORDER);
		GridData gd_txtStartY = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtStartY.minimumWidth = 60;
		gd_txtStartY.widthHint = 60;
		txtStartY.setLayoutData(gd_txtStartY);

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
		btnGrabStartPosition.setImage(ResourceManager.getPluginImage(
				"org.goko.autoleveller", "icons/grab-point.png"));

		Label lblEndPoint = new Label(composite_4, SWT.NONE);
		lblEndPoint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		lblEndPoint.setAlignment(SWT.RIGHT);
		lblEndPoint.setText("End point");

		Label lblNewLabel = new Label(composite_4, SWT.NONE);
		lblNewLabel.setText("X");

		txtEndX = new Text(composite_4, SWT.BORDER);
		GridData gd_txtEndX = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtEndX.minimumWidth = 60;
		gd_txtEndX.widthHint = 60;
		txtEndX.setLayoutData(gd_txtEndX);

		Label lblNewLabel_1 = new Label(composite_4, SWT.NONE);
		lblNewLabel_1.setText("Y");

		txtEndY = new Text(composite_4, SWT.BORDER);
		GridData gd_txtEndY = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtEndY.minimumWidth = 60;
		gd_txtEndY.widthHint = 60;
		txtEndY.setLayoutData(gd_txtEndY);

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
		btnGrabEndPosition.setImage(ResourceManager.getPluginImage(
				"org.goko.autoleveller", "icons/grab-point.png"));

		Composite composite_5 = new Composite(composite_3, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
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
		btnNewButton.setImage(ResourceManager.getPluginImage(
				"org.goko.autoleveller", "icons/document-bounds-32.png"));
		btnNewButton.setToolTipText("Get from GCode bounds");
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));

		Composite composite_1 = new Composite(grpSettings, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		composite_1.setLayout(new GridLayout(6, false));

		Label lblXStep = new Label(composite_1, SWT.NONE);
		lblXStep.setAlignment(SWT.RIGHT);
		GridData gd_lblXStep = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblXStep.widthHint = 60;
		lblXStep.setLayoutData(gd_lblXStep);
		lblXStep.setText("Step");

		Label lblX_1 = new Label(composite_1, SWT.NONE);
		lblX_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblX_1.setText("X");

		txtStepX = new Text(composite_1, SWT.BORDER);
		GridData gd_txtStepX = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_txtStepX.minimumWidth = 60;
		gd_txtStepX.widthHint = 60;
		txtStepX.setLayoutData(gd_txtStepX);

		Label lblY_1 = new Label(composite_1, SWT.NONE);
		lblY_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblY_1.setText("Y");

		txtStepY = new Text(composite_1, SWT.BORDER);
		GridData gd_txtStepY = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_txtStepY.widthHint = 60;
		gd_txtStepY.minimumWidth = 60;
		txtStepY.setLayoutData(gd_txtStepY);
		new Label(composite_1, SWT.NONE);

		Label lblSafeZ = new Label(composite_1, SWT.NONE);
		lblSafeZ.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblSafeZ.setText("Expected");

		Label lblZ_1 = new Label(composite_1, SWT.NONE);
		lblZ_1.setText("Z");
		lblZ_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));

		txtExpectedZ = new Text(composite_1, SWT.BORDER);
		GridData gd_txtExpectedZ = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_txtExpectedZ.minimumWidth = 60;
		gd_txtExpectedZ.widthHint = 60;
		txtExpectedZ.setLayoutData(gd_txtExpectedZ);
		new Label(composite_1, SWT.NONE);

		Label lblExpectedZ = new Label(composite_1, SWT.NONE);
		lblExpectedZ.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblExpectedZ.setText("Start probe");

		txtStartProbe = new Text(composite_1, SWT.BORDER);
		GridData gd_txtStartProbe = new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1);
		gd_txtStartProbe.minimumWidth = 60;
		gd_txtStartProbe.widthHint = 60;
		txtStartProbe.setLayoutData(gd_txtStartProbe);

		Label lblProbeZ = new Label(composite_1, SWT.NONE);
		lblProbeZ.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblProbeZ.setText("Safe");

		Label lblZ = new Label(composite_1, SWT.NONE);
		lblZ.setText("Z");
		lblZ.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1,
				1));

		txtSafeZ = new Text(composite_1, SWT.BORDER);
		GridData gd_txtSafeZ = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_txtSafeZ.widthHint = 60;
		gd_txtSafeZ.minimumWidth = 60;
		txtSafeZ.setLayoutData(gd_txtSafeZ);
		new Label(composite_1, SWT.NONE);

		Label lblMaxProbeZ = new Label(composite_1, SWT.NONE);
		lblMaxProbeZ.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblMaxProbeZ.setText("Max Probe");

		txtMaxProbe = new Text(composite_1, SWT.BORDER);
		GridData gd_txtMaxProbe = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_txtMaxProbe.widthHint = 60;
		gd_txtMaxProbe.minimumWidth = 60;
		txtMaxProbe.setLayoutData(gd_txtMaxProbe);

		Composite composite_2 = new Composite(grpSettings, SWT.NONE);
		composite_2.setLayout(new GridLayout(4, false));
		composite_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));

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
				// ProgressMonitorDialog dialog = new
				// ProgressMonitorDialog(null);
				try {
					// dialog.run(true, false,
					// getController().getProbeCycleRunnable());
					getController().probe();
				} catch (GkException e1) {
					displayMessage(e1);
				}
			}
		});
		btnStartAutolevel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		btnStartAutolevel.setText("Start autolevel");
		try {
			initCustomBindings();
		} catch (GkException e1) {
			displayMessage(e1);
		}
		retrievePersistedState(part);
	}

	protected void initCustomBindings() throws GkException {
		getController().addBigDecimalModifyBinding(txtStartX, "startx");
		getController().addBigDecimalModifyBinding(txtStartY, "starty");
		getController().addBigDecimalModifyBinding(txtEndX, "endx");
		getController().addBigDecimalModifyBinding(txtEndY, "endy");
		getController().addBigDecimalModifyBinding(txtSafeZ, "safeZ");
		getController().addBigDecimalModifyBinding(txtStartProbe, "startProbe");
		getController().addBigDecimalModifyBinding(txtExpectedZ, "expectedZ");
		getController().addBigDecimalModifyBinding(txtMaxProbe, "maxZProbe");
		getController().addBigDecimalModifyBinding(txtStepX, "stepX");
		getController().addBigDecimalModifyBinding(txtStepY, "stepY");

		getDataModel().setStartx(BigDecimal.ZERO);
		getDataModel().setStarty(BigDecimal.ZERO);
		getDataModel().setEndx(BigDecimal.ZERO);
		getDataModel().setEndy(BigDecimal.ZERO);
		getDataModel().setSafeZ(BigDecimal.ZERO);
		getDataModel().setMaxZProbe(BigDecimal.ZERO);
		getDataModel().setStartProbe(BigDecimal.ZERO);
		getDataModel().setExpectedZ(BigDecimal.ZERO);
		getDataModel().setStepX(BigDecimal.ZERO);
		getDataModel().setStepY(BigDecimal.ZERO);
	}

	public void retrievePersistedState(MPart part){
		Map<String, String> states = part.getPersistedState();
		getDataModel().setExpectedZ( new BigDecimal( states.get(PERSITED_EXPECTED_Z)));
		getDataModel().setMaxZProbe(new BigDecimal( states.get(PERSITED_MAX_Z)));
		getDataModel().setSafeZ(new BigDecimal( states.get(PERSITED_SAFE_Z)));
		getDataModel().setStartProbe(new BigDecimal( states.get(PERSITED_START_Z)));
		getDataModel().setStepX(new BigDecimal( states.get(PERSITED_STEP_X)));
		getDataModel().setStepY(new BigDecimal( states.get(PERSITED_STEP_Y)));
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
}
