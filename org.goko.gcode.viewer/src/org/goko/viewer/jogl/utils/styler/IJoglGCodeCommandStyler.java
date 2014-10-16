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
package org.goko.viewer.jogl.utils.styler;

import javax.vecmath.Point3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.viewer.jogl.service.JoglRendererProxy;

public interface IJoglGCodeCommandStyler<T extends GCodeCommand>{

	void enableRenderingStyle(T command, JoglRendererProxy proxy) throws GkException;

	void disableRenderingStyle(T command, JoglRendererProxy proxy) throws GkException;

	void setVertexColor(T command,JoglRendererProxy proxy) throws GkException;

	Point3f getVertexColor(T command,JoglRendererProxy proxy) throws GkException;
}
