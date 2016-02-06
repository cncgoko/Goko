package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.Time;
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
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.AbstractInstructionTimeCalculator#calculateExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction)
	 */
	@Override
	protected Time calculateExecutionTime(GCodeContext context, ArcFeedInstruction instruction) throws GkException {
		Speed feedrate = Speed.ZERO;
		if(context.getFeedrate() != null){
			feedrate = context.getFeedrate();
		}else{
			return Time.ZERO;
		}
		if(feedrate.equals(Speed.ZERO)){
			return Time.ZERO;
		}
		Arc3b arc = InstructionUtils.getArc(context, instruction);		
		return arc.getLength().divide(feedrate);	
	}
}
