package org.goko.tools.viewer.jogl.utils.render.internal;

import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;

import com.jogamp.opengl.GL3;

public abstract class AbstractLineRenderer extends AbstractVboJoglRenderer {
	/** Line width */
	private float lineWidth;
	
	/**
	 * Constructeur
	 * @param renderPrimitive the type of rendered primitives 
	 * @param usedBuffers the used buffers list 
	 */
	protected AbstractLineRenderer(int renderPrimitive, int usedBuffers) {		
		super(renderPrimitive, usedBuffers);	
		this.lineWidth = 2;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {		
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.LINE_SHADER);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#updateShaderData(javax.media.opengl.GL3)
	 */
	@Override
	protected void updateShaderData(GL3 gl) throws GkException {		
		super.updateShaderData(gl);
		gl.glLineWidth(lineWidth);
	}
	
	/**
	 * @return the lineWidth
	 */
	public float getLineWidth() {
		return lineWidth;
	}

	/**
	 * @param lineWidth the lineWidth to set
	 */
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

}
