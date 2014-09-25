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
package org.goko.tinyg.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.IProbingService;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;

public interface ITinyGControllerFirmwareService extends IControllerService, IProbingService {

	/**
	 * Return the lowest supported firmware version
	 * @return a String
	 * @throws GkException GkException
	 */
	String getMinimalSupportedFirmwareVersion() throws GkException;
	/**
	 * Return the highest supported firmware version
	 * @return a String
	 * @throws GkException GkException
	 */
	String getMaximalSupportedFirmwareVersion() throws GkException;

	TinyGConfiguration getConfiguration() throws GkException;
	void setConfiguration(TinyGConfiguration configuration) throws GkException;
	void refreshConfiguration() throws GkException;

}
