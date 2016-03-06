package org.goko.tools.viewer.jogl.utils.render.text.v2;

public class PageBlock {
	/** Id of the page */
	private Integer id;
	/** File associated with the page */
	private String file;
	
	/**
	 * Constructor
	 * @param id Id of the page 
	 * @param file File associated with the page
	 */
	public PageBlock(Integer id, String file) {
		super();
		this.id = id;
		this.file = file;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}
	
	
}
