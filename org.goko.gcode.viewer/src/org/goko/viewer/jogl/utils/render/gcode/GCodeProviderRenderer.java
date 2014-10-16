package org.goko.viewer.jogl.utils.render.gcode;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeCommandVisitor;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.CommentCommand;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.core.gcode.bean.commands.RawCommand;
import org.goko.core.gcode.bean.commands.SettingCommand;
import org.goko.viewer.jogl.service.JoglRendererProxy;
import org.goko.viewer.jogl.utils.render.IJoglRenderer;
import org.goko.viewer.jogl.utils.styler.ArcCommandStyler;
import org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler;
import org.goko.viewer.jogl.utils.styler.LinearCommandStyler;
import org.goko.viewer.jogl.utils.styler.StateStylerWrapper;

/**
 * Basic GCode renderer
 *
 * @author Psyko
 *
 */
public class GCodeProviderRenderer implements IJoglRenderer,IGCodeCommandVisitor {
	protected static final String ID = "org.goko.viewer.jogl.utils.render.gcode.GCodeProviderRenderer";
	private LinearMotionRenderer linearRenderer;
	private ArcMotionRenderer 	 arcRenderer;

	private IJoglGCodeCommandStyler<LinearMotionCommand>  	linearStyler;
	private IJoglGCodeCommandStyler<ArcMotionCommand>  		arcStyler;

	private IGCodeProvider 		 provider;
	private JoglRendererProxy	 proxy;


	public GCodeProviderRenderer(IGCodeProvider provider) {
		this.provider = provider;
		this.linearRenderer = new LinearMotionRenderer();
		this.linearStyler = new StateStylerWrapper<LinearMotionCommand>(new LinearCommandStyler());
		this.arcRenderer = new ArcMotionRenderer();
		this.arcStyler = new StateStylerWrapper<ArcMotionCommand>(new ArcCommandStyler());
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
	public void render(JoglRendererProxy proxy) throws GkException {
		this.proxy = proxy;
		if(CollectionUtils.isNotEmpty(provider.getGCodeCommands())){
			for (GCodeCommand command : provider.getGCodeCommands()) {
				command.accept(this);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.RawCommand)
	 */
	@Override
	public void visit(RawCommand command) throws GkException {
		// Nothing to do here
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.CommentCommand)
	 */
	@Override
	public void visit(CommentCommand command) throws GkException {
		// Nothing to do here
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.SettingCommand)
	 */
	@Override
	public void visit(SettingCommand command) throws GkException {
		// Nothing to do here
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.MotionCommand)
	 */
	@Override
	public void visit(MotionCommand command) throws GkException {
		// Nothing to do here
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.LinearMotionCommand)
	 */
	@Override
	public void visit(LinearMotionCommand command) throws GkException {
		linearRenderer.render(command, proxy, linearStyler );
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.ArcMotionCommand)
	 */
	@Override
	public void visit(ArcMotionCommand command) throws GkException {
		arcRenderer.render(command, proxy, arcStyler);
	}


}
