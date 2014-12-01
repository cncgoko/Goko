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
package org.goko.viewer.jogl.utils.render;

import javax.vecmath.Point3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.viewer.jogl.service.JoglRendererProxy;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
public class CoordinateSystemRenderer implements IJoglRenderer {
	public static final String ID = "org.goko.viewer.jogl.utils.render.CoordinateSystemRenderer";
	private ICoordinateSystemAdapter adapter;

	public CoordinateSystemRenderer() {
	}

	/**
	 * (inheritDoc)
	 *
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/**
	 * (inheritDoc)
	 *
	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#render(org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void render(JoglRendererProxy proxy) throws GkException {
		if(adapter != null){
			EnumCoordinateSystem coordinateSystem = adapter.getCurrentCoordinateSystem();
			Tuple6b offsets = adapter.getCoordinateSystemOffset(coordinateSystem);
			Tuple6b machineOrigin = new Tuple6b().setZero().subtract(new Tuple6b(offsets));

			for (EnumCoordinateSystem cs : adapter.getCoordinateSystem()) {
				Tuple6b csOffset = adapter.getCoordinateSystemOffset(cs);
				if(csOffset != null){
					csOffset = csOffset.add(machineOrigin);
					Point3f color = new Point3f(0.4f,0.4f,0.4f);
					if(cs == coordinateSystem){
						color = new Point3f(1f,1f,0f);
					}
					proxy.drawXYZAxis(csOffset, color, color, color, 3, cs.toString(), 1.5);
				}
			}
		}
	}

	/**
	 * @return the adapter
	 */
	public ICoordinateSystemAdapter getAdapter() {
		return adapter;
	}

	/**
	 * @param adapter the adapter to set
	 */
	public void setAdapter(ICoordinateSystemAdapter adapter) {
		this.adapter = adapter;
	}

}
