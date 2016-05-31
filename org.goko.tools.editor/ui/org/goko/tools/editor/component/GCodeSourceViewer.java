/**
 * 
 */
package org.goko.tools.editor.component;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.tools.editor.component.ruler.LineNumberRulerColumn;
import org.goko.tools.editor.preferences.EditorPreference;

/**
 * @author Psyko
 * @date 23 mai 2016
 */
public class GCodeSourceViewer extends SourceViewer implements IPropertyChangeListener{
	private LineNumberRulerColumn lineRuler;
	
	/**
	 * Constructor 
	 * @param parent
	 * @param styles
	 */
	public GCodeSourceViewer(Composite parent, int styles) {		
		super(parent, new CompositeRuler(), styles);
		
		GCodeSourceConfiguration gcodeSourceConfiguration = new GCodeSourceConfiguration();	
		configure(gcodeSourceConfiguration);
		
		lineRuler = new LineNumberRulerColumn();
		lineRuler.setBackground(SWTResourceManager.getColor(0xFD, 0xFD, 0xFD)); // SWT.COLOR_INFO_BACKGROUND));
		lineRuler.setForeground(SWTResourceManager.getColor(0xA0, 0xA0, 0xA0)); // SWT.COLOR_INFO_FOREGROUND));		
		
		addVerticalRulerColumn(lineRuler);
		setPreferedFont();
		EditorPreference.getInstance().addPropertyChangeListener(this);
	}
	
//	- changer la taille de la font de la ruler des lignes en meme temps que celle du texte
//	- rajouter les mots du gcode (I, J, K, G55, G54, etc...)
//	- rajouter le rafraichissement du gcode
//	
	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.SourceViewer#setDocument(org.eclipse.jface.text.IDocument)
	 */
	@Override
	public void setDocument(IDocument document) {		
		super.setDocument(document);		
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
