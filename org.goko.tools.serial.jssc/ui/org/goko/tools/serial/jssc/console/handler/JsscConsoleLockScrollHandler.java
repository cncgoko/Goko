/**
 * 
 */
package org.goko.tools.serial.jssc.console.handler;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.goko.core.common.exception.GkException;
import org.goko.tools.serial.jssc.console.internal.JsscSerialConsoleController;

/**
 * @author PsyKo
 * @date 6 janv. 2016
 */
public class JsscConsoleLockScrollHandler {
		
	@CanExecute
	public boolean canExecute(MHandledToolItem item, JsscSerialConsoleController jsscConsoleController){
		item.setSelected(jsscConsoleController.getDataModel().isScrollLock());
		return true;
	}
	
	@Execute
	public void execute(MHandledToolItem item, JsscSerialConsoleController jsscConsoleController) throws GkException{		
		boolean newState = !jsscConsoleController.getDataModel().isScrollLock();
		jsscConsoleController.getDataModel().setScrollLock(newState);
		item.setSelected(newState);
	}
}
