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

package org.goko.viewer.jogl.service;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;

import com.jogamp.opengl.util.PMVMatrix;

public abstract class AbstractCoreJoglMultipleRenderer extends AbstractCoreJoglRenderer {
	private List<AbstractCoreJoglRenderer> renderers;
	/**
	 * Constructor
	 * @param shaderProgram the shader program
	 */
	public AbstractCoreJoglMultipleRenderer() {
		super(0);
		this.renderers = new ArrayList<AbstractCoreJoglRenderer>();
	}


	/**
	 * Render
	 * @param gl the GL target
	 * @param cameraMatrix the camera matrix
	 * @throws GkException GkException
	 */
	@Override
	public void render(GL3 gl, PMVMatrix modelViewMatrix) throws GkException {
		if(!isEnabled() || shouldDestroy()){
			return;
		}
		if(!isInitialized()){
			initialize(gl);
		}
		if(CollectionUtils.isNotEmpty(renderers)){
			PMVMatrix localMatrix = new PMVMatrix();
			localMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
			modelViewMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
			localMatrix.glLoadMatrixf(modelViewMatrix.glGetMvMatrixf());
			localMatrix.glMultMatrixf(getModelMatrix().glGetMvMatrixf());
			for (AbstractCoreJoglRenderer abstractCoreJoglRenderer : renderers) {
				abstractCoreJoglRenderer.render(gl, localMatrix);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.AbstractCoreJoglRenderer#initialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void initialize(GL3 gl) throws GkException {
		if(!isInitialized()){
			performInitialize(gl);
			if(CollectionUtils.isNotEmpty(renderers)){
				for (AbstractCoreJoglRenderer abstractCoreJoglRenderer : renderers) {
					abstractCoreJoglRenderer.initialize(gl);
				}
			}
			setInitialized(true);
		}
	}
	@Override
	protected final void performRender(GL3 gl) throws GkException{

	}

	public void addRenderer(AbstractCoreJoglRenderer renderer){
		this.renderers.add(renderer);
	}
	/**
	 * Set the id of the used
	 * @param shaderProgram
	 */
	@Override
	public void setShaderProgram(int shaderProgram){

	}
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.ICoreJoglRenderer#performDestroy(javax.media.opengl.GL3)
	 */
	@Override
	public void performDestroy(GL3 gl) throws GkException {
		if(CollectionUtils.isNotEmpty(getRenderers())){
			for (AbstractCoreJoglRenderer abstractCoreJoglRenderer : renderers) {
				abstractCoreJoglRenderer.performDestroy(gl);
			}
		}
		renderers.clear();
	}
	/**
	 * @return the renderers
	 */
	public List<AbstractCoreJoglRenderer> getRenderers() {
		return renderers;
	}

	@Override
	public void destroy() throws GkException {
		if(CollectionUtils.isNotEmpty(renderers)){
			for (AbstractCoreJoglRenderer abstractCoreJoglRenderer : renderers) {
				abstractCoreJoglRenderer.destroy();
			}
		}
	}


}
