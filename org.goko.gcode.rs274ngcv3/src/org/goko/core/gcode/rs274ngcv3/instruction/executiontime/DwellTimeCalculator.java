package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.execution.ExecutionConstraint;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.DwellInstruction;

public class DwellTimeCalculator extends AbstractInstructionTimeCalculator<DwellInstruction> {

	public DwellTimeCalculator() {
		super(InstructionType.DWELL);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.AbstractInstructionTimeCalculator#calculateInstructionExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction, org.goko.core.execution.ExecutionConstraint)
	 */
	@Override
	public Time calculateInstructionExecutionTime(GCodeContext context, DwellInstruction instruction, ExecutionConstraint constraint) throws GkException {	
		return Time.valueOf(instruction.getSeconds(), TimeUnit.SECOND);	
	}
}
