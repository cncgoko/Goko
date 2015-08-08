package org.goko.tools.viewer.jogl.utils.light;

import javax.vecmath.Color4f;
import javax.vecmath.Point3f;

/**
 * Simple definition of a light
 * 
 * @author PsyKo
 *
 */
public class Light {
	/** Position of the light */
	private Point3f position;
	/** Diffuse color of the light */
	private Color4f diffuse;	
	/** Ambient color of the light */
	private Color4f ambient;
	
	/**
	 * Constructor
	 * @param position position of the light 
	 * @param diffuse diffuse color of the light 
	 */
	public Light(Point3f position, Color4f diffuse, Color4f ambient) {
		super();
		this.position = position;
		this.diffuse = diffuse;
		this.ambient = ambient;
	}
	/**
	 * @return the position
	 */
	public Point3f getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(Point3f position) {
		this.position = position;
	}
	/**
	 * @return the diffuse
	 */
	public Color4f getDiffuse() {
		return diffuse;
	}
	/**
	 * @param diffuse the diffuse to set
	 */
	public void setDiffuse(Color4f diffuse) {
		this.diffuse = diffuse;
	}
	/**
	 * @return the ambient
	 */
	public Color4f getAmbient() {
		return ambient;
	}
	/**
	 * @param ambient the ambient to set
	 */
	public void setAmbient(Color4f ambient) {
		this.ambient = ambient;
	}
	
	
}
