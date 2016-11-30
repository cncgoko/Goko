/**
 * 
 */
package org.goko.tools.macro.service;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.tools.macro.bean.GCodeMacro;

/**
 * @author Psyko
 * @date 15 oct. 2016
 */
public interface IGCodeMacroService extends IGCodeProviderRepository{
	
	/**
	 * Registers the given macro
	 * @param gcodeMacro the macro to register
	 * @throws GkException GkException
	 */
	void addGCodeMacro(GCodeMacro gcodeMacro) throws GkException;
	
	/**
	 * Returns the macro for the given code
	 * @param code the code of the requested macro
	 * @return IGCodeMacro
	 * @throws GkException GkException
	 */
	GCodeMacro getGCodeMacro(String code) throws GkException;
	
	/**
	 * Returns the macro for the given code
	 * @param code the code of the requested macro
	 * @return IGCodeMacro or <code>null</code> iof it doesn't exist
	 * @throws GkException GkException
	 */
	GCodeMacro findGCodeMacro(String code) throws GkException;
	
	/**
	 * Returns the macro for the given id
	 * @param id the id of the requested macro
	 * @return IGCodeMacro
	 * @throws GkException GkException
	 */
	GCodeMacro getGCodeMacro(Integer id) throws GkException;
	
	/**
	 * Returns the macro for the given id
	 * @param id the id of the requested macro
	 * @return IGCodeMacro or <code>null</code> iof it doesn't exist
	 * @throws GkException GkException
	 */
	GCodeMacro findGCodeMacro(Integer id) throws GkException;
	
	/**
	 * Returns all the available macro
	 * @return IGCodeMacro
	 * @throws GkException GkException
	 */
	List<GCodeMacro> getGCodeMacro() throws GkException;
	
	/**
	 * Updates the given macro
	 * @param macro the macro to update
	 * @throws GkException GkException
	 */
	void updateGCodeMacro(GCodeMacro macro) throws GkException;
	
	/**
	 * Deletes the given macro
	 * @param macro the macro to delete
	 * @throws GkException GkException
	 */
	void deleteGCodeMacro(GCodeMacro macro) throws GkException;
	
	/**
	 * Deletes the macro for the given code
	 * @param code the code of the macro to delete
	 * @throws GkException GkException
	 */
	void deleteGCodeMacro(String code) throws GkException;
	
	/**
	 * Deletes the macro for the given id
	 * @param id the id of the macro to delete
	 * @throws GkException GkException
	 */
	void deleteGCodeMacro(Integer id) throws GkException;
	
	/**
	 * Returns the GCode provider for the given macro
	 * @param idMacro id of the macro
	 * @return IGCodeProvider
	 * @throws GkException GkException
	 */
	IGCodeProvider getGCodeProviderByMacro(Integer idMacro) throws GkException;
	
	/**
	 * Returns the GCode provider for the given macro
	 * @param idMacro id of the macro
	 * @return IGCodeProvider (the direct object, not a reference)
	 * @throws GkException GkException
	 */
	IGCodeProvider internalGetGCodeProviderByMacro(Integer idMacro) throws GkException;
	
	/**
	 * Registers the given listener 
	 * @param listener the IGCodeMacroServiceListener to add
	 * @throws GkException GkException
	 */
	void addListener(IGCodeMacroServiceListener listener) throws GkException;
	
	/**
	 * Removes the given listener 
	 * @param listener the IGCodeMacroServiceListener to remove
	 * @throws GkException GkException
	 */
	void removeListener(IGCodeMacroServiceListener listener) throws GkException;
}
