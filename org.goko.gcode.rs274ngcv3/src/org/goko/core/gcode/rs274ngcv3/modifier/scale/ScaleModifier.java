/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.modifier.scale;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
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
		GCodeContext modifiedContext = new GCodeContext();
		while(iterator.hasNext()){			
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
				scaleArcFeed((ArcFeedInstruction)instr, modifiedContext);
			}
			instr.apply(modifiedContext);
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
		if(instr.getX() != null){
			instr.setX(instr.getX().multiply(scaleFactor));
		}
		if(instr.getY() != null){
			instr.setY(instr.getY().multiply(scaleFactor));
		}
		if(instr.getZ() != null){
			instr.setZ(instr.getZ().multiply(scaleFactor));
		}
		if(instr.getI() != null){
			instr.setI(instr.getI().multiply(scaleFactor));
		}
		if(instr.getJ() != null){
			instr.setJ(instr.getJ().multiply(scaleFactor));
		}
		if(instr.getK() != null){
			instr.setK(instr.getK().multiply(scaleFactor));
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
