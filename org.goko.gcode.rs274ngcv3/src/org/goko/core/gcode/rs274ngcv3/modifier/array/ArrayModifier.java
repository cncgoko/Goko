/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.modifier.array;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
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
import org.goko.core.math.Tuple6b;

/**
 * Array modifier 
 * 
 * @author Psyko
 * @date 14 sept. 2016
 */
public class ArrayModifier extends AbstractModifier<GCodeProvider> implements IModifier<GCodeProvider> {
	/** The offset between each copy */
	private Tuple6b offset;
	/** The copy count */
	private int count;
	
	/**
	 * Constructor 
	 */
	public ArrayModifier() {
		super("Array");
		this.count = 1;
		this.offset = new Tuple6b().setZero();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#isConfigured()
	 */
	@Override
	public boolean isConfigured() {		
		return offset != null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier#applyModifier(org.goko.core.gcode.element.IGCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	protected void applyModifier(IGCodeProvider source, GCodeProvider target) throws GkException {
		GCodeContext localContext = new GCodeContext();
		InstructionProvider sourceInstructionSet = getRS274NGCService().getInstructions(localContext, source);
		List<InstructionProvider> arrayInstructionSet = new ArrayList<InstructionProvider>();
		List<Tuple6b> lstOffsets = new ArrayList<Tuple6b>();
		// initialize the target instruction providers 
		for (int i = 0; i < count; i++) {
			arrayInstructionSet.add(new InstructionProvider());
			lstOffsets.add(offset.scale(1 + i));
		}
		
		// Let's cache offset so they are not computed each time
		
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = getRS274NGCService().getIterator(sourceInstructionSet, localContext);
		while(iterator.hasNext()){
			GCodeContext preContext = iterator.getContext();
			AbstractInstruction instr = iterator.next();
			
			for (int i = 0; i < count; i++) {
				AbstractInstruction clonedInstr = instr.clone();
				
				// We only translate in relative distance
				if(preContext.getDistanceMode() == EnumDistanceMode.ABSOLUTE){
					Tuple6b localOffset = lstOffsets.get(i);
					if(instr.getType() == InstructionType.STRAIGHT_FEED
						|| instr.getType() == InstructionType.STRAIGHT_TRAVERSE){
						translateStraightMotion((AbstractStraightInstruction) clonedInstr, localOffset, preContext);
						
					}else if(instr.getType() == InstructionType.ARC_FEED){
						translateArcFeed((ArcFeedInstruction)clonedInstr, localOffset, preContext);
					}
				}
				
				arrayInstructionSet.get(i).addInstruction(clonedInstr);
			}			
		}
		// Now let's merge the several providers
		for (int i = 0; i < count; i++) {
			sourceInstructionSet.addInstructionSet(arrayInstructionSet.get(i).getInstructionSets());
		}
		
		GCodeProvider result = getRS274NGCService().getGCodeProvider(localContext, sourceInstructionSet);
		for (GCodeLine line : result.getLines()) {
			target.addLine(line);
		}		
	}
	
	private void translateStraightMotion(AbstractStraightInstruction straightInstruction, Tuple6b offset, GCodeContext preContext) throws GkException {
		if(straightInstruction.getX() != null){
			straightInstruction.setX(straightInstruction.getX().add(offset.getX()));
		}
		if(straightInstruction.getY() != null){
			straightInstruction.setY(straightInstruction.getY().add(offset.getY()));
		}
		if(straightInstruction.getZ() != null){
			straightInstruction.setZ(straightInstruction.getZ().add(offset.getZ()));
		}
		if(straightInstruction.getA() != null){
			straightInstruction.setA(straightInstruction.getA().add(offset.getA()));
		}
		if(straightInstruction.getB() != null){
			straightInstruction.setB(straightInstruction.getB().add(offset.getB()));
		}
		if(straightInstruction.getC() != null){
			straightInstruction.setC(straightInstruction.getC().add(offset.getC()));
		}
	}
	/**
	 * Translation of an arc feed instruction
	 * @param instr the instruction
	 * @param preContext the context in which the instruction is evaluated
	 * @throws GkException GkException
	 */
	private void translateArcFeed(ArcFeedInstruction instr, Tuple6b offset, GCodeContext preContext) throws GkException {
		if(instr.getX() != null){
			instr.setX(instr.getX().add(offset.getX()));
		}
		if(instr.getY() != null){
			instr.setY(instr.getY().add(offset.getY()));
		}
		if(instr.getZ() != null){
			instr.setZ(instr.getZ().add(offset.getZ()));
		}
		if(instr.getA() != null){
			instr.setA(instr.getA().add(offset.getA()));
		}
		if(instr.getB() != null){
			instr.setB(instr.getB().add(offset.getB()));
		}
		if(instr.getC() != null){
			instr.setC(instr.getC().add(offset.getC()));
		}
	}

	/**
	 * @return the offset
	 */
	public Tuple6b getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Tuple6b offset) {
		this.offset = offset;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
}
