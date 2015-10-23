package org.goko.core.math;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;

public class Arc3b {
	private Tuple6b start;
	private Tuple6b center;
	private Tuple6b end;	
	private Vector3d axis;
	private Quantity<Angle> angle;
	private Quantity<Length> radius;
	
	public Arc3b(Tuple6b start, Tuple6b center, Tuple6b end, Vector3d axis, boolean clockwise) {
		super();
		this.start = start;
		this.center = center;
		this.end = end;
		this.axis = axis;
		this.radius = start.distance(end);//new Vector3d(start.x - center.x, start.y - center.y, start.z - center.z).length();
		build(clockwise);
	}

	private Matrix3d getBaseChangeMatrix(){
		Tuple6b v1 = new Tuple6b(start.getX().subtract(center.getX()),
								 start.getY().subtract(center.getY()),
								 start.getZ().subtract(center.getZ()),null,null,null);
		Vector3d v3 = new Vector3d(axis);		
		v1.normalize();		
		v3.normalize();
		Vector3d v2 = new Vector3d();
		v2.cross(v3, v1);
		
//		Matrix3d m = new Matrix3d(	v1.x, v2.x, v3.x,
//									v1.y, v2.y, v3.y,
//									v1.z, v2.z, v3.z);
		
		Matrix3d m = new Matrix3d(	v1.x, v1.y, v1.z,
									v2.x, v2.y, v2.z,
									v3.x, v3.y, v3.z);
				
		return m;
	}
	
	public void build(boolean clockwise){		
		Matrix3d matrix = getBaseChangeMatrix();
		Matrix3d invMatrix = new Matrix3d();
		invMatrix.invert(matrix);		
		
		Point3d lStart 	= new Point3d(start);
		Point3d lCenter = new Point3d(center);
		Point3d lEnd 	= new Point3d(end);
		
		matrix.transform(lStart);
		matrix.transform(lCenter);
		matrix.transform(lEnd);
		
		Vector3d v1 = new Vector3d(lStart.x - lCenter.x, lStart.y - lCenter.y, lStart.z - lCenter.z);
		Vector3d v2 = new Vector3d(lEnd.x - lCenter.x, lEnd.y - lCenter.y, lEnd.z - lCenter.z);
		
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
		this.angle = NumberQuantity.of(angle, SI.DEGREE_ANGLE);
	}
	
	public Matrix3d getRotationMatrix(double rAngle) {
		Matrix3d m = new Matrix3d();
		double c = Math.cos(rAngle);
		double s = Math.sin(rAngle);
		double t = 1.0 - c;
		// if axis is not already normalised then uncomment this
		// double magnitude = Math.sqrt(a1.x*a1.x + a1.y*a1.y + a1.z*a1.z);
		// if (magnitude==0) throw error;
		// a1.x /= magnitude;
		// a1.y /= magnitude;
		// a1.z /= magnitude;

		m.m00 = c + axis.x * axis.x * t;
		m.m11 = c + axis.y * axis.y * t;
		m.m22 = c + axis.z * axis.z * t;

		double tmp1 = axis.x * axis.y * t;
		double tmp2 = axis.z * s;
		m.m10 = tmp1 + tmp2;
		m.m01 = tmp1 - tmp2;
		tmp1 = axis.x * axis.z * t;
		tmp2 = axis.y * s;
		m.m20 = tmp1 - tmp2;
		m.m02 = tmp1 + tmp2;
		tmp1 = axis.y * axis.z * t;
		tmp2 = axis.x * s;
		m.m21 = tmp1 + tmp2;
		m.m12 = tmp1 - tmp2;
		return m;
	}

	public double getLength(){
		return Math.abs(angle)*radius;
	}
	
	public Point3d point(double i){
		Matrix3d rMat = getRotationMatrix(i * angle);
		Point3d res = new Point3d(start);
		res.sub(center);
		rMat.transform(res);
		res.add(center);
		// along axis motion
		Vector3d delta = new Vector3d(end.x - start.x, end.y - start.y, end.z - start.z);
		double scale = delta.dot(axis) * i;
		delta.x = scale*axis.x;
		delta.y = scale*axis.y;
		delta.z = scale*axis.z;		
		res.add(delta);
		return res;
	}
	
	
}
