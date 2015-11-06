package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider;

import org.eclipse.jface.action.Action;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.log.GkLog;
import org.goko.gcode.rs274ngcv3.ui.workspace.RS274WorkspaceService;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

public class CreateModifierAction extends Action{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(CreateModifierAction.class);
	/** Modifier provider */
	private IModifierUiProvider<GCodeProvider, ?>  modifierUiProvider;
	/** Target GCode provider */
	private Integer idGCodeProvider;
	/** IRS274NGCService */
	private IRS274NGCService rs274Service;
	
	/**
	 * Constructor
	 * @param rs274WorkspaceService the {@link RS274WorkspaceService}
	 * @param idGCodeProvider the target GCodeProvider id
	 */
	public CreateModifierAction(IRS274NGCService rs274Service, IModifierUiProvider<GCodeProvider, ?> modifierUiProvider, Integer idGCodeProvider) {
		super(modifierUiProvider.getModifierName());
		this.modifierUiProvider = modifierUiProvider;
		this.idGCodeProvider = idGCodeProvider;
		this.rs274Service = rs274Service;
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		try {
			rs274Service.addModifier(modifierUiProvider.createDefaultModifier(idGCodeProvider));
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	
}
