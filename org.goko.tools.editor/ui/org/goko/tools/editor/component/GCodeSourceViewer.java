/**
 * 
 */
package org.goko.tools.editor.component;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.tools.editor.BulletAnnotation;
import org.goko.tools.editor.component.annotation.ErrorAnnotation;
import org.goko.tools.editor.component.provider.IDocumentProvider;
import org.goko.tools.editor.component.ruler.LineNumberRulerColumn;
import org.goko.tools.editor.preferences.EditorPreference;

/**
 * @author Psyko
 * @date 23 mai 2016
 */
public class GCodeSourceViewer extends SourceViewer implements IPropertyChangeListener{
	private LineNumberRulerColumn lineRuler;
	private AnnotationRulerColumn annotationRuler;
	
	/**
	 * Constructor 
	 * @param parent
	 * @param styles
	 */
	public GCodeSourceViewer(Composite parent, IOverviewRuler overviewRuler, IAnnotationAccess annotationAccess, int styles) {		
		super(parent, new CompositeRuler(), overviewRuler, true, styles);
		
		GCodeSourceConfiguration gcodeSourceConfiguration = new GCodeSourceConfiguration();	
		configure(gcodeSourceConfiguration);
		
		lineRuler = new LineNumberRulerColumn();
		lineRuler.setBackground(SWTResourceManager.getColor(0xFD, 0xFD, 0xFD)); 
		lineRuler.setForeground(SWTResourceManager.getColor(0xA0, 0xA0, 0xA0)); 		
						
		annotationRuler = new AnnotationRulerColumn(12,annotationAccess);
		annotationRuler.addAnnotationType(BulletAnnotation.TYPE);
		annotationRuler.addAnnotationType(ErrorAnnotation.TYPE);
		annotationRuler.setHover(new DefaultAnnotationHover());
		addVerticalRulerColumn(annotationRuler);
		addVerticalRulerColumn(lineRuler);	

		// Configure overview ruler 
		overviewRuler.addAnnotationType(ErrorAnnotation.TYPE);
		overviewRuler.setAnnotationTypeLayer(ErrorAnnotation.TYPE, 0);
		overviewRuler.setAnnotationTypeColor(ErrorAnnotation.TYPE, ResourceManager.getColor(SWT.COLOR_RED));
		overviewRuler.addHeaderAnnotationType(ErrorAnnotation.TYPE); 
		setPreferedFont();
		EditorPreference.getInstance().addPropertyChangeListener(this);
		

		AnnotationPainter painter = new AnnotationPainter(this, annotationAccess);
		painter.addTextStyleStrategy(ErrorAnnotation.TYPE, new AnnotationPainter.UnderlineStrategy(SWT.UNDERLINE_SQUIGGLE));
		painter.addHighlightAnnotationType(ErrorAnnotation.TYPE);
		painter.addDrawingStrategy(ErrorAnnotation.TYPE, new AnnotationPainter.SquigglesStrategy());
		painter.addAnnotationType(ErrorAnnotation.TYPE, ErrorAnnotation.TYPE);
		painter.setAnnotationTypeColor(ErrorAnnotation.TYPE, ResourceManager.getColor(SWT.COLOR_RED));
		addPainter(painter);
		addTextPresentationListener(painter);
		
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.SourceViewer#setDocument(org.eclipse.jface.text.IDocument)
	 */
	@Override
	public void setDocument(IDocument document) {		
		super.setDocument(document);		
	}

	public void setDocumentProvider(IDocumentProvider provider) throws GkException{
		setDocument(provider.getDocument(), provider.getAnnotationModel());		
		setEditable(provider.isModifiable());		
	}

	/**
	 * Set the font according to the preferences
	 */
	private void setPreferedFont(){
		String fontName = EditorPreference.getInstance().getFontName();
		int fontSize = EditorPreference.getInstance().getFontSize();
		Font font = new Font(null, fontName, fontSize, SWT.NORMAL);
		
		getTextWidget().setFont(font);
		getTextWidget().setForeground(SWTResourceManager.getColor(88,88,88));
		
		lineRuler.setFont(font);
		
		refresh();
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(StringUtils.equals(event.getProperty(), EditorPreference.FONT_NAME)
		|| StringUtils.equals(event.getProperty(), EditorPreference.FONT_SIZE)){
			getTextWidget().getShell().getDisplay().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					setPreferedFont();
				}
			});
			
		}
	}
}
//http://grepcode.com/file/repository.grepcode.com/java/eclipse.org/4.2/org.eclipse.ui/editors/3.8.0/org/eclipse/ui/texteditor/DefaultMarkerAnnotationAccess.java#DefaultMarkerAnnotationAccess.paint%28org.eclipse.jface.text.source.Annotation%2Corg.eclipse.ui.texteditor.GC%2Corg.eclipse.ui.texteditor.Canvas%2Corg.eclipse.ui.texteditor.Rectangle%29

