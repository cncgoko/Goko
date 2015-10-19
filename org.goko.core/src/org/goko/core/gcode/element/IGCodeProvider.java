package org.goko.core.gcode.element;

import java.util.List;


public interface IGCodeProvider {
	/** 
	 * Sets the internal identifier of this provider  
	 * @param id the internal identifier
	 */
	void setId(Integer id);
	
	/**
	 * Returns the internal identifier of this provider
	 * @return the internal identifier of this provider
	 */
	Integer getId();
	
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
