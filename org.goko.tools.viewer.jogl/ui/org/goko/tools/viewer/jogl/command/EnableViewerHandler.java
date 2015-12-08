/**
 *
 */
package org.goko.tools.viewer.jogl.command;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.goko.tools.viewer.jogl.model.GCodeViewer3DController;


public class EnableViewerHandler {

	@Execute
	public void execute(IEclipseContext context, GCodeViewer3DController controller, ECommandService commandeService){
		controller.setRenderEnabled(!controller.getDataModel().isEnabled());

	}
}
