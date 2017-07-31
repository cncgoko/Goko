/**
 * 
 */
package org.goko.tools.viewer.jogl.service.overlay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.math.BigDecimal;

import javax.vecmath.Point4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.config.GokoPreference;
import org.goko.tools.viewer.jogl.service.JoglUtils;

/**
 * @author Psyko
 * @date 22 juil. 2017
 */
public abstract class AbstractPositionOverlay extends AbstractOverlayRenderer{
	private Font font;
	
	/**
	 * @param manager
	 */
	public AbstractPositionOverlay() {
		super();
		this.font = new Font("SansSerif", Font.BOLD, (int) (32));
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#drawOverlayData(java.awt.Graphics2D, java.awt.Rectangle)
	 */
	@Override
	public final void drawOverlayData(Graphics2D g2d, Rectangle bounds) throws GkException {
		if(isOverlayEnabled()){
			drawPositionData(g2d, bounds);
		}
	}
	
	protected abstract void drawPositionData(Graphics2D g2d, Rectangle bounds) throws GkException;
	
	protected void drawPosition(Graphics2D g2d, Rectangle bounds, Point4f p) throws GkException{
		String label = "X";				
		FontRenderContext 	frc = g2d.getFontRenderContext();
		GlyphVector gv = font.createGlyphVector(frc, label);		
	    Rectangle 	glyphBounds = gv.getPixelBounds(frc, 0, 0);
	    
	    g2d.setFont(font);
	    g2d.setColor(Color.WHITE);
		g2d.drawString("Z:"+ ((p.z >= 0 ) ? " ":"") +GokoPreference.getInstance().format(Length.valueOf(BigDecimal.valueOf(p.z), JoglUtils.JOGL_UNIT)), (int)(0.5*glyphBounds.height), bounds.height - (int)(0.5*glyphBounds.height));
		g2d.drawString("Y:"+ ((p.y >= 0 ) ? " ":"") +GokoPreference.getInstance().format(Length.valueOf(BigDecimal.valueOf(p.y), JoglUtils.JOGL_UNIT)), (int)(0.5*glyphBounds.height), bounds.height - (int)(2.0*glyphBounds.height)); 
		g2d.drawString("X:"+ ((p.x >= 0 ) ? " ":"") +GokoPreference.getInstance().format(Length.valueOf(BigDecimal.valueOf(p.x), JoglUtils.JOGL_UNIT)), (int)(0.5*glyphBounds.height), bounds.height - (int)(3.5*glyphBounds.height));
	}

}
