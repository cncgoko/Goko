/**
 * 
 */
package org.goko.tools.macro.service;

import org.goko.tools.macro.bean.GCodeMacro;

/**
 * @author Psyko
 * @date 19 oct. 2016
 */
public interface IGCodeMacroServiceListener {

	void onGCodeMacroCreate(GCodeMacro macro);
	
	void onGCodeMacroUpdate(GCodeMacro macro);
	
	void beforeGCodeMacroDelete(GCodeMacro macro);
	
	void afterGCodeMacroDelete(GCodeMacro macro);
}
