/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.gcode.filesender.editor;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.gcode.filesender.controller.GCodeDocumentProvider;

public class GCodeEditor extends SourceViewer{
	private static final int SELECTED_COMMAND = SWT.COLOR_GREEN;

	private int selectedCommand;
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
	public GCodeEditor(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler, boolean showAnnotationsOverview, int styles) {
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles);
		GCodeEditorConfiguration configuration = new GCodeEditorConfiguration();
		configure(configuration);

	}

	/**
	 * @return the selectedCommand
	 */
	public int getSelectedCommand() {
		return selectedCommand;
	}

	public void setSelectedCommand(int idCommand){
		if(selectedCommand != -1){
			int previousLine = getDocument().getLineForCommand(selectedCommand);
			if(previousLine >= 0){
				getTextWidget().setLineBackground(previousLine, 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		}
		this.selectedCommand = idCommand;
		if(selectedCommand != -1){
			int newLine = getDocument().getLineForCommand(idCommand);
			if(newLine >= 0){
				getTextWidget().setLineBackground(newLine, 1, SWTResourceManager.getColor(SELECTED_COMMAND));
				int nbLineDisplayed = this.getControl().getSize().y / getTextWidget().getLineHeight();
				getTextWidget().setTopIndex(Math.max(0, newLine-nbLineDisplayed/2));

			}
		}
		getTextWidget().redraw();
	}

	@Override
	public GCodeDocumentProvider getDocument() {
		return (GCodeDocumentProvider) super.getDocument();
	}
}
