package org.goko.controller.g2core.handlers;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.g2core.controller.G2CoreControllerService;
import org.goko.controller.g2core.controller.topic.G2CoreExecutionErrorTopic;
import org.goko.controller.tinyg.commons.bean.TinyGExecutionError;
import org.goko.core.common.event.EventBrokerUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.log.GkLog;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class G2CoreStateUpdateHandler implements EventHandler {
	private static final GkLog LOG = GkLog.getLogger(G2CoreStateUpdateHandler.class);
	@Inject
	private IEventBroker broker;
	@Optional
	private Shell currentShell;
	
	@Inject
	public G2CoreStateUpdateHandler(IEventBroker broker) {	
		this.broker = broker;
		broker.subscribe(IControllerService.CONTROLLER_TOPIC_STATE_UPDATE, this);
		broker.subscribe(G2Core.Topic.Error.TOPIC, this);
		
	}

	/** (inheritDoc)
	 * @see org.osgi.service.event.EventHandler#handleEvent(org.osgi.service.event.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		switch(event.getTopic()){
		case IControllerService.CONTROLLER_TOPIC_STATE_UPDATE:broker.post(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
		break;
		case G2Core.Topic.Error.TOPIC: displayErrorMessage(event);
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
			TinyGExecutionError obj = EventBrokerUtils.getData(event, new G2CoreExecutionErrorTopic());
			Status status = new Status(IStatus.ERROR, G2CoreControllerService.SERVICE_ID, obj.getErrorMessage());
			      
			ErrorDialog.openError(currentShell, obj.getTitle(), obj.getMessage(), status);
		} catch (GkException e) {
			LOG.error(e);
		}
	}
}