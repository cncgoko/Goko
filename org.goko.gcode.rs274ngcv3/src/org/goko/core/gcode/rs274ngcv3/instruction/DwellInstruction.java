package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class DwellInstruction extends AbstractInstruction {
	/** The time to pause */
	private int seconds;
	
	/** Constructor */
	public DwellInstruction(int seconds) {
		super(InstructionType.DWELL);
		this.seconds = seconds;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the seconds
	 */
	public int getSeconds() {
		return seconds;
	}

}
