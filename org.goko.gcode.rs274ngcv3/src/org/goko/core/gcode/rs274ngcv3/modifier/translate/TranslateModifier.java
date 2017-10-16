package org.goko.core.gcode.rs274ngcv3.modifier.translate;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier;

public class TranslateModifier extends AbstractModifier<GCodeProvider> implements IModifier<GCodeProvider> {
	private Length translationX;
	private Length translationY;
	private Length translationZ;
	private Angle translationA;
	private Angle translationB;
	private Angle translationC;
	
	
	/**
	 * Constructor
	 * @param idGCodeProvider target provider id
	 */
	public TranslateModifier() {
		super("Translate");
		this.translationX = Length.ZERO;
		this.translationY = Length.ZERO;
		this.translationZ = Length.ZERO;
		this.translationA = Angle.ZERO;
		this.translationB = Angle.ZERO;
		this.translationC = Angle.ZERO;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#isConfigured()
	 */
	@Override
	public boolean isConfigured() {		
		return true;
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#apply(org.goko.core.gcode.rs274ngcv3.element.GCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	protected void applyModifier(IGCodeProvider source, GCodeProvider target) throws GkException {
		GCodeContext localContext = new GCodeContext();
		InstructionProvider sourceInstructionSet = getRS274NGCService().getInstructions(localContext, source);
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = getRS274NGCService().getIterator(sourceInstructionSet, localContext);
		while(iterator.hasNext()){
			GCodeContext preContext = iterator.getContext();
			AbstractInstruction instr = iterator.next();
			// We only translate in relative distance
			if(preContext.getDistanceMode() == EnumDistanceMode.ABSOLUTE){
				if(instr.getType() == InstructionType.STRAIGHT_FEED
					|| instr.getType() == InstructionType.STRAIGHT_TRAVERSE){
					AbstractStraightInstruction straightInstruction = (AbstractStraightInstruction) instr;
					if(straightInstruction.getX() != null){
						straightInstruction.setX(straightInstruction.getX().add(translationX));
					}
					if(straightInstruction.getY() != null){
						straightInstruction.setY(straightInstruction.getY().add(translationY));
					}
					if(straightInstruction.getZ() != null){
						straightInstruction.setZ(straightInstruction.getZ().add(translationZ));
					}
					if(straightInstruction.getA() != null){
						straightInstruction.setA(straightInstruction.getA().add(translationA));
					}
					if(straightInstruction.getB() != null){
						straightInstruction.setB(straightInstruction.getB().add(translationB));
					}
					if(straightInstruction.getC() != null){
						straightInstruction.setC(straightInstruction.getC().add(translationC));
					}
				}else if(instr.getType() == InstructionType.ARC_FEED){
					translateArcFeed((ArcFeedInstruction)instr, preContext);
				}
			}
		}
		GCodeProvider result = getRS274NGCService().getGCodeProvider(localContext, sourceInstructionSet);
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
	private void translateArcFeed(ArcFeedInstruction instr, GCodeContext preContext) throws GkException {
		if(instr.getX() != null){
			instr.setX(instr.getX().add(translationX));
		}
		if(instr.getY() != null){
			instr.setY(instr.getY().add(translationY));
		}
		if(instr.getZ() != null){
			instr.setZ(instr.getZ().add(translationZ));
		}
		if(instr.getA() != null){
			instr.setA(instr.getA().add(translationA));
		}
		if(instr.getB() != null){
			instr.setB(instr.getB().add(translationB));
		}
		if(instr.getC() != null){
			instr.setC(instr.getC().add(translationC));
		}
	}

	/**
	 * @return the translationX
	 */
	public Length getTranslationX() {
		return translationX;
	}

	/**
	 * @param translationX the translationX to set
	 */
	public void setTranslationX(Length translationX) {
		this.translationX = translationX;
		updateModificationDate();
	}

	/**
	 * @return the translationY
	 */
	public Length getTranslationY() {
		return translationY;
	}

	/**
	 * @param translationY the translationY to set
	 */
	public void setTranslationY(Length translationY) {
		this.translationY = translationY;
		updateModificationDate();
	}

	/**
	 * @return the translationZ
	 */
	public Length getTranslationZ() {
		return translationZ;
	}

	/**
	 * @param translationZ the translationZ to set
	 */
	public void setTranslationZ(Length translationZ) {
		this.translationZ = translationZ;
		updateModificationDate();
	}

	/**
	 * @return the translationA
	 */
	public Angle getTranslationA() {
		return translationA;
	}

	/**
	 * @param translationA the translationA to set
	 */
	public void setTranslationA(Angle translationA) {
		this.translationA = translationA;
	}

	/**
	 * @return the translationB
	 */
	public Angle getTranslationB() {
		return translationB;
	}

	/**
	 * @param translationB the translationB to set
	 */
	public void setTranslationB(Angle translationB) {
		this.translationB = translationB;
	}

	/**
	 * @return the translationC
	 */
	public Angle getTranslationC() {
		return translationC;
	}

	/**
	 * @param translationC the translationC to set
	 */
	public void setTranslationC(Angle translationC) {
		this.translationC = translationC;
	}


}
