/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.overlay;

import java.awt.Color;

import org.goko.tools.viewer.jogl.utils.overlay.LegendOverlay;

/**
 * @author Psyko
 * @date 20 juil. 2017
 */
public class SelectedPlaneColorizerOverlay extends LegendOverlay {

	/**
	 * 
	 */
	public SelectedPlaneColorizerOverlay() {
		super("Plane");
		
		addEntry("XY"	, new Color(0.8f,0.0f,0.0f,0.9f));
		addEntry("XZ", new Color(0.0f,0.8f,0.0f,0.9f));
		addEntry("YZ"	, new Color(0.0f,0.0f,0.8f,0.9f));
	}

}
