/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.core.workspace.service.IWorkspaceService;
import org.goko.gcode.rs274ngcv3.ui.workspace.IRS274WorkspaceService;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.AddExecutionQueueAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.DeleteGCodeProviderAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.ExternalEditAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.ModifierSubMenu;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.ReloadGCodeProviderAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcoderepository.AddAllGCodeInQueueAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier.DeleteModifierAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier.EnableDisableAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public class GCodeContainerUiProvider extends ProjectContainerUiProvider {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GCodeContainerUiProvider.class);
	/** GCode service */
	private IRS274NGCService rs274Service;
	private IRS274WorkspaceService rs274WorkspaceService;
	private IWorkspaceService workspaceService;
	private IExecutionService<?, ?> executionService;
	private IStyledLabelProvider labelProvider;

	/**
	 * @param rs274Service
	 * @param type
	 */
	public GCodeContainerUiProvider(IRS274NGCService rs274Service, IRS274WorkspaceService rs274WorkspaceService, IExecutionService<?, ?> executionService, IWorkspaceService workspaceService) {
		super("GCodeContainerUiProvider", 10);
		this.rs274Service = rs274Service;
		this.rs274WorkspaceService = rs274WorkspaceService;
		this.labelProvider = new GCodeContainerLabelProvider();
		this.executionService = executionService;
		this.workspaceService = workspaceService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesLabelFor(java.lang.Object)
	 */
	@Override
	public boolean providesLabelFor(Object content) throws GkException {
		return this.equals(content)
			|| (content instanceof IGCodeProvider)
			|| (content instanceof IModifier);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		return labelProvider.getStyledText(element);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return labelProvider.getImage(element);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesContentFor(java.lang.Object)
	 */
	@Override
	public boolean providesContentFor(Object content) throws GkException {
		return this.equals(content) || content instanceof GCodeProvider;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object content) throws GkException {
		if(content instanceof GCodeProvider){
			List<IModifier<GCodeProvider>> lst = rs274Service.getModifierByGCodeProvider(((GCodeProvider) content).getId());
			return CollectionUtils.isNotEmpty(lst);
		}else if(this.equals(content)){
			return CollectionUtils.isNotEmpty(rs274Service.getGCodeProvider());
		}
		return false;
	}


	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object content) throws GkException {
		if(content instanceof GCodeProvider){
			return rs274Service.getModifierByGCodeProvider(((GCodeProvider) content).getId()).toArray();
		}else if(this.equals(content)){
			return rs274Service.getGCodeProvider().toArray();
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object content) throws GkException {
		if(content instanceof GCodeProvider){
			return this;
		}else if(content instanceof IModifier){
			IModifier<?> modifier = (IModifier<?>)content;
			return rs274Service.getGCodeProvider(modifier.getIdGCodeProvider());
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesMenuFor(java.lang.Object)
	 */
	@Override
	public boolean providesMenuFor(ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		return this.equals(content)
			|| (content instanceof GCodeProvider)
			|| (content instanceof IModifier);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#createMenuFor(org.eclipse.jface.action.IMenuManager, java.lang.Object)
	 */
	@Override
	public void createMenuFor(IMenuManager contextMenu, ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();

		if(this.equals(content)){
			createMenuForGCodeRepository(contextMenu);
		}else if(content instanceof GCodeProvider){
			createMenuForGCodeProvider(contextMenu, (GCodeProvider)content);
		}else if(content instanceof IModifier<?>){
			createMenuForGCodeModifier(contextMenu, (IModifier<?>)content);
		}
	}

	/**
	 * Creates the menu for the GCode repository node of the tree
	 * @param contextMenu the target context menu
	 */
	private void createMenuForGCodeRepository(IMenuManager contextMenu) {
		contextMenu.add(new AddAllGCodeInQueueAction(executionService, rs274Service));
	}

	/**
	 * Creates the menu for a GCode modifier node of the tree
	 * @param contextMenu the target context menu
	 */
	private void createMenuForGCodeModifier(IMenuManager contextMenu, IModifier<?> modifier) {
		contextMenu.add(new EnableDisableAction(rs274Service, modifier.getId()));
		contextMenu.add(new Separator());
		contextMenu.add(new DeleteModifierAction(rs274Service, modifier.getId()));
	}

	/**
	 * Creates the menu for a GCode provider node of the tree
	 * @param contextMenu the target context menu
	 */
	protected void createMenuForGCodeProvider(IMenuManager contextMenu, final GCodeProvider content) throws GkException {
		// Submenu for a specific user
        MenuManager subMenu = new ModifierSubMenu(rs274Service, rs274WorkspaceService, content.getId());
        

        contextMenu.add(new AddExecutionQueueAction(rs274Service, executionService, content.getId()));
        contextMenu.add(new Separator());
        contextMenu.add(new ReloadGCodeProviderAction(rs274Service, content.getId()));
        contextMenu.add(new ExternalEditAction(rs274Service, workspaceService, content.getId()));
        contextMenu.add(subMenu);       
        contextMenu.add(new Separator());
        contextMenu.add(new DeleteGCodeProviderAction(rs274Service, content.getId()));
	}


	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesConfigurationPanelFor(java.lang.Object)
	 */
	@Override
	public boolean providesConfigurationPanelFor(ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();

		if(content instanceof IModifier<?>){
			IModifier<?> iModifier = (IModifier<?>) content;

			List<IModifierUiProvider<?>> lstBuilders = rs274WorkspaceService.getModifierBuilder();
			if(CollectionUtils.isNotEmpty(lstBuilders)){
				for (IModifierUiProvider<?> iModifierUiProvider : lstBuilders) {
					if(iModifierUiProvider.providesConfigurationPanelFor(iModifier)){
						return true;
					}
				}
			}
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#createConfigurationPanelFor(org.eclipse.swt.widgets.Composite, java.lang.Object)
	 */
	@Override
	public IPropertiesPanel createConfigurationPanelFor(Composite parent, ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();

		if(content instanceof IModifier<?>){
			IModifier<?> iModifier = (IModifier<?>) content;

			List<IModifierUiProvider<?>> lstBuilders = rs274WorkspaceService.getModifierBuilder();
			if(CollectionUtils.isNotEmpty(lstBuilders)){
				for (IModifierUiProvider<?> iModifierUiProvider : lstBuilders) {
					if(iModifierUiProvider.providesConfigurationPanelFor(iModifier)){
						IModifierPropertiesPanel<?> panel = iModifierUiProvider.createConfigurationPanelFor(parent, iModifier);
						panel.initializeFromModifier();
						return panel;
					}
				}
			}
		}
		return null;
	}

}
