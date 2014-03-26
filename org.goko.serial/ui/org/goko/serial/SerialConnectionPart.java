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
package org.goko.serial;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.serial.bindings.SerialConnectionBindings;
import org.goko.serial.bindings.SerialConnectionController;

public class SerialConnectionPart {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(SerialConnectionPart.class);

	SerialConnectionController controller;

	private DataBindingContext m_bindingContext;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private SerialConnectionBindings bindings;

	private GkCombo<LabeledValue> comboSerialPort;
	private Button btnConnect;
	private Button btnDisconnect;
	private Label lblDisconnected_1;
	private Label lblDisconnected;
	private GkCombo<LabeledValue> comboBaudrate;
	private Label lblStatus;

	@Inject
	public SerialConnectionPart(IEclipseContext context) {
		bindings = new SerialConnectionBindings();
		controller = new SerialConnectionController(bindings);
		ContextInjectionFactory.inject(controller, context);
		try {
			controller.initialize();
		} catch (GkException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the view part.
	 * @throws GkException
	 */
	@PostConstruct
	public void createControls(Composite parent)  {
		try{
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		parent.setLayout(gl_parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(2, false));

		lblStatus = formToolkit.createLabel(composite, "Status :", SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStatus.setBounds(0, 0, 55, 15);

		Composite composite_3 = formToolkit.createComposite(composite, SWT.NONE);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(2, false));

		lblDisconnected = formToolkit.createLabel(composite_3, "Disconnected", SWT.NONE);
		lblDisconnected.setImage(ResourceManager.getPluginImage("org.goko.serial", "icons/bullet_red.png"));

		lblDisconnected_1 = formToolkit.createLabel(composite_3, "Disconnected", SWT.NONE);

		Label lblSerialPort = new Label(composite, SWT.NONE);
		lblSerialPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSerialPort.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		formToolkit.adapt(lblSerialPort, true, true);
		lblSerialPort.setText("Serial port :");

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);

		comboSerialPort = new GkCombo<LabeledValue>(composite_1, SWT.NONE  | SWT.READ_ONLY);
		Combo combo = comboSerialPort.getCombo();
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 40;
		combo.setLayoutData(gd_combo);


		comboBaudrate = new GkCombo<LabeledValue>(composite_1, SWT.NONE  | SWT.READ_ONLY);
		Combo combo_1 = comboBaudrate.getCombo();
		GridData gd_combo_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_1.widthHint = 40;
		combo_1.setLayoutData(gd_combo_1);


		Button button = formToolkit.createButton(composite_1, "", SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.refreshSerialPortList();
			}
		});

		button.setImage(ResourceManager.getPluginImage("org.goko.serial", "icons/arrow-circle-double.png"));
		new Label(composite, SWT.NONE);

		Composite composite_2 = formToolkit.createComposite(composite, SWT.NONE);
		formToolkit.paintBordersFor(composite_2);
		composite_2.setLayout(new GridLayout(2, false));

		btnConnect = formToolkit.createButton(composite_2, "Connect", SWT.NONE);
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.connect();
			}
		});
		btnConnect.setEnabled(false);

		btnDisconnect = formToolkit.createButton(composite_2, "Disconnect", SWT.NONE);
		btnDisconnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.disconnect();
			}
		});
		btnDisconnect.setEnabled(false);
		btnDisconnect.setBounds(0, 0, 75, 25);
		m_bindingContext = initDataBindings();
		}catch(GkException e){
			LOG.error(e);
		}
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}
	protected DataBindingContext initDataBindings() throws GkException {
		DataBindingContext bindingContext = new DataBindingContext();

		IObservableValue observeImageLblDisconnectedObserveWidget = WidgetProperties.image().observe(lblDisconnected);
		IObservableValue connectionImageBindingsObserveValue = BeanProperties.value("connectionImage").observe(bindings);
		bindingContext.bindValue(observeImageLblDisconnectedObserveWidget, connectionImageBindingsObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
		//
		controller.addEnableBinding(btnConnect, "connectionAllowed");
		controller.addEnableBinding(btnDisconnect, "disconnectionAllowed");

		controller.addTextDisplayBinding(lblDisconnected_1, "connectionStatus");

		controller.addItemsBinding(comboBaudrate, "choiceBaudrate");
		controller.addItemSelectionBinding(comboBaudrate, "baudrate");
		controller.addEnableReverseBinding(comboBaudrate, "connected");

		controller.addItemsBinding(comboSerialPort, "choiceSerialPort");
		controller.addItemSelectionBinding(comboSerialPort, "serialPort");
		controller.addEnableReverseBinding(comboSerialPort, "connected");

		controller.refreshSerialPortList();
		//
		return bindingContext;
	}
}
