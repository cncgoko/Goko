package org.goko.viewer.jogl.service;

import java.nio.FloatBuffer;
import java.util.List;

import javax.media.opengl.GL2;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.viewer.renderer.IRendererProxy;

/**
 * Utility class for Jogl rendering
 *
 * @author Psyko
 *
 */
public class JoglUtils {
	/** Default Jogl unit (by convention)*/
	public static final Unit<Length> JOGL_UNIT = SI.MILLIMETRE;
	
	

	private static final Point3f DEFAULT_COLOR = new Point3f(0.8f,0.8f,0.8f);

	public static void drawPoint(GL2 gl,Tuple6b point) throws GkException {
		drawPoint(gl, point, DEFAULT_COLOR);
	}


	public static void drawPoint(GL2 gl,Tuple6b point, Point3f color) throws GkException {
		drawPoint(gl, point, color, 0);
	}


	public static void drawPoint(GL2 gl,Tuple6b point, Point3f color, int style) throws GkException {
		gl.glPushAttrib(GL2.GL_LINE_BIT);
		gl.glColor3d(color.x, color.y, color.z);
		Point3d startPoint 	= point.toPoint3d();
		gl.glLineWidth(1f);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3d(startPoint.x-0.5, startPoint.y, startPoint.z);
		gl.glVertex3d(startPoint.x+0.5, startPoint.y, startPoint.z);
		gl.glVertex3d(startPoint.x, startPoint.y-0.5, startPoint.z);
		gl.glVertex3d(startPoint.x, startPoint.y+0.5, startPoint.z);
		gl.glVertex3d(startPoint.x, startPoint.y, startPoint.z-0.5);
		gl.glVertex3d(startPoint.x, startPoint.y, startPoint.z+0.5);
		gl.glEnd();

		gl.glPopAttrib();
	}

	public static void drawSegment(GL2 gl, Tuple6b start, Tuple6b end) throws GkException {
		drawSegment(gl, start, end, DEFAULT_COLOR);
	}

	public static void drawSegment(GL2 gl, Tuple6b start, Tuple6b end, Point3f color) throws GkException {
		drawSegment(gl, start, end, color, 0);
	}


	public static void drawSegment(GL2 gl, Tuple6b start, Tuple6b end, Point3f color, int style) throws GkException {
		gl.glPushAttrib(GL2.GL_LINE_BIT);
		Point3d startPoint 	= start.toPoint3d();
		Point3d endPoint 	= end.toPoint3d();
		gl.glLineWidth(1f);
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(startPoint.x, startPoint.y, startPoint.z);
		gl.glVertex3d(endPoint.x, endPoint.y, endPoint.z);
		gl.glEnd();
		gl.glPopAttrib();
	}

	public static void drawCircle(GL2 gl, Tuple6b center, double radius, Vector3f plane, Point3f color) throws GkException {
		Point3d c 	= center.toPoint3d();
		gl.glLineWidth(1f);
		gl.glPushAttrib(GL2.GL_LINE_BIT);
		gl.glColor3f(color.x, color.y, color.z);
		gl.glBegin(GL2.GL_LINE_STRIP);

		// Adaptive points count
		int nbPoints = (int) Math.max(12, radius * 4);
		double deltaAngle = (Math.PI*2) / nbPoints;
		for(int i = 0; i <= nbPoints; i++){
			gl.glVertex3d(c.x + radius * Math.cos(i*deltaAngle), c.y + radius * Math.sin(i*deltaAngle), c.z);
		}

		gl.glEnd();
		gl.glPopAttrib();
	}
	public static void drawArc(GL2 gl, Tuple6b start, Tuple6b end, Tuple6b center, Vector3f plane, int direction) throws GkException {
		drawArc(gl, start, end, center, plane, direction,DEFAULT_COLOR,0);
	}


	public static void drawArc(GL2 gl, Tuple6b start, Tuple6b end, Tuple6b center, Vector3f plane, int direction, Point3f color) throws GkException {
		drawArc(gl, start, end, center, plane, direction,color,0);
	}

	public static void drawArc(GL2 gl, Tuple6b startPoint, Tuple6b endPoint, Tuple6b centerPoint, Vector3f plane, int direction, Point3f color, int style) throws GkException {
		boolean clockwise = direction == IRendererProxy.ARC_CLOCKWISE;
		plane.normalize();
		gl.glBegin(GL2.GL_LINE_STRIP);
		Point3d start 	= startPoint.toPoint3d();
		Point3d center 	= centerPoint.toPoint3d();
		Point3d end 	= endPoint.toPoint3d();

		Vector3d v1 = new Vector3d(start.x - center.x, start.y - center.y, start.z - center.z);
		Vector3d v2 = new Vector3d(end.x - center.x, end.y - center.y, end.z - center.z);

		gl.glColor3f(color.x, color.y, color.z);
		double smallestAngle = StrictMath.atan2(v1.y,v1.x) - StrictMath.atan2(v2.y,v2.x);
		double angle = smallestAngle ;
		// If smallestAngle < 0 then it is a counterclockwise angle.
		if(smallestAngle < 0){
			if(clockwise){ // The angle is CCW but the command is CCW
				angle = - ( 2*Math.PI - Math.abs(smallestAngle) );  // In OpenGl when rotating, CW rotation = negative angle
			}else{
				angle = Math.abs(smallestAngle); // In OpenGl when rotating, CCW rotation = positive angle
			}
		}else{
			if(clockwise){ // The angle is CW and we have a CW command
				angle = - Math.abs(smallestAngle); // In OpenGl when rotating, CW rotation = negative angle
			}else{ // The angle is CW but we want the CCW command
				angle =  2*Math.PI - smallestAngle;
			}
		}

		int nbPoints = 8;
		// Adaptive points count
		double arcLength = Math.abs(angle * v1.length());
		nbPoints = (int) (arcLength * 8 );
		Matrix3d rot = new Matrix3d();
		rot.rotZ(angle / (nbPoints + 1));
		double dz = (end.z - start.z) / (nbPoints + 1);
		gl.glVertex3d(start.x, start.y , start.z);

		for(int i = 0; i < nbPoints; i++){
			rot.transform(v1);
			gl.glVertex3d(center.x + v1.x, center.y + v1.y, start.z + i * dz);
		}
		gl.glColor3f(color.x, color.y, color.z);
		gl.glVertex3d(end.x, end.y, end.z);
		gl.glEnd();

	}


	public static void drawLineStrip(GL2 gl, Tuple6b... points) throws GkException {
		// TODO Auto-generated method stub
	}


	public static void drawLineStrip(GL2 gl, Point3f color, Tuple6b... points) throws GkException {
		// TODO Auto-generated method stub
	}


	public static void drawLineStrip(GL2 gl, Point3f color, int style, Tuple6b... points) throws GkException {
		// TODO Auto-generated method stub
	}

	public static void drawXYZAxis(GL2 gl, Tuple6b position, Point3f xColor, Point3f yColor, Point3f zColor, double scale, String label, double charWidth) throws GkException {
//		gl.glLineWidth(1.5f);
//		GLUT glut = new GLUT();
//
//		Tuple6b zero =  new Tuple6b(0, 0, 0);
//		Tuple6b xaxis = new Tuple6b(1 * scale, 0, 0);
//		Tuple6b yaxis = new Tuple6b(0, 1 * scale, 0);
//		Tuple6b zaxis = new Tuple6b(0, 0, 1 * scale);
//		gl.glPushMatrix();
//		gl.glTranslated(position.getX().doubleValue(), position.getY().doubleValue(), position.getZ().doubleValue());
//
//		gl.glPushMatrix();
//		drawSegment(gl, zero, xaxis, xColor);
//		gl.glTranslated(xaxis.getX().doubleValue() - 1, xaxis.getY().doubleValue(), xaxis.getZ().doubleValue());
//		gl.glRotated(90, 0, 1, 0);
//		glut.glutSolidCone(0.5, 1, 8, 1);
//		gl.glPopMatrix();
//
//		gl.glPushMatrix();
//		drawSegment(gl, zero, yaxis, yColor);
//		gl.glTranslated(yaxis.getX().doubleValue(), yaxis.getY().doubleValue() - 1, yaxis.getZ().doubleValue());
//		gl.glRotated(-90, 1, 0, 0);
//		glut.glutSolidCone(0.5, 1, 8, 1);
//		gl.glPopMatrix();
//
//		gl.glPushMatrix();
//		drawSegment(gl, zero, zaxis, zColor);
//		gl.glTranslated(zaxis.getX().doubleValue(), zaxis.getY().doubleValue(), zaxis.getZ().doubleValue() - 1);
//		glut.glutSolidCone(0.5, 1, 8, 1);
//		gl.glPopMatrix();
//		if(StringUtils.isNotBlank(label)){
//			gl.glPushMatrix();
//			double textScale = charWidth / glut.glutStrokeWidth(GLUT.STROKE_MONO_ROMAN, ' ');
//			gl.glTranslated(1, 1, 0);
//			gl.glScaled(textScale, textScale, textScale);
//			glut.glutStrokeString(GLUT.STROKE_MONO_ROMAN, label);
//			gl.glPopMatrix();
//		}
//		gl.glPopMatrix();
		throw new GkTechnicalException("Not implemented");
	}

	public static void drawXYZAxis(GL2 gl, Tuple6b position, Point3f xColor, Point3f yColor, Point3f zColor, double scale) throws GkException {
		drawXYZAxis(gl, position, xColor, yColor, zColor, scale, null, 1);

	}

	/**
	 * Generate a FloatBuffer with the given list of Tuple4d.
	 * For each tuple4d, 4 float values will be added the the created buffer.
	 * @param lstTuple4d the list of Tuple4d
	 * @return a FloatBuffer
	 */
	public static FloatBuffer buildFloatBuffer4d(List<? extends Tuple4d> lstTuple4d){
		FloatBuffer buffer = FloatBuffer.allocate( CollectionUtils.size(lstTuple4d) * 4);
		for (Tuple4d tuple4d : lstTuple4d) {
			buffer.put((float) tuple4d.x);
			buffer.put((float) tuple4d.y);
			buffer.put((float) tuple4d.z);
			buffer.put((float) tuple4d.w);
		}
		return buffer;
	}
	/**
	 * Generate a FloatBuffer with the given list of Tuple4f.
	 * For each tuple4f, 4 float values will be added the the created buffer.
	 * @param lstTuple4d the list of Tuple4d
	 * @return a FloatBuffer
	 */
	public static FloatBuffer buildFloatBuffer4f(List<? extends Tuple4f> lstTuple4f){
		FloatBuffer buffer = FloatBuffer.allocate( CollectionUtils.size(lstTuple4f) * 4);
		for (Tuple4f tuple4d : lstTuple4f) {
			buffer.put(tuple4d.x);
			buffer.put(tuple4d.y);
			buffer.put(tuple4d.z);
			buffer.put(tuple4d.w);
		}
		return buffer;
	}

	/**
	 * Generate a FloatBuffer with the given list of Tuple3d.
	 * For each tuple3d, 3 float values will be added the the created buffer.
	 * @param lstTuple4d the list of Tuple4d
	 * @return a FloatBuffer
	 */
	public static FloatBuffer buildFloatBuffer3d(List<? extends Tuple3d> lstTuple){
		FloatBuffer buffer = FloatBuffer.allocate( CollectionUtils.size(lstTuple) * 4);
		for (Tuple3d tuple3d : lstTuple) {
			buffer.put((float) tuple3d.x);
			buffer.put((float) tuple3d.y);
			buffer.put((float) tuple3d.z);
			buffer.put(1);
		}
		return buffer;
	}

	/**
	 * Generate a FloatBuffer with the given list of Tuple3f.
	 * For each tuple3f, 3 float values will be added the the created buffer.
	 * @param lstTuple4d the list of Tuple4d
	 * @return a FloatBuffer
	 */
	public static FloatBuffer buildFloatBuffer3f(List<? extends Tuple3f> lstTuple){
		FloatBuffer buffer = FloatBuffer.allocate( CollectionUtils.size(lstTuple) * 4);
		for (Tuple3f tuple3f : lstTuple) {
			buffer.put(tuple3f.x);
			buffer.put(tuple3f.y);
			buffer.put(tuple3f.z);
			buffer.put(1);
		}
		return buffer;
	}
}
