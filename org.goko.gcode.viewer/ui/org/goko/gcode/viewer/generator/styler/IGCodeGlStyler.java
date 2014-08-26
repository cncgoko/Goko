package org.goko.gcode.viewer.generator.styler;

import javax.media.opengl.GL2;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;

public interface IGCodeGlStyler<T extends GCodeCommand> {

	/**
	 * Enable the rendering style to the given GL2
	 * @param command the command to render
	 * @param gl the target gl2
	 * @throws GkException GkException
	 */
	public void enableRenderingStyle(T command, GL2 gl) throws GkException;
	/**
	 * Disable the rendering style to the given GL2
	 * @param command the command to render
	 * @param gl the target gl2
	 * @throws GkException GkException
	 */
	public void disableRenderingStyle(T command, GL2 gl) throws GkException;
	/**
	 * Set the rendering style for the given vertex
	 * @param command the command to render
	 * @param gl the target gl2
	 * @throws GkException GkException
	 */
	public void setVertexStyle(T command, GL2 gl) throws GkException;

	/**
	 * Returns the supported class
	 * @return a class
	 */
	public Class<T> getSupportedCommandClass();
}
