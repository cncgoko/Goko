package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer;

import javax.vecmath.Color4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;

public class ArcDirectionColorizer extends AbstractInstructionColorizer {	
	private static final Color4f DEFAULT_COLOR = new Color4f(0.854f,0.854f,0.854f,0.9f);
	private static final Color4f CW_PLANE_COLOR = new Color4f(0.8f,0.0f,0.0f,0.9f);
	private static final Color4f CCW_PLANE_COLOR = new Color4f(0.0f,0.8f,0.0f,0.9f);
	
	/**
	 */
	public ArcDirectionColorizer() {
		super(null);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#getColor(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public Color4f getColor(GCodeContext context, AbstractInstruction instruction) throws GkException {
		Color4f color = DEFAULT_COLOR;
		if(instruction.getType() == InstructionType.ARC_FEED){
			ArcFeedInstruction arcFeedInstruction = (ArcFeedInstruction) instruction;
			if(arcFeedInstruction.isClockwise()){
				color = CW_PLANE_COLOR;
			}else{
				color = CCW_PLANE_COLOR;
			}
		}
		return color;
	}

}
