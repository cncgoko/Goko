package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class StraightFeedBuilder extends AbstractInstructionBuilder<StraightFeedInstruction> {

	public StraightFeedBuilder() {
		super(InstructionType.STRAIGHT_FEED);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		if(GCodeWordUtils.containsWordRegex("G(0?)1", words)){
			return true; // Explicit feedrate mode
		}else if(GCodeWordUtils.containsWordByLetter("X", words) 
		|| GCodeWordUtils.containsWordByLetter("Y", words)
		|| GCodeWordUtils.containsWordByLetter("Z", words)
		|| GCodeWordUtils.containsWordByLetter("A", words)
		|| GCodeWordUtils.containsWordByLetter("B", words)
		|| GCodeWordUtils.containsWordByLetter("C", words)){
			if(context.getMotionMode() == EnumMotionMode.FEEDRATE){
				// Make sure there is no other motion mode word
				if(!GCodeWordUtils.containsWordRegex("G(0?)0", words)
				&& !GCodeWordUtils.containsWordRegex("G(0?)2", words)
				&& !GCodeWordUtils.containsWordRegex("G(0?)3", words)
				&& !GCodeWordUtils.containsWord("G38.2", words)){
					return true;
				}
			}
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected StraightFeedInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		Length x = findWordLength("X", words, null, context.getUnit().getUnit());
		Length y = findWordLength("Y", words, null, context.getUnit().getUnit());
		Length z = findWordLength("Z", words, null, context.getUnit().getUnit());
		                               
		Angle a = findWordAngle("A", words, null, AngleUnit.DEGREE_ANGLE);
		Angle b = findWordAngle("B", words, null, AngleUnit.DEGREE_ANGLE);
		Angle c = findWordAngle("C", words, null, AngleUnit.DEGREE_ANGLE);

		// Consume the word
		GCodeWordUtils.findAndRemoveWordRegex("G(0?)1", words);
		
		if(context.getDistanceMode() == EnumDistanceMode.RELATIVE){
			x = NumberQuantity.add(x, context.getX());
			y = NumberQuantity.add(y, context.getY());
			z = NumberQuantity.add(z, context.getZ());
			a = NumberQuantity.add(a, context.getA());
			b = NumberQuantity.add(b, context.getB());
			c = NumberQuantity.add(c, context.getC());
		}else{
			if(x == null) x = context.getX();
			if(y == null) y = context.getY();
			if(z == null) z = context.getZ();
			if(a == null) a = context.getA();
			if(b == null) b = context.getB();
			if(c == null) c = context.getC();
		}
		return new StraightFeedInstruction(x, y, z, a, b, c);
	}
}
