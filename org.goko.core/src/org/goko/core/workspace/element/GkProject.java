/**
 *
 */
package org.goko.core.workspace.element;

/**
 * Describes a Goko project
 *
 * @author PsyKo
 * @date 10 oct. 2015
 */
public class GkProject {
	/** The name of the project */
	private String name;
	/** The path to the project file */
	private String filepath;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the filepath
	 */
	public String getFilepath() {
		return filepath;
	}
	/**
	 * @param filepath the filepath to set
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}
