package org.goko.core.viewer.renderer;

import javax.vecmath.Point3f;

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

	void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Point3f plane, int direction) throws GkException;

	void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Point3f plane, int direction, Point3f color) throws GkException;

	void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Point3f plane, int direction, Point3f color, int style) throws GkException;

	void drawLineStrip(Tuple6b... points) throws GkException;

	void drawLineStrip(Point3f color, Tuple6b... points) throws GkException;

	void drawLineStrip(Point3f color, int style, Tuple6b... points) throws GkException;

	void erase(Integer id) throws GkException;
}
