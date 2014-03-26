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
package org.goko.gcode.viewer.controller;

import javax.vecmath.Point3d;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.gcode.bean.IGCodeCommandProvider;

/**
 * GCode 3D viewer model class
 * @author PsyKo
 *
 */
public class GCodeViewer3DModel extends AbstractModelObject{
	/** Display grid ?? */
	private boolean showGrid = true;

	private Point3d currentPosition;

	private IGCodeCommandProvider commandProvider;

	/**
	 * @return the showGrid
	 */
	public boolean isShowGrid() {
		return showGrid;
	}
	public boolean getShowGrid() {
		return isShowGrid();
	}

	/**
	 * @param showGrid the showGrid to set
	 */
	public void setShowGrid(boolean showGrid) {
		firePropertyChange("showGrid",this.showGrid, this.showGrid = showGrid);
	}
	/**
	 * @return the currentPosition
	 */
	public Point3d getCurrentPosition() {
		return currentPosition;
	}
	/**
	 * @param currentPosition the currentPosition to set
	 */
	public void setCurrentPosition(Point3d currentPosition) {
		firePropertyChange("currentPosition",this.currentPosition, this.currentPosition = currentPosition);
	}
	/**
	 * @return the commandProvider
	 */
	public IGCodeCommandProvider getCommandProvider() {
		return commandProvider;
	}
	/**
	 * @param commandProvider the commandProvider to set
	 */
	public void setCommandProvider(IGCodeCommandProvider commandProvider) {
		firePropertyChange("commandProvider",this.commandProvider, this.commandProvider = commandProvider);
	}
}
