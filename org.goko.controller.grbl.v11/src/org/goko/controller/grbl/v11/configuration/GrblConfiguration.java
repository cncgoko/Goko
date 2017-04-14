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
package org.goko.controller.grbl.v11.configuration;

import java.math.BigDecimal;

import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;
import org.goko.controller.grbl.commons.configuration.settings.GrblBigDecimalSetting;
import org.goko.controller.grbl.commons.configuration.settings.GrblBooleanSetting;
import org.goko.controller.grbl.commons.configuration.settings.GrblIntegerSetting;
import org.goko.controller.grbl.v11.Grbl;


/**
 * Grbl 1.1 configuration 
 * @author Psyko
 * @date 5 avr. 2017
 */
public class GrblConfiguration extends AbstractGrblConfiguration<GrblConfiguration>{
	private GrblIntegerSetting stepPulse;
	private GrblIntegerSetting stepIdleDelay;
	private GrblIntegerSetting stepPortInvertMask;
	private GrblIntegerSetting directionInvertMask;
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
	private GrblIntegerSetting maxSpindleSpeed;
	private GrblIntegerSetting minSpindleSpeed;
	private GrblBooleanSetting laserMode;
	private GrblBigDecimalSetting stepsPerMmX;
	private GrblBigDecimalSetting stepsPerMmY;
	private GrblBigDecimalSetting stepsPerMmZ;
	private GrblBigDecimalSetting maxRateX;
	private GrblBigDecimalSetting maxRateY;
	private GrblBigDecimalSetting maxRateZ;
	private GrblBigDecimalSetting accelerationX;
	private GrblBigDecimalSetting accelerationY;
	private GrblBigDecimalSetting accelerationZ;
	private GrblBigDecimalSetting maxTravelX;
	private GrblBigDecimalSetting maxTravelY;
	private GrblBigDecimalSetting maxTravelZ;
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration#initSettings()
	 */
	@Override
	protected void initSettings() {
		registerSetting(stepPulse          = new GrblIntegerSetting(Grbl.Configuration.STEP_PULSE,0));
		registerSetting(stepIdleDelay      = new GrblIntegerSetting(Grbl.Configuration.STEP_IDLE_DELAY,0));
		registerSetting(stepPortInvertMask = new GrblIntegerSetting(Grbl.Configuration.STEP_PORT_INVERT,0));
		registerSetting(directionInvertMask= new GrblIntegerSetting(Grbl.Configuration.DIRECTION_PORT_INVERT,0));
		registerSetting(stepEnableInvert   = new GrblBooleanSetting(Grbl.Configuration.STEP_ENABLE_INVERT,false));
		registerSetting(limitPinInvert     = new GrblBooleanSetting(Grbl.Configuration.LIMIT_PIN_INVERT,false));
		registerSetting(probePinInvert     = new GrblBooleanSetting(Grbl.Configuration.PROBE_PIN_INVERT,false));
		registerSetting(statusReportMask   = new GrblIntegerSetting(Grbl.Configuration.STATUS_REPORT,0));
		registerSetting(junctionDeviation  = new GrblBigDecimalSetting (Grbl.Configuration.JUNCTION_DEVIATION, BigDecimal.ZERO));
		registerSetting(arcTolerance       = new GrblBigDecimalSetting (Grbl.Configuration.ARC_TOLERANCE,BigDecimal.ZERO));
		registerSetting(reportInches       = new GrblBooleanSetting(Grbl.Configuration.REPORT_INCHES,false));
		registerSetting(softLimits         = new GrblBooleanSetting(Grbl.Configuration.SOFT_LIMITS_ENABLED,false));
		registerSetting(hardLimits         = new GrblBooleanSetting(Grbl.Configuration.HARD_LIMITS_ENABLED,false));
		registerSetting(homingCycle        = new GrblBooleanSetting(Grbl.Configuration.HOMING_CYCLE_ENABLED,false));
		registerSetting(homingDirInvertMask= new GrblIntegerSetting(Grbl.Configuration.HOMING_DIRECTION_INVERT,0));
		registerSetting(homingFeed         = new GrblBigDecimalSetting (Grbl.Configuration.HOMING_FEED,BigDecimal.ZERO));
		registerSetting(homingSeek         = new GrblBigDecimalSetting (Grbl.Configuration.HOMING_SEEK,BigDecimal.ZERO));
		registerSetting(homingDebounce     = new GrblBigDecimalSetting (Grbl.Configuration.HOMING_DEBOUNCE,BigDecimal.ZERO));
		registerSetting(homingPullOff      = new GrblBigDecimalSetting (Grbl.Configuration.HOMING_PULL_OFF,BigDecimal.ZERO));
		registerSetting(maxSpindleSpeed    = new GrblIntegerSetting (Grbl.Configuration.MAX_SPINDLE_SPEED,0));
		registerSetting(minSpindleSpeed    = new GrblIntegerSetting (Grbl.Configuration.MIN_SPINDLE_SPEED,0));
		registerSetting(laserMode		   = new GrblBooleanSetting (Grbl.Configuration.LASER_MODE,false));
		registerSetting(stepsPerMmX        = new GrblBigDecimalSetting (Grbl.Configuration.STEPS_PER_MM_X,BigDecimal.ZERO));
		registerSetting(stepsPerMmY        = new GrblBigDecimalSetting (Grbl.Configuration.STEPS_PER_MM_Y,BigDecimal.ZERO));
		registerSetting(stepsPerMmZ        = new GrblBigDecimalSetting (Grbl.Configuration.STEPS_PER_MM_Z,BigDecimal.ZERO));
		registerSetting(maxRateX           = new GrblBigDecimalSetting (Grbl.Configuration.MAX_RATE_X,BigDecimal.ZERO));
		registerSetting(maxRateY           = new GrblBigDecimalSetting (Grbl.Configuration.MAX_RATE_Y,BigDecimal.ZERO));
		registerSetting(maxRateZ           = new GrblBigDecimalSetting (Grbl.Configuration.MAX_RATE_Z,BigDecimal.ZERO));
		registerSetting(accelerationX      = new GrblBigDecimalSetting (Grbl.Configuration.ACCELERATION_X,BigDecimal.ZERO));
		registerSetting(accelerationY      = new GrblBigDecimalSetting (Grbl.Configuration.ACCELERATION_Y,BigDecimal.ZERO));
		registerSetting(accelerationZ      = new GrblBigDecimalSetting (Grbl.Configuration.ACCELERATION_Z,BigDecimal.ZERO));
		registerSetting(maxTravelX         = new GrblBigDecimalSetting (Grbl.Configuration.MAX_TRAVEL_X,BigDecimal.ZERO));
		registerSetting(maxTravelY         = new GrblBigDecimalSetting (Grbl.Configuration.MAX_TRAVEL_Y,BigDecimal.ZERO));
		registerSetting(maxTravelZ         = new GrblBigDecimalSetting (Grbl.Configuration.MAX_TRAVEL_Z,BigDecimal.ZERO));
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration#getCopy()
	 */
	@Override
	public GrblConfiguration getCopy() {
		GrblConfiguration copy = new GrblConfiguration();
		copy.copyFrom(this);
		return copy;
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
	 * @return the directionInvertMask
	 */
	public GrblIntegerSetting getDirectionInvertMask() {
		return directionInvertMask;
	}

	/**
	 * @param directionInvertMask the directionInvertMask to set
	 */
	public void setDirectionInvertMask(GrblIntegerSetting directionInvertMask) {
		this.directionInvertMask = directionInvertMask;
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
	 * @return the stepsPerMmX
	 */
	public GrblBigDecimalSetting getStepsPerMmX() {
		return stepsPerMmX;
	}

	/**
	 * @param stepsPerMmX the stepsPerMmX to set
	 */
	public void setStepsPerMmX(GrblBigDecimalSetting stepsPerMmX) {
		this.stepsPerMmX = stepsPerMmX;
	}

	/**
	 * @return the stepsPerMmY
	 */
	public GrblBigDecimalSetting getStepsPerMmY() {
		return stepsPerMmY;
	}

	/**
	 * @param stepsPerMmY the stepsPerMmY to set
	 */
	public void setStepsPerMmY(GrblBigDecimalSetting stepsPerMmY) {
		this.stepsPerMmY = stepsPerMmY;
	}

	/**
	 * @return the stepsPerMmZ
	 */
	public GrblBigDecimalSetting getStepsPerMmZ() {
		return stepsPerMmZ;
	}

	/**
	 * @param stepsPerMmZ the stepsPerMmZ to set
	 */
	public void setStepsPerMmZ(GrblBigDecimalSetting stepsPerMmZ) {
		this.stepsPerMmZ = stepsPerMmZ;
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
	 * @return the accelerationX
	 */
	public GrblBigDecimalSetting getAccelerationX() {
		return accelerationX;
	}

	/**
	 * @param accelerationX the accelerationX to set
	 */
	public void setAccelerationX(GrblBigDecimalSetting accelerationX) {
		this.accelerationX = accelerationX;
	}

	/**
	 * @return the accelerationY
	 */
	public GrblBigDecimalSetting getAccelerationY() {
		return accelerationY;
	}

	/**
	 * @param accelerationY the accelerationY to set
	 */
	public void setAccelerationY(GrblBigDecimalSetting accelerationY) {
		this.accelerationY = accelerationY;
	}

	/**
	 * @return the accelerationZ
	 */
	public GrblBigDecimalSetting getAccelerationZ() {
		return accelerationZ;
	}

	/**
	 * @param accelerationZ the accelerationZ to set
	 */
	public void setAccelerationZ(GrblBigDecimalSetting accelerationZ) {
		this.accelerationZ = accelerationZ;
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

	/**
	 * @return the maxSpindleSpeed
	 */
	public GrblIntegerSetting getMaxSpindleSpeed() {
		return maxSpindleSpeed;
	}

	/**
	 * @param maxSpindleSpeed the maxSpindleSpeed to set
	 */
	public void setMaxSpindleSpeed(GrblIntegerSetting maxSpindleSpeed) {
		this.maxSpindleSpeed = maxSpindleSpeed;
	}

	/**
	 * @return the minSpindleSpeed
	 */
	public GrblIntegerSetting getMinSpindleSpeed() {
		return minSpindleSpeed;
	}

	/**
	 * @param minSpindleSpeed the minSpindleSpeed to set
	 */
	public void setMinSpindleSpeed(GrblIntegerSetting minSpindleSpeed) {
		this.minSpindleSpeed = minSpindleSpeed;
	}

	/**
	 * @return the laserMode
	 */
	public GrblBooleanSetting getLaserMode() {
		return laserMode;
	}

	/**
	 * @param laserMode the laserMode to set
	 */
	public void setLaserMode(GrblBooleanSetting laserMode) {
		this.laserMode = laserMode;
	}
	
}