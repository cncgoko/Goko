/**
 * 
 */
package goko.dialog;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * @author PsyKo
 *
 */
public class GokoProgressDialog extends Dialog {
	private int runningTasks;
	@Inject
	private UISynchronize sync;
	private Label lblOperationInProgress;
	private Map<Job, JobProgressElement> mapProgressElement;
	private ScrolledComposite scrolledComposite;
	private Composite composite;
	
	/**
	 * Create the dialog.
	 * @param shell 
	 * 
	 * @param parentShell
	 */
	public GokoProgressDialog() {
		super((Shell)null);
		setShellStyle(SWT.BORDER | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setBlockOnOpen(false);	
		mapProgressElement = new HashMap<Job, JobProgressElement>();
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		lblOperationInProgress = new Label(container, SWT.SMOOTH);
		lblOperationInProgress.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblOperationInProgress.setText("Operation in progress...");
		
		scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(1, false));
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		return container;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		this.getShell().setVisible(false);
	}
	
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Hide", true);
		
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(415, 250);
	}

	/**
	 * @return the runningTasks
	 */
	public int getRunningTasks() {
		return runningTasks;
	}

	/**
	 * @param runningTasks the runningTasks to set
	 */
	public void setRunningTasks(int runningTasks) {
		this.runningTasks = runningTasks;
	}
	
	public void requestShow(){
		Display.getCurrent().timerExec(500, new Runnable() {			
			@Override
			public void run() {			
				if(!mapProgressElement.isEmpty()){
					if(!GokoProgressDialog.this.getShell().isDisposed()){
						GokoProgressDialog.this.getShell().setVisible(true);
					}
				}
			}
		});
		//sync.asyncExec();		
	}
	
	public IProgressMonitor addJob(final Job job) {		
		if (job != null && job.isUser()) {
			sync.syncExec(new Runnable() {				
				@Override
				public void run() {
					if(!GokoProgressDialog.this.getShell().isDisposed()){
						JobProgressElement progressElement = new JobProgressElement(composite, SWT.NONE, sync, job, GokoProgressDialog.this);					
						mapProgressElement.put(job, progressElement);					
						progressElement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
						composite.pack();
						requestShow();
					}
				}
			});			
		}		
		return mapProgressElement.get(job);
	}
	
	public void remove(final JobProgressElement elt){
		mapProgressElement.remove(elt.getJob());
		sync.syncExec(new Runnable() {			
			@Override
			public void run() {				
				elt.dispose();
				if(mapProgressElement.isEmpty()){
					if(!GokoProgressDialog.this.getShell().isDisposed()){
						GokoProgressDialog.this.getShell().setVisible(false);
					}
				}
			}
		});		
	}
}
