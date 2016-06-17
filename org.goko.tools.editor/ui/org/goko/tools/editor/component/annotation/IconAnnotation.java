/**
 * 
 */
package org.goko.tools.editor.component.annotation;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationPresentation;
import org.eclipse.jface.text.source.ImageUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

/**
 * @author Psyko
 * @date 16 juin 2016
 */
public abstract class IconAnnotation extends Annotation implements IAnnotationPresentation{

	private Image image;
	
	/**
	 * @param image
	 */
	public IconAnnotation(Image image) {
		super();
		this.image = image;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationPresentation#getLayer()
	 */
	@Override
	public int getLayer() {
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationPresentation#paint(org.eclipse.swt.graphics.GC, org.eclipse.swt.widgets.Canvas, org.eclipse.swt.graphics.Rectangle)
	 */
	@Override
	public void paint(GC gc, Canvas canvas, Rectangle bounds) {
		ImageUtilities.drawImage(image, gc, canvas, bounds, SWT.CENTER);
	}

}
