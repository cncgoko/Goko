package org.goko.core.gcode.element;

import java.util.List;

public interface IInstructionProvider<T extends IInstruction, I extends IInstructionSet<T>> {
	
	List<I> getInstructionSets();
	
	int size();
	
	I get(int index);
}
