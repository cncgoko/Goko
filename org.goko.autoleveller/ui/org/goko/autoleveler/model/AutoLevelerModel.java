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
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.bean.IGCodeProvider;

public class AutoLevelerModel extends AbstractModelObject{
	private BigDecimalQuantity<Length> startx;
	private BigDecimalQuantity<Length> starty;
	private BigDecimalQuantity<Length> endx;
	private BigDecimalQuantity<Length> endy;
	private BigDecimalQuantity<Length> safeZ;
	private BigDecimalQuantity<Length> maxZProbe;
	private BigDecimalQuantity<Length> startProbe;
	private BigDecimalQuantity<Length> expectedZ;
	private BigDecimal stepX;
	private BigDecimal stepY;
	private GridElevationMap map;
	private IGCodeProvider gcodeProvider;
	
	/**
	 * @return the startx
	 */
	public BigDecimalQuantity<Length> getStartx() {
		return startx;
	}
	
	/**
	 * @param startx the startx to set
	 */
	public void setStartx(BigDecimalQuantity<Length> startx) {
		firePropertyChange("startx", this.startx, this.startx = startx);
	}
	
	/**
	 * @return the starty
	 */
	public BigDecimalQuantity<Length> getStarty() {
		return starty;
	}
	
	/**
	 * @param starty the starty to set
	 */
	public void setStarty(BigDecimalQuantity<Length> starty) {
		firePropertyChange("starty", this.starty, this.starty = starty);
	}
	
	/**
	 * @return the endx
	 */
	public BigDecimalQuantity<Length> getEndx() {
		return endx;
	}
	
	/**
	 * @param endx the endx to set
	 */
	public void setEndx(BigDecimalQuantity<Length> endx) {
		firePropertyChange("endx", this.endx, this.endx = endx);
	}
	
	/**
	 * @return the endy
	 */
	public BigDecimalQuantity<Length> getEndy() {
		return endy;
	}
	
	/**
	 * @param endy the endy to set
	 */
	public void setEndy(BigDecimalQuantity<Length> endy) {
		firePropertyChange("endy", this.endy, this.endy = endy);
	}
	
	/**
	 * @return the safeZ
	 */
	public BigDecimalQuantity<Length> getSafeZ() {
		return safeZ;
	}
	
	/**
	 * @param safeZ the safeZ to set
	 */
	public void setSafeZ(BigDecimalQuantity<Length> safeZ) {
		firePropertyChange("safeZ", this.safeZ, this.safeZ = safeZ);
	}
	
	/**
	 * @return the maxZProbe
	 */
	public BigDecimalQuantity<Length> getMaxZProbe() {
		return maxZProbe;
	}
	
	/**
	 * @param maxZProbe the maxZProbe to set
	 */
	public void setMaxZProbe(BigDecimalQuantity<Length> maxZProbe) {
		firePropertyChange("maxZProbe", this.maxZProbe, this.maxZProbe = maxZProbe);
	}
	
	/**
	 * @return the startProbe
	 */
	public BigDecimalQuantity<Length> getStartProbe() {
		return startProbe;
	}
	
	/**
	 * @param startProbe the startProbe to set
	 */
	public void setStartProbe(BigDecimalQuantity<Length> startProbe) {
		firePropertyChange("startProbe", this.startProbe, this.startProbe = startProbe);
	}
	
	/**
	 * @return the expectedZ
	 */
	public BigDecimalQuantity<Length> getExpectedZ() {
		return expectedZ;
	}
	
	/**
	 * @param expectedZ the expectedZ to set
	 */
	public void setExpectedZ(BigDecimalQuantity<Length> expectedZ) {
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
