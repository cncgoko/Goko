package org.goko.tools.viewer.jogl.utils.overlay;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;

public interface IOverlayRenderer extends IIdBean{

	/**
	 * Draws the overlay data 
	 * @param graphics2d the target graphics2d
	 * @param bounds the bounds of the visible area
	 * @throws GkException GkException
	 */
	void drawOverlayData(Graphics2D graphics2d, Rectangle bounds) throws GkException;
	
	/**
	 * Detect if this overlay renderer is to be rendered (<code>true</code>) or not (<code>false</code>)
	 * @return <code>true</code> if this overlay should be drawn, <code>false</code> otherwise
	 */
	boolean isOverlayEnabled();
	
	/**
	 * Changes the enabled state of the overlay
	 * @param enabled <code>true</code> if this overlay should be drawn, <code>false</code> otherwise
	 */
	void setOverlayEnabled(boolean enabled);
}
