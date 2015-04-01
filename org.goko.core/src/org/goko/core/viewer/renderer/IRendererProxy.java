/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.viewer.renderer;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;

public interface IRendererProxy {
	public static final int ARC_CLOCKWISE = 0x01;
	public static final int ARC_COUNTERCLOCKWISE = 0x02;

	void drawGCode(IGCodeProvider gcodeProvider) throws GkException;

	void drawPoint(Tuple6b point) throws GkException;

	void drawPoint(Tuple6b point, Point3f color) throws GkException;

	void drawPoint(Tuple6b point, Point3f color, int style) throws GkException;

	void drawSegment(Tuple6b start, Tuple6b end) throws GkException;

	void drawSegment(Tuple6b start, Tuple6b end, Point3f color) throws GkException;

	void drawSegment(Tuple6b start, Tuple6b end, Point3f color, int style) throws GkException;

	void drawCircle(Tuple6b center, double radius, Vector3f plane, Point3f color) throws GkException;

	void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Vector3f plane, int direction) throws GkException;

	void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Vector3f plane, int direction, Point3f color) throws GkException;

	void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Vector3f plane, int direction, Point3f color, int style) throws GkException;

	void drawLineStrip(Tuple6b... points) throws GkException;

	void drawLineStrip(Point3f color, Tuple6b... points) throws GkException;

	void drawLineStrip(Point3f color, int style, Tuple6b... points) throws GkException;

	void drawXYZAxis(Tuple6b position, Point3f xColor, Point3f yColor, Point3f zColor, double scale) throws GkException;

	void drawXYZAxis(Tuple6b position, Point3f xColor, Point3f yColor, Point3f zColor, double scale, String label, double charwidth) throws GkException;

	void erase(Integer id) throws GkException;
}
