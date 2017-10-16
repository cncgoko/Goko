/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.handler.workspace;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.EventUtils;
import org.goko.core.workspace.service.WorkspaceUIEvent;
import org.osgi.service.event.EventAdmin;

/**
 * @author Psyko
 * @date 17 août 2017
 */
public class DeleteHandler {

	@Execute
	public void execute(EventAdmin eventAdmin){		
		EventUtils.post(eventAdmin, WorkspaceUIEvent.TOPIC_WORKSPACE_DELETE, null);
	}
}
