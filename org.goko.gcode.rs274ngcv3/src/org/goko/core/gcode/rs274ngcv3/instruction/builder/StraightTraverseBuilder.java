package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightTraverseInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class StraightTraverseBuilder extends AbstractInstructionBuilder<StraightTraverseInstruction> {

	public StraightTraverseBuilder() {
		super(InstructionType.STRAIGHT_TRAVERSE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		if(GCodeWordUtils.containsWordRegex("(G|g)(0?)0", words)){
			return true; // Explicit G0
		}else if(GCodeWordUtils.containsWordByLetter("X", words)		
		|| GCodeWordUtils.containsWordByLetter("Y", words)
		|| GCodeWordUtils.containsWordByLetter("Z", words)
		|| GCodeWordUtils.containsWordByLetter("A", words)
		|| GCodeWordUtils.containsWordByLetter("B", words)
		|| GCodeWordUtils.containsWordByLetter("C", words)){
			if(context.getMotionMode() == EnumMotionMode.RAPID){
				// Make sure there is no other motion mode word
				if(!GCodeWordUtils.containsWordRegex("(G|g)(0?)1", words)
				&& !GCodeWordUtils.containsWordRegex("(G|g)(0?)2", words)
				&& !GCodeWordUtils.containsWordRegex("(G|g)(0?)3", words)
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
	protected StraightTraverseInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		Length x = findWordLength("X", words, null, context.getUnit().getUnit());
		Length y = findWordLength("Y", words, null, context.getUnit().getUnit());
		Length z = findWordLength("Z", words, null, context.getUnit().getUnit());
		                               
		Angle a = findWordAngle("A", words, null, AngleUnit.DEGREE_ANGLE);
		Angle b = findWordAngle("B", words, null, AngleUnit.DEGREE_ANGLE);
		Angle c = findWordAngle("C", words, null, AngleUnit.DEGREE_ANGLE);
		
		// Consume g0 if present
		GCodeWordUtils.findAndRemoveWordRegex("(G|g)(0?)0", words);
		
		return new StraightTraverseInstruction(x, y, z, a, b, c);
	}
}
