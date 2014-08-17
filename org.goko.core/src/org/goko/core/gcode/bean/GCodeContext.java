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
	private String motionMode;
	private Tuple6b offset;
	private Tuple6b position;
	private BigDecimal feedrate;
	private boolean isMetric;
	private boolean isAbsolute = true;
	private int currentPlane;


	public GCodeContext() {
		position = new Tuple6b();
		offset = new Tuple6b();
		this.motionMode = "G0";
	}

	public GCodeContext(GCodeContext context) {
		this.position = new Tuple6b(context.position);
		this.offset = new Tuple6b(context.offset);
		this.feedrate = context.feedrate;
		this.isAbsolute = context.isAbsolute;
		this.isMetric = context.isMetric;
		this.currentPlane = context.currentPlane;
		this.motionMode = context.motionMode;
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

	/**
	 * @return the position
	 */
	public Tuple6b getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Tuple6b position) {
		this.position = position;
	}

	/**
	 * @return the motionMode
	 */
	public String getMotionMode() {
		return motionMode;
	}

	/**
	 * @param motionMode the motionMode to set
	 */
	public void setMotionMode(String motionMode) {
		this.motionMode = motionMode;
	}

	/**
	 * @return the offset
	 */
	public Tuple6b getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Tuple6b offset) {
		this.offset = offset;
	}

	/**
	 * @return the currentPlane
	 */
	public int getCurrentPlane() {
		return currentPlane;
	}

	/**
	 * @param currentPlane the currentPlane to set
	 */
	public void setCurrentPlane(int currentPlane) {
		this.currentPlane = currentPlane;
	}


}
