package org.goko.tools.viewer.jogl.service.overlay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.AbstractIdBean;
import org.goko.tools.viewer.jogl.service.JoglSceneManager;
import org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer;

/**
 * Overlay to display the current camera 
 * @author Psyko
 */
public class CameraNameOverlay extends AbstractIdBean implements IOverlayRenderer {
	/** The jogl scene manager */
	private JoglSceneManager joglSceneManager;
	/** The overlay font */
	private Font overlayFont;
	/** Last frame reset timer */	
	private long lastFrameReset;
	/** The frame counter */
	private int frame;
	/** Computed FPS */
	private int fps;
	/**
	 * Constructor 
	 */
	public CameraNameOverlay(JoglSceneManager joglSceneManager) {
		this.joglSceneManager =joglSceneManager;
		this.overlayFont = new Font("SansSerif", Font.PLAIN, 12);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#drawOverlayData(java.awt.Graphics2D, java.awt.Rectangle)
	 */
	@Override
	public void drawOverlayData(Graphics2D g2d, Rectangle s) throws GkException {
		this.frame += 1;
		if(joglSceneManager.getActiveCamera() != null){
			FontRenderContext 	frc = g2d.getFontRenderContext();
			String 				cameraString = joglSceneManager.getActiveCamera().getLabel();
			GlyphVector 		gv = getOverlayFont().createGlyphVector(frc, cameraString);
		    Rectangle 			glyphBounds = gv.getPixelBounds(frc, 0, 0);
		    int x = 5;
		    int y = 5 + glyphBounds.height;
		    g2d.setFont(getOverlayFont());
		    Color overlayColor = new Color(0.8f,0.8f,0.8f);
		    Color transparentColor = new Color(0,0,0,0);
		    g2d.setBackground(transparentColor);
		    g2d.setColor(overlayColor);
		    g2d.clearRect(0, 0, s.width, s.height);
		    if(joglSceneManager.isEnabled()){
		    	g2d.drawString(cameraString,x,y);
		    }else{
		    	g2d.drawString("Disabled",x,y);
		    }
		    if(System.currentTimeMillis() - lastFrameReset >= 500){
		    	this.lastFrameReset = System.currentTimeMillis();
		    	this.fps = this.frame;
		    	this.frame = 0;
		    }
		    g2d.setColor(new Color(0.55f,0.45f,0.28f));
		    g2d.drawString(String.valueOf(this.fps*2)+"fps",x,y+glyphBounds.height+4);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#isOverlayEnabled()
	 */
	@Override
	public boolean isOverlayEnabled() {
		return true;
	}

	/**
	 * @return the overlayFont
	 */
	public Font getOverlayFont() {
		return overlayFont;
	}

	/**
	 * @param overlayFont the overlayFont to set
	 */
	public void setOverlayFont(Font overlayFont) {
		this.overlayFont = overlayFont;
	}

}
