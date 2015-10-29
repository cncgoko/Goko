package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightProbeInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class StraightProbeBuilder extends AbstractInstructionBuilder<StraightProbeInstruction> {

	/** Constructor */
	public StraightProbeBuilder() {
		super(InstructionType.STRAIGHT_PROBE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("G38.2", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected StraightProbeInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		BigDecimalQuantity<Length> x = findWordValue("X", words, null, context.getUnit().getUnit());
		BigDecimalQuantity<Length> y = findWordValue("Y", words, null, context.getUnit().getUnit());
		BigDecimalQuantity<Length> z = findWordValue("Z", words, null, context.getUnit().getUnit());
		
		BigDecimalQuantity<Angle> a = findWordValue("Z", words, null, SI.DEGREE_ANGLE);
		BigDecimalQuantity<Angle> b = findWordValue("Z", words, null, SI.DEGREE_ANGLE);
		BigDecimalQuantity<Angle> c = findWordValue("Z", words, null, SI.DEGREE_ANGLE);
 			
		return new StraightProbeInstruction(x, y, z, a, b, c);
	}
}
