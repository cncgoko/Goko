package org.goko.core.gcode.rs274ngcv3.utils;

import java.math.BigDecimal;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.utils.BigDecimalUtils;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.math.Arc3b;
import org.goko.core.math.Tuple6b;

public class InstructionUtils {

	public static Arc3b getArc(GCodeContext context, ArcFeedInstruction instruction){
		if(instruction.getRadius() != null){
			return getArcRadiusMode(context, instruction);
		}else{
			return getArcIJKMode(context, instruction);
		}
	}

	protected static Arc3b getArcIJKMode(GCodeContext context, ArcFeedInstruction instruction){
		boolean clockwise = instruction.isClockwise();

		Tuple6b startTuple = new Tuple6b(context.getX(), context.getY(), context.getZ(), context.getA(), context.getB(), context.getC());
		// Apply coordinate system offset
		Tuple6b offset = context.getCoordinateSystemData(context.getCoordinateSystem());
		startTuple = startTuple.add(offset);

		Tuple6b center 	= getCenterPointIJKMode(context, instruction);
		Tuple6b end 	= getEndPoint(context, instruction);

		return new Arc3b(startTuple, center, end, getNormalVector(context.getPlane()), clockwise);		
	}
	
	protected static Arc3b getArcRadiusMode(GCodeContext context, ArcFeedInstruction instruction){
		Tuple6b startTuple = new Tuple6b(context.getX(), context.getY(), context.getZ(), context.getA(), context.getB(), context.getC());
		// Apply coordinate system offset
		Tuple6b offset = context.getCoordinateSystemData(context.getCoordinateSystem());
		startTuple = startTuple.add(offset);		
		
		Tuple6b end 	= getEndPoint(context, instruction);
		Tuple6b center 	= getCenterPointRadiusMode(startTuple, end, instruction, getNormalVector(context.getPlane()));
		
		return new Arc3b(startTuple, center, end, getNormalVector(context.getPlane()), instruction.isClockwise());
	}
	
	public static Vector3d getNormalVector(EnumPlane enumGCodeCommandPlane){
		Vector3d vec = null;
		if(EnumPlane.XY_PLANE == enumGCodeCommandPlane){
			vec = new Vector3d(0,0,1);
		}else if(EnumPlane.XZ_PLANE == enumGCodeCommandPlane){
			vec = new Vector3d(0,1,0);
		}else if(EnumPlane.YZ_PLANE == enumGCodeCommandPlane){
			vec = new Vector3d(1,0,0);
		}
		return vec;
	}
	
	public static boolean isValidArcFeedInstruction(GCodeContext context, ArcFeedInstruction arcInstruction){
		boolean valid = true;
		// Words verification
		switch (context.getPlane()) {
		case XY_PLANE:	
				if(arcInstruction.getX() == null && arcInstruction.getY() == null){
					valid = false;
				}
				if(arcInstruction.getRadius() == null && arcInstruction.getI() == null && arcInstruction.getJ() == null){
					valid = false;
				}
			break;
		case YZ_PLANE:	
			if(arcInstruction.getY() == null && arcInstruction.getZ() == null){
				valid = false;
			}
			if(arcInstruction.getRadius() == null && arcInstruction.getJ() == null && arcInstruction.getK() == null){
				valid = false;
			}
			break;
		case XZ_PLANE:
			if(arcInstruction.getZ() == null && arcInstruction.getX() == null){
				valid = false;
			}
			if(arcInstruction.getRadius() == null && arcInstruction.getK() == null && arcInstruction.getI() == null){
				valid = false;
			}
			break;
		default: valid = false;			
		}
		return valid;
	}

	public static Tuple6b getEndPoint(GCodeContext context, ArcFeedInstruction instruction){		
		Tuple6b startTuple = null;
		if(context.getDistanceMode() == EnumDistanceMode.ABSOLUTE){
			startTuple = new Tuple6b(	context.getX(),
										context.getY(),
										context.getZ(),
										context.getA(),
										context.getB(),
										context.getC());
			if(instruction.getX() != null){
				startTuple.setX(instruction.getX());
			}
			if(instruction.getY() != null){
				startTuple.setY(instruction.getY());
			}
			if(instruction.getZ() != null){
				startTuple.setZ(instruction.getZ());
			}
		} else if(context.getDistanceMode() == EnumDistanceMode.RELATIVE){
			startTuple = new Tuple6b(	QuantityUtils.add(context.getX(), instruction.getX()),
										QuantityUtils.add(context.getY(), instruction.getY()),
										QuantityUtils.add(context.getZ(), instruction.getZ()),
										context.getA(),
										context.getB(),
										context.getC());
		}else{
			throw new RuntimeException("Unsupported distance mode "+context.getDistanceMode());
			
		}
		// Apply coordinate system offset
		Tuple6b offset = context.getCoordinateSystemData(context.getCoordinateSystem());
		startTuple = startTuple.add(offset);

		Point3d result = startTuple.toPoint3d(context.getUnit().getUnit());
	
		Tuple6b resultTuple = new Tuple6b(result.x, result.y, result.z,context.getUnit().getUnit());
		return resultTuple;
	}
	
	public static Tuple6b getCenterPoint(GCodeContext context, ArcFeedInstruction instruction){
		if(instruction.getRadius() != null){
			return getCenterPointRadiusMode(context, instruction);
		}else{			
			return getCenterPointIJKMode(context, instruction);
		}
	}
	
	public static Tuple6b getCenterPointIJKMode(GCodeContext context, ArcFeedInstruction instruction){		
		Tuple6b startTuple = new Tuple6b(	QuantityUtils.add(context.getX(), instruction.getI()),
											QuantityUtils.add(context.getY(), instruction.getJ()),
											QuantityUtils.add(context.getZ(), instruction.getK()),
											context.getA(),
											context.getB(),
											context.getC());
		// Apply coordinate system offset
		Tuple6b offset = context.getCoordinateSystemData(context.getCoordinateSystem());
		startTuple = startTuple.add(offset);

		Point3d result = startTuple.toPoint3d(context.getUnit().getUnit());

		Tuple6b resultTuple = new Tuple6b(result.x, result.y, result.z,context.getUnit().getUnit());
		return resultTuple;
	}
	
	private static Tuple6b getCenterPointRadiusMode(GCodeContext context, ArcFeedInstruction instruction) {
		Tuple6b startTuple = new Tuple6b(context.getX(), context.getY(), context.getZ(), context.getA(), context.getB(), context.getC());
		// Apply coordinate system offset
		Tuple6b offset = context.getCoordinateSystemData(context.getCoordinateSystem());
		startTuple = startTuple.add(offset);		
		
		Tuple6b end 	= getEndPoint(context, instruction);
		return  getCenterPointRadiusMode(startTuple, end, instruction, getNormalVector(context.getPlane()));
	}
	
	/**
	 * @param startTuple
	 * @param endTuple
	 * @param instruction
	 * @param vector3d 
	 * @return
	 */
	private static Tuple6b getCenterPointRadiusMode(Tuple6b startTuple, Tuple6b endTuple, ArcFeedInstruction instruction, Vector3d planNormal) {
		final Tuple6b delta = endTuple.subtract(startTuple);
		
		// Zero the delta dimension along the normal axis
		if(Math.abs(planNormal.x) > 0.01) delta.setX( Length.ZERO );
		if(Math.abs(planNormal.y) > 0.01) delta.setY( Length.ZERO );
		if(Math.abs(planNormal.z) > 0.01) delta.setZ( Length.ZERO );
		
		final Length radiusLength = instruction.getRadius();
		final BigDecimal halfLength = delta.length().divide(2).value(radiusLength.getUnit());
		final BigDecimal radius = radiusLength.abs().value(radiusLength.getUnit());		
		
		final BigDecimal height = BigDecimalUtils.sqrt(radius.multiply(radius).subtract(halfLength.multiply(halfLength)), 6);
		final Tuple6b halfDelta = delta.scale(new BigDecimal(0.5));
		
		final Vector3d delta3d = delta.toVector3d(radiusLength.getUnit());
		final Vector3d bisecVector = new Vector3d();		
		
		if(instruction.isClockwise()){			
			bisecVector.cross(delta3d, planNormal);
		}else{
			bisecVector.cross(planNormal, delta3d);
		}
		bisecVector.normalize();
		final Tuple6b bisecTuple = new Tuple6b(bisecVector.x, bisecVector.y, bisecVector.z, radiusLength.getUnit());
		
		return startTuple.add(halfDelta).add( bisecTuple.scale(height));
	}
}
