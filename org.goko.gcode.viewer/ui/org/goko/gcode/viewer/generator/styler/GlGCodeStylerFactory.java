package org.goko.gcode.viewer.generator.styler;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;

public class GlGCodeStylerFactory {
	private Map<Class<?>, IGCodeGlStyler<? extends GCodeCommand>> mapStyler;

	public GlGCodeStylerFactory() {
		mapStyler = new HashMap<Class<?>, IGCodeGlStyler<? extends GCodeCommand>>();
	}

	public <T extends GCodeCommand> void enableRenderingStyle(T command, GL2 gl) throws GkException{
		IGCodeGlStyler<T> styler = findRenderer(command);
		styler.enableRenderingStyle(command, gl);
	}
	public <T extends GCodeCommand> void disableRenderingStyle(T command, GL2 gl) throws GkException{
		IGCodeGlStyler<T> styler = findRenderer(command);
		styler.disableRenderingStyle(command, gl);
	}
	public <T extends GCodeCommand> void setVertexStyle(T command, GL2 gl) throws GkException{
		IGCodeGlStyler<T> styler = findRenderer(command);
		styler.setVertexStyle(command, gl);
	}
	public <T extends GCodeCommand> void registerRenderer(IGCodeGlStyler<T> styler){
		mapStyler.put(styler.getSupportedCommandClass(), styler);
	}

	public <T extends GCodeCommand> IGCodeGlStyler<T> findRenderer(T command) throws GkException{
		if(mapStyler.containsKey(command.getClass())){
			return (IGCodeGlStyler<T>) mapStyler.get(command.getClass());
		}
		return null;
	}

}
