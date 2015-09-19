package org.goko.tools.viewer.jogl.utils.render.gcode.geometry;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandPlane;
import org.goko.tools.viewer.jogl.service.JoglUtils;

/**
 * Linear motion renderer
 *
 * @author PsyKo
 *
 */
public class ArcMotionRenderer{
	
	public List<Point3d> render(ArcMotionCommand command) throws GkException{
		List<Point3d> vertices = new ArrayList<Point3d>();
		boolean clockwise = command.isClockwise();

		Matrix3d matrix = getOrientationMatrix(command.getPlane());
		Matrix3d invMatrix = new Matrix3d();
		invMatrix.invert(matrix);
		
		Point3d start 	= command.getAbsoluteStartCoordinate().to(JoglUtils.JOGL_UNIT).toPoint3d();
		Point3d center 	= command.getAbsoluteCenterCoordinate().to(JoglUtils.JOGL_UNIT).toPoint3d();
		Point3d end 	= command.getAbsoluteEndCoordinate().to(JoglUtils.JOGL_UNIT).toPoint3d();
		
		matrix.transform(start);
		matrix.transform(center);
		matrix.transform(end);
		
		Vector3d v1 = new Vector3d(start.x - center.x, start.y - center.y, start.z - center.z);
		Vector3d v2 = new Vector3d(end.x - center.x, end.y - center.y, end.z - center.z);
		
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

		Point3d realStart = new Point3d();
		invMatrix.transform(start, realStart);
		vertices.add(realStart);
		for(int i = 0; i < nbPoints; i++){
			rot.transform(v1);
			Point3d p = new Point3d(center.x + v1.x, center.y + v1.y, start.z + i * dz);
			invMatrix.transform(p);
			vertices.add(p);
		}
		invMatrix.transform(end);
		vertices.add(end);
		return vertices;
	}
	
	private Matrix3d getOrientationMatrix(EnumGCodeCommandPlane enumGCodeCommandPlane){
		Matrix3d m = null;
		if(EnumGCodeCommandPlane.XY_PLANE == enumGCodeCommandPlane){
			m = new Matrix3d(1, 0, 0,
						     0, 1, 0,
						     0, 0, 1);
		}else if(EnumGCodeCommandPlane.XZ_PLANE == enumGCodeCommandPlane){
			m = new Matrix3d(1, 0, 0,
						     0, 0, -1,
						     0, 1, 0);
		}else if(EnumGCodeCommandPlane.YZ_PLANE == enumGCodeCommandPlane){
			m = new Matrix3d(0, 1, 0,
						     0, 0, 1,
						     1, 0, 0);
		}
		return m;		
	}
}
