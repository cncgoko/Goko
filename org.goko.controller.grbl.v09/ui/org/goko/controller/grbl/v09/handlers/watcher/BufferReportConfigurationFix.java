/**
 * 
 */
package org.goko.controller.grbl.v09.handlers.watcher;

import org.apache.commons.lang3.StringUtils;
import org.goko.controller.grbl.v09.configuration.GrblConfiguration;
import org.goko.controller.grbl.v09.configuration.GrblIntegerSetting;

/**
 * @author Psyko
 * @date 28 nov. 2016
 */
public class BufferReportConfigurationFix implements IGrblConfigurationFix {

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.handlers.watcher.IGrblConfigurationFix#shouldApply(org.goko.controller.grbl.v09.configuration.GrblConfiguration)
	 */
	@Override
	public boolean shouldApply(String identifier, GrblConfiguration configuration) {
		if(StringUtils.isBlank(identifier) || StringUtils.equals(identifier, GrblConfiguration.STATUS_REPORT_MASK)){
			GrblIntegerSetting mask = configuration.getStatusReportMask();
			// Planner Buffer 	4
			return mask.isAssigned() && (mask.getValue() & (1 << 2)) != 4;
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.handlers.watcher.IGrblConfigurationFix#apply(org.goko.controller.grbl.v09.configuration.GrblConfiguration)
	 */
	@Override
	public void apply(GrblConfiguration configuration) {
		GrblIntegerSetting maskSetting = configuration.getStatusReportMask();
		int mask = maskSetting.getValue();
		maskSetting.setValue( mask | (1 << 2));
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.handlers.watcher.IGrblConfigurationFix#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Status report will be modified to include buffer report";
	}

}
