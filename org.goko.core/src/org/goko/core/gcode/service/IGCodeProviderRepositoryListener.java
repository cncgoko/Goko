/**
 * 
 */
package org.goko.core.gcode.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;

/**
 * @author PsyKo
 * @date 6 déc. 2015
 */
public interface IGCodeProviderRepositoryListener {

	void onGCodeProviderCreate(IGCodeProvider provider) throws GkException;
	
	void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException;
	
	void onGCodeProviderDelete(IGCodeProvider provider) throws GkException;
	
	void onGCodeProviderLocked(IGCodeProvider provider) throws GkException;
	
	void onGCodeProviderUnlocked(IGCodeProvider provider) throws GkException;
}
