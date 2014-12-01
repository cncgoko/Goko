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
package org.goko.grbl.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueStore;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandDistanceMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandPlane;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandUnit;
import org.goko.core.log.GkLog;

public class GrblState extends MachineValueStore{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GrblState.class);
	private Map<EnumCoordinateSystem, Tuple6b> offsets;
	private GCodeContext currentContext;

	public GrblState() {
		super();
		offsets = new HashMap<EnumCoordinateSystem, Tuple6b>();
		currentContext = new GCodeContext();
		try {
			initValues();
		} catch (GkException e) {
			LOG.error(e);
		}

	}

	private void initValues() throws GkException{
		storeValue(DefaultControllerValues.STATE, "State", "The state of Grbl controller board", MachineState.UNDEFINED);
		storeValue(Grbl.MACHINE_POSITION_X, "X", "The X position of the machine", new BigDecimal("0.000"));
		storeValue(Grbl.MACHINE_POSITION_Y, "Y", "The Y position of the machine", new BigDecimal("0.000"));
		storeValue(Grbl.MACHINE_POSITION_Z, "Z", "The Z position of the machine", new BigDecimal("0.000"));
		storeValue(Grbl.WORK_POSITION_X, "Wor. X", "The X work position", new BigDecimal("0.000"));
		storeValue(Grbl.WORK_POSITION_Y, "Wor. Y", "The Y work position", new BigDecimal("0.000"));
		storeValue(Grbl.WORK_POSITION_Z, "Wor. Z", "The Z work position", new BigDecimal("0.000"));
		storeValue(Grbl.GRBL_USED_BUFFER, "Grbl Buffer", "The space used in Grbl buffer", 0);
		storeValue(Grbl.CONTEXT_FEEDRATE, "Feedrate", "The current feedrate", new BigDecimal("0.000"));
		storeValue(Grbl.CONTEXT_PLANE, "Plane", "The current plane", EnumGCodeCommandPlane.XY_PLANE);
		storeValue(Grbl.CONTEXT_MOTION_MODE, "Motion mode", "The current motion mode", EnumGCodeCommandMotionMode.RAPID);
		storeValue(Grbl.CONTEXT_UNIT, "Units", "The current units", EnumGCodeCommandUnit.MILLIMETERS);
		storeValue(Grbl.CONTEXT_DISTANCE_MODE, "Distance mode", "The current distance mode", EnumGCodeCommandDistanceMode.ABSOLUTE);
		storeValue(Grbl.CONTEXT_COORD_SYSTEM, "Coordinate system", "The current coordinate system", EnumCoordinateSystem.G53);

		// TODO REMOVE OFFSETS FROM VALUE STORE
		offsets.put(EnumCoordinateSystem.G53, new Tuple6b());
		offsets.put(EnumCoordinateSystem.G54, new Tuple6b());
		offsets.put(EnumCoordinateSystem.G55, new Tuple6b());
		offsets.put(EnumCoordinateSystem.G56, new Tuple6b());
		offsets.put(EnumCoordinateSystem.G57, new Tuple6b());

		addListener(this);
	}

	/**
	 * Return the machine position in the machine space coordinate
	 * @return the position of the tool
	 * @throws GkException GkException
	 */
	public Tuple6b getMachinePosition() throws GkException{
		MachineValue<BigDecimal> x = getBigDecimalValue(Grbl.MACHINE_POSITION_X);
		MachineValue<BigDecimal> y = getBigDecimalValue(Grbl.MACHINE_POSITION_Y);
		MachineValue<BigDecimal> z = getBigDecimalValue(Grbl.MACHINE_POSITION_Z);
		return new Tuple6b(x.getValue(), y.getValue(), z.getValue());
	}
	/**
	 * Sets the machine position in the machine space coordinate
	 * @param position the position
	 * @throws GkException GkException
	 */
	public void setMachinePosition(Tuple6b position) throws GkException{
		if(position != null){
			if(position.getX() != null){
				updateValue(Grbl.MACHINE_POSITION_X, position.getX());
			}
			if(position.getY() != null){
				updateValue(Grbl.MACHINE_POSITION_Y, position.getY());
			}
			if(position.getZ() != null){
				updateValue(Grbl.MACHINE_POSITION_Z, position.getZ());
			}
		}
	}

	/**
	 * Return the machine position in the machine space coordinate
	 * @return the position of the tool
	 * @throws GkException GkException
	 */
	public Tuple6b getWorkPosition() throws GkException{
		MachineValue<BigDecimal> x = getBigDecimalValue(Grbl.WORK_POSITION_X);
		MachineValue<BigDecimal> y = getBigDecimalValue(Grbl.WORK_POSITION_Y);
		MachineValue<BigDecimal> z = getBigDecimalValue(Grbl.WORK_POSITION_Z);
		return new Tuple6b(x.getValue(), y.getValue(), z.getValue());
	}
	/**
	 * Sets the machine position in the machine space coordinate
	 * @param position the position
	 * @throws GkException GkException
	 */
	public void setWorkPosition(Tuple6b position) throws GkException{
		if(position != null){
			if(position.getX() != null){
				updateValue(Grbl.WORK_POSITION_X, position.getX());
			}
			if(position.getY() != null){
				updateValue(Grbl.WORK_POSITION_Y, position.getY());
			}
			if(position.getZ() != null){
				updateValue(Grbl.WORK_POSITION_Z, position.getZ());
			}
		}
		if(position != null && currentContext != null){
			currentContext.setPosition(position);
		}
	}
	public int getUsedBuffer() throws GkException{
		MachineValue<Integer> buffer = getIntegerValue(Grbl.GRBL_USED_BUFFER);
		return buffer.getValue();
	}
	/**
	 * Generic method for setting an offset
	 * @param id id
	 * @param value value
	 * @throws GkException GkException
	 */
	protected void setOffset(EnumCoordinateSystem name, Tuple6b value) throws GkException{
		offsets.put(name, value);
	}
	/**
	 * Generic method for getting an offset
	 * @param id id
	 * @throws GkException GkException
	 */
	public Tuple6b getOffset(EnumCoordinateSystem cs) throws GkException{
		if(offsets.containsKey(cs)){
			return offsets.get(cs);		}

		return null;
	}
	/**
	 * Return G54 offset
	 * @return the offset
	 */
	public Tuple6b getG54Offset() throws GkException{
		return getOffset(EnumCoordinateSystem.G54);
	}
	/**
	 * Sets G54 offset
	 * @param the offset
	 */
	public void setG54Offset(Tuple6b offset) throws GkException{
		setOffset(EnumCoordinateSystem.G54, offset);
	}
	/**
	 * Return G55 offset
	 * @return the offset
	 */
	public Tuple6b getG55Offset()throws GkException{
		return getOffset(EnumCoordinateSystem.G55);
	}
	/**
	 * Sets G55 offset
	 * @param the offset
	 */
	public void setG55Offset(Tuple6b offset)throws GkException{
		setOffset(EnumCoordinateSystem.G55, offset);
	}
	/**
	 * Return G54 offset
	 * @return the offset
	 */
	public Tuple6b getG56Offset()throws GkException{
		return getOffset(EnumCoordinateSystem.G56);
	}
	/**
	 * Sets G56 offset
	 * @param the offset
	 */
	public void setG56Offset(Tuple6b offset)throws GkException{
		setOffset(EnumCoordinateSystem.G56, offset);
	}
	/**
	 * Return G57 offset
	 * @return the offset
	 */
	public Tuple6b getG57Offset() throws GkException{
		return getOffset(EnumCoordinateSystem.G57);
	}
	/**
	 * Sets G57 offset
	 * @param the offset
	 */
	public void setG57Offset(Tuple6b offset)throws GkException{
		setOffset(EnumCoordinateSystem.G57, offset);
	}
	/**
	 * Setter for the state
	 * @param state the state to set
	 * @throws GkException GkException
	 */
	public void setState(MachineState state)throws GkException{
		updateValue(Grbl.STATE, state);
	}
	/**
	 * Getter for the state
	 * @return the State
	 * @throws GkException
	 */
	public MachineState getState()throws GkException{
		return getValue(Grbl.STATE, MachineState.class).getValue();
	}

	public void setUsedGrblBuffer(int usedGrblBuffer) throws GkException {
		updateValue(Grbl.GRBL_USED_BUFFER, usedGrblBuffer);
	}

	public int getUsedGrblBuffer() throws GkException {
		return getValue(Grbl.GRBL_USED_BUFFER, Integer.class).getValue();
	}

	/**
	 * @return the currentContext
	 */
	protected GCodeContext getCurrentContext() {
		return currentContext;
	}

	/**
	 * @param currentContext the currentContext to set
	 */
	protected void setCurrentContext(GCodeContext currentContext) {
		this.currentContext = currentContext;
		try {
			updateValue(Grbl.CONTEXT_FEEDRATE, currentContext.getFeedrate());
			updateValue(Grbl.CONTEXT_COORD_SYSTEM, currentContext.getCoordinateSystem()); //g54 ne sont pas trouvés
			updateValue(Grbl.CONTEXT_DISTANCE_MODE, currentContext.getDistanceMode());
			updateValue(Grbl.CONTEXT_MOTION_MODE, currentContext.getMotionMode());
			updateValue(Grbl.CONTEXT_PLANE, currentContext.getPlane());
			updateValue(Grbl.CONTEXT_UNIT, currentContext.getUnit());
		} catch (GkException e) {
			LOG.error(e);
		}
	}
}

