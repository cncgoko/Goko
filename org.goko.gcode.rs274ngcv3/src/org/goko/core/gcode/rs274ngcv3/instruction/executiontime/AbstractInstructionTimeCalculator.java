package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.execution.ExecutionConstraint;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;

public abstract class AbstractInstructionTimeCalculator<I extends AbstractInstruction> implements IInstructionTimeCalculator {
	/** The type of the instruction */
	private InstructionType type;

	
	/**
	 * Constructor
	 * @param type the instruction type
	 */
	public AbstractInstructionTimeCalculator(InstructionType type) {
		this.type = type;		
	}
	

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.IInstructionTimeCalculator#getInstructionType()
	 */
	@Override
	public InstructionType getInstructionType() {		
		return type; 
	}
	
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.IInstructionTimeCalculator#calculateExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Time calculateExecutionTime(GCodeContext context, AbstractInstruction instruction, ExecutionConstraint constraint) throws GkException {
		return calculateInstructionExecutionTime(context, (I)instruction, constraint);
	}

	/**
	 * Evaluate the time required to execute the given instruction  
	 * @param context the context 
	 * @param instruction the instruction to evaluate 
	 * @return a Time quantity
	 * @throws GkException GkException
	 */
	protected abstract Time calculateInstructionExecutionTime(GCodeContext context, I instruction, ExecutionConstraint constraint) throws GkException;
}
