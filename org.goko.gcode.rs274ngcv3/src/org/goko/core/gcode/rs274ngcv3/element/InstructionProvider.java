package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.element.IInstructionSet;

public class InstructionProvider implements IInstructionProvider {
	/** The list of instruction set */
	List<IInstructionSet> lstInstructionSet;
	
	/** Constructor */
	public InstructionProvider() {
		lstInstructionSet = new ArrayList<IInstructionSet>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionProvider#getInstructionSets()
	 */
	@Override
	public List<IInstructionSet> getInstructionSets() {
		return lstInstructionSet;
	}
	
	/**
	 * Adds the given Instruction set 
	 * @param set the set to add
	 */
	public void addInstructionSet(IInstructionSet set){
		lstInstructionSet.add(set);
	}

}
