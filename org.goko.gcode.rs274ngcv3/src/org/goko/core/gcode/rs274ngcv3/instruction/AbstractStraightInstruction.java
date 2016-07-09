package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.math.Tuple6b;

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
	
	/**
	 * Copy constructor 
	 * @param instr the instruction to copy
	 */
	public AbstractStraightInstruction(InstructionType type, AbstractStraightInstruction instr) {		
		super(type);
		this.x = instr.getX();
		this.y = instr.getY();
		this.z = instr.getZ();
		this.a = instr.getA();
		this.b = instr.getB();
		this.c = instr.getC();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		Tuple6b position = context.getPosition();
		if(context.getDistanceMode() == EnumDistanceMode.RELATIVE){
			position.setX(NumberQuantity.add(x, context.getX()));
			position.setY(NumberQuantity.add(y, context.getY()));
			position.setZ(NumberQuantity.add(z, context.getZ()));
			position.setA(NumberQuantity.add(a, context.getA()));
			position.setB(NumberQuantity.add(b, context.getB()));
			position.setC(NumberQuantity.add(c, context.getC()));
		}else{		
			if(x != null) position.setX(x);// = context.getX();
			if(y != null) position.setY(y);// = context.getY();
			if(z != null) position.setZ(z);// = context.getZ();
			if(a != null) position.setA(a);// = context.getA();
			if(b != null) position.setB(b);// = context.getB();
			if(c != null) position.setC(c);// = context.getC();
		}
//		context.setPosition(x, y, z, a, b, c);
		context.setPosition(position);
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

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		result = prime * result + ((z == null) ? 0 : z.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractStraightInstruction other = (AbstractStraightInstruction) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		if (z == null) {
			if (other.z != null)
				return false;
		} else if (!z.equals(other.z))
			return false;
		return true;
	}
	
	
}
