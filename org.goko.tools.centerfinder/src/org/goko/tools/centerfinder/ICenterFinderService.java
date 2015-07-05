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
package org.goko.tools.centerfinder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.tools.centerfinder.bean.CircleCenterFinderResult;

public interface ICenterFinderService extends IGokoService{

	void capturePoint(Tuple6b point) throws GkException;

	List<Tuple6b> getCapturedPoint() throws GkException;

	void clearCapturedPoint() throws GkException;

	void removeCapturedPoint(Tuple6b point) throws GkException;

	CircleCenterFinderResult getCenter(List<Tuple6b> lstPoints) throws GkException;
}
