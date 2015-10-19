package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.rs274ngcv3.context.EnumCoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class SetCoordinateSystemDataInstruction extends AbstractInstruction {
	/** The target coordinate system */
	private EnumCoordinateSystem targetCoordinateSystem;
	/** X coordinate in the target coordinate system */
	private BigDecimalQuantity<Length> x;
	/** Y coordinate in the target coordinate system */
	private BigDecimalQuantity<Length> y;
	/** Z coordinate in the target coordinate system */
	private BigDecimalQuantity<Length> z;
	/** A coordinate in the target coordinate system */
	private BigDecimalQuantity<Angle> a;
	/** B coordinate in the target coordinate system */
	private BigDecimalQuantity<Angle> b;
	/** C coordinate in the target coordinate system  */
	private BigDecimalQuantity<Angle> c;
	
	/**
	 * Constructor 
	 * @param targetCoordinateSystem The target coordinate system 
	 * @param x X coordinate in the target coordinate system
	 * @param y Y coordinate in the target coordinate system
	 * @param z Z coordinate in the target coordinate system
	 * @param a A coordinate in the target coordinate system
	 * @param b B coordinate in the target coordinate system
	 * @param c C coordinate in the target coordinate system
	 */
	public SetCoordinateSystemDataInstruction(EnumCoordinateSystem targetCoordinateSystem, BigDecimalQuantity<Length> x, BigDecimalQuantity<Length> y, BigDecimalQuantity<Length> z, BigDecimalQuantity<Angle> a, BigDecimalQuantity<Angle> b, BigDecimalQuantity<Angle> c) {
		super(InstructionType.SET_COORDINATE_SYSTEM_DATA);
		this.targetCoordinateSystem = targetCoordinateSystem;
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
//		Tuple6b offset = context.getCoordinateSystemData(targetCoordinateSystem);
//		if(x != null) offset.setX(x);
//		if(y != null) offset.setY(y);
//		if(z != null) offset.setZ(z);
//		if(a != null) offset.setA(a);
//		if(b != null) offset.setB(b);
//		if(c != null) offset.setC(c);
//		context.setCoordinateSystemData(targetCoordinateSystem, offset);
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

	/**
	 * @return the targetCoordinateSystem
	 */
	public EnumCoordinateSystem getTargetCoordinateSystem() {
		return targetCoordinateSystem;
	}

}
