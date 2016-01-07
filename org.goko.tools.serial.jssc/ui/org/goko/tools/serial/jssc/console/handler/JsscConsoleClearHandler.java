/**
 * 
 */
package org.goko.tools.serial.jssc.console.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.goko.core.common.exception.GkException;
import org.goko.tools.serial.jssc.console.internal.JsscSerialConsoleController;

/**
 * @author PsyKo
 * @date 6 janv. 2016
 */
public class JsscConsoleClearHandler {

	@Execute
	public void execute(JsscSerialConsoleController jsscConsoleController) throws GkException{		
		jsscConsoleController.clearConsole();		
	}
}
