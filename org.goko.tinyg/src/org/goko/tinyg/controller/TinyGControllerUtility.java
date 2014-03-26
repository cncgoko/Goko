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

import javax.vecmath.Point3d;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.log.GkLog;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.tinyg.controller.configuration.TinyGGroupSettings;
import org.goko.tinyg.controller.configuration.TinyGSetting;
import org.goko.tinyg.json.TinyGJsonUtils;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class TinyGControllerUtility {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGControllerUtility.class);

	public TinyGControllerUtility() {

	}

	public final static TinyGConfiguration getConfigurationCopy(final TinyGConfiguration baseCfg) throws GkException{
		TinyGConfiguration copy = new TinyGConfiguration();
		for(TinyGGroupSettings group : baseCfg.getGroups()){
			for(TinyGSetting<?> setting : group.getSettings()){
				copy.setSetting(group.getGroupIdentifier(), setting.getIdentifier(), setting.getValue());
			}
		}
		return baseCfg;
	}

	/**
	 * Handling configuration response from TinyG
	 * @return {@link JsonValue}
	 */
	protected static void handleConfigurationModification(TinyGConfiguration configuration, JsonObject responseEnvelope) throws GkException {
		TinyGJsonUtils.setConfiguration(configuration, responseEnvelope, StringUtils.EMPTY);
		LOG.debug("TinyG handleConfigModification "+responseEnvelope.toString());
	}

	/**
	 * Transform a GCode command to a JsonValue
	 * @param command the command to transform
	 * @return {@link JsonValue}
	 */
	protected static JsonValue toJson(GCodeCommand command){
		JsonObject value = new JsonObject();
		value.add(TinyGJsonUtils.GCODE_COMMAND, StringUtils.lowerCase(command.toString()));
		return value;
	}


	protected static Point3d updatePosition(Point3d lastKnownPosition, JsonObject statusReport){
		Point3d newPosition = new Point3d(lastKnownPosition);
		JsonValue newPositionX = statusReport.get(TinyGJsonUtils.STATUS_REPORT_POSITION_X);
		JsonValue newPositionY = statusReport.get(TinyGJsonUtils.STATUS_REPORT_POSITION_Y);
		JsonValue newPositionZ = statusReport.get(TinyGJsonUtils.STATUS_REPORT_POSITION_Z);
		if(newPositionX != null){
			newPosition.x = newPositionX.asBigDecimal().doubleValue();
		}
		if(newPositionY != null){
			newPosition.y = newPositionY.asBigDecimal().doubleValue();
		}
		if(newPositionZ != null){
			newPosition.z = newPositionZ.asBigDecimal().doubleValue();
		}
		return newPosition;
	}
	/**
	 * Convert Integer value to Machine State
	 * @param stateCode the int value
	 * @return {@link MachineState} object
	 */
	protected static MachineState getState(Integer stateCode){
		switch(stateCode){
		case 0: return MachineState.INITIALIZING;
		case 1: return MachineState.READY;
		case 2: return MachineState.ALARM;
		case 3: return MachineState.PROGRAM_STOP;
		case 4: return MachineState.PROGRAM_END;
		case 5: return MachineState.MOTION_RUNNING;
		case 6: return MachineState.MOTION_HOLDING;
		case 7: return MachineState.PROBE_CYCLE;
		case 8: return MachineState.RUNNING;
		case 9: return MachineState.HOMING;
		default: return MachineState.INITIALIZING;
		}

	}
}
