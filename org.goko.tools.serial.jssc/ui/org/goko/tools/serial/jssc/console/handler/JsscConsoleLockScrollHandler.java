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
public class JsscConsoleLockScrollHandler {
		
	@CanExecute
	public boolean canExecute(@Optional MHandledToolItem item, JsscSerialConsoleController jsscConsoleController){
		if(item != null){
			item.setSelected(jsscConsoleController.getDataModel().isScrollLock());
		}
		return true;
	}
	
	@Execute
	public void execute(@Optional MHandledToolItem item, JsscSerialConsoleController jsscConsoleController) throws GkException{		
		boolean newState = !jsscConsoleController.getDataModel().isScrollLock();
		jsscConsoleController.getDataModel().setScrollLock(newState);
		if(item != null){
			item.setSelected(newState);
		}
	}
}
