/**
 * 
 */
package org.goko.gcode.rs274ngcv3.jogl.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.goko.core.gcode.rs274ngcv3.jogl.RS274NGCV3JoglService;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.AbstractInstructionColorizer;
import org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer;

/**
 * @author Psyko
 * @date 19 juil. 2017
 */
public class ColorizerOverlayVisibilityHandler {

	@CanExecute
	public boolean canExecute(){
		return true;
	}


	@Execute
	public void execute(RS274NGCV3JoglService service){
		AbstractInstructionColorizer colorizer = service.getColorizer();
		if(colorizer != null){
			IOverlayRenderer overlay = colorizer.getOverlay();
			if(overlay != null){
				overlay.setOverlayEnabled( ! overlay.isOverlayEnabled() );
			}
		}
	}
}
