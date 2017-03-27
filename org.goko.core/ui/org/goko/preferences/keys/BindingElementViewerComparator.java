/**
 * 
 */
package org.goko.preferences.keys;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.goko.preferences.keys.model.BindingElement;

/**
 * @author Psyko
 * @date 25 mars 2017
 */
public class BindingElementViewerComparator extends ViewerComparator {

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		BindingElement k1 = (BindingElement) e1;
		BindingElement k2 = (BindingElement) e2;
		
		int result = 0;
		if(k1.getCategory() == null && k2.getCategory() != null){
			result = 1;
		}else if(k1.getCategory() != null && k2.getCategory() == null){
			result = -1;
		}else if(k1.getCategory() != null && k2.getCategory() != null){			
			result = StringUtils.defaultString(k1.getCategory()).compareTo(k2.getCategory());
			
			if(result == 0){
				result = StringUtils.defaultString(k1.getName()).compareTo(k2.getName());
			}
		}
		return result;		
	}
}
