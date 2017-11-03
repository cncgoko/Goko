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
package org.goko.controller.grbl.commons;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueStore;
import org.goko.core.controller.bean.OnOffBoolean;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
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
public abstract class AbstractGrblState<S extends MachineState> extends MachineValueStore{
	/** The current GCode context */
	private GCodeContext gcodeContext;
	/** The position stored locally for speed reasons... */
	private Tuple6b position;
	/** The offsets */
	private Map<ICoordinateSystem, Tuple6b> offsets;
	/** Unit in use by TinyG*/
	private Unit<Length> currentUnit;
	/** State of active polling */
	private boolean activePolling;

	/** 
	 * Constructor 
	 * @throws GkException GkException 
	 */
	public AbstractGrblState() throws GkException {
		super();		
		initializeDefaultValue();
		initializeDefaultContext();
	}

	/**
	 * Initialize the default GCodeContext
	 * @throws GkException GkException 
	 */
	protected void initializeDefaultContext() throws GkException {		
		gcodeContext = new GCodeContext();
		gcodeContext.setPosition(new Tuple6b());
		gcodeContext.setMachinePosition(new Tuple6b());
		gcodeContext.setOriginOffset(new Tuple6b());
		gcodeContext.setCoordinateSystem(CoordinateSystem.G54);
		gcodeContext.setSelectedToolNumber(0);
		gcodeContext.setActiveToolNumber(0);
		gcodeContext.setUnit(EnumUnit.MILLIMETERS);
		gcodeContext.setMotionMode(EnumMotionMode.RAPID);
		gcodeContext.setFeedrate(Speed.ZERO);
		gcodeContext.setDistanceMode(EnumDistanceMode.ABSOLUTE);
		gcodeContext.setPlane(EnumPlane.XY_PLANE);
	}

	/**
	 * Initialize the defaults values 
	 * @throws GkException GkException
	 */
	protected void initializeDefaultValue() throws GkException{
		position = new Tuple6b().setZero();
		currentUnit = SIPrefix.MILLI(Units.METRE);
	
		storeValue(DefaultControllerValues.POSITION_X, "X", "The X work position",  Length.ZERO);
		storeValue(DefaultControllerValues.POSITION_Y, "Y", "The Y work position",  Length.ZERO);
		storeValue(DefaultControllerValues.POSITION_Z, "Z", "The Z work position",  Length.ZERO);
		
		storeValue(DefaultControllerValues.MACHINE_POSITION_X, "Machine X", "The X machine position in absolute coord system",  Length.ZERO);
		storeValue(DefaultControllerValues.MACHINE_POSITION_Y, "Machine Y", "The Y machine position in absolute coord system",  Length.ZERO);
		storeValue(DefaultControllerValues.MACHINE_POSITION_Z, "Machine Z", "The Z machine position in absolute coord system",  Length.ZERO);
				
		storeValue(DefaultControllerValues.VELOCITY				, "Velocity", "The current velocity of the machine", Speed.ZERO);
		storeValue(DefaultControllerValues.SPINDLE_STATE		, "Spindle", "The current state of the spindle", new OnOffBoolean(false));
		storeValue(DefaultControllerValues.SPINDLE_SPEED		, "Spindle speed", "Current Spindle RPM", 0);
		storeValue(DefaultControllerValues.CONTEXT_UNIT			, "Units", "The units in use", StringUtils.EMPTY);
		storeValue(DefaultControllerValues.CONTEXT_COORD_SYSTEM , "Coordinates", "The coordinate system",StringUtils.EMPTY);
		storeValue(DefaultControllerValues.CONTEXT_DISTANCE_MODE, "Distance mode", "The distance motion setting", StringUtils.EMPTY);
		storeValue(DefaultControllerValues.CONTEXT_PLANE, "Plane", "The current working plane", StringUtils.EMPTY);		
		storeValue(DefaultControllerValues.CONTEXT_FEEDRATE, "Feedrate", "The current context feedrate", Speed.ZERO);
		
		storeValue(DefaultControllerValues.TOOL_OFFSET, "Tool offset", "The current tool length offset", Length.ZERO);
		
		storeValue(Grbl.GRBL_AVAILABLE_RX_BUFFER, "Grbl RX Buffer", "The available space in Grbl RX buffer", 0);
		storeValue(Grbl.GRBL_AVAILABLE_PLANNER_BUFFER, "Grbl Planner Buffer", "The available space in Grbl planner buffer", 0);
		
		
		offsets = new HashMap<ICoordinateSystem, Tuple6b>();
		offsets.put(CoordinateSystem.G53, new Tuple6b());
		offsets.put(CoordinateSystem.G54, new Tuple6b());
		offsets.put(CoordinateSystem.G55, new Tuple6b());
		offsets.put(CoordinateSystem.G56, new Tuple6b());
		offsets.put(CoordinateSystem.G57, new Tuple6b());
		
		setWorkPosition(new Tuple6b());
		setMachinePosition(new Tuple6b());
	}

	/**
	 * @return the state
	 * @throws GkException
	 */
	public S getState() throws GkException {
		S state = null;
		MachineValue<S> storedState = getValue(DefaultControllerValues.STATE, getStateClass());
		if(storedState != null && storedState.getValue() != null){
			state = storedState.getValue();
		}
		return state;
	}

	protected abstract Class<S> getStateClass();
	
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
		
		updateValue(DefaultControllerValues.CONTEXT_UNIT, String.valueOf(gcodeContext.getUnit()));
		setCurrentUnit( gcodeContext.getUnit().getUnit());
		setWorkPosition(gcodeContext.getPosition());
		setMachinePosition(gcodeContext.getMachinePosition());
		if(gcodeContext.getCoordinateSystem() != null){
			updateValue(DefaultControllerValues.CONTEXT_COORD_SYSTEM, gcodeContext.getCoordinateSystem().getCode());
		}
		updateValue(DefaultControllerValues.CONTEXT_DISTANCE_MODE, String.valueOf(gcodeContext.getDistanceMode()));
		updateValue(DefaultControllerValues.CONTEXT_PLANE, String.valueOf(gcodeContext.getPlane()));		
		updateValue(DefaultControllerValues.CONTEXT_FEEDRATE, gcodeContext.getFeedrate());		
	}

	/**
	 * @return the velocity
	 * @throws GkException
	 */
	public Speed getVelocity() throws GkException {
		return getValue(DefaultControllerValues.VELOCITY, Speed.class).getValue();
	}

	/**
	 * @param velocity the velocity to set
	 * @throws GkException
	 */
	public void setVelocity(Speed velocity) throws GkException {
		updateValue(DefaultControllerValues.VELOCITY, velocity);
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
		updateValue(DefaultControllerValues.POSITION_X, position.getX());
		updateValue(DefaultControllerValues.POSITION_Y, position.getY());
		updateValue(DefaultControllerValues.POSITION_Z, position.getZ());
	}
	
	/**
	 * Sets the machine position
	 * @param position the position to set
	 * @throws GkException GkException
	 */
	public void setMachinePosition(Tuple6b position) throws GkException {				
		updateValue(DefaultControllerValues.MACHINE_POSITION_X, position.getX());
		updateValue(DefaultControllerValues.MACHINE_POSITION_Y, position.getY());
		updateValue(DefaultControllerValues.MACHINE_POSITION_Z, position.getZ());
	}

	public Tuple6b getCoordinateSystemOffset(ICoordinateSystem cs) throws GkException {
		return offsets.get(cs) == null ? new Tuple6b() : new Tuple6b( offsets.get(cs) );
	}

	public void setCoordinateSystemOffset(ICoordinateSystem cs, Tuple6b offset) throws GkException {
		offsets.put(cs, new Tuple6b( offset ));
		if(gcodeContext != null){
			gcodeContext.setCoordinateSystemData(cs, offset);
		}
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

	/**
	 * @return the activePolling
	 */
	public boolean isActivePolling() {
		return activePolling;
	}

	/**
	 * @param activePolling the activePolling to set
	 */
	public void setActivePolling(boolean activePolling) {
		this.activePolling = activePolling;
	}
	
	/**
	 * Setter for the state
	 * @param state the state to set
	 * @throws GkException GkException
	 */
	public void setState(S state)throws GkException{
		updateValue(DefaultControllerValues.STATE, state);
	}
	

	public void setAvailableRxBuffer(int usedGrblBuffer) throws GkException {
		updateValue(Grbl.GRBL_AVAILABLE_RX_BUFFER, usedGrblBuffer);
	}

	public int getAvailableRxBuffer() throws GkException {
		return getValue(Grbl.GRBL_AVAILABLE_RX_BUFFER, Integer.class).getValue();
	}
	
	public EnumUnit getContextUnit(){
		return getGCodeContext().getUnit();
	}
	
	public void setAvailablePlannerBuffer(int usedGrblBuffer) throws GkException {
		updateValue(Grbl.GRBL_AVAILABLE_PLANNER_BUFFER, usedGrblBuffer);
	}

	public int getAvailablePlannerBuffer() throws GkException {
		return getValue(Grbl.GRBL_AVAILABLE_PLANNER_BUFFER, Integer.class).getValue();
	}

	/**
	 * @return the toolLengthOffset
	 * @throws GkException GkException 
	 */
	public Length getToolLengthOffset() throws GkException {
		return getValue(Grbl.TOOL_OFFSET, Length.class).getValue();
	}

	/**
	 * @param toolLengthOffset the toolLengthOffset to set
	 * @throws GkException GkException 
	 */
	public void setToolLengthOffset(Length toolLengthOffset) throws GkException {
		updateValue(Grbl.TOOL_OFFSET, toolLengthOffset);
	}
	
}
