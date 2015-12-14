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
package org.goko.tools.viewer.jogl.utils.render;

import javax.media.opengl.GL3;

import org.goko.core.common.exception.GkException;
import org.goko.core.viewer.renderer.IViewer3DRenderer;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer;
import org.goko.tools.viewer.jogl.service.JoglRendererProxy;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;

import com.jogamp.opengl.util.PMVMatrix;

public class JoglRendererWrapper extends AbstractCoreJoglRenderer {
	private IViewer3DRenderer baseRenderer;
	private PMVMatrix modelMatrix;
	private JoglRendererProxy proxy;
	/**
	 * Constructor
	 * @param baseRenderer the viewer to wrap
	 */
	public JoglRendererWrapper(IViewer3DRenderer baseRenderer) {
		super();
		this.baseRenderer = baseRenderer;
		this.proxy = new JoglRendererProxy(null);
		this.modelMatrix = new PMVMatrix();
		this.modelMatrix.glLoadIdentity();
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#getCode()
	 */
	@Override
	public String getCode() {
		return baseRenderer.getCode();
	}

	@Override
	public void performDestroy(GL3 gl) throws GkException {
		// TODO Auto-generated method stub

	}
	@Override
	protected void performRender(GL3 gl) throws GkException {
		proxy.setGl(gl);
		baseRenderer.render(proxy);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		this.setShaderProgram(ShaderLoader.loadShader(gl, EnumGokoShaderProgram.LINE_SHADER));

	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseRenderer == null) ? 0 : baseRenderer.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		JoglRendererWrapper other = (JoglRendererWrapper) obj;
		if (baseRenderer == null) {
			if (other.baseRenderer != null) {
				return false;
			}
		} else if (!baseRenderer.equals(other.baseRenderer)) {
			return false;
		}
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#destroy()
	 */
	@Override
	public void destroy() throws GkException {
		// TODO Auto-generated method stub

	}
}
