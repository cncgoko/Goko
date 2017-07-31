/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.overlay;

import java.awt.Color;

import org.goko.tools.viewer.jogl.utils.overlay.LegendOverlay;

/**
 * @author Psyko
 * @date 21 juil. 2017
 */
public class SpindleStateColorizerOverlay extends LegendOverlay {
	/**
	 * 
	 */
	public SpindleStateColorizerOverlay() {
		super("Spindle");
		addEntry("Cw"	, new Color(1f,0.48f,0.0f,0.9f));
		addEntry("Ccw"	, new Color(0.44f,0.03f,0.66f,0.9f));
		addEntry("Off", new Color(0.8f,0.8f,0.8f,0.9f));
	}

}
