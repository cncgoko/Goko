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
package org.goko.base.commandpanel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.base.commandpanel.controller.CommandPanelController;
import org.goko.base.commandpanel.controller.CommandPanelModel;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IGkConstants;
import org.goko.core.controller.action.DefaultControllerAction;

public class CommandPanelPart extends GkUiComponent<CommandPanelController, CommandPanelModel> {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Button btnHome;
	private Button btnStop;
	private Button btnPause;
	private Button btnStart;
	private Button btnResetZero;
	private Button btnJogZPos;
	private Button btnSpindleOn;
	private Button btnSpindleOff;
	private Button btnJogXNeg;
	private Button btnJogXPos;
	private Button btnJogYNeg;
	private Button btnJogYPos;
	private Button btnJogZNeg;
	private Text txtJogFeed;
	private Button btnResetX;
	private Button btnResetY;
	private Button btnResetZ;
	private Composite composite_7;
	private Button btnJogANeg;
	private Button btnJogAPos;
	private Button btnResetA;

	@Inject @Preference
	IEclipsePreferences prefs;
	private Text txtJogStep;
	private Button btnIncrementalJog;
	private Button btnKillAlarm;



	@Inject
	public CommandPanelPart(IEclipseContext context) throws GkException {
		super(new CommandPanelController(new CommandPanelModel()));
		ContextInjectionFactory.inject(getController(), context);
		getController().initialize();
	}

	/**
	 * Create contents of the view part.
	 *
	 * @throws GkException
	 */
	@PostConstruct
	public void createControls(Composite parent, MPart part) throws GkException {

		parent.setLayout(new FormLayout());

		Composite composite = formToolkit.createComposite(parent, SWT.NONE);
		composite.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyboard(e);
			}
		});
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		formToolkit.paintBordersFor(composite);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginHeight = 3;
		gl_composite.marginWidth = 3;
		composite.setLayout(gl_composite);

		Group grpManualJog = new Group(composite, SWT.NONE);
		grpManualJog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpManualJog.setText("Manual jog");
		formToolkit.adapt(grpManualJog);
		formToolkit.paintBordersFor(grpManualJog);
		grpManualJog.setLayout(new GridLayout(1, false));

		///if(getDataModel().isIncrementalJogSupported()){
			btnIncrementalJog = new Button(grpManualJog, SWT.CHECK);
			formToolkit.adapt(btnIncrementalJog, true, true);
			btnIncrementalJog.setText("Incremental jog");
		//}
		Composite composite_5 = new Composite(grpManualJog, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(composite_5);
		formToolkit.paintBordersFor(composite_5);
		composite_5.setLayout(new GridLayout(2, false));

		//if(getDataModel().isIncrementalJogSupported()){
			Label lblJogStep = new Label(composite_5, SWT.NONE);
			lblJogStep.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblJogStep.setText("Jog step :");
			formToolkit.adapt(lblJogStep, true, true);

			txtJogStep = new Text(composite_5, SWT.BORDER);
			txtJogStep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			formToolkit.adapt(txtJogStep, true, true);
		//}

		Label lblJogSpeed = new Label(composite_5, SWT.NONE);
		lblJogSpeed.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblJogSpeed.setBounds(0, 0, 55, 15);
		formToolkit.adapt(lblJogSpeed, true, true);
		lblJogSpeed.setText("Jog speed :");

		txtJogFeed = new Text(composite_5, SWT.BORDER);
		GridData gd_txtJogFeed = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_txtJogFeed.widthHint = 60;
		txtJogFeed.setLayoutData(gd_txtJogFeed);
		formToolkit.adapt(txtJogFeed, true, true);
		Composite composite_4 = new Composite(grpManualJog, SWT.NONE);
		formToolkit.adapt(composite_4);
		formToolkit.paintBordersFor(composite_4);
		GridLayout gl_composite_4 = new GridLayout(2, false);
		gl_composite_4.horizontalSpacing = 0;
		gl_composite_4.verticalSpacing = 0;
		composite_4.setLayout(gl_composite_4);

		Composite composite_2 = formToolkit.createComposite(composite_4,
				SWT.NONE);
		composite_2.setSize(45, 125);
		composite_2.setLayout(new GridLayout(1, false));
		formToolkit.paintBordersFor(composite_2);

		btnJogZPos = formToolkit.createButton(composite_2, "+Z", SWT.NONE);
		GridData gd_btnJogZPos = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnJogZPos.heightHint = 55;
		gd_btnJogZPos.widthHint = 35;
		btnJogZPos.setLayoutData(gd_btnJogZPos);

		btnJogZNeg = formToolkit.createButton(composite_2, "-Z", SWT.NONE);

		GridData gd_btnJogZNeg = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnJogZNeg.widthHint = 35;
		gd_btnJogZNeg.heightHint = 55;
		btnJogZNeg.setLayoutData(gd_btnJogZNeg);

		Composite composite_1 = formToolkit.createComposite(composite_4,
				SWT.NONE);
		formToolkit.paintBordersFor(composite_1);
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.horizontalSpacing = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);
		new Label(composite_1, SWT.NONE);

		btnJogYPos = formToolkit.createButton(composite_1, "+Y", SWT.NONE);
		GridData gd_btnJogYPos = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnJogYPos.heightHint = 35;
		gd_btnJogYPos.widthHint = 35;
		btnJogYPos.setLayoutData(gd_btnJogYPos);
		new Label(composite_1, SWT.NONE);

		btnJogXNeg = formToolkit.createButton(composite_1, "-X", SWT.NONE);
		GridData gd_btnJogXNeg = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnJogXNeg.widthHint = 35;
		gd_btnJogXNeg.heightHint = 35;
		btnJogXNeg.setLayoutData(gd_btnJogXNeg);
		new Label(composite_1, SWT.NONE);

		btnJogXPos = formToolkit.createButton(composite_1, "+X", SWT.NONE);
		GridData gd_btnJogXPos = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnJogXPos.heightHint = 35;
		gd_btnJogXPos.widthHint = 35;
		btnJogXPos.setLayoutData(gd_btnJogXPos);
		new Label(composite_1, SWT.NONE);

		btnJogYNeg = formToolkit.createButton(composite_1, "-Y", SWT.NONE);
		GridData gd_btnJogYNeg = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnJogYNeg.widthHint = 35;
		gd_btnJogYNeg.heightHint = 35;
		btnJogYNeg.setLayoutData(gd_btnJogYNeg);
		new Label(composite_1, SWT.NONE);
		new Label(composite_4, SWT.NONE);

		composite_7 = new Composite(composite_4, SWT.NONE);
		composite_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(composite_7);
		formToolkit.paintBordersFor(composite_7);
		composite_7.setLayout(new GridLayout(2, true));

		btnJogANeg = new Button(composite_7, SWT.NONE);
		GridData gd_btnANeg = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnANeg.heightHint = 35;
		btnJogANeg.setLayoutData(gd_btnANeg);
		formToolkit.adapt(btnJogANeg, true, true);
		btnJogANeg.setText("A-");

		btnJogAPos = new Button(composite_7, SWT.NONE);
		GridData gd_btnAPos = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnAPos.heightHint = 35;
		btnJogAPos.setLayoutData(gd_btnAPos);
		formToolkit.adapt(btnJogAPos, true, true);
		btnJogAPos.setText("A+");

		Group grpCommands = new Group(composite, SWT.NONE);
		grpCommands.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpCommands.setLayout(new GridLayout(1, false));
		grpCommands.setText("Zeroing");
		formToolkit.adapt(grpCommands);
		formToolkit.paintBordersFor(grpCommands);

		btnHome = formToolkit.createButton(grpCommands, "Home", SWT.NONE);
		GridData gd_btnNewButton_1 = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_btnNewButton_1.heightHint = 35;
		btnHome.setLayoutData(gd_btnNewButton_1);

		Label label = formToolkit.createSeparator(grpCommands, SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		btnResetZero = formToolkit.createButton(grpCommands, "Zero all axis",
				SWT.NONE);
		GridData gd_btnNewButton_2 = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_btnNewButton_2.heightHint = 35;
		btnResetZero.setLayoutData(gd_btnNewButton_2);

		Composite composite_6 = new Composite(grpCommands, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(composite_6);
		formToolkit.paintBordersFor(composite_6);
		GridLayout gl_composite_6 = new GridLayout(4, true);
		gl_composite_6.horizontalSpacing = 1;
		gl_composite_6.marginHeight = 0;
		gl_composite_6.verticalSpacing = 0;
		gl_composite_6.marginWidth = 0;
		composite_6.setLayout(gl_composite_6);

		btnResetX = formToolkit.createButton(composite_6, "X", SWT.NONE);
		btnResetX.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		GridData gd_btnResetX = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnResetX.heightHint = 30;
		btnResetX.setLayoutData(gd_btnResetX);

		btnResetY = formToolkit.createButton(composite_6, "Y", SWT.NONE);
		btnResetY.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		GridData gd_btnNewButton = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnNewButton.heightHint = 30;
		btnResetY.setLayoutData(gd_btnNewButton);

				btnResetZ = formToolkit.createButton(composite_6, "Z", SWT.NONE);
				btnResetZ.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
				GridData gd_btnNewButton_21 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				gd_btnNewButton_21.heightHint = 30;
				btnResetZ.setLayoutData(gd_btnNewButton_21);

		btnResetA = new Button(composite_6, SWT.NONE);
		GridData gd_btnResetA = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnResetA.heightHint = 30;
		btnResetA.setLayoutData(gd_btnResetA);
		formToolkit.adapt(btnResetA, true, true);
		btnResetA.setText("A");

		Group grpControls = new Group(composite, SWT.NONE);
		grpControls.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpControls.setLayout(new GridLayout(1, false));
		grpControls.setText("Controls");
		formToolkit.adapt(grpControls);
		formToolkit.paintBordersFor(grpControls);

		Composite composite_8 = new Composite(grpControls, SWT.NONE);
		composite_8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(composite_8);
		formToolkit.paintBordersFor(composite_8);
		GridLayout gl_composite_8 = new GridLayout(2, true);
		gl_composite_8.marginWidth = 0;
		gl_composite_8.marginHeight = 0;
		composite_8.setLayout(gl_composite_8);

						btnStart = new Button(composite_8, SWT.NONE);
						GridData gd_btnStart = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
						gd_btnStart.heightHint = 35;
						btnStart.setLayoutData(gd_btnStart);
						formToolkit.adapt(btnStart, true, true);
						btnStart.setText("Start/Resume");

				btnPause = new Button(composite_8, SWT.NONE);
				GridData gd_btnPause = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				gd_btnPause.heightHint = 35;
				btnPause.setLayoutData(gd_btnPause);
				formToolkit.adapt(btnPause, true, true);
				btnPause.setText("Pause");

		btnStop = new Button(grpControls, SWT.NONE);
		btnStop.setImage(ResourceManager.getPluginImage("org.goko.base.commandpanel", "icons/stop.png"));
		btnStop.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		GridData gd_btnStop = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_btnStop.heightHint = 35;
		btnStop.setLayoutData(gd_btnStop);
		formToolkit.adapt(btnStop, true, true);
		btnStop.setText("Stop");

		btnKillAlarm = new Button(grpControls, SWT.NONE);
		btnKillAlarm.setImage(ResourceManager.getPluginImage("org.goko.base.commandpanel", "icons/bell--minus.png"));
		GridData gd_btnKillAlarm = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnKillAlarm.heightHint = 35;
		btnKillAlarm.setLayoutData(gd_btnKillAlarm);
		formToolkit.adapt(btnKillAlarm, true, true);
		btnKillAlarm.setText("Kill alarm");

		Group grpSpindle = new Group(composite, SWT.NONE);
		grpSpindle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpSpindle.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpSpindle.setText("Spindle");
		formToolkit.adapt(grpSpindle);
		formToolkit.paintBordersFor(grpSpindle);

		Composite composite_3 = new Composite(grpSpindle, SWT.NONE);
		formToolkit.adapt(composite_3);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(2, true));

		btnSpindleOn = formToolkit.createButton(composite_3, "On", SWT.NONE);
		GridData gd_btnSpindleOn = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnSpindleOn.heightHint = 35;
		btnSpindleOn.setLayoutData(gd_btnSpindleOn);

		btnSpindleOff = formToolkit.createButton(composite_3, "Off", SWT.NONE);
		GridData gd_btnSpindleOff = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnSpindleOff.heightHint = 35;
		btnSpindleOff.setLayoutData(gd_btnSpindleOff);

		getController().initilizeValues();
		initCustomBindings(part);

	}

	protected void handleKeyboard(KeyEvent e) {
		System.err.println(e);

	}

	protected void initCustomBindings(MPart part) throws GkException {
		getController().bindEnableControlWithAction(btnHome, DefaultControllerAction.HOME);
		getController().bindButtonToExecuteAction(btnHome, DefaultControllerAction.HOME);
		getController().bindEnableControlWithAction(btnStart,DefaultControllerAction.START);
		getController().bindButtonToExecuteAction(btnStart, DefaultControllerAction.START);
		getController().bindEnableControlWithAction(btnPause, DefaultControllerAction.PAUSE);
		getController().bindButtonToExecuteAction(btnPause, DefaultControllerAction.PAUSE);
		getController().bindEnableControlWithAction(btnStop, DefaultControllerAction.STOP);
		getController().bindButtonToExecuteAction(btnStop, DefaultControllerAction.STOP);
		getController().bindEnableControlWithAction(btnResetZero, DefaultControllerAction.RESET_ZERO);
		getController().bindButtonToExecuteAction(btnResetZero, DefaultControllerAction.RESET_ZERO);
		getController().bindEnableControlWithAction(btnResetX, DefaultControllerAction.RESET_ZERO);
		getController().bindButtonToExecuteAction(btnResetX, DefaultControllerAction.RESET_ZERO, IGkConstants.X_AXIS);
		getController().bindEnableControlWithAction(btnResetY, DefaultControllerAction.RESET_ZERO);
		getController().bindButtonToExecuteAction(btnResetY, DefaultControllerAction.RESET_ZERO, IGkConstants.Y_AXIS);
		getController().bindEnableControlWithAction(btnResetZ, DefaultControllerAction.RESET_ZERO);
		getController().bindButtonToExecuteAction(btnResetZ, DefaultControllerAction.RESET_ZERO, IGkConstants.Z_AXIS);
		getController().bindEnableControlWithAction(btnResetA, DefaultControllerAction.RESET_ZERO);
		getController().bindButtonToExecuteAction(btnResetA, DefaultControllerAction.RESET_ZERO, IGkConstants.A_AXIS);
		getController().bindEnableControlWithAction(btnSpindleOn, DefaultControllerAction.SPINDLE_ON);
		getController().bindButtonToExecuteAction(btnSpindleOn, DefaultControllerAction.SPINDLE_ON);
		getController().bindEnableControlWithAction(btnSpindleOff, DefaultControllerAction.SPINDLE_OFF);
		getController().bindButtonToExecuteAction(btnSpindleOff, DefaultControllerAction.SPINDLE_OFF);
		getController().bindEnableControlWithAction(btnKillAlarm, DefaultControllerAction.KILL_ALARM);
		getController().bindButtonToExecuteAction(btnKillAlarm, DefaultControllerAction.KILL_ALARM);

		getController().addBigDecimalModifyBinding(txtJogFeed, "jogSpeed");
		//if(getDataModel().isIncrementalJogSupported()){
			getController().addBigDecimalModifyBinding(txtJogStep, "jogIncrement");
			getController().addSelectionBinding(btnIncrementalJog, "incrementalJog");
			getController().addEnableBinding(txtJogStep, "incrementalJog");
			getController().bindEnableControlWithAction(txtJogStep, DefaultControllerAction.JOG_START);
			getController().bindEnableControlWithAction(btnIncrementalJog, DefaultControllerAction.JOG_START);
		//}

		getController().bindEnableControlWithAction(btnJogYPos, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogYNeg, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogXPos, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogXNeg, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogZPos, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogZNeg, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogAPos, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(btnJogANeg, DefaultControllerAction.JOG_START);
		getController().bindEnableControlWithAction(txtJogFeed, DefaultControllerAction.JOG_START);



		getController().bindJogButton(btnJogYPos, IGkConstants.Y_AXIS);
		getController().bindJogButton(btnJogYNeg, IGkConstants.Y_AXIS_NEGATIVE);
		getController().bindJogButton(btnJogXPos, IGkConstants.X_AXIS);
		getController().bindJogButton(btnJogXNeg, IGkConstants.X_AXIS_NEGATIVE);
		getController().bindJogButton(btnJogZPos, IGkConstants.Z_AXIS);
		getController().bindJogButton(btnJogZNeg, IGkConstants.Z_AXIS_NEGATIVE);
		getController().bindJogButton(btnJogAPos, IGkConstants.A_AXIS);
		getController().bindJogButton(btnJogANeg, IGkConstants.A_AXIS_NEGATIVE);
	}

	@PreDestroy
	public void dispose() {
	}

	@PersistState
	public void persist(MPart part) {
		getController().saveValues();
	}

	@Focus
	public void setFocus() throws GkException {
		getController().refreshExecutableAction();
		getController().saveValues();

	}

}

