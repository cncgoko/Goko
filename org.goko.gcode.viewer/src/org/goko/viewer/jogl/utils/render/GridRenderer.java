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

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Point4d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.viewer.jogl.service.JoglUtils;
import org.goko.viewer.jogl.service.JoglViewerSettings;
import org.goko.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.viewer.jogl.shaders.ShaderLoader;
import org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
public class GridRenderer extends AbstractVboJoglRenderer {
	public static final String ID = "org.goko.viewer.jogl.utils.render.GridRenderer";
	private double size = 100;
	private double majorUnit = 10;
	private double minorUnit = 1;
	private Point4d majorUnitColor	= new Point4d(0.4, 0.4, 0.4, 0.45);
	private Point4d minorUnitColor	= new Point4d(0.4, 0.4, 0.4, 0.15);
	private Point4d originColor		= new Point4d(0.8, 0.8, 0.8, 0.7);

	/**
	 * Constructor
	 */
	public GridRenderer() {
		super(GL.GL_LINES, COLORS | VERTICES);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	private void buildGrid() throws GkException{
		Unit<Length> unit = GokoPreference.getInstance().getLengthUnit();
		Quantity<Length> majorQuantity = NumberQuantity.of(JoglViewerSettings.getInstance().getMajorGridSpacing().doubleValue(), unit);
		Quantity<Length> minorQuantity = NumberQuantity.of(JoglViewerSettings.getInstance().getMinorGridSpacing().doubleValue(), unit);
		majorUnit = majorQuantity.to(SIPrefix.MILLI(SI.METRE)).doubleValue();
		minorUnit = minorQuantity.to(SIPrefix.MILLI(SI.METRE)).doubleValue();
		size = 10 * majorUnit;
		List<Point4d> lstVertices = new ArrayList<Point4d>();
		List<Point4d> lstColors = new ArrayList<Point4d>();

		// Origin
		lstVertices.add(new Point4d(0, -size, 0,1));
		lstVertices.add(new Point4d(0,  size, 0,1));
		lstVertices.add(new Point4d(-size, 0, 0,1));
		lstVertices.add(new Point4d( size, 0, 0,1));
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);

		// Main divisions
		for (double i = -size; i <= size; i += majorUnit) {
			lstVertices.add(new Point4d(i, -size, 0,1));
			lstVertices.add(new Point4d(i, size, 0,1));
			lstVertices.add(new Point4d(-size, i, 0,1));
			lstVertices.add(new Point4d(size, i, 0,1));
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
		}

		// Subdivisions
		for (double i = -size; i <= size; i+=minorUnit) {
			if (i != 0 && Math.abs(i % majorUnit) >= 0.01) {
				lstVertices.add(new Point4d(i, -size, 0,1));
				lstVertices.add(new Point4d(i, size, 0,1));
				lstVertices.add(new Point4d(-size, i, 0,1));
				lstVertices.add(new Point4d(size, i, 0,1));
				lstColors.add(minorUnitColor);
				lstColors.add(minorUnitColor);
				lstColors.add(minorUnitColor);
				lstColors.add(minorUnitColor);
			}
		}
		setVerticesCount(CollectionUtils.size(lstVertices));
		setColorsBuffer(JoglUtils.buildFloatBuffer4d(lstColors));
		setVerticesBuffer(JoglUtils.buildFloatBuffer4d(lstVertices));
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		buildGrid();
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.LINE_SHADER);
	}

}
