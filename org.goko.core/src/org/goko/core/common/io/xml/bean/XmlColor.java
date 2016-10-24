/**
 * 
 */
package org.goko.core.common.io.xml.bean;

import javax.vecmath.Color3f;

/**
 * @author Psyko
 * @date 22 oct. 2016
 */
public class XmlColor {
	private int red;
	private int green;
	private int blue;
	
	/**
	 * Constructor
	 */
	public XmlColor(Color3f color) {
		this(Math.round(color.x * 255),Math.round(color.y * 255),Math.round(color.z * 255));
	}
	
	/**
	 * @param red
	 * @param green
	 * @param blue
	 */
	public XmlColor(int red, int green, int blue) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	/**
	 * @return the red
	 */
	public int getRed() {
		return red;
	}
	/**
	 * @param red the red to set
	 */
	public void setRed(int red) {
		this.red = red;
	}
	/**
	 * @return the green
	 */
	public int getGreen() {
		return green;
	}
	/**
	 * @param green the green to set
	 */
	public void setGreen(int green) {
		this.green = green;
	}
	/**
	 * @return the blue
	 */
	public int getBlue() {
		return blue;
	}
	/**
	 * @param blue the blue to set
	 */
	public void setBlue(int blue) {
		this.blue = blue;
	}
	
	public Color3f getColor(){
		return new Color3f(red/255.0f, green/255.0f, blue/255.0f);
	}
}
