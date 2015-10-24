package org.goko.core.gcode.rs274ngcv3.utils;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.math.Arc3b;
import org.goko.core.math.Tuple6b;

public class InstructionUtils {

	public static Arc3b getArc(GCodeContext context, ArcFeedInstruction instruction){		
		boolean clockwise = instruction.isClockwise();
		
		Tuple6b startTuple = new Tuple6b(context.getX(), context.getY(), context.getZ(), context.getA(), context.getB(), context.getC());
				
		Tuple6b center 	= getCenterPoint(context, instruction);
		Tuple6b end 	= getEndPoint(context, instruction);
		
		return new Arc3b(startTuple, center, end, getNormalVector(context.getPlane()), clockwise);
	}
	
	private static Vector3d getNormalVector(EnumPlane enumGCodeCommandPlane){
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
	
	private static Matrix3d getOrientationMatrix(EnumPlane enumGCodeCommandPlane){
		Matrix3d m = null;
		if(EnumPlane.XY_PLANE == enumGCodeCommandPlane){
			m = new Matrix3d(1, 0, 0,
						     0, 1, 0,
						     0, 0, 1);
		}else if(EnumPlane.XZ_PLANE == enumGCodeCommandPlane){
//			m = new Matrix3d(1, 0, 0,
//						     0, 0, -1,
//						     0, 1, 0);
			m = new Matrix3d(0, 1, 0,
						     0, 0, 1,
						     1, 0, 0);
		}else if(EnumPlane.YZ_PLANE == enumGCodeCommandPlane){
			m = new Matrix3d(0, 0, 1,
						     1, 0, 0,
						     0, 1, 0);
		}
		return m;		
	}
	
	private static Tuple6b getEndPoint(GCodeContext context, ArcFeedInstruction instruction){
		System.out.println("");
		Tuple6b startTuple = new Tuple6b(instruction.getFirstEnd(), instruction.getSecondEnd(), instruction.getAxisEndPoint(), instruction.getA(), instruction.getB(), instruction.getC());
		Point3d result = startTuple.toPoint3d(context.getUnit().getUnit());
		getOrientationMatrix(context.getPlane()).transform(result);
		Tuple6b resultTuple = new Tuple6b(result.x, result.y, result.z,context.getUnit().getUnit());
		return resultTuple; 
	}
	
	private static Tuple6b getCenterPoint(GCodeContext context, ArcFeedInstruction instruction){		
		System.out.println("");
		Tuple6b startTuple = new Tuple6b(instruction.getFirstAxis(), instruction.getSecondAxis(), instruction.getAxisEndPoint(), instruction.getA(), instruction.getB(), instruction.getC());
		Point3d result = startTuple.toPoint3d(context.getUnit().getUnit());
		getOrientationMatrix(context.getPlane()).transform(result);
		Tuple6b resultTuple = new Tuple6b(result.x, result.y, result.z,context.getUnit().getUnit());
		return resultTuple; 
	}
}
