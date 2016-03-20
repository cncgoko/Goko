/**
 *
 */
package org.goko.core.workspace.element;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.workspace.io.IProjectLocation;

/**
 * Describes a Goko project
 *
 * @author PsyKo
 * @date 10 oct. 2015
 */
public class GkProject {
	/** The name of the project */
	private String name;
	/** The project location */
	private IProjectLocation location;
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
	/**
	 * @return the location
	 */
	public IProjectLocation getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(IProjectLocation location) {
		this.location = location;
	}
}
