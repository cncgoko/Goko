package org.goko.viewer.jogl.service;

import javax.media.opengl.GL2;
import javax.vecmath.Point3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.viewer.renderer.IRendererProxy;

public class JoglRendererProxy implements IRendererProxy {

	private GL2 gl;

	public JoglRendererProxy(GL2 gl){
		this.gl = gl;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawPoint(org.goko.core.gcode.bean.Tuple6b)
	 */
	@Override
	public void drawPoint(Tuple6b point) throws GkException {
		JoglUtils.drawPoint(gl, point);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawPoint(org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f)
	 */
	@Override
	public void drawPoint(Tuple6b point, Point3f color) throws GkException {
		JoglUtils.drawPoint(gl, point, color);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawPoint(org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, int)
	 */
	@Override
	public void drawPoint(Tuple6b point, Point3f color, int style) throws GkException {
		JoglUtils.drawPoint(gl, point, color, style);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawSegment(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b)
	 */
	@Override
	public void drawSegment(Tuple6b start, Tuple6b end) throws GkException {
		JoglUtils.drawSegment(gl, start, end);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawSegment(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f)
	 */
	@Override
	public void drawSegment(Tuple6b start, Tuple6b end, Point3f color) throws GkException {
		JoglUtils.drawSegment(gl, start, end, color);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawSegment(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, int)
	 */
	@Override
	public void drawSegment(Tuple6b start, Tuple6b end, Point3f color, int style) throws GkException {
		JoglUtils.drawSegment(gl, start, end, color, style);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawArc(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f)
	 */
	@Override
	public void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Point3f plane, int direction) throws GkException {
		JoglUtils.drawArc(gl, start, end, center, plane, direction);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawArc(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, int, javax.vecmath.Point3f)
	 */
	@Override
	public void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Point3f plane, int direction, Point3f color) throws GkException {
		JoglUtils.drawArc(gl, start, end, center, plane, direction, color);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawArc(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, int, javax.vecmath.Point3f, int)
	 */
	@Override
	public void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Point3f plane, int direction, Point3f color, int style) throws GkException {
		JoglUtils.drawArc(gl, start, end, center, plane, direction, color, style);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawLineStrip(org.goko.core.gcode.bean.Tuple6b[])
	 */
	@Override
	public void drawLineStrip(Tuple6b... points) throws GkException {
		JoglUtils.drawLineStrip(gl, points);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawLineStrip(javax.vecmath.Point3f, org.goko.core.gcode.bean.Tuple6b[])
	 */
	@Override
	public void drawLineStrip(Point3f color, Tuple6b... points) throws GkException {
		JoglUtils.drawLineStrip(gl, color, points);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawLineStrip(javax.vecmath.Point3f, int, org.goko.core.gcode.bean.Tuple6b[])
	 */
	@Override
	public void drawLineStrip(Point3f color, int style, Tuple6b... points) throws GkException {
		JoglUtils.drawLineStrip(gl, color, style, points);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#erase(java.lang.Integer)
	 */
	@Override
	public void erase(Integer id) throws GkException {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawGCode(IGCodeProvider gcodeProvider) throws GkException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the gl
	 */
	public GL2 getGl() {
		return gl;
	}

	/**
	 * @param gl the gl to set
	 */
	public void setGl(GL2 gl) {
		this.gl = gl;
	}

}
