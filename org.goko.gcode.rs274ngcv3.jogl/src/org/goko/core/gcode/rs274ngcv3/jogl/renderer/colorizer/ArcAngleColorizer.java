package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer;

import javax.vecmath.Color4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.Units;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.InstructionUtils;
import org.goko.core.math.Arc3b;

public class ArcAngleColorizer extends AbstractInstructionColorizer {	
	private static final Color4f DEFAULT_COLOR = new Color4f(0.0f,0.0f,0.0f,0.0f);

	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#getColor(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public Color4f getColor(GCodeContext context, AbstractInstruction instruction) throws GkException {
		Color4f color = DEFAULT_COLOR;
		if(instruction.getType() == InstructionType.ARC_FEED){
			ArcFeedInstruction arcFeedInstruction = (ArcFeedInstruction) instruction;
			Arc3b arc = InstructionUtils.getArc(context, arcFeedInstruction);
			float c = Math.abs((float) (arc.getAngle().doubleValue(Units.RADIAN) / (2*Math.PI)));			
			
			color = new Color4f(c,c,c,1f);
			
		}
		return color;
	}

}
