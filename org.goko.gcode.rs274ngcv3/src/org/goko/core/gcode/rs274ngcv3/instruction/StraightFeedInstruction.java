package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter;

/**
 * If there is no rotational motion, move the controlled point in a straight line at feed rate from the current position to the point given by the x, y, and z arguments. Do not move the rotational axes.
 * If there is rotational motion:
 * 1. If the feed reference mode is CANON_XYZ, perform XYZ motion as if there were no rotational  motion.  While  the  XYZ  motion  is  going  on,  move  the  rotational  axes  in coordinated linear motion.
 * 2. If the feed reference mode is CANON_WORKPIECE, the path to follow is the path that would be followed if the feed reference mode were CANON_XYZ, but the rate along that path should be kept constant
 * 
 * @author Psyko
 */
public class StraightFeedInstruction extends AbstractStraightInstruction {

	/**
	 * Constructor 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @param a A coordinate
	 * @param b B coordinate
	 * @param c C coordinate
	 */
	public StraightFeedInstruction(Length x, Length y, Length z, Angle a, Angle b, Angle c) {
		super(InstructionType.STRAIGHT_FEED, x, y, z, a, b, c);
	}

	/**
	 * Copy constructor
	 * @param instr the instruction to copy
	 */
	public StraightFeedInstruction(AbstractStraightInstruction instr) {
		super(InstructionType.STRAIGHT_FEED, instr);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		context.setMotionMode(EnumMotionMode.FEEDRATE);
		super.apply(context);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction#accept(org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter)
	 */
	@Override
	public void accept(GCodeContext context, RS274InstructionVisitorAdapter visitor) throws GkException{
		visitor.visit(context, this);
	}
}
