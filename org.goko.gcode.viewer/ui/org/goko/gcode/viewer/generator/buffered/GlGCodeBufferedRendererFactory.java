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
package org.goko.gcode.viewer.generator.buffered;

import java.util.HashMap;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld;

public class GlGCodeBufferedRendererFactory {
	private Map<String, AbstractGCodeGlRendererOld> mapRenderer;
	private Map<Integer, BufferedRenderingData> buffer;

	public GlGCodeBufferedRendererFactory() {
		this.mapRenderer = new HashMap<String, AbstractGCodeGlRendererOld>();
		this.buffer = new HashMap<Integer, BufferedRenderingData>();
		registerRenderer(new FeedrateLinearGCodeBufferedRenderer(buffer));
		registerRenderer(new RapidGCodeBufferedRenderer(buffer));
		registerRenderer(new CWArcGCodeBufferedRenderer(buffer));
		registerRenderer(new CCWArcGCodeBufferedRenderer(buffer));
	}


	public void registerRenderer(AbstractGCodeGlRendererOld renderer){
		mapRenderer.put(renderer.getSupportedMotionType(), renderer);
	}

	@SuppressWarnings("unchecked")
	public AbstractGCodeGlRendererOld getRenderer(GCodeContext context, GCodeCommand command) throws GkException{
		if(buffer.containsKey(command.getId())){
			return buffer.get(command.getId()).getRenderer();
		}else{
			if(context != null && mapRenderer.containsKey(context.getMotionMode())){
				return  mapRenderer.get(context.getMotionMode());
			}
		}
		return null;
	}

	public void clear(){
		buffer.clear();
	}
}
