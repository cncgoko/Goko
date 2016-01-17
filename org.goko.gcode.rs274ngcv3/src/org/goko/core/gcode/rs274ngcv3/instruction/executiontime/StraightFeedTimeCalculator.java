package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.math.Tuple6b;

public class StraightFeedTimeCalculator extends AbstractInstructionTimeCalculator<StraightFeedInstruction> {

	public StraightFeedTimeCalculator() {
		super(InstructionType.STRAIGHT_FEED);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.executiontime.AbstractInstructionTimeCalculator#calculateExecutionTime(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction)
	 */
	@Override
	protected Time calculateExecutionTime(GCodeContext context, StraightFeedInstruction instruction) throws GkException {
		Tuple6b 		positionBefore 	= context.getPosition();
		Tuple6b 		positionAfter 	= new Tuple6b(instruction.getX(),instruction.getY(),instruction.getZ(),instruction.getA(),instruction.getB(),instruction.getC());

		Tuple6b delta = positionBefore.subtract(positionAfter);
		Unit<Length> unit = context.getUnit().getUnit();
//		double dx = (delta.getX() == null) ? 0 : Math.abs(positionBefore.getX().doubleValue(unit) - positionAfter.getX().doubleValue(unit));
//		double dy = (delta.getY() == null) ? 0 : Math.abs(positionBefore.getY().doubleValue(unit) - positionAfter.getY().doubleValue(unit));
//		double dz = (delta.getZ() == null) ? 0 : Math.abs(positionBefore.getZ().doubleValue(unit) - positionAfter.getZ().doubleValue(unit));
//		double da = (delta.getA() == null) ? 0 : Math.abs(positionBefore.getA().doubleValue(AngleUnit.DEGREE_ANGLE) - positionAfter.getA().doubleValue(AngleUnit.DEGREE_ANGLE));
//		double db = (delta.getB() == null) ? 0 : Math.abs(positionBefore.getB().doubleValue(AngleUnit.DEGREE_ANGLE) - positionAfter.getB().doubleValue(AngleUnit.DEGREE_ANGLE));
//		double dc = (delta.getC() == null) ? 0 : Math.abs(positionBefore.getC().doubleValue(AngleUnit.DEGREE_ANGLE) - positionAfter.getC().doubleValue(AngleUnit.DEGREE_ANGLE));

//		double max = Math.max(dx, Math.max(dy, Math.max(dz, Math.max(da, Math.max(db, dc)))));
		Length max = delta.length();

		BigDecimal feedrate = BigDecimal.ZERO;
		if(context.getFeedrate() != null){
			feedrate = context.getFeedrate();
		}else{
			return Time.ZERO;
		}
		if(feedrate ==  BigDecimal.ZERO){
			return Time.ZERO;
		}		
		return Time.valueOf((max.divide(feedrate).value(LengthUnit.MILLIMETRE)) , TimeUnit.MINUTE);		
	}

}
