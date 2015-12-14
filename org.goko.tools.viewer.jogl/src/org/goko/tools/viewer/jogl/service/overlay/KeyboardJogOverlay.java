package org.goko.tools.viewer.jogl.service.overlay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.AbstractIdBean;
import org.goko.tools.viewer.jogl.GokoJoglCanvas;
import org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer;

public class KeyboardJogOverlay extends AbstractIdBean implements IOverlayRenderer {
	/** The font used for the warning */
	private Font jogWarnFont;
	/** Reference to the canvas */
	private WeakReference<GokoJoglCanvas> canvasReference;
	
	public KeyboardJogOverlay(GokoJoglCanvas canvas) {
		this.canvasReference = new WeakReference<GokoJoglCanvas>(canvas);
		this.jogWarnFont = new Font("SansSerif", Font.BOLD, 16);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#drawOverlayData(java.awt.Graphics2D)
	 */
	@Override
	public void drawOverlayData(Graphics2D g2d, Rectangle bounds) throws GkException {
		if(getCanvas().isKeyboardJogEnabled()){
			// Draw a big red warning saying jog is enabled
			FontRenderContext 	frc = g2d.getFontRenderContext();
			String warn = "Keyboard jog enabled";
			GlyphVector gv = jogWarnFont.createGlyphVector(frc, warn);
		    Rectangle 	glyphBounds = gv.getPixelBounds(frc, 0, 0);
		    int x = (bounds.width - glyphBounds.width) / 2;
		    int y = 5 + glyphBounds.height;
		    Rectangle2D bg = new Rectangle2D.Double(x-5,2, glyphBounds.width + 15, glyphBounds.height + 10);
		    g2d.setFont(jogWarnFont);
		    g2d.setColor(Color.RED);//new Color(0.9f,0,0,0.5f));
		    g2d.fill(bg);
		    g2d.setColor(Color.WHITE);
		    g2d.drawString(warn ,x, y);
		}
	}

	/**
	 * Getter for the canvas 
	 * @return the canvass
	 */
	private GokoJoglCanvas getCanvas() {		
		return canvasReference.get();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer#isOverlayEnabled()
	 */
	@Override
	public boolean isOverlayEnabled() {
		return true;
	}

}
