package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider;

import org.eclipse.jface.action.Action;
import org.goko.common.dialog.GkDialog;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;
import org.goko.gcode.rs274ngcv3.ui.workspace.RS274WorkspaceService;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

public class CreateModifierAction extends Action{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(CreateModifierAction.class);
	/** Modifier provider */
	private IModifierUiProvider<?>  modifierUiProvider;
	/** Target GCode provider */
	private Integer idGCodeProvider;
	/** IRS274NGCService */
	private IRS274NGCService rs274Service;

	/**
	 * Constructor
	 * @param rs274WorkspaceService the {@link RS274WorkspaceService}
	 * @param idGCodeProvider the target GCodeProvider id
	 */
	public CreateModifierAction(IRS274NGCService rs274Service, IModifierUiProvider<?> modifierUiProvider, Integer idGCodeProvider) {
		super(modifierUiProvider.getModifierName());
		this.modifierUiProvider = modifierUiProvider;
		this.idGCodeProvider = idGCodeProvider;
		this.rs274Service = rs274Service;		
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		boolean locked = true;
		try {
			locked = rs274Service.getGCodeProvider(idGCodeProvider).isLocked();
		} catch (GkException e) {
			LOG.error(e);			
		}
		return !locked;
	}
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		IModifier<GCodeProvider> modifier = null;
		try {
			modifier = modifierUiProvider.createDefaultModifier(idGCodeProvider);
			rs274Service.addModifier(modifier);
		} catch (GkException e) {
			if(modifier != null){
				try {
					rs274Service.deleteModifier(modifier);
				} catch (GkException e1) {
					LOG.error(e1);
				}
			}
			LOG.error(e);
			GkDialog.openDialog(null, e);			 
		}
	}

}
