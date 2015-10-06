/**
 * 
 */
package org.goko.tools.viewer.jogl.command;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.goko.tools.viewer.jogl.GCodeViewer3D;

/**
 * Handler to enable jogging using keyboard shortcut
 * @author PsyKo
 *
 */
public class ViewerEnableJogHandler {
	
	@Execute
	public void execute(IEclipseContext context, IEventBroker broker){		
//		GokoJoglCanvas canvas = context.get(GokoJoglCanvas.class);
//		if(canvas != null){
//			canvas.setFocus();
//			canvas.setKeyboardJogEnabled(true);
//		}
		broker.send(GCodeViewer3D.TOPIC_ENABLE_KEYBOARD_JOG, null);
	}
}
