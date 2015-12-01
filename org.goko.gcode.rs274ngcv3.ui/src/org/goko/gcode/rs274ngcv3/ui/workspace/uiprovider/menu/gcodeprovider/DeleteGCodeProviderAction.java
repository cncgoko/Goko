package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.workspace.action.AbstractDeleteAction;

public class DeleteGCodeProviderAction extends AbstractDeleteAction {
	/** GCode service */
	private IGCodeProviderRepository gcodeProviderRepository;
	
	public DeleteGCodeProviderAction(IGCodeProviderRepository gcodeProviderRepository, Integer idTarget) {
		super(idTarget);		
		this.gcodeProviderRepository = gcodeProviderRepository;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.AbstractDeleteAction#deleteById(java.lang.Integer)
	 */
	@Override
	protected void deleteById(Integer id) throws GkException {
		gcodeProviderRepository.deleteGCodeProvider(id);
	}

}
