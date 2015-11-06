/**
 * 
 */
package org.goko.core.workspace.service;

import java.util.List;

import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public interface IWorkspaceUIService extends IGokoService {

	ProjectContainerUiProvider findProjectContainerUiProvider(String type) throws GkTechnicalException;
	
	ProjectContainerUiProvider getProjectContainerUiProvider(String type) throws GkTechnicalException;
	
	List<ProjectContainerUiProvider> getProjectContainerUiProvider() throws GkTechnicalException;
	
	boolean existProjectContainerUiProvider(String type) throws GkTechnicalException;

	void addProjectContainerUiProvider(ProjectContainerUiProvider provider) throws GkTechnicalException;

}