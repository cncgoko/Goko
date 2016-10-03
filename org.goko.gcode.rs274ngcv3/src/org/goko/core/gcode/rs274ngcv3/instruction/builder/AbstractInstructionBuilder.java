package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.math.BigDecimal;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public abstract class AbstractInstructionBuilder<I extends AbstractInstruction> implements IInstructionBuilder<I> {
	/** The type of the instruction */
	private InstructionType type;
	
	/**
	 * Constructor
	 * @param type the instruction type
	 */
	public AbstractInstructionBuilder(InstructionType type) {
		this.type = type;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#getInstructionType()
	 */
	@Override
	public InstructionType getInstructionType() {		
		return type; 
	}

	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public final I toInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		I instruction = getInstruction(context, words);
		GCodeWord word = GCodeWordUtils.findWordByLetter("N", words);
		if(word != null){
			instruction.setIdGCodeLine( Integer.valueOf(word.getValue()));
		}
		return instruction;
	}
		
	protected abstract I getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException;

	/**
	 * Extract the length value of the word designated by the given letter in the list of word
	 * @param letter the letter of the word
	 * @param words the available words
	 * @param context the gcode context
	 * @param defaultValue the default value
	 * @return Length
	 * @throws GkException GkException
	 */
	public Length findWordLength(String letter, List<GCodeWord> words, Length defaultValue, Unit<Length> unit) throws GkException{
		Length result = defaultValue;
		GCodeWord word = GCodeWordUtils.findAndRemoveWordByLetter(letter, words);
		
		if( word != null ){
			result = Length.valueOf(new BigDecimal(word.getValue()), unit);
		}
		return result;
	}
	
	/**
	 * Extract the angle value of the word designated by the given letter in the list of word
	 * @param letter the letter of the word
	 * @param words the available words
	 * @param context the gcode context
	 * @param defaultValue the default value
	 * @return Length
	 * @throws GkException GkException
	 */
	public Angle findWordAngle(String letter, List<GCodeWord> words, Angle defaultValue, Unit<Angle> unit) throws GkException{
		Angle result = defaultValue;
		GCodeWord word = GCodeWordUtils.findAndRemoveWordByLetter(letter, words);
		
		if( word != null ){
			result = Angle.valueOf(new BigDecimal(word.getValue()), unit);
		}
		return result;
	}

}
