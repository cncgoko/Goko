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
//		if(grblService != null){
//			EnumCoordinateSystem coordinateSystem = grblService.getCurrentGCodeContext().getCoordinateSystem();
//			Tuple6b offsets = grblService.getGrblState().getOffset(coordinateSystem);
//			Tuple6b machineOrigin = new Tuple6b().setZero().subtract(new Tuple6b(offsets));
//
//			for (EnumCoordinateSystem cs : EnumCoordinateSystem.values()) {
//				Tuple6b csOffset = grblService.getGrblState().getOffset(cs);
//				if(csOffset != null){
//					csOffset = csOffset.add(machineOrigin);
//					Point3f color = new Point3f(0.4f,0.4f,0.4f);
//					if(cs == coordinateSystem){
//						color = new Point3f(1f,1f,0f);
//					}
//					proxy.drawXYZAxis(csOffset, color, color, color, 3, cs.toString(), 1.5);
//				}
//			}
//			Tuple6b g54offset = grblService.getGrblState().getG54Offset();
//			proxy.drawXYZAxis(g54offset, new Point3f(0.8f,0.8f,0.8f), new Point3f(0.8f,0.8f,0.8f), new Point3f(0.8f,0.8f,0.8f), 3);
//			Tuple6b g55offset = grblService.getGrblState().getG54Offset();
//			proxy.drawPoint(g55offset);
//			Tuple6b g56offset = grblService.getGrblState().getG54Offset();
//			proxy.drawPoint(g56offset);
//			Tuple6b g57offset = grblService.getGrblState().getG54Offset();
//			proxy.drawPoint(g57offset);
		//}
	}

	public void setGrblRendererService(IGrblControllerService grblService) {
		this.grblService = grblService;
	}

}
