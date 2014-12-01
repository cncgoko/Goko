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
package org.goko.gcode.filesender.handler;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.FileDialog;
import org.goko.gcode.filesender.FileSenderPart;

public class OpenGCodeFileHandler {

	@CanExecute
	public boolean canExecute(){
		return true;
	}

	@Execute
	@Inject
	public void execute(FileSenderPart part){
		FileDialog dialog = new FileDialog(part.getShell());
		dialog.setText("Open GCode file...");

		String filePath = dialog.open();
		part.getDataModel().setFilePath(filePath);

		if (StringUtils.isNotBlank(filePath)) {
			part.getController().setGCodeFilepath(filePath);
		}


	}
}
