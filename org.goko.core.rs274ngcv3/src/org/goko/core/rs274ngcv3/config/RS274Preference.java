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

package org.goko.core.rs274ngcv3.config;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreference;


public class RS274Preference extends GkPreference{
	/** Identifier of the config */
	public static final String NODE_ID = "org.goko.core.rs274ngcv3";
	/** Singleton instance */
	private static RS274Preference instance;

	public static final String KEY_DECIMAL_COUNT = "numberDecimalCount";
	public static final int DEFAULT_DECIMAL_COUNT = 3;
	public static final String KEY_TRUNCATE_ENABLED = "truncateEnabled";
	public static final boolean DEFAULT_TRUNCATE_ENABLED = false;
	/**
	 * Constructor
	 */
	private RS274Preference() {
		super(NODE_ID);

	}

	/**
	 * Singleton acces on the configuration
	 * @return the RS274Config instance
	 * @throws GkException GkException
	 */
	public static final RS274Preference getInstance(){
		if(instance == null){
			instance = new RS274Preference();
			instance.initialize();
		}
		return instance;
	}
	
	protected void initialize(){
		getPreferenceStore().setDefault(KEY_DECIMAL_COUNT, DEFAULT_DECIMAL_COUNT);
		getPreferenceStore().setDefault(KEY_TRUNCATE_ENABLED, DEFAULT_TRUNCATE_ENABLED);
	}
	/**
	 * @return the decimalCount
	 */
	public int getDecimalCount() {
		return Integer.valueOf(getPreferenceStore().getInt(KEY_DECIMAL_COUNT));
	}

	/**
	 * @param decimalCount the decimalCount to set
	 */
	public void setDecimalCount(int decimalCount) {		
		getPreferenceStore().setValue(KEY_DECIMAL_COUNT, String.valueOf(decimalCount));
	}


	public void setDecimalTruncateEnabled(boolean enabled) {
		getPreferenceStore().setValue(KEY_TRUNCATE_ENABLED, enabled);
	}


	public boolean isDecimalTruncateEnabled() {
		return getPreferenceStore().getBoolean(KEY_TRUNCATE_ENABLED);
	}
}
