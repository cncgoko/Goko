package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
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
	protected List<GCodeWord> getWords(GCodeContext context, ArcFeedInstruction instruction) throws GkException {
		BigDecimalQuantity<Length> x = instruction.getFirstEnd();
		BigDecimalQuantity<Length> y = instruction.getSecondEnd();
		BigDecimalQuantity<Length> z = instruction.getAxisEndPoint();
				
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
		List<GCodeWord> result = null;
		if(instruction.isClockwise()){
			result = wrap(new GCodeWord("G","2"));
		}else{
			result = wrap(new GCodeWord("G","3"));
		}
		
		switch (context.getPlane()) {
		case XY_PLANE:	
			if(getCoordinate(instruction.getFirstEnd(), context.getX(), context) != null){
				result.add(new GCodeWord("X", getCoordinate(instruction.getFirstEnd(), context.getX(), context).getValue().toPlainString() ));
			}
			if(getCoordinate(instruction.getSecondEnd(), context.getY(), context) != null){
				result.add(new GCodeWord("Y", getCoordinate(instruction.getSecondEnd(), context.getY(), context).getValue().toPlainString() ));
			}
			if(getCoordinate(instruction.getAxisEndPoint(), context.getZ(), context) != null){
				result.add(new GCodeWord("Z", getCoordinate(instruction.getAxisEndPoint(), context.getZ(), context).getValue().toPlainString() ));
			}
			
			result.add(new GCodeWord("I", instruction.getFirstAxis().subtract( context.getX()).to(targetUnit).getValue().toString() ));
			result.add(new GCodeWord("J", instruction.getSecondAxis().subtract(context.getY()).to(targetUnit).getValue().toString() ));
			
			break;		
		case XZ_PLANE:	//instruction	= new ArcFeedInstruction(z, x, k, i, y, r, a, b, c, clockwise);
			if(getCoordinate(instruction.getFirstEnd(), context.getZ(), context) != null){
				result.add(new GCodeWord("Z", getCoordinate(instruction.getFirstEnd(), context.getZ(), context).getValue().toPlainString() ));
			}
			if(getCoordinate(instruction.getSecondEnd(), context.getX(), context) != null){
				result.add(new GCodeWord("X", getCoordinate(instruction.getSecondEnd(), context.getX(), context).getValue().toPlainString() ));
			}
			if(getCoordinate(instruction.getAxisEndPoint(), context.getY(), context) != null){
				result.add(new GCodeWord("Y", getCoordinate(instruction.getAxisEndPoint(), context.getY(), context).getValue().toPlainString() ));
			}
			
			
			result.add(new GCodeWord("K", instruction.getFirstAxis().subtract( context.getZ()).to(targetUnit).getValue().toString() ));
			result.add(new GCodeWord("I", instruction.getSecondAxis().subtract(context.getX()).to(targetUnit).getValue().toString() ));
			break;
		case YZ_PLANE:	//instruction	= new ArcFeedInstruction(y, z, j, k, x, r, a, b, c, clockwise);
			if(getCoordinate(instruction.getFirstEnd(), context.getY(), context) != null){
				result.add(new GCodeWord("Y", getCoordinate(instruction.getFirstEnd(), context.getY(), context).getValue().toPlainString() ));
			}
			if(getCoordinate(instruction.getSecondEnd(), context.getZ(), context) != null){
				result.add(new GCodeWord("Z", getCoordinate(instruction.getSecondEnd(), context.getZ(), context).getValue().toPlainString() ));
			}
			if(getCoordinate(instruction.getAxisEndPoint(), context.getX(), context) != null){
				result.add(new GCodeWord("X", getCoordinate(instruction.getAxisEndPoint(), context.getX(), context).getValue().toPlainString() ));
			}
			
			result.add(new GCodeWord("J", instruction.getFirstAxis().subtract(context.getY()).to(targetUnit).getValue().toString() ));
			result.add(new GCodeWord("K", instruction.getSecondAxis().subtract( context.getZ()).to(targetUnit).getValue().toString() ));			
			break;
		default: throw new GkTechnicalException("Not a valid plane in GCodeContext ["+context.getPlane()+"]");			
		}
		
		return result;
	}
	
	public BigDecimalQuantity<Length> getCoordinate(BigDecimalQuantity<Length> absolute, BigDecimalQuantity<Length> contextValue, GCodeContext context){
		if(absolute != null){
			if(context.getDistanceMode() == EnumDistanceMode.ABSOLUTE){
				return absolute.to(context.getUnit().getUnit());
			}
			return absolute.subtract(contextValue).to(context.getUnit().getUnit());
		}
		return null;
	}

}
