package org.goko.core.gcode.element;

import java.util.List;

public interface IInstructionProvider {
	
	List<IInstructionSet> getInstructionSets();
}
