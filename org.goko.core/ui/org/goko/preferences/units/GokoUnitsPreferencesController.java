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

import java.util.ArrayList;
import java.util.List;

import org.goko.common.GkUiUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.EnumGokoUnit;
import org.goko.core.config.GokoConfig;

/**
 * Controller class for Goko units preferences
 * @author PsyKo
 *
 */
public class GokoUnitsPreferencesController extends AbstractController<GokoUnitsPreferencesModel> {

	/** Constructor */
	public GokoUnitsPreferencesController() {
		super(new GokoUnitsPreferencesModel());
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		getDataModel().setDecimalCount( GokoConfig.getInstance().getDigitCount() );

		List<LabeledValue<EnumGokoUnit>> lengthUnitChoice = new ArrayList<LabeledValue<EnumGokoUnit>>();
		for (EnumGokoUnit gUnit : EnumGokoUnit.values()) {
			lengthUnitChoice.add(new LabeledValue<EnumGokoUnit>(gUnit, gUnit.getCode()));
		};
		getDataModel().setLengthUnitChoice(lengthUnitChoice);

		getDataModel().setLengthUnit( GkUiUtils.getLabelledValueByKey(GokoConfig.getInstance().getEnumLengthUnit(), lengthUnitChoice) );
	}

	public void savePreferences() throws GkException{
		GokoConfig.getInstance().setDigitCount( getDataModel().getDecimalCount() );
		GokoConfig.getInstance().setDistanceUnit( getDataModel().getLengthUnit().getValue() );
		GokoConfig.getInstance().save();
	}
}
