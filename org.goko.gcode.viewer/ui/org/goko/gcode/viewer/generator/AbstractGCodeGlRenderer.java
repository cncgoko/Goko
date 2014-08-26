package org.goko.gcode.viewer.generator;

import javax.media.opengl.GL2;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.gcode.viewer.generator.styler.GlGCodeStylerFactory;

public abstract class AbstractGCodeGlRenderer<C extends GCodeCommand> {
	/** The rendered command */
	protected GCodeCommand renderedCommand;
	protected GlGCodeStylerFactory stylerFactory;

	public abstract Class<C> getSupportedCommandClass();


	public abstract void render(C command, GL2 gl) throws GkException;

	protected final void enableRenderingStyle(C command, GL2 gl) throws GkException{
		if(getStylerFactory() != null){
			stylerFactory.enableRenderingStyle(command, gl);
		}
	}
	protected final void disableRenderingStyle(C command, GL2 gl) throws GkException{
		if(getStylerFactory() != null){
			stylerFactory.disableRenderingStyle(command, gl);
		}
	}
	protected final void setVertexStyle(C command, GL2 gl) throws GkException{
		if(getStylerFactory() != null){
			stylerFactory.setVertexStyle(command, gl);
		}
	}

	/**
	 * @return the renderedCommand
	 */
	public GCodeCommand getRenderedCommand() {
		return renderedCommand;
	}

	/**
	 * @param renderedCommand the renderedCommand to set
	 */
	public void setRenderedCommand(GCodeCommand renderedCommand) {
		this.renderedCommand = renderedCommand;
	}


	/**
	 * @return the stylerFactory
	 */
	public GlGCodeStylerFactory getStylerFactory() {
		return stylerFactory;
	}


	/**
	 * @param stylerFactory the stylerFactory to set
	 */
	public void setStylerFactory(GlGCodeStylerFactory stylerFactory) {
		this.stylerFactory = stylerFactory;
	}
}
