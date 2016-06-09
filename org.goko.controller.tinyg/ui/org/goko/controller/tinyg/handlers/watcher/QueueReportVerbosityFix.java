/**
 * 
 */
package org.goko.controller.tinyg.handlers.watcher;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.controller.tinyg.controller.configuration.TinyGConfigurationValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 7 juin 2016
 */
public class QueueReportVerbosityFix implements ITinyGConfigurationFix{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(QueueReportVerbosityFix.class);
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.handlers.watcher.ITinyGConfigurationFix#shouldApply(org.goko.controller.tinyg.controller.ITinygControllerService)
	 */
	@Override
	public boolean shouldApply(TinyGConfiguration configuration) {
		try {			
			BigDecimal setting = configuration.getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.QUEUE_REPORT_VERBOSITY, BigDecimal.class);
			return !ObjectUtils.equals(TinyGConfigurationValue.QUEUE_REPORT_TRIPLE, setting);
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
			configuration.setSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.QUEUE_REPORT_VERBOSITY, TinyGConfigurationValue.QUEUE_REPORT_TRIPLE);			
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.handlers.watcher.ITinyGConfigurationFix#getDescription()
	 */
	@Override
	public String getDescription() {		
		return "Queue Report Verbosity parameter ($qv) will be set to 2 - Triple";
	}

}
