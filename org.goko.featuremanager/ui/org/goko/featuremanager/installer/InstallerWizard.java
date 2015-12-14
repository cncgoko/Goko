package org.goko.featuremanager.installer;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.goko.core.log.GkLog;
import org.goko.featuremanager.service.IFeatureManager;

public class InstallerWizard extends Wizard {
	private static final GkLog LOG = GkLog.getLogger(InstallerWizard.class);
	private InstallerPageFeatureSelection installerPageFeatureSelection;	
	private InstallerPageOperationRunning installerPageOperationRunning	;
	private InstallerPageOperationStatus installerPageOperationStatus;	
	private IFeatureManager manager;
	private InstallerWizardModel model;	
	private UISynchronize uiSynchronize;
	
	public InstallerWizard(IFeatureManager manager, UISynchronize uiSynchronize) {
		this.manager = manager;		
		setNeedsProgressMonitor(true);
		model = new InstallerWizardModel();
		this.uiSynchronize = uiSynchronize;
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {		
		installerPageFeatureSelection = new InstallerPageFeatureSelection(manager, model);
		installerPageOperationRunning = new InstallerPageOperationRunning(manager, model, uiSynchronize);
		installerPageOperationStatus = new InstallerPageOperationStatus(manager, model);
		addPage(installerPageFeatureSelection);		
		addPage(installerPageOperationRunning);		
		addPage(installerPageOperationStatus);		
	}
	
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {		
		return null; // Disable going back
	}
		
	/** (inheritDoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {		
		return true;
	}

	public boolean isRestartRequired(){
		return model.isRestartRequired();
	}
	
	@Override
	public boolean canFinish() {		
		return installerPageOperationStatus.isPageComplete();
	}
	
	public void turnToNextPage(){
		IWizardPage page = getContainer().getCurrentPage();		
		IWizardPage nextPage = getNextPage(page);
		getContainer().showPage(nextPage);
	}
}
