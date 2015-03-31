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

package org.goko.preferences.units;

import java.util.List;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.config.EnumGokoUnit;

public class GokoUnitsPreferencesModel extends AbstractModelObject{
	private int decimalCount;
	private List<LabeledValue<EnumGokoUnit>> lengthUnitChoice;
	private LabeledValue<EnumGokoUnit> lengthUnit;


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
	 * @return the lengthUnitChoice
	 */
	public List<LabeledValue<EnumGokoUnit>> getLengthUnitChoice() {
		return lengthUnitChoice;
	}
	/**
	 * @param lengthUnitChoice the lengthUnitChoice to set
	 */
	public void setLengthUnitChoice(List<LabeledValue<EnumGokoUnit>> lengthUnitChoice) {
		firePropertyChange("lengthUnitChoice", this.lengthUnitChoice, this.lengthUnitChoice = lengthUnitChoice);
	}
	/**
	 * @return the lengthUnit
	 */
	public LabeledValue<EnumGokoUnit> getLengthUnit() {
		return lengthUnit;
	}
	/**
	 * @param lengthUnit the lengthUnit to set
	 */
	public void setLengthUnit(LabeledValue<EnumGokoUnit> lengthUnit) {
		firePropertyChange("lengthUnit", this.lengthUnit, this.lengthUnit = lengthUnit);
	}


}
