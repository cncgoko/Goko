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
package org.goko.tools.commandpanel.controller;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.controller.IContinuousJogService;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IStepJogService;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.log.GkLog;
import org.goko.tools.commandpanel.Activator;
import org.goko.tools.commandpanel.CommandPanelParameter;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Command panel controller
 *
 * @author PsyKo
 *
 */
public class CommandPanelController  extends AbstractController<CommandPanelModel> {
	private final static GkLog LOG = GkLog.getLogger(CommandPanelController.class);
	@Inject
	private IControllerService controllerService;
	@Inject
	@Optional
	private ICoordinateSystemAdapter coordinateSystemAdapter;
	@Inject
	@Optional
	private IContinuousJogService continuousJogService;
	@Inject
	@Optional
	private IStepJogService stepJogService;

	@Inject	
	public void initServices(){
		// tinyg
//		try {
//			Bundle bundle = FrameworkUtil.getBundle(this.getClass());
//			BundleContext context = bundle.getBundleContext();
//			List<ServiceReference<IControllerService>> serviceReferences = new ArrayList<ServiceReference<IControllerService>>();
//			serviceReferences.addAll( context.getServiceReferences(IControllerService.class, "(targetBoard=tinyg)") );
//			
//			String filterString = "(targetBoard=tinyg)";
//			
//		   	context.getServiceReferences(IControllerService.class, filterString);
//		   	controllerService = context.getService(serviceReferences.get(0));
//			
//		} catch (InvalidSyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public CommandPanelController(CommandPanelModel binding) {
		super(binding);
	}

	@Override
	public void initialize() throws GkException {
		controllerService.addListener(this);
		getDataModel().setStepModeChoiceEnabled( continuousJogService != null && stepJogService != null);
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
					LOG.error(e);
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
						LOG.error(e1);
					}
				}
			});
		}
	}
	
	@Inject
	@Optional
	public void settingChanged(@Preference(nodePath=Activator.PREFERENCE_NODE, value=CommandPanelParameter.JOG_FEEDRATE) String val){
		System.err.println(val);
	}

	public void bindJogButton(Button widget, final EnumControllerAxis axis) throws GkException {

		widget.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					if(getDataModel().isIncrementalJog()){
						Unit<Length> unit = controllerService.getCurrentGCodeContext().getUnit().getUnit();
						getDataModel().setLengthUnit(unit);
						BigDecimalQuantity<Length> step = NumberQuantity.of(getDataModel().getJogIncrement(), unit);
						stepJogService.startJog(axis, getDataModel().getJogSpeed(), step);
					}else{
						continuousJogService.startJog(axis, getDataModel().getJogSpeed());
					}
				} catch (GkException e1) {
					LOG.error(e1);
				}
			}

			@Override
			public void mouseUp(MouseEvent e) {
				try {
					Unit<Length> unit = controllerService.getCurrentGCodeContext().getUnit().getUnit();
					getDataModel().setLengthUnit(unit);
					if(getDataModel().isIncrementalJog()){
						stepJogService.stopJog();
					}else{
						continuousJogService.stopJog();
					}
				} catch (GkException e1) {
					LOG.error(e1);
				}
			}
		});
	}

	public void initilizeValues() throws GkException {
		Unit<Length> unit = controllerService.getCurrentGCodeContext().getUnit().getUnit();
		getDataModel().setLengthUnit(unit);
		getDataModel().setJogSpeed( new BigDecimal(getPreferences().get(CommandPanelParameter.JOG_FEEDRATE, StringUtils.EMPTY)) );
		getDataModel().setJogIncrement( new BigDecimal(getPreferences().get(CommandPanelParameter.JOG_STEP_SIZE, StringUtils.EMPTY)) );
		if(stepJogService != null && continuousJogService != null){
			getDataModel().setIncrementalJog( Boolean.valueOf(getPreferences().get(CommandPanelParameter.JOG_INCREMENTAL, "false")) );
		}else if( stepJogService != null ){
			getDataModel().setIncrementalJog( true );
		}else{
			getDataModel().setIncrementalJog( false );
		}
	}

	public void saveValues() {
		//Cette facon d'utiliser les preferences est la bonne
		if(getDataModel() != null){
			getPreferences().put(CommandPanelParameter.JOG_FEEDRATE, getDataModel().getJogSpeed().toPlainString());
			getPreferences().put(CommandPanelParameter.JOG_INCREMENTAL, Boolean.toString(getDataModel().isIncrementalJog()));
			getPreferences().put(CommandPanelParameter.JOG_STEP_SIZE, getDataModel().getJogIncrement().toPlainString());

			try {
				getPreferences().flush();
			} catch (BackingStoreException e) {
				LOG.error(e);
			}
		}
	}
	
	private IEclipsePreferences getPreferences(){
		return Activator.getPreferences();
	}

	public void setCoordinateSystem(EnumCoordinateSystem enumCoordinateSystem) throws GkException {
		coordinateSystemAdapter.setCurrentCoordinateSystem(enumCoordinateSystem);
	}

	public void zeroCoordinateSystem() throws GkException {
		coordinateSystemAdapter.resetCurrentCoordinateSystem();
	}

}

