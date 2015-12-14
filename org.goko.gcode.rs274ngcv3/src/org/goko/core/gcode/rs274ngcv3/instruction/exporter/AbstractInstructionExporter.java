package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.IInstructionExporter;

public abstract class AbstractInstructionExporter<T extends AbstractInstruction> implements IInstructionExporter{
	/** The type of instruction to match*/
	private InstructionType type;
	
	
	public AbstractInstructionExporter(InstructionType type) {
		super();
		this.type = type;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionExporter#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<AbstractInstruction> instructions) throws GkException {
		if(CollectionUtils.isNotEmpty(instructions)){
			for (IInstruction iInstruction : instructions) {
				if(iInstruction.getType() == type){
					return true;
				}
			}
		}
		return false;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionExporter#toWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public AbstractInstruction toWords(GCodeContext context, List<AbstractInstruction> instructions, List<GCodeWord> targetList) throws GkException{		
		T instruction = getAndRemoveInstruction(instructions);
		targetList.addAll(getWords(context, instruction));
		return instruction;
	}
	
	protected abstract List<GCodeWord> getWords(GCodeContext context, T instruction) throws GkException;
	
	@SuppressWarnings("unchecked")
	protected T getAndRemoveInstruction(List<AbstractInstruction> instructions) throws GkTechnicalException{
		T result = null;
		if(CollectionUtils.isNotEmpty(instructions)){
			for (IInstruction iInstruction : instructions) {
				if(iInstruction.getType() == type){
					result = (T) iInstruction;
					break;
				}
			}
		}
		if(result == null){
			throw new GkTechnicalException("Cannot find an instruction for type ["+type+"]");			
		}
		instructions.remove(result);
		return result;
		
	}
		
	protected List<GCodeWord> wrap(GCodeWord... words){		
		List<GCodeWord> lst = new ArrayList<GCodeWord>();
		for (GCodeWord word : words) {
			lst.add(word);	
		}		
		return lst;
	}
}
