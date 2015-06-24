package org.goko.featuremanager.service;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.featuremanager.installer.GkInstallableUnit;

public interface IFeatureManager extends IGokoService {
	
	List<GkInstallableUnit> getInstallableUnits(IProgressMonitor monitor) throws GkException;
	
	IStatus install(List<GkInstallableUnit> units, IProgressMonitor monitor,  IJobChangeListener listener) throws GkException;
	
}
