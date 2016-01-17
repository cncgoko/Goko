package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

/**
 * Abstract class for a straight instruction 
 * 
 * @author Psyko
 */
public abstract class AbstractStraightInstruction extends AbstractInstruction {
	/** X coordinate in the current coordinate system */
	private Length x;
	/** Y coordinate in the current coordinate system */
	private Length y;
	/** Z coordinate in the current coordinate system */
	private Length z;
	/** A coordinate in the current coordinate system */
	private Angle a;
	/** B coordinate in the current coordinate system */
	private Angle b;
	/** C coordinate in the current coordinate system  */
	private Angle c;

	/**
	 * Constructor 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @param a A coordinate
	 * @param b B coordinate
	 * @param c C coordinate
	 */
	public AbstractStraightInstruction(InstructionType type, Length x, Length y, Length z, Angle a, Angle b, Angle c) {		
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
	public Length getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public Length getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public Length getZ() {
		return z;
	}

	/**
	 * @return the a
	 */
	public Angle getA() {
		return a;
	}

	/**
	 * @return the b
	 */
	public Angle getB() {
		return b;
	}

	/**
	 * @return the c
	 */
	public Angle getC() {
		return c;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(Length x) {
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Length y) {
		this.y = y;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(Length z) {
		this.z = z;
	}

	/**
	 * @param a the a to set
	 */
	public void setA(Angle a) {
		this.a = a;
	}

	/**
	 * @param b the b to set
	 */
	public void setB(Angle b) {
		this.b = b;
	}

	/**
	 * @param c the c to set
	 */
	public void setC(Angle c) {
		this.c = c;
	}
	
}
