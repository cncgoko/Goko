/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.gcode.rs274ngcv3;

import org.goko.common.bindings.AbstractModelObject;

public class RS274GCodePreferencesModel extends AbstractModelObject {
	private int decimalCount;
	private boolean truncateEnabled;
	/**
	 * @return the decimalCount
	 */
	public int getDecimalCount() {
		return decimalCount;
	}
	/**
	 * @param decimalCount the decimalCount to set
	 */
	public void setDecimalCount(int decimalCount) {
		firePropertyChange("decimalCount", this.decimalCount, this.decimalCount = decimalCount);
	}
	/**
	 * @return the truncateEnabled
	 */
	public boolean isTruncateEnabled() {
		return truncateEnabled;
	}
	/**
	 * @param truncateEnabled the truncateEnabled to set
	 */
	public void setTruncateEnabled(boolean truncateEnabled) {
		firePropertyChange("truncateEnabled", this.truncateEnabled, this.truncateEnabled =truncateEnabled);
	}

}
