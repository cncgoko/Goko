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
package org.goko.tools.serial.jssc.toolbar.control;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.GkUiComponent;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.tools.serial.jssc.JsscUiEvent;

/**
 * The Jssc Combo in the toolbar that allow the change of the com port
 * @author PsyKo
 *
 */
public class JsscSerialPortSelectorCombo extends GkUiComponent<JsscSerialPortSelectorController, JsscSerialPortSelectorModel>{
	/** The combo */
	private GkCombo<LabeledValue<String>> gkCombo;

	/**
	 * Constructor
	 * @param context IEclipseContext
	 */
	@Inject
	public JsscSerialPortSelectorCombo(IEclipseContext context) {
		super(new JsscSerialPortSelectorController());
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			displayMessage(e);
		}
	}

	/**
	 * Create the combo
	 * @param parent the parent composite
	 */
	@PostConstruct
	public void createGui(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 3;
		layout.marginHeight = 0;
		layout.marginTop = 3;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);

		gkCombo = new GkCombo<LabeledValue<String>>(composite, SWT.READ_ONLY);
		Combo combo = gkCombo.getCombo();
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.widthHint = 60;

		combo.setLayoutData(gd);
		try{
			getController().addItemsBinding(gkCombo, "availableSerialPorts");
			getController().addItemSelectionBinding(gkCombo, "selectedSerialPort");
			getController().addEnableReverseBinding(gkCombo, "serviceConnected");

		}catch(GkException e){
			displayMessage(e);
		}

		gkCombo.addPostSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				getController().onSerialPortChange();
			}
		});

		getController().refreshSerialPortList();
	}

	/**
	 * Refresh the list of available ports when requested
	 * @param event
	 */
	@Inject
	@Optional
	public void getNotifiedRefresh(@UIEventTopic(JsscUiEvent.JSSC_REFRESH_COM_PORT_LIST) boolean event){
		getController().refreshSerialPortList();
	}

}
