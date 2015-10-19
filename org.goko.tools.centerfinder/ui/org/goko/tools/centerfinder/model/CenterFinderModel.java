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
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;
import org.goko.core.math.Tuple6b;
import org.goko.tools.centerfinder.bean.CircleCenterFinderResult;

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
	private String radius;
	private Tuple6b selectedPoint;
	private CircleCenterFinderResult circleCenterResult;

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
	/**
	 * @return the circleCenterResult
	 */
	public CircleCenterFinderResult getCircleCenterResult() {
		return circleCenterResult;
	}
	/**
	 * @param circleCenterResult the circleCenterResult to set
	 * @throws GkException 
	 */
	public void setCircleCenterResult(CircleCenterFinderResult circleCenterResult) throws GkException {
		this.circleCenterResult = circleCenterResult;
		if(circleCenterResult != null && circleCenterResult.getCenter() != null){
			
			setCenterXPosition(GokoPreference.getInstance().format(circleCenterResult.getCenter().getX(), true, true));
			setCenterYPosition(GokoPreference.getInstance().format(circleCenterResult.getCenter().getY(), true, true));
			setCenterZPosition(GokoPreference.getInstance().format(circleCenterResult.getCenter().getZ(), true, true));
			setRadius(GokoPreference.getInstance().format(circleCenterResult.getRadius(), true, true));
//			setCenterXPosition(new BigDecimal(circleCenterResult.getCenter().x).setScale(3, BigDecimal.ROUND_HALF_DOWN).toString());
//			setCenterYPosition(new BigDecimal(circleCenterResult.getCenter().y).setScale(3, BigDecimal.ROUND_HALF_DOWN).toString());
//			setCenterZPosition(new BigDecimal(circleCenterResult.getCenter().z).setScale(3, BigDecimal.ROUND_HALF_DOWN).toString());
//			setRadius(new BigDecimal(circleCenterResult.getRadius()).setScale(3, BigDecimal.ROUND_HALF_DOWN).toString());
		}else{
			setCenterXPosition("--");
			setCenterYPosition("--");
			setCenterZPosition("--");
			setRadius("--");
		}
	}
	/**
	 * @return the radius
	 */
	public String getRadius() {
		return radius;
	}
	/**
	 * @param radius the radius to set
	 */
	public void setRadius(String radius) {
		firePropertyChange("radius", this.radius, this.radius = radius);
	}


}
