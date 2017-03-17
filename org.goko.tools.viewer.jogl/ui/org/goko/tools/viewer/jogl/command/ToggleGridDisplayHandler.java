package org.goko.tools.viewer.jogl.command;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.model.GCodeViewer3DController;

/**
 * Handler for the ToolItem of the GCode viewer part allowing to enable/disable the grid
 *
 * @author Psyko
 */
public class ToggleGridDisplayHandler {

	@CanExecute
	public boolean canExecute(@Optional MHandledToolItem item, GCodeViewer3DController controller){
		if(item != null){
			item.setSelected(controller.getDataModel().isShowGrid());
		}
		return true;
	}

	@Execute
	public void execute(@Optional MHandledToolItem item, IEclipseContext context, GCodeViewer3DController controller) throws GkException{
		boolean newState = !controller.getDataModel().isShowGrid();
		controller.setShowGrid(newState);
		if(item != null){
			item.setSelected(newState);
		}
	}
}
