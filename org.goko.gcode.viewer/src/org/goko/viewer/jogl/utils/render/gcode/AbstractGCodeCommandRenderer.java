package org.goko.viewer.jogl.utils.render.gcode;

import java.lang.ref.WeakReference;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.viewer.jogl.utils.render.AbstractJoglRenderer;
import org.goko.viewer.jogl.utils.styler.AbstractGCodeCommandStyler;
import org.goko.viewer.jogl.utils.styler.DefaultGCodeCommandStyler;

public abstract class AbstractGCodeCommandRenderer<T extends GCodeCommand> extends AbstractJoglRenderer {
	private WeakReference<T> commandRef;
	private AbstractGCodeCommandStyler styler;

	public AbstractGCodeCommandRenderer() {
		styler = new DefaultGCodeCommandStyler();
	}

	protected T getGCodeCommand(){
		if(commandRef != null ){
			return commandRef.get();
		}
		return null;
	}

	public void setGCodeCommand(T command){
		this.commandRef = new WeakReference<T>(command);
	}

	/**
	 * @return the styler
	 */
	public AbstractGCodeCommandStyler getStyler() {
		return styler;
	}

	/**
	 * @param styler the styler to set
	 */
	public void setStyler(AbstractGCodeCommandStyler styler) {
		this.styler = styler;
	}
}
