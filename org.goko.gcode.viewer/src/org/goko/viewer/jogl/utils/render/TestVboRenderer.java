/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.viewer.jogl.utils.render;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import org.goko.core.common.exception.GkException;
import org.goko.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.viewer.jogl.shaders.ShaderLoader;
import org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

public class TestVboRenderer extends AbstractVboJoglRenderer {

	public TestVboRenderer() {
		super(GL.GL_TRIANGLES, AbstractVboJoglRenderer.VERTICES | AbstractVboJoglRenderer.COLORS);
	}

	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.LINE_SHADER);
	}
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		setVerticesCount(3);
		FloatBuffer vertices = FloatBuffer.allocate(getVerticesCount()*4);
		FloatBuffer colors 	 = FloatBuffer.allocate(getVerticesCount()*4);

		vertices.put(new float[]{0,0,0,1});
		vertices.put(new float[]{10,0,0,1});
		vertices.put(new float[]{0,10,0,1});

		colors.put(new float[]{1,0,0,1});
		colors.put(new float[]{0,1,0,1});
		colors.put(new float[]{0,0,1,1});

		setVerticesBuffer(vertices);
		setColorsBuffer(colors);
	}

}
