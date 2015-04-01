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

import org.goko.core.common.exception.GkException;
import org.goko.core.config.AbstractGkConfig;


public class RS274Config extends AbstractGkConfig{
	/** Identifier of the config */
	private static final String CONFIG_ID = "org.goko.gcode.rs274ngcv3.config";
	/** Singleton instance */
	private static RS274Config instance;

	private static final String KEY_DECIMAL_COUNT = "numberDecimalCount";
	private static final String KEY_TRUNCATE_ENABLED = "truncateEnabled";
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
	public static final RS274Config getConfig() throws GkException{
		if(instance == null){
			instance = new RS274Config();
		}
		return instance;
	}

	/**
	 * @return the decimalCount
	 */
	public int getDecimalCount() {
		return getPreferences().getInt(KEY_DECIMAL_COUNT, 3);
	}

	/**
	 * @param decimalCount the decimalCount to set
	 */
	public void setDecimalCount(int decimalCount) {
		getPreferences().putInt(KEY_DECIMAL_COUNT, decimalCount);
	}


	public void setDecimalTruncateEnabled(boolean enabled) {
		getPreferences().putBoolean(KEY_TRUNCATE_ENABLED, enabled);
	}


	public boolean isDecimalTruncateEnabled() {
		return getPreferences().getBoolean(KEY_TRUNCATE_ENABLED, true);
	}
}
