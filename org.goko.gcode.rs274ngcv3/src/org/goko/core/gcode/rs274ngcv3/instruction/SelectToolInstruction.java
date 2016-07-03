package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class SelectToolInstruction extends AbstractInstruction {
	private Integer toolNumber;
	
	public SelectToolInstruction(Integer toolNumber) {
		super(InstructionType.SELECT_TOOL);
		this.toolNumber = toolNumber;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		context.setSelectedToolNumber(toolNumber);
	}

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

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((toolNumber == null) ? 0 : toolNumber.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelectToolInstruction other = (SelectToolInstruction) obj;
		if (toolNumber == null) {
			if (other.toolNumber != null)
				return false;
		} else if (!toolNumber.equals(other.toolNumber))
			return false;
		return true;
	}

	
}
