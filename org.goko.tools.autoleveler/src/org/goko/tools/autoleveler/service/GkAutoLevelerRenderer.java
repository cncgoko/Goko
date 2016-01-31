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
package org.goko.tools.autoleveler.service;

import javax.vecmath.Point3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.math.Tuple6b;
import org.goko.core.viewer.renderer.AbstractViewer3DRenderer;
import org.goko.core.viewer.renderer.IRendererProxy;
import org.goko.tools.autoleveler.bean.IAxisElevationPattern;

public class GkAutoLevelerRenderer extends AbstractViewer3DRenderer{
	private String id;
	private IAxisElevationPattern pattern;
	private GridElevationMap elevationMap;
	private double minZ;
	private double maxZ;
	private double dz;

	public GkAutoLevelerRenderer(String id) {
		this.id = id;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#render(org.goko.core.viewer.renderer.IRendererProxy)
	 */
	@Override
	public void render(IRendererProxy proxy) throws GkException {
		if(pattern != null){
			for (Tuple6b position : pattern.getPatternPositions()) {
				proxy.drawPoint(position);
			}
		}
		if(elevationMap != null){
			for (Tuple6b position : elevationMap.getProbedPositions()) {
				double z = position.getZ().doubleValue();
				if(z > maxZ){
					maxZ = z;
				}
				if(z < minZ){
					minZ = z;
				}
				dz = maxZ - minZ;
			}
		}
		if(elevationMap != null){
			for (Tuple6b position : elevationMap.getProbedPositions()) {
				proxy.drawPoint(position, getInterpolatedZColor(position));
			}
		}
	}

	protected Point3f getInterpolatedZColor(Tuple6b position){
		float dr = (float) ((position.getZ().doubleValue() - minZ) / dz);
		return new Point3f(dr,1-dr,0);
	}
	/**
	 * @return the pattern
	 */
	public IAxisElevationPattern getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(IAxisElevationPattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the elevationMap
	 */
	public GridElevationMap getElevationMap() {
		return elevationMap;
	}

	/**
	 * @param elevationMap the elevationMap to set
	 */
	public void setElevationMap(GridElevationMap elevationMap) {
		this.elevationMap = elevationMap;
	}

}
