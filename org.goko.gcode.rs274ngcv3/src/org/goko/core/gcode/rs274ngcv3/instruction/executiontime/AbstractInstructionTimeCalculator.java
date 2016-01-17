package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;

public abstract class AbstractInstructionTimeCalculator<I extends AbstractInstruction> {
	/** The type of the instruction */
	private InstructionType type;
	
	/**
	 * Constructor
	 * @param type the instruction type
	 */
	public AbstractInstructionTimeCalculator(InstructionType type) {
		this.type = type;
	}
	
	/**
	 * Returns the instruction type supported by this builder 
	 * @return the type of instruction 
	 */
	public InstructionType getInstructionType() {		
		return type; 
	}
	
	/**
	 * Evaluate the time required to execute the given instruction  
	 * @param context the context 
	 * @param instruction the instruction to evaluate 
	 * @return a Time quantity
	 * @throws GkException GkException
	 */
	@SuppressWarnings("unchecked")
	public final Time getExecutionTime(GCodeContext context, AbstractInstruction instruction) throws GkException{
		return calculateExecutionTime(context, (I)instruction);
	}
	
	/**
	 * Evaluate the time required to execute the given instruction  
	 * @param context the context 
	 * @param instruction the instruction to evaluate 
	 * @return a Time quantity
	 * @throws GkException GkException
	 */
	protected abstract Time calculateExecutionTime(GCodeContext context, I instruction) throws GkException;
	
}
