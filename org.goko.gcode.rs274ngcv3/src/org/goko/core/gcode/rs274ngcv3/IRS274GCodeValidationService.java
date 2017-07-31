/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3;

import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.service.IGCodeValidationService;

/**
 * @author Psyko
 * @date 8 juil. 2017
 */
public interface IRS274GCodeValidationService extends IGCodeValidationService<AbstractInstruction, GCodeContext, InstructionSet> {

}
