package org.goko.core.workspace.tree;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.GkProject;
import org.goko.core.workspace.bean.IProjectNode;

public class GkProjectTreeContentProvider implements ITreeContentProvider {
	private static final GkLog LOG = GkLog.getLogger(GkProjectTreeContentProvider.class);
	
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
		List<IProjectNode<? extends IIdBean>> lst = ((GkProject)inputElement).getNodes();
		return lst.toArray();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof IProjectNode<?>){
			try {
				return ((IProjectNode<?>) parentElement).getChildren().toArray();
			} catch (GkException e) {
				LOG.error(e);
			}
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof IProjectNode<?>){
			try {
				return CollectionUtils.isNotEmpty(((IProjectNode<?>) element).getChildren());
			} catch (GkException e) {				
				LOG.error(e);
			}	
		}
		return false;
	}

}
