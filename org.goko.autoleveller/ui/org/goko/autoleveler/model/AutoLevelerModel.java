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
package org.goko.autoleveler.model;

import java.math.BigDecimal;

import org.goko.autoleveler.bean.GridElevationMap;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.gcode.bean.IGCodeProvider;

public class AutoLevelerModel extends AbstractModelObject{
	private BigDecimal startx;
	private BigDecimal starty;
	private BigDecimal endx;
	private BigDecimal endy;
	private BigDecimal safeZ;
	private BigDecimal maxZProbe;
	private BigDecimal startProbe;
	private BigDecimal expectedZ;
	private BigDecimal stepX;
	private BigDecimal stepY;
	private GridElevationMap map;
	private IGCodeProvider gcodeProvider;
	/**
	 * @return the startx
	 */
	public BigDecimal getStartx() {
		return startx;
	}
	/**
	 * @param startx the startx to set
	 */
	public void setStartx(BigDecimal startx) {
		firePropertyChange("startx", this.startx, this.startx = startx);
	}
	/**
	 * @return the starty
	 */
	public BigDecimal getStarty() {
		return starty;
	}
	/**
	 * @param starty the starty to set
	 */
	public void setStarty(BigDecimal starty) {
		firePropertyChange("starty", this.starty, this.starty = starty);
	}
	/**
	 * @return the endx
	 */
	public BigDecimal getEndx() {
		return endx;
	}
	/**
	 * @param endx the endx to set
	 */
	public void setEndx(BigDecimal endx) {
		firePropertyChange("endx", this.endx, this.endx = endx);
	}
	/**
	 * @return the endy
	 */
	public BigDecimal getEndy() {
		return endy;
	}
	/**
	 * @param endy the endy to set
	 */
	public void setEndy(BigDecimal endy) {
		firePropertyChange("endy", this.endy, this.endy = endy);
	}
	/**
	 * @return the safeZ
	 */
	public BigDecimal getSafeZ() {
		return safeZ;
	}
	/**
	 * @param safeZ the safeZ to set
	 */
	public void setSafeZ(BigDecimal safeZ) {
		firePropertyChange("safeZ", this.safeZ, this.safeZ = safeZ);
	}
	/**
	 * @return the maxZProbe
	 */
	public BigDecimal getMaxZProbe() {
		return maxZProbe;
	}
	/**
	 * @param maxZProbe the maxZProbe to set
	 */
	public void setMaxZProbe(BigDecimal maxZProbe) {
		firePropertyChange("maxZProbe", this.maxZProbe, this.maxZProbe = maxZProbe);
	}
	/**
	 * @return the startProbe
	 */
	public BigDecimal getStartProbe() {
		return startProbe;
	}
	/**
	 * @param startProbe the startProbe to set
	 */
	public void setStartProbe(BigDecimal startProbe) {
		firePropertyChange("startProbe", this.startProbe, this.startProbe = startProbe);
	}
	/**
	 * @return the expectedZ
	 */
	public BigDecimal getExpectedZ() {
		return expectedZ;
	}
	/**
	 * @param expectedZ the expectedZ to set
	 */
	public void setExpectedZ(BigDecimal expectedZ) {
		firePropertyChange("expectedZ", this.expectedZ, this.expectedZ = expectedZ);
	}
	/**
	 * @return the stepX
	 */
	public BigDecimal getStepX() {
		return stepX;
	}
	/**
	 * @param stepX the stepX to set
	 */
	public void setStepX(BigDecimal stepX) {
		firePropertyChange("stepX", this.stepX, this.stepX = stepX);
	}
	/**
	 * @return the stepY
	 */
	public BigDecimal getStepY() {
		return stepY;
	}
	/**
	 * @param stepY the stepY to set
	 */
	public void setStepY(BigDecimal stepY) {
		firePropertyChange("stepY", this.stepY, this.stepY = stepY);
	}
	/**
	 * @return the map
	 */
	public GridElevationMap getMap() {
		return map;
	}
	/**
	 * @param map the map to set
	 */
	public void setMap(GridElevationMap map) {
		this.map = map;
	}

	/**
	 * @return the gcodeProvider
	 */
	public IGCodeProvider getGcodeProvider() {
		return gcodeProvider;
	}
	/**
	 * @param gcodeProvider the gcodeProvider to set
	 */
	public void setGcodeProvider(IGCodeProvider gcodeProvider) {
		this.gcodeProvider = gcodeProvider;
	}
}
