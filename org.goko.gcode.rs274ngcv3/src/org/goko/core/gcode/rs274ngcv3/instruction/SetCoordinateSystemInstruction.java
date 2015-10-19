package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.context.EnumCoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class SetCoordinateSystemInstruction extends AbstractInstruction {
	/** The target coordinate system */
	private EnumCoordinateSystem targetCoordinateSystem;

	/**
	 * Constructor 
	 * @param targetCoordinateSystem The target coordinate system 
	 */
	public SetCoordinateSystemInstruction(EnumCoordinateSystem targetCoordinateSystem) {
		super(InstructionType.SET_COORDINATE_SYSTEM);
		this.targetCoordinateSystem = targetCoordinateSystem;
	}

//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setCoordinateSystem(targetCoordinateSystem);
//	}

	/**
	 * @return the targetCoordinateSystem
	 */
	public EnumCoordinateSystem getTargetCoordinateSystem() {
		return targetCoordinateSystem;
	}

}
