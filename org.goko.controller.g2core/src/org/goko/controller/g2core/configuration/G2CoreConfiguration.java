/**
 * 
 */
package org.goko.controller.g2core.configuration;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.controller.tinyg.commons.configuration.TinyGGroupSettings;

/**
 * @author Psyko
 * @date 8 janv. 2017
 */
public class G2CoreConfiguration extends AbstractTinyGConfiguration<G2CoreConfiguration>{

	/**
	 * Constructor 
	 */
	public G2CoreConfiguration() {
		this(true);
	}
	
	/**
	 * Constructor with parameterized group or setting initialization 
	 */
	protected G2CoreConfiguration(boolean init) {
		super();
		if(init){
			initialize();
		}
	}
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration#newInstance()
	 */
	@Override
	protected G2CoreConfiguration newInstance() {		
		return new G2CoreConfiguration(false);
	}
	
	/**
	 * Initialization of the configuration
	 */
	protected void initialize(){
		/*
		 *  System group
		 */
		TinyGGroupSettings systemGroup = new TinyGGroupSettings(G2Core.Configuration.Groups.SYSTEM);
		systemGroup.addSettingReadOnly(G2Core.Configuration.System.FIRMWARE_VERSION		, BigDecimal.ZERO); 		
		systemGroup.addSettingReadOnly(G2Core.Configuration.System.FIRMWARE_BUILD		, BigDecimal.ZERO); 	
		systemGroup.addSetting(G2Core.Configuration.System.FIRMWARE_BUILD_STRING		, StringUtils.EMPTY); 	
		systemGroup.addSetting(G2Core.Configuration.System.FIRMWARE_BUILD_CONFIG		, StringUtils.EMPTY); 	
		systemGroup.addSetting(G2Core.Configuration.System.HARDWARE_PLATFORM			, BigDecimal.ZERO); 	
		systemGroup.addSetting(G2Core.Configuration.System.HARDWARE_VERSION				, BigDecimal.ZERO); 	
		systemGroup.addSetting(G2Core.Configuration.System.BOARD_ID						, StringUtils.EMPTY); 	
		                      
		systemGroup.addSetting(G2Core.Configuration.System.JUNCTION_INTEGRATION_TIME	, BigDecimal.ZERO); 	
		systemGroup.addSetting(G2Core.Configuration.System.CHORDAL_TOLERANCE			, BigDecimal.ZERO); 	
		systemGroup.addSetting(G2Core.Configuration.System.MOTOR_DISABLE_TIMEOUT		, BigDecimal.ZERO); 	
		                      
		systemGroup.addSetting(G2Core.Configuration.System.JSON_MODE					, BigDecimal.ZERO); 	
		systemGroup.addSetting(G2Core.Configuration.System.JSON_MODE_VERBOSITY			, BigDecimal.ZERO); 			
		systemGroup.addSetting(G2Core.Configuration.System.TEXT_MODE_VERBOSITY			, BigDecimal.ZERO); 			
		systemGroup.addSetting(G2Core.Configuration.System.QUEUE_REPORT_VERBOSITY		, BigDecimal.ZERO); 		
		systemGroup.addSetting(G2Core.Configuration.System.STATUS_REPORT_VERBOSITY		, BigDecimal.ZERO); 	
		systemGroup.addSetting(G2Core.Configuration.System.STATUS_REPORT_INTERVAL		, BigDecimal.ZERO); 		
		//systemGroup.addSetting(G2Core.Configuration.System.FLOW_CONTROL					, BigDecimal.ZERO); 				
		                      
		systemGroup.addSetting(G2Core.Configuration.System.DEFAULT_PLANE_SELECTION		, BigDecimal.ZERO); 		
		systemGroup.addSetting(G2Core.Configuration.System.DEFAULT_UNITS_MODE			, BigDecimal.ZERO); 			
		systemGroup.addSetting(G2Core.Configuration.System.DEFAULT_COORDINATE_SYSTEM	, BigDecimal.ZERO); 	
		systemGroup.addSetting(G2Core.Configuration.System.DEFAULT_PATH_CONTROL			, BigDecimal.ZERO); 			
		systemGroup.addSetting(G2Core.Configuration.System.DEFAULT_DISTANCE_MODE		, BigDecimal.ZERO); 		
		
		addGroup(systemGroup);
		
		/*
		 * Axis groups
		 */
		addGroup( new G2CoreLinearAxisSettings(G2Core.Configuration.Groups.X_AXIS) );
		addGroup( new G2CoreLinearAxisSettings(G2Core.Configuration.Groups.Y_AXIS) );
		addGroup( new G2CoreLinearAxisSettings(G2Core.Configuration.Groups.Z_AXIS) );
		
		addGroup( new G2CoreRotationalAxisSettings(G2Core.Configuration.Groups.A_AXIS) );
		addGroup( new G2CoreRotationalAxisSettings(G2Core.Configuration.Groups.B_AXIS) );		
		addGroup( new G2CoreRotationalAxisSettings(G2Core.Configuration.Groups.C_AXIS) );
				
		/*
		 * Motor groups		
		 */
		addGroup( new G2CoreMotorSettings(G2Core.Configuration.Groups.MOTOR_1));
		addGroup( new G2CoreMotorSettings(G2Core.Configuration.Groups.MOTOR_2));
		addGroup( new G2CoreMotorSettings(G2Core.Configuration.Groups.MOTOR_3));
		addGroup( new G2CoreMotorSettings(G2Core.Configuration.Groups.MOTOR_4));
		
		/*
		 * PWM Channel
		 */
		addGroup( new G2CorePWMChannelSettings(G2Core.Configuration.Groups.PWM_CHANNEL_1));
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration#getDefaultGroup()
	 */
	@Override
	protected String getDefaultGroup() {		
		return G2Core.Configuration.Groups.SYSTEM;
	}
}
