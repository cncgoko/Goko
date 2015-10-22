package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
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
	
	protected List<GCodeWord> wrap(GCodeWord word){
		List<GCodeWord> lst = new ArrayList<GCodeWord>();
		lst.add(word);
		return lst;
	}
	
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
	 * @return BigDecimalQuantity<Length>
	 * @throws GkException GkException
	 */
	public <Q extends Quantity<Q>> BigDecimalQuantity<Q> findWordValue(String letter, List<GCodeWord> words, BigDecimalQuantity<Q> defaultValue, Unit<Q> unit) throws GkException{
		BigDecimalQuantity<Q> result = defaultValue;
		GCodeWord word = GCodeWordUtils.findAndRemoveWordByLetter(letter, words);
		
		if( word != null ){
			result = NumberQuantity.of(new BigDecimal(word.getValue()), unit);
		}
		return result;
	}

}
