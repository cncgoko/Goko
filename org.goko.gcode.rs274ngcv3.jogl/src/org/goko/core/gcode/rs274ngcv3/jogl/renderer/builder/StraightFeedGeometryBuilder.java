package org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;

public class StraightFeedGeometryBuilder extends AbstractStraightGeometryBuilder<StraightFeedInstruction>{

	/**
	 * Constructor
	 */
	public StraightFeedGeometryBuilder() {
		super(InstructionType.STRAIGHT_FEED);
	}
	
}
