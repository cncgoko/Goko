/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.modifier;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 18 janv. 2016
 */
public interface IModifierListener {

	void onModifierCreate(Integer idModifier) throws GkException;
	
	void onModifierUpdate(Integer idModifier) throws GkException;
	
	void onModifierDelete(Integer idModifier) throws GkException;
}
