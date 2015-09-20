/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.gcode.bean.provider;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.execution.IGCodeStreamedExecutionToken;
import org.goko.core.log.GkLog;

/**
 * Implementation of a {@link IGCodeProvider} for execution planner on a distant controller
 *
 * @author PsyKo
 *
 */
public class GCodeStreamedExecutionToken  extends GCodeExecutionToken implements IGCodeStreamedExecutionToken{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GCodeStreamedExecutionToken.class);
	/** The map of sent commands */
	protected Map<Integer, GCodeCommand> mapSentCommandById;
	/** The map of confirmed commands */
	protected Map<Integer, GCodeCommand> mapConfirmedCommandById;

	/**
	 * Constructor
	 * @param provider the base provider
	 */
	public GCodeStreamedExecutionToken(IGCodeProvider provider) {
		super(provider);
		this.mapSentCommandById		 = new LinkedHashMap<Integer, GCodeCommand>();
		this.mapConfirmedCommandById = new LinkedHashMap<Integer, GCodeCommand>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeStreamedExecutionToken#markAsSent(java.lang.Integer)
	 */
	@Override
	public void markAsSent(Integer idCommand) throws GkException {
		GCodeCommand command = getCommandById(idCommand);
		this.mapSentCommandById.put(command.getId(), command);
		mapStateByIdCommand.put(command.getId(), GCodeCommandState.SENT);
		notifyListeners(new GCodeCommandExecutionEvent(this, command, GCodeCommandState.SENT));
		if(isMonitorService()){
			getMonitorService().notifyCommandStateChanged(this, idCommand);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeStreamedExecutionToken#getSentCommandCount()
	 */
	@Override
	public int getSentCommandCount() throws GkException {
		return this.mapSentCommandById.size();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeStreamedExecutionToken#markAsConfirmed(java.lang.Integer)
	 */
	@Override
	public void markAsConfirmed(Integer idCommand) throws GkException {
		GCodeCommand command = getCommandById(idCommand);
		this.mapSentCommandById.remove(command.getId());
		this.mapConfirmedCommandById.put(command.getId(), command);
		mapStateByIdCommand.put(command.getId(), GCodeCommandState.CONFIRMED);
		notifyListeners(new GCodeCommandExecutionEvent(this, command, GCodeCommandState.CONFIRMED));
		if(isMonitorService()){
			getMonitorService().notifyCommandStateChanged(this, idCommand);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeStreamedExecutionToken#getConfirmedCommandCount()
	 */
	@Override
	public int getConfirmedCommandCount() throws GkException {
		return this.mapConfirmedCommandById.size();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.provider.GCodeExecutionToken#markAsExecuted(java.lang.Integer)
	 */
	@Override
	public void markAsExecuted(Integer idCommand) throws GkException {
		GCodeCommand command = getCommandById(idCommand);
		this.mapConfirmedCommandById.remove(command.getId());
		updateCompleteState();
		super.markAsExecuted(idCommand);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.provider.GCodeExecutionToken#markAsError(java.lang.Integer)
	 */
	@Override
	public void markAsError(Integer idCommand) throws GkException {
		GCodeCommand command = getCommandById(idCommand);
		this.mapSentCommandById.remove(command.getId());
		updateCompleteState();
		super.markAsError(idCommand);
	}
	
	/**
	 * Mark th enext command as executed
	 * @throws GkException GkException
	 */
	public GCodeCommand markNextCommandAsError() throws GkException{
		if(MapUtils.isNotEmpty(mapSentCommandById)){
			GCodeCommand nextCommand = mapSentCommandById.get(mapSentCommandById.keySet().toArray()[0]);
			markAsError(nextCommand.getId());
			return nextCommand;
		}
		return null;
	}
	
	protected void updateCompleteState(){
		if(CollectionUtils.isNotEmpty(mapExecutedCommandById)){
			if(mapErrorsCommandById.size() + mapExecutedCommandById.size() == mapCommandById.size()){
				setComplete(true);
			}
		}
	}
}
//
//private static final GkLog LOG = GkLog.getLogger(GCodeStreamedExecutionToken.class);
///**
// * The list of commansd awaiting acknowledgement
// */
//private List<GCodeCommand> unacknowledgedCommands;
//
///**
// * Constructor.
// * Creates an execution queue from the given provider
// * @param provider the provider to get command from
// */
//public GCodeStreamedExecutionToken(IGCodeProvider provider) {
//	super(provider);
//	unacknowledgedCommands = new ArrayList<GCodeCommand>();
//}
//
///**
// * Constructor
// * @param lstCommands
// */
//public GCodeStreamedExecutionToken(List<GCodeCommand> lstCommands) {
//	super(lstCommands);
//}
//
//public void confirmCommand(final GCodeCommand command) throws GkException{
//	if(CollectionUtils.isNotEmpty(unacknowledgedCommands)){
//		GCodeCommand pendingCommand = unacknowledgedCommands.get(0);
//		if(ObjectUtils.equals(pendingCommand, command)){
//			unacknowledgedCommands.remove(0);
//			setCommandState(pendingCommand, GCodeCommandState.CONFIRMED);
//		}else{
//			LOG.debug("  /!\\  Cannot confirm GCode command "+command);
//		}
//	}
//}
//
//public void stop() {
//	unacknowledgedCommands.clear();
//}
