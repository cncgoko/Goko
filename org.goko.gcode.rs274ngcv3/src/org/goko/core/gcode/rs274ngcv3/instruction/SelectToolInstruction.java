package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class SelectToolInstruction extends AbstractInstruction {
	private Integer toolNumber;
	
	public SelectToolInstruction(Integer toolNumber) {
		super(InstructionType.SELECT_TOOL);
		this.toolNumber = toolNumber;
	}

//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setSelectedToolNumber(toolNumber);
//	}

	/**
	 * @return the toolNumber
	 */
	public Integer getToolNumber() {
		return toolNumber;
	}

	/**
	 * @param toolNumber the toolNumber to set
	 */
	public void setToolNumber(Integer toolNumber) {
		this.toolNumber = toolNumber;
	}

}
