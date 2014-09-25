package org.goko.viewer.jogl.utils.render;

import org.goko.core.common.exception.GkException;
import org.goko.core.viewer.renderer.AbstractViewer3DRenderer;
import org.goko.core.viewer.renderer.IRendererProxy;
import org.goko.viewer.jogl.service.JoglRendererProxy;

public abstract class AbstractJoglRenderer extends AbstractViewer3DRenderer{

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#render(org.goko.core.viewer.renderer.IRendererProxy)
	 */
	@Override
	public void render(IRendererProxy proxy) throws GkException {
		if(isEnabled()){
			renderJogl((JoglRendererProxy)proxy);
		}
	}

	public abstract void renderJogl(JoglRendererProxy joglProxy) throws GkException;

}
