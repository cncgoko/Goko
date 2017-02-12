/**
 * 
 */
package org.goko.controller.tinyg.handlers.watcher;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.controller.tinyg.commons.configuration.watcher.ITinyGConfigurationFix;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.controller.tinyg.controller.configuration.TinyGConfigurationValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 7 juin 2016
 */
public class JSonModeFix implements ITinyGConfigurationFix<TinyGConfiguration>{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JSonModeFix.class);
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.handlers.watcher.ITinyGConfigurationFix#shouldApply(org.goko.controller.tinyg.controller.ITinygControllerService)
	 */
	@Override
	public boolean shouldApply(TinyGConfiguration configuration) {
		try {			
			BigDecimal setting = configuration.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.JSON_MODE, BigDecimal.class);
			return !ObjectUtils.equals(TinyGConfigurationValue.JSON_MODE_ENABLE, setting);
		} catch (GkException e) {
			LOG.error(e);
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.handlers.watcher.ITinyGConfigurationFix#apply(org.goko.controller.tinyg.controller.ITinygControllerService)
	 */
	@Override
	public void apply(TinyGConfiguration configuration) {
		try{			
			configuration.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.JSON_MODE, TinyGConfigurationValue.JSON_MODE_ENABLE);			
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.handlers.watcher.ITinyGConfigurationFix#getDescription()
	 */
	@Override
	public String getDescription() {		
		return "JSON Mode parameter ($ej) will be set to 1 - Enable JSON mode on power-up and reset";
	}

}
