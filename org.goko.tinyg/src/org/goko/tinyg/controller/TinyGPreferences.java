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
package org.goko.tinyg.controller;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.goko.core.common.exception.GkTechnicalException;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class TinyGPreferences {
	private Preferences preferences;
	private static String PLANNER_BUFFER_SPACE_CHECK = "TinyGPreferences.plannerBufferSpaceCheck";

	public TinyGPreferences() {
		preferences = InstanceScope.INSTANCE.getNode(TinyGControllerService.SERVICE_ID);
	}

	/**
	 * @return the plannerBufferSpaceCheck
	 */
	public boolean isPlannerBufferSpaceCheck() {
		return preferences.getBoolean(PLANNER_BUFFER_SPACE_CHECK, true);
	}

	/**
	 * @param value the plannerBufferSpaceCheck to set
	 * @throws GkTechnicalException GkTechnicalException
	 */
	public void setPlannerBufferSpaceCheck(boolean value) throws GkTechnicalException {
		try {
			preferences.putBoolean(PLANNER_BUFFER_SPACE_CHECK, value);
			preferences.flush();
		} catch (BackingStoreException e) {
			throw new GkTechnicalException(e);
		}
	}


}
