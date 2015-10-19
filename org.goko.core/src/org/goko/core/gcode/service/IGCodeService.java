package org.goko.core.gcode.service;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeContext;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.element.IInstructionSet;

public interface IGCodeService<T extends IGCodeContext> {

	IGCodeProvider parse(InputStream inputStream) throws GkException;	
	
	IInstructionProvider getInstructions(T context, IGCodeProvider gcodeProvider) throws GkException;
	
	T update(T baseContext, IInstruction instruction) throws GkException;
	
	T update(T baseContext, IInstructionSet instructionSet) throws GkException;
}
