 
package org.goko.core.workspace;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.goko.core.workspace.service.WorkspaceService;
import org.goko.core.workspace.tree.GkProjectContentProvider;
import org.goko.core.workspace.tree.GkProjectLabelProvider;

public class WorkspacePart {
	@Inject
	private WorkspaceService workspaceService;
	
	@Inject
	public WorkspacePart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		TreeViewer treeViewer = new TreeViewer(parent, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		treeViewer.setContentProvider(new GkProjectContentProvider());
		treeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new GkProjectLabelProvider()));
		treeViewer.setInput(workspaceService.getProject());		
	}
}