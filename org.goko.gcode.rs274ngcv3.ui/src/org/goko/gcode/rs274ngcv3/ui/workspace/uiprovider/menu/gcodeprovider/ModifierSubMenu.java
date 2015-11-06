package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.IRS274WorkspaceService;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.ModifierUiProviderSorter;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.ModifierUiProviderSorter.EnumModifierUiProviderSort;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

public class ModifierSubMenu extends MenuManager {
	/** IRS274NGCService */
	private IRS274NGCService rs274Service;
	/** IRS274WorkspaceService */
	private IRS274WorkspaceService rs274WorkspaceService;
	/** Id of the target GCode Provider */
	private Integer idGCodeProvider;

	public ModifierSubMenu(IRS274NGCService rs274Service, IRS274WorkspaceService rs274WorkspaceService, Integer idGCodeProvider) throws GkException {
		super("Modifiers");
		this.rs274Service = rs274Service;
		this.rs274WorkspaceService = rs274WorkspaceService;
		this.idGCodeProvider = idGCodeProvider;
		buildMenu();		
	}

	/**
	 * Builds the actual menu
	 * @throws GkException GkException
	 */
	private void buildMenu() throws GkException {
		List<IModifierUiProvider<GCodeProvider, ?>> lstBuilders = rs274WorkspaceService.getModifierBuilder();
		
		Collections.sort(lstBuilders, new ModifierUiProviderSorter(EnumModifierUiProviderSort.ALPHABETICAL));
		
        for (final IModifierUiProvider<GCodeProvider, ?> iModifierUiProvider : lstBuilders) {
			// Actions for the sub menu
			add(new CreateModifierAction(rs274Service, iModifierUiProvider, idGCodeProvider));
		}
	}
	
	
}
