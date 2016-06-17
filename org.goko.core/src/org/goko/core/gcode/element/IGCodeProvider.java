package org.goko.core.gcode.element;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.ICodeBean;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.gcode.element.validation.IValidationTarget;


public interface IGCodeProvider extends IIdBean, ICodeBean, IValidationTarget{

	/**
	 * Getter for the user displayed code of this provider  
	 * @return code the displayed code of this provider
	 */
	String getCode();
	
	/**
	 * Setter for the user displayed code of this provider  
	 * @param code the displayed code of this provider
	 */
	void setCode(String code);
	
	/**
	 * Returns all the lines in this provider 
	 * @return the list of GCodeLine
	 */
	List<GCodeLine> getLines() throws GkException;
	
	/**
	 * Returns the count of lines in this provider 
	 * @return an integer
	 */
	int getLinesCount() throws GkException;
	
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
	
	/**
	 * Determines if this provider is locked
	 * @return <code>true</code> if it's locked, <code>false</code> otherwise
	 */
	boolean isLocked();
	
	/**
	 * Sets the locked state of this provider 
	 * @param locked <code>true</code> if it's locked, <code>false</code> otherwise
	 */
	void setLocked(boolean locked);
	
	/**
	 * Return the source of this provider
	 * @return IGCodeProviderSource
	 */
	IGCodeProviderSource getSource();
}
