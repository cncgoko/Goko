package org.goko.core.gcode.element;

public interface IInstruction{
	/** 
	 * Returns the internal identifier of this instruction 
	 * @return the internal identifier of this instruction
	 */
	Integer getId();
	
	IInstructionType getType();
	
}
