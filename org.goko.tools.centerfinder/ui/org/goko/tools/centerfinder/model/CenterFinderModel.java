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
package org.goko.tools.centerfinder.model;

import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.gcode.bean.Tuple6b;

/**
 * Center finder model
 *
 * @author PsyKo
 *
 */
public class CenterFinderModel extends AbstractModelObject{

	private IObservableList samplePoints;
	private String centerXPosition;
	private String centerYPosition;
	private String centerZPosition;
	private Tuple6b selectedPoint;

	public CenterFinderModel(){
		samplePoints = new WritableList();
	}
	/**
	 * @return the samplePoints
	 */
	public IObservableList getSamplePoints() {
		return samplePoints;
	}

	/**
	 * @param samplePoints the samplePoints to set
	 */
	public void setSamplePoints(List<Tuple6b> samplePoints) {
		IObservableList writableList = new WritableList(samplePoints, Tuple6b.class);
		firePropertyChange("samplePoints", this.samplePoints, this.samplePoints = writableList);
	}
	/**
	 * @return the centerXPosition
	 */
	public String getCenterXPosition() {
		return centerXPosition;
	}
	/**
	 * @param centerXPosition the centerXPosition to set
	 */
	public void setCenterXPosition(String centerXPosition) {
		firePropertyChange("centerXPosition", this.centerXPosition, this.centerXPosition = centerXPosition);
	}
	/**
	 * @return the centerYPosition
	 */
	public String getCenterYPosition() {
		return centerYPosition;
	}
	/**
	 * @param centerYPosition the centerYPosition to set
	 */
	public void setCenterYPosition(String centerYPosition) {
		firePropertyChange("centerYPosition", this.centerYPosition, this.centerYPosition = centerYPosition);
	}
	/**
	 * @return the centerZPosition
	 */
	public String getCenterZPosition() {
		return centerZPosition;
	}
	/**
	 * @param centerZPosition the centerZPosition to set
	 */
	public void setCenterZPosition(String centerZPosition) {
		firePropertyChange("centerZPosition", this.centerZPosition, this.centerZPosition = centerZPosition);
	}
	/**
	 * @return the selectedPoint
	 */
	public Tuple6b getSelectedPoint() {
		return selectedPoint;
	}
	/**
	 * @param selectedPoint the selectedPoint to set
	 */
	public void setSelectedPoint(Tuple6b selectedPoint) {
		firePropertyChange("selectedPoint", this.selectedPoint, this.selectedPoint = selectedPoint);
	}


}
