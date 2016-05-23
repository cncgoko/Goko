/**
 * 
 */
package org.goko.tools.editor.component;

import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * @author Psyko
 * @date 23 mai 2016
 */
public class GCodeSourceViewer extends SourceViewer{

	/**
	 * Constructor 
	 * @param parent
	 * @param styles
	 */
	public GCodeSourceViewer(Composite parent, int styles) {		
		super(parent, new CompositeRuler(), styles);
		
		GCodeSourceConfiguration gcodeSourceConfiguration = new GCodeSourceConfiguration();	
		configure(gcodeSourceConfiguration);
		
		LineNumberRulerColumn lineRuler = new LineNumberRulerColumn();
		lineRuler.setBackground(SWTResourceManager.getColor(0xFD, 0xFD, 0xFD)); // SWT.COLOR_INFO_BACKGROUND));
		lineRuler.setForeground(SWTResourceManager.getColor(0xA0, 0xA0, 0xA0)); // SWT.COLOR_INFO_FOREGROUND));		
				
		addVerticalRulerColumn(lineRuler);
	}

	
}
