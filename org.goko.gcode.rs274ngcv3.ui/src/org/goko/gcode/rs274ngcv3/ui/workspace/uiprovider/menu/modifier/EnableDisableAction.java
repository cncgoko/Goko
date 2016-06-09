package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier;

import org.eclipse.jface.action.Action;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;

public class EnableDisableAction extends Action {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(EnableDisableAction.class);
	/** GCode service */
	private IRS274NGCService rs274Service;
	/** Target modifier */
	private Integer idModifier;

	public EnableDisableAction(IRS274NGCService rs274Service, Integer idModifier) {
		super("Enable", Action.AS_CHECK_BOX);
		this.idModifier = idModifier;
		this.rs274Service = rs274Service;
		try {
			IModifier<GCodeProvider> modifier = rs274Service.getModifier(idModifier);
			setChecked(modifier.isEnabled());
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		try {
			IModifier<GCodeProvider> modifier = rs274Service.getModifier(idModifier);
			IGCodeProvider provider = rs274Service.getGCodeProvider(modifier.getIdGCodeProvider());
			return !provider.isLocked();
		} catch (GkException e) {
			LOG.error(e);
			return false;
		}
		
	}
	/**
	 * (inheritDoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		try {
			IModifier<GCodeProvider> modifier = rs274Service.getModifier(idModifier);
			modifier.setEnabled(isChecked());
			rs274Service.updateModifier(modifier);
		} catch (GkException e) {
			LOG.error(e);
		}
	}
}
