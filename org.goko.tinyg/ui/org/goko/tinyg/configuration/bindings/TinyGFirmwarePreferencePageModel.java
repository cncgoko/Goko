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
	/** Selected firmware version */
	private String firmwareVersion;

	/**
	 * @return the firmwareVersion
	 */
	protected String getFirmwareVersion() {
		return firmwareVersion;
	}

	/**
	 * @param firmwareVersion the firmwareVersion to set
	 */
	protected void setFirmwareVersion(String firmwareVersion) {
		firePropertyChange("this.firmwareVersion", this.firmwareVersion, this.firmwareVersion = firmwareVersion);
	}

}
