/**
 * 
 */
package org.goko.preferences.keys.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jface.bindings.TriggerSequence;

/**
 * @author Psyko
 * @date 23 mars 2017
 */
public class ConflictModel extends CommonModel<ConflictElement>{

	public static final String PROP_CONFLICTS = "conflicts"; 
	public static final String PROP_CONFLICTS_ADD = "conflictsAdd"; 
	public static final String PROP_CONFLICTS_REMOVE = "conflictsRemove"; 
	
	private Map<ConflictElementKey, ConflictElement> conflictElementByKey; 
	
	/**
	 * @param controller
	 */
	public ConflictModel(KeyController controller) {
		super(controller);
	}

	public boolean hasConflicts(BindingElement bindingElement){
		ConflictElementKey key = new ConflictElementKey(bindingElement.getContext(), bindingElement.getTrigger());
		ConflictElement ce = getConflictElementByKey().get(key);
		return ce != null && CollectionUtils.size(ce.getConflicts()) > 1;
	}

	public int getConflictsCount(){
		int count = 0;
		for (ConflictElement ce : conflictElementByKey.values()) {
			count += Math.max(0, CollectionUtils.size(ce.getConflicts()) - 1);
		}
		return count;
	}
	/**
	 * @return the conflictElementByKey
	 */
	private Map<ConflictElementKey, ConflictElement> getConflictElementByKey() {
		if(conflictElementByKey == null){
			conflictElementByKey = new HashMap<ConflictElementKey, ConflictElement>();
		}
		return conflictElementByKey;
	}

	private ConflictElement getConflictElement(ConflictElementKey key) {
		return getConflictElementByKey().get(key);
	}

	/**
	 * Init conflicts
	 */
	public void init() {
		Collection<BindingElement> bindings = getController().getBindingModel().getBindings();
		for (BindingElement bindingElement : bindings) {
			if(bindingElement.getTrigger() != null){
				ConflictElementKey key = new ConflictElementKey(bindingElement.getContext(), bindingElement.getTrigger());
				ConflictElement ce = getConflictElementByKey().get(key);
				// No conflict yet			
				if(ce == null){
					ce = new ConflictElement(getController());
					ce.setKey(key);
				}
				ce.addBinding(bindingElement);
				getConflictElementByKey().put(key, ce);				
			}
		}
	}


	/**
	 * @param binding
	 */
	public void setSelectedBinding(BindingElement bindingElement) {
		if(bindingElement == null || bindingElement.getTrigger() == null){
			setSelectedElement(null);
		}else{
			ConflictElementKey key = new ConflictElementKey(bindingElement.getContext(), bindingElement.getTrigger());
			setSelectedElement(getConflictElement(key));
		}
	}


	public void updateConflictsFor(BindingElement source, ContextElement oldContext, ContextElement newContext) {
		// Remove the old key
		if(oldContext != null){
			ConflictElementKey oldKey = new ConflictElementKey(oldContext, source.getTrigger());
			ConflictElement ce = getConflictElement(oldKey);
			if(ce != null){
				ce.removeBinding(source);
			}
		}
		if(newContext != null){
			ConflictElementKey newKey = new ConflictElementKey(newContext, source.getTrigger());
			ConflictElement ce = getConflictElement(newKey);
			if(ce == null){
				ce = new ConflictElement(getController());
				ce.setKey(newKey);
				getConflictElementByKey().put(newKey, ce);
			}
			ce.addBinding(source);
			if(ObjectUtils.equals(source, getController().getBindingModel().getSelectedElement())){
				setSelectedElement(ce);
			}
		}
		getController().firePropertyChange(this, PROP_CONFLICTS, null, this);
	}
	/**
	 * @param source
	 * @param oldValue
	 * @param newValue
	 */
	public void updateConflictsFor(BindingElement source, TriggerSequence oldValue, TriggerSequence newValue) {
		// Remove the old key
		if(oldValue != null && !oldValue.isEmpty()){
			ConflictElementKey oldKey = new ConflictElementKey(source.getContext(), oldValue);
			ConflictElement ce = getConflictElement(oldKey);
			if(ce != null){
				ce.removeBinding(source);
			}
		}
		if(newValue != null && !newValue.isEmpty()){
			ConflictElementKey newKey = new ConflictElementKey(source.getContext(), newValue);
			ConflictElement ce = getConflictElement(newKey);
			if(ce == null){
				ce = new ConflictElement(getController());
				ce.setKey(newKey);
				getConflictElementByKey().put(newKey, ce);
			}
			ce.addBinding(source);
			if(ObjectUtils.equals(source, getController().getBindingModel().getSelectedElement())){
				setSelectedElement(ce);
			}
		}
		getController().firePropertyChange(this, PROP_CONFLICTS, null, this);
	}
}
