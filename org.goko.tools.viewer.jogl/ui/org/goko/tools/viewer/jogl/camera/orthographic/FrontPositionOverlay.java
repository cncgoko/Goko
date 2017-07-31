/**
 * 
 */
package org.goko.tools.viewer.jogl.camera.orthographic;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.vecmath.Point2i;
import javax.vecmath.Point4f;

import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.service.JoglSceneManager;
import org.goko.tools.viewer.jogl.service.overlay.AbstractPositionOverlay;

/**
 * @author Psyko
 * @date 22 juil. 2017
 */
public class FrontPositionOverlay extends AbstractPositionOverlay{
	private JoglSceneManager manager;
	private OrthographicCamera targetCamera;
	
	/**
	 * @param manager
	 */
	public FrontPositionOverlay(OrthographicCamera camera, JoglSceneManager manager) {
		super();
		this.targetCamera = camera;
		this.manager = manager;
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.overlay.AbstractPositionOverlay#drawPositionData(java.awt.Graphics2D, java.awt.Rectangle)
	 */
	@Override
	public void drawPositionData(Graphics2D g2d, Rectangle bounds) throws GkException {
		Point2i point = manager.getMouseCanvasPosition();
		if(point != null){
			float x = (2.0f * point.x) / bounds.width - 1.0f;
			float y = 1.0f - (2.0f * point.y) / bounds.height;
			
			x = (float) (x * targetCamera.getSpaceWidth() );
			y = (float) (y * targetCamera.getSpaceHeight() );
			
			Point4f camera = new Point4f(targetCamera.eye.x , 0, targetCamera.eye.z, 1f);
			camera.scale(targetCamera.zoomFactor);
			camera.x = camera.x + x;
			camera.z = camera.z + y;
			drawPosition(g2d, bounds, camera);
		}
	}

}
