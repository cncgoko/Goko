/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.log.part.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.applicative.logging.ApplicativeLogEvent;


public class ProblemTreeLabelProvider extends StyledCellLabelProvider {
	// TODO Remove JFace init from here
	private static RGB HYPERLINK_COLOR = new RGB(59,153,204);
	public static int COLUMN_DESCRIPTION = 0;
	public static int COLUMN_SOURCE = 1;
	public static int COLUMN_DATE = 2;
	private int targetColumn;


	public ProblemTreeLabelProvider(int targetColumn) {
		super();
		this.targetColumn = targetColumn;
	}

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if(element == null){
			super.update(cell);
			return;
		}
		JFaceResources.getColorRegistry().put(JFacePreferences.COUNTER_COLOR, HYPERLINK_COLOR);
		StyledString styledString = new StyledString();

		if(element instanceof LogLevelNode){
			LogLevelNode logNode = (LogLevelNode) element;
			if(targetColumn == COLUMN_DESCRIPTION){
				styledString.append(logNode.getLabel());
				styledString.append(" (" +CollectionUtils.size(logNode.getMessages()) + ")", StyledString.COUNTER_STYLER);
				cell.setImage(getImage(element));

			}
		}else if(element instanceof ApplicativeLogEvent){
			ApplicativeLogEvent event = (ApplicativeLogEvent) element;
			if(targetColumn == COLUMN_DESCRIPTION){
				styledString.append(event.getMessage());
			}else if(targetColumn == COLUMN_SOURCE){
				styledString.append(event.getSource());
			}else if(targetColumn == COLUMN_DATE){
				DateFormat df = new SimpleDateFormat("HH:mm:ss");
				styledString.append( df.format( event.getDate()));
			}
		}else{
			styledString.append( String.valueOf(element));
		}

		cell.setText(styledString.getString());
		cell.setStyleRanges(styledString.getStyleRanges());

		super.update(cell);
	}

	public Image getImage(Object element) {
		if(element instanceof LogLevelNode){
			LogLevelNode logNode = (LogLevelNode) element;
			if(logNode.getErrorLevel() == ProblemTreeContent.ERROR){
				return ResourceManager.getPluginImage("org.goko.log.ui", "icons/error.gif");
			}else if(logNode.getErrorLevel() == ProblemTreeContent.WARNING){
				return ResourceManager.getPluginImage("org.goko.log.ui", "icons/warning.gif");
			}
		}
		return null;
	}
//
//	@Override
//	public void addListener(ILabelProviderListener listener) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void dispose() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public boolean isLabelProperty(Object element, String property) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void removeListener(ILabelProviderListener listener) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public Image getColumnImage(Object element, int columnIndex) {
//		if(element instanceof LogLevelNode){
//			LogLevelNode logNode = (LogLevelNode) element;
//			if(logNode.getErrorLevel() == ProblemTreeContent.ERROR){
//				return ResourceManager.getPluginImage("org.goko.log.ui", "icons/error.gif");
//			}else if(logNode.getErrorLevel() == ProblemTreeContent.WARNING){
//				return ResourceManager.getPluginImage("org.goko.log.ui", "icons/warning.gif");
//			}
//		}
//
//		return null;
//	}
//
//	@Override
//	public String getColumnText(Object element, int columnIndex) {
//
//		if(element instanceof String){
//			return (String) element;
//		}else if(element instanceof LogLevelNode){
//			LogLevelNode logNode = (LogLevelNode) element;
//			return logNode.getLabel()+ " ("+CollectionUtils.size(logNode.getMessages())+")";
//		}
//
//		return StringUtils.EMPTY;
//	}

}
