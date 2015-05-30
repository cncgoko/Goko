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
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point3d;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.US;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoConfig;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueStore;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.log.GkLog;

public class TinyGState extends MachineValueStore{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGState.class);
	/** The current GCode context */
	private GCodeContext gcodeContext;
	/** The position stored locally for spped reasons... */
	private Tuple6b position;
	/** The offsets */
	private Map<EnumCoordinateSystem, Tuple6b> offsets;
	/** Unit in use by TinyG*/
	private Unit<Length> currentUnit;

	public TinyGState() {
		super();
		try {
			initValues();
		} catch (GkException e) {
			LOG.error(e);
		}

	}

	private void initValues() throws GkException{
		gcodeContext = new GCodeContext();
		position = new Tuple6b().setZero();
		currentUnit = SIPrefix.MILLI(SI.METRE);
		storeValue(TinyG.STATE, "State", "The state of TinyG controller board", MachineState.UNDEFINED);
		//storeValue(TinyG.POSITION, "Pos", "The position of the machine", new Point3d());
		storeValue(TinyG.POSITION_X, "X", "The X position of the machine",  StringUtils.EMPTY);
		storeValue(TinyG.POSITION_Y, "Y", "The Y position of the machine",  StringUtils.EMPTY);
		storeValue(TinyG.POSITION_Z, "Z", "The Z position of the machine",  StringUtils.EMPTY);
		storeValue(TinyG.POSITION_A, "A", "The A position of the machine", new BigDecimal("0.000"));
		storeValue(TinyG.VELOCITY, "Velocity", "The current velocity of the machine",new BigDecimal("0.000"));
		storeValue(TinyG.SPINDLE_STATE, "Spindle", "The current state of the spindle", "false");
		storeValue(TinyG.CONTEXT_UNIT, "Units", "The units in use",StringUtils.EMPTY);
		storeValue(TinyG.CONTEXT_COORD_SYSTEM, "Coordinates", "The coordinate system",StringUtils.EMPTY);
		storeValue(TinyG.CONTEXT_DISTANCE_MODE, "Distance mode", "The distance motion setting", StringUtils.EMPTY);
		storeValue(TinyG.CONTEXT_PLANE, "Plane", "The current working plane", StringUtils.EMPTY);
		storeValue(TinyG.TINYG_BUFFER_COUNT, "TinyG Buffer", "The available space in the planner buffer", 0);
		storeValue(TinyG.CONTEXT_FEEDRATE, "Feedrate", "The current context feedrate", BigDecimal.ZERO);
		addListener(this);
		offsets = new HashMap<EnumCoordinateSystem, Tuple6b>();
		offsets.put(EnumCoordinateSystem.G53, new Tuple6b());
		offsets.put(EnumCoordinateSystem.G54, new Tuple6b());
		offsets.put(EnumCoordinateSystem.G55, new Tuple6b());
		offsets.put(EnumCoordinateSystem.G56, new Tuple6b());
		offsets.put(EnumCoordinateSystem.G57, new Tuple6b());

	}

	/**
	 * @return the availableBuffer
	 * @throws GkException GkException
	 */
	public int getAvailableBuffer() throws GkException {
		return getIntegerValue(TinyG.TINYG_BUFFER_COUNT).getValue();
	}

	/**
	 * @param availableBuffer the availableBuffer to set
	 * @throws GkException GkException
	 */
	public void setAvailableBuffer(int availableBuffer) throws GkException {
		updateValue(TinyG.TINYG_BUFFER_COUNT, availableBuffer);
	}

	/**
	 * @return the state
	 * @throws GkException
	 */
	public MachineState getState() throws GkException {
		MachineState state = MachineState.UNDEFINED;
		MachineValue<MachineState> storedState = getValue(TinyG.STATE, MachineState.class);
		if(storedState != null && storedState.getValue() != null){
			state = storedState.getValue();
		}
		return state;
	}

	/**
	 * @param state the state to set
	 * @throws GkException
	 */
	public void setState(MachineState state) throws GkException {
		updateValue(TinyG.STATE, state);
	}

	public boolean isSpindleOn() throws GkException{
		return StringUtils.equals(getValue(TinyG.SPINDLE_STATE, String.class).getValue(), TinyG.ON);
	}

	public boolean isSpindleOff() throws GkException{
		return StringUtils.equals(getValue(TinyG.SPINDLE_STATE, String.class).getValue(), TinyG.OFF);
	}

	/**
	 * @return the gcodeContext
	 */
	public GCodeContext getGCodeContext() {
		return new GCodeContext(gcodeContext);
	}

	/**
	 * @param gcodeContext the gcodeContext to set
	 */
	public void setGCodeContext(GCodeContext gcodeContext) {
		this.gcodeContext = gcodeContext;
		try {
			updateValue(TinyG.CONTEXT_UNIT, String.valueOf(gcodeContext.getUnit()));
			switch(gcodeContext.getUnit()){
			case INCHES: setCurrentUnit( US.INCH );
			break;
			case MILLIMETERS: setCurrentUnit( SIPrefix.MILLI(SI.METRE));
			break;
			}
			setWorkPosition(gcodeContext.getPosition());
			updateValue(TinyG.CONTEXT_COORD_SYSTEM, String.valueOf(gcodeContext.getCoordinateSystem()));
			updateValue(TinyG.CONTEXT_DISTANCE_MODE, String.valueOf(gcodeContext.getDistanceMode()));
			updateValue(TinyG.CONTEXT_PLANE, String.valueOf(gcodeContext.getPlane()));
			updateValue(TinyG.CONTEXT_FEEDRATE, gcodeContext.getFeedrate());
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/**
	 * @return the velocity
	 * @throws GkException
	 */
	public BigDecimal getVelocity() throws GkException {
		return getValue(TinyG.VELOCITY, BigDecimal.class).getValue();
	}

	/**
	 * @param velocity the velocity to set
	 * @throws GkException
	 */
	public void setVelocity(BigDecimal velocity) throws GkException {
		updateValue(TinyG.VELOCITY, velocity);
	}

	/**
	 * @return the position
	 * @throws GkException
	 */
	public Tuple6b getWorkPosition() throws GkException {
		return new Tuple6b(this.position);
//		return new Tuple6b(getValue(TinyG.POSITION_X, BigDecimal.class).getValue(),
//						   getValue(TinyG.POSITION_Y, BigDecimal.class).getValue(),
//						   getValue(TinyG.POSITION_Z, BigDecimal.class).getValue(),
//						   getValue(TinyG.POSITION_A, BigDecimal.class).getValue(),
//						   null,null);
	}
	protected BigDecimalQuantity<Length> getX() throws GkException{
		return position.getX();
		//return getValue(TinyG.POSITION_X, BigDecimal.class).getValue();
	}
	protected BigDecimalQuantity<Length> getY() throws GkException{
		return position.getY();
		//return getValue(TinyG.POSITION_Y, BigDecimal.class).getValue();
	}
	protected BigDecimalQuantity<Length> getZ() throws GkException{
		return position.getZ();
		//return getValue(TinyG.POSITION_Z, BigDecimal.class).getValue();
	}
	protected BigDecimalQuantity<Angle> getA() throws GkException{
		return position.getA();
		//return getValue(TinyG.POSITION_A, BigDecimal.class).getValue();
	}
	/**
	 * @param position the position to set
	 * @throws GkException
	 */
	public void setWorkPosition(Tuple6b position) throws GkException {
		this.position = new Tuple6b(position);
		String x = GokoConfig.getInstance().format(NumberQuantity.of(new BigDecimal(position.getX().doubleValue()), currentUnit), true);
		String y = GokoConfig.getInstance().format(NumberQuantity.of(new BigDecimal(position.getY().doubleValue()), currentUnit), true);
		String z = GokoConfig.getInstance().format(NumberQuantity.of(new BigDecimal(position.getZ().doubleValue()), currentUnit), true);
		updateValue(TinyG.POSITION_X, x);
		updateValue(TinyG.POSITION_Y, y);
		updateValue(TinyG.POSITION_Z, z);
		updateValue(TinyG.POSITION_A, new BigDecimal(position.getA().doubleValue()).setScale(3, RoundingMode.HALF_DOWN));
	}

	public Tuple6b getCoordinateSystemOffset(EnumCoordinateSystem cs) throws GkException {
		return offsets.get(cs);
	}

	public void setCoordinateSystemOffset(EnumCoordinateSystem cs, Tuple6b offset) throws GkException {
		offsets.put(cs, offset);
	}

	/**
	 * @return the currentUnit
	 */
	protected Unit<Length> getCurrentUnit() {
		return currentUnit;
	}

	/**
	 * @param currentUnit the currentUnit to set
	 */
	protected void setCurrentUnit(Unit<Length> currentUnit) {
		this.currentUnit = currentUnit;
	}
}
