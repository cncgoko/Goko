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
package org.goko.tools.viewer.jogl.service;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.viewer.service.IViewer3DService;
import org.goko.tools.viewer.jogl.GokoJoglCanvas;
import org.goko.tools.viewer.jogl.camera.AbstractCamera;
import org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer;

/**
 * Interface of the Goko Jogl viewer service
 *
 * @author PsyKo
 *
 */
public interface IJoglViewerService extends IViewer3DService{

	public GokoJoglCanvas createCanvas(Composite parent) throws GkException;

	public List<AbstractCamera> getSupportedCamera() throws GkException;

	public void addCamera(AbstractCamera camera) throws GkException;

	public void setActiveCamera(String idCamera) throws GkException;

	public AbstractCamera getActiveCamera() throws GkException;

	public boolean isLockCameraOnTool() throws GkException;

	public void setLockCameraOnTool(boolean lockOnTool) throws GkException;

	public void setEnabled(boolean enabled);

	public boolean isEnabled();

	public ICoreJoglRenderer getJoglRenderer(String name) throws GkException;

	void addRenderer(ICoreJoglRenderer renderer) throws GkException;

	void removeRenderer(ICoreJoglRenderer renderer) throws GkException;

	void setRendererEnabled(String idRenderer, boolean enabled) throws GkException;

	public void zoomToFit() throws GkException;

	public void setLayerVisible(int layerId, boolean visible);
	
	public boolean isLayerVisible(int layerId);
	
	void addOverlayRenderer(IOverlayRenderer overlayRenderer) throws GkException;
	
	void removeOverlayRenderer(IOverlayRenderer overlayRenderer) throws GkException;
}
