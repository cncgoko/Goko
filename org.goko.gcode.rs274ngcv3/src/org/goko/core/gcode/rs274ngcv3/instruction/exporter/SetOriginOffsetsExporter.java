package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetOriginOffsetInstruction;

public class SetOriginOffsetsExporter extends AbstractInstructionExporter<SetOriginOffsetInstruction>{

	public SetOriginOffsetsExporter() {
		super(InstructionType.SET_ORIGIN_OFFSETS);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, SetOriginOffsetInstruction instruction, RenderingFormat format) throws GkException {
		List<GCodeWord> words = new ArrayList<GCodeWord>();
		words.add(new GCodeWord("G", "92"));
		Unit<Length> targetUnit = context.getUnit().getUnit();
		if(instruction.getX() != null) words.add(new GCodeWord("X", format.format(instruction.getX(), targetUnit) ));
		if(instruction.getY() != null) words.add(new GCodeWord("Y", format.format(instruction.getY(), targetUnit) ));
		if(instruction.getZ() != null) words.add(new GCodeWord("Z", format.format(instruction.getZ(), targetUnit) ));
		if(instruction.getA() != null) words.add(new GCodeWord("A", format.format(instruction.getA(), AngleUnit.DEGREE_ANGLE) ));
		if(instruction.getB() != null) words.add(new GCodeWord("B", format.format(instruction.getB(), AngleUnit.DEGREE_ANGLE) ));
		if(instruction.getC() != null) words.add(new GCodeWord("C", format.format(instruction.getC(), AngleUnit.DEGREE_ANGLE) ));
		
		return words;
	}

}
