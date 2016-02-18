/**
 *
 */
package org.goko.core.workspace.element;

import java.util.ArrayList;
import java.util.List;

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
	/** Dirty state of the project  */
	private boolean dirty;
	/** The list of container */
	private List<AbstractProjectContainer> container;
	
	/**
	 * Constructor
	 */
	public GkProject() {
		container = new ArrayList<AbstractProjectContainer>();
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
	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}
	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public void addContainer(AbstractProjectContainer container){
		this.container.add(container);
	}
	/**
	 * @return the container
	 */
	public List<AbstractProjectContainer> getContainer() {
		return container;
	}
}
