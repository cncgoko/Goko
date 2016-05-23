/**
 * 
 */
package org.goko.tools.editor.component;

import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.goko.tools.editor.component.scanner.CommentScanner;
import org.goko.tools.editor.component.scanner.CoordinateWordScanner;
import org.goko.tools.editor.component.scanner.GWordScanner;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class GCodeSourceConfiguration extends SourceViewerConfiguration {
	/** Supported contents */
	public static String[] CONTENT_TYPES = new String[]{
			GCodePartitionScanner.GCODE_G_COMMENT,
			GCodePartitionScanner.GCODE_G_WORD,
			GCodePartitionScanner.GCODE_G0_WORD,
			GCodePartitionScanner.GCODE_G1_WORD,
			GCodePartitionScanner.GCODE_COORD_WORD,
	};
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {		
		return CONTENT_TYPES;
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		
//		DefaultDamagerRepairer g0DamageRepairer = new DefaultDamagerRepairer(new G0WordScanner());
//	    reconciler.setDamager(g0DamageRepairer, GCodePartitionScanner.GCODE_G0_WORD);
//	    reconciler.setRepairer(g0DamageRepairer, GCodePartitionScanner.GCODE_G0_WORD);
//	    
//		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new G1WordScanner());
//	    reconciler.setDamager(dr, GCodePartitionScanner.GCODE_G1_WORD);
//	    reconciler.setRepairer(dr, GCodePartitionScanner.GCODE_G1_WORD);
	    	    
	    DefaultDamagerRepairer coordDamageRepairer = new DefaultDamagerRepairer(new CoordinateWordScanner());
	    reconciler.setDamager( coordDamageRepairer, GCodePartitionScanner.GCODE_COORD_WORD);
	    reconciler.setRepairer(coordDamageRepairer, GCodePartitionScanner.GCODE_COORD_WORD);
	    
	    DefaultDamagerRepairer gDamageRepairer = new DefaultDamagerRepairer(new GWordScanner());
	    reconciler.setDamager( gDamageRepairer, GCodePartitionScanner.GCODE_G_WORD);
	    reconciler.setRepairer(gDamageRepairer, GCodePartitionScanner.GCODE_G_WORD);
	    
	    DefaultDamagerRepairer commentDamageRepairer = new DefaultDamagerRepairer(new CommentScanner());
	    reconciler.setDamager( commentDamageRepairer, GCodePartitionScanner.GCODE_G_COMMENT);
	    reconciler.setRepairer(commentDamageRepairer, GCodePartitionScanner.GCODE_G_COMMENT);
	  //  a continuer avec les commentaires
		return reconciler;
	}

}
