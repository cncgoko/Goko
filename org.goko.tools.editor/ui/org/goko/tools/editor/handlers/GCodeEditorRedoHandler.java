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

/**
 * @author Psyko
 * @date 31 mai 2016
 */
public class GCodeEditorRedoHandler {

	@CanExecute
	public boolean canExecute(@Optional GCodeEditorPart part){
		if(part != null){			
			return part.getUndoManager() != null 
					&& part.getUndoManager().redoable();
		}
		return false;
	}
		
	@Execute	
	public void execute(Shell shell, @Optional GCodeEditorPart part) throws GkException{
		if(part != null){			
			if(part.getUndoManager() != null){
				part.getUndoManager().redo();
			}
		}
	}
}
