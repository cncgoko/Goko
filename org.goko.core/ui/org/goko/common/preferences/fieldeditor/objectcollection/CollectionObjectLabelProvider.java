package org.goko.common.preferences.fieldeditor.objectcollection;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * Label provider for CollectionObject
 * @author PsyKo
 *
 */
public class CollectionObjectLabelProvider extends LabelProvider{
		
	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		return ((CollectionObject)element).getLabel();
	}
}