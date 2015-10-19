package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

/**
 * If there is no rotational motion, move the controlled point in a straight line at feed rate from the current position to the point given by the x, y, and z arguments. Do not move the rotational axes.
 * If there is rotational motion:
 * 1. If the feed reference mode is CANON_XYZ, perform XYZ motion as if there were no rotational  motion.  While  the  XYZ  motion  is  going  on,  move  the  rotational  axes  in coordinated linear motion.
 * 2. If the feed reference mode is CANON_WORKPIECE, the path to follow is the path that would be followed if the feed reference mode were CANON_XYZ, but the rate along that path should be kept constant
 * 
 * @author Psyko
 */
public class StraightFeedInstruction extends AbstractInstruction {
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
	public StraightFeedInstruction(BigDecimalQuantity<Length> x, BigDecimalQuantity<Length> y, BigDecimalQuantity<Length> z, BigDecimalQuantity<Angle> a, BigDecimalQuantity<Angle> b, BigDecimalQuantity<Angle> c) {
		super(InstructionType.STRAIGHT_FEED);
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
//		context.setMotionMode(EnumMotionMode.FEEDRATE);
//		context.setPosition(x, y, z, a, b, c);
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
