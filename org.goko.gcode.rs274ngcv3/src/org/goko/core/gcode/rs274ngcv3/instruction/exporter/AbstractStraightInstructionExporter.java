package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.ArrayList;
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
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction;

public abstract class AbstractStraightInstructionExporter<T extends AbstractStraightInstruction> extends AbstractInstructionExporter<T> {

	public AbstractStraightInstructionExporter(InstructionType type) {
		super(type);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, T instruction, RenderingFormat format) throws GkException {
		Length x = instruction.getX();
		Length y = instruction.getY();
		Length z = instruction.getZ();
		Angle a = instruction.getA();
		Angle b = instruction.getB();
		Angle c = instruction.getC();

		Unit<Length> targetUnit = context.getUnit().getUnit();
		
		List<GCodeWord> result = new ArrayList<GCodeWord>();
		if(x != null) result.add(new GCodeWord("X", format.format(x, targetUnit) ));
		if(y != null) result.add(new GCodeWord("Y", format.format(y, targetUnit) ));
		if(z != null) result.add(new GCodeWord("Z", format.format(z, targetUnit) ));
		if(a != null) result.add(new GCodeWord("A", format.format(a, AngleUnit.DEGREE_ANGLE) ));
		if(b != null) result.add(new GCodeWord("B", format.format(b, AngleUnit.DEGREE_ANGLE) ));
		if(c != null) result.add(new GCodeWord("C", format.format(c, AngleUnit.DEGREE_ANGLE) ));
		return result;
	}

}
