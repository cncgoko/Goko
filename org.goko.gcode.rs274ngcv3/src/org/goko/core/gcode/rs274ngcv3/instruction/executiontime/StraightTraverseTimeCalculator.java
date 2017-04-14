package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.execution.ExecutionConstraint;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightTraverseInstruction;
import org.goko.core.math.Tuple6b;

public class StraightTraverseTimeCalculator extends AbstractInstructionTimeCalculator<StraightTraverseInstruction> {

	public StraightTraverseTimeCalculator() {
		super(InstructionType.STRAIGHT_TRAVERSE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.AbstractInstructionTimeCalculator#calculateInstructionExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction, org.goko.core.execution.ExecutionConstraint)
	 */
	@Override
	public Time calculateInstructionExecutionTime(GCodeContext context, StraightTraverseInstruction instruction, ExecutionConstraint constraint) throws GkException {
		Tuple6b 		positionBefore 	= context.getPosition();
		GCodeContext postContext = new GCodeContext(context);
		instruction.apply(postContext);
		Tuple6b 		positionAfter 	=  postContext.getPosition();

		Tuple6b delta = positionBefore.subtract(positionAfter);
		Length max = delta.length();

		Speed feedrate = Speed.valueOf(1500, SpeedUnit.MILLIMETRE_PER_MINUTE);
		feedrate = constraint.getMaximumFeedrate(feedrate, delta);
		return max.divide(feedrate);		
	}

}
