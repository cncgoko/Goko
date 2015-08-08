package org.goko.tools.viewer.jogl.utils.render.internal;

import javax.media.opengl.GL3;
import javax.vecmath.Color3f;

import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.material.Material;

public abstract class AbstractShadedRenderer extends AbstractVboJoglRenderer{
	private Material material;

	protected AbstractShadedRenderer(int renderPrimitive, int usedBuffers) {
		super(renderPrimitive, usedBuffers);
	}

	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {		
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.SHADED_SHADER);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#updateShaderData(javax.media.opengl.GL3)
	 */
	@Override
	protected void updateShaderData(GL3 gl) throws GkException {		
		super.updateShaderData(gl);
		int shaderProgram = getShaderProgram();
				
		int ambient = gl.glGetUniformLocation(shaderProgram, "material.ambient");
		if(ambient >= 0){
			Color3f ambientColor = material.getAmbientColor();
			gl.glUniform3fv(ambient, 1, new float[]{ambientColor.x, ambientColor.y, ambientColor.z},0);
		}
		int diffuse = gl.glGetUniformLocation(shaderProgram, "material.diffuse");
		if(diffuse >= 0){
			Color3f diffuseColor = material.getAmbientColor();
			gl.glUniform3fv(diffuse, 1, new float[]{diffuseColor.x, diffuseColor.y, diffuseColor.z},0);
		}
		int specular = gl.glGetUniformLocation(shaderProgram, "material.specular");
		if(specular >= 0){
			Color3f specularColor = material.getAmbientColor();
			gl.glUniform3fv(specular, 1, new float[]{specularColor.x, specularColor.y, specularColor.z},0);
		}		
	}

	/**
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @param material the material to set
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	
}
