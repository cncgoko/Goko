package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;

public class ArcFeedExporter extends AbstractInstructionExporter<ArcFeedInstruction> {

	public ArcFeedExporter() {
		super(InstructionType.ARC_FEED);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, ArcFeedInstruction instruction, RenderingFormat format) throws GkException {
		Length x = instruction.getX();
		Length y = instruction.getY();
		Length z = instruction.getZ();
		
		Length i = instruction.getI();
		Length j = instruction.getJ();
		Length k = instruction.getK();
				
		Angle a = instruction.getA();
		Angle b = instruction.getB();
		Angle c = instruction.getC();
		
		List<GCodeWord> result = null;
		if(instruction.isClockwise()){
			result = wrap(new GCodeWord("G","2"));
		}else{
			result = wrap(new GCodeWord("G","3"));
		}
		Unit<Length> unit = context.getUnit().getUnit();
		
		if(x != null){
			result.add(new GCodeWord("X", format.format(x, unit) ));
		}
		if(y != null){
			result.add(new GCodeWord("Y", format.format(y, unit) ));
		}
		if(z != null){
			result.add(new GCodeWord("Z", format.format(z, unit) ));
		}
		if(i != null){
			result.add(new GCodeWord("I", format.format(i, unit) ));
		}
		if(j != null){
			result.add(new GCodeWord("J", format.format(j, unit) ));
		}
		if(k != null){
			result.add(new GCodeWord("K", format.format(k, unit) ));
		}
		
		if(a != null){
			result.add(new GCodeWord("A", format.format(a, AngleUnit.DEGREE_ANGLE) ));
		}
		if(b != null){
			result.add(new GCodeWord("B", format.format(b, AngleUnit.DEGREE_ANGLE)));
		}
		if(c != null){
			result.add(new GCodeWord("C", format.format(c, AngleUnit.DEGREE_ANGLE)));
		}
		
		return result;
	}
}
