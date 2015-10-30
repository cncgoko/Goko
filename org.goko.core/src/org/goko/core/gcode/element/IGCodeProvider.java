package org.goko.core.gcode.element;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;


public interface IGCodeProvider extends IIdBean{
	
	String getCode();
	
	/**
	 * Returns all the lines in this provider 
	 * @return the list of GCodeLine
	 */
	List<GCodeLine> getLines() throws GkException;
	
	/**
	 * Returns the line with the given id 
	 * @param idLine the id of the line
	 * @return GCodeLine
	 */
	GCodeLine getLine(Integer idLine) throws GkException;	
	
	/**
	 * Returns the line at the given position 
	 * @param indexLine the index of the line
	 * @return GCodeLine the GCodeLine
	 */
	GCodeLine getLineAtIndex(Integer indexLine) throws GkException;	
	
}
