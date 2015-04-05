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
package org.goko.tinyg.configuration.bindings;

import org.goko.common.bindings.AbstractModelObject;

public class TinyGFirmwarePreferencePageModel extends AbstractModelObject{
	private boolean homingEnabledAxisX;
	private boolean homingEnabledAxisY;
	private boolean homingEnabledAxisZ;
	private boolean homingEnabledAxisA;
	
	public boolean isHomingEnabledAxisX() {
		return homingEnabledAxisX;
	}

	public void setHomingEnabledAxisX(boolean homingEnabledAxisX) {
		firePropertyChange("homingEnabledAxisX", this.homingEnabledAxisX, this.homingEnabledAxisX = homingEnabledAxisX);
	}

	public boolean isHomingEnabledAxisY() {
		return homingEnabledAxisY;
	}
	public boolean getHomingEnabledAxisY() {
		return homingEnabledAxisY;
	}
	public void setHomingEnabledAxisY(boolean homingEnabledAxisY) {
		firePropertyChange("homingEnabledAxisY", this.homingEnabledAxisY, this.homingEnabledAxisY = homingEnabledAxisY);
	}

	public boolean isHomingEnabledAxisZ() {
		return homingEnabledAxisZ;
	}
	public boolean getHomingEnabledAxisZ() {
		return homingEnabledAxisZ;
	}

	public void setHomingEnabledAxisZ(boolean homingEnabledAxisZ) {
		firePropertyChange("homingEnabledAxisZ", this.homingEnabledAxisZ, this.homingEnabledAxisZ = homingEnabledAxisZ);
	}

	public boolean isHomingEnabledAxisA() {
		return homingEnabledAxisA;
	}
	public boolean getHomingEnabledAxisA() {
		return homingEnabledAxisA;
	}
	public void setHomingEnabledAxisA(boolean homingEnabledAxisA) {
		firePropertyChange("homingEnabledAxisA", this.homingEnabledAxisA, this.homingEnabledAxisA = homingEnabledAxisA);
	}

}
