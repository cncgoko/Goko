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
package org.goko.controller.grbl.v08.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

	private GrblDoubleSetting stepsMmX;
	private GrblDoubleSetting stepsMmY;
	private GrblDoubleSetting stepsMmZ;
	private GrblIntegerSetting stepPulse;
	private GrblDoubleSetting defaultFeed;
	private GrblDoubleSetting defaultSeek;
	private GrblIntegerSetting stepPortInvertMask;
	private GrblIntegerSetting stepIdleDelay;
	private GrblDoubleSetting acceleration;
	private GrblDoubleSetting junctionDeviation;
	private GrblDoubleSetting arc;
	private GrblDoubleSetting arcCorrection;
	private GrblIntegerSetting decimalCount;
	private GrblBooleanSetting reportInches;
	private GrblBooleanSetting autoStart;
	private GrblBooleanSetting invertStepEnable;
	private GrblBooleanSetting hardLimits;
	private GrblBooleanSetting homingCycle;
	private GrblIntegerSetting homingInvertMask;
	private GrblDoubleSetting homingFeed;
	private GrblDoubleSetting homingSeek;
	private GrblDoubleSetting homingDebounce;
	private GrblDoubleSetting homingPullOff;

	public GrblConfiguration(){
		this.lstGrblSetting = new ArrayList<GrblSetting<?>>();
		initSettings();
	}

	private void initSettings() {
		registerSetting(stepsMmX = new GrblDoubleSetting("$0", 0.0));
		registerSetting(stepsMmY = new GrblDoubleSetting("$1", 0.0));
		registerSetting(stepsMmZ = new GrblDoubleSetting("$2", 0.0));
		registerSetting(stepPulse = new GrblIntegerSetting("$3", 0));
		registerSetting(defaultFeed = new GrblDoubleSetting("$4", 0.0));
		registerSetting(defaultSeek = new GrblDoubleSetting("$5", 0.0));
		registerSetting(stepPortInvertMask = new GrblIntegerSetting("$6", 0));
		registerSetting(stepIdleDelay = new GrblIntegerSetting("$7", 0));
		registerSetting(acceleration = new GrblDoubleSetting("$8", 0.0));
		registerSetting(junctionDeviation = new GrblDoubleSetting("$9", 0.0));
		registerSetting(arc = new GrblDoubleSetting("$10", 0.0));
		registerSetting(arcCorrection = new GrblDoubleSetting("$11", 0.0));
		registerSetting(decimalCount = new GrblIntegerSetting("$12", 0));
		registerSetting(reportInches = new GrblBooleanSetting("$13", false));
		registerSetting(autoStart = new GrblBooleanSetting("$14", false));
		registerSetting(invertStepEnable = new GrblBooleanSetting("$15", false));
		registerSetting(hardLimits = new GrblBooleanSetting("$16", false));
		registerSetting(homingCycle = new GrblBooleanSetting("$17", false));
		registerSetting(homingInvertMask = new GrblIntegerSetting("$18", 0));
		registerSetting(homingFeed = new GrblDoubleSetting("$19", 0.0));
		registerSetting(homingSeek = new GrblDoubleSetting("$20", 0.0));
		registerSetting(homingDebounce = new GrblDoubleSetting("$21", 0.0));
		registerSetting(homingPullOff = new GrblDoubleSetting("$22", 0.0));
	}


	public void setValue(String identifier, String value) {
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equals(grblSetting.getIdentifier(), identifier)){
				grblSetting.setValueFromString(value);
			}
		}
	}
	private void registerSetting(GrblSetting<?> setting){
		lstGrblSetting.add(setting);
	}

	/**
	 * @return the lstGrblSetting
	 */
	public List<GrblSetting<?>> getLstGrblSetting() {
		return lstGrblSetting;
	}

	/**
	 * @return the stepsMmX
	 */
	public GrblDoubleSetting getStepsMmX() {
		return stepsMmX;
	}

	/**
	 * @return the stepsMmY
	 */
	public GrblDoubleSetting getStepsMmY() {
		return stepsMmY;
	}

	/**
	 * @return the stepsMmZ
	 */
	public GrblDoubleSetting getStepsMmZ() {
		return stepsMmZ;
	}

	/**
	 * @return the stepPulse
	 */
	public GrblIntegerSetting getStepPulse() {
		return stepPulse;
	}

	/**
	 * @return the defaultFeed
	 */
	public GrblDoubleSetting getDefaultFeed() {
		return defaultFeed;
	}

	/**
	 * @return the defaultSeek
	 */
	public GrblDoubleSetting getDefaultSeek() {
		return defaultSeek;
	}

	/**
	 * @return the stepPortInvertMask
	 */
	public GrblIntegerSetting getStepPortInvertMask() {
		return stepPortInvertMask;
	}

	/**
	 * @return the stepIdleDelay
	 */
	public GrblIntegerSetting getStepIdleDelay() {
		return stepIdleDelay;
	}

	/**
	 * @return the acceleration
	 */
	public GrblDoubleSetting getAcceleration() {
		return acceleration;
	}

	/**
	 * @return the junctionDeviation
	 */
	public GrblDoubleSetting getJunctionDeviation() {
		return junctionDeviation;
	}

	/**
	 * @return the arc
	 */
	public GrblDoubleSetting getArc() {
		return arc;
	}

	/**
	 * @return the arcCorrection
	 */
	public GrblDoubleSetting getArcCorrection() {
		return arcCorrection;
	}

	/**
	 * @return the decimalCount
	 */
	public GrblIntegerSetting getDecimalCount() {
		return decimalCount;
	}

	/**
	 * @return the reportInches
	 */
	public GrblBooleanSetting getReportInches() {
		return reportInches;
	}

	public Unit<Length> getReportUnit(){
		Unit<Length> unit = LengthUnit.MILLIMETRE;
		if(getReportInches().getValue() == true){
			unit = LengthUnit.INCH;
		}
		return unit;
	}
	/**
	 * @return the autoStart
	 */
	public GrblBooleanSetting getAutoStart() {
		return autoStart;
	}

	/**
	 * @return the invertStepEnable
	 */
	public GrblBooleanSetting getInvertStepEnable() {
		return invertStepEnable;
	}

	/**
	 * @return the hardLimits
	 */
	public GrblBooleanSetting getHardLimits() {
		return hardLimits;
	}

	/**
	 * @return the homingCycle
	 */
	public GrblBooleanSetting getHomingCycle() {
		return homingCycle;
	}

	/**
	 * @return the homingInvertMask
	 */
	public GrblIntegerSetting getHomingInvertMask() {
		return homingInvertMask;
	}

	/**
	 * @return the homingFeed
	 */
	public GrblDoubleSetting getHomingFeed() {
		return homingFeed;
	}

	/**
	 * @return the homingSeek
	 */
	public GrblDoubleSetting getHomingSeek() {
		return homingSeek;
	}

	/**
	 * @return the homingDebounce
	 */
	public GrblDoubleSetting getHomingDebounce() {
		return homingDebounce;
	}

	/**
	 * @return the homingPullOff
	 */
	public GrblDoubleSetting getHomingPullOff() {
		return homingPullOff;
	}

}