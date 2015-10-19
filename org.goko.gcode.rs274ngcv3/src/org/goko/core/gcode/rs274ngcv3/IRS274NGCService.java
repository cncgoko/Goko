package org.goko.core.gcode.rs274ngcv3;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.service.IGCodeService;

public interface IRS274NGCService extends IGCodeService<GCodeContext> {

	IGCodeProvider parse(InputStream inputStream) throws GkException;
	
	IGCodeProvider parse(String inputString) throws GkException;
	
	GCodeLine parseLine(String inputString) throws GkException;
	
	String toString(GCodeLine line) throws GkException;
	
	IInstructionProvider getInstructions(GCodeContext context, IGCodeProvider gcodeProvider) throws GkException;
}
