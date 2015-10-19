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
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeProvider;


/**
 * Interface definition for execution time service.
 *
 * @author PsyKo
 *
 */
public interface IGCodeExecutionTimeService extends IGokoService{
	/**
	 * Evaluate the execution time of the given provider
	 * @param provider the provider
	 * @return a long giving the seconds required to execute the code
	 * @throws GkException GkException
	 */
	double evaluateExecutionTime(IGCodeProvider provider) throws GkException;
}
