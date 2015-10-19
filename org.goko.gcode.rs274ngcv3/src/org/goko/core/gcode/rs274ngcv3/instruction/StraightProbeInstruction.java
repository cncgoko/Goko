package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class StraightProbeInstruction extends AbstractInstruction {
	/** X coordinate in the current coordinate system */
	private BigDecimalQuantity<Length> x;
	/** Y coordinate in the current coordinate system */
	private BigDecimalQuantity<Length> y;
	/** Z coordinate in the current coordinate system */
	private BigDecimalQuantity<Length> z;
	
	/** Constructor */
	public StraightProbeInstruction(BigDecimalQuantity<Length> x, BigDecimalQuantity<Length> y, BigDecimalQuantity<Length> z) {
		super(InstructionType.STRAIGHT_PROBE);
		this.x = x;
		this.y = y;
		this.z = z;
	}

//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		// TODO Auto-generated method stub
//
//	}

	/**
	 * @return the x
	 */
	public BigDecimalQuantity<Length> getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public BigDecimalQuantity<Length> getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public BigDecimalQuantity<Length> getZ() {
		return z;
	}
}
