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

package org.goko.tools.viewer.jogl.utils.render.internal;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;

import com.jogamp.common.nio.Buffers;

/**
 * An abstract VBO based renderer
 *
 * @author PsyKo
 *
 */
public abstract class DeprecatedAbstractVboJoglRenderer extends AbstractCoreJoglRenderer {
	protected int vao;
	protected int vbo[];
	private int internVerticesCount;
	private FloatBuffer vertices;
	private FloatBuffer colors;

	public DeprecatedAbstractVboJoglRenderer() {

	}

	protected void generateVbo(GL3 gl) throws GkException{
		vertices = generateVertices();
		vertices.rewind();

		colors = generateColors();
		colors.rewind();

		// JOGL Part now
		IntBuffer vaoBuffer = IntBuffer.allocate(1);
		gl.glGenVertexArrays(1, vaoBuffer);		
		vao = vaoBuffer.get();
		gl.glBindVertexArray(vao);
		vbo = new int[2];
		gl.glGenBuffers(2, vbo, 0);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, internVerticesCount*4*Buffers.SIZEOF_FLOAT, vertices, GL.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(0);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[1]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, internVerticesCount*4*Buffers.SIZEOF_FLOAT, colors, GL.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(1);
	}

	@Override
	public void destroy(){

	}
	@Override
	public void performDestroy(GL3 gl) throws GkException {
		// TODO Auto-generated method stub

	}

	protected abstract FloatBuffer generateColors() throws GkException;
	protected abstract FloatBuffer generateVertices() throws GkException;
	protected abstract int getVerticesCount() throws GkException;

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		this.setShaderProgram(ShaderLoader.loadShader(gl, EnumGokoShaderProgram.LINE_SHADER));
		this.internVerticesCount = getVerticesCount();
		generateVbo(gl);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performRender(javax.media.opengl.GL3)
	 */
	@Override
	protected void performRender(GL3 gl) throws GkException {
		if(!isInitialized()){
			initialize(gl);
		}
		gl.glBindVertexArray(this.vao);
		gl.glUseProgram(getShaderProgram());
		gl.glEnableVertexAttribArray(0);
		gl.glEnableVertexAttribArray(1);
	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[0]);

	    gl.glVertexAttribPointer(0,4, GL.GL_FLOAT, false, 0, 0);
	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[1]);

	    gl.glVertexAttribPointer(1,4, GL.GL_FLOAT, false, 0, 0);

	    gl.glDrawArrays(getRenderType(), 0, getVerticesCount());
	//    gl.glDisableVertexAttribArray(0);
	//    gl.glDisableVertexAttribArray(1);
	    gl.glUseProgram(0);
	}

	protected abstract int getRenderType();

	/**
	 * @return the vertices
	 */
	public FloatBuffer getVertices() {
		return vertices;
	}

	/**
	 * @param vertices the vertices to set
	 */
	public void setVertices(FloatBuffer vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the colors
	 */
	public FloatBuffer getColors() {
		return colors;
	}

	/**
	 * @param colors the colors to set
	 */
	public void setColors(FloatBuffer colors) {
		this.colors = colors;
	}

	/**
	 * @return the internVerticesCount
	 */
	protected int getInternVerticesCount() {
		return internVerticesCount;
	}

	/**
	 * @param internVerticesCount the internVerticesCount to set
	 */
	protected void setInternVerticesCount(int internVerticesCount) {
		this.internVerticesCount = internVerticesCount;
	}
}
