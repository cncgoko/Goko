package org.goko.core.gcode.rs274ngcv3.modifier;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.internal.Activator;

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
		this.translationX = NumberQuantity.of(BigDecimal.ZERO, SI.MILLIMETRE);
		this.translationY = NumberQuantity.of(BigDecimal.ZERO, SI.MILLIMETRE);
		this.translationZ = NumberQuantity.of(BigDecimal.ZERO, SI.MILLIMETRE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#apply(org.goko.core.gcode.rs274ngcv3.element.GCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	protected void applyModifier(GCodeProvider source, GCodeProvider target) throws GkException {
		GCodeContext localContext = new GCodeContext();
		InstructionProvider sourceInstructionSet = Activator.getRS274NGCService().getInstructions(localContext, source);
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = Activator.getRS274NGCService().getIterator(sourceInstructionSet, localContext);
		while(iterator.hasNext()){
			AbstractInstruction instr = iterator.next();
			if(instr.getType() == InstructionType.STRAIGHT_FEED){
				StraightFeedInstruction sfi = (StraightFeedInstruction) instr;				
				sfi.setX(sfi.getX().add(translationX));
				sfi.setY(sfi.getY().add(translationY));
				sfi.setZ(sfi.getZ().add(translationZ));
			}
		}
		GCodeProvider result = Activator.getRS274NGCService().getGCodeProvider(localContext, sourceInstructionSet);
		for (GCodeLine line : result.getLines()) {
			target.addLine(line);
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
	}

	
}
