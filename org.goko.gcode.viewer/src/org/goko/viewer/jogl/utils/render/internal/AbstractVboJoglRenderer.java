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

package org.goko.viewer.jogl.utils.render.internal;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.viewer.jogl.service.AbstractCoreJoglRenderer;

import com.jogamp.common.nio.Buffers;

public abstract class AbstractVboJoglRenderer extends AbstractCoreJoglRenderer{
	protected static final int VERTICES = 1 << 1;
	private static final int VERTICES_LAYOUT = 0;
	protected static final int COLORS = 1 << 2;
	private static final int COLORS_LAYOUT = 1;
	protected static final int UVS = 1 << 3;
	private static final int UVS_LAYOUT = 2;

	private Integer verticesCount;
	private boolean useVerticesBuffer;
	private FloatBuffer verticesBuffer;
	private boolean useColorsBuffer;
	private FloatBuffer colorsBuffer;
	private boolean useUvsBuffer;
	private FloatBuffer uvsBuffer;
	/** The type of primitive used for render */
	private Integer renderPrimitive;
	/** The id of the VAO in use */
	private Integer vertexArrayObject;
	/** The id of the VBO in use for the vertices */
	private Integer verticesBufferObject;
	/** The id of the VBO in use for the colors */
	private Integer colorsBufferObject;
	/** The id of the VBO in use for the uvs */
	private Integer uvsBufferObject;
	/** Request for buffer update */
	private boolean updateBuffer;


	protected AbstractVboJoglRenderer(int renderPrimitive, int usedBuffers){
		this.renderPrimitive = renderPrimitive;
		this.useVerticesBuffer = (usedBuffers & VERTICES) == VERTICES;
		this.useColorsBuffer = (usedBuffers & COLORS) == COLORS;
		this.useUvsBuffer = (usedBuffers & UVS) == UVS;
	}

	/**
	 * Method allowing to build the geometry of this renderer
	 * @throws GkException
	 */
	protected abstract void buildGeometry() throws GkException;

	/**
	 * Method allowing to load the shader program
	 * @return the identifier of the shader program
	 * @throws GkException
	 */
	protected abstract int loadShaderProgram(GL3 gl) throws GkException;

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		buildGeometry();
		setShaderProgram(loadShaderProgram(gl));
		initializeVertexArrayObject(gl);
		initializeBufferObjects(gl);
	}

	protected void updateBufferObjects() throws GkException {
		this.updateBuffer = true;
	}
	/**
	 * Performs the update in the vertex buffers
	 * @param gl the GL
	 * @throws GkException GkException
	 */
	protected void performUpdateBufferObjects(GL3 gl) throws GkException {
		buildGeometry();
		initializeBufferObjects(gl);
		this.updateBuffer = false;
	}
	/**
	 * Initializes and bind the several vertex buffer objects
	 * @param gl the GL
	 * @throws GkException GkException
	 */
	private void initializeBufferObjects(GL3 gl) throws GkException {
		initializeVerticesBufferObject(gl);
		initializeColorsBufferObject(gl);
		initializeUvsBufferObject(gl);
		initializeAdditionalBufferObjects(gl);
	}
	/**
	 * Performs the initialisation of any additional buffer in subclasses
	 * @param gl the GL context
	 * @throws GkException GkException
	 */
	protected void initializeAdditionalBufferObjects(GL3 gl) throws GkException {
	}
	/**
	 * Initializes and bind the buffer object for vertices
	 * @param gl the GL
	 * @throws GkException GkException
	 */
	private void initializeVerticesBufferObject(GL3 gl) throws GkException {
		if(!useVerticesBuffer){
			return;	// Vertices not used, nothing to do
		}
		if(getVerticesBuffer() == null){
			throw new GkTechnicalException(this+" Vertices buffer not initialized.");
		}
		// Everything looks fine, we can initialize
		if(this.verticesBufferObject == null){
			int[] vbo = new int[1];
			gl.glGenBuffers(1, vbo, 0);
			this.verticesBufferObject = vbo[0];
		}
		// Make sure we take everything
		getVerticesBuffer().rewind();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, verticesBufferObject);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getVerticesCount()*4*Buffers.SIZEOF_FLOAT, getVerticesBuffer(), GL.GL_DYNAMIC_DRAW);
		gl.glEnableVertexAttribArray(VERTICES_LAYOUT);
	}

	/**
	 * Initializes and bind the buffer object for Colors
	 * @param gl the GL
	 * @throws GkException GkException
	 */
	private void initializeColorsBufferObject(GL3 gl) throws GkException {
		if(!useColorsBuffer){
			return;	// Vertices not used, nothing to do
		}
		if(getColorsBuffer() == null){
			throw new GkTechnicalException(" Colors buffer not initialized.");
		}
		// Everything looks fine, we can initialize
		if(this.colorsBufferObject == null){
			int[] vbo = new int[1];
			gl.glGenBuffers(1, vbo, 0);
			this.colorsBufferObject = vbo[0];
		}
		// Make sure we take everything
		getColorsBuffer().rewind();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, colorsBufferObject);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getVerticesCount()*4*Buffers.SIZEOF_FLOAT, getColorsBuffer(), GL.GL_DYNAMIC_DRAW);
		gl.glEnableVertexAttribArray(COLORS_LAYOUT);
	}

	/**
	 * Initializes and bind the buffer object for UVs
	 * @param gl the GL
	 * @throws GkException GkException
	 */
	private void initializeUvsBufferObject(GL3 gl) throws GkException {
		if(!useUvsBuffer){
			return;	// Vertices not used, nothing to do
		}
		if(getUvsBuffer() == null){
			throw new GkTechnicalException(" Uvs buffer not initialized.");
		}
		// Everything looks fine, we can initialize
		if(this.uvsBufferObject ==null){
		int[] vbo = new int[1];
			gl.glGenBuffers(1, vbo, 0);
			this.uvsBufferObject = vbo[0];
		}
		// Make sure we take everything
		getUvsBuffer().rewind();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, uvsBufferObject);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getVerticesCount()*2*Buffers.SIZEOF_FLOAT, getUvsBuffer(), GL.GL_DYNAMIC_DRAW);
		gl.glEnableVertexAttribArray(UVS_LAYOUT);
	}

	/**
	 * Initializes and bind the main vertex array object
	 * @param gl the GL
	 */
	private void initializeVertexArrayObject(GL3 gl) {
		IntBuffer vao = IntBuffer.allocate(1);
		gl.glGenVertexArrays(1, vao);
		this.vertexArrayObject = vao.get(0);
		gl.glBindVertexArray(this.vertexArrayObject);

	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.AbstractCoreJoglRenderer#performRender(javax.media.opengl.GL3)
	 */
	@Override
	protected void performRender(GL3 gl) throws GkException {
		if(!isInitialized()){
			initialize(gl);
		}
		if(updateBuffer){
			performUpdateBufferObjects(gl);
		}
		gl.glUseProgram(getShaderProgram());

		if(useVerticesBuffer){
			gl.glEnableVertexAttribArray(VERTICES_LAYOUT);
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, verticesBufferObject);
			gl.glVertexAttribPointer(VERTICES_LAYOUT, 4, GL.GL_FLOAT, false, 0, 0);
		}
		if(useColorsBuffer){
			gl.glEnableVertexAttribArray(COLORS_LAYOUT);
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, colorsBufferObject);
			gl.glVertexAttribPointer(COLORS_LAYOUT, 4, GL.GL_FLOAT, false, 0, 0);
		}
		if(useUvsBuffer){
			gl.glEnableVertexAttribArray(UVS_LAYOUT);
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, uvsBufferObject);
			gl.glVertexAttribPointer(UVS_LAYOUT, 2, GL.GL_FLOAT, false, 0, 0);
		}
		enableAdditionalVertexAttribArray(gl);

		gl.glDrawArrays(getRenderPrimitive(), 0, getVerticesCount());

	    disableAdditionalVertexAttribArray(gl);
	    if(useVerticesBuffer){
	    	gl.glDisableVertexAttribArray(VERTICES_LAYOUT);
	    }
	    if(useColorsBuffer){
	    	gl.glDisableVertexAttribArray(COLORS_LAYOUT);
	    }
	    if(useUvsBuffer){
	    	gl.glDisableVertexAttribArray(UVS_LAYOUT);
	    }
	    gl.glUseProgram(0);
	}

	protected void enableAdditionalVertexAttribArray(GL3 gl) throws GkException {

	}
	protected void disableAdditionalVertexAttribArray(GL3 gl) throws GkException {

	}
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.ICoreJoglRenderer#performDestroy(javax.media.opengl.GL3)
	 */
	@Override
	public void performDestroy(GL3 gl) throws GkException {
		if(!isInitialized()){
			return;
		}
		List<Integer> lstBuffers = new ArrayList<Integer>();

		if(useVerticesBuffer){
			lstBuffers.add(verticesBufferObject);
		}
		if(useColorsBuffer){
			lstBuffers.add(colorsBufferObject);
		}
		if(useUvsBuffer){
			lstBuffers.add(uvsBufferObject);
		}

		IntBuffer buffers = IntBuffer.allocate(lstBuffers.size());
		for (Integer integer : lstBuffers) {
			buffers.put(integer);
		}
		buffers.rewind();
		gl.glDeleteBuffers(lstBuffers.size(),buffers);
		IntBuffer intBuffer = IntBuffer.wrap(new int[]{vertexArrayObject});
		gl.glDeleteVertexArrays(1, intBuffer);
	}

	/**
	 * Update the buffers
	 */
	public void update(){
		this.updateBuffer = true;
	}
	/**
	 * @return the verticesBuffer
	 */
	protected FloatBuffer getVerticesBuffer() {
		return verticesBuffer;
	}

	/**
	 * @param verticesBuffer the verticesBuffer to set
	 */
	protected void setVerticesBuffer(FloatBuffer verticesBuffer) {
		this.verticesBuffer = verticesBuffer;
	}

	/**
	 * @return the colorsBuffer
	 */
	protected FloatBuffer getColorsBuffer() {
		return colorsBuffer;
	}

	/**
	 * @param colorsBuffer the colorsBuffer to set
	 */
	protected void setColorsBuffer(FloatBuffer colorsBuffer) {
		this.colorsBuffer = colorsBuffer;
	}

	/**
	 * @return the uvsBuffer
	 */
	protected FloatBuffer getUvsBuffer() {
		return uvsBuffer;
	}

	/**
	 * @param uvsBuffer the uvsBuffer to set
	 */
	protected void setUvsBuffer(FloatBuffer uvsBuffer) {
		this.uvsBuffer = uvsBuffer;
	}

	/**
	 * @return the verticesCount
	 */
	protected Integer getVerticesCount() {
		return verticesCount;
	}

	/**
	 * @param verticesCount the verticesCount to set
	 * @throws GkFunctionalException GkFunctionalException
	 */
	protected void setVerticesCount(Integer verticesCount) throws GkFunctionalException {
		if(!ObjectUtils.equals(verticesCount, this.verticesCount) && isInitialized()){
			throw new GkFunctionalException("Cannot change vertices count once initialized");
		}
		this.verticesCount = verticesCount;
	}

	/**
	 * @return the renderPrimitive
	 */
	protected Integer getRenderPrimitive() {
		return renderPrimitive;
	}

	/**
	 * @param renderPrimitive the renderPrimitive to set
	 */
	protected void setRenderPrimitive(Integer renderPrimitive) {
		this.renderPrimitive = renderPrimitive;
	}

	/**
	 * @return the updateBuffer
	 */
	protected boolean isUpdateBuffer() {
		return updateBuffer;
	}

	/**
	 * @param updateBuffer the updateBuffer to set
	 */
	protected void setUpdateBuffer(boolean updateBuffer) {
		this.updateBuffer = updateBuffer;
	}


}
