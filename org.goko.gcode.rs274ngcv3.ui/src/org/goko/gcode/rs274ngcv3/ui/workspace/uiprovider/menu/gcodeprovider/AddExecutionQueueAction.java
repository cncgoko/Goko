/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider;

import org.eclipse.jface.action.Action;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.log.GkLog;
import org.goko.gcode.rs274ngcv3.ui.workspace.RS274WorkspaceService;

/**
 * @author PsyKo
 * @date 22 nov. 2015
 */
public class AddExecutionQueueAction extends Action{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AddExecutionQueueAction.class);	
	/** Target GCode provider */
	private Integer idGCodeProvider;
	/** IRS274NGCService */
	private IGCodeProviderRepository gcodeProviderRepository;
	/** Target execution service*/
	private IExecutionService<?, ?> executionService;
	/**
	 * Constructor
	 * @param rs274WorkspaceService the {@link RS274WorkspaceService}
	 * @param idGCodeProvider the target GCodeProvider id
	 */
	public AddExecutionQueueAction(IGCodeProviderRepository gcodeProviderRepository, IExecutionService<?, ?> executionService, Integer idGCodeProvider) {
		super("Add to execution queue");
		this.executionService = executionService;
		this.idGCodeProvider = idGCodeProvider;
		this.gcodeProviderRepository = gcodeProviderRepository;		
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {		
		try {
			IGCodeProvider provider = gcodeProviderRepository.getGCodeProvider(idGCodeProvider);
			boolean exists = executionService.findExecutionTokenByGCodeProvider(ExecutionQueueType.DEFAULT, provider) != null;
			return !exists && provider.hasErrors() == false;
		} catch (GkException e) {
			LOG.error(e);
		}		
		return false;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {		
		try {
			executionService.addToExecutionQueue(gcodeProviderRepository.getGCodeProvider(idGCodeProvider));
		} catch (GkException e) {
			LOG.error(e);
		}
	}
}
