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
package org.goko.controller.tinyg.controller.configuration.type;

import org.goko.controller.tinyg.commons.configuration.TinyGSetting;

public class TinyGStringSetting extends TinyGSetting<String>{

	public TinyGStringSetting(String identifier, String value) {
		super(identifier, value);
	}

	public TinyGStringSetting(String identifier, String value, boolean readonly) {
		super(identifier, value, readonly);
	}

	protected TinyGStringSetting(String identifier, String value, boolean readonly, boolean assigned) {
		super(identifier, value, readonly, assigned);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.configuration.TinyGSetting#getType()
	 */
	@Override
	public Class<String> getType() {
		return String.class;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.configuration.TinyGSetting#clone(java.lang.Object)
	 */
	@Override
	protected String clone(String value) {
		return value;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.configuration.TinyGSetting#copy()
	 */
	@Override
	protected TinyGSetting<String> copy() {
		return new TinyGStringSetting(getIdentifier(), getValue(), isReadOnly(), isAssigned());
	}
}
