/**
 *
 */
package org.goko.core.workspace.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	/** The list of loaded container */
	private Map<String, ProjectContainer> projectContainers;

	public GkProject() {
		this.projectContainers = new HashMap<String, ProjectContainer>();
	}
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

	public ProjectContainer getProjectContainer(String type){
		return projectContainers.get(type);
	}

	public List<ProjectContainer> getProjectContainer(){
		return new ArrayList<ProjectContainer>(projectContainers.values());
	}

	public void addProjectContainer(ProjectContainer container){
		projectContainers.put(container.getType(), container);
	}
}
