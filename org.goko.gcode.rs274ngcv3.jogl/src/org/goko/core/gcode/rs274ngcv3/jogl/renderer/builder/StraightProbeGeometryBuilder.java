package org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightProbeInstruction;

public class StraightProbeGeometryBuilder extends AbstractStraightGeometryBuilder<StraightProbeInstruction>{

	/**
	 * Constructor
	 */
	public StraightProbeGeometryBuilder() {
		super(InstructionType.STRAIGHT_PROBE);
	}
	
}
