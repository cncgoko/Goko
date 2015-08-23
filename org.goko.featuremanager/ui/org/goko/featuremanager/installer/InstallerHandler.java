package org.goko.featuremanager.installer;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.featuremanager.service.IFeatureManager;

public class InstallerHandler {

	@Execute
	public void execute(final Shell shell, final IFeatureManager manager, final IWorkbench workbench, final UISynchronize uiSynchronize) {
		shell.getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				InstallerWizard installerWizar = new InstallerWizard(manager, uiSynchronize);
				WizardDialog dialog = new WizardDialog(shell, installerWizar);
				
				int result = dialog.open(); 	
				if(result ==  Dialog.OK || installerWizar.isRestartRequired()){
					boolean restart = MessageDialog.openQuestion(shell, "Restart required","The application need to restart to apply the changes. Do you want to restart now ?");
					if(restart){
						workbench.restart();
					}
				}
			}
		});
		
	}
	
	@CanExecute
	public boolean canExecute(){
		return true;
	}
}
