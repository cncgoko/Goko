package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer;

import javax.vecmath.Color4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.EnumSpindleMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.overlay.SpindleStateColorizerOverlay;

public class SpindleStateColorizer extends AbstractInstructionColorizer {	
	private static final Color4f SPINDLE_CW_COLOR  = new Color4f(1f,0.48f,0.0f,0.9f);
	private static final Color4f SPINDLE_CCW_COLOR = new Color4f(0.59f,0.29f,0.79f,0.9f);
	private static final Color4f SPINDLE_OFF_COLOR = new Color4f(0.8f,0.8f,0.8f,0.9f);	

	/**
	 */
	public SpindleStateColorizer() {
		super(new SpindleStateColorizerOverlay());
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#getColor(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public Color4f getColor(GCodeContext context, AbstractInstruction instruction) throws GkException {
		Color4f color = SPINDLE_OFF_COLOR;
		if(context.getSpindleMode() == EnumSpindleMode.ON_COUNTERCLOCKWISE){
			color = SPINDLE_CCW_COLOR;
		}else if(context.getSpindleMode() == EnumSpindleMode.ON_CLOCKWISE){
			color = SPINDLE_CW_COLOR;
		}
		return color;
	}
}
