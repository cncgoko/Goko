package org.goko.core.gcode.rs274ngcv3.modifier.translate;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
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

public class TranslateModifier extends AbstractModifier<GCodeProvider> implements IModifier<GCodeProvider> {
	private BigDecimalQuantity<Length> translationX;
	private BigDecimalQuantity<Length> translationY;
	private BigDecimalQuantity<Length> translationZ;

	/**
	 * Constructor
	 * @param idGCodeProvider target provider id
	 */
	public TranslateModifier(Integer idGCodeProvider) {
		super(idGCodeProvider, "Translate");
		this.translationX = NumberQuantity.of(BigDecimal.ZERO, Units.MILLIMETRE);
		this.translationY = NumberQuantity.of(BigDecimal.ZERO, Units.MILLIMETRE);
		this.translationZ = NumberQuantity.of(BigDecimal.ZERO, Units.MILLIMETRE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#apply(org.goko.core.gcode.rs274ngcv3.element.GCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
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
				straightInstruction.setX(straightInstruction.getX().add(translationX));
				straightInstruction.setY(straightInstruction.getY().add(translationY));
				straightInstruction.setZ(straightInstruction.getZ().add(translationZ));
			}else if(instr.getType() == InstructionType.ARC_FEED){
				translateArcFeed((ArcFeedInstruction)instr, preContext);
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
	private void translateArcFeed(ArcFeedInstruction instr, GCodeContext preContext) throws GkException {
		switch (preContext.getPlane()) {
		case XY_PLANE:	instr.setFirstEnd( instr.getFirstEnd().add(translationX));
						instr.setSecondEnd( instr.getSecondEnd().add(translationY));
						instr.setFirstAxis( instr.getFirstAxis().add(translationX));
						instr.setSecondAxis( instr.getSecondAxis().add(translationY));
						instr.setAxisEndPoint( instr.getAxisEndPoint().add(translationZ));
			break;
		case XZ_PLANE:	instr.setFirstEnd( instr.getFirstEnd().add(translationZ));
						instr.setSecondEnd( instr.getSecondEnd().add(translationX));
						instr.setFirstAxis( instr.getFirstAxis().add(translationZ));
						instr.setSecondAxis( instr.getSecondAxis().add(translationX));
						instr.setAxisEndPoint( instr.getAxisEndPoint().add(translationY));
			break;
		case YZ_PLANE:	instr.setFirstEnd( instr.getFirstEnd().add(translationY));
						instr.setSecondEnd( instr.getSecondEnd().add(translationZ));
						instr.setFirstAxis( instr.getFirstAxis().add(translationY));
						instr.setSecondAxis( instr.getSecondAxis().add(translationZ));
						instr.setAxisEndPoint( instr.getAxisEndPoint().add(translationX));
			break;
		default: throw new GkTechnicalException("Not a valid plane in GCodeContext ["+preContext.getPlane()+"]");
		}
	}

	/**
	 * @return the translationX
	 */
	public BigDecimalQuantity<Length> getTranslationX() {
		return translationX;
	}

	/**
	 * @param translationX the translationX to set
	 */
	public void setTranslationX(BigDecimalQuantity<Length> translationX) {
		this.translationX = translationX;
		updateModificationDate();
	}

	/**
	 * @return the translationY
	 */
	public BigDecimalQuantity<Length> getTranslationY() {
		return translationY;
	}

	/**
	 * @param translationY the translationY to set
	 */
	public void setTranslationY(BigDecimalQuantity<Length> translationY) {
		this.translationY = translationY;
		updateModificationDate();
	}

	/**
	 * @return the translationZ
	 */
	public BigDecimalQuantity<Length> getTranslationZ() {
		return translationZ;
	}

	/**
	 * @param translationZ the translationZ to set
	 */
	public void setTranslationZ(BigDecimalQuantity<Length> translationZ) {
		this.translationZ = translationZ;
		updateModificationDate();
	}


}
