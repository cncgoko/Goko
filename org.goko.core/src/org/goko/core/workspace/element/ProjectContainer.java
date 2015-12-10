/**
 * 
 */
package org.goko.core.workspace.element;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public class ProjectContainer {
	/** The type of this container */
	private String type;

	
	/**
	 * @param type
	 */
	public ProjectContainer(String type) {
		super();
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
}
