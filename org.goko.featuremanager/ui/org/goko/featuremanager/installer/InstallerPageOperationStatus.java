package org.goko.featuremanager.installer;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.log.GkLog;
import org.goko.featuremanager.service.IFeatureManager;

public class InstallerPageOperationStatus extends WizardPage {
	private static final GkLog LOG = GkLog.getLogger(InstallerPageOperationStatus.class);
	private IFeatureManager manager;
	private String operationDescription;
	private Label descriptionLabel;
	private InstallerWizardModel model;
	
	
	/**
	 * Create the wizard.
	 */
	public InstallerPageOperationStatus(IFeatureManager manager, InstallerWizardModel model) {		
		super("featureSelection");		
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("org.goko.featuremanager", "resources/icons/arrow-270.png"));
		this.model = model;
		this.manager = manager;		 
		setTitle("Installation result");
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
		descriptionLabel.setText("Test");
		descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		setPageComplete(false);
	}
		
	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {		
		super.setVisible(visible);
		updateOperationResult();				
	}
	
	protected void updateOperationResult(){
		IStatus status = model.getOperationResult();
		if(status.getCode() == IStatus.OK){
			setOperationDescription("Installation complete.");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor("org.goko.featuremanager", "resources/icons/success.png"));
			setPageComplete(true);
		}else{
			setImageDescriptor(ResourceManager.getPluginImageDescriptor("org.goko.featuremanager", "resources/icons/error.png"));
			StringBuffer strStatus = new StringBuffer();
			for (IStatus iStatus : status.getChildren()) {
				strStatus.append(iStatus.getMessage());
				strStatus.append('\n');
			}
			setOperationDescription(strStatus.toString());
			setPageComplete(false);
		}
	}
	
	/**
	 * @return the operationDescription
	 */
	public String getOperationDescription() {
		return operationDescription;
	}

	/**
	 * @param operationDescription the operationDescription to set
	 */
	public void setOperationDescription(String operationDescription) {
		this.operationDescription = operationDescription;
		final String tmpOperationDescription = this.operationDescription;
		Display.getCurrent().asyncExec(new Runnable() {			
			@Override
			public void run() {
				descriptionLabel.setText(StringUtils.defaultString(tmpOperationDescription));
				descriptionLabel.pack();
			}
		});
	}
}
