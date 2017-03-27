/**
 * 
 */
package org.goko.preferences.keys.model;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * @author Psyko
 * @date 23 mars 2017
 */
public class KeyController{
	private boolean notifying = true;
	private ListenerList eventManager = null;
	private ContextModel contextModel;
	private BindingModel bindingModel;
	private TableModel tableModel;
	private ConflictModel conflictModel;
	
	public void init(MApplication application){
		contextModel = new ContextModel(this);
		contextModel.init(application.getBindingContexts());
		
		bindingModel = new BindingModel(this);
		bindingModel.init(application);
		
		tableModel = new TableModel(this);
		tableModel.init(application.getBindingTables());
		
		conflictModel = new ConflictModel(this);
		conflictModel.init();
		
		addSetBindingListener();
		addSetKeySequenceListener();
	}
	
	private void addSetBindingListener() {
		addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getSource() == bindingModel
						&& CommonModel.PROP_SELECTED_ELEMENT.equals(event.getProperty())) {
					BindingElement binding = (BindingElement) event.getNewValue();
					if (binding == null) {
						contextModel.setSelectedElement(null);
						conflictModel.setSelectedBinding(null);						
						return;
					}					
					contextModel.setSelectedElement(binding.getContext());
					conflictModel.setSelectedBinding(binding);										
				}
			}
		});
	}
	
	private void addSetKeySequenceListener() {
		addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (BindingElement.PROP_TRIGGER.equals(event.getProperty())) {
					updateTrigger((BindingElement) event.getSource(), (TriggerSequence) event.getOldValue(), (TriggerSequence) event.getNewValue());
					getConflictModel().updateConflictsFor((BindingElement) event.getSource(), (TriggerSequence) event.getOldValue(), (TriggerSequence) event.getNewValue());
				}
			}
		});
	}
	
	/**
	 * @param source
	 * @param oldValue
	 * @param newValue
	 */
	protected void updateTrigger(BindingElement activeBinding, TriggerSequence oldValue, TriggerSequence newValue) { 
		if(activeBinding == null){
			return;
		}
		if(!newValue.isEmpty() && activeBinding.getContext() == null){
			ContextElement targetContext = getContextModel().getSelectedElement();
			if(targetContext == null){
				targetContext = getContextModel().getRootContext();				
			}
			activeBinding.setContext(targetContext);
		}				
	}
	
	private ListenerList getEventManager() {
		if (eventManager == null) {
			eventManager = new ListenerList(ListenerList.IDENTITY);
		}
		return eventManager;
	}
	
	public void  addPropertyChangeListener(IPropertyChangeListener listener) {
		getEventManager().add(listener);
	}


	public void  removePropertyChangeListener(IPropertyChangeListener listener) {
		getEventManager().remove(listener);
	}


	public void firePropertyChange(Object source, String propId, Object oldVal, Object newVal) {
		if (!isNotifying()) {
			return;
		}
		if (ObjectUtils.equals(oldVal, newVal)) {
			return;
		}

		Object[] listeners = getEventManager().getListeners();
		PropertyChangeEvent event = new PropertyChangeEvent(source, propId, oldVal, newVal);
		System.out.println("    FirePropertyChange "+source+" prop:"+propId);
		for (int i = 0; i < listeners.length; i++) {
			((IPropertyChangeListener) listeners[i]).propertyChange(event);
		}
	}

	/**
	 * @param eventManager the eventManager to set
	 */
	public void setEventManager(ListenerList eventManager) {
		this.eventManager = eventManager;
	}


	/**
	 * @return the notifying
	 */
	public boolean isNotifying() {
		return notifying;
	}


	/**
	 * @param notifying the notifying to set
	 */
	public void setNotifying(boolean notifying) {
		this.notifying = notifying;
	}

	/**
	 * @return the contextModel
	 */
	public ContextModel getContextModel() {
		return contextModel;
	}

	/**
	 * @param contextModel the contextModel to set
	 */
	public void setContextModel(ContextModel contextModel) {
		this.contextModel = contextModel;
	}

	/**
	 * @return the bindingModel
	 */
	public BindingModel getBindingModel() {
		return bindingModel;
	}

	/**
	 * @param bindingModel the bindingModel to set
	 */
	public void setBindingModel(BindingModel bindingModel) {
		this.bindingModel = bindingModel;
	}

	/**
	 * @return the conflictModel
	 */
	public ConflictModel getConflictModel() {
		return conflictModel;
	}

	/**
	 * @param conflictModel the conflictModel to set
	 */
	public void setConflictModel(ConflictModel conflictModel) {
		this.conflictModel = conflictModel;
	}
	
	
}
