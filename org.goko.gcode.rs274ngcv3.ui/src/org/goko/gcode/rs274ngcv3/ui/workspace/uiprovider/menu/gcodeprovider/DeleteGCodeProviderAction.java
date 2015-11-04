package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.AbstractDeleteAction;

public class DeleteGCodeProviderAction extends AbstractDeleteAction {
	/** GCode service */
	private IRS274NGCService rs274Service;
	
	public DeleteGCodeProviderAction(IRS274NGCService rs274Service, Integer idTarget) {
		super(idTarget);		
		this.rs274Service = rs274Service;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.AbstractDeleteAction#deleteById(java.lang.Integer)
	 */
	@Override
	protected void deleteById(Integer id) throws GkException {
		rs274Service.deleteGCodeProvider(id);
	}

}
