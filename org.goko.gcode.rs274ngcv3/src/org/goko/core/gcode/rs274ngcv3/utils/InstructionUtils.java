package org.goko.core.gcode.rs274ngcv3.utils;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.math.Arc3b;
import org.goko.core.math.Tuple6b;

public class InstructionUtils {

	public static Arc3b getArc(GCodeContext context, ArcFeedInstruction instruction){
		boolean clockwise = instruction.isClockwise();

		Tuple6b startTuple = new Tuple6b(context.getX(), context.getY(), context.getZ(), context.getA(), context.getB(), context.getC());
		// Apply coordinate system offset
		Tuple6b offset = context.getCoordinateSystemData(context.getCoordinateSystem());
		startTuple = startTuple.add(offset);

		Tuple6b center 	= getCenterPoint(context, instruction);
		Tuple6b end 	= getEndPoint(context, instruction);

		return new Arc3b(startTuple, center, end, getNormalVector(context.getPlane()), clockwise);
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
}
