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
package org.goko.tinyg.configuration.bindings;

import javax.inject.Inject;

import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.tinyg.controller.prefs.TinyGPreferences;
import org.goko.tinyg.service.ITinyGControllerServiceSelector;

public class TinyGFirmwarePreferencePageController extends AbstractController<TinyGFirmwarePreferencePageModel> {

	@Inject
	ITinyGControllerServiceSelector serviceSelector;

	public TinyGFirmwarePreferencePageController() {
		super(new TinyGFirmwarePreferencePageModel());
	}


	@Override
	public void initialize() throws GkException {
		getDataModel().setHomingEnabledAxisX(TinyGPreferences.getInstance().isHomingEnabledAxisX());
		getDataModel().setHomingEnabledAxisY(TinyGPreferences.getInstance().isHomingEnabledAxisY());
		getDataModel().setHomingEnabledAxisZ(TinyGPreferences.getInstance().isHomingEnabledAxisZ());
		getDataModel().setHomingEnabledAxisA(TinyGPreferences.getInstance().isHomingEnabledAxisA());

	}

	public void performApply() throws GkException{
		TinyGPreferences.getInstance().setHomingEnabledAxisX(getDataModel().isHomingEnabledAxisX());
		TinyGPreferences.getInstance().setHomingEnabledAxisY(getDataModel().isHomingEnabledAxisY());
		TinyGPreferences.getInstance().setHomingEnabledAxisZ(getDataModel().isHomingEnabledAxisZ());
		TinyGPreferences.getInstance().setHomingEnabledAxisA(getDataModel().isHomingEnabledAxisA());
		TinyGPreferences.getInstance().save();
	}
}
