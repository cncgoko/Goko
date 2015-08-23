package org.goko.core.workspace.mock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Tree;

public class WorkspaceViewMock {

	public WorkspaceViewMock() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		TreeViewer treeViewer = new TreeViewer(parent, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCommands = new Label(composite, SWT.NONE);
		lblCommands.setText("Commands :");
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("125 362");
		
		Label lblEstimatedTime = new Label(composite, SWT.NONE);
		lblEstimatedTime.setText("Estimated time :");
		
		Label lblhMin = new Label(composite, SWT.NONE);
		lblhMin.setText("01:23:00");
		
		Label lblRemaining = new Label(composite, SWT.NONE);
		lblRemaining.setText("Remaining :");
		
		Label lblMin = new Label(composite, SWT.NONE);
		lblMin.setText("00:45:00");
		
		ProgressBar progressBar = new ProgressBar(parent, SWT.NONE);
		progressBar.setSelection(50);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Button btnExecuteGcode = new Button(parent, SWT.NONE);
		GridData gd_btnExecuteGcode = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnExecuteGcode.heightHint = 35;
		btnExecuteGcode.setLayoutData(gd_btnExecuteGcode);
		btnExecuteGcode.setText("Execute GCode");
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
