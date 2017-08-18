/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.handler.workspace;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.goko.core.workspace.service.WorkspaceUIEvent;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * @author Psyko
 * @date 17 août 2017
 */
public class DeleteHandler {

	@Execute
	public void execute(EventAdmin eventAdmin){		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(IEventBroker.DATA, null);		
		eventAdmin.postEvent(new Event(WorkspaceUIEvent.TOPIC_WORKSPACE_DELETE, data));
	}
}
