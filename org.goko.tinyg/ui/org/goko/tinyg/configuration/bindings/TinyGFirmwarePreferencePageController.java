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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.inject.Inject;

import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.tinyg.service.ITinyGControllerServiceSelector;

public class TinyGFirmwarePreferencePageController extends AbstractController<TinyGFirmwarePreferencePageModel> implements PropertyChangeListener {

	@Inject
	ITinyGControllerServiceSelector serviceSelector;

	public TinyGFirmwarePreferencePageController() {
		super(new TinyGFirmwarePreferencePageModel());
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() throws GkException {
		getDataModel().setFirmwareVersion(serviceSelector.getFirmware());

	}

	public void performApply() throws GkException {
		serviceSelector.setFirmware(getDataModel().getFirmwareVersion());

	}

}
