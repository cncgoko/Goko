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

import java.math.BigDecimal;

/**
 * Defines a version range
 *
 * @author PsyKo
 *
 */
public class VersionRange {
	/** Minimum version */
	private BigDecimal minVersion;
	/** Maximum version */
	private BigDecimal maxVersion;

	/**
	 * Constructor
	 * @param minVersion minimum version
	 * @param maxVersion maximum version
	 */
	public VersionRange(String minVersion, String maxVersion){
		this.minVersion = new BigDecimal(minVersion);
		this.maxVersion = new BigDecimal(maxVersion);
	}

	/**
	 * Determines if the given version is in this range (min and max version included)
	 * @param version the version to check
	 * @return <code>true</code> if the version is inside this range, <code>false</code> otherwise
	 */
	public boolean contains(String version){
		BigDecimal vers = new BigDecimal(version);
		return minVersion.compareTo(vers) <= 0 && maxVersion.compareTo(vers) >= 0;
	}
}
