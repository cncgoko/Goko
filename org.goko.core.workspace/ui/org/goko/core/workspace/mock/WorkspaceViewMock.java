package org.goko.core.workspace.mock;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.workspace.bean.GkProject;
import org.goko.core.workspace.bean.NodeGCodeProviderContainer;
import org.goko.core.workspace.bean.ProjectNode;
import org.goko.core.workspace.service.GCodeProviderEvent;
import org.goko.core.workspace.tree.GkProjectTreeContentProvider;
import org.goko.core.workspace.tree.GkProjectTreeLabelProvider;

public class WorkspaceViewMock {

	public WorkspaceViewMock() {
	}

	/**
	 * Create contents of the view part.
	 * @throws GkException 
	 */
	@PostConstruct
	public void createControls(Composite parent) throws GkException {
		parent.setLayout(new GridLayout(1, false));
		
		TreeViewer treeViewer = new TreeViewer(parent, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setText("New Button");
		
		GkProject project = new GkProject();
		NodeGCodeProviderContainer node = new NodeGCodeProviderContainer();		
		node.setContent(new IGCodeProvider() {
			
			@Override
			public void setId(Integer id) {
					
			}
			
			@Override
			public Integer getId() {
				return 1;
			}
			
			@Override
			public List<GCodeLine> getLines() throws GkException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public GCodeLine getLineAtIndex(Integer indexLine) throws GkException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public GCodeLine getLine(Integer idLine) throws GkException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getCode() {
				return "Tata.nc";
			}
		});
		ProjectNode<IGCodeProvider> node2 = new ProjectNode<IGCodeProvider>(NodeGCodeProviderContainer.NODE_TYPE);
		node2.setContent(new IGCodeProvider() {
			
			@Override
			public void setId(Integer id) {
					
			}
			
			@Override
			public Integer getId() {
				return 1;
			}
			
			@Override
			public List<GCodeLine> getLines() throws GkException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public GCodeLine getLineAtIndex(Integer indexLine) throws GkException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public GCodeLine getLine(Integer idLine) throws GkException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getCode() {				
				return "Toto.nc";
			}
		});
		node.addChild(node2);			
		project.addNode(node);
		
		treeViewer.setContentProvider(new GkProjectTreeContentProvider());
		treeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new GkProjectTreeLabelProvider()));
		treeViewer.setInput(project);
		parent.pack();
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();
		WorkspaceViewMock w = new WorkspaceViewMock();
		try {
			w.createControls(shell);
		} catch (GkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// run the event loop as long as the window is open
		while (!shell.isDisposed()) {
		    // read the next OS event queue and transfer it to a SWT event 
		  if (!display.readAndDispatch())
		   {
		  // if there are currently no other OS event to process
		  // sleep until the next OS event is available 
		    display.sleep();
		   }
		}

		// disposes all associated windows and their components
		display.dispose(); 
	}
}
