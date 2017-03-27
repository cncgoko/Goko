/**
 * 
 */
package org.goko.preferences.keys.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;

/**
 * @author Psyko
 * @date 23 mars 2017
 */
public class BindingModel extends CommonModel<BindingElement>{
	private static final Object SYSTEM_COMMAND_TAG = "system";
	
	public static final String PROP_BINDING_ADD = "bindingAdd"; 
	public static final String PROP_BINDING_ELEMENT_MAP = "bindingElementMap"; 
	public static final String PROP_BINDING_FILTER = "bindingFilter"; 
	public static final String PROP_BINDING_REMOVE = "bindingRemove"; 
	public static final String PROP_BINDINGS = "bindings"; 
	public static final String PROP_CONFLICT_ELEMENT_MAP = "bindingConfictMap"; 
		
	private Map<String, BindingElement> bindingElementById; 
	/**
	 * @param controller
	 */
	public BindingModel(KeyController controller) {
		super(controller);
	}

	/**
	 * @param lstCommands
	 */
	public void init(MApplication application) {
		List<MCommand> lstCommands = application.getCommands();
		for (MCommand mCommand : lstCommands) {
			if(isNotSystemCommand(mCommand)){
				BindingElement be = new BindingElement(getController());
				be.init(mCommand);
				getBindingElementById().put(be.getId(), be);
			}
		}
		
		// Update with binding tables
	}
	
	private boolean isNotSystemCommand(MCommand command){
		List<String> tags = command.getTags();
		return !tags.contains(SYSTEM_COMMAND_TAG);
	}

	public BindingElement getBinding(String id){
		return getBindingElementById().get(id);
	}
	
	private Map<String, BindingElement> getBindingElementById(){
		if(bindingElementById == null){
			bindingElementById = new HashMap<>();
		}
		return bindingElementById;
	}

	/**
	 * @return
	 */
	public Collection<BindingElement> getBindings() {
		return getBindingElementById().values();
	}
}
