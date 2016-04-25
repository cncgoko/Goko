package org.goko.controller.grbl.v09.internal.handlers;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.goko.controller.grbl.v09.GrblMachineState;
import org.goko.controller.grbl.v09.IGrblControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

public class GrblCheckModeHandler {
	private static final GkLog LOG = GkLog.getLogger(GrblCheckModeHandler.class);
		
	@CanExecute
	public boolean canExecute(MHandledMenuItem menuItem, IGrblControllerService controllerService){
		try {
			menuItem.setSelected(ObjectUtils.equals( GrblMachineState.CHECK, controllerService.getState()));	
						
			return ObjectUtils.equals( GrblMachineState.READY, controllerService.getState()) || ObjectUtils.equals( GrblMachineState.CHECK, controllerService.getState());
		} catch (GkException e) {			
			LOG.error(e);
		}
		return false;
	}
	
	@Execute
	public void execute(MHandledMenuItem menuItem, IGrblControllerService service) throws GkException {
		if(ObjectUtils.equals( GrblMachineState.CHECK, service.getState())){
			service.setCheckModeEnabled(false);
			menuItem.setSelected(false);
		}else{
			service.setCheckModeEnabled(true);
			menuItem.setSelected(true);
		}
				
	}
}
