/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.execution.ExecutionConstraint;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;

/**
 * @author Psyko
 * @date 12 avr. 2017
 */
public interface IInstructionTimeCalculator {

	/**
	 * Evaluate the time required to execute the given instruction  
	 * @param context the context 
	 * @param instruction the instruction to evaluate
	 * @param constraint constraints about the capabilities of the machine 
	 * @return a Time quantity
	 * @throws GkException GkException
	 */
	Time calculateExecutionTime(GCodeContext context, AbstractInstruction instruction, ExecutionConstraint constraint) throws GkException;
	
	/**
	 * Returns the instruction type supported by this builder 
	 * @return the type of instruction 
	 */
	InstructionType getInstructionType();
}
