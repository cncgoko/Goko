/**
 * 
 */
package org.goko.preferences.keys.model;

import org.eclipse.e4.ui.model.application.commands.MBindingContext;

/**
 * @author Psyko
 * @date 21 mars 2017
 */
public class ContextElement extends ModelElement<MBindingContext>{

	/**
	 * @param controller
	 */
	public ContextElement(KeyController controller) {
		super(controller);
	}

	/**
	 * @param mBindingContext
	 */
	public void init(MBindingContext mBindingContext) {
		setId(mBindingContext.getElementId());
		setName(mBindingContext.getName());
		setContributorURI(mBindingContext.getContributorURI());
		setModelObject(mBindingContext);		
	}

}
