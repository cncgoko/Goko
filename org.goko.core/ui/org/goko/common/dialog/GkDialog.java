/**
 * 
 */
package org.goko.common.dialog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;

/**
 * @author PsyKo
 *
 */
public class GkDialog {

	public static int openDialog(GkFunctionalException e){
		return openDialog(null, e);
	}
	
	public static int openDialog(Shell parentShell, GkFunctionalException e){
		return GkWarningDialog.openDialog(parentShell, e);
	}
	
	public static int openDialog(Shell parentShell, IStatus status){
		return GkWarningDialog.openDialog(parentShell, status);
	}
	
	public static int openDialog(GkException e){
		return openDialog(null, e);
	}
	
	public static int openDialog(Shell parentShell, GkException e){
		if(e instanceof GkFunctionalException){
			return GkWarningDialog.openDialog(parentShell, (GkFunctionalException)e);
		}else{
			return new GkErrorDialog(parentShell,e).open();
		}
	}
}
