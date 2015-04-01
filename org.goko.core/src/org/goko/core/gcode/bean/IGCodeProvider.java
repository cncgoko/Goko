/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.gcode.bean;

import java.util.List;

import org.goko.core.common.exception.GkException;

/**
 * Generic interface for a GCodeCommand provider
 * @author PsyKo
 *
 */
public interface IGCodeProvider {
	/**
	 * Return the id of the provider
	 * @return the id pf the provider
	 */
	Integer getId();
	/**
	 * Setter for the id
	 * @param id the id to set
	 */
	void setId(Integer  id);
	/**
	 * Returns the name of this provider
	 * @return the name of this provider
	 */
	String getName();

	/**
	 * Return the list of GCodeLine provided by this provider
	 * @return a {@link List} of {@link GCodeLine}
	 */
	List<GCodeCommand> getGCodeCommands();

	GCodeCommand getCommandById(Integer id) throws GkException;

	BoundingTuple6b getBounds();
}
