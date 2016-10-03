package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumCoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetCoordinateSystemDataInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;
import org.goko.core.math.Tuple6b;

public class SetCoordinateSystemDataBuilder extends AbstractInstructionBuilder<SetCoordinateSystemDataInstruction> {
	
	/** Constructor */
	public SetCoordinateSystemDataBuilder() {
		super(InstructionType.SET_COORDINATE_SYSTEM_DATA);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("G10", words) && GCodeWordUtils.containsWord("L2", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected SetCoordinateSystemDataInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWord("G10", words);
		GCodeWordUtils.getAndRemoveWord("L2", words);
		GCodeWord pWord = GCodeWordUtils.getAndRemoveWordByLetter("P", words);
		int coordinateSystemInteger = GCodeWordUtils.intValue(pWord);
		
		if(coordinateSystemInteger < 1 || coordinateSystemInteger > 9){
			throw new GkFunctionalException("GCO-120");
		}
		
		EnumCoordinateSystem targetCoordinateSystem = EnumCoordinateSystem.getEnum(coordinateSystemInteger);
		Length x = findWordLength("X", words, null, context.getUnit().getUnit());
		Length y = findWordLength("Y", words, null, context.getUnit().getUnit());
		Length z = findWordLength("Z", words, null, context.getUnit().getUnit());
		Angle a  = findWordAngle("A", words, null, AngleUnit.DEGREE_ANGLE);
		Angle b  = findWordAngle("B", words, null, AngleUnit.DEGREE_ANGLE);
		Angle c  = findWordAngle("C", words, null, AngleUnit.DEGREE_ANGLE);
		
		Tuple6b currentOffset = context.getCoordinateSystemData(targetCoordinateSystem);
		if(x == null) x = currentOffset.getX();
		if(y == null) y = currentOffset.getY();
		if(z == null) z = currentOffset.getZ();
		if(a == null) a = currentOffset.getA();
		if(b == null) b = currentOffset.getB();
		if(c == null) c = currentOffset.getC();
		
		return new SetCoordinateSystemDataInstruction(targetCoordinateSystem, x, y, z, a, b, c);
	}
}
