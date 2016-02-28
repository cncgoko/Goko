package org.goko.tools.autoleveler.modifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightTraverseInstruction;
import org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier;
import org.goko.core.math.Tuple6b;
import org.goko.tools.autoleveler.bean.IHeightMap;

public abstract class AutoLevelerModifier<T extends IHeightMap> extends AbstractModifier<GCodeProvider> implements IModifier<GCodeProvider>{
	/** The offset map builder */
	private T heightMap;
	/** The theoric height to compare with the probed height */
	private Length theoricHeight;

	public AutoLevelerModifier() {
		super("Auto leveler");
		this.theoricHeight = Length.ZERO;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#isConfigured()
	 */
	@Override
	public boolean isConfigured() {		
		return heightMap != null && heightMap.isProbed();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier#applyModifier(org.goko.core.gcode.element.IGCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	protected void applyModifier(IGCodeProvider source, GCodeProvider target) throws GkException {
		if(heightMap == null){
			return;//throw new GkTechnicalException("No valid height map");
		}
		GCodeContext localContext = new GCodeContext();
		InstructionProvider sourceInstructionSet = getRS274NGCService().getInstructions(localContext, source);
		InstructionProvider resultInstructionProvider = new InstructionProvider();

		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = getRS274NGCService().getIterator(sourceInstructionSet, localContext);


		while(iterator.hasNext()){
			localContext = new GCodeContext(iterator.getContext()); // Get the context before applying the command
			AbstractInstruction instr = iterator.next();
			

			if(instr.getType() == InstructionType.STRAIGHT_FEED || instr.getType() == InstructionType.STRAIGHT_TRAVERSE){
				resultInstructionProvider.addInstructionSet(applyModifier(localContext, (AbstractStraightInstruction) instr));

//			}else if(instr.getType() == InstructionType.ARC_FEED){
//				throw new GkTechnicalException("ARC_FEED command are not supported in auto leveler modifier");

			}else{
				// Other non modified instruction
				InstructionSet resultInstructionSet = new InstructionSet();
				resultInstructionSet.addInstruction(instr);
				resultInstructionProvider.addInstructionSet(resultInstructionSet);
			}
			
		}
		GCodeProvider result = getRS274NGCService().getGCodeProvider(new GCodeContext(), resultInstructionProvider);
		for (GCodeLine line : result.getLines()) {
			target.addLine(line);			
		}
	}

	/**
	 * Apply the modifier to a straight instruction
	 * @param source the source instruction
	 * @return an AbstractStraightInstruction
	 * @throws GkException GkException
	 */
	private List<InstructionSet> applyModifier(GCodeContext localContext, AbstractStraightInstruction source) throws GkException{
		Tuple6b start = new Tuple6b(Units.MILLIMETRE, localContext.getX(), localContext.getY(), localContext.getZ() );
		Tuple6b end   = new Tuple6b(Units.MILLIMETRE, source.getX(), source.getY(), source.getZ() );

		List<InstructionSet> sets = new ArrayList<InstructionSet>();
		List<Tuple6b> lstPoints = heightMap.splitSegment(start, end);
		int nbPoints = lstPoints.size();

		for(int i = 0;i < nbPoints - 1; i++){
			Tuple6b target = lstPoints.get(i+1);
			InstructionSet instructionSet = new InstructionSet();
			Length probedHeight = heightMap.getHeight(target.getX(), target.getY());
			if(probedHeight == null){
				probedHeight = Length.ZERO;
			}
			// Let's compute the theorical height in the given segment
			BigDecimal factor = BigDecimal.ONE;
			if(!start.getX().equals(end.getX())){
				factor = ( target.getX().subtract(start.getX()) ).divide(end.getX().subtract(start.getX()));
			}else if(!start.getY().equals(end.getY())){
				factor = ( target.getY().subtract(start.getY()) ).divide(end.getY().subtract(start.getY()));
			}
			Length interpolatedHeight = start.getZ().add(end.getZ().subtract(start.getZ()).multiply( factor ));

			// Now we can apply the computed offset to the interpolated height
			Length fixedHeight 	= interpolatedHeight.subtract( theoricHeight.subtract(probedHeight));

			if( source.getType() == InstructionType.STRAIGHT_TRAVERSE ){
				instructionSet.addInstruction(new StraightTraverseInstruction(target.getX(), target.getY(), fixedHeight, source.getA(), source.getB(), source.getC()));
			}else if(source.getType() == InstructionType.STRAIGHT_FEED){
				instructionSet.addInstruction(new StraightFeedInstruction(target.getX(), target.getY(), fixedHeight, source.getA(), source.getB(), source.getC()));
			}
			sets.add(instructionSet);
		}
		return sets;
	}

	/**
	 * @return the theoricHeight
	 */
	public Length getTheoricHeight() {
		return theoricHeight;
	}

	/**
	 * @param theoricHeight the theoricHeight to set
	 */
	public void setTheoricHeight(Length theoricHeight) {
		this.theoricHeight = theoricHeight;
	}

	/**
	 * @return the heightMap
	 */
	public T getHeightMap() {
		return heightMap;
	}

	/**
	 * @param heightMap the heightMap to set
	 */
	public void setHeightMap(T heightMap) {
		this.heightMap = heightMap;
	}


}
