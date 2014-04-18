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

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.gcode.viewer.generator.renderer.CWArcGCodeRenderer;
import org.goko.gcode.viewer.generator.renderer.FeedrateLinearGCodeRenderer;
import org.goko.gcode.viewer.generator.renderer.RapidGCodeRenderer;

public class GlGCodeRendererFactory {
	private Map<Class<?>, AbstractGCodeGlRenderer<?>> mapRenderer;

	public GlGCodeRendererFactory() {
		this.mapRenderer = new HashMap<Class<?>, AbstractGCodeGlRenderer<?>>();
		registerRenderer(new FeedrateLinearGCodeRenderer());
		registerRenderer(new RapidGCodeRenderer());
		registerRenderer(new CWArcGCodeRenderer());
	}

	public void registerRenderer(AbstractGCodeGlRenderer<?> renderer){
		mapRenderer.put(renderer.getRenderedCommandClass(), renderer);
	}
	@SuppressWarnings("unchecked")
	public <T extends GCodeCommand> AbstractGCodeGlRenderer<T> getRenderer(T command) throws GkException{
		if(mapRenderer.containsKey(command.getClass())){
			return (AbstractGCodeGlRenderer<T>) mapRenderer.get(command.getClass());
		}
		return null;
	}

}
