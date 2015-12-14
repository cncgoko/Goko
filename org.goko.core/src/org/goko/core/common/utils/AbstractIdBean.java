package org.goko.core.common.utils;

public abstract class AbstractIdBean implements IIdBean{
	/** Internal id */
	private Integer id;	
	
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
}
