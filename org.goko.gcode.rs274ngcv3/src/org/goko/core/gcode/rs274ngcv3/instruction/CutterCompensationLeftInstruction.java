package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter;

public class CutterCompensationLeftInstruction extends AbstractInstruction {
	private Length offset;	
	
	/** Constructor */
	public CutterCompensationLeftInstruction() {
		super(InstructionType.CUTTER_COMPENSATION_LEFT);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {

	}

	/**
	 * @return the offset
	 */
	public Length getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Length offset) {
		this.offset = offset;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction#accept(org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter)
	 */
	@Override
	public void accept(GCodeContext context, RS274InstructionVisitorAdapter visitor) throws GkException{
		visitor.visit(context, this);
	}

}
