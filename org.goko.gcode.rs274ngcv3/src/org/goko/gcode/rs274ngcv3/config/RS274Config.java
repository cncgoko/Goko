/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.gcode.rs274ngcv3.config;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.AbstractGkConfig;


public class RS274Config extends AbstractGkConfig{
	/** Identifier of the config */
	private static final String CONFIG_ID = "org.goko.gcode.rs274ngcv3.config";
	/** Singleton instance */
	private static RS274Config instance;

	public static final String KEY_DECIMAL_COUNT = "numberDecimalCount";
	public static final String KEY_TRUNCATE_ENABLED = "truncateEnabled";
	/**
	 * Constructor
	 */
	private RS274Config() {
		super(CONFIG_ID);

	}

	/**
	 * Singleton acces on the configuration
	 * @return the RS274Config instance
	 * @throws GkException GkException
	 */
	public static final RS274Config getInstance(){
		if(instance == null){
			instance = new RS274Config();
			instance.initialize();
		}
		return instance;
	}

	private void initialize() {
		getPreferences().setDefault(KEY_TRUNCATE_ENABLED, false);
		getPreferences().setDefault(KEY_DECIMAL_COUNT, "3");		
		if(StringUtils.isBlank(KEY_DECIMAL_COUNT)){
			getPreferences().setToDefault(KEY_DECIMAL_COUNT);	
		}
	}
	
	/**
	 * @return the decimalCount
	 */
	public int getDecimalCount() {
		return Integer.valueOf(getPreferences().getString(KEY_DECIMAL_COUNT));
	}

	/**
	 * @param decimalCount the decimalCount to set
	 */
	public void setDecimalCount(int decimalCount) {
		getPreferences().setValue(KEY_DECIMAL_COUNT, String.valueOf(decimalCount));
	}


	public void setDecimalTruncateEnabled(boolean enabled) {
		getPreferences().setValue(KEY_TRUNCATE_ENABLED, enabled);
	}


	public boolean isDecimalTruncateEnabled() {
		return getPreferences().getBoolean(KEY_TRUNCATE_ENABLED);
	}
}
