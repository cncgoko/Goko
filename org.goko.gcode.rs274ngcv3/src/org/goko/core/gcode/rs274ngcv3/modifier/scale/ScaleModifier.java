/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.modifier.scale;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.internal.Activator;
import org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier;

/**
 * @author PsyKo
 * @date 7 mars 2016
 */
public class ScaleModifier extends AbstractModifier<GCodeProvider> implements IModifier<GCodeProvider>{
	private BigDecimal scaleFactor;
	
	/**
	 * Constructor
	 */
	public ScaleModifier() {
		super("Scale");
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#isConfigured()
	 */
	@Override
	public boolean isConfigured() {
		return scaleFactor != null && !scaleFactor.equals(BigDecimal.ZERO);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier#applyModifier(org.goko.core.gcode.element.IGCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	protected void applyModifier(IGCodeProvider source, GCodeProvider target) throws GkException {
		GCodeContext localContext = new GCodeContext();
		InstructionProvider sourceInstructionSet = Activator.getRS274NGCService().getInstructions(localContext, source);
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = Activator.getRS274NGCService().getIterator(sourceInstructionSet, localContext);
		while(iterator.hasNext()){
			GCodeContext preContext = iterator.getContext();
			AbstractInstruction instr = iterator.next();
			if(instr.getType() == InstructionType.STRAIGHT_FEED
				|| instr.getType() == InstructionType.STRAIGHT_TRAVERSE){
				AbstractStraightInstruction straightInstruction = (AbstractStraightInstruction) instr;
				if(straightInstruction.getX() != null){
					straightInstruction.setX(straightInstruction.getX().multiply(scaleFactor));
				}
				if(straightInstruction.getY() != null){
					straightInstruction.setY(straightInstruction.getY().multiply(scaleFactor));
				}
				if(straightInstruction.getZ() != null){
					straightInstruction.setZ(straightInstruction.getZ().multiply(scaleFactor));
				}
			}else if(instr.getType() == InstructionType.ARC_FEED){
				scaleArcFeed((ArcFeedInstruction)instr, preContext);
			}
		}
		GCodeProvider result = Activator.getRS274NGCService().getGCodeProvider(localContext, sourceInstructionSet);
		for (GCodeLine line : result.getLines()) {
			target.addLine(line);
		}
	}
	
	/**
	 * Translation of an arc feed instruction
	 * @param instr the instruction
	 * @param preContext the context in which the instruction is evaluated
	 * @throws GkException GkException
	 */
	private void scaleArcFeed(ArcFeedInstruction instr, GCodeContext preContext) throws GkException {
		switch (preContext.getPlane()) {
		case XY_PLANE:	instr.setFirstEnd( instr.getFirstEnd().multiply(scaleFactor));
						instr.setSecondEnd( instr.getSecondEnd().multiply(scaleFactor));
						instr.setFirstAxis( instr.getFirstAxis().multiply(scaleFactor));
						instr.setSecondAxis( instr.getSecondAxis().multiply(scaleFactor));
						instr.setAxisEndPoint( instr.getAxisEndPoint().multiply(scaleFactor));
			break;
		case XZ_PLANE:	instr.setFirstEnd( instr.getFirstEnd().multiply(scaleFactor));
						instr.setSecondEnd( instr.getSecondEnd().multiply(scaleFactor));
						instr.setFirstAxis( instr.getFirstAxis().multiply(scaleFactor));
						instr.setSecondAxis( instr.getSecondAxis().multiply(scaleFactor));
						instr.setAxisEndPoint( instr.getAxisEndPoint().multiply(scaleFactor));
			break;
		case YZ_PLANE:	instr.setFirstEnd( instr.getFirstEnd().multiply(scaleFactor));
						instr.setSecondEnd( instr.getSecondEnd().multiply(scaleFactor));
						instr.setFirstAxis( instr.getFirstAxis().multiply(scaleFactor));
						instr.setSecondAxis( instr.getSecondAxis().multiply(scaleFactor));
						instr.setAxisEndPoint( instr.getAxisEndPoint().multiply(scaleFactor));
			break;
		default: throw new GkTechnicalException("Not a valid plane in GCodeContext ["+preContext.getPlane()+"]");
		}
	}

	/**
	 * @return the scaleFactor
	 */
	public BigDecimal getScaleFactor() {
		return scaleFactor;
	}

	/**
	 * @param scaleFactor the scaleFactor to set
	 */
	public void setScaleFactor(BigDecimal scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

}
