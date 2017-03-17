/**
 * 
 */
package org.goko.preferences.keys;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.model.application.commands.MCategory;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * @author Psyko
 * @date 8 mars 2017
 */
public class KeyBindingViewerComparator extends ViewerComparator {
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		KeyBinding k1 = (KeyBinding) e1;
		KeyBinding k2 = (KeyBinding) e2;
		int result = 0;
		
		if(k1.getCommand().getCategory() == null && k2.getCommand().getCategory() != null){
			result = 1;
		}else if(k1.getCommand().getCategory() != null && k2.getCommand().getCategory() == null){
			result = -1;
		}else if(k1.getCommand().getCategory() != null && k2.getCommand().getCategory() != null){
			MCategory c1 = k1.getCommand().getCategory();
			MCategory c2 = k2.getCommand().getCategory();
			result = StringUtils.defaultString(c1.getName()).compareTo(c2.getName());
			
			if(result == 0){
				result = StringUtils.defaultString(k1.getCommand().getCommandName()).compareTo(k2.getCommand().getCommandName());
			}
		}
		
        return result;
};
}
