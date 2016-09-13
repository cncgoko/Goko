package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
/**
 * 
 *  Move in a helical arc from the current position at the existing feed rate. The axis of the helix is
 *	parallel to the X, Y, or Z-axis, according to which one is perpendicular to the selected plane. The
 *	helical arc may degenerate to a circular arc if there is no motion parallel to the axis of the helix.
 *	If the selected plane is the XY-plane:
 *	 	1. first_end is the X coordinate of the end of the arc.
 *	 	2.second_end is the Y coordinate of the end of the arc.
 *	 	3.first_axis is the X coordinate of the axis (center) of the arc.
 *	 	4.second_axis is the Y coordinate of the axis (center) of the arc.
 *	 	5.axis_end_point is the Z coordinate of the end of the arc.
 *	If the selected plane is the YZ-plane:
 *	 	1.first_end is the Y coordinate of the end of the arc.
 *	 	2second_end is the Z coordinate of the end of the arc.
 *	 	3.first_axis is the Y coordinate of the axis (center) of the arc.
 *	 	4.second_axis is the Z coordinate of the axis (center) of the arc.
 *	 	5.axis_end_point is the X coordinate of the end of the arc.
 *	If the selected plane is the XZ-plane:
 *	 	1.first_end is the Z coordinate of the end of the arc.
 *	 	2.second_end is the X coordinate of the end of the arc.
 *	 	3.first_axis is the Z coordinate of the axis (center) of the arc.
 *	 	4.second_axis is the X coordinate of the axis (center) of the arc.
 *	 	5.axis_end_point is the Y coordinate of the end of the arc.
 *	If rotation is positive, the arc is traversed counterclockwise as viewed from the positive end of the coordinate axis perpendicular to the currently selected plane. 
 *	If rotation is negative, the arc is traversed clockwise. If rotation is 0, first_end and second_end must be the same as the  corresponding  coordinates  of  the  current  position  and  no  arc  is  made  (but  there  may  be
 *	translation parallel to the axis perpendicular to the selected plane and rotational axis motion).
 *	If rotation is 1, more than 0 but not more than 360 degrees of arc should be made. In general, if rotation is n, n is not 0, and we let N be the absolute value of n, the absolute value of the
 *	amount of rotation in the arc should be more than ([N-1] x 360) but not more than (N x 360). The radius of the helix is determined by the distance from the current position to the axis of helix
 *	or by the distance from the end location to the axis of the helix. It is an error if the two radii are not the same (within some tolerance, to be set by the implementation). The feed rate applies to the distance traveled along the helix. This differs from many existing
 *	systems, which apply the feed rate to the distance traveled by a point on a circle which is the projection of the helix on a plane perpendicular to the axis of the helix. Rotational  axis  motion  along  with  helical  XYZ  motion  has  no  known  applications,  but  is  not
 *	illegal. Rotational axis motion is handled as follows, if there is rotational motion.
 *
 */
public class ArcFeedInstruction extends AbstractInstruction {	
//	/** The first coordinate of the end of the arc */
//	private Length firstEnd;
//	/** The second coordinate of the end of the arc */
//	private Length secondEnd;
//	/** The first coordinate of the center of the arc */
//	private Length firstAxis;
//	/** The second coordinate of the center of the arc */
//	private Length secondAxis;
//	/** The third coordinate of the end of the arc */
//	private Length axisEndPoint;
	/** The rotation count (1 for an arc, N+1 for N turns */
	private Integer rotation;
	/** Raw X coordinate in command, context independent */
	private Length x;
	/** Raw Y coordinate in command, context independent */
	private Length y;
	/** Raw Z coordinate in command, context independent */
	private Length z;
	/** Raw I coordinate in command, context independent */
	private Length i;
	/** Raw J coordinate in command, context independent */
	private Length j;
	/** Raw K coordinate in command, context independent */
	private Length k;
	/** Raw A coordinate in command, context independent */
	private Angle a;
	/** Raw B coordinate in command, context independent */
	private Angle b;
	/** Raw C coordinate in command, context independent */
	private Angle c;
	/** Rotation direction */
	private boolean clockwise;
	

	/** Constructor */
	public ArcFeedInstruction() {
		super(InstructionType.ARC_FEED);
	}

	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param i
	 * @param j
	 * @param k
	 * @param a
	 * @param b
	 * @param c
	 */
	public ArcFeedInstruction(Length x, Length y, Length z, Length i, Length j, Length k, Angle a, Angle b, Angle c, Integer rotation, boolean clockwise) {
		super(InstructionType.ARC_FEED);
		this.x = x;
		this.y = y;
		this.z = z;
		this.i = i;
		this.j = j;
		this.k = k;
		this.a = a;
		this.b = b;
		this.c = c;
		this.rotation = rotation;		
		this.clockwise = clockwise;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		if(clockwise){
			context.setMotionMode(EnumMotionMode.ARC_CLOCKWISE);
		}else{
			context.setMotionMode(EnumMotionMode.ARC_COUNTERCLOCKWISE);
		}
		if(context.getDistanceMode() == EnumDistanceMode.RELATIVE){
			context.setX(NumberQuantity.add(x, context.getX()));
			context.setY(NumberQuantity.add(y, context.getY()));
			context.setZ(NumberQuantity.add(z, context.getZ()));
			context.setA(NumberQuantity.add(a, context.getA()));
			context.setB(NumberQuantity.add(b, context.getB()));
			context.setC(NumberQuantity.add(c, context.getC()));			
		}else{		
			if( x != null ) context.setX(x);
			if( y != null ) context.setY(y);
			if( z != null ) context.setZ(z);
			if( a != null ) context.setA(a);
			if( b != null ) context.setB(b);
			if( c != null ) context.setC(c);
		}
		
//		le renderer des arcs n'est probablement plus bon sur les plans différents de XY depuis le changement (à revoir)
//		context.setPosition(x, y, z, a, b, c);
		
//		switch (context.getPlane()) {
//		case XY_PLANE: applyXyPlane(context);//context.setPosition(firstEnd, secondEnd, axisEndPoint, a, b, c);			
//			break;
//		case XZ_PLANE: applyXzPlane(context);//context.setPosition(secondEnd, axisEndPoint, firstEnd, a, b, c);			
//			break;
//		case YZ_PLANE: applyYzPlane(context);//context.setPosition(axisEndPoint, firstEnd, secondEnd, a, b, c);			
//			break;
//		default:
//			break;
//		}		
	}

//	private void applyXyPlane(GCodeContext context) throws GkException {
//		/*
//		 *	If the selected plane is the XY-plane:
//		 *	 	1. first_end is the X coordinate of the end of the arc.
//		 *	 	2.second_end is the Y coordinate of the end of the arc.
//		 *	 	3.first_axis is the X coordinate of the axis (center) of the arc.
//		 *	 	4.second_axis is the Y coordinate of the axis (center) of the arc.
//		 *	 	5.axis_end_point is the Z coordinate of the end of the arc.
//		 */
//		// Length firstEnd, Length secondEnd, Length firstAxis, Length secondAxis, Length axisEndPoint
//		// x, y, i, j, z
//		if(firstEnd == null){
//			if(context.getDistanceMode() == EnumDistanceMode.RELATIVE){
//				firstEnd = QuantityUtils.add(context.getX(), x);
//				secondEnd = QuantityUtils.add(context.getY(), y);
//				axisEndPoint = QuantityUtils.add(context.getZ(), z);
//			}else{				
//				firstEnd = (x != null) ? x : context.getX();				
//				secondEnd = (y != null) ? y : context.getY();
//				axisEndPoint = (z != null) ? z : context.getZ();
//			}
//			firstAxis = QuantityUtils.add(context.getX(), i);
//			secondAxis = QuantityUtils.add(context.getY(), j);
//		}
//		context.setPosition(firstEnd, secondEnd, axisEndPoint, a, b, c);	
//	}
//	
//	private void applyXzPlane(GCodeContext context) throws GkException {
//		/*
//		 *	If the selected plane is the XZ-plane:
//		 *	 	1.first_end is the Z coordinate of the end of the arc.
//		 *	 	2.second_end is the X coordinate of the end of the arc.
//		 *	 	3.first_axis is the Z coordinate of the axis (center) of the arc.
//		 *	 	4.second_axis is the X coordinate of the axis (center) of the arc.
//		 *	 	5.axis_end_point is the Y coordinate of the end of the arc.
//		 */
//		if(firstEnd == null){
//			if(context.getDistanceMode() == EnumDistanceMode.RELATIVE){
//				firstEnd = QuantityUtils.add(context.getZ(), z);
//				secondEnd = QuantityUtils.add(context.getX(), x);				
//				axisEndPoint = QuantityUtils.add(context.getY(), y);				
//			}else{
//				firstEnd = (z != null) ? z : context.getZ();
//				secondEnd = (x != null) ? x : context.getX();
//				axisEndPoint = (y != null) ? y : context.getY();
//			}
//			firstAxis = QuantityUtils.add(context.getZ(), k);
//			secondAxis = QuantityUtils.add(context.getX(), i);
//		}
//		context.setPosition(secondEnd, axisEndPoint, firstEnd, a, b, c);	
//	}
//	
//	private void applyYzPlane(GCodeContext context) throws GkException {
//		/*
//		 *	If the selected plane is the YZ-plane:
//		 *	 	1.first_end is the Y coordinate of the end of the arc.
//		 *	 	2second_end is the Z coordinate of the end of the arc.
//		 *	 	3.first_axis is the Y coordinate of the axis (center) of the arc.
//		 *	 	4.second_axis is the Z coordinate of the axis (center) of the arc.
//		 *	 	5.axis_end_point is the X coordinate of the end of the arc.
//		 */
//		if(firstEnd == null){
//			if(context.getDistanceMode() == EnumDistanceMode.RELATIVE){
//				firstEnd = QuantityUtils.add(context.getY(), y);
//				secondEnd = QuantityUtils.add(context.getZ(), z);				
//				axisEndPoint = QuantityUtils.add(context.getX(), x);				
//			}else{
//				firstEnd = (y != null) ? y : context.getY();
//				secondEnd = (z != null) ? z : context.getZ();
//				axisEndPoint = (x != null) ? x : context.getX();
//			}
//			firstAxis = QuantityUtils.add(context.getY(), j);
//			secondAxis = QuantityUtils.add(context.getX(), i);
//		}
//		context.setPosition(axisEndPoint, firstEnd, secondEnd, a, b, c);
//	}
//	/**
//	 * @return the firstEnd
//	 */
//	public Length getFirstEnd() {
//		return firstEnd;
//	}
//
//	/**
//	 * @param firstEnd the firstEnd to set
//	 */
//	public void setFirstEnd(Length firstEnd) {
//		this.firstEnd = firstEnd;
//	}
//
//	/**
//	 * @return the secondEnd
//	 */
//	public Length getSecondEnd() {
//		return secondEnd;
//	}
//
//	/**
//	 * @param secondEnd the secondEnd to set
//	 */
//	public void setSecondEnd(Length secondEnd) {
//		this.secondEnd = secondEnd;
//	}
//
//	/**
//	 * @return the firstAxis
//	 */
//	public Length getFirstAxis() {
//		return firstAxis;
//	}
//
//	/**
//	 * @param firstAxis the firstAxis to set
//	 */
//	public void setFirstAxis(Length firstAxis) {
//		this.firstAxis = firstAxis;
//	}
//
//	/**
//	 * @return the secondAxis
//	 */
//	public Length getSecondAxis() {
//		return secondAxis;
//	}
//
//	/**
//	 * @param secondAxis the secondAxis to set
//	 */
//	public void setSecondAxis(Length secondAxis) {
//		this.secondAxis = secondAxis;
//	}
//
//	/**
//	 * @return the axisEndPoint
//	 */
//	public Length getAxisEndPoint() {
//		return axisEndPoint;
//	}
//
//	/**
//	 * @param axisEndPoint the axisEndPoint to set
//	 */
//	public void setAxisEndPoint(Length axisEndPoint) {
//		this.axisEndPoint = axisEndPoint;
//	}

	/**
	 * @return the rotation
	 */
	public Integer getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the a
	 */
	public Angle getA() {
		return a;
	}

	/**
	 * @return the b
	 */
	public Angle getB() {
		return b;
	}

	/**
	 * @return the c
	 */
	public Angle getC() {
		return c;
	}

	/**
	 * @return the clockwise
	 */
	public boolean isClockwise() {
		return clockwise;
	}

	/**
	 * @param clockwise the clockwise to set
	 */
	public void setClockwise(boolean clockwise) {
		this.clockwise = clockwise;
	}

	/**
	 * @return the x
	 */
	public Length getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(Length x) {
		this.x = x;
	//	this.firstEnd = null;
	}

	/**
	 * @return the y
	 */
	public Length getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Length y) {
		this.y = y;
	//	this.firstEnd = null;
	}

	/**
	 * @return the z
	 */
	public Length getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(Length z) {
		this.z = z;
	//	this.firstEnd = null;
	}

	/**
	 * @return the i
	 */
	public Length getI() {
		return i;
	}

	/**
	 * @param i the i to set
	 */
	public void setI(Length i) {
		this.i = i;
	//	this.firstEnd = null;
	}

	/**
	 * @return the j
	 */
	public Length getJ() {
		return j;		
	}

	/**
	 * @param j the j to set
	 */
	public void setJ(Length j) {
		this.j = j;
	//	this.firstEnd = null;
	}

	/**
	 * @return the k
	 */
	public Length getK() {
		return k;
	}

	/**
	 * @param k the k to set
	 */
	public void setK(Length k) {
		this.k = k;
	//	this.firstEnd = null;
	}

	/**
	 * @param a the a to set
	 */
	public void setA(Angle a) {
		this.a = a;
	}

	/**
	 * @param b the b to set
	 */
	public void setB(Angle b) {
		this.b = b;
	}

	/**
	 * @param c the c to set
	 */
	public void setC(Angle c) {
		this.c = c;
	}


	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + (clockwise ? 1231 : 1237);
		result = prime * result + ((i == null) ? 0 : i.hashCode());
		result = prime * result + ((j == null) ? 0 : j.hashCode());
		result = prime * result + ((k == null) ? 0 : k.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		result = prime * result + ((z == null) ? 0 : z.hashCode());
		return result;
	}


	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArcFeedInstruction other = (ArcFeedInstruction) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (clockwise != other.clockwise)
			return false;
		if (i == null) {
			if (other.i != null)
				return false;
		} else if (!i.equals(other.i))
			return false;
		if (j == null) {
			if (other.j != null)
				return false;
		} else if (!j.equals(other.j))
			return false;
		if (k == null) {
			if (other.k != null)
				return false;
		} else if (!k.equals(other.k))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		if (z == null) {
			if (other.z != null)
				return false;
		} else if (!z.equals(other.z))
			return false;
		return true;
	}

	
	
}
