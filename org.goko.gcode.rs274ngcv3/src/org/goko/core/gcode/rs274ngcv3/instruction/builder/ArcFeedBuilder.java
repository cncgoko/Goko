package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class ArcFeedBuilder extends AbstractInstructionBuilder<ArcFeedInstruction> {

	public ArcFeedBuilder() {
		super(InstructionType.ARC_FEED);
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
			if(context.getMotionMode() == EnumMotionMode.ARC_CLOCKWISE || context.getMotionMode() == EnumMotionMode.ARC_COUNTERCLOCKWISE ){
				// Make sure there is no other motion mode word
				if(!GCodeWordUtils.containsWordRegex("(G|g)(0?)0", words) 
				&& !GCodeWordUtils.containsWordRegex("(G|g)(0?)1", words)
				&& !GCodeWordUtils.containsWord("G38.2", words)){
					return true;
				}
			}else{
				//Context motion mode is not ARC, we need an explicit G2 or G3
				return GCodeWordUtils.containsWordRegex("(G|g)(0?)2", words) || GCodeWordUtils.containsWordRegex("(G|g)(0?)3", words);
			}
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected ArcFeedInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		EnumPlane plane = context.getPlane();
		
		Length x = findWordLength("X", words, null, context.getUnit().getUnit());
		Length y = findWordLength("Y", words, null, context.getUnit().getUnit());
		Length z = findWordLength("Z", words, null, context.getUnit().getUnit());
		                               
		Angle a = findWordAngle("A", words, null, AngleUnit.DEGREE_ANGLE);
		Angle b = findWordAngle("B", words, null, AngleUnit.DEGREE_ANGLE);
		Angle c = findWordAngle("C", words, null, AngleUnit.DEGREE_ANGLE);
		
		Length i = findWordLength("I", words, null, context.getUnit().getUnit());
		Length j = findWordLength("J", words, null, context.getUnit().getUnit());
		Length k = findWordLength("K", words, null, context.getUnit().getUnit());
		
		Integer r = 1;
		boolean isValid = true;
		Boolean clockwise = null;
		if(context.getMotionMode() == EnumMotionMode.ARC_CLOCKWISE){
			clockwise = true;
		}else if(context.getMotionMode() == EnumMotionMode.ARC_COUNTERCLOCKWISE){
			clockwise = false;
		}

		GCodeWord gWord = GCodeWordUtils.findAndRemoveWordRegex("(G|g)(0?)2", words);
		if(gWord == null){			
			gWord = GCodeWordUtils.findAndRemoveWordRegex("(G|g)(0?)3", words);
			if(gWord != null){
				clockwise = false;
			}
		}else{
			clockwise = true;
		}
		
		GCodeWord rWord = GCodeWordUtils.findAndRemoveWordByLetter("R", words);
		if( rWord != null ){
			r = Integer.valueOf(rWord.getValue());
		}
		
		if(plane == null){
			throw new GkTechnicalException("No plane in GCodeContext ["+plane+"]");
		}		
		return new ArcFeedInstruction(x, y, z, i, j, k, a, b, c, r, clockwise);
	}
	
}
