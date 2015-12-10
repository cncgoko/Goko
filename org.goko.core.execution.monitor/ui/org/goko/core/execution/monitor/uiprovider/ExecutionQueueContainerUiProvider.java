package org.goko.core.execution.monitor.uiprovider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.uiprovider.menu.executionqueue.ClearExecutionQueueAction;
import org.goko.core.execution.monitor.uiprovider.menu.executiontoken.DeleteExecutionTokenAction;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.core.workspace.element.ProjectContainer;

public class ExecutionQueueContainerUiProvider extends ProjectContainerUiProvider {
	/** The underlying ExecutionService */
	private IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService;
	/** The label provider */
	private ExecutionQueueContainerLabelProvider labelProvider;

	public ExecutionQueueContainerUiProvider(IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService) {
		super("EXECUTIONQUEUE");
		this.labelProvider = new ExecutionQueueContainerLabelProvider();
		this.executionService = executionService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesLabelFor(java.lang.Object)
	 */
	@Override
	public boolean providesLabelFor(Object content) throws GkException {
		if(content instanceof ProjectContainer){
			return StringUtils.equals(getType(), ((ProjectContainer) content).getType());
		}
		return content instanceof ExecutionToken;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		return labelProvider.getStyledText(element);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return labelProvider.getImage(element);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesConfigurationPanelFor(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public boolean providesConfigurationPanelFor(ISelection content) throws GkException {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#createConfigurationPanelFor(org.eclipse.swt.widgets.Composite, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void createConfigurationPanelFor(Composite parent, ISelection content) throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesContentFor(java.lang.Object)
	 */
	@Override
	public boolean providesContentFor(Object content) throws GkException {
		if(content instanceof ProjectContainer){
			return StringUtils.equals(getType(), ((ProjectContainer) content).getType());
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object content) throws GkException {
		if(content instanceof ProjectContainer){
			return CollectionUtils.isNotEmpty(executionService.getExecutionQueue().getExecutionToken());
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object content) throws GkException {
		if(content instanceof ProjectContainer){
			return executionService.getExecutionQueue().getExecutionToken().toArray();
		}
		return new Object[0];
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object content) throws GkException {
		// TODO Auto-generated method stub
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesMenuFor(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public boolean providesMenuFor(ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		if(content instanceof ProjectContainer){
			return StringUtils.equals(getType(), ((ProjectContainer) content).getType());
		}
		return (content instanceof ExecutionToken);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#createMenuFor(org.eclipse.jface.action.IMenuManager, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void createMenuFor(IMenuManager contextMenu, ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		if(content instanceof ExecutionToken){
			createMenuForExecutionToken(contextMenu, (ExecutionToken<?>)content);
		}else if(content instanceof ProjectContainer && StringUtils.equals(getType(), ((ProjectContainer) content).getType())){
			createMenuForExecutionQueue(contextMenu);
		}
	}

	/**
	 * Create the menu for an execution token
	 * @param contextMenu
	 * @param content
	 */
	private void createMenuForExecutionToken(IMenuManager contextMenu, ExecutionToken<?> token) {
		contextMenu.add(new DeleteExecutionTokenAction(executionService, token.getId()));
	}

	/**
	 * Create the menu for the execution queue
	 * @param contextMenu
	 * @param content
	 */
	private void createMenuForExecutionQueue(IMenuManager contextMenu) {
		contextMenu.add(new Separator());
		contextMenu.add(new ClearExecutionQueueAction(executionService));
		contextMenu.add(new Separator());
	}

}
