package org.goko.core.gcode.rs274ngcv3.element;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;

public class InstructionIterator implements IInstructionSetIterator<GCodeContext, AbstractInstruction>{
	/** The context */
	private GCodeContext context;
	/** The used instruction provider */
	private IInstructionProvider<AbstractInstruction, InstructionSet> provider; 
	/** Current iterator index */
	private int providerCurrentIndex;
	/** Current iterator index */
	private int instructionSetCurrentIndex;
	/** GCode service */
	private IRS274NGCService service;
	
	public InstructionIterator(IInstructionProvider<AbstractInstruction, InstructionSet> provider, GCodeContext context, IRS274NGCService service){
		this.provider = provider;		
		this.service = service;
		this.providerCurrentIndex = 0;
		this.instructionSetCurrentIndex = 0;
		this.initialize(context);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionSetIterator#initialize(org.goko.core.gcode.element.IGCodeContext)
	 */
	@Override
	public void initialize(GCodeContext context) {
		this.context = context;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionSetIterator#hasNext()
	 */
	@Override
	public boolean hasNext() {		
		return internalHasNext(providerCurrentIndex, instructionSetCurrentIndex + 1);
	}

	/**
	 * Internal hasNext implementation 
	 * @param providerCurrentIndex the input index in the provider list 
	 * @param instructionSetCurrentIndex the input index in the instruction set
	 * @return boolean
	 */
	private boolean internalHasNext(int providerCurrentIndex, int instructionSetCurrentIndex) {
		if(providerCurrentIndex < provider.size()){
			if(instructionSetCurrentIndex < provider.get(providerCurrentIndex).size()){
				return true;
			}else{
				return internalHasNext(providerCurrentIndex + 1, 0);
			}
		}
		return false;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionSetIterator#next()
	 */
	@Override
	public AbstractInstruction next() throws GkException {
		AbstractInstruction instruction = null; 
		instructionSetCurrentIndex = instructionSetCurrentIndex + 1;
		
		if(providerCurrentIndex < provider.size()){
			if(instructionSetCurrentIndex > provider.get(providerCurrentIndex).size()){				
				providerCurrentIndex = providerCurrentIndex + 1;
				instructionSetCurrentIndex = 0;
			}
		}
		if(providerCurrentIndex < provider.size()){
			instruction = provider.get(providerCurrentIndex).get(instructionSetCurrentIndex);		
			context = service.update(context, instruction);
		}
		return instruction;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionSetIterator#getContext()
	 */
	@Override
	public GCodeContext getContext() {
		return context;
	}

}
