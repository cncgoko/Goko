package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter;

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

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		context.setPlane(plane);
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((plane == null) ? 0 : plane.hashCode());
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
		SelectPlaneInstruction other = (SelectPlaneInstruction) obj;
		if (plane != other.plane)
			return false;
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction#accept(org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter)
	 */
	@Override
	public void accept(GCodeContext context, RS274InstructionVisitorAdapter visitor) throws GkException{
		visitor.visit(context, this);
	}
}
