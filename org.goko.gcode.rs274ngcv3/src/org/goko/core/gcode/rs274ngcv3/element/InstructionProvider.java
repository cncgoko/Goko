package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;

public class InstructionProvider implements IInstructionProvider<AbstractInstruction, InstructionSet> {
	/** The list of instruction set */
	List<InstructionSet> lstInstructionSet;

	/** Constructor */
	public InstructionProvider() {
		lstInstructionSet = new ArrayList<InstructionSet>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionProvider#getInstructionSets()
	 */
	@Override
	public List<InstructionSet> getInstructionSets() {
		return lstInstructionSet;
	}
	
	/**
	 * Adds the given Instruction set
	 * @param set the set to add
	 */
	public void addInstruction(AbstractInstruction instruction){
		InstructionSet set = new InstructionSet();
		set.addInstruction(instruction);
		lstInstructionSet.add(set);
	}
	
	/**
	 * Adds the given Instruction set
	 * @param set the set to add
	 */
	public void addInstructionSet(InstructionSet set){
		lstInstructionSet.add(set);
	}

	/**
	 * Adds the given Instruction set
	 * @param set the set to add
	 */
	public void addInstructionSet(List<InstructionSet> lstSet){
		lstInstructionSet.addAll(lstSet);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionProvider#size()
	 */
	@Override
	public int size() {
		return CollectionUtils.size(lstInstructionSet);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionProvider#get(int)
	 */
	@Override
	public InstructionSet get(int index) {
		return lstInstructionSet.get(index);
	}

}
