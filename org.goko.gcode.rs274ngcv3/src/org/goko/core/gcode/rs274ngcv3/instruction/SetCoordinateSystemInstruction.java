package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.EnumCoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
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

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		context.setCoordinateSystem(targetCoordinateSystem);
	}

	/**
	 * @return the targetCoordinateSystem
	 */
	public EnumCoordinateSystem getTargetCoordinateSystem() {
		return targetCoordinateSystem;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((targetCoordinateSystem == null) ? 0 : targetCoordinateSystem.hashCode());
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
		SetCoordinateSystemInstruction other = (SetCoordinateSystemInstruction) obj;
		if (targetCoordinateSystem != other.targetCoordinateSystem)
			return false;
		return true;
	}

}
