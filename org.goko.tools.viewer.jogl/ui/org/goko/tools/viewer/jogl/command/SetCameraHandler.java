package org.goko.tools.viewer.jogl.command;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.service.IJoglViewerService;

public class SetCameraHandler {

	@Execute
	public void execute(@Named("jogl.camera.id")String idCamera, IJoglViewerService joglViewer) throws GkException{
		joglViewer.setActiveCamera(idCamera);
	}
}
