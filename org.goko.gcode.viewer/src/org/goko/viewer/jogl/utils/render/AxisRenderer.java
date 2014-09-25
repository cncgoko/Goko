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
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.viewer.jogl.service.JoglRendererProxy;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
public class AxisRenderer extends AbstractJoglRenderer {
	protected static final String ID = "org.goko.viewer.jogl.utils.render.AxisRenderer";
	private Tuple6b zero;
	private Tuple6b xaxis;
	private Tuple6b yaxis;
	private Tuple6b zaxis;
	private Point3f xcolor;
	private Point3f ycolor;
	private Point3f zcolor;

	public AxisRenderer() {
		zero   = new Tuple6b(0,0,0);
		xaxis  = new Tuple6b(10,0,0);
		yaxis  = new Tuple6b(0,10,0);
		zaxis  = new Tuple6b(0,0,10);
		xcolor = new Point3f(1,0,0);
		ycolor = new Point3f(0,1,0);
		zcolor = new Point3f(0,0,1);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.AbstractJoglRenderer#renderJogl(org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void renderJogl(JoglRendererProxy proxy) throws GkException {
		proxy.getGl().glLineWidth(1.5f);
		proxy.drawSegment(zero, xaxis, xcolor);
		proxy.drawSegment(zero, yaxis, ycolor);
		proxy.drawSegment(zero, zaxis, zcolor);
	}

}
