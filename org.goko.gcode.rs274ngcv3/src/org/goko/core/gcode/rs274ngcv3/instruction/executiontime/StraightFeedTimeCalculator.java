package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.math.Tuple6b;

public class StraightFeedTimeCalculator extends AbstractInstructionTimeCalculator<StraightFeedInstruction> {

	public StraightFeedTimeCalculator() {
		super(InstructionType.STRAIGHT_FEED);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.AbstractInstructionTimeCalculator#calculateExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction)
	 */
	@Override
	protected Time calculateExecutionTime(GCodeContext context, StraightFeedInstruction instruction) throws GkException {
		Length length = Length.ZERO;
		if(instruction != null){
			
			// The complete angle around the 4th axis
			Angle deltaAngle = Angle.ZERO;
			// FIXME Use a setting to define the 4th axis and then do a dynamic angle detection around the axe A,B or C
			if( instruction.getA() != null){
				deltaAngle = context.getA().subtract(instruction.getA());
			}
			
			if(deltaAngle.abs().lowerThan(Angle.valueOf("0.0001", AngleUnit.DEGREE_ANGLE))){
				length = calculateLengthLinearLine(context, instruction);
			}else{				
				length = calculateLengthRotaryLine(context, instruction);
			}
		}		

		Speed feedrate = context.getFeedrate();
		if(context.getFeedrate() == null || feedrate.equals(Speed.ZERO)){			
			return Time.ZERO;
		}
		return length.abs().divide(feedrate);		
	}

	/**
	 * @param context
	 * @param instruction
	 * @return
	 * @throws GkException 
	 */
	private Length calculateLengthRotaryLine(GCodeContext context, StraightFeedInstruction instruction) throws GkException {
		Tuple6b 		positionBefore 	= context.getPosition();
		GCodeContext postContext = new GCodeContext(context);
		instruction.apply(postContext);
		Tuple6b 		positionAfter 	=  postContext.getPosition();
		
		Tuple6b delta = positionBefore.subtract(positionAfter);
		
		return Length.valueOf(delta.getA().value(AngleUnit.DEGREE_ANGLE), context.getUnit().getUnit());
	}

	/**
	 * @param context
	 * @param instruction
	 * @return
	 * @throws GkException 
	 */
	private Length calculateLengthLinearLine(GCodeContext context, StraightFeedInstruction instruction) throws GkException {
		Tuple6b 		positionBefore 	= context.getPosition();
		GCodeContext postContext = new GCodeContext(context);
		instruction.apply(postContext);
		Tuple6b 		positionAfter 	=  postContext.getPosition();
		Tuple6b delta = positionBefore.subtract(positionAfter);
		
		return delta.length();
	}

}
