package org.goko.core.gcode.rs274ngcv3.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
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
		
	/**
	 * Constructor
	 * @param idGCodeProvider target provider id
	 */
	public TranslateModifier(Integer idGCodeProvider) {
		super(idGCodeProvider, "Translate");
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
				sfi.setX(sfi.getX().add(NumberQuantity.of("5", SI.MILLIMETRE)));
			}
		}
		GCodeProvider result = Activator.getRS274NGCService().getGCodeProvider(localContext, sourceInstructionSet);
		for (GCodeLine line : result.getLines()) {
			target.addLine(line);
		}
	}

}
