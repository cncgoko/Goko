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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.dimension.Dimension;
import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.log.GkLog;

public class GokoPreference extends GkPreference implements IPropertyChangeListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GokoPreference.class);
	/** ID of this configuration */
	public static final String NODE_ID = "org.goko.core";
	/** The instance of this config */
	private static GokoPreference instance;
	/** Key to set the clear persisted state option at runtime */
	public static final String KEY_SYSTEM_CLEAR_PERSISTED_STATE = "systemClearPersistedState";
	/** Default value of the clear persisted state option  */
	public static final boolean DEFAULT_SYSTEM_CLEAR_PERSISTED_STATE = false;
	/** The target board id */
	public static final String KEY_TARGET_BOARD = "targetBoard";
	/** Default target board */
	public static final String DEFAULT_TARGET_BOARD = StringUtils.EMPTY;
	/** The unit configured for distances */
	public static final String KEY_DISTANCE_UNIT = "distanceUnit";
	/** The default unit configured for distances */
	public static final String DEFAULT_DISTANCE_UNIT_CODE = EnumGokoUnit.MILLIMETERS.getCode();
	/** The default unit configured for distances */
	public static final EnumGokoUnit DEFAULT_DISTANCE_UNIT = EnumGokoUnit.MILLIMETERS;
	/** The precision to display after coma for distance values */
	public static final String KEY_DISTANCE_DIGIT_COUNT = "distanceDigitCount";
	/** Automatic update check*/
	public static final String KEY_CHECK_UPDATE = "checkAutomaticUpdate";
	/** Automatic update check (0 = Each start, 1=Once a day, 2=Once a week)*/
	public static final String KEY_CHECK_UPDATE_FREQUENCY = "checkAutomaticUpdateFrequency";
	/** Time of last update check */
	public static final String KEY_LAST_UPDATE_CHECK_TIMESTAMP = "lastUpdateCheckTimestamp";
	/** Default check for update */
	public static final boolean DEFAULT_CHECK_UPDATE = true;
	/** Default update check frequency */
	public static final String DEFAULT_CHECK_UPDATE_FREQUENCY  = EnumUpdateCheckFrequency.ONCE_A_DAY.getCode();
	/** The default precision to display after coma for distance values  */
	public static final int DEFAULT_DISTANCE_DIGIT_COUNT = 3;
		
	/** Used units */
	private Map<Dimension, Unit> mapConfiguredUnits;

	public GokoPreference() throws GkException {
		super(NODE_ID);
		mapConfiguredUnits = new HashMap<Dimension, Unit>();
		addPropertyChangeListener(this);
	}
		
	/**
	 * Singleton like access
	 * @return the instance of this configuration
	 */
	public static GokoPreference getInstance() {
		if(instance == null){
			try {
				instance = new GokoPreference();				
				//nstance.initialize();
			} catch (GkException e) {
				LOG.error(e);
			}
		}
		return instance;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.config.GkPreference#setValue(java.lang.String, boolean)
	 */
	@Override
	public void setValue(String name, boolean value) {
		// TODO Auto-generated method stub
		super.setValue(name, value);
	}
	/**
	 * @return the distanceUnit
	 * @throws GkException
	 */
	public Unit<Length> getLengthUnit() throws GkException {
		return mapConfiguredUnits.get(QuantityDimension.LENGTH);
	}
	
	/**
	 * @return the distanceUnit
	 * @throws GkException
	 */
	public Unit<Speed> getSpeedUnit() throws GkException {
		return mapConfiguredUnits.get(QuantityDimension.SPEED);
	}
	
	/**
	 * Returns the configured target board 
	 * @return the configured target board
	 */
	public String getTargetBoard(){
		return getString(KEY_TARGET_BOARD);
	}
	
	/**
	 * Sets the configured target board
	 * @param targetBoard the target board id to use 
	 */
	public void setTargetBoard(String targetBoard){
		putValue(KEY_TARGET_BOARD, targetBoard);
	}
	/**
	 * @param distanceUnit the distanceUnit to set
	 * @throws GkTechnicalException GkTechnicalException 
	 */
	public void setDistanceUnit(EnumGokoUnit distanceUnit) throws GkTechnicalException {
		getPreferenceStore().setValue(KEY_DISTANCE_UNIT, distanceUnit.getCode());
		mapConfiguredUnits.put(QuantityDimension.LENGTH, distanceUnit.getUnit());
		if(distanceUnit == EnumGokoUnit.MILLIMETERS){
			mapConfiguredUnits.put(QuantityDimension.SPEED, SpeedUnit.MILLIMETRE_PER_MINUTE);	
		}else if(distanceUnit == EnumGokoUnit.INCHES){
			mapConfiguredUnits.put(QuantityDimension.SPEED, SpeedUnit.INCH_PER_MINUTE);
		}else{
			throw new GkTechnicalException("Unsupported distance unit ["+distanceUnit.getCode()+"]");
		}
	}
	
	/**
	 * @return the digitCount
	 */
	public int getDigitCount() {
		return getPreferenceStore().getInt(KEY_DISTANCE_DIGIT_COUNT);
	}
	/**
	 * @param digitCount the digitCount to set
	 */
	public void setDigitCount(int digitCount) {
		getPreferenceStore().setValue(KEY_DISTANCE_DIGIT_COUNT, String.valueOf(digitCount));
	}

	public boolean isCheckForUpdate(){
		return getBoolean(KEY_CHECK_UPDATE);
	}

	public <Q extends Quantity<Q>> String format(Q quantity) throws GkException{
		return format(quantity, false);
	}

	public <Q extends Quantity<Q>> String format(Q quantity, boolean keepTraillingZero) throws GkException{
		return format(quantity, keepTraillingZero, true);
	}
	
	public <Q extends Quantity<Q>> String format(Q quantity, boolean keepTraillingZero, boolean displayUnit) throws GkException{
		return format(quantity, keepTraillingZero, displayUnit, null);
	}
	
	public <Q extends Quantity<Q>> String format(Q quantity, boolean keepTraillingZero, boolean displayUnit, Unit<Q> ptargetUnit) throws GkException{
		String result = StringUtils.EMPTY;
		Unit<Q> 	localTargetUnit 	= ptargetUnit;
		if(ptargetUnit == null){
			localTargetUnit = getConfiguredUnit(quantity);
		}		
		
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		if(keepTraillingZero){
			df.setMinimumFractionDigits(getDigitCount());
		}
		df.setMaximumFractionDigits(getDigitCount());
		result = df.format(quantity.doubleValue(localTargetUnit));
		if(displayUnit){
			result += quantity.getUnit().getSymbol();
		}
		return result;
	}

	protected <Q extends Quantity<Q>> Unit<Q> getConfiguredUnit(Quantity<Q> quantity) throws GkException{
		Unit genericUnit = mapConfiguredUnits.get(quantity.getUnit().getDimension());
		if(genericUnit != null){
			return genericUnit;
		}
		return quantity.getUnit();
	}

	public void setLastUpdateCheckTimestamp(Date date){
		setValue(KEY_LAST_UPDATE_CHECK_TIMESTAMP, date.getTime());
	}
	
	public Date getLastUpdateCheckTimestamp(){
		return new Date(getLong(KEY_LAST_UPDATE_CHECK_TIMESTAMP));
	}

	/**
	 * @return the updateCheckFrequency
	 * @throws GkException GkException 
	 */
	public EnumUpdateCheckFrequency getUpdateCheckFrequency() throws GkException {
		return EnumUpdateCheckFrequency.getValue(getString(KEY_CHECK_UPDATE_FREQUENCY));
	}

	/**
	 * @param updateCheckFrequency the updateCheckFrequency to set
	 */
	public void setUpdateCheckFrequency(EnumUpdateCheckFrequency updateCheckFrequency) {
		setValue(KEY_CHECK_UPDATE_FREQUENCY, updateCheckFrequency.getCode());
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		try {
			// Update the map of selected units
			String unit = getPreferenceStore().getString(KEY_DISTANCE_UNIT);
			mapConfiguredUnits.put(QuantityDimension.LENGTH, EnumGokoUnit.getEnum(unit).getUnit());
		} catch (GkTechnicalException e) {
			LOG.error(e);
		}		
	}
	
	/**
	 * @return the systemClearPersistedState
	 */
	public boolean isSystemClearPersistedState() {
		return getPreferenceStore().getBoolean(KEY_SYSTEM_CLEAR_PERSISTED_STATE);
	}

	/**
	 * @param systemClearPersistedState the systemClearPersistedState to set
	 */
	public void setSystemClearPersistedState(boolean systemClearPersistedState) {
		getPreferenceStore().setValue(KEY_SYSTEM_CLEAR_PERSISTED_STATE, systemClearPersistedState);
	}
}
