package org.goko.featuremanager.installer;

import org.eclipse.jface.viewers.ICheckStateProvider;

public class InstallableUnitCheckStateProvider implements ICheckStateProvider {

	@Override
	public boolean isChecked(Object element) {
		if(element instanceof GkInstallableUnit){
			return ((GkInstallableUnit) element).isInstalled();
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.ICheckStateProvider#isGrayed(java.lang.Object)
	 */
	@Override
	public boolean isGrayed(Object element) {		
		return false;
	}

}
