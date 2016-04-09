/**
 * 
 */
package org.goko.core.controller.bean;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 8 avr. 2016
 */
public class MachineQuantityValue<Q extends Quantity<Q>> extends MachineValue<Q> {
	private static final GkLog LOG = GkLog.getLogger(MachineQuantityValue.class);
	/**
	 * @param machineValue
	 */
	public MachineQuantityValue(MachineValue<Q> machineValue) {
		super(machineValue);
	}

	/**
	 * @param descriptor
	 * @param value
	 */
	public MachineQuantityValue(MachineValueDefinition descriptor, Q value) {
		super(descriptor, value);
	}

	/**
	 * @param idDescriptor
	 * @param value
	 */
	public MachineQuantityValue(String idDescriptor, Q value) {
		super(idDescriptor, value);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.bean.MachineValue#getStringValue()
	 */
	@Override
	public String getStringValue() {		
		try {
			return GokoPreference.getInstance().format(getValue(), true);
		} catch (GkException e) {
			LOG.error(e);
			return "ERROR";
		}
	}

	public MachineValue<Q> clone(){
		return new MachineQuantityValue<Q>(this);
	}
}
