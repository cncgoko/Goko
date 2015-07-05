package org.goko.featuremanager.installer;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class InstallableUnitContentProvider implements ITreeContentProvider {

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		List<GkInstallableUnitCategory> lst = (List<GkInstallableUnitCategory>) inputElement;
		return lst.toArray();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof GkInstallableUnitCategory){
			return ((GkInstallableUnitCategory) parentElement).getLstGkInstallableUnit().toArray();
		}
		return new Object[]{};
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {		
		if(element instanceof GkInstallableUnit){
			return ((GkInstallableUnit) element).getCategory();
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof GkInstallableUnitCategory){
			return CollectionUtils.isNotEmpty(((GkInstallableUnitCategory) element).getLstGkInstallableUnit());
		}
		return false;
	}

}
