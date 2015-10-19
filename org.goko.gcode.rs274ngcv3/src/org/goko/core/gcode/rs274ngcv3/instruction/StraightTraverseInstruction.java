package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class StraightTraverseInstruction extends AbstractInstruction {
	/** X coordinate */
	private BigDecimalQuantity<Length> x;
	/** Y coordinate */
	private BigDecimalQuantity<Length> y;
	/** Z coordinate */
	private BigDecimalQuantity<Length> z;
	/** A coordinate */
	private BigDecimalQuantity<Angle> a;
	/** B coordinate */
	private BigDecimalQuantity<Angle> b;
	/** C coordinate */
	private BigDecimalQuantity<Angle> c;
	
	/**
	 * Constructor 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @param a A coordinate
	 * @param b B coordinate
	 * @param c C coordinate
	 */
	public StraightTraverseInstruction(BigDecimalQuantity<Length> x, BigDecimalQuantity<Length> y, BigDecimalQuantity<Length> z, BigDecimalQuantity<Angle> a, BigDecimalQuantity<Angle> b, BigDecimalQuantity<Angle> c) {
		super(InstructionType.STRAIGHT_TRAVERSE);
		this.x = x;
		this.y = y;
		this.z = z;
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setMotionMode(EnumMotionMode.RAPID);
//		//throw new GkTechnicalException(" implementer la mise à jour de la position ");
//	}

	/**
	 * @return the x
	 */
	public BigDecimalQuantity<Length> getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(BigDecimalQuantity<Length> x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public BigDecimalQuantity<Length> getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(BigDecimalQuantity<Length> y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public BigDecimalQuantity<Length> getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(BigDecimalQuantity<Length> z) {
		this.z = z;
	}

	/**
	 * @return the a
	 */
	public BigDecimalQuantity<Angle> getA() {
		return a;
	}

	/**
	 * @return the b
	 */
	public BigDecimalQuantity<Angle> getB() {
		return b;
	}

	/**
	 * @return the c
	 */
	public BigDecimalQuantity<Angle> getC() {
		return c;
	}


}
