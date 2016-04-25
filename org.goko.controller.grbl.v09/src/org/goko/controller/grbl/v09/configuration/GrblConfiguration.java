/*
 *
 *   Goko
 *   Copyright (C) 2013, 2016  PsyKo
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
package org.goko.controller.grbl.v09.configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.units.Unit;


/**
 * Grbl configuration definition
 *
 * @author PsyKo
 *
 */
public class GrblConfiguration {
	private List<GrblSetting<?>> lstGrblSetting;

	private GrblIntegerSetting stepPulse;
	private GrblIntegerSetting stepIdleDelay;
	private GrblIntegerSetting stepPortInvertMask;
	private GrblIntegerSetting dirInvertMask;
	private GrblBooleanSetting stepEnableInvert;
	private GrblBooleanSetting limitPinInvert;
	private GrblBooleanSetting probePinInvert;
	private GrblIntegerSetting statusReportMask;
	private GrblBigDecimalSetting junctionDeviation;
	private GrblBigDecimalSetting arcTolerance;
	private GrblBooleanSetting reportInches;
	private GrblBooleanSetting softLimits;
	private GrblBooleanSetting hardLimits;
	private GrblBooleanSetting homingCycle;
	private GrblIntegerSetting homingDirInvertMask;
	private GrblBigDecimalSetting homingFeed;
	private GrblBigDecimalSetting homingSeek;
	private GrblBigDecimalSetting homingDebounce;
	private GrblBigDecimalSetting homingPullOff;
	private GrblBigDecimalSetting stepsMmX;
	private GrblBigDecimalSetting stepsMmY;
	private GrblBigDecimalSetting stepsMmZ;
	private GrblBigDecimalSetting maxRateX;
	private GrblBigDecimalSetting maxRateY;
	private GrblBigDecimalSetting maxRateZ;
	private GrblBigDecimalSetting accelX;
	private GrblBigDecimalSetting accelY;
	private GrblBigDecimalSetting accelZ;
	private GrblBigDecimalSetting maxTravelX;
	private GrblBigDecimalSetting maxTravelY;
	private GrblBigDecimalSetting maxTravelZ;


	public GrblConfiguration(){
		this.lstGrblSetting = new ArrayList<GrblSetting<?>>();
		initSettings();
	}

	private void initSettings() {
		registerSetting(stepPulse          = new GrblIntegerSetting("$0",0));
		registerSetting(stepIdleDelay      = new GrblIntegerSetting("$1",0));
		registerSetting(stepPortInvertMask = new GrblIntegerSetting("$2",0));
		registerSetting(dirInvertMask      = new GrblIntegerSetting("$3",0));
		registerSetting(stepEnableInvert   = new GrblBooleanSetting("$4",false));
		registerSetting(limitPinInvert     = new GrblBooleanSetting("$5",false));
		registerSetting(probePinInvert     = new GrblBooleanSetting("$6",false));
		registerSetting(statusReportMask   = new GrblIntegerSetting("$10",0));
		registerSetting(junctionDeviation  = new GrblBigDecimalSetting ("$11", BigDecimal.ZERO));
		registerSetting(arcTolerance       = new GrblBigDecimalSetting ("$12",BigDecimal.ZERO));
		registerSetting(reportInches       = new GrblBooleanSetting("$13",false));
		registerSetting(softLimits         = new GrblBooleanSetting("$20",false));
		registerSetting(hardLimits         = new GrblBooleanSetting("$21",false));
		registerSetting(homingCycle        = new GrblBooleanSetting("$22",false));
		registerSetting(homingDirInvertMask= new GrblIntegerSetting("$23",0));
		registerSetting(homingFeed         = new GrblBigDecimalSetting ("$24",BigDecimal.ZERO));
		registerSetting(homingSeek         = new GrblBigDecimalSetting ("$25",BigDecimal.ZERO));
		registerSetting(homingDebounce     = new GrblBigDecimalSetting ("$26",BigDecimal.ZERO));
		registerSetting(homingPullOff      = new GrblBigDecimalSetting ("$27",BigDecimal.ZERO));
		registerSetting(stepsMmX           = new GrblBigDecimalSetting ("$100",BigDecimal.ZERO));
		registerSetting(stepsMmY           = new GrblBigDecimalSetting ("$101",BigDecimal.ZERO));
		registerSetting(stepsMmZ           = new GrblBigDecimalSetting ("$102",BigDecimal.ZERO));
		registerSetting(maxRateX           = new GrblBigDecimalSetting ("$110",BigDecimal.ZERO));
		registerSetting(maxRateY           = new GrblBigDecimalSetting ("$111",BigDecimal.ZERO));
		registerSetting(maxRateZ           = new GrblBigDecimalSetting ("$112",BigDecimal.ZERO));
		registerSetting(accelX             = new GrblBigDecimalSetting ("$120",BigDecimal.ZERO));
		registerSetting(accelY             = new GrblBigDecimalSetting ("$121",BigDecimal.ZERO));
		registerSetting(accelZ             = new GrblBigDecimalSetting ("$122",BigDecimal.ZERO));
		registerSetting(maxTravelX         = new GrblBigDecimalSetting ("$130",BigDecimal.ZERO));
		registerSetting(maxTravelY         = new GrblBigDecimalSetting ("$131",BigDecimal.ZERO));
		registerSetting(maxTravelZ         = new GrblBigDecimalSetting ("$132",BigDecimal.ZERO));
	}


	public void setValue(String identifier, String value) {
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equals(grblSetting.getIdentifier(), identifier)){
				grblSetting.setValueFromString(value);
			}
		}
	}
	
	/**
	 * Sets the setting value
	 * @param group the TinyGGroupSettings
	 * @param identifier the identifier
	 * @param value the value to set
	 * @throws GkException GkException
	 */
	@SuppressWarnings("unchecked")
	public <T> void setSetting(String identifier, T value) throws GkException{
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equalsIgnoreCase( grblSetting.getIdentifier(), identifier ) ){
				if(value != null && grblSetting.getType() != value.getClass()){
					throw new GkTechnicalException("Setting '"+identifier+"' type mismatch. Expecting "+grblSetting.getType()+"', got'"+value.getClass()+"'. ");
				}
				((GrblSetting<T>)grblSetting).setValue(value);
				return;
			}
		}
	}
	
	/**
	 * Returns the setting as the specified type or null if not found
	 * @param identifier the identifier
	 * @param clazz the expected type
	 * @return the value as clazz
	 * @throws GkException GkException
	 */
	public <T> T findSetting(String identifier, Class<T> clazz) throws GkException{
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equalsIgnoreCase( grblSetting.getIdentifier(), identifier ) ){
				if(grblSetting.getType() != clazz){
					throw new GkTechnicalException("Cannot retrieve setting '"+identifier+"' type. Requesting "+clazz+"', got'"+grblSetting.getType()+"'. ");
				}
				return (T) grblSetting.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Returns the setting as the specified type
	 * @param identifier the identifier
	 * @param clazz the expected type
	 * @return the value as clazz
	 * @throws GkException GkException
	 */
	public <T> T getSetting(String identifier, Class<T> clazz) throws GkException{
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equalsIgnoreCase( grblSetting.getIdentifier(), identifier ) ){
				if(grblSetting.getType() != clazz){
					throw new GkTechnicalException("Cannot retrieve setting '"+identifier+"' type. Requesting "+clazz+"', got'"+grblSetting.getType()+"'. ");
				}
				return (T) grblSetting.getValue();
			}
		}
		throw new GkFunctionalException("Setting '"+identifier+"' is unknown");
	}
	
	private void registerSetting(GrblSetting<?> setting){
		lstGrblSetting.add(setting);
	}

	public Unit<Length> getReportUnit(){
		Unit<Length> unit = LengthUnit.MILLIMETRE;
		if(getReportInches().getValue() == true){
			unit = LengthUnit.INCH;
		}
		return unit;
	}

	/**
	 * @return the lstGrblSetting
	 */
	public List<GrblSetting<?>> getLstGrblSetting() {
		return lstGrblSetting;
	}

	/**
	 * @param lstGrblSetting the lstGrblSetting to set
	 */
	public void setLstGrblSetting(List<GrblSetting<?>> lstGrblSetting) {
		this.lstGrblSetting = lstGrblSetting;
	}

	/**
	 * @return the stepPulse
	 */
	public GrblIntegerSetting getStepPulse() {
		return stepPulse;
	}

	/**
	 * @param stepPulse the stepPulse to set
	 */
	public void setStepPulse(GrblIntegerSetting stepPulse) {
		this.stepPulse = stepPulse;
	}

	/**
	 * @return the stepIdleDelay
	 */
	public GrblIntegerSetting getStepIdleDelay() {
		return stepIdleDelay;
	}

	/**
	 * @param stepIdleDelay the stepIdleDelay to set
	 */
	public void setStepIdleDelay(GrblIntegerSetting stepIdleDelay) {
		this.stepIdleDelay = stepIdleDelay;
	}

	/**
	 * @return the stepPortInvertMask
	 */
	public GrblIntegerSetting getStepPortInvertMask() {
		return stepPortInvertMask;
	}

	/**
	 * @param stepPortInvertMask the stepPortInvertMask to set
	 */
	public void setStepPortInvertMask(GrblIntegerSetting stepPortInvertMask) {
		this.stepPortInvertMask = stepPortInvertMask;
	}

	/**
	 * @return the dirInvertMask
	 */
	public GrblIntegerSetting getDirInvertMask() {
		return dirInvertMask;
	}

	/**
	 * @param dirInvertMask the dirInvertMask to set
	 */
	public void setDirInvertMask(GrblIntegerSetting dirInvertMask) {
		this.dirInvertMask = dirInvertMask;
	}

	/**
	 * @return the stepEnableInvert
	 */
	public GrblBooleanSetting getStepEnableInvert() {
		return stepEnableInvert;
	}

	/**
	 * @param stepEnableInvert the stepEnableInvert to set
	 */
	public void setStepEnableInvert(GrblBooleanSetting stepEnableInvert) {
		this.stepEnableInvert = stepEnableInvert;
	}

	/**
	 * @return the limitPinInvert
	 */
	public GrblBooleanSetting getLimitPinInvert() {
		return limitPinInvert;
	}

	/**
	 * @param limitPinInvert the limitPinInvert to set
	 */
	public void setLimitPinInvert(GrblBooleanSetting limitPinInvert) {
		this.limitPinInvert = limitPinInvert;
	}

	/**
	 * @return the probePinInvert
	 */
	public GrblBooleanSetting getProbePinInvert() {
		return probePinInvert;
	}

	/**
	 * @param probePinInvert the probePinInvert to set
	 */
	public void setProbePinInvert(GrblBooleanSetting probePinInvert) {
		this.probePinInvert = probePinInvert;
	}

	/**
	 * @return the statusReportMask
	 */
	public GrblIntegerSetting getStatusReportMask() {
		return statusReportMask;
	}

	/**
	 * @param statusReportMask the statusReportMask to set
	 */
	public void setStatusReportMask(GrblIntegerSetting statusReportMask) {
		this.statusReportMask = statusReportMask;
	}

	/**
	 * @return the junctionDeviation
	 */
	public GrblBigDecimalSetting getJunctionDeviation() {
		return junctionDeviation;
	}

	/**
	 * @param junctionDeviation the junctionDeviation to set
	 */
	public void setJunctionDeviation(GrblBigDecimalSetting junctionDeviation) {
		this.junctionDeviation = junctionDeviation;
	}

	/**
	 * @return the arcTolerance
	 */
	public GrblBigDecimalSetting getArcTolerance() {
		return arcTolerance;
	}

	/**
	 * @param arcTolerance the arcTolerance to set
	 */
	public void setArcTolerance(GrblBigDecimalSetting arcTolerance) {
		this.arcTolerance = arcTolerance;
	}

	/**
	 * @return the reportInches
	 */
	public GrblBooleanSetting getReportInches() {
		return reportInches;
	}

	/**
	 * @param reportInches the reportInches to set
	 */
	public void setReportInches(GrblBooleanSetting reportInches) {
		this.reportInches = reportInches;
	}

	/**
	 * @return the softLimits
	 */
	public GrblBooleanSetting getSoftLimits() {
		return softLimits;
	}

	/**
	 * @param softLimits the softLimits to set
	 */
	public void setSoftLimits(GrblBooleanSetting softLimits) {
		this.softLimits = softLimits;
	}

	/**
	 * @return the hardLimits
	 */
	public GrblBooleanSetting getHardLimits() {
		return hardLimits;
	}

	/**
	 * @param hardLimits the hardLimits to set
	 */
	public void setHardLimits(GrblBooleanSetting hardLimits) {
		this.hardLimits = hardLimits;
	}

	/**
	 * @return the homingCycle
	 */
	public GrblBooleanSetting getHomingCycle() {
		return homingCycle;
	}

	/**
	 * @param homingCycle the homingCycle to set
	 */
	public void setHomingCycle(GrblBooleanSetting homingCycle) {
		this.homingCycle = homingCycle;
	}

	/**
	 * @return the homingDirInvertMask
	 */
	public GrblIntegerSetting getHomingDirInvertMask() {
		return homingDirInvertMask;
	}

	/**
	 * @param homingDirInvertMask the homingDirInvertMask to set
	 */
	public void setHomingDirInvertMask(GrblIntegerSetting homingDirInvertMask) {
		this.homingDirInvertMask = homingDirInvertMask;
	}

	/**
	 * @return the homingFeed
	 */
	public GrblBigDecimalSetting getHomingFeed() {
		return homingFeed;
	}

	/**
	 * @param homingFeed the homingFeed to set
	 */
	public void setHomingFeed(GrblBigDecimalSetting homingFeed) {
		this.homingFeed = homingFeed;
	}

	/**
	 * @return the homingSeek
	 */
	public GrblBigDecimalSetting getHomingSeek() {
		return homingSeek;
	}

	/**
	 * @param homingSeek the homingSeek to set
	 */
	public void setHomingSeek(GrblBigDecimalSetting homingSeek) {
		this.homingSeek = homingSeek;
	}

	/**
	 * @return the homingDebounce
	 */
	public GrblBigDecimalSetting getHomingDebounce() {
		return homingDebounce;
	}

	/**
	 * @param homingDebounce the homingDebounce to set
	 */
	public void setHomingDebounce(GrblBigDecimalSetting homingDebounce) {
		this.homingDebounce = homingDebounce;
	}

	/**
	 * @return the homingPullOff
	 */
	public GrblBigDecimalSetting getHomingPullOff() {
		return homingPullOff;
	}

	/**
	 * @param homingPullOff the homingPullOff to set
	 */
	public void setHomingPullOff(GrblBigDecimalSetting homingPullOff) {
		this.homingPullOff = homingPullOff;
	}

	/**
	 * @return the stepsMmX
	 */
	public GrblBigDecimalSetting getStepsMmX() {
		return stepsMmX;
	}

	/**
	 * @param stepsMmX the stepsMmX to set
	 */
	public void setStepsMmX(GrblBigDecimalSetting stepsMmX) {
		this.stepsMmX = stepsMmX;
	}

	/**
	 * @return the stepsMmY
	 */
	public GrblBigDecimalSetting getStepsMmY() {
		return stepsMmY;
	}

	/**
	 * @param stepsMmY the stepsMmY to set
	 */
	public void setStepsMmY(GrblBigDecimalSetting stepsMmY) {
		this.stepsMmY = stepsMmY;
	}

	/**
	 * @return the stepsMmZ
	 */
	public GrblBigDecimalSetting getStepsMmZ() {
		return stepsMmZ;
	}

	/**
	 * @param stepsMmZ the stepsMmZ to set
	 */
	public void setStepsMmZ(GrblBigDecimalSetting stepsMmZ) {
		this.stepsMmZ = stepsMmZ;
	}

	/**
	 * @return the maxRateX
	 */
	public GrblBigDecimalSetting getMaxRateX() {
		return maxRateX;
	}

	/**
	 * @param maxRateX the maxRateX to set
	 */
	public void setMaxRateX(GrblBigDecimalSetting maxRateX) {
		this.maxRateX = maxRateX;
	}

	/**
	 * @return the maxRateY
	 */
	public GrblBigDecimalSetting getMaxRateY() {
		return maxRateY;
	}

	/**
	 * @param maxRateY the maxRateY to set
	 */
	public void setMaxRateY(GrblBigDecimalSetting maxRateY) {
		this.maxRateY = maxRateY;
	}

	/**
	 * @return the maxRateZ
	 */
	public GrblBigDecimalSetting getMaxRateZ() {
		return maxRateZ;
	}

	/**
	 * @param maxRateZ the maxRateZ to set
	 */
	public void setMaxRateZ(GrblBigDecimalSetting maxRateZ) {
		this.maxRateZ = maxRateZ;
	}

	/**
	 * @return the accelX
	 */
	public GrblBigDecimalSetting getAccelX() {
		return accelX;
	}

	/**
	 * @param accelX the accelX to set
	 */
	public void setAccelX(GrblBigDecimalSetting accelX) {
		this.accelX = accelX;
	}

	/**
	 * @return the accelY
	 */
	public GrblBigDecimalSetting getAccelY() {
		return accelY;
	}

	/**
	 * @param accelY the accelY to set
	 */
	public void setAccelY(GrblBigDecimalSetting accelY) {
		this.accelY = accelY;
	}

	/**
	 * @return the accelZ
	 */
	public GrblBigDecimalSetting getAccelZ() {
		return accelZ;
	}

	/**
	 * @param accelZ the accelZ to set
	 */
	public void setAccelZ(GrblBigDecimalSetting accelZ) {
		this.accelZ = accelZ;
	}

	/**
	 * @return the maxTravelX
	 */
	public GrblBigDecimalSetting getMaxTravelX() {
		return maxTravelX;
	}

	/**
	 * @param maxTravelX the maxTravelX to set
	 */
	public void setMaxTravelX(GrblBigDecimalSetting maxTravelX) {
		this.maxTravelX = maxTravelX;
	}

	/**
	 * @return the maxTravelY
	 */
	public GrblBigDecimalSetting getMaxTravelY() {
		return maxTravelY;
	}

	/**
	 * @param maxTravelY the maxTravelY to set
	 */
	public void setMaxTravelY(GrblBigDecimalSetting maxTravelY) {
		this.maxTravelY = maxTravelY;
	}

	/**
	 * @return the maxTravelZ
	 */
	public GrblBigDecimalSetting getMaxTravelZ() {
		return maxTravelZ;
	}

	/**
	 * @param maxTravelZ the maxTravelZ to set
	 */
	public void setMaxTravelZ(GrblBigDecimalSetting maxTravelZ) {
		this.maxTravelZ = maxTravelZ;
	}

}