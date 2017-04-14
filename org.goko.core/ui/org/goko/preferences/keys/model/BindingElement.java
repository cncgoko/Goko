/**
 * 
 */
package org.goko.preferences.keys.model;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.ParseException;

/**
 * @author Psyko
 * @date 23 mars 2017
 */
public class BindingElement extends ModelElement<Object>{
	public static final String PROP_TRIGGER = "trigger"; 
	public static final String PROP_CONTEXT = "bindingContext"; 
	public static final String PROP_CATEGORY = "category"; 
	public static final String PROP_CONFLICT = "bindingConflict";
	
	private TriggerSequence trigger;
	private ContextElement context;
	private String category;
	private boolean changed;
	/**
	 * @param controller
	 */
	public BindingElement(KeyController controller) {
		super(controller);
	}
	/**
	 * @param mCommand
	 */
	public void init(MCommand mCommand) {
		setId(mCommand.getElementId());
		setName(mCommand.getCommandName());
		setContributorURI(mCommand.getContributorURI());
		setDescription(StringUtils.defaultString(mCommand.getDescription()));
		if(mCommand.getCategory() != null){
			setCategory(mCommand.getCategory().getName());
		}
		setModelObject(mCommand);
		setChanged(false);
	}
	
	public void complete(MKeyBinding mKeyBinding, ContextElement context){		
		try {
			setTrigger( KeySequence.getInstance(mKeyBinding.getKeySequence()) );
			setContext(context);
			setModelObject(mKeyBinding);
			setChanged(false);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return the trigger
	 */
	public TriggerSequence getTrigger() {
		return trigger;
	}
	/**
	 * @param trigger the trigger to set
	 */
	public void setTrigger(TriggerSequence trigger) {
		if (!ObjectUtils.equals(this.trigger, trigger)) {
			setChanged(true);
		}
		getController().firePropertyChange(this, PROP_TRIGGER, this.trigger, this.trigger = trigger);		
	}
	/**
	 * @return the context
	 */
	public ContextElement getContext() {
		return context;
	}
	/**
	 * @param context the context to set
	 */
	public void setContext(ContextElement context) {
		if (!ObjectUtils.equals(this.context, context)) {
			setChanged(true);
		}
		getController().firePropertyChange(this, PROP_CONTEXT, this.context, this.context = context);
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		getController().firePropertyChange(this, PROP_CATEGORY, this.category, this.category = category);
	}
	/**
	 * @return
	 */
	public boolean hasConflicts() {		
		return getController().getConflictModel().hasConflicts(this);
	} 

	public boolean isComplete(){
		return getTrigger() != null && !getTrigger().isEmpty() && getContext() != null;		
	}
	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}
	/**
	 * @param changed the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
		
}
