/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.overlay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.AbstractIdBean;

/**
 * @author Psyko
 * @date 20 juil. 2017
 */
public class LegendOverlay extends AbstractIdBean implements IOverlayRenderer {
	private double scale = 1.5;
	/** Used font */
	private Font font;
	private String title;
	private List<LegendEntry> lstEntry;
	private String longestLabel;
	private boolean enabled;
	
	/**
	 * 
	 */
	public LegendOverlay(String title) {
		this.font = new Font("SansSerif", Font.BOLD, 20);
		this.lstEntry = new ArrayList<>();
		this.longestLabel = title;
		this.title = title;
		this.enabled = true;
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#drawOverlayData(java.awt.Graphics2D, java.awt.Rectangle)
	 */
	@Override
	public void drawOverlayData(Graphics2D graphics2d, Rectangle bounds) throws GkException {		
		if(isOverlayEnabled()){			
			// Draw a big red warning saying jog is enabled
			FontRenderContext 	frc = graphics2d.getFontRenderContext();
			GlyphVector gv = font.createGlyphVector(frc, longestLabel);		
		    Rectangle 	glyphBounds = gv.getPixelBounds(frc, 0, 0);
		    
		    int totalHeight = (int) (glyphBounds.getHeight() * ((lstEntry.size() + 1.5) * 2 ));
		    int totalwidth  = (int) (glyphBounds.getWidth() + 4 * glyphBounds.getHeight());
		    
		    int x = (int) (glyphBounds.getHeight() * scale);
		    int y = (int) ((bounds.height - totalHeight) / 2.0);
	
		    RoundRectangle2D bg = new RoundRectangle2D.Double(x,y, totalwidth, totalHeight, 10d, 10d);
		    graphics2d.setFont(font);
		    graphics2d.setColor(new Color(0,0,0,0.5f));	    
		    graphics2d.fill(bg);
		    
		    double xOffset = glyphBounds.getHeight();
		    double halfGlyph = glyphBounds.getHeight() / 2;
		    
		    int i = 2;
		    graphics2d.setColor(Color.WHITE);
	    	graphics2d.drawString(title ,(int) (x + xOffset), (int)(y + i * glyphBounds.getHeight()));
	    	i = i + 2;
		    for (LegendEntry legendEntry : lstEntry) {
		    	graphics2d.setColor(Color.WHITE);
		    	graphics2d.drawString(legendEntry.label	,(int)(x + 2.5 * glyphBounds.getHeight()), (int)(y + i * glyphBounds.getHeight()));	    	
		    	graphics2d.setColor(legendEntry.color);
		 	    graphics2d.fill(new Rectangle2D.Double(x + xOffset, y + ((i - 2) + 0.6) * glyphBounds.getHeight()  + halfGlyph , glyphBounds.getHeight(), glyphBounds.getHeight()));
		 	    i = i + 2;
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#isOverlayEnabled()
	 */
	@Override
	public final boolean isOverlayEnabled() {
		return enabled;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#setOverlayEnabled(boolean)
	 */
	@Override
	public void setOverlayEnabled(boolean enabled) {	
		this.enabled = enabled;
	}
	
	public void addEntry(String label, Color color){
		lstEntry.add(new LegendEntry(color, label));		
		if(StringUtils.length(label) > StringUtils.length(longestLabel)){
			longestLabel = label;
		}
	}
	/**
	 * Inner class for legend entry
	 * @author Psyko
	 * @date 21 juil. 2017
	 */
	private class LegendEntry{		
		private Color color;
		private String label;
		
		/**
		 * @param color
		 * @param label
		 */
		public LegendEntry(Color color, String label) {
			super();
			this.color = color;
			this.label = label;
		}
	}	
}
