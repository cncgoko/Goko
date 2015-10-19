package org.goko.core.gcode.element;

public interface IInstruction{
	
	IInstructionType getType();
	
	//void apply(T context) throws GkException;
}
