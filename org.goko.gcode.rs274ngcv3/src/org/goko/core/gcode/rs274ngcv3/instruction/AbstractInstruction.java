package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public abstract class AbstractInstruction implements IInstruction {
	/** The type of the instruction */
	private InstructionType type;
	
	/**
	 * Constructor
	 * @param type the instruction type
	 */
	public AbstractInstruction(InstructionType type) {
		this.type = type;
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#getType()
	 */
	@Override
	public InstructionType getType() {
		return type;
	}

}
