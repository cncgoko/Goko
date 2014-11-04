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
package org.goko.grbl.controller;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.viewer.renderer.AbstractViewer3DRenderer;
import org.goko.core.viewer.renderer.IRendererProxy;

public class GrblRendererService extends AbstractViewer3DRenderer {
	/** Id of the renderer */
	private static final String GRBL_RENDERER_ID = "org.goko.grbl.renderer";
	/** The GrblService to render */
	private IGrblControllerService grblService;

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return GRBL_RENDERER_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#render(org.goko.core.viewer.renderer.IRendererProxy)
	 */
	@Override
	public void render(IRendererProxy proxy) throws GkException {
		if(grblService != null){
			Tuple6b g54offset = grblService.getGrblState().getG54Offset();
			proxy.drawPoint(g54offset);
		}
	}

	public void setGrblRendererService(IGrblControllerService grblService) {
		this.grblService = grblService;
	}

}
