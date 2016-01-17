package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.math.Tuple6b;

public class SetOriginOffsetInstruction extends AbstractInstruction {
	/** Target X coordinate in the current coordinate system */
	private Length x;
	/** Target Y coordinate in the current coordinate system */
	private Length y;
	/** Target Z coordinate in the current coordinate system */
	private Length z;
	/** Target A coordinate in the current coordinate system */
	private Angle a;
	/** Target B coordinate in the current coordinate system */
	private Angle b;
	/** Target C coordinate in the current coordinate system  */
	private Angle c;

	/** Constructor */
	public SetOriginOffsetInstruction(Length x, Length y, Length z, Angle a, Angle b, Angle c) {
		super(InstructionType.SET_ORIGIN_OFFSETS);
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
		Tuple6b offset = context.getOriginOffset();
		if(x != null){
			offset.setX( offset.getX().subtract(x) );
		}
		if(y != null){
			offset.setY( offset.getX().subtract(y) );
		}
		if(z != null){
			offset.setZ( offset.getX().subtract(z) );
		}
		if(a != null){
			offset.setA( offset.getA().subtract(a) );
		}
		if(b != null){
			offset.setB( offset.getB().subtract(b) );
		}
		if(c != null){
			offset.setC( offset.getC().subtract(c) );
		}
		context.setOriginOffset(offset);
		context.setOriginOffsetActive(true);
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

}
