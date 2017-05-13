/**
 * 
 */
package org.goko.preferences.keys;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.goko.preferences.keys.model.BindingModel;
import org.goko.preferences.keys.model.ContextModel;

/**
 * @author Psyko
 * @date 25 mars 2017
 */
public class ModelContentProvider implements IStructuredContentProvider{

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// do nothing.
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing.
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof BindingModel){
			return ((BindingModel)inputElement).getBindings().toArray();	
		}else if(inputElement instanceof ContextModel){
			return ((ContextModel)inputElement).getSortedContexts().toArray();
		}
		return new Object[]{};
	}

}
