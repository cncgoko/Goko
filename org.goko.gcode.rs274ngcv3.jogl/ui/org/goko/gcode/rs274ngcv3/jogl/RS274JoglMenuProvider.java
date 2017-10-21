/**
 * 
 */
package org.goko.gcode.rs274ngcv3.jogl;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.jogl.RS274NGCV3JoglService;
import org.goko.core.workspace.bean.IProjectMenuProvider;
import org.goko.core.workspace.service.IWorkspaceUIService;
import org.goko.gcode.rs274ngcv3.jogl.action.HideShowGCodeProviderAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.GCodeContainerUiProvider;

/**
 * @author Psyko
 * @date 21 oct. 2017
 */
public class RS274JoglMenuProvider implements IProjectMenuProvider {
	/** GCode service */
	private IRS274NGCService rs274Service;
	/** RS274NGCV3Jogl  service */
	private RS274NGCV3JoglService rs274JoglService;
	/** Workspace UI Service */
	private IWorkspaceUIService workspaceUIService;
	/**
	 */
	public RS274JoglMenuProvider() {
		super();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.IProjectMenuProvider#providesMenuFor(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public boolean providesMenuFor(ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();

		return content instanceof IGCodeProvider;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.IProjectMenuProvider#createMenuFor(org.eclipse.jface.action.IMenuManager, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void createMenuFor(IMenuManager contextMenu, ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();

		if(content instanceof IGCodeProvider){
			createMenuForGCodeProvider(contextMenu, (IGCodeProvider)content);
		}
	}
	
	/**
	 * Creates the menu for a GCode provider node of the tree
	 * @param contextMenu the target context menu
	 */
	protected void createMenuForGCodeProvider(IMenuManager contextMenu, final IGCodeProvider content) throws GkException {
		contextMenu.add(new Separator());
		
		contextMenu.add(new HideShowGCodeProviderAction(rs274Service, rs274JoglService, content.getId()));
	}

	/**
	 * @return the rs274Service
	 */
	public IRS274NGCService getRs274Service() {
		return rs274Service;
	}

	/**
	 * @param rs274Service the rs274Service to set
	 */
	public void setRs274Service(IRS274NGCService rs274Service) {
		this.rs274Service = rs274Service;
	}

	/**
	 * @return the rs274JoglService
	 */
	public RS274NGCV3JoglService getRs274JoglService() {
		return rs274JoglService;
	}

	/**
	 * @param rs274JoglService the rs274JoglService to set
	 */
	public void setRs274JoglService(RS274NGCV3JoglService rs274JoglService) {
		this.rs274JoglService = rs274JoglService;
	}

	/**
	 * @param workspaceUIService the workspaceUIService to set
	 * @throws GkException 
	 */
	public void setWorkspaceUIService(IWorkspaceUIService workspaceUIService) throws GkException {
		this.workspaceUIService = workspaceUIService;
		this.workspaceUIService.addProjectMenuProvider(GCodeContainerUiProvider.TYPE, this);
	}
	
	/**
	 * @return the workspaceUIService
	 */
	public IWorkspaceUIService getWorkspaceUIService() {
		return workspaceUIService;
	}
}
