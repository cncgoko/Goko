package org.goko.viewer.jogl.utils.render.gcode;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.viewer.jogl.service.JoglRendererProxy;
import org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler;

public abstract class AbstractGCodeCommandRenderer<T extends GCodeCommand> {

	public abstract void render(T command, JoglRendererProxy proxy, IJoglGCodeCommandStyler<T> styler ) throws GkException;

}
