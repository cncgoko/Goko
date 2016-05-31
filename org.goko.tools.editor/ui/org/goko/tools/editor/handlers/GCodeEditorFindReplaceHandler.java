/**
 * 
 */
package org.goko.tools.editor.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.tools.editor.GCodeEditorPart;
import org.goko.tools.editor.handlers.findreplace.GCodeFindReplaceDialog;

/**
 * @author Psyko
 * @date 30 mai 2016
 */
public class GCodeEditorFindReplaceHandler {

	@CanExecute
	public boolean canExecute(@Optional GCodeEditorPart part){
		if(part != null){			
			return part.getActiveDocumentProvider() != null;
		}
		return true;
	}
	
	//regler le probleme de dependences circulaires
	
	@Execute	
	public void execute(Shell shell, @Optional GCodeEditorPart part) throws GkException{
		if(part != null){			
			GCodeFindReplaceDialog dialog = new GCodeFindReplaceDialog(shell, part.getFindReplaceTargetSupplier());
			dialog.open();
		}
	}
}
