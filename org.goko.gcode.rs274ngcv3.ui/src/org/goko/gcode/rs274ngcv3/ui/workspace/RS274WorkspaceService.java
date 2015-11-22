/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.IWorkspaceUIService;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.GCodeContainerUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public class RS274WorkspaceService implements IRS274WorkspaceService{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274WorkspaceService.class);
	/** Service ID */
	private static final String SERVICE_ID ="org.goko.gcode.rs274ngcv3.ui.workspace.RS274WorkspaceService";
	/** Modifier UI provider extension point */
	private static final String MODIFIER_UI_PROVIDER = "org.goko.gcode.rs274ngcv3.ui.modifier.factory";
	private static final String MODIFIER_UI_BUILDER = "modifierUiBuilder";
	/** Workspace UI service */
	private IWorkspaceUIService workspaceUIService;
	/** Workspace UI service */
	private IExecutionService<?,?> executionService;
	/** Workspace UI service */
	private IRS274NGCService gcodeService;
	/** Extension registry */
	private IExtensionRegistry extensionRegistry;
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {		
		LOG.info("Starting  "+getServiceId());
		getWorkspaceUIService().addProjectContainerUiProvider(new GCodeContainerUiProvider(getGcodeService(), this, executionService));
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

	}

	/**
	 * @return the workspaceUiService
	 */
	public IWorkspaceUIService getWorkspaceUIService() {
		return workspaceUIService;
	}

	/**
	 * @param workspaceUiService the workspaceUiService to set
	 */
	public void setWorkspaceUIService(IWorkspaceUIService workspaceUiService) {
		this.workspaceUIService = workspaceUiService;
	}

	/**
	 * @return the gcodeService
	 */
	public IRS274NGCService getGcodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGcodeService(IRS274NGCService gcodeService) {
		this.gcodeService = gcodeService;
	}
	
	/**
	 * @param registry the IExtensionRegistry to set
	 */
	public void setExtensionRegistry(IExtensionRegistry registry){
		this.extensionRegistry = registry;
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.IRS274WorkspaceService#getModifierBuilder()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IModifierUiProvider<GCodeProvider, ?>> getModifierBuilder() throws GkException{
		List<IModifierUiProvider<GCodeProvider, ?>> result = new ArrayList<IModifierUiProvider<GCodeProvider, ?>>();
		
		IConfigurationElement[] elts = extensionRegistry.getConfigurationElementsFor(MODIFIER_UI_PROVIDER);
		for (IConfigurationElement elt : elts){
			try {
				Object dynBuilder = elt.createExecutableExtension("class");
				result.add((IModifierUiProvider<GCodeProvider, ?>) dynBuilder);
			} catch (CoreException e) {
				throw new GkTechnicalException(e);
			}			
		}
		
		return result;
	}

	/**
	 * @return the executionService
	 */
	public IExecutionService<?, ?> getExecutionService() {
		return executionService;
	}

	/**
	 * @param executionService the executionService to set
	 */
	public void setExecutionService(IExecutionService<?, ?> executionService) {
		this.executionService = executionService;
	}
	
}
