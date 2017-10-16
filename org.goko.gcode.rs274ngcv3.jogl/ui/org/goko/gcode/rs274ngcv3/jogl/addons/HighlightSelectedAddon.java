/**
 * 
 */
package org.goko.gcode.rs274ngcv3.jogl.addons;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.rs274ngcv3.jogl.RS274NGCV3JoglService;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.RS274GCodeRenderer;
import org.goko.core.workspace.service.WorkspaceUIEvent;

/**
 * @author Psyko
 * @date 19 août 2017
 */
@Singleton
public class HighlightSelectedAddon {
	private float currentSelectionWidth;
	private RS274GCodeRenderer currentSelection;
	
	@Inject
	@Optional	
	private void subscribeWorkspaceSelection(RS274NGCV3JoglService joglService, @UIEventTopic(WorkspaceUIEvent.TOPIC_WORKSPACE_UI_SELECT) Object data) throws GkException {
		if(currentSelection != null){
			currentSelection.setLineWidth(currentSelectionWidth);
		}
		RS274GCodeRenderer renderer = null;
		
		if(data instanceof IGCodeProvider){
			IGCodeProvider provider = (IGCodeProvider) data;			
			renderer = joglService.findRendererByGCodeProvider(provider);
			
		}else if(data instanceof IExecutionToken<?>){
			IExecutionToken<?> token = (IExecutionToken<?>) data;			
			renderer = joglService.findRendererByGCodeProvider(token.getGCodeProvider());
			
		}
		
		if(renderer != null){
			currentSelection = renderer;
			currentSelectionWidth = renderer.getLineWidth();
			renderer.setLineWidth(5);
		}
	}
	
	
}
