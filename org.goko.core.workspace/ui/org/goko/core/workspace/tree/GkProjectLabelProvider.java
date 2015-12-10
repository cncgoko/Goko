package org.goko.core.workspace.tree;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.core.workspace.element.ProjectContainer;
import org.goko.core.workspace.internal.Activator;

public class GkProjectLabelProvider extends LabelProvider implements IStyledLabelProvider {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GkProjectLabelProvider.class);

	public GkProjectLabelProvider() {
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.BaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		try {
			if(element instanceof ProjectContainer){
				ProjectContainer container = (ProjectContainer) element;
				ProjectContainerUiProvider uiProvider = Activator.getWorkspaceUIService().findProjectContainerUiProvider(container.getType());
				if(uiProvider != null){
					return uiProvider.getStyledText(element);
				}
			}else{
				List<ProjectContainerUiProvider> uiProvider = Activator.getWorkspaceUIService().getProjectContainerUiProvider();
				if(CollectionUtils.isNotEmpty(uiProvider)){
					for (ProjectContainerUiProvider projectContainerUiProvider : uiProvider) {
						if(projectContainerUiProvider.providesLabelFor(element)){
							return projectContainerUiProvider.getStyledText(element);
						}
					}
				}
			}
		} catch (GkException e) {
			LOG.error(e);
		}
		return new StyledString("No label provider found for ["+String.valueOf(element)+"]");
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		try {
			if(element instanceof ProjectContainer){
				ProjectContainer container = (ProjectContainer) element;
				ProjectContainerUiProvider uiProvider = Activator.getWorkspaceUIService().findProjectContainerUiProvider(container.getType());
				if(uiProvider != null){
					return uiProvider.getImage(element);
				}
			}else{
				List<ProjectContainerUiProvider> uiProvider = Activator.getWorkspaceUIService().getProjectContainerUiProvider();
				if(CollectionUtils.isNotEmpty(uiProvider)){
					for (ProjectContainerUiProvider projectContainerUiProvider : uiProvider) {
						if(projectContainerUiProvider.providesLabelFor(element)){
							return projectContainerUiProvider.getImage(element);
						}
					}
				}
			}
		} catch (GkException e) {
			LOG.error(e);
		}
		return null;
	}

}
