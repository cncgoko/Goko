package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter;
import org.goko.core.math.Tuple6b;

public class SetCoordinateSystemDataInstruction extends AbstractInstruction {
	/** The target coordinate system */
	private ICoordinateSystem targetCoordinateSystem;
	/** X coordinate in the target coordinate system */
	private Length x;
	/** Y coordinate in the target coordinate system */
	private Length y;
	/** Z coordinate in the target coordinate system */
	private Length z;
	/** A coordinate in the target coordinate system */
	private Angle a;
	/** B coordinate in the target coordinate system */
	private Angle b;
	/** C coordinate in the target coordinate system  */
	private Angle c;
	
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
	public SetCoordinateSystemDataInstruction(ICoordinateSystem targetCoordinateSystem, Length x, Length y, Length z, Angle a, Angle b, Angle c) {
		super(InstructionType.SET_COORDINATE_SYSTEM_DATA);
		this.targetCoordinateSystem = targetCoordinateSystem;
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
		Tuple6b offset = context.getCoordinateSystemData(targetCoordinateSystem);
		if(x != null) offset.setX(x);
		if(y != null) offset.setY(y);
		if(z != null) offset.setZ(z);
		if(a != null) offset.setA(a);
		if(b != null) offset.setB(b);
		if(c != null) offset.setC(c);
		context.setCoordinateSystemData(targetCoordinateSystem, offset);
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
	 * @return the targetCoordinateSystem
	 */
	public ICoordinateSystem getTargetCoordinateSystem() {
		return targetCoordinateSystem;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction#accept(org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter)
	 */
	@Override
	public void accept(GCodeContext context, RS274InstructionVisitorAdapter visitor) throws GkException{
		visitor.visit(context, this);
	}
}
