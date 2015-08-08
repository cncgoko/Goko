package org.goko.tools.viewer.jogl.service;

import javax.media.opengl.GL2;
import javax.media.opengl.GL3;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.viewer.renderer.IRendererProxy;

public class JoglRendererProxy implements IRendererProxy {
	private GL3 gl;


	public JoglRendererProxy(GL3 gl){
		this.gl = gl;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawPoint(org.goko.core.gcode.bean.Tuple6b)
	 */
	@Override
	public void drawPoint(Tuple6b point) throws GkException {
		JoglUtils.drawPoint(getGl2(), point);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawPoint(org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f)
	 */
	@Override
	public void drawPoint(Tuple6b point, Point3f color) throws GkException {
		JoglUtils.drawPoint(getGl2(), point, color);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawPoint(org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, int)
	 */
	@Override
	public void drawPoint(Tuple6b point, Point3f color, int style) throws GkException {
		JoglUtils.drawPoint(getGl2(), point, color, style);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawSegment(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b)
	 */
	@Override
	public void drawSegment(Tuple6b start, Tuple6b end) throws GkException {
		JoglUtils.drawSegment(getGl2(), start, end);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawSegment(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f)
	 */
	@Override
	public void drawSegment(Tuple6b start, Tuple6b end, Point3f color) throws GkException {
		JoglUtils.drawSegment(getGl2(), start, end, color);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawSegment(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, int)
	 */
	@Override
	public void drawSegment(Tuple6b start, Tuple6b end, Point3f color, int style) throws GkException {
		JoglUtils.drawSegment(getGl2(), start, end, color, style);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawArc(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f)
	 */
	@Override
	public void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Vector3f plane, int direction) throws GkException {
		JoglUtils.drawArc(getGl2(), start, end, center, plane, direction);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawArc(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, int, javax.vecmath.Point3f)
	 */
	@Override
	public void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Vector3f plane, int direction, Point3f color) throws GkException {
		JoglUtils.drawArc(getGl2(), start, end, center, plane, direction, color);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawArc(org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, int, javax.vecmath.Point3f, int)
	 */
	@Override
	public void drawArc(Tuple6b start, Tuple6b end, Tuple6b center, Vector3f plane, int direction, Point3f color, int style) throws GkException {
		JoglUtils.drawArc(getGl2(), start, end, center, plane, direction, color, style);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawLineStrip(org.goko.core.gcode.bean.Tuple6b[])
	 */
	@Override
	public void drawLineStrip(Tuple6b... points) throws GkException {
		JoglUtils.drawLineStrip(getGl2(), points);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawLineStrip(javax.vecmath.Point3f, org.goko.core.gcode.bean.Tuple6b[])
	 */
	@Override
	public void drawLineStrip(Point3f color, Tuple6b... points) throws GkException {
		JoglUtils.drawLineStrip(getGl2(), color, points);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#drawLineStrip(javax.vecmath.Point3f, int, org.goko.core.gcode.bean.Tuple6b[])
	 */
	@Override
	public void drawLineStrip(Point3f color, int style, Tuple6b... points) throws GkException {
		JoglUtils.drawLineStrip(getGl2(), color, style, points);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IRendererProxy#drawXYZAxis(org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, javax.vecmath.Point3f, javax.vecmath.Point3f, double)
	 */
	@Override
	public void drawXYZAxis( Tuple6b position, Point3f xColor, Point3f yColor, Point3f zColor, double scale) throws GkException{

	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IRendererProxy#drawXYZAxis(org.goko.core.gcode.bean.Tuple6b, javax.vecmath.Point3f, javax.vecmath.Point3f, javax.vecmath.Point3f, double, java.lang.String, double)
	 */
	@Override
	public void drawXYZAxis(Tuple6b position, Point3f xColor, Point3f yColor, Point3f zColor, double scale, String label, double charwidth) throws GkException {
		JoglUtils.drawXYZAxis(getGl2(), position, xColor, yColor, zColor, scale, label, charwidth);
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
	public GL3 getGl() {
		return gl;
	}

	public GL2 getGl2() {
		return gl.getGL2();
	}

	/**
	 * @param gl the gl to set
	 */
	public void setGl(GL3 gl) {
		this.gl = gl;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IRendererProxy#drawCircle(org.goko.core.gcode.bean.Tuple6b, double, javax.vecmath.Vector3f, javax.vecmath.Point3f)
	 */
	@Override
	public void drawCircle(Tuple6b center, double radius, Vector3f plane, Point3f color) throws GkException {
		JoglUtils.drawCircle(getGl2(), center, radius, plane, color);
	}

}
