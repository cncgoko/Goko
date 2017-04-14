/**
 * 
 */
package org.goko.preferences.keys.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.model.application.commands.MKeyBinding;

/**
 * @author Psyko
 * @date 26 mars 2017
 */
public class ConflictElement extends ModelElement<MKeyBinding>{	
	private ConflictElementKey key;
	private List<BindingElement> conflicts;
	
	/**
	 * @param controller
	 */
	public ConflictElement(KeyController controller) {
		super(controller);
	}

	/**
	 * @return the conflicts
	 */
	public List<BindingElement> getConflicts() {
		if(conflicts == null){
			conflicts = new ArrayList<BindingElement>();
		}
		return conflicts;
	}

	/**
	 * @param conflicts the conflicts to set
	 */
	public void setConflicts(List<BindingElement> conflicts) {
		this.conflicts = conflicts;
	}
	
	public void addBinding(BindingElement conflict) {
		if(!getConflicts().contains(conflict)){
			getConflicts().add(conflict);
			getController().firePropertyChange(this, ConflictModel.PROP_CONFLICTS_ADD, null, conflict);
		}
	}
	
	public void removeBinding(BindingElement conflict) {
		if(getConflicts().contains(conflict)){
			getConflicts().remove(conflict);
			getController().firePropertyChange(this, ConflictModel.PROP_CONFLICTS_REMOVE, null, conflict);
		}
	}

	/**
	 * @return the key
	 */
	public ConflictElementKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(ConflictElementKey key) {
		this.key = key;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((conflicts == null) ? 0 : conflicts.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConflictElement other = (ConflictElement) obj;
		if (conflicts == null) {
			if (other.conflicts != null)
				return false;
		} else if (!conflicts.equals(other.conflicts))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	
	
}

