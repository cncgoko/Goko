 
package org.goko.core.workspace;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.core.workspace.service.IWorkspaceUIService;
import org.goko.core.workspace.service.WorkspaceService;
import org.goko.core.workspace.service.WorkspaceUIEvent;
import org.goko.core.workspace.tree.GkProjectContentProvider;
import org.goko.core.workspace.tree.GkProjectLabelProvider;

public class WorkspacePart {
	private static final GkLog LOG = GkLog.getLogger(WorkspacePart.class);
	@Inject
	private WorkspaceService workspaceService;
	@Inject
	private IWorkspaceUIService workspaceUiService;
	/** The workspace tree viewer */
	private TreeViewer workspaceTreeViewer; 
	
	@Inject
	public WorkspacePart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		workspaceTreeViewer = new TreeViewer(parent, SWT.BORDER);
		Tree tree = workspaceTreeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		workspaceTreeViewer.setContentProvider(new GkProjectContentProvider());
		workspaceTreeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new GkProjectLabelProvider()));
		workspaceTreeViewer.setInput(workspaceService.getProject());
		
		createWorkspaceTreeContextMenu();
	}
	
	public void createWorkspaceTreeContextMenu(){
		MenuManager contextMenu = new MenuManager("#ViewerMenu"); //$NON-NLS-1$
	    contextMenu.setRemoveAllWhenShown(true);
	    contextMenu.addMenuListener(new IMenuListener() {
	        /** (inheritDoc)
	         * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	         */
	        @Override
	        public void menuAboutToShow(IMenuManager mgr) {
	        	try{
		        	List<ProjectContainerUiProvider> uiProviders = workspaceUiService.getProjectContainerUiProvider();
		            if(CollectionUtils.isNotEmpty(uiProviders)){
		            	for (ProjectContainerUiProvider projectContainerUiProvider : uiProviders) {	            		
		            		if(projectContainerUiProvider.providesMenuFor(workspaceTreeViewer.getSelection())){
		            			projectContainerUiProvider.createMenuFor(mgr, workspaceTreeViewer.getSelection());
		            		}
						}
		            }
	        	}catch(GkException e){
	        		LOG.error(e);
	        	}
	        	
	        }
	    });

	    Menu menu = contextMenu.createContextMenu(workspaceTreeViewer.getControl());
	    workspaceTreeViewer.getControl().setMenu(menu);
	}
		
	/**
	 * Enable subscription to the workspace refresh event
	 * @param data the data of the event
	 */
	@Inject
	@Optional
	private void subscribeWorkspaceRefresh(@UIEventTopic(WorkspaceUIEvent.TOPIC_WORKSPACE_REFRESH) Map<Object, Object> data) {
	  if(workspaceTreeViewer != null){
		  workspaceTreeViewer.refresh(); 
	  }
	} 


}