package org.goko.core.gcode.rs274ngcv3.instruction.executiontime;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
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
	protected Quantity<Time> calculateExecutionTime(GCodeContext context, StraightFeedInstruction instruction) throws GkException {
		Tuple6b 		positionBefore 	= context.getPosition();
		Tuple6b 		positionAfter 	= new Tuple6b(instruction.getX(),instruction.getY(),instruction.getZ(),instruction.getA(),instruction.getB(),instruction.getC());

		Tuple6b delta = positionBefore.subtract(positionAfter);

		double dx = (delta.getX() == null) ? 0 : Math.abs(positionBefore.getX().doubleValue() - positionAfter.getX().doubleValue());
		double dy = (delta.getY() == null) ? 0 : Math.abs(positionBefore.getY().doubleValue() - positionAfter.getY().doubleValue());
		double dz = (delta.getZ() == null) ? 0 : Math.abs(positionBefore.getZ().doubleValue() - positionAfter.getZ().doubleValue());
		double da = (delta.getA() == null) ? 0 : Math.abs(positionBefore.getA().doubleValue() - positionAfter.getA().doubleValue());
		double db = (delta.getB() == null) ? 0 : Math.abs(positionBefore.getB().doubleValue() - positionAfter.getB().doubleValue());
		double dc = (delta.getC() == null) ? 0 : Math.abs(positionBefore.getC().doubleValue() - positionAfter.getC().doubleValue());

		double max = Math.max(dx, Math.max(dy, Math.max(dz, Math.max(da, Math.max(db, dc)))));

		double feedrate = 0;
		if(context.getFeedrate() != null){
			feedrate = context.getFeedrate().doubleValue();
		}else{
			return NumberQuantity.zero(SI.SECOND);
		}
		if(feedrate == 0){
			return NumberQuantity.zero(SI.SECOND);
		}		
		return NumberQuantity.of((max / feedrate) , SI.MINUTE);		
	}

}
