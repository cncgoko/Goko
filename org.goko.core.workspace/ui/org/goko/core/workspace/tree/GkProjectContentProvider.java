package org.goko.core.workspace.tree;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.core.workspace.internal.Activator;

public class GkProjectContentProvider implements ITreeContentProvider {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GkProjectContentProvider.class);

	public GkProjectContentProvider() {
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {

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
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		List<ProjectContainerUiProvider> containers = ((List<ProjectContainerUiProvider>)inputElement);
		return containers.toArray();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		try {
			return getChildrenIntern(parentElement);
		} catch (GkException e) {
			LOG.error(e);
		}
		return null;
	}

	private Object[] getChildrenIntern(Object parentElement) throws GkException{
		if(parentElement instanceof ProjectContainerUiProvider){
			ProjectContainerUiProvider uiProvider = (ProjectContainerUiProvider) parentElement;
			if(uiProvider != null){
				return uiProvider.getChildren(parentElement);
			}
		}else{
			List<ProjectContainerUiProvider> uiProvider = Activator.getWorkspaceUIService().getProjectContainerUiProvider();
			if(CollectionUtils.isNotEmpty(uiProvider)){
				for (ProjectContainerUiProvider projectContainerUiProvider : uiProvider) {
					if(projectContainerUiProvider.providesContentFor(parentElement)){
						return projectContainerUiProvider.getChildren(parentElement);
					}
				}
			}
		}
		return new Object[0];
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		if(element instanceof ProjectContainerUiProvider){
			try {
				ProjectContainerUiProvider uiProvider = (ProjectContainerUiProvider)element;
				if(uiProvider != null){
					return uiProvider.getParent(element);
				}
			} catch (GkException e) {
				LOG.error(e);
			}
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		try {
			return hasChildrenIntern(element);
		} catch (GkException e) {
			LOG.error(e);
		}

		return false;
	}

	private boolean hasChildrenIntern(Object element) throws GkException {
		if (element instanceof ProjectContainerUiProvider) {
			ProjectContainerUiProvider uiProvider = (ProjectContainerUiProvider) element;
			if (uiProvider != null) {
				return uiProvider.hasChildren(element);
			}

		} else {
			List<ProjectContainerUiProvider> uiProvider = Activator.getWorkspaceUIService().getProjectContainerUiProvider();
			if (CollectionUtils.isNotEmpty(uiProvider)) {
				for (ProjectContainerUiProvider projectContainerUiProvider : uiProvider) {
					if (projectContainerUiProvider.providesContentFor(element)) {
						return projectContainerUiProvider.hasChildren(element);
					}
				}
			}
		}
		return false;
	}
}
