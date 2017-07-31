/**
 * 
 */
package org.goko.core.gcode.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeContext;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.element.IInstructionSet;
import org.goko.core.gcode.element.validation.ValidationResult;

/**
 * @author Psyko
 * @date 2 juil. 2017
 */
public interface IGCodeValidationService<I extends IInstruction, T extends IGCodeContext, S extends IInstructionSet<I>> extends IGokoService{
	
	ValidationResult validate(Integer idGcodeProvider) throws GkException;
	
	ValidationResult getValidationResult(Integer idGCodeProvider);
	
}
