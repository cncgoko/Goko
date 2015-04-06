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

import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.tools.centerfinder.bean.CircleCenterFinderResult;

public interface ICenterFinderService extends IGokoService{

	void capturePoint(Point3d point) throws GkException;

	List<Point3d> getCapturedPoint() throws GkException;

	void clearCapturedPoint() throws GkException;

	void removeCapturedPoint(Point3d point) throws GkException;

	CircleCenterFinderResult getCenter(List<Point3d> lstPoints) throws GkException;
}
