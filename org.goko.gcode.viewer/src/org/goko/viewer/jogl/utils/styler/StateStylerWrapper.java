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
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.viewer.jogl.service.JoglRendererProxy;

public class StateStylerWrapper<T extends GCodeCommand> implements IJoglGCodeCommandStyler<T> {
	private static Point3f COLOR_SENT 		= new Point3f(0.3f,0.3f,0.3f);

	private IJoglGCodeCommandStyler<T> baseStyler;

	/**
	 * Constructor
	 * @param styler the styler to wrap
	 */
	public StateStylerWrapper(IJoglGCodeCommandStyler<T> styler) {
		this.baseStyler = styler;
	}
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#enableRenderingStyle(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void enableRenderingStyle(T command, JoglRendererProxy proxy) throws GkException {
		baseStyler.enableRenderingStyle(command, proxy);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#disableRenderingStyle(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void disableRenderingStyle(T command, JoglRendererProxy proxy) throws GkException {
		baseStyler.disableRenderingStyle(command, proxy);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#setVertexColor(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void setVertexColor(T command, JoglRendererProxy proxy) throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#getVertexColor(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public Point3f getVertexColor(T command, JoglRendererProxy proxy) throws GkException {
		if(command.getState() != null && command.getState().isState(GCodeCommandState.SENT)){
			return COLOR_SENT;
		}else{
			return baseStyler.getVertexColor(command, proxy);
		}
	}

}
