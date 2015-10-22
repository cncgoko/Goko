package org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;

public class StraightTraverseGeometryBuilder extends AbstractStraightGeometryBuilder<StraightFeedInstruction>{

	/**
	 * Constructor
	 */
	public StraightTraverseGeometryBuilder() {
		super(InstructionType.STRAIGHT_TRAVERSE);
	}
	
}
