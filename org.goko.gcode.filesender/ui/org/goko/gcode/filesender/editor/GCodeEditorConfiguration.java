package org.goko.gcode.filesender.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class GCodeEditorConfiguration extends SourceViewerConfiguration {

	private ITokenScanner scanner;

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer damager = new DefaultDamagerRepairer(getScanner());
		reconciler.setDamager(damager, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(damager, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}


	private ITokenScanner getScanner(){
		if(scanner == null) {
			scanner=new GCodeEditorScanner();
		}
		return scanner;
	}
}
