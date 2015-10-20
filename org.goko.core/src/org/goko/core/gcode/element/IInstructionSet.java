package org.goko.core.gcode.element;

import java.util.List;

public interface IInstructionSet<I extends IInstruction> {

	List<I> getInstructions();
	
	int size();
	
	I get(int index);
}
