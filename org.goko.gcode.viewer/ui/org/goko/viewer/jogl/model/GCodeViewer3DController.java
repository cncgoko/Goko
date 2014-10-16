/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.viewer.jogl.model;

import javax.inject.Inject;

import org.goko.common.bindings.AbstractController;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.viewer.jogl.camera.OrthographicCamera;
import org.goko.viewer.jogl.camera.PerspectiveCamera;
import org.goko.viewer.jogl.service.IJoglViewerService;
import org.goko.viewer.jogl.utils.render.GridRenderer;

/**
 * GCode 3D Viewer controller
 * @author PsyKo
 *
 */
public class GCodeViewer3DController extends AbstractController<GCodeViewer3DModel> {
	@Inject
	private IControllerService controllerService;
	@Inject
	private IJoglViewerService viewerService;

	public GCodeViewer3DController(GCodeViewer3DModel binding) {
		super(binding);
	}

	@Override
	public void initialize() throws GkException {
		controllerService.addListener(this);
	}

	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineValueUpdate(MachineValueUpdateEvent updateEvent) throws GkException{
		getDataModel().setCurrentPosition(controllerService.getPosition());
	}

	public void setPerspectiveCamera(){
		try {
			viewerService.setActiveCamera(PerspectiveCamera.ID);
		} catch (GkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setOrthographicCamera(){
		try {
			viewerService.setActiveCamera(OrthographicCamera.ID);
		} catch (GkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setGCodeFile(IGCodeProvider provider) throws GkException {
		viewerService.renderGCode(provider);
	}

	public void setLockCameraOnTool(boolean lockOnTool) {
		try {
			viewerService.setLockCameraOnTool(lockOnTool);
		} catch (GkException e) {
			notifyException(e);
		}
	}

	public void setShowGrid(boolean showGrid) {
		try {
			viewerService.setRendererEnabled(GridRenderer.ID, showGrid);
		} catch (GkException e) {
			notifyException(e);
		}
	}

	public void setRenderEnabled(boolean enabled){
		viewerService.setEnabled(enabled);
	}
}
