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

import javax.media.opengl.GL2;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;

public abstract class AbstractGCodeGlRenderer {
	/** The rendered command */
	protected GCodeCommand renderedCommand;

	public abstract String getSupportedMotionType();

	public abstract void render(GCodeContext preContext,GCodeContext postContext, GCodeCommand command, GL2 gl);

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
}
