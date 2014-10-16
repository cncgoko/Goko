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
package org.goko.viewer.jogl.utils.render.gcode;

import java.math.BigDecimal;

import javax.media.opengl.GL2;
import javax.vecmath.Point3f;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.BoundingTuple6b;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.viewer.jogl.service.JoglRendererProxy;
import org.goko.viewer.jogl.utils.render.IJoglRenderer;

import com.jogamp.opengl.util.gl2.GLUT;

public class BoundsRenderer implements IJoglRenderer{
	private static Point3f COLOR = new Point3f(0.80f,0.80f,0.80f);
	private BoundingTuple6b bounds;
	private float offset = 5;

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#getId()
	 */
	@Override
	public String getId() {
		return String.valueOf(bounds);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#render(org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void render(JoglRendererProxy proxy) throws GkException {
		proxy.getGl().glPushAttrib(GL2.GL_LINE_BIT);
		proxy.getGl().glLineWidth(1.5f);
		// X Ruler
		Tuple6b xStart = new Tuple6b(bounds.getMin().getX(), bounds.getMin().getY().add(new BigDecimal(-offset)), new BigDecimal("0.01"));
		Tuple6b xEnd   = new Tuple6b(bounds.getMax().getX(), bounds.getMin().getY().add(new BigDecimal(-offset)), new BigDecimal("0.01"));
		proxy.drawSegment(xStart, xEnd,COLOR);
		xStart.setX(bounds.getMin().getX());
		xStart.setY(bounds.getMin().getY().add( new BigDecimal( -offset / 2)));
		xEnd.setX(bounds.getMin().getX());
		xEnd.setY(bounds.getMin().getY().add( new BigDecimal( -offset - (offset / 2))));
		proxy.drawSegment(xStart, xEnd,COLOR);
		xStart.setX(bounds.getMax().getX());
		xEnd.setX(bounds.getMax().getX());
		proxy.drawSegment(xStart, xEnd,COLOR);
		BigDecimal dx = bounds.getMax().subtract(bounds.getMin()).getX().setScale(3, BigDecimal.ROUND_HALF_UP);

		// Y Ruler
		Tuple6b yStart = new Tuple6b(bounds.getMin().getX().add(new BigDecimal(-offset)), bounds.getMin().getY(), new BigDecimal("0.01"));
		Tuple6b yEnd   = new Tuple6b(bounds.getMin().getX().add(new BigDecimal(-offset)), bounds.getMax().getY(), new BigDecimal("0.01"));
		proxy.drawSegment(yStart, yEnd,COLOR);

		BigDecimal dy = bounds.getMax().subtract(bounds.getMin()).getY().setScale(3,BigDecimal.ROUND_HALF_UP);

		xStart.setY(bounds.getMin().getY());
		xStart.setX(bounds.getMin().getX().add( new BigDecimal( -offset / 2)));
		xEnd.setY(bounds.getMin().getY());
		xEnd.setX(bounds.getMin().getX().add( new BigDecimal( -offset - offset / 2)));
		proxy.drawSegment(xStart, xEnd,COLOR);
		xStart.setY(bounds.getMax().getY());
		xEnd.setY(bounds.getMax().getY());
		proxy.drawSegment(xStart, xEnd,COLOR);

		proxy.getGl().glLineWidth(1.1f);

		// Let's draw the measurements
		GLUT glut = new GLUT();
		double charWidth = 4.0;
		double charHeight = 6.0;
		double scale = charWidth / glut.glutStrokeWidth(GLUT.STROKE_MONO_ROMAN, ' ');
		// X axis measurement
		proxy.getGl().glPushMatrix();
		proxy.getGl(). glScaled(scale,scale,scale);
		int nbChar = StringUtils.length(dx.toString());
		proxy.getGl(). glTranslated(((bounds.getMin().getX().add(bounds.getMax().getX()).doubleValue()/2)-(nbChar*charWidth/2))/scale,
									(bounds.getMin().getY().doubleValue() - offset - charHeight)/scale,
									0);
		glut.glutStrokeString(GLUT.STROKE_MONO_ROMAN, dx.toString());
        proxy.getGl().glPopMatrix();

        // Y axis measurement
		proxy.getGl().glPushMatrix();
		proxy.getGl().glRotated(-90, 0, 0, 1);
		proxy.getGl(). glScaled(scale,scale,scale);
		nbChar = StringUtils.length(dy.toString());
		proxy.getGl(). glTranslated(((-bounds.getMin().getY().add(bounds.getMax().getY()).doubleValue()/2)-(nbChar*charWidth/2))/scale,
									(bounds.getMin().getX().doubleValue() - offset - charHeight)/scale,
									0);
		glut.glutStrokeString(GLUT.STROKE_MONO_ROMAN, dy.toString());
        proxy.getGl().glPopMatrix();

		proxy.getGl().glPopAttrib();
	}

	/**
	 * @return the bounds
	 */
	public BoundingTuple6b getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(BoundingTuple6b bounds) {
		this.bounds = bounds;
	}

}
