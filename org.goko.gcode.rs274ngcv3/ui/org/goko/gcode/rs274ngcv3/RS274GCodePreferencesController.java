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

package org.goko.gcode.rs274ngcv3;

import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.gcode.rs274ngcv3.config.RS274Config;

public class RS274GCodePreferencesController extends AbstractController<RS274GCodePreferencesModel>{
	private static final GkLog LOG = GkLog.getLogger(RS274GCodePreferencesController.class);
	public RS274GCodePreferencesController() {
		super(new RS274GCodePreferencesModel());
		try {
			initialize();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		getDataModel().setTruncateEnabled(RS274Config.getConfig().isDecimalTruncateEnabled());
		getDataModel().setDecimalCount(RS274Config.getConfig().getDecimalCount());
	}

	public void savePreferences() throws GkException {
		RS274Config.getConfig().setDecimalCount(getDataModel().getDecimalCount());
		RS274Config.getConfig().setDecimalTruncateEnabled(getDataModel().isTruncateEnabled());
		RS274Config.getConfig().save();
	}

}
