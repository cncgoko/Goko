package org.goko.tools.editor.component.annotation;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IAnnotationAccessExtension;
import org.eclipse.jface.text.source.IAnnotationPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.wb.swt.SWTResourceManager;

public class BasicAnnotationAccess implements IAnnotationAccess, IAnnotationAccessExtension{

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationAccessExtension#getTypeLabel(org.eclipse.jface.text.source.Annotation)
	 */
	@Override
	public String getTypeLabel(Annotation annotation) {
		return annotation.getText();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationAccessExtension#getLayer(org.eclipse.jface.text.source.Annotation)
	 */
	@Override
	public int getLayer(Annotation annotation) {
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationAccessExtension#paint(org.eclipse.jface.text.source.Annotation, org.eclipse.swt.graphics.GC, org.eclipse.swt.widgets.Canvas, org.eclipse.swt.graphics.Rectangle)
	 */
	@Override
	public void paint(Annotation annotation, GC gc, Canvas canvas, Rectangle bounds) {
		if (annotation instanceof IAnnotationPresentation) {
			IAnnotationPresentation presentation= (IAnnotationPresentation) annotation;
			presentation.paint(gc, canvas, bounds);
			return;

		}
		if(gc != null){
			gc.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
			gc.fillRectangle(bounds.x,bounds.y ,bounds.width,bounds.height);
		}
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationAccessExtension#isPaintable(org.eclipse.jface.text.source.Annotation)
	 */
	@Override
	public boolean isPaintable(Annotation annotation) {
		return true;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationAccessExtension#isSubtype(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean isSubtype(Object annotationType, Object potentialSupertype) {
		return ObjectUtils.equals(annotationType, potentialSupertype);
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationAccessExtension#getSupertypes(java.lang.Object)
	 */
	@Override
	public Object[] getSupertypes(Object annotationType) {
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationAccess#getType(org.eclipse.jface.text.source.Annotation)
	 */
	@Override
	public Object getType(Annotation annotation) {
		return annotation.getType();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationAccess#isMultiLine(org.eclipse.jface.text.source.Annotation)
	 */
	@Override
	public boolean isMultiLine(Annotation annotation) {
		return false;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.IAnnotationAccess#isTemporary(org.eclipse.jface.text.source.Annotation)
	 */
	@Override
	public boolean isTemporary(Annotation annotation) {
		return false;
	}
	
}