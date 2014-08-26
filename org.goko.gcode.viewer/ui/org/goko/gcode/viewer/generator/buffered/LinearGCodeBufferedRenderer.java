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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.vecmath.Point3d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld;

public abstract class LinearGCodeBufferedRenderer extends AbstractGCodeGlRendererOld {


	private Map<Integer, BufferedRenderingData> buffer;

	public LinearGCodeBufferedRenderer(Map<Integer, BufferedRenderingData> buffer) {
		this.buffer = buffer;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld#render(org.goko.core.gcode.bean.GCodeCommand, javax.media.opengl.GL2)
	 */
	@Override
	public final void render(GCodeContext preContext,GCodeContext postContext, GCodeCommand command, GL2 gl) {
		setRenderedCommand(command);
		BufferedRenderingData renderingData = null;

		if(!buffer.containsKey(command.getId())){
			renderingData = addGeometryToBuffer(preContext, postContext, command);
			buffer.put(command.getId(), renderingData);
		}else{
			renderingData = buffer.get(command.getId());
		}
		List<Point3d> lstPoint = renderingData.getPoints();
		if(CollectionUtils.isNotEmpty(lstPoint)){

			enableLineStyle(gl);
			gl.glBegin(GL2.GL_LINE_STRIP);
			for (Point3d p : lstPoint) {
				setColor(gl);
				gl.glVertex3d(p.x, p.y, p.z);
			}
			gl.glEnd();
			disableLineStyle(gl);
		}
	}


	private BufferedRenderingData addGeometryToBuffer(GCodeContext preContext,GCodeContext postContext, GCodeCommand command) {
		List<Point3d> lst = new ArrayList<Point3d>();
		lst.add( new Point3d(preContext.getPosition().getX().doubleValue(), preContext.getPosition().getY().doubleValue(), preContext.getPosition().getZ().doubleValue()));

		Tuple6b tuple = postContext.getPosition();

		lst.add( new Point3d(tuple.getX().doubleValue(), tuple.getY().doubleValue(), tuple.getZ().doubleValue()));

		return new BufferedRenderingData(this, lst);
	}


	protected abstract void enableLineStyle(GL2 gl);

	protected abstract void disableLineStyle(GL2 gl);

	protected abstract void setColor(GL2 gl);
}
