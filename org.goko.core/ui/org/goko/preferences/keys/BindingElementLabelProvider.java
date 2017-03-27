/**
 * 
 */
package org.goko.preferences.keys;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.preferences.keys.model.BindingElement;
import org.goko.preferences.keys.model.ContextElement;

/**
 * @author Psyko
 * @date 25 mars 2017
 */
public class BindingElementLabelProvider extends LabelProvider implements ITableLabelProvider {
	private static final int COMMAND_NAME_COLUMN = 0;
	private static final int KEY_SEQUENCE_COLUMN = 1;
	private static final int CONTEXT_COLUMN = 2;
	private static final int CATEGORY_COLUMN = 3;
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {	
		if(columnIndex == COMMAND_NAME_COLUMN){
			BindingElement bindingElement = ((BindingElement) element);
			if(bindingElement.hasConflicts()){
				return ResourceManager.getPluginImage("org.goko.core", "icons/status-conflict.gif");
			}
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		BindingElement bindingElement = ((BindingElement) element);
		switch (columnIndex) {
		case COMMAND_NAME_COLUMN: // name
			return bindingElement.getName();
		case KEY_SEQUENCE_COLUMN: // keys
			TriggerSequence seq = bindingElement.getTrigger();
			return seq == null ? StringUtils.EMPTY : seq.format();
		case CONTEXT_COLUMN: // when
			ContextElement context = bindingElement.getContext();
			return context == null ? StringUtils.EMPTY : context.getName();
		case CATEGORY_COLUMN: // category
			return bindingElement.getCategory();		
		}
		return null;
	}
	
}
