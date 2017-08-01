/**
 * 
 */
package org.goko.tools.viewer.jogl.command;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.service.JoglSceneManager;

/**
 * @author Psyko
 * @date 23 juil. 2017
 */
public class TogglePositionDisplayHandler {
	
	@CanExecute
	public boolean canExecute(@Optional MHandledToolItem item, JoglSceneManager manager){
		if(item != null){
			item.setSelected(manager.isDisplayPositionOverlay());
		}
		return true;
	}

	@Execute
	public void execute(@Optional MHandledToolItem item, IEclipseContext context, JoglSceneManager manager) throws GkException{
		boolean newState = !manager.isDisplayPositionOverlay();
		manager.setDisplayPositionOverlay(newState);
		System.err.println("---------");
		if(item != null){
			item.setSelected(newState);
		}
	}
}
