package org.goko.core.gcode.element;

import org.goko.core.common.exception.GkException;

public interface IInstructionSetIterator<G extends IGCodeContext, T extends IInstruction> {

	void initialize(G context);
	
	boolean hasNext();
	
	T next() throws GkException;
	
	G getContext();
}
