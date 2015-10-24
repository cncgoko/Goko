package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer;

import javax.vecmath.Color4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;

public class SelectedPlaneColorizer extends AbstractInstructionColorizer {	
	private static final Color4f DEFAULT_COLOR = new Color4f(0.854f,0.854f,0.854f,0.9f);
	private static final Color4f XY_PLANE_COLOR = new Color4f(0.8f,0.0f,0.0f,0.9f);
	private static final Color4f XZ_PLANE_COLOR = new Color4f(0.0f,0.8f,0.0f,0.9f);	
	private static final Color4f YZ_PLANE_COLOR = new Color4f(0.0f,0.0f,0.8f,0.9f);

	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#getColor(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public Color4f getColor(GCodeContext context, AbstractInstruction instruction) throws GkException {
		Color4f color = DEFAULT_COLOR;
		if(context.getPlane() == EnumPlane.XY_PLANE){
			color = XY_PLANE_COLOR;
		}else if(context.getPlane() == EnumPlane.XZ_PLANE){
			color = XZ_PLANE_COLOR;
		}else if(context.getPlane() == EnumPlane.YZ_PLANE){
			color = YZ_PLANE_COLOR;
		}
		return color;
	}

}
