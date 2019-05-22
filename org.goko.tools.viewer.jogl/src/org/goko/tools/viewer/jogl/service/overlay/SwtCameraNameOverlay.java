package org.goko.tools.viewer.jogl.service.overlay;

import java.awt.Graphics2D;

import javax.vecmath.Color3f;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.graphics.Rectangle;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.AbstractIdBean;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.tools.viewer.jogl.service.JoglSceneManager;
import org.goko.tools.viewer.jogl.utils.overlay.swt.IOverlayRenderer;

/**
 * Overlay to display the current camera 
 * @author Psyko
 */
public class SwtCameraNameOverlay extends AbstractIdBean implements IOverlayRenderer, IPropertyChangeListener {
	/** The jogl scene manager */
	private JoglSceneManager joglSceneManager;
	/** The overlay font */
	private Font overlayFont;
	/** */
	private Color overlayColor;
	private Color transparent;
    
	/** Last frame reset timer */	
	private long lastFrameReset;
	/** The frame counter */
	private int frame;
	/** Computed FPS */
	private int fps;
	
	private RGBA textColor;
	/**
	 * Constructor 
	 */
	public SwtCameraNameOverlay(JoglSceneManager joglSceneManager) {
		this.joglSceneManager = joglSceneManager;
		//this.overlayFont = new Font("SansSerif", Font.TRUETYPE_FONT, 13);
		JoglViewerPreference.getInstance().addPropertyChangeListener(this);
		updateTextColor();
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.swt.IOverlayRenderer#drawOverlayData(org.eclipse.swt.graphics.GC, org.eclipse.swt.graphics.Rectangle)
	 */
	@Override
	public void drawOverlayData(GC gc, Rectangle bounds) throws GkException {
		if (overlayColor == null) {
			overlayColor = new Color(gc.getDevice(), textColor.rgb.red, textColor.rgb.green, textColor.rgb.blue, textColor.alpha);
			transparent = new Color(gc.getDevice(), textColor.rgb.red, textColor.rgb.green, textColor.rgb.blue, 0);
			overlayFont = new Font(gc.getDevice(), "SansSerif", 13, SWT.NONE);
		}
		if(joglSceneManager.getActiveCamera() == null){
			String cameraString = "Camera name";
			gc.setFont(overlayFont);			
			gc.setForeground(overlayColor);
			gc.drawText(cameraString, 100, 100, false);
		}else  if(joglSceneManager.getActiveCamera() != null){
			String cameraString = joglSceneManager.getActiveCamera().getLabel();
			gc.setFont(overlayFont);			
			gc.setForeground(overlayColor);
			gc.drawText(cameraString, 100, 100, false);
		}
	}
	
	public void drawOverlayData(Graphics2D g2d, java.awt.Rectangle s) throws GkException {
		
		if(joglSceneManager.getActiveCamera() != null){
//			g2d.setColor(Color.WHITE);
//			g2d.drawLine(s.width/2, s.height/2 - 10, s.width/2, s.height/2 + 10);
//			g2d.drawLine(s.width/2 - 10, s.height/2 , s.width/2 + 10, s.height/2 );
			/*
			g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			FontRenderContext 	frc = g2d.getFontRenderContext();
			String 				cameraString = joglSceneManager.getActiveCamera().getLabel();
			GlyphVector 		gv = getOverlayFont().createGlyphVector(frc, cameraString);
			java.awt.Rectangle 			glyphBounds = gv.getPixelBounds(frc, 0, 0);
		    int x = 5;
		    int y = 5 + glyphBounds.height;
		    g2d.setFont(getOverlayFont());
		    //Color overlayColor = new Color(0.8f,0.8f,0.8f);
		    java.awt.Color overlayColor = new java.awt.Color(textColor.rgb.red, textColor.rgb.green, textColor.rgb.blue, textColor.alpha);
		    java.awt.Color transparentColor = new java.awt.Color(0,0,0,0);
		    g2d.setBackground(transparentColor);
		    g2d.setColor(overlayColor);
		    if(joglSceneManager.isEnabled()){
		    	g2d.drawString(cameraString,x,y);
		    }else{
		    	g2d.drawString("Disabled",x,y);
		    }
		    if(isShowFps()){
		    	this.frame += 1;
			    if(System.currentTimeMillis() - lastFrameReset >= 500){
			    	this.lastFrameReset = System.currentTimeMillis();
			    	this.fps = this.frame;
			    	this.frame = 0;
			    }
			    g2d.setColor(new java.awt.Color(0.55f,0.45f,0.28f));
			    g2d.drawString(String.valueOf(this.fps*2)+"fps",x,y+glyphBounds.height+4);
		    }		  */  
		}
	}

	/**
	 * Detect if FPS should be shown
	 * @return <code>true</code> if the FPS should be displayed, <code>false</code> otherwise
	 */
	private boolean isShowFps() {
		return JoglViewerPreference.getInstance().isShowFps();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#isOverlayEnabled()
	 */
	@Override
	public boolean isOverlayEnabled() {
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#setOverlayEnabled(boolean)
	 */
	@Override
	public void setOverlayEnabled(boolean enabled) {	}
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

	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		updateTextColor();
	}
	
	private void updateTextColor(){
		// Display overlay text as complementary color of background
		Color3f color = JoglViewerPreference.getInstance().getBackgroundColor();
		RGB rgbColor = new RGB( (int)(color.x * 255), (int)(color.y * 255), (int)(color.z * 255));
		float[] hsb = rgbColor.getHSB();
		float complementaryHue = (hsb[0] + 180 ) % 360;
		textColor = new RGBA(complementaryHue,  1 - hsb[1], 1 - hsb[2], 255f);		
	}
}
