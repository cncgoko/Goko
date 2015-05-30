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
package org.goko.base.dro.controller;

import javax.inject.Inject;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.goko.base.dro.IDROService;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.log.GkLog;

/**
 * Controller for the DRO part
 *
 * @author PsyKo
 *
 */
public class DisplayReadOutController extends AbstractController<DisplayReadOutModel> {
	private static final GkLog LOG = GkLog.getLogger(DisplayReadOutController.class);
	
	@Inject
	private IControllerService controllerService;
	@Inject
	private IDROService droService;

	
	@Inject
	public DisplayReadOutController() {
		super(new DisplayReadOutModel());
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {

		updateDisplayedValues();
		controllerService.addListener(this);

	}

	public void updateDisplayedValues() throws GkException{
		getDataModel().getWritableObservedValuesDefinition().clear();
		for(MachineValueDefinition definition : droService.getDisplayedMachineValueDefinition()){
			getDataModel().addObservedValueDefinition(definition);
			getDataModel().addObservedValue(definition.getId(), controllerService.getMachineValue(definition.getId(),definition.getType()));
		}
	}

	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineStateUpdate(MachineValueUpdateEvent updateEvent){
		final MachineValue value= updateEvent.getTarget();
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				//MachineValueDefinition definition = controllerService.getMachineValueDefinition(value.getIdDescriptor());
				getDataModel().addObservedValue(value.getIdDescriptor(), value);
			}
		});
	}

	public void enableTextBindingOnValue(Text widget, String valueId){
		IObservableValue 	modelObservable 	= Observables.observeMapEntry(getDataModel().getWritableObservedValues(), valueId);
		IObservableValue controlObservable 		= WidgetProperties.text().observe(widget);

		UpdateValueStrategy policy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		policy.setConverter(new IConverter() {
			@Override
			public Object getToType() {
				return String.class;
			}

			@Override
			public Object getFromType() {
				return Object.class;
			}

			@Override
			public Object convert(Object fromObject) {
				return ((MachineValue)fromObject).getStringValue();
			}
		});
		getBindingContext().bindValue(controlObservable, modelObservable,null, policy);



	}
}
