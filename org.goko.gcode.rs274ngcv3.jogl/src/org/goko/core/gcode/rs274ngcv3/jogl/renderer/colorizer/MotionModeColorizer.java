package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer;

import javax.vecmath.Color4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;

public class MotionModeColorizer extends AbstractInstructionColorizer {
	private static final Color4f DEFAULT_COLOR = new Color4f(0.58f,0.58f,0.58f,0.9f);
//	private static final Color4f RAPID_COLOR = new Color4f(0.854f,0.0f,0.0f,0.9f);
	private static final Color4f RAPID_COLOR = new Color4f(1f,0.77f,0.04f, 0.75f);
	private static final Color4f FEEDRATE_COLOR = new Color4f(0.14f,0.33f,0.80f,0.9f);	
	private static final Color4f ARC_COLOR = new Color4f(0,0.86f,0,0.9f);
	private static final Color4f PROBE_COLOR = new Color4f(0.80f,0.40f,1f,0.9f);

	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#getColor(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public Color4f getColor(GCodeContext token, AbstractInstruction instruction) throws GkException {
		Color4f color = DEFAULT_COLOR;
		if(instruction.getType() == InstructionType.ARC_FEED){
			color = ARC_COLOR;
		}else if(instruction.getType() == InstructionType.STRAIGHT_FEED){
			color = FEEDRATE_COLOR;
		}else if(instruction.getType() == InstructionType.STRAIGHT_TRAVERSE){
			color = RAPID_COLOR;
		}else if(instruction.getType() == InstructionType.STRAIGHT_PROBE){
			color = PROBE_COLOR;
		}
		return color;
	}

}
