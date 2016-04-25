package org.goko.controller.grbl.v09.internal.handlers;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.controller.grbl.v09.Grbl;
import org.goko.controller.grbl.v09.GrblControllerService;
import org.goko.controller.grbl.v09.bean.GrblExecutionError;
import org.goko.controller.grbl.v09.configuration.topic.GrblExecutionErrorTopic;
import org.goko.core.common.event.EventBrokerUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.log.GkLog;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class GrblStateUpdateHandler implements EventHandler {
	private static final GkLog LOG = GkLog.getLogger(GrblStateUpdateHandler.class);
	@Inject
	private IEventBroker broker;
	@Optional
	private Shell currentShell;
	
	@Inject
	public GrblStateUpdateHandler(IEventBroker broker) {	
		this.broker = broker;
		broker.subscribe(IControllerService.CONTROLLER_TOPIC_STATE_UPDATE, this);
		broker.subscribe(Grbl.Topic.GrblExecutionError.TOPIC, this);
		
	}

	/** (inheritDoc)
	 * @see org.osgi.service.event.EventHandler#handleEvent(org.osgi.service.event.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		switch(event.getTopic()){
		case IControllerService.CONTROLLER_TOPIC_STATE_UPDATE:broker.post(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
		break;
		case Grbl.Topic.GrblExecutionError.TOPIC: displayErrorMessage(event);
		break;
		default:broker.post(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
		}
	}
	
	/**
	 * Display the error message
	 * @param event the error event received
	 */
	protected void displayErrorMessage(Event event){	
		
		try {
			GrblExecutionError obj = EventBrokerUtils.getData(event, new GrblExecutionErrorTopic());
			Status status = new Status(IStatus.ERROR, GrblControllerService.SERVICE_ID, obj.getErrorMessage());
			      
			ErrorDialog.openError(currentShell, obj.getTitle(), obj.getMessage(), status);
		} catch (GkException e) {
			LOG.error(e);
		}
	}
}
