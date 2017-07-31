/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.overlay;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.RoundRectangle2D;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.config.GokoPreference;
import org.goko.tools.viewer.jogl.service.overlay.AbstractOverlayRenderer;

/**
 * @author Psyko
 * @date 24 juil. 2017
 */
public class FeedrateColorizerOverlay extends AbstractOverlayRenderer {
	private static final Color MIN_COLOR = new Color(32,34,151);
	private static final Color MED_COLOR_3 = new Color(255,255,0);
	private static final Color MED_COLOR_2 = new Color(10,250,40);
	private static final Color MED_COLOR_1 = new Color(0,250,255);
	private static final Color MAX_COLOR = new Color(254,37,0);
	private Speed minFeedrate = Speed.ZERO;	
	private Speed maxFeedrate = Speed.valueOf(1000, SpeedUnit.MILLIMETRE_PER_MINUTE);
	private Font titleFont;	
	private Font smallFont;	
	private String title;
	/**
	 * 
	 */
	public FeedrateColorizerOverlay(String title) {
		this.titleFont = new Font("SansSerif", Font.BOLD, 20);
		this.smallFont = new Font("SansSerif", Font.BOLD, 16);
		this.title = title;
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#drawOverlayData(java.awt.Graphics2D, java.awt.Rectangle)
	 */
	@Override
	public void drawOverlayData(Graphics2D graphics2d, Rectangle bounds) throws GkException {
		if(isOverlayEnabled()){			
			// Draw a big red warning saying jog is enabled
			FontRenderContext 	frc = graphics2d.getFontRenderContext();
			String minLabel = GokoPreference.getInstance().format(minFeedrate, false, false);
			String maxLabel = GokoPreference.getInstance().format(maxFeedrate, false, false);
			GlyphVector titleFontGv = titleFont.createGlyphVector(frc, title);		
		    Rectangle 	titleBounds = titleFontGv.getPixelBounds(frc, 0, 0);
		    
		    GlyphVector smallFontGv = smallFont.createGlyphVector(frc, maxLabel);		
		    Rectangle 	smallBounds = smallFontGv.getPixelBounds(frc, 0, 0);
		    
		    int totalHeight = (int) (100 + 6 * titleBounds.getHeight());
		    int totalwidth  = (int) (titleBounds.getWidth() + 2 * titleBounds.getHeight());
		    
		    int x = (int) (titleBounds.getHeight() * 2);
		    int y = (int) ((bounds.height - totalHeight) / 2.0);
	
		    int yPointer = y;
		    RoundRectangle2D bg = new RoundRectangle2D.Double(x,yPointer, totalwidth, totalHeight, 10d, 10d);
		    graphics2d.setFont(titleFont);
		    graphics2d.setColor(new Color(0,0,0,0.5f));	    
		    graphics2d.fill(bg);
		    
		    double xOffset = titleBounds.getHeight();
		    double halfGlyph = titleBounds.getHeight() / 2;
		    
		    yPointer += 2 * titleBounds.getHeight();
		    graphics2d.setColor(Color.WHITE);
	    	graphics2d.drawString(title ,(int) (x + xOffset), yPointer);
	    	
	    	yPointer += 1.5 * smallBounds.getHeight();
	    	graphics2d.setFont(smallFont);	    	
	    	graphics2d.drawString(GokoPreference.getInstance().getSpeedUnit().getSymbol() ,(int) (x + xOffset), yPointer);
	    	
	    	yPointer += 2 * smallBounds.getHeight();		    
	    	GradientPaint gradient1 = new GradientPaint(0,	yPointer	,MAX_COLOR,0, yPointer + 25 ,MED_COLOR_3);
	    	GradientPaint gradient2 = new GradientPaint(0,yPointer + 25	,MED_COLOR_3,0, yPointer + 50 ,MED_COLOR_2);
	    	GradientPaint gradient3 = new GradientPaint(0,yPointer + 50 ,MED_COLOR_2,0, yPointer + 75 ,MED_COLOR_1);
	    	GradientPaint gradient4 = new GradientPaint(0,yPointer + 75 ,MED_COLOR_1,0, yPointer + 100 ,MIN_COLOR);
	    	
	    	graphics2d.setPaint(gradient1);
	    	graphics2d.fillRect((int)(titleBounds.getHeight() * 3), yPointer, (int)(x * 0.8), 25);
	    	graphics2d.setPaint(gradient2);
	    	graphics2d.fillRect((int)(titleBounds.getHeight() * 3), yPointer+25, (int)(x * 0.8), 25);
	    	graphics2d.setPaint(gradient3);
	    	graphics2d.fillRect((int)(titleBounds.getHeight() * 3), yPointer+50, (int)(x * 0.8), 25);
	    	graphics2d.setPaint(gradient4);
	    	graphics2d.fillRect((int)(titleBounds.getHeight() * 3), yPointer+75, (int)(x * 0.8), 25);
	    	
	    	graphics2d.setColor(Color.WHITE);
	    	graphics2d.drawRect((int)(titleBounds.getHeight() * 3), yPointer, (int)(x * 0.8), 100);
	    	graphics2d.drawLine((int)(titleBounds.getHeight() * 3 + x * 0.8),
	    			yPointer,
					(int)(titleBounds.getHeight() * 3.5 + x * 0.8),
					yPointer);
	    	graphics2d.drawLine((int)(titleBounds.getHeight() * 3 + x * 0.8),
	    			yPointer + 100,
					(int)(titleBounds.getHeight() * 3.5 + x * 0.8),
					yPointer + 100);
	    	graphics2d.drawString(maxLabel ,(int)(x * 0.8 + titleBounds.getHeight() * 4), yPointer + (int)(smallBounds.getHeight() * 0.5));
	    	graphics2d.drawString(minLabel ,(int)(x * 0.8 + titleBounds.getHeight() * 4), yPointer + (int)(smallBounds.getHeight() * 0.5) + 100);
//	    	for (LegendEntry legendEntry : lstEntry) {
//		    	graphics2d.setColor(Color.WHITE);
//		    	graphics2d.drawString(legendEntry.label	,(int)(x + 2.5 * glyphBounds.getHeight()), (int)(y + i * glyphBounds.getHeight()));	    	
//		    	graphics2d.setColor(legendEntry.color);
//		 	    graphics2d.fill(new Rectangle2D.Double(x + xOffset, y + ((i - 2) + 0.6) * glyphBounds.getHeight()  + halfGlyph , glyphBounds.getHeight(), glyphBounds.getHeight()));
//		 	    i = i + 2;
//			}
		}
	}
	/**
	 * @return the minFeedrate
	 */
	public Speed getMinFeedrate() {
		return minFeedrate;
	}

	/**
	 * @param minFeedrate the minFeedrate to set
	 */
	public void setMinFeedrate(Speed minFeedrate) {
		this.minFeedrate = minFeedrate;
	}

	/**
	 * @return the maxFeedrate
	 */
	public Speed getMaxFeedrate() {
		return maxFeedrate;
	}

	/**
	 * @param maxFeedrate the maxFeedrate to set
	 */
	public void setMaxFeedrate(Speed maxFeedrate) {
		this.maxFeedrate = maxFeedrate;
	}

}
