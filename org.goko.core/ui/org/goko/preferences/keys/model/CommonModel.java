/**
 * 
 */
package org.goko.preferences.keys.model;

/**
 * @author Psyko
 * @date 23 mars 2017
 */
public class CommonModel<T extends ModelElement> extends ModelElement{
	public static final String PROP_SELECTED_ELEMENT = "selectedElement";
	
	private T selectedElement;	
	
	/**
	 * @param controller
	 */
	public CommonModel(KeyController controller) {
		super(controller);
	}

	public void setSelectedElement(T selectedContext) {
		T old = this.selectedElement;
		this.selectedElement = selectedContext;
		getController().firePropertyChange(this, PROP_SELECTED_ELEMENT, old, selectedContext);
	}

	/**
	 * @return the selectedElement
	 */
	public T getSelectedElement() {
		return selectedElement;
	}
}
