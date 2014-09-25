package org.goko.viewer.jogl.service;

import javax.media.opengl.GL2;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.viewer.renderer.IRendererProxy;

/**
 * Utility class for Jogl rendering
 *
 * @author Psyko
 *
 */
public class JoglUtils {


	private static final Point3f DEFAULT_COLOR = new Point3f(0.8f,0.8f,0.8f);


	public static void drawPoint(GL2 gl,Tuple6b point) throws GkException {
		Point3d startPoint 	= point.toPoint3d();
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3d(startPoint.x-0.5, startPoint.y, startPoint.z);
		gl.glVertex3d(startPoint.x+0.5, startPoint.y, startPoint.z);
		gl.glVertex3d(startPoint.x, startPoint.y-0.5, startPoint.z);
		gl.glVertex3d(startPoint.x, startPoint.y+0.5, startPoint.z);
		gl.glVertex3d(startPoint.x, startPoint.y, startPoint.z-0.5);
		gl.glVertex3d(startPoint.x, startPoint.y, startPoint.z+0.5);
		gl.glEnd();
	}


	public static void drawPoint(GL2 gl,Tuple6b point, Point3f color) throws GkException {
		gl.glColor3d(color.x, color.y, color.z);
		drawPoint(gl, point);
	}


	public static void drawPoint(GL2 gl,Tuple6b point, Point3f color, int style) throws GkException {
		// TODO Auto-generated method stub
	}

	public static void drawSegment(GL2 gl, Tuple6b start, Tuple6b end) throws GkException {
		Point3d startPoint 	= start.toPoint3d();
		Point3d endPoint 	= end.toPoint3d();
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3d(startPoint.x, startPoint.y, startPoint.z);
		gl.glVertex3d(endPoint.x, endPoint.y, endPoint.z);
		gl.glEnd();
	}

	public static void drawSegment(GL2 gl, Tuple6b start, Tuple6b end, Point3f color) throws GkException {
		Point3d startPoint 	= start.toPoint3d();
		Point3d endPoint 	= end.toPoint3d();
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(startPoint.x, startPoint.y, startPoint.z);
		gl.glVertex3d(endPoint.x, endPoint.y, endPoint.z);
		gl.glEnd();
	}


	public static void drawSegment(GL2 gl, Tuple6b start, Tuple6b end, Point3f color, int style) throws GkException {
		// TODO Auto-generated method stub
	}


	public static void drawArc(GL2 gl, Tuple6b start, Tuple6b end, Tuple6b center, Point3f plane, int direction) throws GkException {
		drawArc(gl, start, end, center, plane, direction,DEFAULT_COLOR,0);
	}


	public static void drawArc(GL2 gl, Tuple6b start, Tuple6b end, Tuple6b center, Point3f plane, int direction, Point3f color) throws GkException {
		drawArc(gl, start, end, center, plane, direction,color,0);
	}

	public static void drawArc(GL2 gl, Tuple6b startPoint, Tuple6b endPoint, Tuple6b centerPoint, Point3f plane, int direction, Point3f color, int style) throws GkException {
		boolean clockwise = direction == IRendererProxy.ARC_CLOCKWISE;
		gl.glBegin(GL2.GL_LINE_STRIP);
		Point3d start = startPoint.toPoint3d();
		Point3d center = centerPoint.toPoint3d();
		Point3d end = endPoint.toPoint3d();

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

		gl.glVertex3d(start.x, start.y , start.z);

		for(int i = 0; i < nbPoints; i++){
			rot.transform(v1);
			gl.glVertex3d(center.x + v1.x, center.y + v1.y, start.z);
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
}
