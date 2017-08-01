package org.goko.core.gcode.service;

import org.eclipse.core.runtime.IProgressMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeContext;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IGCodeProviderSource;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.element.IInstructionSet;
import org.goko.core.gcode.element.IInstructionSetIterator;

public interface IGCodeService<I extends IInstruction, T extends IGCodeContext, S extends IInstructionSet<I>> extends IGokoService{

	GCodeLine parseLine(String inputString) throws GkException;

	IGCodeProvider parse(IGCodeProviderSource source, IProgressMonitor monitor) throws GkException;

	IInstructionProvider<I, S> getInstructions(T context, IGCodeProvider gcodeProvider) throws GkException;
	
	T update(T baseContext, I instruction) throws GkException;

	T update(T baseContext, S instructionSet) throws GkException;

	T update(T baseContext, IInstructionProvider<I, S> instructionProvider) throws GkException;

	IInstructionSetIterator<T, I> getIterator(IInstructionProvider<I, S> instructionProvider, T baseContext) throws GkException;

	String render(GCodeLine line) throws GkException;
	
}
