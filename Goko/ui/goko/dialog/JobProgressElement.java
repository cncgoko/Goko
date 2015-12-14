/**
 *
 */
package goko.dialog;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.dialog.GkDialog;

/**
 * @author PsyKo
 *
 */
public class JobProgressElement extends Composite implements IProgressMonitor,IJobChangeListener {
	private UISynchronize sync;
	private ProgressBar jobProgressBar;
	private Label lblTaskName;
	private GokoProgressDialog parentMonitor;
	private Job job;
	private Label lblJobName;

	/**
	 * @param parent
	 * @param style
	 */
	public JobProgressElement(Composite parent, int style) {
		super(parent, style);
		createContent();
	}

	public JobProgressElement(Composite parent, int style, UISynchronize sync, Job job, GokoProgressDialog parentMonitor) {
		super(parent, style);
		this.sync = sync;
		this.parentMonitor = parentMonitor;
		this.job = job;
		createContent();
		job.addJobChangeListener(this);
	}

	private void createContent(){
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 2;
		setLayout(gridLayout);

		lblJobName = new Label(this, SWT.NONE);
		GridData gd_lblJobName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lblJobName.verticalIndent = 2;
		gd_lblJobName.horizontalIndent = 2;
		lblJobName.setLayoutData(gd_lblJobName);
		lblJobName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblJobName.setText(job.getName());

		jobProgressBar = new ProgressBar(this, SWT.NONE);
		jobProgressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblTaskName = new Label(this, SWT.NONE);
		lblTaskName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblTaskName.setText("taskName");
		lblTaskName.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		lblTaskName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1));
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String, int)
	 */
	@Override
	public void beginTask(final String name, final int totalWork) {
		System.err.println("beginTask "+name);
		sync.asyncExec(new Runnable() {

			@Override
			public void run() {
				if(!isDisposed()){
					parentMonitor.requestShow();
					jobProgressBar.setSelection(0);
					jobProgressBar.setMaximum(totalWork);
					lblTaskName.setText(name);
				}
			}
		});
	}
	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#done()
	 */
	@Override
	public void done() {
		System.err.println("done()");
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
	 */
	@Override
	public void internalWorked(final double work) {
		sync.asyncExec(new Runnable() {

			@Override
			public void run() {
				if(!isDisposed()){
					jobProgressBar.setSelection((int) (jobProgressBar.getSelection()+work));
				}
			}
		});
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		return false;
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
	 */
	@Override
	public void setCanceled(boolean value) {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
	 */
	@Override
	public void setTaskName(final String name) {
		sync.asyncExec(new Runnable() {

			@Override
			public void run() {
				if(!isDisposed()){
					lblTaskName.setText(name);
				}
			}
		});
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
	 */
	@Override
	public void subTask(final String name) {
		sync.asyncExec(new Runnable() {

			@Override
			public void run() {
				if(!isDisposed()){
					lblTaskName.setText(name);
				}
			}
		});
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
	 */
	@Override
	public void worked(final int work) {
		sync.asyncExec(new Runnable() {

			@Override
			public void run() {
				jobProgressBar.setSelection(jobProgressBar.getSelection() + work);
			}
		});
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#aboutToRun(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	@Override
	public void aboutToRun(IJobChangeEvent event) {
		// TODO Auto-generated method stub
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#awake(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	@Override
	public void awake(IJobChangeEvent event) {
		// TODO Auto-generated method stub
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#done(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	@Override
	public void done(final IJobChangeEvent event) {
		sync.asyncExec(new Runnable() {

			@Override
			public void run() {
				if(!isDisposed()){
					if(event.getResult() != null && Status.OK_STATUS.equals(event.getResult())){
						lblTaskName.setText("Complete " +job.getName());
					}
					if(event.getResult() != null && event.getResult().getSeverity() == Status.ERROR){
						lblTaskName.setText("Error " +job.getName());
						GkDialog.openDialog(null, event.getResult());
					}
				}
			}
		});
		event.getJob().removeJobChangeListener(this);
		parentMonitor.remove(this);
	}

	/** (inheritDoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if(job != null){
			job.removeJobChangeListener(this);
		}
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#running(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	@Override
	public void running(IJobChangeEvent event) {
		sync.asyncExec(new Runnable() {

			@Override
			public void run() {
				if(!isDisposed()){
					lblTaskName.setText("Running " +job.getName());
				}
			}
		});
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#scheduled(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	@Override
	public void scheduled(IJobChangeEvent event) {
		// TODO Auto-generated method stub
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#sleeping(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	@Override
	public void sleeping(IJobChangeEvent event) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the job
	 */
	public Job getJob() {
		return job;
	}

	/**
	 * @param job the job to set
	 */
	public void setJob(Job job) {
		this.job = job;
	}
}
