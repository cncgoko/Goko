/**
 * 
 */
package org.goko.preferences.keys.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;

/**
 * @author Psyko
 * @date 24 mars 2017
 */
public class TableElement extends ModelElement<MBindingTable> {

	private ContextElement contextElement;
	
	private List<String> bindingElementIds;
	
	/**
	 * @param controller
	 */
	public TableElement(KeyController controller) {
		super(controller);
	}

	/**
	 * @param mBindingTable
	 */
	public void init(MBindingTable mBindingTable) {
		setId(mBindingTable.getElementId());
		setContributorURI(mBindingTable.getContributorURI());
		setContextElement( getController().getContextModel().getContext(mBindingTable.getBindingContext().getElementId() ));
		setModelObject(mBindingTable);
		// Update bindings
		List<MKeyBinding> bindings = mBindingTable.getBindings();
		for (MKeyBinding mKeyBinding : bindings) {
			BindingElement be = getController().getBindingModel().getBinding(mKeyBinding.getCommand().getElementId());
			if(be != null){
				be.complete(mKeyBinding, getContextElement());
				getBindingElementIds().add(be.getId());
			}
		}
		
	}
	
	protected List<String> getBindingElementIds(){
		if(bindingElementIds  == null){
			bindingElementIds = new ArrayList<String>();
		}
		return bindingElementIds;
	}

	/**
	 * @return the conntextElement
	 */
	public ContextElement getContextElement() {
		return contextElement;
	}

	/**
	 * @param conntextElement the conntextElement to set
	 */
	public void setContextElement(ContextElement contextElement) {
		this.contextElement = contextElement;
	}

}
