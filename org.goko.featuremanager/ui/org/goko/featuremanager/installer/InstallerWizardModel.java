package org.goko.featuremanager.installer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;

public class InstallerWizardModel {
	private List<GkInstallableUnit> unitsToInstall;
	private boolean restartRequired;
	private IStatus operationResult;
	
	public InstallerWizardModel() {
		super();
		unitsToInstall = new ArrayList<GkInstallableUnit>();
	}

	/**
	 * @return the unitsToInstall
	 */
	public List<GkInstallableUnit> getUnitsToInstall() {
		return unitsToInstall;
	}

	/**
	 * @param unitsToInstall the unitsToInstall to set
	 */
	public void setUnitsToInstall(List<GkInstallableUnit> unitsToInstall) {
		this.unitsToInstall = unitsToInstall;
	}

	/**
	 * @return the restartRequired
	 */
	public boolean isRestartRequired() {
		return restartRequired;
	}

	/**
	 * @param restartRequired the restartRequired to set
	 */
	public void setRestartRequired(boolean restartRequired) {
		this.restartRequired = restartRequired;
	}

	/**
	 * @return the operationResult
	 */
	public IStatus getOperationResult() {
		return operationResult;
	}

	/**
	 * @param operationResult the operationResult to set
	 */
	public void setOperationResult(IStatus operationResult) {
		this.operationResult = operationResult;
	}
	
}
