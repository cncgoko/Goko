package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
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
		if(GCodeWordUtils.containsWordByLetter("X", words) 
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
			}else{
				//Context motion mode is not FEEDRATE, we need an explicit G1
				return GCodeWordUtils.containsWordRegex("G(0?)1", words);				
			}
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected StraightFeedInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		BigDecimalQuantity<Length> x = findWordValue("X", words, null, context.getUnit().getUnit());
		BigDecimalQuantity<Length> y = findWordValue("Y", words, null, context.getUnit().getUnit());
		BigDecimalQuantity<Length> z = findWordValue("Z", words, null, context.getUnit().getUnit());
		                               
		BigDecimalQuantity<Angle> a = findWordValue("A", words, null, SI.DEGREE_ANGLE);
		BigDecimalQuantity<Angle> b = findWordValue("B", words, null, SI.DEGREE_ANGLE);
		BigDecimalQuantity<Angle> c = findWordValue("C", words, null, SI.DEGREE_ANGLE);

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
