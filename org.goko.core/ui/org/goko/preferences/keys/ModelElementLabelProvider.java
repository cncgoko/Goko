package org.goko.preferences.keys;

import org.eclipse.jface.viewers.LabelProvider;
import org.goko.preferences.keys.model.ModelElement;

class ModelElementLabelProvider extends LabelProvider {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		return ((ModelElement) element).getName();
	}
}