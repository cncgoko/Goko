package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetOriginOffsetInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class SetOriginOffsetsBuilder extends AbstractInstructionBuilder<SetOriginOffsetInstruction> {
	/** Constructor */
	public SetOriginOffsetsBuilder() {
		super(InstructionType.SET_ORIGIN_OFFSETS);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWord("G92", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected SetOriginOffsetInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWord("G92", words);
		
		BigDecimalQuantity<Length> x = findWordValue("X", words, null, context.getUnit().getUnit());
		BigDecimalQuantity<Length> y = findWordValue("Y", words, null, context.getUnit().getUnit());
		BigDecimalQuantity<Length> z = findWordValue("Z", words, null, context.getUnit().getUnit());
		                               
		BigDecimalQuantity<Angle> a = findWordValue("A", words, null, Units.DEGREE_ANGLE);
		BigDecimalQuantity<Angle> b = findWordValue("B", words, null, Units.DEGREE_ANGLE);
		BigDecimalQuantity<Angle> c = findWordValue("C", words, null, Units.DEGREE_ANGLE);
		
		if(x == null && y == null && z == null && a == null && b == null && c == null){
			throw new GkFunctionalException("GCO-110", "G92");
		}
		if(x == null) x = context.getX();
		if(y == null) y = context.getY();
		if(z == null) z = context.getZ();
		if(a == null) a = context.getA();
		if(b == null) b = context.getB();
		if(c == null) c = context.getC();
		
		return new SetOriginOffsetInstruction(x, y, z, a, b, c);
	}
}
