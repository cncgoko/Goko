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
package org.goko.viewer.jogl.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.viewer.jogl.service.JoglViewerSettings.EnumRotaryAxisDirection;

/**
 * Model object for Jogl Viewer preferences
 *
 * @author PsyKo
 *
 */
public class JoglViewerPreferencesModel extends AbstractModelObject {

	private boolean rotaryAxisEnabled;
	private BigDecimal rotaryAxisPositionX;
	private BigDecimal rotaryAxisPositionY;
	private BigDecimal rotaryAxisPositionZ;
	private LabeledValue<EnumRotaryAxisDirection> rotaryAxisDirection;
	private List<LabeledValue<EnumRotaryAxisDirection>> rotaryAxisDirectionChoice;

	public JoglViewerPreferencesModel() {
		rotaryAxisDirectionChoice = new ArrayList<LabeledValue<EnumRotaryAxisDirection>>();
		rotaryAxisDirectionChoice.add( new LabeledValue<EnumRotaryAxisDirection>(EnumRotaryAxisDirection.X, "X axis"));
		rotaryAxisDirectionChoice.add( new LabeledValue<EnumRotaryAxisDirection>(EnumRotaryAxisDirection.Y, "Y axis"));
		rotaryAxisDirectionChoice.add( new LabeledValue<EnumRotaryAxisDirection>(EnumRotaryAxisDirection.Z, "Z axis"));
	}
	/**
	 * @return the rotaryAxisEnabled
	 */
	public boolean isRotaryAxisEnabled() {
		return rotaryAxisEnabled;
	}
	/**
	 * @param rotaryAxisEnabled the rotaryAxisEnabled to set
	 */
	public void setRotaryAxisEnabled(boolean rotaryAxisEnabled) {
		firePropertyChange("rotaryAxisEnabled", this.rotaryAxisEnabled, this.rotaryAxisEnabled =rotaryAxisEnabled);
	}
	/**
	 * @return the rotaryAxisPositionX
	 */
	public BigDecimal getRotaryAxisPositionX() {
		return rotaryAxisPositionX;
	}
	/**
	 * @param rotaryAxisPositionX the rotaryAxisPositionX to set
	 */
	public void setRotaryAxisPositionX(BigDecimal rotaryAxisPositionX) {
		firePropertyChange("rotaryAxisPositionX", this.rotaryAxisPositionX, this.rotaryAxisPositionX =rotaryAxisPositionX);
	}
	/**
	 * @return the rotaryAxisPositionY
	 */
	public BigDecimal getRotaryAxisPositionY() {
		return rotaryAxisPositionY;
	}
	/**
	 * @param rotaryAxisPositionY the rotaryAxisPositionY to set
	 */
	public void setRotaryAxisPositionY(BigDecimal rotaryAxisPositionY) {
		firePropertyChange("rotaryAxisPositionY", this.rotaryAxisPositionY, this.rotaryAxisPositionY =rotaryAxisPositionY);
	}
	/**
	 * @return the rotaryAxisPositionZ
	 */
	public BigDecimal getRotaryAxisPositionZ() {
		return rotaryAxisPositionZ;
	}
	/**
	 * @param rotaryAxisPositionZ the rotaryAxisPositionZ to set
	 */
	public void setRotaryAxisPositionZ(BigDecimal rotaryAxisPositionZ) {
		firePropertyChange("rotaryAxisPositionZ", this.rotaryAxisPositionZ, this.rotaryAxisPositionZ =rotaryAxisPositionZ);
	}
	/**
	 * @return the rotaryAxisDirection
	 */
	public LabeledValue<EnumRotaryAxisDirection> getRotaryAxisDirection() {
		return rotaryAxisDirection;
	}
	/**
	 * @param rotaryAxisDirection the rotaryAxisDirection to set
	 */
	public void setRotaryAxisDirection(LabeledValue<EnumRotaryAxisDirection> rotaryAxisDirection) {
		firePropertyChange("rotaryAxisDirection", this.rotaryAxisDirection, this.rotaryAxisDirection = rotaryAxisDirection);
	}
	/**
	 * @return the rotaryAxisDirectionChoice
	 */
	public List<LabeledValue<EnumRotaryAxisDirection>> getRotaryAxisDirectionChoice() {
		return rotaryAxisDirectionChoice;
	}
	/**
	 * @param rotaryAxisDirectionChoice the rotaryAxisDirectionChoice to set
	 */
	public void setRotaryAxisDirectionChoice(
			List<LabeledValue<EnumRotaryAxisDirection>> rotaryAxisDirectionChoice) {
		this.rotaryAxisDirectionChoice = rotaryAxisDirectionChoice;
	}

}
