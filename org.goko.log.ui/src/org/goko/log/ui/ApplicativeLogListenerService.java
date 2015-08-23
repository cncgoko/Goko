/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.log.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.applicative.logging.ApplicativeLogEvent;
import org.goko.core.common.applicative.logging.IApplicativeLogListener;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.exception.GkException;

/**
 * Simple applicative log service implementation
 *
 * @author PsyKo
 *
 */
public class ApplicativeLogListenerService implements IApplicativeLogService{
	private List<IApplicativeLogListener> listeners;
	private List<ApplicativeLogEvent> events;

	/**
	 * Constructor
	 */
	public ApplicativeLogListenerService() {
		listeners = Collections.synchronizedList(new ArrayList<IApplicativeLogListener>());
		events = Collections.synchronizedList(new ArrayList<ApplicativeLogEvent>());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogService#log(int, java.lang.String)
	 */
	@Override
	public void log(int severity, String message) {
		log(severity, message, null);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogService#log(int, java.lang.String, java.lang.String)
	 */
	@Override
	public void log(int severity, String message, String source) {
		ApplicativeLogEvent event = new ApplicativeLogEvent(severity, message, source);
		events.add(event);
		
		if(CollectionUtils.isNotEmpty(listeners)){	
			synchronized (listeners) {
				for (IApplicativeLogListener logListener : listeners) {
					logListener.onLogEvent(event);
				}
			}
		}

	}
	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogService#registerApplicativeLogListener(org.goko.core.common.applicative.logging.IApplicativeLogListener)
	 */
	@Override
	public void registerApplicativeLogListener(IApplicativeLogListener listener) {
		listeners.add(listener);
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogService#unregisterApplicativeLogListener(org.goko.core.common.applicative.logging.IApplicativeLogListener)
	 */
	@Override
	public void unregisterApplicativeLogListener( IApplicativeLogListener listener) {
		listeners.remove(listener);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogService#error(java.lang.String, java.lang.String)
	 */
	@Override
	public void error(String message, String source) {
		log(ApplicativeLogEvent.LOG_ERROR, message, source);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogService#warning(java.lang.String, java.lang.String)
	 */
	@Override
	public void warning(String message, String source) {
		log(ApplicativeLogEvent.LOG_WARNING, message, source);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogService#info(java.lang.String, java.lang.String)
	 */
	@Override
	public void info(String message, String source) {
		log(ApplicativeLogEvent.LOG_INFO, message, source);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogService#getEvents()
	 */
	@Override
	public List<ApplicativeLogEvent> getEvents() throws GkException {		
		return events;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogService#clearEvents()
	 */
	@Override
	public void clearEvents() throws GkException {
		events.clear();
	}

}
