package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
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
	protected List<GCodeWord> getWords(GCodeContext context, T instruction) throws GkException {
		BigDecimalQuantity<Length> x = instruction.getX();
		BigDecimalQuantity<Length> y = instruction.getY();
		BigDecimalQuantity<Length> z = instruction.getZ();
		BigDecimalQuantity<Angle> a = instruction.getA();
		BigDecimalQuantity<Angle> b = instruction.getB();
		BigDecimalQuantity<Angle> c = instruction.getC();
		if(context.getDistanceMode() == EnumDistanceMode.RELATIVE){
			if(x != null) x = x.subtract(context.getX());
			if(y != null) y = y.subtract(context.getY());
			if(z != null) z = z.subtract(context.getZ());
			if(a != null) a = a.subtract(context.getA());
			if(b != null) b = b.subtract(context.getB());
			if(c != null) c = c.subtract(context.getC());
		}
		Unit<Length> targetUnit = context.getUnit().getUnit();
		
		List<GCodeWord> result = new ArrayList<GCodeWord>();
		if(x != null && !x.equals(context.getX())) result.add(new GCodeWord("X", x.to(targetUnit).getValue().toPlainString() ));
		if(y != null && !y.equals(context.getY())) result.add(new GCodeWord("Y", y.to(targetUnit).getValue().toPlainString() ));
		if(z != null && !z.equals(context.getZ())) result.add(new GCodeWord("Z", z.to(targetUnit).getValue().toPlainString() ));
		if(a != null && !a.equals(context.getA())) result.add(new GCodeWord("A", a.to(Units.DEGREE_ANGLE).getValue().toPlainString() ));
		if(b != null && !b.equals(context.getB())) result.add(new GCodeWord("B", b.to(Units.DEGREE_ANGLE).getValue().toPlainString() ));
		if(c != null && !c.equals(context.getC())) result.add(new GCodeWord("C", c.to(Units.DEGREE_ANGLE).getValue().toPlainString() ));
		
		return result;
	}

}
