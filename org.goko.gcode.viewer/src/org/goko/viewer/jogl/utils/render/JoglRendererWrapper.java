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
package org.goko.viewer.jogl.utils.render;

import org.goko.core.common.exception.GkException;
import org.goko.core.viewer.renderer.IViewer3DRenderer;
import org.goko.viewer.jogl.service.JoglRendererProxy;

public class JoglRendererWrapper implements IJoglRenderer {
	private IViewer3DRenderer baseRenderer;
	/**
	 * Constructor
	 * @param baseRenderer the viewer to wrap
	 */
	public JoglRendererWrapper(IViewer3DRenderer baseRenderer) {
		super();
		this.baseRenderer = baseRenderer;
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#getId()
	 */
	@Override
	public String getId() {
		return baseRenderer.getId();
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#render(org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void render(JoglRendererProxy proxy) throws GkException {
		baseRenderer.render(proxy);
	}

}
