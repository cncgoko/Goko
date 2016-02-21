package org.goko.core.workspace.io;

import java.io.File;

public class SaveContext {
	/** The name of the project to save */
	private String projectName;
	/** The actual project file */
	private File projectFile;
	/** The name of the resources folder for the project */
	private String resourcesFolderName;
	/** The actual resources folder */
	private File resourcesFolder;
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the resourcesFolderName
	 */
	public String getResourcesFolderName() {
		return resourcesFolderName;
	}
	/**
	 * @param resourcesFolderName the resourcesFolderName to set
	 */
	public void setResourcesFolderName(String resourcesFolderName) {
		this.resourcesFolderName = resourcesFolderName;
	}
	/**
	 * @return the resourcesFolder
	 */
	public File getResourcesFolder() {
		return resourcesFolder;
	}
	/**
	 * @param resourcesFolder the resourcesFolder to set
	 */
	public void setResourcesFolder(File resourcesFolder) {
		this.resourcesFolder = resourcesFolder;
	}
	/**
	 * @return the projectFile
	 */
	public File getProjectFile() {
		return projectFile;
	}
	/**
	 * @param projectFile the projectFile to set
	 */
	public void setProjectFile(File projectFile) {
		this.projectFile = projectFile;
	}

	/**
	 * Build the relative path for the given resources as it will be stored in the resource folder
	 * @param resourceName the name of the resource
	 * @return a String being the relative path for the given resources as it will be stored in the resource folder
	 */
	public String getResourcePath(String resourceName){
		return resourcesFolderName + File.separator + resourceName;
	}
}
