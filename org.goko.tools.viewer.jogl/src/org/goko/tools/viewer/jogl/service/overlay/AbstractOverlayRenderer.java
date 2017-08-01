/**
 * 
 */
package org.goko.tools.viewer.jogl.service.overlay;

import org.goko.core.common.utils.AbstractIdBean;
import org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer;

/**
 * @author Psyko
 * @date 22 juil. 2017
 */
public abstract class AbstractOverlayRenderer extends AbstractIdBean implements IOverlayRenderer {
	public boolean enabled = true;
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#isOverlayEnabled()
	 */
	@Override
	public boolean isOverlayEnabled() {
		return enabled;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#setOverlayEnabled(boolean)
	 */
	@Override
	public void setOverlayEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
