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
package org.goko.core.controller.listener;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.MachineValue;

/**
 * Interface describing a machine state listener.
 * It gets notified by the controller service each time an internal state of the controller changes.
 *
 * @author PsyKo
 *
 */
public interface IMachineStateListener {

	void onMachineStateValueChange(List<MachineValue> lstMachineStateValue) throws GkException;
}
