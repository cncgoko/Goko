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
package org.goko.log.part.model;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.applicative.logging.ApplicativeLogEvent;
import org.goko.core.common.applicative.logging.IApplicativeLogListener;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * Controller for problems view
 *
 * @author PsyKo
 *
 */
public class ProblemsLogController extends AbstractController<ProblemsLogModel> implements IApplicativeLogListener{
	private static final GkLog LOG = GkLog.getLogger(ProblemsLogController.class);
	@Inject
	private IApplicativeLogService logListenerService;
	/**
	 * Constructor
	 */
	public ProblemsLogController() {
		super(new ProblemsLogModel());
		getDataModel().setTableContent( new ProblemTreeContent() );
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		List<ApplicativeLogEvent> lstEvents = logListenerService.getEvents();
		if(CollectionUtils.isNotEmpty(lstEvents)){
			for (ApplicativeLogEvent applicativeLogEvent : lstEvents) {
				onLogEvent(applicativeLogEvent);
			}
		}
		logListenerService.registerApplicativeLogListener(this);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.applicative.logging.IApplicativeLogListener#onLogEvent(org.goko.core.common.applicative.logging.ApplicativeLogEvent)
	 */
	@Override
	public void onLogEvent(final ApplicativeLogEvent event) {
		if(event.getSeverity() == ApplicativeLogEvent.LOG_ERROR){
			getDataModel().getTableContent().addError(event);
		}else if(event.getSeverity() ==  ApplicativeLogEvent.LOG_WARNING){
			getDataModel().getTableContent().addWarning(event);
		}
	}

	public void clearLog(){
		getDataModel().getTableContent().clearAll();
		try {
			logListenerService.clearEvents();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

}
