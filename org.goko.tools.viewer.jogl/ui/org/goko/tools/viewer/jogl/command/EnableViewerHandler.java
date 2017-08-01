/**
 *
 */
package org.goko.tools.viewer.jogl.command;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.goko.tools.viewer.jogl.model.GCodeViewer3DController;

/**
 * Handler for the ToolItem of the GCode viewer part allowing to enable/disable the renderer
 *
 * @author Psyko
 */
public class EnableViewerHandler {

	@CanExecute
	public boolean canExecute(@Optional MHandledToolItem item, GCodeViewer3DController controller){
		updateIcon(item, controller.getDataModel().isEnabled());
		return true;
	}

	@Execute
	public void execute(@Optional MHandledToolItem item, IEclipseContext context, GCodeViewer3DController controller){
		boolean newState = !controller.getDataModel().isEnabled();
		controller.setRenderEnabled(newState);
		updateIcon(item, newState);
	}

	private void updateIcon(MHandledToolItem item, boolean state){
		if(item != null){
			if(state){
				item.setIconURI("platform:/plugin/org.goko.tools.viewer.jogl/resources/icons/activated.png");
			}else{
				item.setIconURI("platform:/plugin/org.goko.tools.viewer.jogl/resources/icons/deactivated.png");
			}
		}
	}
}
