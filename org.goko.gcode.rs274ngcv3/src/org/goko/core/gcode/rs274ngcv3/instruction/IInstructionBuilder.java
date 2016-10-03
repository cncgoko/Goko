package org.goko.core.gcode.rs274ngcv3.instruction;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public interface IInstructionBuilder<I extends IInstruction> {

	boolean match(GCodeContext context, List<GCodeWord> words) throws GkException;
	
	/**
	 * Returns the first possible creatable instruction from the list of word.
	 * The instruction creation can consume one or more words. The instruction creation can use 
	 * only some of the provided words.
	 * The used words are always removed from the given list
	 * @param context the context in which the word will be evaluated to create the command
	 * @param words the list of words
	 * @return <I> instruction
	 * @throws GkException GkException
	 * @param <I> the template of the instruction to use
	 */  
	I toInstruction(GCodeContext context, List<GCodeWord> words) throws GkException;
	
	InstructionType getInstructionType();	
}
