package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class UserLengthUnitsInstruction extends AbstractInstruction {
	/** The specified unit */
	private EnumUnit unit;	
	
	/** Constructor */
	public UserLengthUnitsInstruction(EnumUnit unit) {
		super(InstructionType.USER_LENGTH_UNITS);
		this.unit = unit;
	}

//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setUnit(unit);
//	}

	/**
	 * @return the unit
	 */
	public EnumUnit getUnit() {
		return unit;
	}

}
