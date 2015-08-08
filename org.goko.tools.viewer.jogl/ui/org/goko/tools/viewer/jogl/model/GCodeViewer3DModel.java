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
package org.goko.tools.viewer.jogl.model;

import javax.vecmath.Point3d;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.gcode.bean.IGCodeProvider;

/**
 * GCode 3D viewer model class
 * @author PsyKo
 *
 */
public class GCodeViewer3DModel extends AbstractModelObject{
	/** Display grid ?? */
	private boolean showGrid = true;

	private Point3d currentPosition;

	private IGCodeProvider commandProvider;

	private boolean enabled;

	private boolean followTool;

	private boolean showCoordinateSystem;

	private boolean showBounds;

	private IObservableMap coordinateSystemEnabled;

	public GCodeViewer3DModel() {
		coordinateSystemEnabled =new WritableMap(String.class, Boolean.class);
	}
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

	public boolean isEnabled(){
		return enabled;
	}
	public boolean getEnabled(){
		return enabled;
	}
	/**
	 * @param showGrid the showGrid to set
	 */
	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled",this.enabled, this.enabled = enabled);
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
	public IGCodeProvider getCommandProvider() {
		return commandProvider;
	}
	/**
	 * @param commandProvider the commandProvider to set
	 */
	public void setCommandProvider(IGCodeProvider commandProvider) {
		firePropertyChange("commandProvider",this.commandProvider, this.commandProvider = commandProvider);
	}
	/**
	 * @return the followTool
	 */
	public boolean isFollowTool() {
		return followTool;
	}
	public boolean getFollowTool() {
		return isFollowTool();
	}
	/**
	 * @param followTool the followTool to set
	 */
	public void setFollowTool(boolean followTool) {
		firePropertyChange("followTool",this.followTool, this.followTool = followTool);
	}
	/**
	 * @return the showCoordinateSystem
	 */
	public boolean isShowCoordinateSystem() {
		return showCoordinateSystem;
	}
	/**
	 * @param showCoordinateSystem the showCoordinateSystem to set
	 */
	public void setShowCoordinateSystem(boolean showCoordinateSystem) {
		firePropertyChange("showCoordinateSystem",this.showCoordinateSystem, this.showCoordinateSystem = showCoordinateSystem);
	}
	/**
	 * @return the showBounds
	 */
	protected boolean isShowBounds() {
		return showBounds;
	}
	/**
	 * @param showBounds the showBounds to set
	 */
	protected void setShowBounds(boolean showBounds) {
		firePropertyChange("showBounds",this.showBounds, this.showBounds = showBounds);
	}
	/**
	 * @return the coordinateSystemEnabled
	 */
	public IObservableMap getCoordinateSystemEnabled() {
		return coordinateSystemEnabled;
	}
	/**
	 * @param coordinateSystemEnabled the coordinateSystemEnabled to set
	 */
	public void setCoordinateSystemEnabled(IObservableMap coordinateSystemEnabled) {
		this.coordinateSystemEnabled = coordinateSystemEnabled;
	}

}
