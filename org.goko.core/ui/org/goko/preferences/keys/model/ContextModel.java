/**
 * 
 */
package org.goko.preferences.keys.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;

/**
 * @author Psyko
 * @date 21 mars 2017
 */
public class ContextModel extends CommonModel<ContextElement>{
	public static final String PROP_CONTEXTS = "contexts"; 
	public static final String PROP_CONTEXT_MAP = "contextIdElementMap";
	static final Object SYSTEM_CONTEXT_TAG = "system";
	
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
			if(isNotSystemBinding(mBindingContext)){
				ContextElement ce = new ContextElement(getController());
				ce.init(mBindingContext);
				if(rootContext == null){
					rootContext = ce;
				}
				getContextElementById().put(ce.getId(), ce);
				init(mBindingContext.getChildren());
			}
		}
	}

	private boolean isNotSystemBinding(MBindingContext mBindingContext){
		List<String> tags = mBindingContext.getTags();
		return !tags.contains(SYSTEM_CONTEXT_TAG);
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
	
	public Collection<ContextElement> getSortedContexts() {
		List<ContextElement> contexts = new ArrayList<>(getContextElementById().values());		
		Collections.sort(contexts, new Comparator<ContextElement>() {
			@Override
			public int compare(ContextElement o1, ContextElement o2) {
				return ObjectUtils.compare(o1.getName(), o2.getName());
			}
		});
		return contexts;
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
