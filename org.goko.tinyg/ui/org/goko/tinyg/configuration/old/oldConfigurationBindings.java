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
package org.goko.tinyg.configuration.old;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.databinding.BindingStatus;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;

public class oldConfigurationBindings extends AbstractModelObject{
	// Identification setting
	private BigDecimal hardwareVersion;
	private BigDecimal firmwareBuild;
	private BigDecimal firmwareVersion;
	private String uniqueId;

	// System settings
	private BigDecimal junctionAcceleration;
	private BigDecimal chordalTolerance;
	private BigDecimal motorDisableTimeout;

	// Communication settings
	private String enableJson;
	private LabeledValue<BigDecimal> jsonVerbosity;
	private LabeledValue<BigDecimal> textVerbosity;
	private LabeledValue<BigDecimal> queueReportVerbosity;
	private LabeledValue<BigDecimal> statusReportVerbosity;
	private LabeledValue<BigDecimal> switchType;
	private BigDecimal statusReportInterval;
	private LabeledValue<BigDecimal> baudrate;

	private boolean enableXonXoff;
	private boolean enableCrOnTx;
	private boolean ignoreCrOnRx;
	private boolean ignoreLfOnRx;
	private boolean enableEcho;
	//GCode default
	private LabeledValue<BigDecimal> defaultPlaneSelection;
	private LabeledValue<BigDecimal> defaultUnitsMode;
	private LabeledValue<BigDecimal> defaultCoordinateSystem;
	private LabeledValue<BigDecimal> defaultPathControl;
	private LabeledValue<BigDecimal> defaultDistanceMode;
	private BigDecimal maxJerk;
	private BigDecimal maxTravel;
	private BigDecimal maxVelocity;
	private BigDecimal statusinterval;
	private BigDecimal stepAngleM1;
	private BigDecimal travelPerRevM1;
	private BigDecimal stepAngleM2;
	private BigDecimal travelPerRevM2;
	private BigDecimal stepAngleM3;
	private BigDecimal travelPerRevM3;
	private BigDecimal stepAngleM4;
	private BigDecimal travelPerRevM4;

	// Current axis params
	private BigDecimal velocityMaximum;
	private BigDecimal maximumFeedrate;
	private BigDecimal travelMaximum;
	private BigDecimal jerkMaximum;
	private BigDecimal jerkHoming;
	private BigDecimal junctionDeviation;
	private BigDecimal radiusValue;
	private BigDecimal searchVelocity;
	private BigDecimal latchVelocity;
	private BigDecimal zeroBackoff;

	private LabeledValue<String> selectedAxis;
	private LabeledValue<BigDecimal> maxSwitchMode;
	private LabeledValue<BigDecimal> minSwitchMode;

	private List<LabeledValue<BigDecimal>> choicesMotorMapping;
	private List<LabeledValue<BigDecimal>> choicesMicrosteps;
	private List<LabeledValue<BigDecimal>> choicesPowerManagement;
	private List<LabeledValue<BigDecimal>> choicesPolarity;
	private List<LabeledValue<BigDecimal>> choicesJSonVerbosity;
	private List<LabeledValue<BigDecimal>> choicesExtendedVerbosity;
	private List<LabeledValue<BigDecimal>> choicesVerbosity;
	private List<LabeledValue<String>> choicesConfigurableAxes;
	private List<LabeledValue<BigDecimal>> choicesSwitchModes;
	private List<LabeledValue<BigDecimal>> choicesPlaneSelection;
	private List<LabeledValue<BigDecimal>> choicesUnits;
	private List<LabeledValue<BigDecimal>> choicesCoordinateSystem;
	private List<LabeledValue<BigDecimal>> choicesPathControl;
	private List<LabeledValue<BigDecimal>> choicesDistanceMode;
	private List<LabeledValue<BigDecimal>> choicesBaudrate;
	private List<LabeledValue<BigDecimal>> choicesSwitchType;

	// Motor params
	private LabeledValue<BigDecimal>  axisForMotor1;
	private LabeledValue<BigDecimal>  axisForMotor2;
	private LabeledValue<BigDecimal>  axisForMotor3;
	private LabeledValue<BigDecimal>  axisForMotor4;

	private BigDecimal motor1StepAngle;
	private BigDecimal motor1TravelPerRevolution;
	private LabeledValue<BigDecimal> motor1Microsteps;
	private LabeledValue<BigDecimal> motor1Polarity;
	private LabeledValue<BigDecimal> motor1PowerManagement;

	private BigDecimal motor2StepAngle;
	private BigDecimal motor2TravelPerRevolution;
	private LabeledValue<BigDecimal> motor2Microsteps;
	private LabeledValue<BigDecimal> motor2Polarity;
	private LabeledValue<BigDecimal> motor2PowerManagement;

	private BigDecimal motor3StepAngle;
	private BigDecimal motor3TravelPerRevolution;
	private LabeledValue<BigDecimal> motor3Microsteps;
	private LabeledValue<BigDecimal> motor3Polarity;
	private LabeledValue<BigDecimal> motor3PowerManagement;

	private BigDecimal motor4StepAngle;
	private BigDecimal motor4TravelPerRevolution;
	private LabeledValue<BigDecimal> motor4Microsteps;
	private LabeledValue<BigDecimal> motor4Polarity;
	private LabeledValue<BigDecimal> motor4PowerManagement;

	public oldConfigurationBindings() {
		this.choicesMotorMapping = new ArrayList<LabeledValue<BigDecimal>>();
		choicesMotorMapping.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "X axis"));
		choicesMotorMapping.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Y axis"));
		choicesMotorMapping.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "Z axis"));
		choicesMotorMapping.add(new LabeledValue<BigDecimal>(new BigDecimal("3"), "A axis"));
		choicesMotorMapping.add(new LabeledValue<BigDecimal>(new BigDecimal("4"), "B axis"));
		choicesMotorMapping.add(new LabeledValue<BigDecimal>(new BigDecimal("5"), "C axis"));

		this.choicesMicrosteps = new ArrayList<LabeledValue<BigDecimal>>();
		choicesMicrosteps.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "1"));
		choicesMicrosteps.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "2"));
		choicesMicrosteps.add(new LabeledValue<BigDecimal>(new BigDecimal("4"), "4"));
		choicesMicrosteps.add(new LabeledValue<BigDecimal>(new BigDecimal("8"), "8"));

		this.choicesPolarity = new ArrayList<LabeledValue<BigDecimal>>();
		choicesPolarity.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Clockwise"));
		choicesPolarity.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Counterclockwise"));


		this.choicesPowerManagement = new ArrayList<LabeledValue<BigDecimal>>();
		choicesPowerManagement.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Powered when idle"));
		choicesPowerManagement.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Unpowered when idle"));


		this.choicesConfigurableAxes = new ArrayList<LabeledValue<String>>();
		choicesConfigurableAxes.add(new LabeledValue<String>("x", "X axis"));
		choicesConfigurableAxes.add(new LabeledValue<String>("y", "Y axis"));
		choicesConfigurableAxes.add(new LabeledValue<String>("z", "Z axis"));
		choicesConfigurableAxes.add(new LabeledValue<String>("a", "A axis"));
		choicesConfigurableAxes.add(new LabeledValue<String>("b", "B axis"));
		choicesConfigurableAxes.add(new LabeledValue<String>("c", "C axis"));


		this.choicesSwitchModes = new ArrayList<LabeledValue<BigDecimal>>();
		choicesSwitchModes.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Disabled"));
		choicesSwitchModes.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Homing only"));
		choicesSwitchModes.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "Limit only"));
		choicesSwitchModes.add(new LabeledValue<BigDecimal>(new BigDecimal("3"), "Homing and limit"));

		selectedAxis = new LabeledValue<String>("x", "X axis");

		choicesPlaneSelection 	= new ArrayList<LabeledValue<BigDecimal>>();
		choicesPlaneSelection.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "XY Plane - G17"));
		choicesPlaneSelection.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "XZ Plane - G18"));
		choicesPlaneSelection.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "YZ Plane - G19"));

		choicesUnits			= new ArrayList<LabeledValue<BigDecimal>>();
		choicesUnits.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Inches - G20"));
		choicesUnits.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Millimeters - G21"));

		choicesCoordinateSystem= new ArrayList<LabeledValue<BigDecimal>>();
		choicesCoordinateSystem.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Coordinate system 1 - G54"));
		choicesCoordinateSystem.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "Coordinate system 2 - G55"));
		choicesCoordinateSystem.add(new LabeledValue<BigDecimal>(new BigDecimal("3"), "Coordinate system 3 - G56"));
		choicesCoordinateSystem.add(new LabeledValue<BigDecimal>(new BigDecimal("4"), "Coordinate system 4 - G57"));
		choicesCoordinateSystem.add(new LabeledValue<BigDecimal>(new BigDecimal("5"), "Coordinate system 5 - G58"));
		choicesCoordinateSystem.add(new LabeledValue<BigDecimal>(new BigDecimal("6"), "Coordinate system 6 - G59"));

		choicesPathControl		= new ArrayList<LabeledValue<BigDecimal>>();
		choicesPathControl.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Exact path - G61"));
		choicesPathControl.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Exact stop - G61.1"));
		choicesPathControl.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "Continuous - G64"));

		choicesDistanceMode	= new ArrayList<LabeledValue<BigDecimal>>();
		choicesDistanceMode.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Absolute - G60"));
		choicesDistanceMode.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Incremental - G91"));


		choicesJSonVerbosity= new ArrayList<LabeledValue<BigDecimal>>();
		choicesJSonVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Silent"));
		choicesJSonVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Footer"));
		choicesJSonVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "Messages"));
		choicesJSonVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("3"), "Configs"));
		choicesJSonVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("4"), "Lineum"));
		choicesJSonVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("5"), "Verbose"));


		choicesExtendedVerbosity= new ArrayList<LabeledValue<BigDecimal>>();
		choicesExtendedVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Silent"));
		choicesExtendedVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Filtered"));
		choicesExtendedVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "Verbose"));

		choicesVerbosity= new ArrayList<LabeledValue<BigDecimal>>();
		choicesVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Silent"));
		choicesVerbosity.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Verbose"));

		choicesBaudrate= new ArrayList<LabeledValue<BigDecimal>>();
		choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "9600"));
		choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("2"), "19200"));
		choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("3"), "38400"));
		choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("4"), "57600"));
		choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("5"), "115200"));
		choicesBaudrate.add(new LabeledValue<BigDecimal>(new BigDecimal("6"), "230400"));

		choicesSwitchType = new ArrayList<LabeledValue<BigDecimal>>();
		choicesSwitchType.add(new LabeledValue<BigDecimal>(new BigDecimal("0"), "Normally open"));
		choicesSwitchType.add(new LabeledValue<BigDecimal>(new BigDecimal("1"), "Normally closed"));

	}

	/**
	 * @return the hardwareVersion
	 */
	public BigDecimal getHardwareVersion() {
		return hardwareVersion;
	}

	/**
	 * @param hardwareVersion the hardwareVersion to set
	 */
	public void setHardwareVersion(BigDecimal hardwareVersion) {
		firePropertyChange("hardwareVersion", this.hardwareVersion,
				this.hardwareVersion = hardwareVersion);
	}

	/**
	 * @return the firmwareBuild
	 */
	public BigDecimal getFirmwareBuild() {
		return firmwareBuild;
	}

	/**
	 * @param firmwareBuild the firmwareBuild to set
	 */
	public void setFirmwareBuild(BigDecimal firmwareBuild) {
		firePropertyChange("firmwareBuild", this.firmwareBuild,
				this.firmwareBuild = firmwareBuild);
	}

	/**
	 * @return the firmwareVersion
	 */
	public BigDecimal getFirmwareVersion() {
		return firmwareVersion;
	}

	/**
	 * @param firmwareVersion the firmwareVersion to set
	 */
	public void setFirmwareVersion(BigDecimal firmwareVersion) {
		firePropertyChange("firmwareVersion", this.firmwareVersion,
				this.firmwareVersion = firmwareVersion);
	}

	/**
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		firePropertyChange("uniqueId", this.uniqueId, this.uniqueId = uniqueId);
	}

	/**
	 * @return the junctionAcceleration
	 */
	public BigDecimal getJunctionAcceleration() {
		return junctionAcceleration;
	}

	/**
	 * @param junctionAcceleration the junctionAcceleration to set
	 */
	public void setJunctionAcceleration(BigDecimal junctionAcceleration) {
		firePropertyChange("junctionAcceleration", this.junctionAcceleration,
				this.junctionAcceleration = junctionAcceleration);
	}

	/**
	 * @return the chordalTolerance
	 */
	public BigDecimal getChordalTolerance() {
		return chordalTolerance;
	}

	/**
	 * @param chordalTolerance the chordalTolerance to set
	 */
	public void setChordalTolerance(BigDecimal chordalTolerance) {
		firePropertyChange("chordalTolerance", this.chordalTolerance,
				this.chordalTolerance = chordalTolerance);
	}

	/**
	 * @return the motorDisableTimeout
	 */
	public BigDecimal getMotorDisableTimeout() {
		return motorDisableTimeout;
	}

	/**
	 * @param motorDisableTimeout the motorDisableTimeout to set
	 */
	public void setMotorDisableTimeout(BigDecimal motorDisableTimeout) {
		firePropertyChange("motorDisableTimeout", this.motorDisableTimeout,
				this.motorDisableTimeout = motorDisableTimeout);
	}

	/**
	 * @return the enableJson
	 */
	public String getEnableJson() {
		return enableJson;
	}

	/**
	 * @param enableJson the enableJson to set
	 */
	public void setEnableJson(String enableJson) {
		firePropertyChange("enableJson", this.enableJson,
				this.enableJson = enableJson);
	}

	/**
	 * @return the statusReportInterval
	 */
	public BigDecimal getStatusReportInterval() {
		return statusReportInterval;
	}

	/**
	 * @param statusReportInterval the statusReportInterval to set
	 */
	public void setStatusReportInterval(BigDecimal statusReportInterval) {
		firePropertyChange("statusReportInterval", this.statusReportInterval,
				this.statusReportInterval = statusReportInterval);
	}

	/**
	 * @return the maxJerk
	 */
	public BigDecimal getMaxJerk() {
		return maxJerk;
	}

	/**
	 * @param maxJerk the maxJerk to set
	 */
	public void setMaxJerk(BigDecimal maxJerk) {
		firePropertyChange("maxJerk", this.maxJerk, this.maxJerk = maxJerk);
	}

	/**
	 * @return the maxTravel
	 */
	public BigDecimal getMaxTravel() {
		return maxTravel;
	}

	/**
	 * @param maxTravel the maxTravel to set
	 */
	public void setMaxTravel(BigDecimal maxTravel) {
		firePropertyChange("maxTravel", this.maxTravel,
				this.maxTravel = maxTravel);
	}

	/**
	 * @return the maxVelocity
	 */
	public BigDecimal getMaxVelocity() {
		return maxVelocity;
	}

	/**
	 * @param maxVelocity the maxVelocity to set
	 */
	public void setMaxVelocity(BigDecimal maxVelocity) {
		firePropertyChange("maxVelocity", this.maxVelocity,
				this.maxVelocity = maxVelocity);
	}

	/**
	 * @return the statusinterval
	 */
	public BigDecimal getStatusinterval() {
		return statusinterval;
	}

	/**
	 * @param statusinterval the statusinterval to set
	 */
	public void setStatusinterval(BigDecimal statusinterval) {
		firePropertyChange("statusinterval", this.statusinterval,
				this.statusinterval = statusinterval);
	}

	/**
	 * @return the stepAngleM1
	 */
	public BigDecimal getStepAngleM1() {
		return stepAngleM1;
	}

	/**
	 * @param stepAngleM1 the stepAngleM1 to set
	 */
	public void setStepAngleM1(BigDecimal stepAngleM1) {
		firePropertyChange("stepAngleM1", this.stepAngleM1,
				this.stepAngleM1 = stepAngleM1);
	}

	/**
	 * @return the travelPerRevM1
	 */
	public BigDecimal getTravelPerRevM1() {
		return travelPerRevM1;
	}

	/**
	 * @param travelPerRevM1 the travelPerRevM1 to set
	 */
	public void setTravelPerRevM1(BigDecimal travelPerRevM1) {
		firePropertyChange("travelPerRevM1", this.travelPerRevM1,
				this.travelPerRevM1 = travelPerRevM1);
	}

	/**
	 * @return the stepAngleM2
	 */
	public BigDecimal getStepAngleM2() {
		return stepAngleM2;
	}

	/**
	 * @param stepAngleM2 the stepAngleM2 to set
	 */
	public void setStepAngleM2(BigDecimal stepAngleM2) {
		firePropertyChange("stepAngleM2", this.stepAngleM2,
				this.stepAngleM2 = stepAngleM2);
	}

	/**
	 * @return the travelPerRevM2
	 */
	public BigDecimal getTravelPerRevM2() {
		return travelPerRevM2;
	}

	/**
	 * @param travelPerRevM2 the travelPerRevM2 to set
	 */
	public void setTravelPerRevM2(BigDecimal travelPerRevM2) {
		firePropertyChange("travelPerRevM2", this.travelPerRevM2,
				this.travelPerRevM2 = travelPerRevM2);
	}

	/**
	 * @return the stepAngleM3
	 */
	public BigDecimal getStepAngleM3() {
		return stepAngleM3;
	}

	/**
	 * @param stepAngleM3 the stepAngleM3 to set
	 */
	public void setStepAngleM3(BigDecimal stepAngleM3) {
		firePropertyChange("stepAngleM3", this.stepAngleM3,
				this.stepAngleM3 = stepAngleM3);
	}

	/**
	 * @return the travelPerRevM3
	 */
	public BigDecimal getTravelPerRevM3() {
		return travelPerRevM3;
	}

	/**
	 * @param travelPerRevM3 the travelPerRevM3 to set
	 */
	public void setTravelPerRevM3(BigDecimal travelPerRevM3) {
		firePropertyChange("travelPerRevM3", this.travelPerRevM3,
				this.travelPerRevM3 = travelPerRevM3);
	}

	/**
	 * @return the stepAngleM4
	 */
	public BigDecimal getStepAngleM4() {
		return stepAngleM4;
	}

	/**
	 * @param stepAngleM4 the stepAngleM4 to set
	 */
	public void setStepAngleM4(BigDecimal stepAngleM4) {
		firePropertyChange("stepAngleM4", this.stepAngleM4,
				this.stepAngleM4 = stepAngleM4);
	}

	/**
	 * @return the travelPerRevM4
	 */
	public BigDecimal getTravelPerRevM4() {
		return travelPerRevM4;
	}

	/**
	 * @param travelPerRevM4 the travelPerRevM4 to set
	 */
	public void setTravelPerRevM4(BigDecimal travelPerRevM4) {
		firePropertyChange("travelPerRevM4", this.travelPerRevM4,
				this.travelPerRevM4 = travelPerRevM4);
	}

	/**
	 * @return the choicesConfigurableAxes
	 */
	public List<LabeledValue<String>> getChoicesConfigurableAxes() {
		return choicesConfigurableAxes;
	}

	/**
	 * @param choicesConfigurableAxes the choicesConfigurableAxes to set
	 */
	public void setChoicesConfigurableAxes(
			List<LabeledValue<String>> choicesConfigurableAxes) {
		firePropertyChange("choicesConfigurableAxes",
				this.choicesConfigurableAxes,
				this.choicesConfigurableAxes = choicesConfigurableAxes);
	}

	/**
	 * @return the choicesSwitchModes
	 */
	public List<LabeledValue<BigDecimal>> getChoicesSwitchModes() {
		return choicesSwitchModes;
	}

	/**
	 * @param choicesSwitchModes the choicesSwitchModes to set
	 */
	public void setChoicesSwitchModes(List<LabeledValue<BigDecimal>> choicesSwitchModes) {
		firePropertyChange("choicesSwitchModes", this.choicesSwitchModes,
				this.choicesSwitchModes = choicesSwitchModes);
	}

	/**
	 * @return the choicesPlaneSelection
	 */
	public List<LabeledValue<BigDecimal>> getChoicesPlaneSelection() {
		return choicesPlaneSelection;
	}

	/**
	 * @param choicesPlaneSelection the choicesPlaneSelection to set
	 */
	public void setChoicesPlaneSelection(List<LabeledValue<BigDecimal>> choicesPlaneSelection) {
		firePropertyChange("choicesPlaneSelection",
				this.choicesPlaneSelection,
				this.choicesPlaneSelection = choicesPlaneSelection);
	}

	/**
	 * @return the choicesUnits
	 */
	public List<LabeledValue<BigDecimal>> getChoicesUnits() {
		return choicesUnits;
	}

	/**
	 * @param choicesUnits the choicesUnits to set
	 */
	public void setChoicesUnits(List<LabeledValue<BigDecimal>> choicesUnits) {
		firePropertyChange("choicesUnits", this.choicesUnits,
				this.choicesUnits = choicesUnits);
	}

	/**
	 * @return the choicesCoordinateSystem
	 */
	public List<LabeledValue<BigDecimal>> getChoicesCoordinateSystem() {
		return choicesCoordinateSystem;
	}

	/**
	 * @param choicesCoordinateSystem the choicesCoordinateSystem to set
	 */
	public void setChoicesCoordinateSystem(
			List<LabeledValue<BigDecimal>> choicesCoordinateSystem) {
		firePropertyChange("choicesCoordinateSystem",
				this.choicesCoordinateSystem,
				this.choicesCoordinateSystem = choicesCoordinateSystem);
	}

	/**
	 * @return the choicesPathControl
	 */
	public List<LabeledValue<BigDecimal>> getChoicesPathControl() {
		return choicesPathControl;
	}

	/**
	 * @param choicesPathControl the choicesPathControl to set
	 */
	public void setChoicesPathControl(List<LabeledValue<BigDecimal>> choicesPathControl) {
		firePropertyChange("choicesPathControl", this.choicesPathControl,
				this.choicesPathControl = choicesPathControl);
	}

	/**
	 * @return the choicesDistanceMode
	 */
	public List<LabeledValue<BigDecimal>> getChoicesDistanceMode() {
		return choicesDistanceMode;
	}

	/**
	 * @param choicesDistanceMode the choicesDistanceMode to set
	 */
	public void setChoicesDistanceMode(List<LabeledValue<BigDecimal>> choicesDistanceMode) {
		firePropertyChange("choicesDistanceMode", this.choicesDistanceMode,
				this.choicesDistanceMode = choicesDistanceMode);
	}

	/**
	 * @return the velocityMaximum
	 */
	public BigDecimal getVelocityMaximum() {
		return velocityMaximum;
	}

	/**
	 * @param velocityMaximum the velocityMaximum to set
	 */
	public void setVelocityMaximum(BigDecimal velocityMaximum) {
		firePropertyChange("velocityMaximum", this.velocityMaximum,
				this.velocityMaximum = velocityMaximum);
	}

	/**
	 * @return the maximumFeedrate
	 */
	public BigDecimal getMaximumFeedrate() {
		return maximumFeedrate;
	}

	/**
	 * @param maximumFeedrate the maximumFeedrate to set
	 */
	public void setMaximumFeedrate(BigDecimal maximumFeedrate) {
		firePropertyChange("maximumFeedrate", this.maximumFeedrate,
				this.maximumFeedrate = maximumFeedrate);
	}

	/**
	 * @return the travelMaximum
	 */
	public BigDecimal getTravelMaximum() {
		return travelMaximum;
	}

	/**
	 * @param travelMaximum the travelMaximum to set
	 */
	public void setTravelMaximum(BigDecimal travelMaximum) {
		firePropertyChange("travelMaximum", this.travelMaximum,
				this.travelMaximum = travelMaximum);
	}

	/**
	 * @return the jerkMaximum
	 */
	public BigDecimal getJerkMaximum() {
		return jerkMaximum;
	}

	/**
	 * @param jerkMaximum the jerkMaximum to set
	 */
	public void setJerkMaximum(BigDecimal jerkMaximum) {
		firePropertyChange("jerkMaximum", this.jerkMaximum,
				this.jerkMaximum = jerkMaximum);
	}

	/**
	 * @return the jerkHoming
	 */
	public BigDecimal getJerkHoming() {
		return jerkHoming;
	}

	/**
	 * @param jerkHoming the jerkHoming to set
	 */
	public void setJerkHoming(BigDecimal jerkHoming) {
		firePropertyChange("jerkHoming", this.jerkHoming,
				this.jerkHoming = jerkHoming);
	}

	/**
	 * @return the junctionDeviation
	 */
	public BigDecimal getJunctionDeviation() {
		return junctionDeviation;
	}

	/**
	 * @param junctionDeviation the junctionDeviation to set
	 */
	public void setJunctionDeviation(BigDecimal junctionDeviation) {
		firePropertyChange("junctionDeviation", this.junctionDeviation,
				this.junctionDeviation = junctionDeviation);
	}

	/**
	 * @return the radiusValue
	 */
	public BigDecimal getRadiusValue() {
		return radiusValue;
	}

	/**
	 * @param radiusValue the radiusValue to set
	 */
	public void setRadiusValue(BigDecimal radiusValue) {
		firePropertyChange("radiusValue", this.radiusValue,
				this.radiusValue = radiusValue);
	}

	/**
	 * @return the searchVelocity
	 */
	public BigDecimal getSearchVelocity() {
		return searchVelocity;
	}

	/**
	 * @param searchVelocity the searchVelocity to set
	 */
	public void setSearchVelocity(BigDecimal searchVelocity) {
		firePropertyChange("searchVelocity", this.searchVelocity,
				this.searchVelocity = searchVelocity);
	}

	/**
	 * @return the latchVelocity
	 */
	public BigDecimal getLatchVelocity() {
		return latchVelocity;
	}

	/**
	 * @param latchVelocity the latchVelocity to set
	 */
	public void setLatchVelocity(BigDecimal latchVelocity) {
		firePropertyChange("latchVelocity", this.latchVelocity,
				this.latchVelocity = latchVelocity);
	}

	/**
	 * @return the zeroBackoff
	 */
	public BigDecimal getZeroBackoff() {
		return zeroBackoff;
	}

	/**
	 * @param zeroBackoff the zeroBackoff to set
	 */
	public void setZeroBackoff(BigDecimal zeroBackoff) {
		firePropertyChange("zeroBackoff", this.zeroBackoff,
				this.zeroBackoff = zeroBackoff);
	}

	/**
	 * @return the selectedAxis
	 */
	public LabeledValue<String> getSelectedAxis() {
		return selectedAxis;
	}

	/**
	 * @param selectedAxis the selectedAxis to set
	 */
	public void setSelectedAxis(LabeledValue<String> selectedAxis) {
		firePropertyChange("selectedAxis", this.selectedAxis,
				this.selectedAxis = selectedAxis);
	}

	/**
	 * @return the maxSwitchMode
	 */
	public LabeledValue<BigDecimal> getMaxSwitchMode() {
		return maxSwitchMode;
	}

	/**
	 * @param maxSwitchMode the maxSwitchMode to set
	 */
	public void setMaxSwitchMode(LabeledValue<BigDecimal> maxSwitchMode) {
		firePropertyChange("maxSwitchMode", this.maxSwitchMode,
				this.maxSwitchMode = maxSwitchMode);
	}

	/**
	 * @return the minSwitchMode
	 */
	public LabeledValue<BigDecimal> getMinSwitchMode() {
		return minSwitchMode;
	}

	/**
	 * @param minSwitchMode the minSwitchMode to set
	 */
	public void setMinSwitchMode(LabeledValue<BigDecimal> minSwitchMode) {
		firePropertyChange("minSwitchMode", this.minSwitchMode,
				this.minSwitchMode = minSwitchMode);
	}

	/**
	 * @return the choicesMotorMapping
	 */
	public List<LabeledValue<BigDecimal>> getChoicesMotorMapping() {
		return choicesMotorMapping;
	}

	/**
	 * @param choicesMotorMapping the choicesMotorMapping to set
	 */
	public void setChoicesMotorMapping(List<LabeledValue<BigDecimal>> choicesMotorMapping) {
		firePropertyChange("choicesMotorMapping", this.choicesMotorMapping,
				this.choicesMotorMapping = choicesMotorMapping);
	}

	/**
	 * @return the choicesMicrosteps
	 */
	public List<LabeledValue<BigDecimal>> getChoicesMicrosteps() {
		return choicesMicrosteps;
	}

	/**
	 * @param choicesMicrosteps the choicesMicrosteps to set
	 */
	public void setChoicesMicrosteps(List<LabeledValue<BigDecimal>> choicesMicrosteps) {
		firePropertyChange("choicesMicrosteps", this.choicesMicrosteps,
				this.choicesMicrosteps = choicesMicrosteps);
	}

	/**
	 * @return the choicesPowerManagement
	 */
	public List<LabeledValue<BigDecimal>> getChoicesPowerManagement() {
		return choicesPowerManagement;
	}

	/**
	 * @param choicesPowerManagement the choicesPowerManagement to set
	 */
	public void setChoicesPowerManagement(List<LabeledValue<BigDecimal>> choicesPowerManagement) {
		firePropertyChange("choicesPowerManagement",
				this.choicesPowerManagement,
				this.choicesPowerManagement = choicesPowerManagement);
	}

	/**
	 * @return the choicesPolarity
	 */
	public List<LabeledValue<BigDecimal>> getChoicesPolarity() {
		return choicesPolarity;
	}

	/**
	 * @param choicesPolarity the choicesPolarity to set
	 */
	public void setChoicesPolarity(List<LabeledValue<BigDecimal>> choicesPolarity) {
		firePropertyChange("choicesPolarity", this.choicesPolarity,
				this.choicesPolarity = choicesPolarity);
	}

	/**
	 * @return the axisForMotor1
	 */
	public LabeledValue<BigDecimal> getAxisForMotor1() {
		return axisForMotor1;
	}

	/**
	 * @param axisForMotor1 the axisForMotor1 to set
	 */
	public void setAxisForMotor1(LabeledValue<BigDecimal> axisForMotor1) {
		firePropertyChange("axisForMotor1", this.axisForMotor1,
				this.axisForMotor1 = axisForMotor1);
	}

	/**
	 * @return the axisForMotor2
	 */
	public LabeledValue<BigDecimal> getAxisForMotor2() {
		return axisForMotor2;
	}

	/**
	 * @param axisForMotor2 the axisForMotor2 to set
	 */
	public void setAxisForMotor2(LabeledValue<BigDecimal> axisForMotor2) {
		firePropertyChange("axisForMotor2", this.axisForMotor2,
				this.axisForMotor2 = axisForMotor2);
	}

	/**
	 * @return the axisForMotor3
	 */
	public LabeledValue<BigDecimal> getAxisForMotor3() {
		return axisForMotor3;
	}

	/**
	 * @param axisForMotor3 the axisForMotor3 to set
	 */
	public void setAxisForMotor3(LabeledValue<BigDecimal> axisForMotor3) {
		firePropertyChange("axisForMotor3", this.axisForMotor3,
				this.axisForMotor3 = axisForMotor3);
	}

	/**
	 * @return the axisForMotor4
	 */
	public LabeledValue<BigDecimal> getAxisForMotor4() {
		return axisForMotor4;
	}

	/**
	 * @param axisForMotor4 the axisForMotor4 to set
	 */
	public void setAxisForMotor4(LabeledValue<BigDecimal> axisForMotor4) {
		firePropertyChange("axisForMotor4", this.axisForMotor4,
				this.axisForMotor4 = axisForMotor4);
	}

	/**
	 * @return the motor1StepAngle
	 */
	public BigDecimal getMotor1StepAngle() {
		return motor1StepAngle;
	}

	/**
	 * @param motor1StepAngle the motor1StepAngle to set
	 */
	public void setMotor1StepAngle(BigDecimal motor1StepAngle) {
		firePropertyChange("motor1StepAngle", this.motor1StepAngle,
				this.motor1StepAngle = motor1StepAngle);
	}

	/**
	 * @return the motor1TravelPerRevolution
	 */
	public BigDecimal getMotor1TravelPerRevolution() {
		return motor1TravelPerRevolution;
	}

	/**
	 * @param motor1TravelPerRevolution the motor1TravelPerRevolution to set
	 */
	public void setMotor1TravelPerRevolution(BigDecimal motor1TravelPerRevolution) {
		firePropertyChange("motor1TravelPerRevolution",
				this.motor1TravelPerRevolution,
				this.motor1TravelPerRevolution = motor1TravelPerRevolution);
	}

	/**
	 * @return the motor1Microsteps
	 */
	public LabeledValue<BigDecimal> getMotor1Microsteps() {
		return motor1Microsteps;
	}

	/**
	 * @param motor1Microsteps the motor1Microsteps to set
	 */
	public void setMotor1Microsteps(LabeledValue<BigDecimal> motor1Microsteps) {
		firePropertyChange("motor1Microsteps", this.motor1Microsteps,
				this.motor1Microsteps = motor1Microsteps);
	}

	/**
	 * @return the motor1Polarity
	 */
	public LabeledValue<BigDecimal> getMotor1Polarity() {
		return motor1Polarity;
	}

	/**
	 * @param motor1Polarity the motor1Polarity to set
	 */
	public void setMotor1Polarity(LabeledValue<BigDecimal> motor1Polarity) {
		firePropertyChange("motor1Polarity", this.motor1Polarity,
				this.motor1Polarity = motor1Polarity);
	}

	/**
	 * @return the motor1PowerManagement
	 */
	public LabeledValue<BigDecimal> getMotor1PowerManagement() {
		return motor1PowerManagement;
	}

	/**
	 * @param motor1PowerManagement the motor1PowerManagement to set
	 */
	public void setMotor1PowerManagement(LabeledValue<BigDecimal> motor1PowerManagement) {
		firePropertyChange("motor1PowerManagement",
				this.motor1PowerManagement,
				this.motor1PowerManagement = motor1PowerManagement);
	}

	/**
	 * @return the motor2StepAngle
	 */
	public BigDecimal getMotor2StepAngle() {
		return motor2StepAngle;
	}

	/**
	 * @param motor2StepAngle the motor2StepAngle to set
	 */
	public void setMotor2StepAngle(BigDecimal motor2StepAngle) {
		firePropertyChange("motor2StepAngle", this.motor2StepAngle,
				this.motor2StepAngle = motor2StepAngle);
	}

	/**
	 * @return the motor2TravelPerRevolution
	 */
	public BigDecimal getMotor2TravelPerRevolution() {
		return motor2TravelPerRevolution;
	}

	/**
	 * @param motor2TravelPerRevolution the motor2TravelPerRevolution to set
	 */
	public void setMotor2TravelPerRevolution(BigDecimal motor2TravelPerRevolution) {
		firePropertyChange("motor2TravelPerRevolution",
				this.motor2TravelPerRevolution,
				this.motor2TravelPerRevolution = motor2TravelPerRevolution);
	}

	/**
	 * @return the motor2Microsteps
	 */
	public LabeledValue<BigDecimal> getMotor2Microsteps() {
		return motor2Microsteps;
	}

	/**
	 * @param motor2Microsteps the motor2Microsteps to set
	 */
	public void setMotor2Microsteps(LabeledValue<BigDecimal> motor2Microsteps) {
		firePropertyChange("motor2Microsteps", this.motor2Microsteps,
				this.motor2Microsteps = motor2Microsteps);
	}

	/**
	 * @return the motor2Polarity
	 */
	public LabeledValue<BigDecimal> getMotor2Polarity() {
		return motor2Polarity;
	}

	/**
	 * @param motor2Polarity the motor2Polarity to set
	 */
	public void setMotor2Polarity(LabeledValue<BigDecimal> motor2Polarity) {
		firePropertyChange("motor2Polarity", this.motor2Polarity,
				this.motor2Polarity = motor2Polarity);
	}

	/**
	 * @return the motor2PowerManagement
	 */
	public LabeledValue<BigDecimal> getMotor2PowerManagement() {
		return motor2PowerManagement;
	}

	/**
	 * @param motor2PowerManagement the motor2PowerManagement to set
	 */
	public void setMotor2PowerManagement(LabeledValue<BigDecimal> motor2PowerManagement) {
		firePropertyChange("motor2PowerManagement",
				this.motor2PowerManagement,
				this.motor2PowerManagement = motor2PowerManagement);
	}

	/**
	 * @return the motor3StepAngle
	 */
	public BigDecimal getMotor3StepAngle() {
		return motor3StepAngle;
	}

	/**
	 * @param motor3StepAngle the motor3StepAngle to set
	 */
	public void setMotor3StepAngle(BigDecimal motor3StepAngle) {
		firePropertyChange("motor3StepAngle", this.motor3StepAngle,
				this.motor3StepAngle = motor3StepAngle);
	}

	/**
	 * @return the motor3TravelPerRevolution
	 */
	public BigDecimal getMotor3TravelPerRevolution() {
		return motor3TravelPerRevolution;
	}

	/**
	 * @param motor3TravelPerRevolution the motor3TravelPerRevolution to set
	 */
	public void setMotor3TravelPerRevolution(BigDecimal motor3TravelPerRevolution) {
		firePropertyChange("motor3TravelPerRevolution",
				this.motor3TravelPerRevolution,
				this.motor3TravelPerRevolution = motor3TravelPerRevolution);
	}

	/**
	 * @return the motor3Microsteps
	 */
	public LabeledValue<BigDecimal> getMotor3Microsteps() {
		return motor3Microsteps;
	}

	/**
	 * @param motor3Microsteps the motor3Microsteps to set
	 */
	public void setMotor3Microsteps(LabeledValue<BigDecimal> motor3Microsteps) {
		firePropertyChange("motor3Microsteps", this.motor3Microsteps,
				this.motor3Microsteps = motor3Microsteps);
	}

	/**
	 * @return the motor3Polarity
	 */
	public LabeledValue<BigDecimal> getMotor3Polarity() {
		return motor3Polarity;
	}

	/**
	 * @param motor3Polarity the motor3Polarity to set
	 */
	public void setMotor3Polarity(LabeledValue<BigDecimal> motor3Polarity) {
		firePropertyChange("motor3Polarity", this.motor3Polarity,
				this.motor3Polarity = motor3Polarity);
	}

	/**
	 * @return the motor3PowerManagement
	 */
	public LabeledValue<BigDecimal> getMotor3PowerManagement() {
		return motor3PowerManagement;
	}

	/**
	 * @param motor3PowerManagement the motor3PowerManagement to set
	 */
	public void setMotor3PowerManagement(LabeledValue<BigDecimal> motor3PowerManagement) {
		firePropertyChange("motor3PowerManagement",
				this.motor3PowerManagement,
				this.motor3PowerManagement = motor3PowerManagement);
	}

	/**
	 * @return the motor4StepAngle
	 */
	public BigDecimal getMotor4StepAngle() {
		return motor4StepAngle;
	}

	/**
	 * @param motor4StepAngle the motor4StepAngle to set
	 */
	public void setMotor4StepAngle(BigDecimal motor4StepAngle) {
		firePropertyChange("motor4StepAngle", this.motor4StepAngle,
				this.motor4StepAngle = motor4StepAngle);
	}

	/**
	 * @return the motor4TravelPerRevolution
	 */
	public BigDecimal getMotor4TravelPerRevolution() {
		return motor4TravelPerRevolution;
	}

	/**
	 * @param motor4TravelPerRevolution the motor4TravelPerRevolution to set
	 */
	public void setMotor4TravelPerRevolution(BigDecimal motor4TravelPerRevolution) {
		firePropertyChange("motor4TravelPerRevolution",
				this.motor4TravelPerRevolution,
				this.motor4TravelPerRevolution = motor4TravelPerRevolution);
	}

	/**
	 * @return the motor4Microsteps
	 */
	public LabeledValue<BigDecimal> getMotor4Microsteps() {
		return motor4Microsteps;
	}

	/**
	 * @param motor4Microsteps the motor4Microsteps to set
	 */
	public void setMotor4Microsteps(LabeledValue<BigDecimal> motor4Microsteps) {
		firePropertyChange("motor4Microsteps", this.motor4Microsteps,
				this.motor4Microsteps = motor4Microsteps);
	}

	/**
	 * @return the motor4Polarity
	 */
	public LabeledValue<BigDecimal> getMotor4Polarity() {
		return motor4Polarity;
	}

	/**
	 * @param motor4Polarity the motor4Polarity to set
	 */
	public void setMotor4Polarity(LabeledValue<BigDecimal> motor4Polarity) {
		firePropertyChange("motor4Polarity", this.motor4Polarity,
				this.motor4Polarity = motor4Polarity);
	}

	/**
	 * @return the motor4PowerManagement
	 */
	public LabeledValue<BigDecimal> getMotor4PowerManagement() {
		return motor4PowerManagement;
	}

	/**
	 * @param motor4PowerManagement the motor4PowerManagement to set
	 */
	public void setMotor4PowerManagement(LabeledValue<BigDecimal> motor4PowerManagement) {
		firePropertyChange("motor4PowerManagement",
				this.motor4PowerManagement,
				this.motor4PowerManagement = motor4PowerManagement);
	}

	/**
	 * @return the defaultPlaneSelection
	 */
	public LabeledValue<BigDecimal> getDefaultPlaneSelection() {
		return defaultPlaneSelection;
	}

	/**
	 * @param defaultPlaneSelection the defaultPlaneSelection to set
	 */
	public void setDefaultPlaneSelection(LabeledValue<BigDecimal> defaultPlaneSelection) {
		firePropertyChange("defaultPlaneSelection",
				this.defaultPlaneSelection,
				this.defaultPlaneSelection = defaultPlaneSelection);
	}

	/**
	 * @return the defaultUnitsMode
	 */
	public LabeledValue<BigDecimal> getDefaultUnitsMode() {
		return defaultUnitsMode;
	}

	/**
	 * @param defaultUnitsMode the defaultUnitsMode to set
	 */
	public void setDefaultUnitsMode(LabeledValue<BigDecimal> defaultUnitsMode) {
		firePropertyChange("defaultUnitsMode", this.defaultUnitsMode,
				this.defaultUnitsMode = defaultUnitsMode);
	}

	/**
	 * @return the defaultCoordinateSystem
	 */
	public LabeledValue<BigDecimal> getDefaultCoordinateSystem() {
		return defaultCoordinateSystem;
	}

	/**
	 * @param defaultCoordinateSystem the defaultCoordinateSystem to set
	 */
	public void setDefaultCoordinateSystem(LabeledValue<BigDecimal> defaultCoordinateSystem) {
		firePropertyChange("defaultCoordinateSystem",
				this.defaultCoordinateSystem,
				this.defaultCoordinateSystem = defaultCoordinateSystem);
	}

	/**
	 * @return the defaultPathControl
	 */
	public LabeledValue<BigDecimal> getDefaultPathControl() {
		return defaultPathControl;
	}

	/**
	 * @param defaultPathControl the defaultPathControl to set
	 */
	public void setDefaultPathControl(LabeledValue<BigDecimal> defaultPathControl) {
		firePropertyChange("defaultPathControl", this.defaultPathControl,
				this.defaultPathControl = defaultPathControl);
	}

	/**
	 * @return the defaultDistanceMode
	 */
	public LabeledValue<BigDecimal> getDefaultDistanceMode() {
		return defaultDistanceMode;
	}

	/**
	 * @param defaultDistanceMode the defaultDistanceMode to set
	 */
	public void setDefaultDistanceMode(LabeledValue<BigDecimal> defaultDistanceMode) {
		firePropertyChange("defaultDistanceMode", this.defaultDistanceMode,
				this.defaultDistanceMode = defaultDistanceMode);
	}

	/**
	 * @return the enableCrOnTx
	 */
	public boolean isEnableCrOnTx() {
		return enableCrOnTx;
	}

	/**
	 * @param enableCrOnTx the enableCrOnTx to set
	 */
	public void setEnableCrOnTx(boolean enableCrOnTx) {
		firePropertyChange("enableCrOnTx", this.enableCrOnTx,
				this.enableCrOnTx = enableCrOnTx);
	}

	/**
	 * @return the ignoreCrOnRx
	 */
	public boolean isIgnoreCrOnRx() {
		return ignoreCrOnRx;
	}

	/**
	 * @param ignoreCrOnRx the ignoreCrOnRx to set
	 */
	public void setIgnoreCrOnRx(boolean ignoreCrOnRx) {
		firePropertyChange("ignoreCrOnRx", this.ignoreCrOnRx,
				this.ignoreCrOnRx = ignoreCrOnRx);
	}

	/**
	 * @return the ignoreLfOnRx
	 */
	public boolean isIgnoreLfOnRx() {
		return ignoreLfOnRx;
	}

	/**
	 * @param ignoreLfOnRx the ignoreLfOnRx to set
	 */
	public void setIgnoreLfOnRx(boolean ignoreLfOnRx) {
		firePropertyChange("ignoreLfOnRx", this.ignoreLfOnRx,
				this.ignoreLfOnRx = ignoreLfOnRx);
	}

	/**
	 * @param enableXonXoff the enableXonXoff to set
	 */
	public void setEnableXonXoff(boolean enableXonXoff) {
		firePropertyChange("enableXonXoff", this.enableXonXoff,
				this.enableXonXoff = enableXonXoff);
	}

	/**
	 * @return the choicesJSonVerbosity
	 */
	public List<LabeledValue<BigDecimal>> getChoicesJSonVerbosity() {
		return choicesJSonVerbosity;
	}

	/**
	 * @param choicesJSonVerbosity the choicesJSonVerbosity to set
	 */
	public void setChoicesJSonVerbosity(List<LabeledValue<BigDecimal>> choicesJSonVerbosity) {
		firePropertyChange("choicesJSonVerbosity", this.choicesJSonVerbosity,
				this.choicesJSonVerbosity = choicesJSonVerbosity);
	}

	/**
	 * @return the choicesExtendedVerbosity
	 */
	public List<LabeledValue<BigDecimal>> getChoicesExtendedVerbosity() {
		return choicesExtendedVerbosity;
	}

	/**
	 * @param choicesExtendedVerbosity the choicesExtendedVerbosity to set
	 */
	public void setChoicesExtendedVerbosity(
			List<LabeledValue<BigDecimal>> choicesExtendedVerbosity) {
		firePropertyChange("choicesExtendedVerbosity",
				this.choicesExtendedVerbosity,
				this.choicesExtendedVerbosity = choicesExtendedVerbosity);
	}

	/**
	 * @return the choicesVerbosity
	 */
	public List<LabeledValue<BigDecimal>> getChoicesVerbosity() {
		return choicesVerbosity;
	}

	/**
	 * @param choicesVerbosity the choicesVerbosity to set
	 */
	public void setChoicesVerbosity(List<LabeledValue<BigDecimal>> choicesVerbosity) {
		firePropertyChange("choicesVerbosity", this.choicesVerbosity,
				this.choicesVerbosity = choicesVerbosity);
	}

	/**
	 * @return the enableXonXoff
	 */
	public boolean isEnableXonXoff() {
		return enableXonXoff;
	}

	/**
	 * @return the jsonVerbosity
	 */
	public LabeledValue<BigDecimal> getJsonVerbosity() {
		return jsonVerbosity;
	}

	/**
	 * @param jsonVerbosity the jsonVerbosity to set
	 */
	public void setJsonVerbosity(LabeledValue<BigDecimal> jsonVerbosity) {
		firePropertyChange("jsonVerbosity", this.jsonVerbosity,
				this.jsonVerbosity = jsonVerbosity);
	}

	/**
	 * @return the textVerbosity
	 */
	public LabeledValue<BigDecimal> getTextVerbosity() {
		return textVerbosity;
	}

	/**
	 * @param textVerbosity the textVerbosity to set
	 */
	public void setTextVerbosity(LabeledValue<BigDecimal> textVerbosity) {
		firePropertyChange("textVerbosity", this.textVerbosity,
				this.textVerbosity = textVerbosity);
	}

	/**
	 * @return the queueReportVerbosity
	 */
	public LabeledValue<BigDecimal> getQueueReportVerbosity() {
		return queueReportVerbosity;
	}

	/**
	 * @param queueReportVerbosity the queueReportVerbosity to set
	 */
	public void setQueueReportVerbosity(LabeledValue<BigDecimal> queueReportVerbosity) {
		firePropertyChange("queueReportVerbosity", this.queueReportVerbosity,
				this.queueReportVerbosity = queueReportVerbosity);
	}

	/**
	 * @return the statusReportVerbosity
	 */
	public LabeledValue<BigDecimal> getStatusReportVerbosity() {
		return statusReportVerbosity;
	}

	/**
	 * @param statusReportVerbosity the statusReportVerbosity to set
	 */
	public void setStatusReportVerbosity(LabeledValue<BigDecimal> statusReportVerbosity) {
		firePropertyChange("statusReportVerbosity",
				this.statusReportVerbosity,
				this.statusReportVerbosity = statusReportVerbosity);
	}

	/**
	 * @return the enableEcho
	 */
	public boolean isEnableEcho() {
		return enableEcho;
	}

	/**
	 * @param enableEcho the enableEcho to set
	 */
	public void setEnableEcho(boolean enableEcho) {
		firePropertyChange("enableEcho", this.enableEcho,
				this.enableEcho = enableEcho);
	}

	/**
	 * @return the baudrate
	 */
	public LabeledValue<BigDecimal> getBaudrate() {
		return baudrate;
	}

	/**
	 * @param baudrate the baudrate to set
	 */
	public void setBaudrate(LabeledValue<BigDecimal> baudrate) {
		firePropertyChange("baudrate", this.baudrate, this.baudrate = baudrate);
	}

	/**
	 * @return the choicesBaudrate
	 */
	public List<LabeledValue<BigDecimal>> getChoicesBaudrate() {
		return choicesBaudrate;
	}

	/**
	 * @param choicesBaudrate the choicesBaudrate to set
	 */
	public void setChoicesBaudrate(List<LabeledValue<BigDecimal>> choicesBaudrate) {
		firePropertyChange("choicesBaudrate", this.choicesBaudrate,
				this.choicesBaudrate = choicesBaudrate);
	}

	/**
	 * @return the switchType
	 */
	public LabeledValue<BigDecimal> getSwitchType() {
		return switchType;
	}

	/**
	 * @param switchType the switchType to set
	 */
	public void setSwitchType(LabeledValue<BigDecimal> switchType) {
		firePropertyChange("switchType", this.switchType,
				this.switchType = switchType);
	}

	/**
	 * @return the choicesSwitchType
	 */
	public List<LabeledValue<BigDecimal>> getChoicesSwitchType() {
		return choicesSwitchType;
	}

	/**
	 * @param choicesSwitchType the choicesSwitchType to set
	 */
	public void setChoicesSwitchType(List<LabeledValue<BigDecimal>> choicesSwitchType) {
		firePropertyChange("choicesSwitchType", this.choicesSwitchType,
				this.choicesSwitchType = choicesSwitchType);
	}

	/**
	 * @return the bindingStatus
	 */
	@Override
	public List<BindingStatus> getValidationMessages() {
		return validationMessages;
	}

	/**
	 * @param bindingStatus the bindingStatus to set
	 */
	@Override
	public void setValidationMessages(List<BindingStatus> bindingStatus) {
		firePropertyChange("validationMessages", this.validationMessages, this.validationMessages = bindingStatus);
	}
}
