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

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lstInstructions == null) ? 0 : lstInstructions.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstructionSet other = (InstructionSet) obj;
		if (lstInstructions == null) {
			if (other.lstInstructions != null)
				return false;
		} else if (!CollectionUtils.isEqualCollection(lstInstructions, other.lstInstructions))
			return false;
		return true;
	}

}
