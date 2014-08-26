/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.gcode.viewer.generator;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.gcode.viewer.generator.renderer.v2.ArcGCodeRenderer;
import org.goko.gcode.viewer.generator.renderer.v2.LinearGCodeRenderer;
import org.goko.gcode.viewer.generator.styler.GlGCodeStylerFactory;
import org.goko.gcode.viewer.generator.styler.impl.ArcGCodeStyler;
import org.goko.gcode.viewer.generator.styler.impl.LinearGCodeStyler;

public class GlGCodeRendererFactory {
	private Map<Class<?>, AbstractGCodeGlRenderer<? extends GCodeCommand>> mapRenderer;
	private GlGCodeStylerFactory stylerFactory;
	
	public GlGCodeRendererFactory() {
		this.mapRenderer = new HashMap<Class<?>, AbstractGCodeGlRenderer<?>>();
		
		this.stylerFactory = new GlGCodeStylerFactory();
		this.stylerFactory.registerRenderer(new LinearGCodeStyler());
		this.stylerFactory.registerRenderer(new ArcGCodeStyler());
		
		registerRenderer(new ArcGCodeRenderer());
		registerRenderer(new LinearGCodeRenderer());	
		
		
	}

	public void registerRenderer(AbstractGCodeGlRenderer<?> renderer){
		mapRenderer.put(renderer.getSupportedCommandClass(), renderer);
		renderer.setStylerFactory(stylerFactory);
	}

	public AbstractGCodeGlRenderer<GCodeCommand> getRenderer(GCodeCommand command) throws GkException{
		if(mapRenderer.containsKey(command.getClass())){
			return (AbstractGCodeGlRenderer<GCodeCommand>) mapRenderer.get(command.getClass());
		}
		return null;
	}

	public void render(GCodeCommand gCodeCommand, GL2 gl) throws GkException {
		AbstractGCodeGlRenderer<GCodeCommand> renderer = getRenderer(gCodeCommand);
		if(renderer != null){
			renderer.render(gCodeCommand, gl);
		}
	}

}
