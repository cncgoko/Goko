/**
 * 
 */
package org.goko.tools.serial.jssc.console.handler;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.goko.core.common.exception.GkException;
import org.goko.tools.serial.jssc.console.internal.JsscSerialConsoleController;

/**
 * @author PsyKo
 * @date 6 janv. 2016
 */
public class JsscConsoleEnableHandler {
		
	@CanExecute
	public boolean canExecute(@Optional MHandledToolItem item, JsscSerialConsoleController jsscConsoleController){
		updateIcon(item, jsscConsoleController.getDataModel().isConsoleEnabled());
		return true;
	}
	
	@Execute
	public void execute(@Optional MHandledToolItem item, JsscSerialConsoleController jsscConsoleController) throws GkException{		
		boolean newState = !jsscConsoleController.getDataModel().isConsoleEnabled();
		jsscConsoleController.getDataModel().setConsoleEnabled(newState);
		updateIcon(item, newState);
	}

	private void updateIcon(MHandledToolItem item, boolean state){
		if(item != null){
			if(state){
				item.setIconURI("platform:/plugin/org.goko.tools.serial.jssc/resources/icons/activated.png");
			}else{
				item.setIconURI("platform:/plugin/org.goko.tools.serial.jssc/resources/icons/deactivated.png");
			}
		}
	}
}
