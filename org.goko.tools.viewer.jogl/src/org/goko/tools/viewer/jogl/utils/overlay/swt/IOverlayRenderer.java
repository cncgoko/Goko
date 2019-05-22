package org.goko.tools.viewer.jogl.utils.overlay.swt;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;

public interface IOverlayRenderer extends IIdBean{

	/**
	 * Draws the overlay data 
	 * @param gc the target GC
	 * @param bounds the bounds of the visible area
	 * @throws GkException GkException
	 */
	void drawOverlayData(GC gc, Rectangle bounds) throws GkException;
	
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
