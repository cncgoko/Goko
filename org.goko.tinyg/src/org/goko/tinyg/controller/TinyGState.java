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
package org.goko.tinyg.controller;

import java.math.BigDecimal;

import javax.vecmath.Point3d;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.bean.MachineValueStore;
import org.goko.core.log.GkLog;

public class TinyGState extends MachineValueStore{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGState.class);

	public TinyGState() {
		super();
		try {
			initValues();
		} catch (GkException e) {
			LOG.error(e);
		}

	}

	private void initValues() throws GkException{
		storeValue(DefaultControllerValues.STATE, "State", "The state of TinyG controller board", MachineState.UNDEFINED);
		storeValue(DefaultControllerValues.POSITION, "Pos", "The position of the machine", new Point3d());
		storeValue(DefaultControllerValues.POSITION_X, "X", "The X position of the machine", new BigDecimal("0.000"));
		storeValue(DefaultControllerValues.POSITION_Y, "Y", "The Y position of the machine", new BigDecimal("0.000"));
		storeValue(DefaultControllerValues.POSITION_Z, "Z", "The Z position of the machine", new BigDecimal("0.000"));
		storeValue(DefaultControllerValues.POSITION_A, "A", "The A position of the machine", new BigDecimal("0.000"));
		storeValue(DefaultControllerValues.VELOCITY, "Velocity", "The current velocity of the machine",new BigDecimal("0.000")));
		storeValue(DefaultControllerValues.SPINDLE_STATE, "Spindle", "The current state of the spindle", new Boolean(false));
		storeValue(DefaultControllerValues.UNITS, "Units", "The units in use",StringUtils.EMPTY));
		storeValue(DefaultControllerValues.COORDINATES, "Coordinates", "The coordinate system",StringUtils.EMPTY);
		storeValue(DefaultControllerValues.DISTANCE_MODE, "Distance mode", "The distance motion setting", StringUtils.EMPTY);
		storeValue(TINYG_BUFFER_COUNT, "TinyG Buffer", "The available space in the planner buffer", 0);
		addListener(this);
	}
}
