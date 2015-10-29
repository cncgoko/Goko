package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.math.Tuple6b;

public class ArcFeedTimeCalculator extends AbstractInstructionTimeCalculator<ArcFeedInstruction> {

	public ArcFeedTimeCalculator() {
		super(InstructionType.ARC_FEED);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.AbstractInstructionTimeCalculator#calculateExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction)
	 */
	@Override
	protected Quantity<Time> calculateExecutionTime(GCodeContext context, ArcFeedInstruction instruction) throws GkException {
		boolean clockwise = instruction.isClockwise();

		Matrix3d matrix = getOrientationMatrix(context.getPlane());
		Matrix3d invMatrix = new Matrix3d();
		invMatrix.invert(matrix);
		
		Tuple6b startTuple = new Tuple6b(context.getX(), context.getY(), context.getZ(), context.getA(), context.getB(), context.getC());
		
		Point3d start 	= startTuple.toPoint3d(context.getUnit().getUnit());
		Point3d center 	= getCenterPoint(context, instruction);
		Point3d end 	= getEndPoint(context, instruction);
		
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

		double feedrate = 0;
		if(context.getFeedrate() != null){
			feedrate = context.getFeedrate().doubleValue();
		}else{
			return NumberQuantity.zero(SI.SECOND);
		}
		if(feedrate == 0){
			return NumberQuantity.zero(SI.SECOND);
		}
		return NumberQuantity.of( ((Math.abs(angle) * v1.length()) / feedrate) * 60, SI.SECOND);	
	}
	
	private Matrix3d getOrientationMatrix(EnumPlane enumGCodeCommandPlane){
		Matrix3d m = null;
		if(EnumPlane.XY_PLANE == enumGCodeCommandPlane){
			m = new Matrix3d(1, 0, 0,
						     0, 1, 0,
						     0, 0, 1);
		}else if(EnumPlane.XZ_PLANE == enumGCodeCommandPlane){
			m = new Matrix3d(1, 0, 0,
						     0, 0, -1,
						     0, 1, 0);
		}else if(EnumPlane.YZ_PLANE == enumGCodeCommandPlane){
			m = new Matrix3d(0, 1, 0,
						     0, 0, 1,
						     1, 0, 0);
		}
		return m;		
	}
	
	private Point3d getEndPoint(GCodeContext context, ArcFeedInstruction instruction){
		Tuple6b startTuple = new Tuple6b(instruction.getFirstEnd(), instruction.getSecondEnd(), instruction.getAxisEndPoint(), instruction.getA(), instruction.getB(), instruction.getC());
		Point3d result = startTuple.toPoint3d(context.getUnit().getUnit());
		getOrientationMatrix(context.getPlane()).transform(result);
		return result; 
	}
	
	private Point3d getCenterPoint(GCodeContext context, ArcFeedInstruction instruction){
		Tuple6b startTuple = new Tuple6b(instruction.getFirstAxis(), instruction.getSecondAxis(), instruction.getAxisEndPoint(), instruction.getA(), instruction.getB(), instruction.getC());
		Point3d result = startTuple.toPoint3d(context.getUnit().getUnit());
		getOrientationMatrix(context.getPlane()).transform(result);
		return result; 
	}

}
