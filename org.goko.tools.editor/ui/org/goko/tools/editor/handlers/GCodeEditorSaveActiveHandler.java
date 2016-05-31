/**
 * 
 */
package org.goko.tools.editor.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.core.common.exception.GkException;
import org.goko.tools.editor.GCodeEditorPart;

/**
 * @author Psyko
 * @date 27 mai 2016
 */
public class GCodeEditorSaveActiveHandler {
	
	
	@CanExecute
	public boolean canExecute(@Optional GCodeEditorPart part){
		if(part != null){			
			return part.getActiveDocumentProvider() != null && part.getActiveDocumentProvider().isDirty();
		}
		return true;
	}
	
	//regler le probleme de dependences circulaires
	
	@Execute	
	public void execute(@Optional GCodeEditorPart part) throws GkException{
		if(part != null){
			part.saveActiveDocument();
		}
	}
}
