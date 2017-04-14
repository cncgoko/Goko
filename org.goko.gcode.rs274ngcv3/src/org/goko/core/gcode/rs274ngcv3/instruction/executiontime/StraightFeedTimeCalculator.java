package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.execution.ExecutionConstraint;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.math.Tuple6b;

public class StraightFeedTimeCalculator extends AbstractInstructionTimeCalculator<StraightFeedInstruction> {

	public StraightFeedTimeCalculator() {
		super(InstructionType.STRAIGHT_FEED);
	}

	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.AbstractInstructionTimeCalculator#calculateInstructionExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction, org.goko.core.execution.ExecutionConstraint)
	 */
	@Override
	public Time calculateInstructionExecutionTime(GCodeContext context, StraightFeedInstruction instruction, ExecutionConstraint constraint) throws GkException {
		Length length = Length.ZERO;
		Speed feedrate = context.getFeedrate();
		Time result = Time.ZERO;
		if(instruction != null){
			
			// The complete angle around the 4th axis
			Angle deltaAngle = Angle.ZERO;
			// FIXME Use a setting to define the 4th axis and then do a dynamic angle detection around the axe A,B or C
			if( instruction.getA() != null){
				deltaAngle = context.getA().subtract(instruction.getA());
			}
			
			Tuple6b 		positionBefore 	= context.getPosition();
			GCodeContext postContext = new GCodeContext(context);
			instruction.apply(postContext);
			Tuple6b 		positionAfter 	=  postContext.getPosition();
			
			Tuple6b delta = positionBefore.subtract(positionAfter);
			
			if(deltaAngle.abs().lowerThan(Angle.valueOf("0.0001", AngleUnit.DEGREE_ANGLE))){
				length = delta.length();
			}else{				
				length = Length.valueOf(delta.getA().value(AngleUnit.DEGREE_ANGLE), context.getUnit().getUnit());
			}
			result = constraint.getTravelTime(feedrate, delta);
		}		
		
		
		return result;//length.abs().divide(feedrate);		
	}
}
