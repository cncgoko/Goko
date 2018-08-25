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
package org.goko.tools.viewer.jogl.utils.render.coordinate;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.utils.render.text.v2.TextRenderer;

import com.jogamp.opengl.GL3;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
public class CoordinateSystemRenderer extends AbstractCoreJoglMultipleRenderer{	
	/**
	 * Constructor
	 * @param enumCoordinateSystem the coordinate system enum
	 * @param scale the scale
	 * @param colorX the X axis color
	 * @param colorY the Y axis color
	 * @param colorZ the Z axis color
	 */
	public CoordinateSystemRenderer(ICoordinateSystem enumCoordinateSystem, float scale, Color3f colorX, Color3f colorY, Color3f colorZ, Color3f textColor, double textSize) {
		super();
		addRenderer(new ThreeAxisRenderer(scale, colorX, colorY, colorZ));
		TextRenderer textRenderer = new TextRenderer(enumCoordinateSystem.getCode(), textSize, new Point3d(), TextRenderer.BOTTOM | TextRenderer.LEFT);
		textRenderer.setColor(textColor.x, textColor.y, textColor.z, 1);
		textRenderer.setPadding(Length.valueOf("0.2", JoglUtils.JOGL_UNIT));
		addRenderer( textRenderer );
	}	
	
	public void createActiveRenderer(){
		
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		// TODO Auto-generated method stub

	}

}