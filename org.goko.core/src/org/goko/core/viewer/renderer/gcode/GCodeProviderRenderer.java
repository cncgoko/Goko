package org.goko.core.viewer.renderer.gcode;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionType;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandType;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.core.viewer.renderer.AbstractViewer3DRenderer;
import org.goko.core.viewer.renderer.IRendererProxy;

/**
 * Basic GCode renderer
 *
 * @author Psyko
 *
 */
public class GCodeProviderRenderer extends AbstractViewer3DRenderer {
	protected static final String ID = "org.goko.core.viewer.renderer.gcode.GCodeProviderRenderer";

	private LinearMotionRenderer linearRenderer;
	private ArcMotionRenderer 	 arcRenderer;
	private IGCodeProvider 		 provider;

	public GCodeProviderRenderer(IGCodeProvider provider) {
		this.provider = provider;
	}


	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/** {@inheritDoc}
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#render()
	 */
	@Override
	public void render(IRendererProxy proxy) throws GkException {
		for (GCodeCommand command : provider.getGCodeCommands()) {
			AbstractGCodeCommandRenderer<?> renderer = getRenderer(command);
			if(renderer != null){
				renderer.render(proxy);
			}
		}
	}

	protected AbstractGCodeCommandRenderer<?> getRenderer(GCodeCommand command){
		if(command.getType() == EnumGCodeCommandType.MOTION){
			MotionCommand motionCommand = (MotionCommand) command;
			if(motionCommand.getMotionType() == EnumGCodeCommandMotionType.LINEAR){
				linearRenderer.setGCodeCommand((LinearMotionCommand) command);
				return linearRenderer;
			}else if(motionCommand.getMotionType() == EnumGCodeCommandMotionType.ARC){
				arcRenderer.setGCodeCommand( (ArcMotionCommand) command);
				return arcRenderer;
			}
		}
		return null;
	}


}
