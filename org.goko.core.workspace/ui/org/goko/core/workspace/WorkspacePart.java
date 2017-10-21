
package org.goko.core.workspace;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventUtils;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.IProjectMenuProvider;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.core.workspace.service.IWorkspaceUIService;
import org.goko.core.workspace.service.WorkspaceUIEvent;
import org.goko.core.workspace.tree.GkProjectContentProvider;
import org.goko.core.workspace.tree.GkProjectLabelProvider;
import org.osgi.service.event.EventAdmin;

public class WorkspacePart implements Listener {
	private static final GkLog LOG = GkLog.getLogger(WorkspacePart.class);
	@Inject
	private IWorkspaceUIService workspaceUiService;
	/** The workspace tree viewer */
	private TreeViewer workspaceTreeViewer;
	/** Composite containing the configuration panel */
	private Composite configurationComposite;
	/** The currently displayed properties panel */
	private IPropertiesPanel currentPropertiesPanel;
	private ScrolledComposite scrolledComposite;
	/** Previous selection in the tree */
	private Object previousSelection;
	@Inject
	private EventAdmin eventAdmin;
	
	@Inject	
	public WorkspacePart() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) throws GkException {
		
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setBounds(0, 0, 448, 452);
		rootComposite.setLayout(new GridLayout(1, false));
		new Label(rootComposite, SWT.NONE);

		SashForm sashForm = new SashForm(rootComposite, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite composite = new Composite(sashForm, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.horizontalSpacing = 0;
		composite.setLayout(gl_composite);

		workspaceTreeViewer = new TreeViewer(composite, SWT.BORDER);		
		Tree tree = workspaceTreeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		workspaceTreeViewer.setContentProvider(new GkProjectContentProvider());
		workspaceTreeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new GkProjectLabelProvider()));
		workspaceTreeViewer.setInput(workspaceUiService.getProjectContainerUiProvider());
		
		scrolledComposite = new ScrolledComposite(sashForm, SWT.BORDER |SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setAlwaysShowScrollBars(true);
		//scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);		
		scrolledComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		scrolledComposite.addListener(SWT.Resize, this);
		
		configurationComposite = new Composite(scrolledComposite, SWT.NONE);
		GridLayout gl_configurationComposite = new GridLayout(1, false);
		gl_configurationComposite.marginWidth = 2;
		gl_configurationComposite.marginHeight = 0;
		configurationComposite.setLayout(gl_configurationComposite);
		
	
		scrolledComposite.setContent(configurationComposite);
		sashForm.setWeights(new int[] {1, 1});

		createWorkspaceTreeContextMenu();

		createSelectionListener();
	}

	private void createSelectionListener() {
		workspaceTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				try{
					
					Object newSelection = ((StructuredSelection)event.getSelection()).getFirstElement();
					if(ObjectUtils.equals(previousSelection, newSelection)){
						return;
					}
					previousSelection = newSelection;
					
					if(currentPropertiesPanel != null){					
						currentPropertiesPanel.beforeDiscard();
						currentPropertiesPanel.discard();
						currentPropertiesPanel = null;
					}
					// Remove any child of the configuration composite just in case
					for (Control control : configurationComposite.getChildren()) {
						control.dispose();
					}				
					
		        	List<ProjectContainerUiProvider> uiProviders = workspaceUiService.getProjectContainerUiProvider();
		            if(CollectionUtils.isNotEmpty(uiProviders)){
		            	for (ProjectContainerUiProvider projectContainerUiProvider : uiProviders) {
		            		if(projectContainerUiProvider.providesConfigurationPanelFor(workspaceTreeViewer.getSelection())){		            			
		            			currentPropertiesPanel = projectContainerUiProvider.createConfigurationPanelFor(configurationComposite, workspaceTreeViewer.getSelection());
		            		}
						}
		            }  		
		            scrolledComposite.setContent(configurationComposite);
		            Point size = configurationComposite.computeSize(280, SWT.DEFAULT);		            
		            configurationComposite.setSize(size);
		            configurationComposite.layout(true);
		            configurationComposite.getParent().layout();
		            
		    		scrolledComposite.setMinSize(configurationComposite.computeSize(280, SWT.DEFAULT));		    		
		    		scrolledComposite.layout(true);	
		    		//workspaceUiService.select(newSelection);
		    		EventUtils.post(eventAdmin, WorkspaceUIEvent.TOPIC_WORKSPACE_UI_SELECT, newSelection);
	        	}catch(GkException e){
	        		LOG.error(e);
	        	}
			}
		});
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
		            			
		            			String type = projectContainerUiProvider.getType();
		            			List<IProjectMenuProvider> lstMenuProvider = workspaceUiService.getProjectMenuProvider(type);
		            			for (IProjectMenuProvider menuProvider : lstMenuProvider) {
									if(menuProvider.providesMenuFor(workspaceTreeViewer.getSelection())){
										menuProvider.createMenuFor(mgr, workspaceTreeViewer.getSelection());
									}
								}
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

	@Inject
	@Optional
	private void subscribeWorkspaceSelect(@UIEventTopic(WorkspaceUIEvent.TOPIC_WORKSPACE_SELECT) Object data) {
		workspaceTreeViewer.expandAll();		
		workspaceTreeViewer.setSelection(new StructuredSelection(data), true);
		
	}

	@Inject
	@Optional
	private void subscribeWorkspaceDelete(@UIEventTopic(WorkspaceUIEvent.TOPIC_WORKSPACE_DELETE) Object data) throws GkException {		
		List<ProjectContainerUiProvider> uiProviders = workspaceUiService.getProjectContainerUiProvider();
		
		ISelection selection = workspaceTreeViewer.getSelection();
		
        if(!selection.isEmpty() && CollectionUtils.isNotEmpty(uiProviders)){
        	Object selectedObject = ((StructuredSelection)selection).getFirstElement();
        	GkProjectContentProvider contentProvider = (GkProjectContentProvider) workspaceTreeViewer.getContentProvider();
        	
            Object parent = contentProvider.getParent(selectedObject);
            
            Object[] childrenBefore = contentProvider.getChildren(parent);
            int index = ArrayUtils.indexOf(childrenBefore, selectedObject);
            
            for (ProjectContainerUiProvider projectContainerUiProvider : uiProviders) {
        		if(projectContainerUiProvider.handleDelete(workspaceTreeViewer.getSelection())){
        			break;
        		}
			}
        	Object[] childrenAfter = contentProvider.getChildren(parent);
        	if(childrenAfter != null && childrenAfter.length > 0){
        		int newIndex = Math.max(0, Math.min(childrenAfter.length - 1, index));
        		workspaceTreeViewer.setSelection(new StructuredSelection(childrenAfter[newIndex]));
        	}
        }  
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {	
		configurationComposite.setSize(configurationComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setMinSize(configurationComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.layout(true);
	}
}