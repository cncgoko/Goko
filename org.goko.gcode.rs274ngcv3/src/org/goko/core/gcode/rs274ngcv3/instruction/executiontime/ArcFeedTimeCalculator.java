package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.execution.ExecutionConstraint;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.InstructionUtils;
import org.goko.core.math.Arc3b;

public class ArcFeedTimeCalculator extends AbstractInstructionTimeCalculator<ArcFeedInstruction> {

	public ArcFeedTimeCalculator() {
		super(InstructionType.ARC_FEED);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.AbstractInstructionTimeCalculator#calculateInstructionExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction, org.goko.core.execution.ExecutionConstraint)
	 */
	@Override
	public Time calculateInstructionExecutionTime(GCodeContext context, ArcFeedInstruction instruction, ExecutionConstraint constraint) throws GkException {
		Speed feedrate = Speed.ZERO;
		if(context.getFeedrate() != null){
			feedrate = context.getFeedrate();
		}else{
			return Time.ZERO;
		}
		if(feedrate.equals(Speed.ZERO)){
			return Time.ZERO;
		}
		//feedrate = constraint.getMaximumFeedrate(feedrate, null);
		Arc3b arc = InstructionUtils.getArc(context, instruction);	
		return constraint.getTravelTime(feedrate, arc);//arc.getLength().divide(feedrate);	
	}
}
