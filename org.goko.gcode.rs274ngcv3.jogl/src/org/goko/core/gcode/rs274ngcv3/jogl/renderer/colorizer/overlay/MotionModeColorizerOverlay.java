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
public class MotionModeColorizerOverlay extends LegendOverlay {

	/**
	 * 
	 */
	public MotionModeColorizerOverlay() {
		super("Motion");
		addEntry("Feed"	, new Color(0.14f,0.33f,0.80f,0.9f));
		addEntry("Rapid", new Color(1f,0.77f,0.04f, 0.75f));
		addEntry("Arc"	, new Color(0,0.86f,0,0.9f));
		addEntry("Probe", new Color(0.80f,0.40f,1f,0.9f));
	}

}
