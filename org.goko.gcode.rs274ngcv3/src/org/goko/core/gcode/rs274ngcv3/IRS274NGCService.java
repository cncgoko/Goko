package org.goko.core.gcode.rs274ngcv3;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
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
import org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.math.BoundingTuple6b;

public interface IRS274NGCService extends IGCodeService<AbstractInstruction, GCodeContext, InstructionSet>,
										  IGCodeExecutionTimeService, IGCodeProviderRepository{

	IGCodeProvider parse(String inputString) throws GkException;

	void reload(Integer idGCodeProvider, IProgressMonitor monitor)throws GkException;
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#getInstructions(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	InstructionProvider getInstructions(GCodeContext context, IGCodeProvider gcodeProvider) throws GkException;

	GCodeProvider getGCodeProvider(GCodeContext context, InstructionProvider instructionProvider) throws GkException;

	BoundingTuple6b getBounds(GCodeContext context, InstructionProvider instructionProvider) throws GkException;

	IModifier<GCodeProvider> getModifier(Integer id) throws GkException;
	
	IModifier<GCodeProvider> findModifier(Integer id) throws GkException;

	void addModifier(IModifier<GCodeProvider> modifier) throws GkException;

	void updateModifier(IModifier<GCodeProvider> modifier) throws GkException;

	void deleteModifier(IModifier<GCodeProvider> modifier) throws GkException;

	void deleteModifier(Integer idModifier) throws GkException;

	void addModifierListener(IModifierListener listener);
	
	List<IModifier<GCodeProvider>> getModifier(List<Integer> lstId) throws GkException;

	List<IModifier<GCodeProvider>> getModifierByGCodeProvider(Integer idGcodeProvider) throws GkException;

	String render(GCodeLine line, RenderingFormat format) throws GkException;
}
