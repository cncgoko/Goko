/**
 * 
 */
package org.goko.tools.viewer.jogl.service.overlay;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.tools.viewer.jogl.service.JoglSceneManager;
import org.goko.tools.viewer.jogl.utils.overlay.swt.IOverlayRenderer;

/**
 * @author Psyko
 * @date 21 déc. 2018
 */
public class SWTOverlay implements PaintListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JoglSceneManager.class);
	/** The list of overlay renderers */
	private List<IOverlayRenderer> overlayRenderers;
	private Image bufferImage; 
	/**
	 * Constructor
	 */
	public SWTOverlay() {
		this.overlayRenderers = new ArrayList<>();
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	@Override
	public void paintControl(PaintEvent e) {		
		System.out.println("SWTOverlay.paintControl()");
		//bufferImage = new Image(e.gc.getDevice(), e.width, e.height);
		//final GC buffergc = new GC(bufferImage, SWT.TRANSPARENT);
		final Rectangle size = new Rectangle(0, 0, e.width, e.height);
		for (IOverlayRenderer overlayRenderer : overlayRenderers) {
			try {
				overlayRenderer.drawOverlayData(e.gc, size);
			} catch (GkException ex) {				
				LOG.error(ex);
			}
		}
		//e.gc.drawImage(bufferImage, 0, 0);
		//buffergc.dispose();
		//bufferImage.dispose();
	}
	
	/**
	 * Registers the given overlay renderer
	 * @param overlayRenderer the renderer to register
	 * @throws GkException GkException
	 */
	public void addOverlayRenderer(IOverlayRenderer overlayRenderer) throws GkException{
		overlayRenderers.add(overlayRenderer);
	}
	
	/**
	 * Removes the given overlay renderer
	 * @param overlayRenderer the renderer to register
	 * @throws GkException GkException
	 */
	public void removeOverlayRenderer(IOverlayRenderer overlayRenderer) throws GkException{
		overlayRenderers.remove(overlayRenderer);
	}
	
}
