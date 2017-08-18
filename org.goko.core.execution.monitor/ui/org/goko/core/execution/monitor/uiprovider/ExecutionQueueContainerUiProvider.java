package org.goko.core.execution.monitor.uiprovider;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.execution.monitor.uiprovider.menu.executionqueue.ClearExecutionQueueAction;
import org.goko.core.execution.monitor.uiprovider.menu.executionqueue.IExecutionQueueContributionItem;
import org.goko.core.execution.monitor.uiprovider.menu.executiontoken.DeleteExecutionTokenAction;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.core.workspace.service.IWorkspaceUIService;

public class ExecutionQueueContainerUiProvider extends ProjectContainerUiProvider implements IGokoService {
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.core.execution.monitor.uiprovider.ExecutionQueueContainerUiProvider";
	/** The underlying ExecutionService */
	private IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService;
	/** The label provider */
	private ExecutionQueueContainerLabelProvider labelProvider;
	/** List of execution queue provider */
	private List<IExecutionQueueContributionItem> lstExecutionQueueContributionItem;
	/** Workspace UI Service */
	private IWorkspaceUIService workspaceUiService;
	
	public ExecutionQueueContainerUiProvider() {
		super("EXECUTIONQUEUE", 20);
		this.labelProvider = new ExecutionQueueContainerLabelProvider();	
		this.lstExecutionQueueContributionItem = new ArrayList<IExecutionQueueContributionItem>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {		
		return SERVICE_ID;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		workspaceUiService.addProjectContainerUiProvider(this);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub
		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesLabelFor(java.lang.Object)
	 */
	@Override
	public boolean providesLabelFor(Object content) throws GkException {
		return this.equals(content) || content instanceof ExecutionToken;
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
	public IPropertiesPanel createConfigurationPanelFor(Composite parent, ISelection content) throws GkException {
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesContentFor(java.lang.Object)
	 */
	@Override
	public boolean providesContentFor(Object content) throws GkException {
		return this.equals(content);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object content) throws GkException {
		if(this.equals(content)){
			return CollectionUtils.isNotEmpty(executionService.getExecutionQueue(ExecutionQueueType.DEFAULT).getExecutionToken());
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object content) throws GkException {
		if(this.equals(content)){
			return executionService.getExecutionQueue(ExecutionQueueType.DEFAULT).getExecutionToken().toArray();
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
		return this.equals(content) || (content instanceof ExecutionToken);
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
		}else if(this.equals(content)){
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
        if(CollectionUtils.isNotEmpty(lstExecutionQueueContributionItem)){
        	contextMenu.add(new Separator());
	        for (IExecutionQueueContributionItem contributionItem : lstExecutionQueueContributionItem) {
	        	contextMenu.add(contributionItem.getItem());
			}
        }
	}
	
	public void addExecutionQueueContributionItem(IExecutionQueueContributionItem contribution){
		lstExecutionQueueContributionItem.add(contribution);
	}

	/**
	 * @param executionService the executionService to set
	 */
	public void setExecutionService(IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService) {
		this.executionService = executionService;
	}

	/**
	 * @param workspaceUiService the workspaceUiService to set
	 */
	public void setWorkspaceUiService(IWorkspaceUIService workspaceUiService) {
		this.workspaceUiService = workspaceUiService;		
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#delete(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public boolean delete(ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		if(content instanceof ExecutionToken){
			executionService.removeFromExecutionQueue(ExecutionQueueType.DEFAULT, ((ExecutionToken<?>) content).getId());
		}
		return false;
	}
}
