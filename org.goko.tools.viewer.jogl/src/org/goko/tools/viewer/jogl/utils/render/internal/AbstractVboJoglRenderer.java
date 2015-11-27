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

import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer;

import com.jogamp.common.nio.Buffers;

public abstract class AbstractVboJoglRenderer extends AbstractCoreJoglRenderer{
	protected static final int VERTICES = 1 << 1;
	private static final int VERTICES_LAYOUT = 0;
	protected static final int COLORS = 1 << 2;
	private static final int COLORS_LAYOUT = 1;
	protected static final int UVS = 1 << 3;
	private static final int UVS_LAYOUT = 2;
	protected static final int NORMALS = 1 << 4;
	private static final int NORMALS_LAYOUT = 3;
	
	private Integer verticesCount;
	private boolean useVerticesBuffer;
	private FloatBuffer verticesBuffer;
	private boolean useColorsBuffer;
	private FloatBuffer colorsBuffer;
	private boolean useUvsBuffer;
	private FloatBuffer uvsBuffer;
	private boolean useNormalsBuffer;
	private FloatBuffer normalsBuffer;
	/** The type of primitive used for render */
	private Integer renderPrimitive;
	/** The id of the VAO in use */
	private Integer vertexArrayObject;	
	/** Request for buffer update */
	private boolean updateBuffer;
	/** Request for geometry update */
	private boolean updateGeometry;
	/** Interleaved buffer */
	private FloatBuffer interleavedBuffer;
	/** The id of the VBO in use for the vertices data */
	private Integer interleavedBufferObject;
	/** Stride in interleaved buffer */
	private int stride;
	
	protected AbstractVboJoglRenderer(int renderPrimitive, int usedBuffers){
		this.renderPrimitive = renderPrimitive;
		this.useVerticesBuffer = (usedBuffers & VERTICES) == VERTICES;
		this.useColorsBuffer = (usedBuffers & COLORS) == COLORS;
		this.useUvsBuffer = (usedBuffers & UVS) == UVS;
		this.useNormalsBuffer = (usedBuffers & NORMALS) == NORMALS;
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
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
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
	
	protected void updateGeometry() throws GkException {
		this.updateGeometry = true;
	}
	/**
	 * Performs the update of the geometry
	 * @param gl the GL
	 * @throws GkException GkException
	 */
	protected void performUpdateGeometry(GL3 gl) throws GkException {		
		//////////// TEST
//		IntBuffer buffers = IntBuffer.wrap(new int[]{interleavedBufferObject});
//		gl.glDeleteBuffers(1,buffers);
//		interleavedBufferObject = null;
//		
//		IntBuffer intBuffer = IntBuffer.wrap(new int[]{vertexArrayObject});
//		gl.glDeleteVertexArrays(1, intBuffer);
//		vertexArrayObject = null;
		//////////// END of TEST
		this.updateGeometry = false;
		buildGeometry();
		performUpdateBufferObjects(gl);
		this.updateGeometry = false;
	}
	/**
	 * Performs the update in the vertex buffers
	 * @param gl the GL
	 * @throws GkException GkException
	 */
	protected void performUpdateBufferObjects(GL3 gl) throws GkException {		
		initializeBufferObjects(gl);
		this.updateBuffer = false;
	}
	/**
	 * Initializes and bind the several vertex buffer objects
	 * @param gl the GL
	 * @throws GkException GkException
	 */
	protected void initializeBufferObjects(GL3 gl) throws GkException {
		if(this.interleavedBufferObject == null){
			int[] vbo = new int[1];
			gl.glGenBuffers(1, vbo, 0);
			this.interleavedBufferObject = vbo[0];
		}
		buildInterleavedBuffer();
		interleavedBuffer.rewind();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, interleavedBufferObject);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getVerticesCount()*stride*Buffers.SIZEOF_FLOAT, interleavedBuffer, GL.GL_DYNAMIC_DRAW);
		gl.glEnableVertexAttribArray(VERTICES_LAYOUT);
		
		int offset = 0;
		if(useVerticesBuffer){								
			gl.glVertexAttribPointer(VERTICES_LAYOUT, 4, GL.GL_FLOAT, false, stride*Buffers.SIZEOF_FLOAT, offset);
			gl.glEnableVertexAttribArray(VERTICES_LAYOUT);
			offset += 4;
		}
		if(useColorsBuffer){			
			gl.glVertexAttribPointer(COLORS_LAYOUT, 4, GL.GL_FLOAT, false, stride*Buffers.SIZEOF_FLOAT, offset*Buffers.SIZEOF_FLOAT);
			gl.glEnableVertexAttribArray(COLORS_LAYOUT);
			offset += 4;
		}
		if(useUvsBuffer){			
			gl.glVertexAttribPointer(UVS_LAYOUT, 2, GL.GL_FLOAT, false, stride*Buffers.SIZEOF_FLOAT, offset*Buffers.SIZEOF_FLOAT);
			gl.glEnableVertexAttribArray(UVS_LAYOUT);
			offset += 2;
		}
		if(useNormalsBuffer){			
			gl.glVertexAttribPointer(NORMALS_LAYOUT, 4, GL.GL_FLOAT, false, stride*Buffers.SIZEOF_FLOAT, offset*Buffers.SIZEOF_FLOAT);
			gl.glEnableVertexAttribArray(NORMALS_LAYOUT);
			offset += 4;
		}
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
	 * Initializes and bind the main vertex array object
	 * @param gl the GL
	 */
	private void initializeVertexArrayObject(GL3 gl) {
		if(this.vertexArrayObject == null){
			IntBuffer vao = IntBuffer.allocate(1);
			gl.glGenVertexArrays(1, vao);
			this.vertexArrayObject = vao.get(0);
			gl.glBindVertexArray(this.vertexArrayObject);
		}

	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performRender(javax.media.opengl.GL3)
	 */
	@Override
	protected void performRender(GL3 gl) throws GkException {
		if(!isInitialized()){
			initialize(gl);
		}
		gl.glBindVertexArray(this.vertexArrayObject);	
		if(updateGeometry){
			performUpdateGeometry(gl);
		}
		if(updateBuffer){
			performUpdateBufferObjects(gl);
		}
		if(interleavedBuffer == null){
			buildInterleavedBuffer();
		}
			
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, interleavedBufferObject);
		
		gl.glUseProgram(getShaderProgram());
		
		enableAdditionalVertexAttribArray(gl);
		updateShaderData(gl);
		
		gl.glDrawArrays(getRenderPrimitive(), 0, getVerticesCount());

	    disableAdditionalVertexAttribArray(gl);
    

	    gl.glUseProgram(0);
	}

	protected void updateShaderData(GL3 gl) throws GkException {

	}
	protected void enableAdditionalVertexAttribArray(GL3 gl) throws GkException {

	}
	protected void disableAdditionalVertexAttribArray(GL3 gl) throws GkException {

	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#performDestroy(javax.media.opengl.GL3)
	 */
	@Override
	public void performDestroy(GL3 gl) throws GkException {
		if(!isInitialized()){
			return;
		}
		IntBuffer buffers = IntBuffer.wrap(new int[]{interleavedBufferObject});
		gl.glDeleteBuffers(1,buffers);
		
		IntBuffer intBuffer = IntBuffer.wrap(new int[]{vertexArrayObject});
		gl.glDeleteVertexArrays(1, intBuffer);
	}

	protected void buildInterleavedBuffer() throws GkTechnicalException{
		stride = 0;
				
		if(useVerticesBuffer){
			stride += 4;		// 4
			if(getVerticesBuffer() == null){
				throw new GkTechnicalException(this+" Vertices buffer not initialized.");
			}
		}
		if(useColorsBuffer){
			stride +=4;			// 4	
			if(getColorsBuffer() == null){
				throw new GkTechnicalException(" Colors buffer not initialized.");
			}
		}
		if(useUvsBuffer){
			stride += 2;			//2
			if(getUvsBuffer() == null){
				throw new GkTechnicalException(" Uvs buffer not initialized.");
			}
		}
		if(useNormalsBuffer){
			stride += 4;		//4
			if(getNormalsBuffer() == null){
				throw new GkTechnicalException(" Normals buffer not initialized.");
			}
		}
		if(interleavedBuffer != null){
			interleavedBuffer.clear();
		}
		interleavedBuffer = FloatBuffer.allocate(getVerticesCount()*stride);
		//interleavedBuffer = GLBuffers.newDirectFloatBuffer(getVerticesCount()*stride);
		
		for (int i = 0; i < verticesCount; i++) {
			
			if(useVerticesBuffer){				
				interleavedBuffer.put(verticesBuffer.get(i * 4 ));
				interleavedBuffer.put(verticesBuffer.get(i * 4 +1));
				interleavedBuffer.put(verticesBuffer.get(i * 4 +2));
				interleavedBuffer.put(verticesBuffer.get(i * 4 +3));
			}
			if(useColorsBuffer){;
				interleavedBuffer.put(colorsBuffer.get(i * 4 ));
				interleavedBuffer.put(colorsBuffer.get(i * 4 +1));
				interleavedBuffer.put(colorsBuffer.get(i * 4 +2));
				interleavedBuffer.put(colorsBuffer.get(i * 4 +3));
			}
			if(useUvsBuffer){
				interleavedBuffer.put(uvsBuffer.get(i * 2 ));
				interleavedBuffer.put(uvsBuffer.get(i * 2 +1));				
			}
			if(useNormalsBuffer){
				interleavedBuffer.put(normalsBuffer.get(i * 4 ));
				interleavedBuffer.put(normalsBuffer.get(i * 4 +1));
				interleavedBuffer.put(normalsBuffer.get(i * 4 +2));
				interleavedBuffer.put(normalsBuffer.get(i * 4 +3));
			}
		}
		interleavedBuffer.rewind();
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
//		if(!ObjectUtils.equals(verticesCount, this.verticesCount) && isInitialized()){
//			throw new GkFunctionalException("Cannot change vertices count once initialized");
//		}
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

	/**
	 * @return the normalsBuffer
	 */
	public FloatBuffer getNormalsBuffer() {
		return normalsBuffer;
	}

	/**
	 * @param normalsBuffer the normalsBuffer to set
	 */
	public void setNormalsBuffer(FloatBuffer normalsBuffer) {
		this.normalsBuffer = normalsBuffer;
	}


}
