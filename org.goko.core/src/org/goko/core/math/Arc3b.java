package org.goko.core.math;

import java.math.BigDecimal;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;

public class Arc3b {
	private Tuple6b start;
	private Tuple6b center;
	private Tuple6b end;
	private Point3d pStart;
	private Point3d pCenter;
	private Point3d pEnd;
	
	private Vector3d axis;
	private Angle angle;
	private Length radius;
	private Unit<Length> unit;
	
	public Arc3b(Tuple6b start, Tuple6b center, Tuple6b end, Vector3d axis, boolean clockwise) {
		super();
		this.start = start;
		this.center = center;
		this.end = end;		
		this.axis = axis;
		this.radius = start.distance(center);//new Vector3d(start.x - center.x, start.y - center.y, start.z - center.z).length();
		// Internal fields
		this.unit = start.getX().getUnit();
		this.pStart = start.toPoint3d(unit);
		this.pCenter= center.toPoint3d(unit);
		this.pEnd 	= end.toPoint3d(unit);
		build(clockwise);
	}

	private Matrix3d getBaseChangeMatrix(){
		Vector3d v1 = new Vector3d();
		v1.sub(pStart, pCenter);
		Vector3d v3 = new Vector3d(axis);		
		v1.normalize();		
		v3.normalize();
		Vector3d v2 = new Vector3d();
		v2.cross(v3, v1);
				
		Matrix3d m = new Matrix3d(	v1.x, v1.y, v1.z,
									v2.x, v2.y, v2.z,
									v3.x, v3.y, v3.z);
				
		return m;
	}
	
	public void build(boolean clockwise){		
		Matrix3d matrix = getBaseChangeMatrix();
		Matrix3d invMatrix = new Matrix3d();
		invMatrix.invert(matrix);		
		
		Point3d lStart 	= new Point3d(pStart);
		Point3d lCenter = new Point3d(pCenter);
		Point3d lEnd 	= new Point3d(pEnd);
		
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
				angle =  smallestAngle;//2*Math.PI - smallestAngle;
			}
		}	
		this.angle = Angle.valueOf(BigDecimal.valueOf(angle), AngleUnit.RADIAN);
	}
	
	public Matrix3d getRotationMatrix(Angle rAngle) {
		Matrix3d m = new Matrix3d();
		double c = Math.cos(rAngle.doubleValue(AngleUnit.RADIAN));
		double s = Math.sin(rAngle.doubleValue(AngleUnit.RADIAN));
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

	public Length getLength(){
		return radius.multiply(angle.value(AngleUnit.RADIAN).abs());
	}
	
	public Tuple6b point(double i){
		Matrix3d rMat = getRotationMatrix(angle.multiply(BigDecimal.valueOf(i)));
		Point3d res = new Point3d(pStart);
		res.sub(pCenter);
		rMat.transform(res);
		res.add(pCenter);
		// along axis motion
		Vector3d delta = new Vector3d(pEnd.x - pStart.x, pEnd.y - pStart.y, pEnd.z - pStart.z);
		double scale = delta.dot(axis) * i;
		delta.x = scale*axis.x;
		delta.y = scale*axis.y;
		delta.z = scale*axis.z;		
		res.add(delta);
		return new Tuple6b(res.x, res.y, res.z, unit);
	}

	/**
	 * @return the start
	 */
	public Tuple6b getStart() {
		return start;
	}

	/**
	 * @return the center
	 */
	public Tuple6b getCenter() {
		return center;
	}

	/**
	 * @return the end
	 */
	public Tuple6b getEnd() {
		return end;
	}

	/**
	 * @return the axis
	 */
	public Vector3d getAxis() {
		return axis;
	}

	/**
	 * @return the angle
	 */
	public Angle getAngle() {
		return angle;
	}

	/**
	 * @return the radius
	 */
	public Length getRadius() {
		return radius;
	}
	
	
}
