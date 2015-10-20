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
	Integer getIdGCodeLine();
	
	IInstructionType getType();
	
	//void apply(T context) throws GkException;
}
