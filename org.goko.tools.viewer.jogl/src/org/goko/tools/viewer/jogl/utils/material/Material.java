package org.goko.tools.viewer.jogl.utils.material;

import javax.vecmath.Color3f;

public class Material {
	private Color3f ambientColor;
	private Color3f diffuseColor;
	private Color3f specularColor;
	
	/**
	 * @param ambientColor
	 * @param diffuseColor
	 * @param specularColor
	 */
	public Material(Color3f ambientColor, Color3f diffuseColor, Color3f specularColor) {
		super();
		this.ambientColor = ambientColor;
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
	}

	/**
	 * @return the ambientColor
	 */
	public Color3f getAmbientColor() {
		return ambientColor;
	}

	/**
	 * @param ambientColor the ambientColor to set
	 */
	public void setAmbientColor(Color3f ambientColor) {
		this.ambientColor = ambientColor;
	}

	/**
	 * @return the diffuseColor
	 */
	public Color3f getDiffuseColor() {
		return diffuseColor;
	}

	/**
	 * @param diffuseColor the diffuseColor to set
	 */
	public void setDiffuseColor(Color3f diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	/**
	 * @return the specularColor
	 */
	public Color3f getSpecularColor() {
		return specularColor;
	}

	/**
	 * @param specularColor the specularColor to set
	 */
	public void setSpecularColor(Color3f specularColor) {
		this.specularColor = specularColor;
	}
	
	
}
