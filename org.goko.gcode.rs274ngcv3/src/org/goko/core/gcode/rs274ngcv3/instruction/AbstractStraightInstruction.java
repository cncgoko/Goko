package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

/**
 * Abstract class for a straight instruction 
 * 
 * @author Psyko
 */
public abstract class AbstractStraightInstruction extends AbstractInstruction {
	/** X coordinate in the current coordinate system */
	private BigDecimalQuantity<Length> x;
	/** Y coordinate in the current coordinate system */
	private BigDecimalQuantity<Length> y;
	/** Z coordinate in the current coordinate system */
	private BigDecimalQuantity<Length> z;
	/** A coordinate in the current coordinate system */
	private BigDecimalQuantity<Angle> a;
	/** B coordinate in the current coordinate system */
	private BigDecimalQuantity<Angle> b;
	/** C coordinate in the current coordinate system  */
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
	public AbstractStraightInstruction(InstructionType type, BigDecimalQuantity<Length> x, BigDecimalQuantity<Length> y, BigDecimalQuantity<Length> z, BigDecimalQuantity<Angle> a, BigDecimalQuantity<Angle> b, BigDecimalQuantity<Angle> c) {		
		super(type);
		this.x = x;
		this.y = y;
		this.z = z;
		this.a = a;
		this.b = b;
		this.c = c;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {		
		context.setPosition(x, y, z, a, b, c);
	}

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

	/**
	 * @param x the x to set
	 */
	public void setX(BigDecimalQuantity<Length> x) {
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(BigDecimalQuantity<Length> y) {
		this.y = y;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(BigDecimalQuantity<Length> z) {
		this.z = z;
	}

	/**
	 * @param a the a to set
	 */
	public void setA(BigDecimalQuantity<Angle> a) {
		this.a = a;
	}

	/**
	 * @param b the b to set
	 */
	public void setB(BigDecimalQuantity<Angle> b) {
		this.b = b;
	}

	/**
	 * @param c the c to set
	 */
	public void setC(BigDecimalQuantity<Angle> c) {
		this.c = c;
	}
	
}
