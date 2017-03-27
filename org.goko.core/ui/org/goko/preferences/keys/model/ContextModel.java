/**
 * 
 */
package org.goko.preferences.keys.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.ui.model.application.commands.MBindingContext;

/**
 * @author Psyko
 * @date 21 mars 2017
 */
public class ContextModel extends CommonModel<ContextElement>{
	public static final String PROP_CONTEXTS = "contexts"; 
	public static final String PROP_CONTEXT_MAP = "contextIdElementMap";
	
	private Map<String, ContextElement> contextElementById; 
	private ContextElement rootContext;
	
	/**
	 * @param controller
	 */
	public ContextModel(KeyController controller) {
		super(controller);
	}
	/**
	 * @param bindingContexts
	 */
	public void init(List<MBindingContext> bindingContexts) {		
		for (MBindingContext mBindingContext : bindingContexts) {
			ContextElement ce = new ContextElement(getController());
			ce.init(mBindingContext);
			if(rootContext == null){
				rootContext = ce;
			}
			getContextElementById().put(ce.getId(), ce);
			init(mBindingContext.getChildren());
		}
	}

	public ContextElement getContext(String id){
		return getContextElementById().get(id);
	}
	
	private Map<String, ContextElement> getContextElementById(){
		if(contextElementById == null){
			contextElementById = new HashMap<>();
		}
		return contextElementById;
	}
	
	/**
	 * @return
	 */
	public Collection<ContextElement> getContexts() {
		return getContextElementById().values();
	}
	/**
	 * @return the rootContext
	 */
	public ContextElement getRootContext() {
		return rootContext;
	}
	/**
	 * @param rootContext the rootContext to set
	 */
	public void setRootContext(ContextElement rootContext) {
		this.rootContext = rootContext;
	}
}
