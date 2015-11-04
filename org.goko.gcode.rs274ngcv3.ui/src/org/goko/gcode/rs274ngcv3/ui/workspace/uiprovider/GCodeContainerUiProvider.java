/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.ProjectContainer;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.IRS274WorkspaceService;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.DeleteGCodeProviderAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier.DeleteModifierAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier.EnableDisableAction;

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
	private IStyledLabelProvider labelProvider;

	/**
	 * @param rs274Service 
	 * @param type
	 */
	public GCodeContainerUiProvider(IRS274NGCService rs274Service, IRS274WorkspaceService rs274WorkspaceService) {
		super("TEST");
		this.rs274Service = rs274Service;
		this.rs274WorkspaceService = rs274WorkspaceService;
		this.labelProvider = new GCodeContainerLabelProvider();
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesLabelFor(java.lang.Object)
	 */
	@Override
	public boolean providesLabelFor(Object content) throws GkException {
		if(content instanceof ProjectContainer){
			return StringUtils.equals(getType(), ((ProjectContainer) content).getType());
		}
		return (content instanceof IGCodeProvider)
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
		if(content instanceof ProjectContainer){
			return StringUtils.equals(getType(), ((ProjectContainer) content).getType());
		}
		return content instanceof GCodeProvider;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object content) throws GkException {
		if(content instanceof GCodeProvider){
			return CollectionUtils.isNotEmpty(rs274Service.getModifierByGCodeProvider(((GCodeProvider) content).getId()));
		}else if(content instanceof ProjectContainer){			
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
		}else if(content instanceof ProjectContainer){			
			return rs274Service.getGCodeProvider().toArray();			
		}
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object content) throws GkException {		
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesMenuFor(java.lang.Object)
	 */
	@Override
	public boolean providesMenuFor(ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		if(content instanceof ProjectContainer){
			return StringUtils.equals(getType(), ((ProjectContainer) content).getType());
		}
		return (content instanceof GCodeProvider)
			|| (content instanceof IModifier);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#createMenuFor(org.eclipse.jface.action.IMenuManager, java.lang.Object)
	 */
	@Override
	public void createMenuFor(IMenuManager contextMenu, ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		if(content instanceof GCodeProvider){
			createMenuForGCodeProvider(contextMenu, (GCodeProvider)content);
		}else if(content instanceof IModifier<?>){
			createMenuForGCodeModifier(contextMenu, (IModifier<?>)content);
		}
	}
	
	private void createMenuForGCodeModifier(IMenuManager contextMenu, IModifier<?> modifier) {
		contextMenu.add(new EnableDisableAction(rs274Service, modifier.getId()));
		contextMenu.add(new DeleteModifierAction(rs274Service, modifier.getId()));		
	}

	protected void createMenuForGCodeProvider(IMenuManager contextMenu, final GCodeProvider content) throws GkException {		
		contextMenu.add(new DeleteGCodeProviderAction(rs274Service, content.getId()));
		
		// submenu for a specific user
        MenuManager subMenu = new MenuManager("Modifier", null);
        
        List<IModifierUiProvider<GCodeProvider, ?>> lstBuilders = rs274WorkspaceService.getModifierBuilder();
        for (final IModifierUiProvider<GCodeProvider, ?> iModifierUiProvider : lstBuilders) {
			// Actions for the sub menu
			subMenu.add(new Action(iModifierUiProvider.getModifierName()) {
				/**
				 * (inheritDoc)
				 * 
				 * @see org.eclipse.jface.action.Action#run()
				 */
				@Override
				public void run() {
					try {
						rs274Service.addModifier(iModifierUiProvider.createDefaultModifier(content.getId()));
					} catch (GkException e) {
						LOG.error(e);
					}
				}
			});
		}
       
//a completer avec un referentiel des modifiers		
		// add the action to the submenu
        contextMenu.add(subMenu);
	}
}
