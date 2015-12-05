package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightTraverseInstruction;
import org.goko.core.math.Tuple6b;

public class StraightTraverseTimeCalculator extends AbstractInstructionTimeCalculator<StraightTraverseInstruction> {

	public StraightTraverseTimeCalculator() {
		super(InstructionType.STRAIGHT_TRAVERSE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.AbstractInstructionTimeCalculator#calculateExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction)
	 */
	@Override
	protected Quantity<Time> calculateExecutionTime(GCodeContext context, StraightTraverseInstruction instruction) throws GkException {
		Tuple6b 		positionBefore 	= context.getPosition();
		Tuple6b 		positionAfter 	= new Tuple6b(instruction.getX(),instruction.getY(),instruction.getZ(),instruction.getA(),instruction.getB(),instruction.getC());

		Tuple6b delta = positionBefore.subtract(positionAfter);
		Unit<Length> unit = context.getUnit().getUnit();
//		double dx = (delta.getX() == null) ? 0 : Math.abs(positionBefore.getX().doubleValue(unit) - positionAfter.getX().doubleValue(unit));
//		double dy = (delta.getY() == null) ? 0 : Math.abs(positionBefore.getY().doubleValue(unit) - positionAfter.getY().doubleValue(unit));
//		double dz = (delta.getZ() == null) ? 0 : Math.abs(positionBefore.getZ().doubleValue(unit) - positionAfter.getZ().doubleValue(unit));
//		double da = (delta.getA() == null) ? 0 : Math.abs(positionBefore.getA().doubleValue(Units.DEGREE_ANGLE) - positionAfter.getA().doubleValue(Units.DEGREE_ANGLE));
//		double db = (delta.getB() == null) ? 0 : Math.abs(positionBefore.getB().doubleValue(Units.DEGREE_ANGLE) - positionAfter.getB().doubleValue(Units.DEGREE_ANGLE));
//		double dc = (delta.getC() == null) ? 0 : Math.abs(positionBefore.getC().doubleValue(Units.DEGREE_ANGLE) - positionAfter.getC().doubleValue(Units.DEGREE_ANGLE));

//		double max = Math.max(dx, Math.max(dy, Math.max(dz, Math.max(da, Math.max(db, dc)))));
		double max = delta.length().doubleValue(unit);

		double feedrate = 1500;				
		return NumberQuantity.of((max / feedrate) , Units.MINUTE);		
	}

}
