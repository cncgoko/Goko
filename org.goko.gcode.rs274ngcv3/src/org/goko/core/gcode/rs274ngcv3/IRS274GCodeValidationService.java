/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.service.IGCodeValidationService;

/**
 * @author Psyko
 * @date 8 juil. 2017
 */
public interface IRS274GCodeValidationService extends IGCodeValidationService<AbstractInstruction, GCodeContext, InstructionSet> {

	/**
	 * @return the arcToleranceCheckEnabled
	 */
	boolean isArcToleranceCheckEnabled();
	
	/**
	 * @param arcToleranceCheckEnabled the arcToleranceCheckEnabled to set
	 */
	void setArcToleranceCheckEnabled(boolean arcToleranceCheckEnabled);

	/**
	 * @return the arcTolerance
	 */
	Length getArcTolerance();

	/**
	 * @param arcTolerance the arcTolerance to set
	 */
	void setArcTolerance(Length arcTolerance);
}
