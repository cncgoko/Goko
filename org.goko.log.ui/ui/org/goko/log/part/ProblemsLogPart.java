package org.goko.log.part;


import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.log.part.model.ProblemTreeContentProvider;
import org.goko.log.part.model.ProblemTreeLabelProvider;
import org.goko.log.part.model.ProblemsLogController;
import org.goko.log.part.model.ProblemsLogModel;

public class ProblemsLogPart extends GkUiComponent<ProblemsLogController, ProblemsLogModel>{

	@Inject
	public ProblemsLogPart(IEclipseContext context) {
		super(new ProblemsLogController());
		context.set(ProblemsLogPart.class, this);
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			displayMessage(e);
		}

	}


	@PostConstruct
	public void postConstruct(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		parent.setLayout(gl_parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TreeViewer treeViewer = new TreeViewer(composite, SWT.BORDER | SWT.VIRTUAL);
		Tree tree = treeViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn columnDescription = treeViewerColumn.getColumn();
		columnDescription.setWidth(202);
		columnDescription.setText("Description");

		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnSource = treeViewerColumn_1.getColumn();
		trclmnSource.setWidth(100);
		trclmnSource.setText("Source");

		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn columnDate = treeViewerColumn_2.getColumn();
		columnDate.setWidth(100);
		columnDate.setText("Date");

		treeViewer.setContentProvider( new ProblemTreeContentProvider() );
		treeViewerColumn.setLabelProvider( new ProblemTreeLabelProvider(ProblemTreeLabelProvider.COLUMN_DESCRIPTION) );
		treeViewerColumn_1.setLabelProvider( new ProblemTreeLabelProvider(ProblemTreeLabelProvider.COLUMN_SOURCE) );
		treeViewerColumn_2.setLabelProvider( new ProblemTreeLabelProvider(ProblemTreeLabelProvider.COLUMN_DATE) );

		treeViewer.setInput(getDataModel().getTableContent());
	}
}