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
import org.goko.core.gcode.bean.execution.IGCodeExecutionToken;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.gcode.service.IGCodeExecutionMonitorService;
import org.goko.core.log.GkLog;

/**
 * Default implementation of the GCode execution monitor service
 *
 * @author PsyKo
 *
 */
public class GCodeExecutionMonitorServiceImpl implements IGCodeExecutionMonitorService, IGokoService {
	/** Service ID */
	public static final String SERVICE_ID = "org.goko.core.execution.monitor.service.GCodeExecutionMonitorServiceImpl";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GCodeExecutionMonitorServiceImpl.class);
	/** The list of listener*/
	private List<IGCodeExecutionListener> listenerList;

	/**
	 * Constructor
	 */
	public GCodeExecutionMonitorServiceImpl() {
		listenerList = new ArrayList<IGCodeExecutionListener>();
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
	 * @see org.goko.core.gcode.service.IGCodeExecutionMonitorService#addExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void addExecutionListener(IGCodeExecutionListener listener) throws GkException {
		if(!listenerList.contains(listener)){
			listenerList.add(listener);
		}

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeExecutionMonitorService#removeExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void removeExecutionListener(IGCodeExecutionListener listener) throws GkException {
		if(listenerList.contains(listener)){
			listenerList.remove(listener);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeExecutionMonitorService#notifyExecutionStart(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionStart(IGCodeExecutionToken token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener executionListener : listenerList) {
				executionListener.onExecutionStart(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeExecutionMonitorService#notifyCommandStateChanged(org.goko.core.gcode.bean.execution.IGCodeExecutionToken, java.lang.Integer)
	 */
	@Override
	public void notifyCommandStateChanged(IGCodeExecutionToken token, Integer idCommand) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener executionListener : listenerList) {
				executionListener.onCommandStateChanged(token, idCommand);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeExecutionMonitorService#notifyExecutionCanceled(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionCanceled(IGCodeExecutionToken token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener executionListener : listenerList) {
				executionListener.onExecutionCanceled(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeExecutionMonitorService#notifyExecutionPause(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionPause(IGCodeExecutionToken token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener executionListener : listenerList) {
				executionListener.onExecutionPause(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeExecutionMonitorService#notifyExecutionComplete(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionComplete(IGCodeExecutionToken token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener executionListener : listenerList) {
				executionListener.onExecutionComplete(token);
			}
		}
	}

}
