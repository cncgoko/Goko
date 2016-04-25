/**
 * 
 */
package org.goko.controller.grbl.v09;

import java.util.ArrayList;
import java.util.List;

import org.goko.controller.grbl.v09.bean.IGrblStateChangeListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 24 avr. 2016
 */
public class GrblResetOnHoldListener implements IGrblStateChangeListener {
	private static final GkLog LOG = GkLog.getLogger(GrblResetOnHoldListener.class);
	private GrblControllerService grblService;
	private GrblCommunicator communicator;
	
	/**
	 * @param grblService
	 * @param communicator
	 */
	public GrblResetOnHoldListener(GrblControllerService grblService, GrblCommunicator communicator) {
		super();
		this.grblService = grblService;
		this.communicator = communicator;
	}


	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.bean.IGrblStateChangeListener#execute()
	 */
	@Override
	public void execute() {
		try{
			if(GrblMachineState.HOLD.equals(grblService.getState())){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					LOG.error(e);
				}		
				List<Byte> resetCommand = new ArrayList<Byte>();
				resetCommand.add(Grbl.RESET_COMMAND);
				communicator.sendImmediately(resetCommand);
				
				// Remove itself from listening to the state change
				grblService.removeStateListener(this);
			}	
		}catch(GkException e){
			LOG.error(e);
		}
	}

}
