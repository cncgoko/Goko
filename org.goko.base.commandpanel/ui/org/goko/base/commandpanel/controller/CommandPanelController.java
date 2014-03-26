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
package org.goko.base.commandpanel.controller;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.action.DefaultControllerAction;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.event.MachineValueUpdateEvent;

/**
 * Command panel controller
 *
 * @author PsyKo
 *
 */
public class CommandPanelController  extends AbstractController<CommandPanelModel> {
	@Inject
	private IControllerService controllerService;

	public CommandPanelController(CommandPanelModel binding) {
		super(binding);
	}

	@Override
	public void initialize() throws GkException {
		controllerService.addListener(this);
	}

	public void bindEnableControlWithAction(Control widget, String actionId) throws GkException{
		getDataModel().setActionEnabled( actionId, false );
		// Let's do the binding
		IObservableValue 	modelObservable 	= Observables.observeMapEntry(getDataModel().getActionState(), actionId, Boolean.class);
		IObservableValue controlObservable 		= WidgetProperties.enabled().observe(widget);
		getBindingContext().bindValue(controlObservable, modelObservable);

		// If supported, let's report to the action definition
		if(controllerService.isControllerAction(actionId)){
			IGkControllerAction action = controllerService.getControllerAction(actionId);
			getDataModel().setActionEnabled( action.getId(), action.canExecute() );
		}
	}

	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineStateUpdate(final MachineValueUpdateEvent event) throws GkException{
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					refreshExecutableAction();
				} catch (GkException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void refreshExecutableAction() throws GkException{
		for(Object key : getDataModel().getActionState().keySet()){
			if(controllerService.isControllerAction(String.valueOf(key))){
				IGkControllerAction action = controllerService.getControllerAction(String.valueOf(key));
				getDataModel().setActionEnabled( action.getId(), action.canExecute() );
			}
		}
	}
	public void bindButtonToExecuteAction(Control widget, String actionId, final Object... parameters) throws GkException{
		if(controllerService.isControllerAction(actionId)){
			final IGkControllerAction action = controllerService.getControllerAction(actionId);
			widget.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					try {
						action.execute(parameters);
					} catch (GkException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
	}

	public void bindJogButton(Button widget, final String axis) throws GkException {
		if(controllerService.isControllerAction(DefaultControllerAction.JOG_START)
				&& controllerService.isControllerAction(DefaultControllerAction.JOG_STOP)){
			final IGkControllerAction actionStart = controllerService.getControllerAction(DefaultControllerAction.JOG_START);
			final IGkControllerAction actionStop = controllerService.getControllerAction(DefaultControllerAction.JOG_STOP);

			widget.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					try {
						actionStart.execute(axis, getDataModel().getJogSpeed());
					} catch (GkException e1) {

					}
				}
				@Override
				public void mouseUp(MouseEvent e) {
					try {
						actionStop.execute(axis);
					} catch (GkException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
	}

}

