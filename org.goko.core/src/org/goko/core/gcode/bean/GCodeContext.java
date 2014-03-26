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
package org.goko.core.gcode.bean;

import java.math.BigDecimal;

public class GCodeContext {
	private BigDecimal positionX;
	private BigDecimal positionY;
	private BigDecimal positionZ;
	private BigDecimal positionA;
	private BigDecimal positionB;
	private BigDecimal positionC;
	private BigDecimal feedrate;
	private boolean isMetric;
	private boolean isAbsolute = true;
	private int currentPlane;

	public GCodeContext() {
		positionX = new BigDecimal(0);
		positionY = new BigDecimal(0);
		positionZ = new BigDecimal(0);
		positionA = new BigDecimal(0);
		positionB = new BigDecimal(0);
		positionC = new BigDecimal(0);
	}
	/**
	 * @return the positionX
	 */
	public BigDecimal getPositionX() {
		return positionX;
	}
	/**
	 * @param positionX the positionX to set
	 */
	public void setPositionX(BigDecimal positionX) {
		this.positionX = positionX;
	}
	/**
	 * @return the positionY
	 */
	public BigDecimal getPositionY() {
		return positionY;
	}
	/**
	 * @param positionY the positionY to set
	 */
	public void setPositionY(BigDecimal positionY) {
		this.positionY = positionY;
	}
	/**
	 * @return the positionZ
	 */
	public BigDecimal getPositionZ() {
		return positionZ;
	}
	/**
	 * @param positionZ the positionZ to set
	 */
	public void setPositionZ(BigDecimal positionZ) {
		this.positionZ = positionZ;
	}
	/**
	 * @return the positionA
	 */
	public BigDecimal getPositionA() {
		return positionA;
	}
	/**
	 * @param positionA the positionA to set
	 */
	public void setPositionA(BigDecimal positionA) {
		this.positionA = positionA;
	}
	/**
	 * @return the positionB
	 */
	public BigDecimal getPositionB() {
		return positionB;
	}
	/**
	 * @param positionB the positionB to set
	 */
	public void setPositionB(BigDecimal positionB) {
		this.positionB = positionB;
	}
	/**
	 * @return the positionC
	 */
	public BigDecimal getPositionC() {
		return positionC;
	}
	/**
	 * @param positionC the positionC to set
	 */
	public void setPositionC(BigDecimal positionC) {
		this.positionC = positionC;
	}
	/**
	 * @return the feedrate
	 */
	public BigDecimal getFeedrate() {
		return feedrate;
	}
	/**
	 * @param feedrate the feedrate to set
	 */
	public void setFeedrate(BigDecimal feedrate) {
		this.feedrate = feedrate;
	}
	/**
	 * @return the isMetric
	 */
	public boolean isMetric() {
		return isMetric;
	}
	/**
	 * @param isMetric the isMetric to set
	 */
	public void setMetric(boolean isMetric) {
		this.isMetric = isMetric;
	}
	/**
	 * @return the isAbsolute
	 */
	public boolean isAbsolute() {
		return isAbsolute;
	}
	/**
	 * @param isAbsolute the isAbsolute to set
	 */
	public void setAbsolute(boolean isAbsolute) {
		this.isAbsolute = isAbsolute;
	}


}
