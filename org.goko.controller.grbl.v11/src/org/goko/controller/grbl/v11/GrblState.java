/**
 * 
 */
package org.goko.controller.grbl.v11;

import org.goko.controller.grbl.commons.AbstractGrblState;
import org.goko.controller.grbl.v11.bean.GrblMachineState;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.math.Tuple6b;

/**
 * Grbl 1.1 state object
 * 
 * @author Psyko
 * @date 5 avr. 2017
 */
public class GrblState extends AbstractGrblState<GrblMachineState> {
	/** The current work coordinate offset */
	private Tuple6b currentWorkCoordinateOffset;
	
	/**
	 * Cosntructor
	 * @throws GkException
	 */
	public GrblState() throws GkException {
		super();
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblState#getStateClass()
	 */
	@Override
	protected Class<GrblMachineState> getStateClass() {
		return GrblMachineState.class;
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.AbstractGrblState#initializeDefaultValue()
	 */
	@Override
	protected void initializeDefaultValue() throws GkException {		
		super.initializeDefaultValue();
		storeValue(DefaultControllerValues.STATE, "State", "The state of Grbl controller board", GrblMachineState.UNDEFINED);
		
		storeValue(Grbl.MachineValue.FEED_OVERRIDE, "Feed ov.", "Current Feed override factor", 100);
		storeValue(Grbl.MachineValue.RAPID_OVERRIDE, "Rapid ov.", "Current Rapid override factor", 100);
		storeValue(Grbl.MachineValue.SPINDLE_OVERRIDE, "Spindle ov.", "Current Spindle override factor", 100);
		
		storeValue(Grbl.MachineValue.WCO_X, "WCO X", "The current X work coordinate offset",  Length.ZERO);
		storeValue(Grbl.MachineValue.WCO_Y, "WCO Y", "The current Y work coordinate offset",  Length.ZERO);
		storeValue(Grbl.MachineValue.WCO_Z, "WCO Z", "The current Z work coordinate offset",  Length.ZERO);
	}

	/**
	 * @return the overrideFeed
	 */
	public Integer getOverrideFeed() throws GkException {
		return getValue(Grbl.MachineValue.FEED_OVERRIDE, Integer.class).getValue();
	}

	/**
	 * @param overrideFeed the overrideFeed to set
	 */
	public void setOverrideFeed(Integer overrideFeed) throws GkException {
		updateValue(Grbl.MachineValue.FEED_OVERRIDE, overrideFeed);
	}

	/**
	 * @return the overrideRapid
	 */
	public Integer getOverrideRapid() throws GkException {
		return getValue(Grbl.MachineValue.RAPID_OVERRIDE, Integer.class).getValue();
	}

	/**
	 * @param overrideRapid the overrideRapid to set
	 */
	public void setOverrideRapid(Integer overrideRapid) throws GkException {
		updateValue(Grbl.MachineValue.RAPID_OVERRIDE, overrideRapid);
	}

	/**
	 * @return the overrideSpindle
	 */
	public Integer getOverrideSpindle() throws GkException {
		return getValue(Grbl.MachineValue.SPINDLE_OVERRIDE, Integer.class).getValue();
	}

	/**
	 * @param overrideSpindle the overrideSpindle to set
	 * @throws GkException 
	 */
	public void setOverrideSpindle(Integer overrideSpindle) throws GkException {
		updateValue(Grbl.MachineValue.SPINDLE_OVERRIDE, overrideSpindle);
	}

	/**
	 * Sets the current work coordinate offset
	 * @param wco the offset to set
	 * @throws GkException GkException
	 */
	public void setCurrentWorkCoordinateOffset(Tuple6b wco) throws GkException {
		this.currentWorkCoordinateOffset = new Tuple6b(wco);		
		updateValue(Grbl.MachineValue.WCO_X, wco.getX());
		updateValue(Grbl.MachineValue.WCO_Y, wco.getY());
		updateValue(Grbl.MachineValue.WCO_Z, wco.getZ());
	}

	/**
	 * Returns the current work coordinate offset
	 * @return the currentWorkCoordinateOffset
	 */
	public Tuple6b getCurrentWorkCoordinateOffset() {
		return currentWorkCoordinateOffset;
	}
	
	/**
	 * @return the velocity
	 * @throws GkException
	 */
	public Integer getSpindleSpeed() throws GkException {
		return getValue(DefaultControllerValues.SPINDLE_SPEED, Integer.class).getValue();
	}

	/**
	 * @param velocity the velocity to set
	 * @throws GkException
	 */
	public void setSpindleSpeed(Integer velocity) throws GkException {
		updateValue(DefaultControllerValues.SPINDLE_SPEED, velocity);
	}
}
