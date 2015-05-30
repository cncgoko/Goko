package org.goko.grbl.controller.internal.handlers;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.controller.IControllerService;
import org.goko.grbl.controller.Grbl;
import org.goko.grbl.controller.GrblControllerService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class GrblStateUpdateHandler implements EventHandler {
	@Inject
	private IEventBroker broker;
	@Inject
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
		String title = (String) event.getProperty(Grbl.Topic.GrblExecutionError.TITLE);
		String message = (String) event.getProperty(Grbl.Topic.GrblExecutionError.MESSAGE);
		String error = (String) event.getProperty(Grbl.Topic.GrblExecutionError.ERROR);
			
		Status status = new Status(IStatus.ERROR, GrblControllerService.SERVICE_ID, error);
		      
		ErrorDialog.openError(currentShell, title, message, status);
	}
}
