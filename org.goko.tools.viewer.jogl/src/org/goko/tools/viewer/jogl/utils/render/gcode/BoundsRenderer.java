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
package org.goko.tools.viewer.jogl.utils.render.gcode;

import javax.media.opengl.GL3;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.BoundingTuple6b;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.service.Layer;
import org.goko.tools.viewer.jogl.utils.render.coordinate.measurement.DistanceRenderer;
import org.goko.tools.viewer.jogl.utils.render.text.TextRenderer;

public class BoundsRenderer extends AbstractCoreJoglMultipleRenderer{
	public static final String ID = "org.goko.viewer.jogl.boundsRenderer";
	private BoundingTuple6b bounds;

	/**
	 * Constructor
	 * @param bounds the bound to display
	 */
	public BoundsRenderer(BoundingTuple6b bounds) {
		this.setBounds(bounds);
		this.setLayerId(Layer.LAYER_BOUNDS);
	}

	@Override
	public String getId() {
		return ID;
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		if(bounds != null && bounds.getMin() != null && bounds.getMax() != null){
			Point3d ptMin = bounds.getMin().to(JoglUtils.JOGL_UNIT).toPoint3d();
			Point3d ptMax = bounds.getMax().to(JoglUtils.JOGL_UNIT).toPoint3d();

			addRenderer( new DistanceRenderer(new Point3d((float)ptMin.x,(float)ptMin.y-1,(float)ptMin.z), new Point3d((float)ptMax.x,(float)ptMin.y-1,(float)ptMin.z), new Vector3d(0,0,1), TextRenderer.TOP) );
			addRenderer( new DistanceRenderer(new Point3d((float)ptMin.x-1,(float)ptMax.y,(float)ptMin.z), new Point3d((float)ptMin.x-1,(float)ptMin.y,(float)ptMin.z), new Vector3d(0,0,1), TextRenderer.TOP) );
			addRenderer( new DistanceRenderer(new Point3d((float)ptMin.x-1,(float)ptMin.y-1,(float)ptMin.z), new Point3d((float)ptMin.x-1,(float)ptMin.y-1,(float)ptMax.z), new Vector3d(1,-1,0), TextRenderer.BOTTOM) );
		}
	}

	/**
	 * @return the bounds
	 */
	protected BoundingTuple6b getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	protected void setBounds(BoundingTuple6b bounds) {
		this.bounds = bounds;
	}


}
