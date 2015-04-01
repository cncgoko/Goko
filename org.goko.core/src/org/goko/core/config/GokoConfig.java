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

package org.goko.core.config;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.US;
import org.goko.core.common.measure.dimension.Dimension;
import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.log.GkLog;

public class GokoConfig{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GokoConfig.class);
	/** ID of this configuration */
	private static final String ID = "goko.config";
	/** The instance of this config */
	private static GokoConfig instance;
	/** The preference store */
	private ScopedPreferenceStore preferences;
	/** The unit configured for distances */
	public static final String KEY_DISTANCE_UNIT = "distanceUnit";
	/** The precision to display after coma for distance values */
	public static final String KEY_DISTANCE_DIGIT_COUNT = "distanceDigitCount";
	/** Used units */
	private Map<Dimension, Unit> mapConfiguredUnits;

	protected GokoConfig() throws GkException {
		preferences = new ScopedPreferenceStore(ConfigurationScope.INSTANCE, ID);
		mapConfiguredUnits = new HashMap<Dimension, Unit>();
		initialize();
	}
	/**
	 * Singleton like access
	 * @return the instance of this configuration
	 */
	public static GokoConfig getInstance() {
		if(instance == null){
			try {
				instance = new GokoConfig();
			} catch (GkException e) {
				LOG.error(e);
			}
		}
		return instance;
	}

	private void initialize() throws GkException{
		getPreferences().setDefault(KEY_DISTANCE_UNIT, EnumGokoUnit.MILLIMETERS.getCode());
		if(StringUtils.isEmpty(getPreferences().getString(KEY_DISTANCE_UNIT))){
			getPreferences().setToDefault(KEY_DISTANCE_UNIT);
		}
		setDistanceUnit(EnumGokoUnit.getEnum(getPreferences().getString(KEY_DISTANCE_UNIT)));

		getPreferences().setDefault(KEY_DISTANCE_DIGIT_COUNT, "3");
		if(StringUtils.isEmpty(getPreferences().getString(KEY_DISTANCE_DIGIT_COUNT))){
			getPreferences().setToDefault(KEY_DISTANCE_DIGIT_COUNT);
		}
		setDigitCount(Integer.valueOf(getPreferences().getString(KEY_DISTANCE_DIGIT_COUNT)));
	}

	public EnumGokoUnit getEnumLengthUnit() throws GkException {
		String unit = getPreferences().getString(KEY_DISTANCE_UNIT);
		return EnumGokoUnit.getEnum(unit);
	}
	/**
	 * @return the distanceUnit
	 * @throws GkException
	 */
	public Unit<Length> getLengthUnit() throws GkException {
		String unit = getPreferences().getString(KEY_DISTANCE_UNIT);
		Unit<Length> foundUnit = SIPrefix.MILLI(SI.METRE);
		if(StringUtils.isNotBlank(unit)){
			EnumGokoUnit unitEnum = EnumGokoUnit.getEnum(unit);
			switch (unitEnum) {
			case INCHES: foundUnit = US.INCH;
				break;
			default: SIPrefix.MILLI(SI.METRE);
				break;
			}
		}
		return foundUnit;
	}
	/**
	 * @param distanceUnit the distanceUnit to set
	 */
	public void setDistanceUnit(EnumGokoUnit distanceUnit) {
		getPreferences().putValue(KEY_DISTANCE_UNIT, distanceUnit.getCode());
		mapConfiguredUnits.put(QuantityDimension.LENGTH, distanceUnit.getUnit());
	}
	/**
	 * @return the preferences
	 */
	public ScopedPreferenceStore getPreferences() {
		return preferences;
	}
	/**
	 * @return the digitCount
	 */
	public int getDigitCount() {
		return Integer.valueOf(getPreferences().getString(KEY_DISTANCE_DIGIT_COUNT));
	}
	/**
	 * @param digitCount the digitCount to set
	 */
	public void setDigitCount(int digitCount) {
		getPreferences().setValue(KEY_DISTANCE_DIGIT_COUNT, String.valueOf(digitCount));
	}


	public <Q extends Quantity<Q>> String format(BigDecimalQuantity<Q> quantity) throws GkException{
		Unit<Q> targetUnit = getConfiguredUnit(quantity);
		BigDecimal newValue = quantity.getValue().setScale(getDigitCount(), RoundingMode.HALF_DOWN);
		BigDecimalQuantity<Q> baseQuantity = NumberQuantity.of(newValue, targetUnit);
		Quantity<Q> convertedQuantity = baseQuantity.to(targetUnit);
		return format(convertedQuantity);
	}

	public <Q extends Quantity<Q>> String format(Quantity<Q> quantity) throws GkException{
		return format(quantity, false);
	}

	public <Q extends Quantity<Q>> String format(Quantity<Q> quantity, boolean keepTraillingZero) throws GkException{
		String result = StringUtils.EMPTY;
		Unit<Q> targetUnit = getConfiguredUnit(quantity);
		Quantity<Q> convertedQuantity = quantity.to(targetUnit);
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		if(keepTraillingZero){
			df.setMinimumFractionDigits(getDigitCount());
		}
		df.setMaximumFractionDigits(getDigitCount());
		result = df.format(convertedQuantity.doubleValue())+convertedQuantity.getUnit().getSymbol();
		return result;
	}

	protected <Q extends Quantity<Q>> Unit<Q> getConfiguredUnit(Quantity<Q> quantity) throws GkException{
		Unit genericUnit = mapConfiguredUnits.get(quantity.getUnit().getDimension());
		if(genericUnit != null){
			return genericUnit;
		}
		return quantity.getUnit();
	}
	public void save() throws GkException{
		try {
			getPreferences().save();
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}
}
