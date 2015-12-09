package org.goko.tools.viewer.jogl.command;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.model.GCodeViewer3DController;

/**
 * Handler for the ToolItem of the GCode viewer part allowing to zoom/unzoom to fit the scene
 *
 * @author Psyko
 */
public class ZoomToFitHandler {

	@Execute
	public void execute(IEclipseContext context, GCodeViewer3DController controller) throws GkException{
		controller.zoomToFit();
	}
}
