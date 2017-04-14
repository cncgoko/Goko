/**
 * 
 */
package org.goko.preferences.keys.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.e4.ui.bindings.internal.BindingTableManager;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
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
		addSetContextListener();
	}
	
	public void restore(MApplication application){
		contextModel = new ContextModel(this);
		contextModel.init(application.getBindingContexts());
		
		bindingModel = new BindingModel(this);
		bindingModel.init(application);
		
		tableModel = new TableModel(this);
		tableModel.init(application.getBindingTables());
		
		conflictModel = new ConflictModel(this);
		conflictModel.init();
	}
	/**
	 * Register listener when the selected binding in the bindingModel changes:
	 *  - Update selected context element
	 *  - Update selected conflict element
	 */
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
	/**
	 * Register listener when the key sequence of the selected binding changes
	 *   - 
	 */
	private void addSetKeySequenceListener() {
		addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (BindingElement.PROP_TRIGGER.equals(event.getProperty())) {
					BindingElement bindingElement = (BindingElement) event.getSource();
					updateTrigger(bindingElement, (TriggerSequence) event.getOldValue(), (TriggerSequence) event.getNewValue());
					getConflictModel().updateConflictsFor(bindingElement, (TriggerSequence) event.getOldValue(), (TriggerSequence) event.getNewValue());
				}
			}
		});
	}
	
	/**
	 * Register listener when the context of the selected binding changes
	 *   - 
	 */
	private void addSetContextListener() {
		addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (BindingElement.PROP_CONTEXT.equals(event.getProperty())) {
					BindingElement bindingElement = (BindingElement) event.getSource();
					getConflictModel().updateConflictsFor(bindingElement, (ContextElement) event.getOldValue(), (ContextElement) event.getNewValue());
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
		if(newValue != null && !newValue.isEmpty() && activeBinding.getContext() == null){
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
		for (int i = 0; i < listeners.length; i++) {
			((IPropertyChangeListener) listeners[i]).propertyChange(event);
		}
	}
	
	public void applyChanges(MApplication application, BindingTableManager bindingTableManager){
		setNotifying(false);		
		
		removeModifiedBindings(application);
		
		updateModifiedBindings(application, bindingTableManager);
		
		setNotifying(true);
	}
	
	/**
	 * @param modifiedKeyBinding
	 */
	private void updateModifiedBindings(MApplication application, BindingTableManager btm) {
		for (BindingElement be : getBindingModel().getBindings()) {			
			Object modelObject = be.getModelObject();			
			// Make sure we have everything we need
			if(be.isChanged() && be.isComplete()){
				TableElement tableElement = getTableModel().getTableByContext(be.getContext().getId());
				
				MBindingTable targetTable = tableElement.getModelObject(); 
				MCommand mCommand = null;
				String elementId = StringUtils.EMPTY;
				String contributorURI = StringUtils.EMPTY;
				
				if(modelObject instanceof MKeyBinding){
					MKeyBinding mKeyBinding = (MKeyBinding) modelObject;
					mCommand = mKeyBinding.getCommand();
					elementId = mKeyBinding.getElementId();
					contributorURI = mKeyBinding.getContributorURI();
				}else if(modelObject instanceof MCommand){				
					mCommand = (MCommand) modelObject;
					elementId = "kb."+mCommand.getElementId();
					contributorURI = mCommand.getContributorURI();
				}
				if(mCommand != null){
					MKeyBinding binding = MCommandsFactory.INSTANCE.createKeyBinding();				
					
					binding.setCommand(mCommand);					
					binding.setElementId(elementId);
					binding.setContributorURI(contributorURI);
					binding.setKeySequence(be.getTrigger().format());
					targetTable.getBindings().add(binding);
				}
			}
		}
	}

	/**
	 * @return
	 */
	private List<BindingElement> removeModifiedBindings(MApplication application) {
		
		List<BindingElement> modifiedKeyBinding = new ArrayList<>();
		
		for (MBindingTable mBindingTable : application.getBindingTables()) {			
			ArrayList<MKeyBinding> localBinding = new ArrayList<>(mBindingTable.getBindings());
			for (MKeyBinding mKeyBinding : localBinding) {				
				MCommand command = mKeyBinding.getCommand();
				List<String> tags = command.getTags();
				if(!tags.contains(BindingModel.SYSTEM_COMMAND_TAG)){
					BindingElement be = getBindingModel().getBinding(command.getElementId());
				
					if( CollectionUtils.isEmpty(command.getParameters()) // Ignore parametrized commands for now 
						&& be.isChanged()){
						// Actual remove
						mBindingTable.getBindings().remove(mKeyBinding);
						if(be.getTrigger() != null){
							modifiedKeyBinding.add(be);
						}
					}					
				}
			}						
		}
		
		return modifiedKeyBinding;
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

	/**
	 * @return the tableModel
	 */
	public TableModel getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	
}
