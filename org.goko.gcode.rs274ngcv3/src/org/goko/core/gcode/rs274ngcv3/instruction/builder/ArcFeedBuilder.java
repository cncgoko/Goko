package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
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
				if(!GCodeWordUtils.containsWord("G0", words) 
				&& !GCodeWordUtils.containsWord("G1", words)
				&& !GCodeWordUtils.containsWord("G38.2", words)){
					return true;
				}
			}else{
				//Context motion mode is not ARC, we need an explicit G2 or G3
				return GCodeWordUtils.containsWord("G2", words) || GCodeWordUtils.containsWord("G3", words);
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
		
		Boolean clockwise = null;
		if(context.getMotionMode() == EnumMotionMode.ARC_CLOCKWISE){
			clockwise = true;
		}else if(context.getMotionMode() == EnumMotionMode.ARC_COUNTERCLOCKWISE){
			clockwise = false;
		}

		GCodeWord gWord = GCodeWordUtils.findAndRemoveWord("G2", words);
		if(gWord == null){			
			gWord = GCodeWordUtils.findAndRemoveWord("G3", words);
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
		
		ArcFeedInstruction instruction = null;
		if(plane == null){
			throw new GkTechnicalException("No plane in GCodeContext ["+plane+"]");
		}
		// Words verification
		switch (plane) {
		case XY_PLANE:	
				if(x == null && y == null){
					throw new GkFunctionalException("GCO-130", "X", "Y");
				}
				if(i == null && j == null){
					throw new GkFunctionalException("GCO-130", "I", "J");
				}
			break;
		case YZ_PLANE:	
			if(x == null && z == null){
				throw new GkFunctionalException("GCO-130", "X", "Z");
			}
			if(j == null && k == null){
				throw new GkFunctionalException("GCO-130", "J", "K");
			}
			break;
		case XZ_PLANE:
			if(z == null && y == null){
				throw new GkFunctionalException("GCO-130", "Y", "Z");
			}
			if(k == null && i == null){
				throw new GkFunctionalException("GCO-130", "I", "K");
			}
			break;
		default: throw new GkTechnicalException("Not a valid plane in GCodeContext ["+plane+"]");			
		}
		
		if(context.getDistanceMode() == EnumDistanceMode.RELATIVE){
//			x = NumberQuantity.add(x, context.getX());
//			y = NumberQuantity.add(y, context.getY());
//			z = NumberQuantity.add(z, context.getZ());
//			a = NumberQuantity.add(a, context.getA());
//			b = NumberQuantity.add(b, context.getB());
//			c = NumberQuantity.add(c, context.getC());
		}
//		else{
//			if(x == null) x = context.getX();
//			if(y == null) y = context.getY();
//			if(z == null) z = context.getZ();
//			if(a == null) a = context.getA();
//			if(b == null) b = context.getB();
//			if(c == null) c = context.getC();
//		}
		// Compute the center of the arc 
//		i = NumberQuantity.add(i, context.getX());
//		j = NumberQuantity.add(j, context.getY());
//		k = NumberQuantity.add(k, context.getZ());

		instruction	= new ArcFeedInstruction(x, y, z, i, j, k, a, b, c, r, clockwise);
		
//		switch (plane) {
//		// public ArcFeedInstruction(Length firstEnd, Length secondEnd, Length firstAxis, Length secondAxis, Length axisEndPoint, Integer rotation, Angle a, Angle b, Angle c, boolean clockwise) {
//		case XY_PLANE:	instruction	= new ArcFeedInstruction(x, y, i, j, z, r, a, b, c, clockwise);
//			break;		
//		case XZ_PLANE:	instruction	= new ArcFeedInstruction(z, x, k, i, y, r, a, b, c, clockwise);
//			break;
//		case YZ_PLANE:	instruction	= new ArcFeedInstruction(y, z, j, k, x, r, a, b, c, clockwise);
//			break;
//		default: throw new GkTechnicalException("Not a valid plane in GCodeContext ["+plane+"]");			
//		}
		return instruction;
	}

}
