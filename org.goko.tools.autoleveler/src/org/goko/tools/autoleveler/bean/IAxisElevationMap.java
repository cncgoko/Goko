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
package org.goko.tools.autoleveler.bean;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.math.Segment;
import org.goko.core.math.Tuple6b;

/**
 * Defines an abstract Elevation map used to retrieve a single axis position at given location
 *
 * @author PsyKo
 *
 */
public abstract interface IAxisElevationMap {
	/**
	 * Register a position used for elevation correction
	 * @param patternPosition the theoretical pattern position
	 * @param realPosition the real position
	 * @throws GkException GkException
	 */
	public void addProbedPosition(Tuple6b patternPosition, Tuple6b realPosition) throws GkException;

	/**
	 * Returns the list of probed positions
	 * @return the list of real position
	 */
	public List<Tuple6b> getProbedPositions() throws GkException;

	/**
	 * Return the given position modified with the stored elevation
	 * @param  the position to change
	 * @return Tuple6b
	 * @throws GkException GkException
	 */
	public Tuple6b getCorrectedElevation(final Tuple6b position) throws GkException;
	
	/**
	 * Split the given segment using this elevation map 
	 * @param segment the segment to split
	 * @return the list of resulting segments
	 * @throws GkException GkException
	 */
	public List<Segment> splitSegment(final Segment segment) throws GkException;
}
