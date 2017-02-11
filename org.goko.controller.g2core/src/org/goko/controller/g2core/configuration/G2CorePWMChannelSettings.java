/**
 * 
 */
package org.goko.controller.g2core.configuration;

import java.math.BigDecimal;

import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.TinyGGroupSettings;

/**
 * @author Psyko
 * @date 31 janv. 2017
 */
public class G2CorePWMChannelSettings extends TinyGGroupSettings {

	public G2CorePWMChannelSettings(String channel){
		super(channel);
		addSetting(G2Core.Configuration.PwmChannel.FREQUENCY 			,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.PwmChannel.CLOCKWISE_SPEED_LOW 	,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.PwmChannel.CLOCKWISE_SPEED_HIGH	,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.PwmChannel.CLOCKWISE_PHASE_LOW 	,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.PwmChannel.CLOCKWISE_PHASE_HIGH	,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.PwmChannel.COUNTERCLOCKWISE_SPEED_LOW,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.PwmChannel.COUNTERCLOCKWISE_SPEED_HIGH,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.PwmChannel.COUNTERCLOCKWISE_PHASE_LOW,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.PwmChannel.COUNTERCLOCKWISE_PHASE_HIGH,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.PwmChannel.PHASE_OFF			,BigDecimal.ZERO);
	}
}
