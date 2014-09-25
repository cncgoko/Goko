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
package org.goko.viewer.jogl.model;

import org.goko.common.GkUiUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.viewer.jogl.service.JoglViewerSettings;

/**
 * Controller for Jogl Viewer Preferences
 * @author PsyKo
 *
 */
public class JoglViewerPreferencesController extends AbstractController<JoglViewerPreferencesModel>{
	/** Constructor */
	public JoglViewerPreferencesController() {
		super(new JoglViewerPreferencesModel());
	}

	@Override
	public void initialize() throws GkException {
		JoglViewerSettings prefs = JoglViewerSettings.getInstance();

		getDataModel().setRotaryAxisDirection(GkUiUtils.getLabelledValueByKey(prefs.getRotaryAxisDirection(), getDataModel().getRotaryAxisDirectionChoice()));
		getDataModel().setRotaryAxisEnabled( prefs.isRotaryAxisEnabled() );
		getDataModel().setRotaryAxisPositionX( prefs.getRotaryAxisPosition().getX() );
		getDataModel().setRotaryAxisPositionY( prefs.getRotaryAxisPosition().getY() );
		getDataModel().setRotaryAxisPositionZ( prefs.getRotaryAxisPosition().getZ() );
	}

	public void savePreferences(){
		JoglViewerSettings.getInstance().setRotaryAxisDirection( getDataModel().getRotaryAxisDirection().getValue() );
		JoglViewerSettings.getInstance().setRotaryAxisEnabled( getDataModel().isRotaryAxisEnabled() );

		Tuple6b position = new Tuple6b(getDataModel().getRotaryAxisPositionX(), getDataModel().getRotaryAxisPositionY(), getDataModel().getRotaryAxisPositionZ());
		JoglViewerSettings.getInstance().setRotaryAxisPosition(position);
		JoglViewerSettings.getInstance().save();
	}

}
