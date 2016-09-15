package org.goko.core.gcode.element;

public interface IInstruction{
	/** 
	 * Returns the internal identifier of this instruction 
	 * @return the internal identifier of this instruction
	 */
	Integer getId();
	
	/**
	 *  Returns the id of the GCode line that generated this instruction
	 *  @return the id of the GCode line that generated this instruction
	 */
	@Deprecated
	Integer getIdGCodeLine();
	
	IInstructionType getType();
	
}
