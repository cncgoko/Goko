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
package org.goko.core.controller;

import java.util.concurrent.Future;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.gcode.bean.Tuple6b;

/**
 * Interface defining a probing service
 * @author PsyKo
 *
 */
public interface IProbingService extends IGokoService {

	/**
	 * Initiate a probe sequence with the given parameters
	 * @param axis the probing axis
	 * @param feedrate the probing speed
	 * @param maximumPosition the maximum position at which the probing should stop if nothing was detected
	 * @return a future Tuple6b
	 * @throws GkException GkException
	 */
	Future<Tuple6b> probe(EnumControllerAxis axis, double feedrate, double maximumPosition) throws GkException;
}
