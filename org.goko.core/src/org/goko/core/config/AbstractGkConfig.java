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

package org.goko.core.config;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.osgi.service.prefs.BackingStoreException;

public abstract class AbstractGkConfig {
	/** The stored preference */
	private IEclipsePreferences preferences;

	/**
	 * Constructor
	 */
	protected AbstractGkConfig(String id) {
		preferences = InstanceScope.INSTANCE.getNode(id);
	}

	protected IEclipsePreferences getPreferences(){
		return preferences;
	}

	/**
	 * Save the current values
	 * @throws GkException GkException
	 */
	public void save() throws GkException{
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			throw new GkTechnicalException(e);
		}
	}
}

///** The stored preference */
//private IPersistentPreferenceStore preferences;
//
///**
// * Constructor
// */
//protected AbstractGkConfig(String id) {
//	preferences = new ScopedPreferenceStore(ConfigurationScope.INSTANCE, id);
//}
//
//protected IPreferenceStore getPreferences(){
//	return preferences;
//}
//
///**
// * Save the current values
// * @throws GkException GkException
// */
//public void save() throws GkException{		
//	try {
//		preferences.save();
//	} catch (IOException e) {
//		throw new GkTechnicalException(e);
//	}
//}
