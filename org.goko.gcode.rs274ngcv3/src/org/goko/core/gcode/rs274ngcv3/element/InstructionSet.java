package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.gcode.element.IInstructionSet;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;

public class InstructionSet implements IInstructionSet<AbstractInstruction> {
	/** The list of instructions */
	private List<AbstractInstruction> lstInstructions;

	/** Constructor */
	public InstructionSet() {
		lstInstructions = new ArrayList<AbstractInstruction>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionSet#getInstructions()
	 */
	@Override
	public List<AbstractInstruction> getInstructions() {		
		return lstInstructions;
	}
	
	/**
	 * Add the given instruction
	 * @param instruction the instruction to add
	 */
	public void addInstruction(AbstractInstruction instruction){
		lstInstructions.add(instruction);	
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionSet#size()
	 */
	@Override
	public int size() {
		return CollectionUtils.size(lstInstructions);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionSet#get(int)
	 */
	@Override
	public AbstractInstruction get(int index) {
		return lstInstructions.get(index);
	}

}
