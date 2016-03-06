package org.goko.tools.viewer.jogl.utils.render.text.v2;

public class CharBlock {
	private int id;
	private int x;
	private int y;
	private int width;
	private int height;
	private int xoffset;
	private int yoffset;
	private int xadvance;
	private int page;
	
	public CharBlock(int id, int x, int y, int width, int height, int xoffset, int yoffset, int xadvance, int page) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.xadvance = xadvance;
		this.page = page;
	}
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * @return the xoffset
	 */
	public int getXoffset() {
		return xoffset;
	}
	/**
	 * @param xoffset the xoffset to set
	 */
	public void setXoffset(int xoffset) {
		this.xoffset = xoffset;
	}
	/**
	 * @return the yoffset
	 */
	public int getYoffset() {
		return yoffset;
	}
	/**
	 * @param yoffset the yoffset to set
	 */
	public void setYoffset(int yoffset) {
		this.yoffset = yoffset;
	}
	/**
	 * @return the xadvance
	 */
	public int getXadvance() {
		return xadvance;
	}
	/**
	 * @param xadvance the xadvance to set
	 */
	public void setXadvance(int xadvance) {
		this.xadvance = xadvance;
	}
	
	
	
}
