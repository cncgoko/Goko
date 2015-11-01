/**
 * 
 */
package org.goko.core.workspace.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;

/**
 * @author PsyKo
 * @date 30 oct. 2015
 */
public class WorkspaceUIService implements IGokoService, IWorkspaceUIService {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(WorkspaceService.class);
	/** Service ID */
	private static final String SERVICE_ID ="org.goko.core.workspace.WorkspaceUIService";
	private Map<String, ProjectContainerUiProvider> mapProjectContainerUiProvider;
	
	/**
	 * 
	 */
	public WorkspaceUIService() {
		
	}
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
		this.mapProjectContainerUiProvider = new HashMap<String, ProjectContainerUiProvider>();		
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#findProjectContainerUiProvider(java.lang.String)
	 */
	@Override
	public ProjectContainerUiProvider findProjectContainerUiProvider(String type)  throws GkTechnicalException{
		return mapProjectContainerUiProvider.get(type);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#getProjectContainerUiProvider(java.lang.String)
	 */
	@Override
	public ProjectContainerUiProvider getProjectContainerUiProvider(String type) throws GkTechnicalException{
		ProjectContainerUiProvider result = findProjectContainerUiProvider(type);
		if(result == null){
			throw new GkTechnicalException("ProjectContainerUiProvider not found for type ["+type+"]");
		}
		return result;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#existProjectContainerUiProvider(java.lang.String)
	 */
	@Override
	public boolean existProjectContainerUiProvider(String type)  throws GkTechnicalException{
		return mapProjectContainerUiProvider.containsKey(type);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#addProjectContainerUiProvider(org.goko.core.workspace.bean.ProjectContainerUiProvider)
	 */
	@Override
	public void addProjectContainerUiProvider(ProjectContainerUiProvider provider)  throws GkTechnicalException{	
		mapProjectContainerUiProvider.put(provider.getType(), provider);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceUIService#getProjectContainerUiProvider()
	 */
	@Override
	public List<ProjectContainerUiProvider> getProjectContainerUiProvider() throws GkTechnicalException {		
		return new ArrayList<ProjectContainerUiProvider>(mapProjectContainerUiProvider.values());
	}
}
