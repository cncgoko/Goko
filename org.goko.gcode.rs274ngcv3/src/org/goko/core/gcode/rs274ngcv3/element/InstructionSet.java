package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.element.IInstructionSet;

public class InstructionSet implements IInstructionSet {
	/** The list of instructions */
	private List<IInstruction> lstInstructions;

	/** Constructor */
	public InstructionSet() {
		lstInstructions = new ArrayList<IInstruction>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionSet#getInstructions()
	 */
	@Override
	public List<IInstruction> getInstructions() {		
		return lstInstructions;
	}
	
	/**
	 * Add the given instruction
	 * @param instruction the instruction to add
	 */
	public void addInstruction(IInstruction instruction){
		lstInstructions.add(instruction);	
	}

}
