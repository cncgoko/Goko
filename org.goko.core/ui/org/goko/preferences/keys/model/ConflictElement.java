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
		getConflicts().add(conflict);
		getController().firePropertyChange(this, ConflictModel.PROP_CONFLICTS_ADD, null, conflict);
	}
	
	public void removeBinding(BindingElement conflict) {
		getConflicts().remove(conflict);
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

	
}

