package org.goko.gcode.filesender.editor;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

public class GCodeEditor extends SourceViewer{

	/**
	 * @param parent
	 * @param ruler
	 * @param styles
	 */
	public GCodeEditor(Composite parent, IVerticalRuler ruler, int styles) {
		super(parent, ruler, styles);
		GCodeEditorConfiguration configuration = new GCodeEditorConfiguration();
		configure(configuration);
	}

	/**
	 * @param parent
	 * @param verticalRuler
	 * @param overviewRuler
	 * @param showAnnotationsOverview
	 * @param styles
	 */
	public GCodeEditor(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler, boolean showAnnotationsOverview,
			int styles) {
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles);
		GCodeEditorConfiguration configuration = new GCodeEditorConfiguration();
		configure(configuration);
	}


}
