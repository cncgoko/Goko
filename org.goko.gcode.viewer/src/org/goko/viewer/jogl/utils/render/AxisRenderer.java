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

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
public class AxisRenderer implements IJoglRenderer {
	protected static final String ID = "org.goko.viewer.jogl.utils.render.AxisRenderer";
	private static final double CHAR_WIDTH = 2.0;
	private Tuple6b zero;
	private Tuple6b xaxis;
	private Tuple6b yaxis;
	private Tuple6b zaxis;
	private Point3f xcolor;
	private Point3f ycolor;
	private Point3f zcolor;
	private double textScale;

	public AxisRenderer() {
		GLUT glut = new GLUT();
		textScale  = CHAR_WIDTH / glut.glutStrokeWidth(GLUT.STROKE_MONO_ROMAN, ' ');
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
	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#render(org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void render(JoglRendererProxy proxy) throws GkException {
		proxy.getGl().glLineWidth(1.5f);
		GLUT glut = new GLUT();

		proxy.getGl().glPushMatrix();
			proxy.drawSegment(zero, xaxis, xcolor);
			proxy.getGl().glTranslated(xaxis.getX().doubleValue(), xaxis.getY().doubleValue(), xaxis.getZ().doubleValue());
				proxy.getGl().glPushMatrix();
				proxy.getGl().glTranslated( CHAR_WIDTH,-1,0);
				proxy.getGl().glScaled(textScale,textScale,textScale);
				glut.glutStrokeString(GLUT.STROKE_MONO_ROMAN, "X");
				proxy.getGl().glPopMatrix();

			proxy.getGl().glRotated(90, 0, 1, 0);
			glut.glutSolidCone(0.5, 1, 8, 1);
		proxy.getGl().glPopMatrix();


		proxy.getGl().glPushMatrix();
		proxy.drawSegment(zero, yaxis, ycolor);
		proxy.getGl().glTranslated(yaxis.getX().doubleValue(), yaxis.getY().doubleValue(), yaxis.getZ().doubleValue());
			proxy.getGl().glPushMatrix();
			proxy.getGl().glTranslated( -1,CHAR_WIDTH,0);
			proxy.getGl().glScaled(textScale,textScale,textScale);
			glut.glutStrokeString(GLUT.STROKE_MONO_ROMAN, "Y");
			proxy.getGl().glPopMatrix();
		proxy.getGl().glRotated(-90, 1, 0, 0);
		glut.glutSolidCone(0.5, 1, 8, 1);
		proxy.getGl().glPopMatrix();

		proxy.getGl().glPushMatrix();
		proxy.drawSegment(zero, zaxis, zcolor);
		proxy.getGl().glTranslated(zaxis.getX().doubleValue(), zaxis.getY().doubleValue(), zaxis.getZ().doubleValue());
			proxy.getGl().glPushMatrix();
			proxy.getGl().glTranslated( -1,0,CHAR_WIDTH);
			proxy.getGl().glScaled(textScale,textScale,textScale);
			proxy.getGl().glRotated(90, 1, 0, 0);
			glut.glutStrokeString(GLUT.STROKE_MONO_ROMAN, "Z");
			proxy.getGl().glPopMatrix();
		glut.glutSolidCone(0.5, 1, 8, 1);
		proxy.getGl().glPopMatrix();

	}

}
