/**
 * 
 */
package org.goko.core.workspace.bean;

import java.util.List;

import org.goko.core.gcode.element.IGCodeProvider;


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
	/** The list of loaded gcode file */
	private List<IGCodeProvider> gcodeProviders;
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
	/**
	 * @return the gcodeProviders
	 */
	public List<IGCodeProvider> getGcodeProviders() {
		return gcodeProviders;
	}
	/**
	 * @param gcodeProviders the gcodeProviders to set
	 */
	public void setGcodeProviders(List<IGCodeProvider> gcodeProviders) {
		this.gcodeProviders = gcodeProviders;
	}
		
}
