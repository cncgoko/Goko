package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
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
	/** The first coordinate of the end of the arc */
	private Length firstEnd;
	/** The second coordinate of the end of the arc */
	private Length secondEnd;
	/** The first coordinate of the center of the arc */
	private Length firstAxis;
	/** The second coordinate of the center of the arc */
	private Length secondAxis;
	/** The third coordinate of the end of the arc */
	private Length axisEndPoint;
	/** The rotation count (1 for an arc, N+1 for N turns */
	private Integer rotation;
	/** A coordinate */
	private Angle a;
	/** B coordinate */
	private Angle b;
	/** C coordinate */
	private Angle c;
	/** Rotation direction */
	private boolean clockwise;
	
	/** Constructor */
	public ArcFeedInstruction() {
		super(InstructionType.ARC_FEED);
	}

	/** Constructor */	
	public ArcFeedInstruction(Length firstEnd, Length secondEnd, Length firstAxis, Length secondAxis, Length axisEndPoint, Integer rotation, Angle a, Angle b, Angle c, boolean clockwise) {
		super(InstructionType.ARC_FEED);
		this.firstEnd = firstEnd;
		this.secondEnd = secondEnd;
		this.firstAxis = firstAxis;
		this.secondAxis = secondAxis;
		this.axisEndPoint = axisEndPoint;
		this.rotation = rotation;
		this.a = a;
		this.b = b;
		this.c = c;
		this.clockwise = clockwise;
	}



	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		if(rotation < 0){
			context.setMotionMode(EnumMotionMode.ARC_CLOCKWISE);
		}else{
			context.setMotionMode(EnumMotionMode.ARC_COUNTERCLOCKWISE);
		}
		switch (context.getPlane()) {
		case XY_PLANE: context.setPosition(firstEnd, secondEnd, axisEndPoint, a, b, c);			
			break;
		case XZ_PLANE: context.setPosition(secondEnd, axisEndPoint, firstEnd, a, b, c);			
			break;
		case YZ_PLANE: context.setPosition(axisEndPoint, firstEnd, secondEnd, a, b, c);			
			break;
		default:
			break;
		}		
	}

	/**
	 * @return the firstEnd
	 */
	public Length getFirstEnd() {
		return firstEnd;
	}

	/**
	 * @param firstEnd the firstEnd to set
	 */
	public void setFirstEnd(Length firstEnd) {
		this.firstEnd = firstEnd;
	}

	/**
	 * @return the secondEnd
	 */
	public Length getSecondEnd() {
		return secondEnd;
	}

	/**
	 * @param secondEnd the secondEnd to set
	 */
	public void setSecondEnd(Length secondEnd) {
		this.secondEnd = secondEnd;
	}

	/**
	 * @return the firstAxis
	 */
	public Length getFirstAxis() {
		return firstAxis;
	}

	/**
	 * @param firstAxis the firstAxis to set
	 */
	public void setFirstAxis(Length firstAxis) {
		this.firstAxis = firstAxis;
	}

	/**
	 * @return the secondAxis
	 */
	public Length getSecondAxis() {
		return secondAxis;
	}

	/**
	 * @param secondAxis the secondAxis to set
	 */
	public void setSecondAxis(Length secondAxis) {
		this.secondAxis = secondAxis;
	}

	/**
	 * @return the axisEndPoint
	 */
	public Length getAxisEndPoint() {
		return axisEndPoint;
	}

	/**
	 * @param axisEndPoint the axisEndPoint to set
	 */
	public void setAxisEndPoint(Length axisEndPoint) {
		this.axisEndPoint = axisEndPoint;
	}

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

	
}
