/**
 * 
 */
package org.goko.preferences.keys.model;

/**
 * @author Psyko
 * @date 23 mars 2017
 */
public class ModelElement<T> {
	public static final String PROP_PARENT = "parent"; 
	public static final String PROP_ID = "id"; 
	public static final String PROP_NAME = "name"; 
	public static final String PROP_DESCRIPTION = "description"; 
	public static final String PROP_MODEL_OBJECT = "modelObject"; 
	private KeyController controller;
	private String id;
	private String name;
	private String description;
	private String contributorURI;
	private T modelObject;
	
	/**
	 * @param controller
	 */
	public ModelElement(KeyController controller) {
		super();
		this.controller = controller;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		getController().firePropertyChange(this, PROP_ID, this.id, this.id = id);
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
		getController().firePropertyChange(this, PROP_NAME, this.name, this.name = name);
	}
	/**
	 * @return the controller
	 */
	public KeyController getController() {
		return controller;
	}
	/**
	 * @param controller the controller to set
	 */
	public void setController(KeyController controller) {
		this.controller = controller;
	}
	/**
	 * @return the modelObject
	 */
	public T getModelObject() {
		return modelObject;
	}
	/**
	 * @param modelObject the modelObject to set
	 */
	public void setModelObject(T modelObject) {
		getController().firePropertyChange(this, PROP_MODEL_OBJECT, this.modelObject, this.modelObject = modelObject);
	}
	/**
	 * @return the contributorURI
	 */
	public String getContributorURI() {
		return contributorURI;
	}
	/**
	 * @param contributorURI the contributorURI to set
	 */
	public void setContributorURI(String contributorURI) {
		this.contributorURI = contributorURI;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		getController().firePropertyChange(this, PROP_DESCRIPTION, this.description, this.description = description);
	}
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelElement other = (ModelElement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
