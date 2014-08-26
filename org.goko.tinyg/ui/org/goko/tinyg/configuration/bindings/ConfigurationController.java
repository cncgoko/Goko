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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.common.GkUiUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.tinyg.configuration.bindings.wrapper.TinyGLinearAxisSettingsWrapper;
import org.goko.tinyg.controller.TinyGControllerService;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.tinyg.controller.configuration.TinyGGroupSettings;
import org.goko.tinyg.controller.configuration.TinyGSetting;
import org.goko.tinyg.service.ITinyGControllerFirmwareService;

public class ConfigurationController extends AbstractController<ConfigurationBindings> implements PropertyChangeListener {
	private static final GkLog LOG = GkLog.getLogger(ConfigurationController.class);
	@Inject
	private ITinyGControllerFirmwareService controllerService;
	/**
	 * Constructor
	 * @param binding
	 */
	public ConfigurationController(ConfigurationBindings binding) {
		super(binding);
		binding.addPropertyChangeListener(this);
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		controllerService.addListener(this);
	}

	/**
	 * Refresh the display of the settings
	 * @throws GkException GkException
	 */
	public void applySettingChanges() throws GkException{
		TinyGConfiguration cfg =  getControllerService().getConfiguration();
		// Global system settings
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.JUNCTION_ACCELERATION	 , getDataModel().getJunctionAcceleration());
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.CHORDAL_TOLERANCE		 , getDataModel().getChordalTolerance());
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.SWITCH_TYPE		 	 , getDataModel().getSwitchType().getValue());
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.MOTOR_DISABLE_TIMEOUT	 , getDataModel().getMotorDisableTimeout());

		// Communication settings
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.JSON_VERBOSITY		, getDataModel().getJsonVerbosity().getValue());
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.TEXT_MODE_VERBOSITY	, getDataModel().getTextVerbosity().getValue());
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.QUEUE_REPORT_VERBOSITY, getDataModel().getQueueReportVerbosity().getValue());
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.STATUS_REPORT_VERBOSITY, getDataModel().getStatusReportVerbosity().getValue());
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.STATUS_REPORT_INTERVAL , getDataModel().getStatusinterval());
		boolean ignoreCrOnRx = getDataModel().isIgnoreCrOnRx();
		boolean ignoreLfOnRx = getDataModel().isIgnoreLfOnRx();
		int crlf = 0;
		if(ignoreCrOnRx){ crlf = 1; }
		if(ignoreLfOnRx){ crlf = 2; }
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.IGNORE_CR_LF_ON_RX , 		new BigDecimal(String.valueOf(crlf)));
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_CR_ON_TX 	 , 		new BigDecimal(booleanAs0Or1(getDataModel().isEnableCrOnTx())));
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_CHARACTER_ECHO, 	new BigDecimal(booleanAs0Or1(getDataModel().isEnableEcho())));
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_XON_XOFF, 			new BigDecimal(booleanAs0Or1(getDataModel().isEnableXonXoff())));
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.BAUD_RATE, 				getDataModel().getBaudrate().getValue() );

		// GCode defaults
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_PLANE_SELECTION	, getDataModel().getDefaultPlaneSelection().getValue() );
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_UNITS_MODE		, getDataModel().getDefaultUnitsMode().getValue() );
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_COORDINATE_SYSTEM	, getDataModel().getDefaultCoordinateSystem().getValue() );
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_PATH_CONTROL		, getDataModel().getDefaultPathControl().getValue() );
		cfg.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_DISTANCE_MODE		, getDataModel().getDefaultDistanceMode().getValue() );

		// Apply motors settings
		applyMotorSetting(cfg);

		//
		applyAxisConfiguration(cfg, "x", getDataModel().getxAxisWrapper());
		applyAxisConfiguration(cfg, "y", getDataModel().getyAxisWrapper());
		applyAxisConfiguration(cfg, "z", getDataModel().getzAxisWrapper());
		applyAxisConfiguration(cfg, "a", getDataModel().getaAxisWrapper());
		getControllerService().setConfiguration(cfg);
	}

	private void applyMotorSetting(TinyGConfiguration cfg) throws GkException{
		cfg.setSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.STEP_ANGLE, getDataModel().getMotor1StepAngle());
		cfg.setSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.TRAVEL_PER_REVOLUTION, getDataModel().getTravelPerRevM1());
		cfg.setSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.MICROSTEPS, getDataModel().getMotor1Microsteps().getValue());
		cfg.setSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.POLARITY, getDataModel().getMotor1Polarity().getValue());
		cfg.setSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.POWER_MANAGEMENT_MODE, getDataModel().getMotor1PowerManagement().getValue());

		cfg.setSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.STEP_ANGLE, getDataModel().getMotor2StepAngle());
		cfg.setSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.TRAVEL_PER_REVOLUTION, getDataModel().getTravelPerRevM2());
		cfg.setSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.MICROSTEPS, getDataModel().getMotor2Microsteps().getValue());
		cfg.setSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.POLARITY, getDataModel().getMotor2Polarity().getValue());
		cfg.setSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.POWER_MANAGEMENT_MODE, getDataModel().getMotor2PowerManagement().getValue());

		cfg.setSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.STEP_ANGLE, getDataModel().getMotor3StepAngle());
		cfg.setSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.TRAVEL_PER_REVOLUTION, getDataModel().getTravelPerRevM3());
		cfg.setSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.MICROSTEPS, getDataModel().getMotor3Microsteps().getValue());
		cfg.setSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.POLARITY, getDataModel().getMotor3Polarity().getValue());
		cfg.setSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.POWER_MANAGEMENT_MODE, getDataModel().getMotor3PowerManagement().getValue());

		cfg.setSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.STEP_ANGLE, getDataModel().getMotor4StepAngle());
		cfg.setSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.TRAVEL_PER_REVOLUTION, getDataModel().getTravelPerRevM4());
		cfg.setSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.MICROSTEPS, getDataModel().getMotor4Microsteps().getValue());
		cfg.setSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.POLARITY, getDataModel().getMotor4Polarity().getValue());
		cfg.setSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.POWER_MANAGEMENT_MODE, getDataModel().getMotor4PowerManagement().getValue());

	}
	private String booleanAs0Or1(boolean bool){
		if(bool){
			return "1";
		}
		return "0";
	}
	public void requestConfigurationRefresh(){
		try {
			getControllerService().refreshConfiguration();
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	public void refreshConfiguration() throws GkException{
		TinyGConfiguration cfg = getControllerService().getConfiguration();
		getDataModel().setFirmwareBuild( cfg.getSetting(TinyGConfiguration.FIRMWARE_BUILD, BigDecimal.class) );
		getDataModel().setFirmwareVersion( cfg.getSetting(TinyGConfiguration.FIRMWARE_VERSION, BigDecimal.class) );
		getDataModel().setHardwareVersion( cfg.getSetting(TinyGConfiguration.HARDWARE_VERSION, BigDecimal.class) );
		getDataModel().setUniqueId( cfg.getSetting(TinyGConfiguration.UNIQUE_ID) );

		refreshGlobalSettings();
		refreshGCodeDefaults();
		refreshCommunicationSettings();
		refreshAxisConfiguration();
		refreshMotorConfiguration();
	}

	public void refreshGlobalSettings() throws GkException{
		TinyGConfiguration cfg = getControllerService().getConfiguration();

		getDataModel().setChordalTolerance(  	cfg.getSetting(TinyGConfiguration.CHORDAL_TOLERANCE, BigDecimal.class));
		getDataModel().setMotorDisableTimeout( 	cfg.getSetting(TinyGConfiguration.MOTOR_DISABLE_TIMEOUT, BigDecimal.class));
		getDataModel().setStatusinterval( 		cfg.getSetting(TinyGConfiguration.STATUS_REPORT_INTERVAL, BigDecimal.class) );
		getDataModel().setJunctionAcceleration( cfg.getSetting(TinyGConfiguration.JUNCTION_ACCELERATION, BigDecimal.class));

		BigDecimal switchType = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.SWITCH_TYPE, BigDecimal.class);
		getDataModel().setSwitchType( 	GkUiUtils.getLabelledValueByKey(switchType, getDataModel().getChoicesSwitchType()));


	}
	public void refreshGCodeDefaults() throws GkException{
		TinyGConfiguration cfg = getControllerService().getConfiguration();

		BigDecimal planeSelection = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_PLANE_SELECTION, BigDecimal.class);
		getDataModel().setDefaultPlaneSelection( GkUiUtils.getLabelledValueByKey(planeSelection, getDataModel().getChoicesPlaneSelection()) );

		BigDecimal units = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_UNITS_MODE, BigDecimal.class);
		getDataModel().setDefaultUnitsMode( GkUiUtils.getLabelledValueByKey(units, getDataModel().getChoicesUnits()) );

		BigDecimal coordinateSystem = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_COORDINATE_SYSTEM, BigDecimal.class);
		getDataModel().setDefaultCoordinateSystem( GkUiUtils.getLabelledValueByKey(coordinateSystem, getDataModel().getChoicesCoordinateSystem()) );

		BigDecimal pathControl = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_PATH_CONTROL, BigDecimal.class);
		getDataModel().setDefaultPathControl( GkUiUtils.getLabelledValueByKey(pathControl, getDataModel().getChoicesPathControl()) );

		BigDecimal distanceMode = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.DEFAULT_DISTANCE_MODE, BigDecimal.class);
		getDataModel().setDefaultDistanceMode( GkUiUtils.getLabelledValueByKey(distanceMode, getDataModel().getChoicesDistanceMode()) );

	}

	private void refreshCommunicationSettings() throws GkException{
		TinyGConfiguration cfg = getControllerService().getConfiguration();

		BigDecimal jsonVerbosity = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.JSON_VERBOSITY, BigDecimal.class);
		getDataModel().setJsonVerbosity( GkUiUtils.getLabelledValueByKey(jsonVerbosity, getDataModel().getChoicesJSonVerbosity()) );

		BigDecimal textVerbosity = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.TEXT_MODE_VERBOSITY, BigDecimal.class );
		getDataModel().setTextVerbosity( GkUiUtils.getLabelledValueByKey(textVerbosity, getDataModel().getChoicesVerbosity()) );

		BigDecimal queueVerbosity = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.QUEUE_REPORT_VERBOSITY, BigDecimal.class );
		getDataModel().setQueueReportVerbosity( GkUiUtils.getLabelledValueByKey(queueVerbosity, getDataModel().getChoicesExtendedVerbosity()) );

		BigDecimal statusVerbosity = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.STATUS_REPORT_VERBOSITY, BigDecimal.class );
		getDataModel().setStatusReportVerbosity( GkUiUtils.getLabelledValueByKey(statusVerbosity, getDataModel().getChoicesExtendedVerbosity()));

		BigDecimal characterEcho = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_CHARACTER_ECHO, BigDecimal.class);
		getDataModel().setEnableEcho( ObjectUtils.equals(characterEcho, new BigDecimal("1")) );

		BigDecimal ignoreCrLfOnRx = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.IGNORE_CR_LF_ON_RX, BigDecimal.class);
		getDataModel().setIgnoreCrOnRx( ObjectUtils.equals(ignoreCrLfOnRx, new BigDecimal("1")) );
		getDataModel().setIgnoreLfOnRx( ObjectUtils.equals(ignoreCrLfOnRx, new BigDecimal("2")) );

		BigDecimal enableCrTx = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_CR_ON_TX, BigDecimal.class);
		getDataModel().setEnableCrOnTx( ObjectUtils.equals(enableCrTx, new BigDecimal("1")) );

		BigDecimal enableXonXoff = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.ENABLE_XON_XOFF, BigDecimal.class);
		getDataModel().setEnableXonXoff( ObjectUtils.equals(enableXonXoff, new BigDecimal("1")) );

		BigDecimal baudrate = cfg.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.BAUD_RATE, BigDecimal.class );
		getDataModel().setBaudrate( GkUiUtils.getLabelledValueByKey(baudrate, getDataModel().getChoicesBaudrate()) );

	}
	public void refreshMotorConfiguration() throws GkException{
		TinyGConfiguration cfg = getControllerService().getConfiguration();

		BigDecimal motor1Mapping = cfg.getSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.MOTOR_MAPPING, BigDecimal.class );
		getDataModel().setAxisForMotor1( GkUiUtils.getLabelledValueByKey(motor1Mapping, getDataModel().getChoicesMotorMapping()) );

		BigDecimal motor2Mapping = cfg.getSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.MOTOR_MAPPING, BigDecimal.class );
		getDataModel().setAxisForMotor2( GkUiUtils.getLabelledValueByKey(motor2Mapping, getDataModel().getChoicesMotorMapping()) );

		BigDecimal motor3Mapping = cfg.getSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.MOTOR_MAPPING, BigDecimal.class );
		getDataModel().setAxisForMotor3( GkUiUtils.getLabelledValueByKey(motor3Mapping, getDataModel().getChoicesMotorMapping()) );

		BigDecimal motor4Mapping = cfg.getSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.MOTOR_MAPPING, BigDecimal.class );
		getDataModel().setAxisForMotor4( GkUiUtils.getLabelledValueByKey(motor4Mapping, getDataModel().getChoicesMotorMapping()) );

		{
			getDataModel().setMotor1StepAngle(	cfg.getSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.STEP_ANGLE, BigDecimal.class) );
			getDataModel().setTravelPerRevM1(	cfg.getSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.TRAVEL_PER_REVOLUTION, BigDecimal.class) );

			BigDecimal microsteps1 = cfg.getSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.MICROSTEPS, BigDecimal.class);
			getDataModel().setMotor1Microsteps( GkUiUtils.getLabelledValueByKey(microsteps1, getDataModel().getChoicesMicrosteps()) );

			BigDecimal polarityM1 = cfg.getSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.POLARITY, BigDecimal.class);
			if(polarityM1 == null){
				polarityM1 = new BigDecimal("0");
			}
			getDataModel().setMotor1Polarity( GkUiUtils.getLabelledValueByKey(polarityM1, getDataModel().getChoicesPolarity()) );

			BigDecimal powerMode = cfg.getSetting(TinyGConfiguration.MOTOR_1_SETTINGS, TinyGConfiguration.POWER_MANAGEMENT_MODE, BigDecimal.class);
			if(powerMode == null){
				powerMode = new BigDecimal("0");
			}
			getDataModel().setMotor1PowerManagement( GkUiUtils.getLabelledValueByKey(powerMode, getDataModel().getChoicesPowerManagement()) );
		}
		{
			getDataModel().setMotor2StepAngle(	cfg.getSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.STEP_ANGLE, BigDecimal.class) );
			getDataModel().setTravelPerRevM2(	cfg.getSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.TRAVEL_PER_REVOLUTION, BigDecimal.class) );
			BigDecimal microsteps2 = cfg.getSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.MICROSTEPS, BigDecimal.class);
			getDataModel().setMotor2Microsteps( GkUiUtils.getLabelledValueByKey(microsteps2, getDataModel().getChoicesMicrosteps()) );

			BigDecimal polarityM2 = cfg.getSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.POLARITY, BigDecimal.class);
			if(polarityM2 == null){
				polarityM2 = new BigDecimal("0");
			}
			getDataModel().setMotor2Polarity( GkUiUtils.getLabelledValueByKey(polarityM2, getDataModel().getChoicesPolarity()) );

			BigDecimal powerMode = cfg.getSetting(TinyGConfiguration.MOTOR_2_SETTINGS, TinyGConfiguration.POWER_MANAGEMENT_MODE, BigDecimal.class);
			if(powerMode == null){
				powerMode = new BigDecimal("0");
			}
			getDataModel().setMotor2PowerManagement( GkUiUtils.getLabelledValueByKey(powerMode, getDataModel().getChoicesPowerManagement()) );
		}
		{
			getDataModel().setMotor3StepAngle(	cfg.getSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.STEP_ANGLE, BigDecimal.class) );
			getDataModel().setTravelPerRevM3(	cfg.getSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.TRAVEL_PER_REVOLUTION, BigDecimal.class) );

			BigDecimal microsteps3 = cfg.getSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.MICROSTEPS, BigDecimal.class);
			getDataModel().setMotor3Microsteps( GkUiUtils.getLabelledValueByKey(microsteps3, getDataModel().getChoicesMicrosteps()) );

			BigDecimal polarityM3 = cfg.getSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.POLARITY, BigDecimal.class);
			if(polarityM3 == null){
				polarityM3 = new BigDecimal("0");
			}
			getDataModel().setMotor3Polarity( GkUiUtils.getLabelledValueByKey(polarityM3, getDataModel().getChoicesPolarity()) );

			BigDecimal powerMode = cfg.getSetting(TinyGConfiguration.MOTOR_3_SETTINGS, TinyGConfiguration.POWER_MANAGEMENT_MODE, BigDecimal.class);
			if(powerMode == null){
				powerMode = new BigDecimal("0");
			}
			getDataModel().setMotor3PowerManagement( GkUiUtils.getLabelledValueByKey(powerMode, getDataModel().getChoicesPowerManagement()) );
		}
		{
			getDataModel().setMotor4StepAngle(	cfg.getSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.STEP_ANGLE, BigDecimal.class) );
			getDataModel().setTravelPerRevM4(	cfg.getSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.TRAVEL_PER_REVOLUTION, BigDecimal.class) );

			BigDecimal microsteps4 = cfg.getSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.MICROSTEPS, BigDecimal.class);
			getDataModel().setMotor4Microsteps( GkUiUtils.getLabelledValueByKey(microsteps4, getDataModel().getChoicesMicrosteps()) );

			BigDecimal polarityM4 = cfg.getSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.POLARITY, BigDecimal.class);
			if(polarityM4 == null){
				polarityM4 = new BigDecimal("0");
			}
			getDataModel().setMotor4Polarity( GkUiUtils.getLabelledValueByKey(polarityM4, getDataModel().getChoicesPolarity()) );

			BigDecimal powerMode = cfg.getSetting(TinyGConfiguration.MOTOR_4_SETTINGS, TinyGConfiguration.POWER_MANAGEMENT_MODE, BigDecimal.class);
			if(powerMode == null){
				powerMode = new BigDecimal("0");
			}
			getDataModel().setMotor4PowerManagement( GkUiUtils.getLabelledValueByKey(powerMode, getDataModel().getChoicesPowerManagement()) );
		}
	}

	public void refreshAxisConfiguration() throws GkException{
		TinyGConfiguration cfg = getControllerService().getConfiguration();
		getDataModel().getxAxisWrapper().setConfiguration(cfg);
		getDataModel().getyAxisWrapper().setConfiguration(cfg);
		getDataModel().getzAxisWrapper().setConfiguration(cfg);
		getDataModel().getaAxisWrapper().setConfiguration(cfg);
	}


	private void applyAxisConfiguration(TinyGConfiguration cfg, String targetGroup, TinyGLinearAxisSettingsWrapper wrapper) throws GkException{
		TinyGGroupSettings grp = cfg.getGroup(targetGroup);
		TinyGGroupSettings wrapperGrp = wrapper.getConfiguration().getGroup(targetGroup);

		for (TinyGSetting<?> setting : grp.getSettings()) {
			// On passe la valeur du wrapper dans la config
			@SuppressWarnings("unchecked")
			Object wrapperValue = wrapper.getConfiguration().getSetting(wrapperGrp.getGroupIdentifier(), setting.getIdentifier(), setting.getType());
			cfg.setSetting(grp.getGroupIdentifier(), setting.getIdentifier(),wrapperValue);
		}
	}

	/**
	 * @return the controllerService
	 */
	public ITinyGControllerFirmwareService getControllerService() {
		return controllerService;
	}

	/**
	 * @param controllerService the controllerService to set
	 */
	public void setControllerService(TinyGControllerService controllerService) {
		this.controllerService = controllerService;
	}

	/** (inheritDoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		try {
			if(StringUtils.equals( event.getPropertyName(), "selectedAxis")){
				refreshAxisConfiguration();
			}
		} catch (GkException e) {
			e.printStackTrace();
		}
	}



}
