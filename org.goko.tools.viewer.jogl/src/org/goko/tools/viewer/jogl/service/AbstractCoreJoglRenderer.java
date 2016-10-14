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

package org.goko.tools.viewer.jogl.service;

import javax.media.opengl.GL3;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import org.goko.core.common.exception.GkException;
import org.goko.core.math.BoundingTuple6b;

import com.jogamp.opengl.util.PMVMatrix;

/**
 * @author Psyko
 *
 */
public abstract class AbstractCoreJoglRenderer implements ICoreJoglRenderer {
	private static final String SHADER_MODEL_VIEW_MATRIX_NAME = "modelViewMatrix";	
	/** The model matrix of this renderer */
	private PMVMatrix modelMatrix;
	/** The ID of the shader program */
	private int shaderProgram;
	/** The id of the shader's internal model view matrix */
	private Integer shaderModelViewMatrixId;
	/** Initialized state */
	private boolean initialized;
	/** Enabled/disabled state */
	private boolean disabled;
	/** Destroy request */
	private boolean shouldDestroy;
	/** Layer id */
	private int layerId;
	/** Indicate that this renderer uses alpha layer */
	private boolean useAlpha;
	/** The bounds of this renderer */
	private BoundingTuple6b bounds;
	/** The code of this renderer */
	private String code;
	
	/**
	 * Constructor
	 * @param shaderProgram the shader program
	 */
	public AbstractCoreJoglRenderer(int shaderProgram) {
		super();
		this.shaderProgram = shaderProgram;
		this.modelMatrix = new PMVMatrix();
		this.modelMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		this.modelMatrix.glLoadIdentity();
		this.layerId= Layer.LAYER_DEFAULT;
		this.code = this.toString();
	}

	public AbstractCoreJoglRenderer() {
		this(-1);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#getCode()
	 */
	@Override
	public String getCode(){
		return code;
	}

	/**
	 * Render
	 * @param gl the GL target
	 * @param modelViewMatrix the current model view  matrix
	 * @throws GkException GkException
	 */
	@Override
	public void render(GL3 gl, PMVMatrix modelViewMatrix) throws GkException {
		if(disabled || shouldDestroy()){
			return;
		}
		if(!isInitialized()){
			initialize(gl);
		}
		// Check shader program
		if(shaderProgram == 0){
			return;
		}
		// Activate shader
		gl.glUseProgram(shaderProgram);

		// Apply complete model view matrix
		PMVMatrix localModelMatrix = new PMVMatrix();
		modelViewMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		localModelMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		localModelMatrix.glLoadMatrixf(modelViewMatrix.glGetMvMatrixf());
		PMVMatrix modeMat = getModelMatrix();
		localModelMatrix.glMultMatrixf(modeMat.glGetMvMatrixf());
		localModelMatrix.update();
		gl.glUniformMatrix4fv(shaderModelViewMatrixId, 1, false, localModelMatrix.glGetMvMatrixf());

		// Perform render
		performRender(gl);

		// Deactivate shader
		gl.glUseProgram(0);
		
	}

	protected void initialize(GL3 gl) throws GkException {
		if(!isInitialized()){
			performInitialize(gl);
			shaderModelViewMatrixId = gl.glGetUniformLocation(shaderProgram, SHADER_MODEL_VIEW_MATRIX_NAME);
			setInitialized(true);
		}
	}
	protected abstract void performRender(GL3 gl) throws GkException;
	protected abstract void performInitialize(GL3 gl) throws GkException;
	/**
	 * Set the id of the used
	 * @param shaderProgram
	 */
	public void setShaderProgram(int shaderProgram){
		this.shaderProgram = shaderProgram;
	}

	/**
	 * @return the modelMatrix
	 */
	public PMVMatrix getModelMatrix() {
		return modelMatrix;
	}
	/**
	 * @param modelMatrix the modelMatrix to set
	 */
	public void setModelMatrix(PMVMatrix modelMatrix) {
		this.modelMatrix = modelMatrix;
	}

	/**
	 * @return the shaderProgram
	 */
	protected int getShaderProgram() {
		return shaderProgram;
	}

	/**
	 * @return the initialized
	 */
	protected boolean isInitialized() {
		return initialized && !shouldDestroy;
	}

	/**
	 * @param initialized the initialized to set
	 */
	protected void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return !disabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.disabled = !enabled;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#shouldDestroy()
	 */
	@Override
	public boolean shouldDestroy() throws GkException {
		return shouldDestroy;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#destroy()
	 */
	@Override
	public void destroy() throws GkException {
		shouldDestroy = true;
	}

	/**
	 * @return the layerId
	 */
	public int getLayerId() {
		return layerId;
	}

	/**
	 * @param layerId the layerId to set
	 */
	public void setLayerId(int layerId) {
		this.layerId = layerId;
	}

	/**
	 * @return the useAlpha
	 */
	public boolean useAlpha() {
		return useAlpha;
	}

	/**
	 * @param useAlpha the useAlpha to set
	 */
	public void setUseAlpha(boolean useAlpha) {
		this.useAlpha = useAlpha;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#getBounds()
	 */
	@Override
	public BoundingTuple6b getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(BoundingTuple6b bounds) {
		this.bounds = bounds;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
}
