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
package org.goko.tools.viewer.jogl.utils.render.basic;

import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.math.BoundingTuple6b;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.service.Layer;
import org.goko.tools.viewer.jogl.utils.render.coordinate.measurement.DistanceRenderer;
import org.goko.tools.viewer.jogl.utils.render.text.v2.TextRenderer;

import com.jogamp.opengl.GL3;

public class BoundsRenderer extends AbstractCoreJoglMultipleRenderer{
	public static final String CODE = "org.goko.viewer.jogl.boundsRenderer";
	private BoundingTuple6b renderedBounds;

	/**
	 * Constructor
	 * @param bounds the bound to display
	 */
	public BoundsRenderer(BoundingTuple6b bounds) {
		this.setRenderedBounds(bounds);
		this.setLayerId(Layer.LAYER_BOUNDS);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#getCode()
	 */
	@Override
	public String getCode() {
		return CODE;
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		if(renderedBounds != null && renderedBounds.getMin() != null && renderedBounds.getMax() != null){
			Tuple6b ptMin = renderedBounds.getMin();
			Tuple6b ptMax = renderedBounds.getMax();
			Length offset = Length.valueOf(1, JoglUtils.JOGL_UNIT);
			
			Unit<Length> u = JoglUtils.JOGL_UNIT;
			addRenderer( new DistanceRenderer(new Tuple6b(ptMin.getX(), ptMin.getY().subtract(offset), ptMin.getZ()), new Tuple6b( ptMax.getX(), ptMin.getY().subtract(offset), ptMin.getZ()), new Vector3d(0,0,1), TextRenderer.TOP) );
			addRenderer( new DistanceRenderer(new Tuple6b(ptMin.getX().subtract(offset), ptMax.getY(), ptMin.getZ()), new Tuple6b( ptMin.getX().subtract(offset), ptMin.getY(), ptMin.getZ()), new Vector3d(0,0,1), TextRenderer.TOP) );
			addRenderer( new DistanceRenderer(new Tuple6b(ptMin.getX().subtract(offset), ptMin.getY().subtract(offset), ptMin.getZ()), new Tuple6b(ptMin.getX().subtract(offset), ptMin.getY().subtract(offset), ptMax.getZ()), new Vector3d(1,-1,0), TextRenderer.BOTTOM) );
		}
	}

	/**
	 * @return the renderedBounds
	 */
	public BoundingTuple6b getRenderedBounds() {
		return renderedBounds;
	}

	/**
	 * @param renderedBounds the renderedBounds to set
	 */
	public void setRenderedBounds(BoundingTuple6b renderedBounds) {
		this.renderedBounds = renderedBounds;
	}

}
