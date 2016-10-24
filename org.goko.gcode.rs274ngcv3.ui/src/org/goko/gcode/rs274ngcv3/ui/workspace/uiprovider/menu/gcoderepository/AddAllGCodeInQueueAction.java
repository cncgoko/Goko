package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcoderepository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.action.Action;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.log.GkLog;

/**
 * Action to add all the gcode provider to the queue 
 * 
 * @author Psyko
 *
 */
public class AddAllGCodeInQueueAction extends Action {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AddAllGCodeInQueueAction.class);
	/** Target execution service */
	private IExecutionService<?,?> executionService;
	/** Target GCode service */
	private IGCodeProviderRepository gcodeRepository;
	
	/**
	 * Constructor
	 * @param executionService the execution service 
	 * @param gcodeService the gcode service
	 */
	public AddAllGCodeInQueueAction(IExecutionService<?, ?> executionService, IGCodeProviderRepository gcodeRepository) {
		super("Add all to execution queue");
		this.executionService = executionService;
		this.gcodeRepository = gcodeRepository;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		boolean enabled = false;
		try {
			// Enabled when not running
			enabled = !ExecutionState.RUNNING.equals(executionService.getExecutionState());
		} catch (GkException e) {
			LOG.error(e);
		}
		return enabled;
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {		
		try {
			List<IGCodeProvider> lstProvider = gcodeRepository.getGCodeProvider();
			if(CollectionUtils.isNotEmpty(lstProvider)){
				for (IGCodeProvider gcodeProvider : lstProvider) {
					if(executionService.findExecutionTokenByGCodeProvider(gcodeProvider) == null){
						executionService.addToExecutionQueue(gcodeProvider);
					}
				}
			}
		} catch (GkException e) {
			LOG.error(e);
		}		
	}
}
