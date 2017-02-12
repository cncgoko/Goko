/**
 * 
 */
package org.goko.controller.g2core.handlers.watcher;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.controller.g2core.configuration.G2CoreConfiguration;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.watcher.ITinyGConfigurationFix;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 7 juin 2016
 */
public class JSonModeFix implements ITinyGConfigurationFix<G2CoreConfiguration>{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JSonModeFix.class);
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.handlers.watcher.ITinyGConfigurationFix#shouldApply(org.goko.controller.tinyg.controller.ITinygControllerService)
	 */
	@Override
	public boolean shouldApply(G2CoreConfiguration configuration) {
		try {			
			BigDecimal setting = configuration.getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.JSON_MODE, BigDecimal.class);
			return !ObjectUtils.equals( G2Core.Configuration.Values.JSON_MODE_ENABLE, setting);
		} catch (GkException e) {
			LOG.error(e);
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.handlers.watcher.ITinyGConfigurationFix#apply(org.goko.controller.tinyg.controller.ITinygControllerService)
	 */
	@Override
	public void apply(G2CoreConfiguration configuration) {
		try{			
			configuration.setSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.JSON_MODE, G2Core.Configuration.Values.JSON_MODE_ENABLE);			
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
