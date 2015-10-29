package org.goko.core.gcode.rs274ngcv3.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.AbstractIdBean;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.internal.Activator;

public class TestModifier extends AbstractIdBean implements IModifier {

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#apply(org.goko.core.gcode.rs274ngcv3.element.GCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	public void apply(GCodeContext initialContext, GCodeProvider source, GCodeProvider target) throws GkException{
		GCodeContext localContext = new GCodeContext(initialContext);
		InstructionProvider sourceInstructionSet = Activator.getRS274NGCService().getInstructions(localContext, source);
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = Activator.getRS274NGCService().getIterator(sourceInstructionSet, localContext);
//		while(iterator.hasNext()){
//			AbstractInstruction instr = iterator.next();
//			if(instr.getType() == InstructionType.STRAIGHT_FEED){
//				StraightFeedInstruction sfi = (StraightFeedInstruction) instr;
//				sfi.setZ(sfi.getZ().add(NumberQuantity.of("3", SI.MILLIMETRE)));
//			}
//		}
		GCodeProvider result = Activator.getRS274NGCService().getGCodeProvider(initialContext, sourceInstructionSet);
		for (GCodeLine line : result.getLines()) {
			target.addLine(line);
		}
		
	}

}
