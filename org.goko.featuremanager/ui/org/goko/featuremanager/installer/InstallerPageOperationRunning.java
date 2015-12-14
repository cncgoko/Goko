package org.goko.featuremanager.installer;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.featuremanager.service.IFeatureManager;

public class InstallerPageOperationRunning extends WizardPage implements IJobChangeListener {
	private static final GkLog LOG = GkLog.getLogger(InstallerPageOperationRunning.class);
	private IFeatureManager manager;	
	private Label descriptionLabel;
	private InstallerWizardModel model;
	private UISynchronize uiSynchronize;
	
	/**
	 * Create the wizard.
	 */
	public InstallerPageOperationRunning(IFeatureManager manager, InstallerWizardModel model, UISynchronize uiSynchronize) {		
		super("featureSelection");		
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("org.goko.featuremanager", "resources/icons/arrow-270.png"));
		this.model = model;
		this.manager = manager;
		this.uiSynchronize = uiSynchronize; 
		setTitle("Installing...");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		descriptionLabel = new Label(container, SWT.NONE);
		descriptionLabel.setText("Requested features are being installed... Please wait");
		descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		setPageComplete(false);
	}
		
	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {		
		super.setVisible(visible);
		if(visible){ // Bad hack for running install 
			runInstall();
		}
				
	}
	
	protected void runInstall(){
		try {
			getContainer().run(false, false, new IRunnableWithProgress() {				
				@Override
				public void run(IProgressMonitor wizardMonitor) throws InvocationTargetException, InterruptedException {
					try {				
						UIProgressMonitor uiMonitor = new UIProgressMonitor(uiSynchronize, wizardMonitor);
						manager.install(model.getUnitsToInstall(), uiMonitor, InstallerPageOperationRunning.this);
						model.getUnitsToInstall().clear();
					} catch (GkException e) {
						LOG.error(e);
					}	
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			LOG.error(e);
		}	
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
		uiSynchronize.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				LOG.info("Installation Job done");
				InstallerWizard wizard = (InstallerWizard)getWizard();		
				model.setOperationResult(event.getResult());
				setPageComplete(true);
				wizard.turnToNextPage();
			}
		});			
	}

	@Override
	public void running(IJobChangeEvent event) {
		// TODO Auto-generated method stub
		
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
}
