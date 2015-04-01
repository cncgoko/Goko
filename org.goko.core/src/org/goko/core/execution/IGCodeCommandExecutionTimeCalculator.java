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
package org.goko.core.execution;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;

/**
 * Estimates the execution time for a given GCode command
 *
 * @author PsyKo
 *
 * @param <T>
 */
public interface IGCodeCommandExecutionTimeCalculator<T extends GCodeCommand> {

	/**
	 * Compute the execution time
	 * @param provider the provider to evaluate
	 * @return long describing the number of seconds
	 * @throws GkException GkException
	 */
	double evaluateExecutionTime(T command, GCodeContext context) throws GkException;
}
