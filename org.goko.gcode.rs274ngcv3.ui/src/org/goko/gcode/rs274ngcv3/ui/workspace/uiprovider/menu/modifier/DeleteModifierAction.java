package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.action.AbstractDeleteAction;

public class DeleteModifierAction extends AbstractDeleteAction {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(DeleteModifierAction.class);
	/** GCode service */
	private IRS274NGCService rs274Service;
	
	public DeleteModifierAction(IRS274NGCService rs274Service, Integer idTarget) {
		super(idTarget);		
		this.rs274Service = rs274Service;
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		try {
			IModifier<GCodeProvider> modifier = rs274Service.getModifier(getIdTarget());
			IGCodeProvider provider = rs274Service.getGCodeProvider(modifier.getIdGCodeProvider());
			return !provider.isLocked();
		} catch (GkException e) {
			LOG.error(e);
			return false;
		}
		
	}
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.AbstractDeleteAction#deleteById(java.lang.Integer)
	 */
	@Override
	protected void deleteById(Integer id) throws GkException {
		rs274Service.deleteModifier(id);
	}

}
