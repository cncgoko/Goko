package org.goko.core.gcode.rs274ngcv3;

import java.io.InputStream;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.execution.IGCodeExecutionTimeService;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.math.BoundingTuple6b;

public interface IRS274NGCService extends IGCodeService<AbstractInstruction, GCodeContext, InstructionSet>,
										  IGCodeExecutionTimeService<GCodeContext>{

	IGCodeProvider parse(InputStream inputStream) throws GkException;
	
	IGCodeProvider parse(String inputString) throws GkException;
	
	GCodeLine parseLine(String inputString) throws GkException;
		
	InstructionProvider getInstructions(GCodeContext context, IGCodeProvider gcodeProvider) throws GkException;
	
	GCodeProvider getGCodeProvider(GCodeContext context, InstructionProvider instructionProvider) throws GkException;
	
	BoundingTuple6b getBounds(GCodeContext context, InstructionProvider instructionProvider) throws GkException;
	
	IModifier<GCodeProvider> getModifier(Integer id) throws GkException;

	void addModifier(IModifier<GCodeProvider> modifier) throws GkException;
	
	void updateModifier(IModifier<GCodeProvider> modifier) throws GkException;
	
	void deleteModifier(IModifier<GCodeProvider> modifier) throws GkException;
	
	void deleteModifier(Integer idModifier) throws GkException;
	
	List<IModifier<GCodeProvider>> getModifier(List<Integer> lstId) throws GkException;
	
	List<IModifier<GCodeProvider>> getModifierByGCodeProvider(Integer idGcodeProvider) throws GkException;
}
