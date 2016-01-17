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
package org.goko.controller.tinyg.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueStore;
import org.goko.core.gcode.rs274ngcv3.context.EnumCoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.math.Tuple6b;

/**
 * Storage of the internal state of the TinyG board
 * 
 * @author PsyKo
 */
public class TinyGState extends MachineValueStore{
	/** The current GCode context */
	private GCodeContext gcodeContext;
	/** The position stored locally for speed reasons... */
	private Tuple6b position;
	/** The offsets */
	private Map<EnumCoordinateSystem, Tuple6b> offsets;
	/** Unit in use by TinyG*/
	private Unit<Length> currentUnit;

	/** 
	 * Constructor 
	 * @throws GkException GkException 
	 */
	public TinyGState() throws GkException {
		super();		
		initializeDefaultValue();
		initializeDefaultContext();
	}

	/**
	 * Initialize the default GCodeContext
	 * @throws GkException GkException 
	 */
	private void initializeDefaultContext() throws GkException {		
		gcodeContext = new GCodeContext();
		gcodeContext.setPosition(new Tuple6b());
		gcodeContext.setOriginOffset(new Tuple6b());
		gcodeContext.setCoordinateSystem(EnumCoordinateSystem.G54);
		gcodeContext.setSelectedToolNumber(0);
		gcodeContext.setActiveToolNumber(0);
		gcodeContext.setUnit(EnumUnit.MILLIMETERS);
		gcodeContext.setMotionMode(EnumMotionMode.RAPID);
		gcodeContext.setFeedrate(BigDecimal.ZERO);
		gcodeContext.setDistanceMode(EnumDistanceMode.ABSOLUTE);
		gcodeContext.setPlane(EnumPlane.XY_PLANE);
	}

	/**
	 * Initialize the defaults values 
	 * @throws GkException GkException
	 */
	private void initializeDefaultValue() throws GkException{
		position = new Tuple6b().setZero();
		currentUnit = SIPrefix.MILLI(Units.METRE);
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
	
	/**
	 * Determine if the spindle is ON
	 * @return <code>true</code> if the spindle is ON, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public boolean isSpindleOn() throws GkException{
		return StringUtils.equals(getValue(TinyG.SPINDLE_STATE, String.class).getValue(), TinyG.ON);
	}

	/**
	 * Determine if the spindle is OFF
	 * @return <code>true</code> if the spindle is OFF, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
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
	 * @throws GkException GkException 
	 */
	public void setGCodeContext(GCodeContext gcodeContext) throws GkException {
		this.gcodeContext = gcodeContext;
		
		updateValue(TinyG.CONTEXT_UNIT, String.valueOf(gcodeContext.getUnit()));
		setCurrentUnit( gcodeContext.getUnit().getUnit());
		setWorkPosition(gcodeContext.getPosition());
		updateValue(TinyG.CONTEXT_COORD_SYSTEM, String.valueOf(gcodeContext.getCoordinateSystem()));
		updateValue(TinyG.CONTEXT_DISTANCE_MODE, String.valueOf(gcodeContext.getDistanceMode()));
		updateValue(TinyG.CONTEXT_PLANE, String.valueOf(gcodeContext.getPlane()));
		updateValue(TinyG.CONTEXT_FEEDRATE, gcodeContext.getFeedrate());		
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
	}
	
	/**
	 * Access the X position 
	 * @return {@link BigDecimalQuantity} the X position
	 * @throws GkException GkException
	 */
	protected Length getX() throws GkException{
		return position.getX();
	}
	
	/**
	 * Access the Y position 
	 * @return {@link BigDecimalQuantity} the Y position
	 * @throws GkException GkException
	 */
	protected Length getY() throws GkException{
		return position.getY();
	}
	
	/**
	 * Access the Z position 
	 * @return {@link BigDecimalQuantity} the Z position
	 * @throws GkException GkException
	 */
	protected Length getZ() throws GkException{
		return position.getZ();
	}
	
	/**
	 * Access the A position 
	 * @return {@link BigDecimalQuantity} the A position
	 * @throws GkException GkException
	 */
	protected Angle getA() throws GkException{
		return position.getA();
	}
	
	/**
	 * Sets the work position
	 * @param position the position to set
	 * @throws GkException GkException
	 */
	public void setWorkPosition(Tuple6b position) throws GkException {
		this.position = new Tuple6b(position);		
		String x = GokoPreference.getInstance().format(position.getX(), true, false, currentUnit);
		String y = GokoPreference.getInstance().format(position.getY(), true, false, currentUnit);
		String z = GokoPreference.getInstance().format(position.getZ(), true, false, currentUnit);
		updateValue(TinyG.POSITION_X, x);
		updateValue(TinyG.POSITION_Y, y);
		updateValue(TinyG.POSITION_Z, z);
		updateValue(TinyG.POSITION_A, new BigDecimal(position.getA().doubleValue(AngleUnit.DEGREE_ANGLE)).setScale(3, RoundingMode.HALF_DOWN));
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
