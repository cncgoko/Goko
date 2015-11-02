package org.goko.core.gcode.service;

import java.io.InputStream;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeContext;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.element.IInstructionSet;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.element.IModifier;

public interface IGCodeService<I extends IInstruction, T extends IGCodeContext, S extends IInstructionSet<I>> extends IGokoService{

	List<IGCodeProvider> getGCodeProvider() throws GkException;
	
	IGCodeProvider getGCodeProvider(Integer id) throws GkException;
	
	void deleteGCodeProvider(Integer id) throws GkException;
		
	IGCodeProvider parse(InputStream inputStream) throws GkException;	
	
	IInstructionProvider<I, S> getInstructions(T context, IGCodeProvider gcodeProvider) throws GkException;
	
	T update(T baseContext, I instruction) throws GkException;
	
	T update(T baseContext, S instructionSet) throws GkException;
	
	IInstructionSetIterator<T, I> getIterator(IInstructionProvider<I, S> instructionProvider, T baseContext) throws GkException;
	
	String render(GCodeLine line) throws GkException;
	
	void addModifier(IModifier<IGCodeProvider, T> modifier) throws GkException;
}
