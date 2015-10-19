package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class SelectPlaneInstruction extends AbstractInstruction {
	/** The target plane of this instruction */
	private EnumPlane plane;
	
	/**
	 * Seklect plane intruction
	 * @param type InstructionType
	 * @param plane EnumPlane
	 */
	public SelectPlaneInstruction(EnumPlane plane) {
		super(InstructionType.SELECT_PLANE);
		this.plane = plane;
	}

	/** Constructor */
	public SelectPlaneInstruction() {
		super(InstructionType.SELECT_PLANE);
	}

	/**
	 * @return the plane
	 */
	public EnumPlane getPlane() {
		return plane;
	}

	/**
	 * @param plane the plane to set
	 */
	public void setPlane(EnumPlane plane) {
		this.plane = plane;
	}

//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setPlane(plane);
//	}

}
