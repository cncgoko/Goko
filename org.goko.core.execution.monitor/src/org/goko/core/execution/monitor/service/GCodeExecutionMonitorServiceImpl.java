/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.core.execution.monitor.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.service.IExecutionMonitorService;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.log.GkLog;

/**
 * Default implementation of the GCode execution monitor service
 *
 * @author PsyKo
 *
 */
public class GCodeExecutionMonitorServiceImpl implements IExecutionMonitorService<ExecutionState, ExecutionToken<ExecutionState>>, IGokoService {
	/** Service ID */
	public static final String SERVICE_ID = "org.goko.core.execution.monitor.service.GCodeExecutionMonitorServiceImpl";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GCodeExecutionMonitorServiceImpl.class);
	/** The list of listener*/
	private List<IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>>> listenerList;

	/**
	 * Constructor
	 */
	public GCodeExecutionMonitorServiceImpl() {
		listenerList = new ArrayList<IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>>>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		LOG.info("Starting "+getServiceId());

	}
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		LOG.info("Stopping  "+getServiceId());
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionMonitorService#addExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void addExecutionListener(IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> listener) throws GkException {
		if(!listenerList.contains(listener)){
			listenerList.add(listener);
		}

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionMonitorService#removeExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void removeExecutionListener(IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> listener) throws GkException {
		if(listenerList.contains(listener)){
			listenerList.remove(listener);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionMonitorService#notifyExecutionStart(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionStart(ExecutionToken<ExecutionState> token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onExecutionStart(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionMonitorService#notifyCommandStateChanged(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken, java.lang.Integer)
	 */
	@Override
	public void notifyCommandStateChanged(ExecutionToken<ExecutionState> token, Integer idLine) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onLineStateChanged(token, idLine);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionMonitorService#notifyExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionCanceled(ExecutionToken<ExecutionState> token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onExecutionCanceled(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionMonitorService#notifyExecutionPause(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionPause(ExecutionToken<ExecutionState> token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onExecutionPause(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionMonitorService#notifyExecutionComplete(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionComplete(ExecutionToken<ExecutionState> token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onExecutionComplete(token);
			}
		}
	}

}
