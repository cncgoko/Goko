package org.goko.core.gcode.element;

import java.util.List;

import org.goko.core.common.utils.IIdBean;


public interface IGCodeProvider extends IIdBean{
		
	/**
	 * Returns all the lines in this provider 
	 * @return the list of GCodeLine
	 */
	List<GCodeLine> getLines();
	
	/**
	 * Returns the line with the given id 
	 * @param idLine the id of the line
	 * @return GCodeLine
	 */
	GCodeLine getLine(Integer idLine);
}
