package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer;

import java.util.List;
import java.util.function.Supplier;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer;

public abstract class AbstractInstructionColorizer implements IInstructionColorizer<GCodeContext, AbstractInstruction> {	
	private IOverlayRenderer overlay;

	/**
	 * @param overlay
	 */
	public AbstractInstructionColorizer(IOverlayRenderer overlay) {
		super();
		this.overlay = overlay;
	}	
	
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#initialize(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.InstructionProvider)
	 */
	@Override
	public void initialize(GCodeContext context, InstructionProvider instructionSet) throws GkException { }
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#initialize(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public void initialize(GCodeContext context, List<Supplier<InstructionProvider>> instructionSet) throws GkException { }
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#conclude()
	 */
	@Override
	public void conclude() throws GkException {	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#getOverlay()
	 */
	@Override
	public IOverlayRenderer getOverlay() {		
		return overlay;
	}

}
