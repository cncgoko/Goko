/**
 * 
 */
package org.goko.core.workspace.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.CacheById;
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
	/** The IGCodeProviders */
	private CacheById<IGCodeProvider> cacheGCodeProvider;
	/** The list of loaded gcode file */
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
	

	public IGCodeProvider getGCodeProvider(Integer id) throws GkException {
		return cacheGCodeProvider.get(id);
	}

	public List<IGCodeProvider> getGCodeProvider() throws GkException {		
		return cacheGCodeProvider.get();
	}
	

	public void addGCodeProvider(IGCodeProvider provider) throws GkException {
		cacheGCodeProvider.add(provider);
	}
	
	public void deleteGCodeProvider(Integer id) throws GkException {
		IGCodeProvider provider = cacheGCodeProvider.get(id);		
		cacheGCodeProvider.remove(id);		
	}
}
